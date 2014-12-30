package net.auscraft.BlivUtils.managers;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.minecraftdimensions.bungeesuitechat.managers.PlayerManager;
import com.minecraftdimensions.bungeesuitechat.objects.BSPlayer;

public class AFKManager implements CommandExecutor
{
	
	private Utilities util;
	
	public AFKManager(BlivUtils instance)
	{
		util = instance.getUtil();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) 
	{
		if ((cmd.getName().equalsIgnoreCase("afk") && (sender instanceof Player)))
		{
			Essentials e = new Essentials();
			User user = e.getUser(sender.getName());
			
			BSPlayer bsp = PlayerManager.getPlayer((Player) sender);
		
			//This may cause two AFK notifications to show up in chat, I hope this isn't the case.
			//Double up on the AFKs, since Essentials will be the server-specific afk manager, and BSuite will be the global AFK setting.
			if(bsp.isAFK())
			{
				bsp.setAFK(false);
			}
			else
			{
				bsp.setAFK(true);
			}

			user.toggleAfk();
			
		}
		else if(cmd.getName().equalsIgnoreCase("modlist"))
		{
			//TODO: Move me to an automated scheduled ASync task, every 5 minutes or so to reduce lag.
			Collection<BSPlayer> onPlayers = PlayerManager.getOnlinePlayers();
			BSPlayer[] onlineAdmins = new BSPlayer[100];
			int num = 0;
			
			for(BSPlayer p : onPlayers)
			{
				PermissionUser user = PermissionsEx.getUser(p.getPlayer());
				if(user.has("bungeesuite.chat.channel.admin"))
				{
					onlineAdmins[num] = p;
					num++;
				}
			}
			
			if(onlineAdmins[0] != null)
			{
				num = 0;
				String bAdmin = "", Mod = "", Admin = "", Musketeer = "";
				
				while(onlineAdmins[num] != null)
				{
					PermissionUser userLoop = PermissionsEx.getUser(onlineAdmins[num].getPlayer());
					String AFK = "", pName = onlineAdmins[num].getName();
					if(userLoop.inGroup("BasicAdmin") || userLoop.inGroup("trialbAdmin"))
					{
						bAdmin = adminList(onlineAdmins[num]);
					}
					else if(userLoop.inGroup("Mod") || userLoop.inGroup("trialMod"))
					{
						 Mod = adminList(onlineAdmins[num]);
					}
					else if(userLoop.inGroup("Admin") || userLoop.inGroup("trialAdmin"))
					{
						 Admin = adminList(onlineAdmins[num]);
					}
					else if(userLoop.inGroup("Musketeer"))
					{
						 Musketeer = adminList(onlineAdmins[num]);
					}
					else
					{
						util.logError("Player not in any of the listed groups for /modlist");
					}
				}
				
				String onlineList = bAdmin + Mod + Admin + Musketeer;
				
				sender.sendMessage("Online Admins:\n" + onlineList);
				
			}
			
			
		}
		
		return false;
	}
	
	private String adminList(BSPlayer player)
	{
		String AFK = "", pName = player.getName(), result = "";
		if(player.isAFK())
		{
			AFK = ChatColor.GRAY + "[AFK]";
		}
		if(player.hasNickname())
		{
			pName = player.getNickname();
		}
		result += " " + AFK + pName + ",";
		
		return result;
	}
	
}
