package net.auscraft.BlivUtils.purchases;

import java.util.HashMap;
import java.util.Random;

import net.auscraft.BlivUtils.utils.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.minecraftdimensions.bungeesuitechat.managers.PlayerManager;
import com.minecraftdimensions.bungeesuitechat.objects.BSPlayer;

public class Nicknames
{
	
	private Utilities util;
	private HashMap<Player, Integer> nick;
	
	public Nicknames(Utilities inUtil)
	{
		util = inUtil;
		nick = new HashMap<Player, Integer>();
	}
	
	public void nickPlayer(Player p)
	{
		BSPlayer bsp = PlayerManager.getPlayer(p);
		if(bsp.hasNickname())
		{
			util.logInfo("Player " + p.getName() + " should have had a random colour assigned, but they already had a nickname.");
			return; //Exit. If player has a nickname, it is most likely coloured, or it is a rebuy of the package.
		}
		
		String newName = randomColour() + p.getName(); //Default minecraft name
		
		PlayerManager.nicknamePlayer(p.getName(), p.getName(), newName, true);
	}
	
	public ChatColor randomColour()
	{
		Random rand = new Random(System.currentTimeMillis());
		
		int randColour = rand.nextInt(13);
		ChatColor colour = ChatColor.GRAY; //&7 -- Default
		
		switch(randColour)
		{
			case 1:
				colour = ChatColor.AQUA; //&b
				break;
			case 2:
				colour = ChatColor.DARK_AQUA; //&9?
				break;
			case 3:
				colour = ChatColor.DARK_GRAY; //&8
				break;
			case 4:
				colour = ChatColor.DARK_GREEN; //&2
				break;
			case 5:
				colour = ChatColor.DARK_PURPLE; //&5
				break;
			case 6:
				colour = ChatColor.DARK_RED; //&4
				break;
			case 7:
				colour = ChatColor.GOLD; //&6
				break;
			case 8:
				colour = ChatColor.GRAY; //&7 (Currently default)
				break;
			case 9:
				colour = ChatColor.GREEN; //&a
				break;
			case 10:
				colour = ChatColor.LIGHT_PURPLE; //&d
				break;
			case 11:
				colour = ChatColor.RED; //&d
				break;
			case 12:
				colour = ChatColor.WHITE; //&f
				break;
			case 13:
				colour = ChatColor.YELLOW; //&e
				break;
		}
		
		util.logInfo("Colour selected for player: " + colour.toString());
		
		return colour;
	}
	
	public HashMap<Player, Integer> getNick()
	{
		return nick;
	}

}
