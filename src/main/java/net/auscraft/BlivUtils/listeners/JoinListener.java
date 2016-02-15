package net.auscraft.BlivUtils.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.Arrays;
import java.util.List;

public class JoinListener implements Listener
{

	private static final String ENDER_PREFIX_ENDERMAN = "§5§lEnderman §d§l»",
								ENDER_PREFIX_ENDERDRAGON = "§4§lEnderDragon §c§l»",
								ENDER_PREFIX_WITHER = "§8§lWither §7§l»";
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();

		if(event.getPlayer().hasPlayedBefore()) //Process this here, since a player cannot be a paid rank with an old prefix and a fresh joiner
		{
			if(player.hasPermission("blivutils.prefix.convert"))
			{
				PermissionUser user = PermissionsEx.getUser(player);

				List<PermissionGroup> groups = Arrays.asList(user.getGroups());
				boolean isValid = false;

				for(PermissionGroup group : groups)
				{
					if(group.getName().toLowerCase().equals("EnderRank"))
					{
						isValid = true;
						break;
					}
				}

				if(!isValid)
				{
					return;
				}

				//If the player already has a migrated prefix, ignore.
				if(     !user.getPrefix().equals(ENDER_PREFIX_ENDERMAN) &&
						        !user.getPrefix().equals(ENDER_PREFIX_ENDERDRAGON) &&
						        !user.getPrefix().equals(ENDER_PREFIX_WITHER))
				{
					switch(user.getOption("EnderRankValue"))
					{
						case "1": user.setPrefix(ENDER_PREFIX_ENDERMAN, null); break;
						case "2": user.setPrefix(ENDER_PREFIX_ENDERDRAGON, null); break;
						case "3": user.setPrefix(ENDER_PREFIX_WITHER, null); break;
						default:
							break;
					}
				}
			}
			return;
		}
			
		PermissionUser user = PermissionsEx.getUser(player);
		PermissionGroup[] groups = user.getGroups();
		for(PermissionGroup group : groups)
		{
			if(group.getName().equals("Squid"))
			{
				user.removeGroup("Squid");
				user.addGroup("Chicken");
			}
		}
		
		Bukkit.dispatchCommand(player, "kit tools");
	}

}
