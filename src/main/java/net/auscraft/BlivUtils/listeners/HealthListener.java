package net.auscraft.BlivUtils.listeners;

import net.auscraft.BlivUtils.utils.BUtil;

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
	
	public HealthListener()
	{
		
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
		if(p.hasPermission("blivutils.ender.hearts.off"))
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
		else
		{
			p.setMaxHealth(20.0);
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
				Player player = (Player) sender;
				if(args[0].equals("default"))
				{
					player.setMaxHealth(20.0);
					BUtil.printInfo(sender, "Your health bonus has been temporarily reset");
				}
				else if(args[0].equals("reset"))
				{
					if(player.hasPermission("blivutils.ender.hearts.double"))
					{
						player.setMaxHealth(40.0);
						BUtil.printInfo(sender, "Your health bonus has returned!");
					}
					else if(player.hasPermission("blivutils.ender.hearts.oneandahalf"))
					{
						player.setMaxHealth(30.0);
						BUtil.printInfo(sender, "Your health bonus has returned!");
					}
					
				}
				else if(args[0].equals("toggle"))
				{
					PermissionUser user = PermissionsEx.getUser(player);
					if(sender.hasPermission("blivutils.ender.hearts.off"))
					{
						user.removePermission("blivutils.ender.hearts.off");
						if(player.hasPermission("blivutils.ender.hearts.double"))
						{
							player.setMaxHealth(40.0);
							BUtil.printInfo(sender, "Your health bonus has returned!");
						}
						else if(player.hasPermission("blivutils.ender.hearts.oneandahalf"))
						{
							player.setMaxHealth(30.0);
							BUtil.printInfo(sender, "Your health bonus has returned!");
						}
					}
					else
					{
						user.addPermission("blivutils.ender.hearts.off");
						player.setMaxHealth(20.0);
						BUtil.printInfo(sender, "Your health bonus has been turned off");
					}
				}
			}
		}
		else
		{
			BUtil.printError(sender, "You dont have access to bonus health!\n     (This is an Ender Perk)");
		}
		return true;
	}
}
