package net.auscraft.BlivUtils.listeners;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class HealthListener implements Listener, CommandExecutor
{

private Utilities util;
	
	public HealthListener(BlivUtils instance)
	{
		util = instance.getUtil();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
		if(!(p.hasPermission("blivutils.ender.hearts.off")))
		{
			p.setMaxHealth(20.0);
		}
		else if(p.hasPermission("blivutils.ender.hearts.double"))
		{
			p.setMaxHealth(40.0);
		}
		else if(p.hasPermission("blivutils.ender.hearts.oneandahalf"))
		{
			p.setMaxHealth(30.0);
		}
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
	{
		if (cmd.getName().equalsIgnoreCase("health") && (sender.hasPermission("blivutils.ender.hearts")))
		{
			if(args.length == 0)
			{
				sender.sendMessage(ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- "
						+ ChatColor.BLUE + "" + ChatColor.BOLD	+ "Aus"	+ ChatColor.WHITE + ChatColor.BOLD + "Craft" + ChatColor.YELLOW	+ " Health Menu "
						+ ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- "
						+ ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- "
						+ ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- \n"
						+ ChatColor.GREEN + "| " + ChatColor.AQUA + "/health default" + ChatColor.WHITE + " - Reset your health bonus to default hearts\n" + ChatColor.DARK_GREEN + "| " + ChatColor.WHITE + "for the session\n"
						+ ChatColor.GREEN + "| " + ChatColor.AQUA + "/health reset" + ChatColor.WHITE + " - Increase your health bonus to your ranks\n" + ChatColor.DARK_GREEN + "| " + ChatColor.WHITE + " default\n"
						+ ChatColor.GREEN + "| " + ChatColor.AQUA + "/health toggle" + ChatColor.WHITE + " - Toggle your health bonus on/off\n"
						+ ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- "
						+ ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- "
						+ ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- "
						+ ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- "
						+ ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- "
						+ ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- ");
			}
			else if(args.length >= 1)
			{
				Player p = (Player)sender;
				if(args[0].equals("default"))
				{
					p.setMaxHealth(20.0);
					util.printInfo(sender, "Your health bonus has been temporarily reset");
				}
				else if(args[0].equals("reset"))
				{
					if(p.hasPermission("blivutils.ender.hearts.double"))
					{
						p.setMaxHealth(40.0);
						util.printInfo(sender, "Your health bonus has returned!");
					}
					else if(p.hasPermission("blivutils.ender.hearts.oneandahalf"))
					{
						p.setMaxHealth(30.0);
						util.printInfo(sender, "Your health bonus has returned!");
					}
					
				}
				else if(args[0].equals("toggle"))
				{
					PermissionUser user = PermissionsEx.getUser(p);
					if(sender.hasPermission("blivutils.ender.hearts.off"))
					{
						user.removePermission("blivutils.ender.hearts.off");
						if(p.hasPermission("blivutils.ender.hearts.double"))
						{
							p.setMaxHealth(40.0);
							util.printInfo(sender, "Your health bonus has returned!");
						}
						else if(p.hasPermission("blivutils.ender.hearts.oneandahalf"))
						{
							p.setMaxHealth(30.0);
							util.printInfo(sender, "Your health bonus has returned!");
						}
					}
					else
					{
						user.addPermission("blivutils.ender.hearts.off");
						p.setMaxHealth(20.0);
						util.printInfo(sender, "Your health bonus has been turned off");
					}
				}
			}
		}
		else
		{
			util.printError(sender, "You dont have access to bonus health!\n     (This is an Ender Perk)");
		}
		return true;
	}
}
