package net.auscraft.BlivUtils.util;

import net.auscraft.BlivUtils.BlivUtils;
import org.bukkit.Bukkit;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by OhBlihv (Chris) on 16/01/2016.
 * This file is part of a project created for BlivUtils
 */
public class PrefixFlatFile extends FlatFile
{

	/*
	 * Temporary class to aid in the conversion of old EnderRank prefixes to the
	 * new system, lacking brackets and forcing bold formatting
	 */

	private static PrefixFlatFile instance = null;
	public static PrefixFlatFile getInstance()
	{
		if(instance == null)
		{
			instance = new PrefixFlatFile();
		}
		return instance;
	}

	private static final ArrayDeque<UUID> convertedUUIDDeque = new ArrayDeque<>();

	private PrefixFlatFile()
	{
		super("prefix.yml");

		load();

		Bukkit.getScheduler().runTaskTimerAsynchronously(BlivUtils.getInstance(), this::save, 36000L, 36000L);
	}

	public void load()
	{
		convertedUUIDDeque.clear();

		BASE64Decoder base64Decoder = new BASE64Decoder();

		try
		{
			for(String uuidString : save.getStringList("prefix"))
			{
				convertedUUIDDeque.add(fromBytes(base64Decoder.decodeBuffer(uuidString.split(":")[0].concat("=="))));
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			BUtil.logError("Error loading UUIDs for ability disables.");
		}
	}

	public void save()
	{
		ArrayList<String> bountyStrings = new ArrayList<>(convertedUUIDDeque.size());

		BASE64Encoder base64Encoder = new BASE64Encoder();

		//Encode as Base64, and trim off the unnecessary trailing ='s (These will be added on load)
		for(UUID uuid : convertedUUIDDeque)
		{
			bountyStrings.add(base64Encoder.encode(toBytes(uuid)).split("=")[0]);
		}
		saveValue("prefix", bountyStrings);
	}

	/*
	 * Copied from UUIDUtils.class to avoid NoClassDefErrors which occur on Compilex' Spigot.
	 */

	public static byte[] toBytes(UUID uuid)
	{
		ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
		byteBuffer.putLong(uuid.getMostSignificantBits());
		byteBuffer.putLong(uuid.getLeastSignificantBits());
		return byteBuffer.array();
	}

	public static UUID fromBytes(byte[] array)
	{
		if (array.length != 16)
		{
			throw new IllegalArgumentException("Illegal byte array length: " + array.length);
		}

		ByteBuffer byteBuffer = ByteBuffer.wrap(array);
		long mostSignificant = byteBuffer.getLong();
		long leastSignificant = byteBuffer.getLong();

		return new UUID(mostSignificant, leastSignificant);
	}

}
