package net.auscraft.BlivUtils.executors;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.BUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class GenericExecutor implements CommandExecutor
{
	
	private BlivUtils plugin;
	private final String serversHeader = ChatColor.GOLD + "- - - - - " + ChatColor.BLUE + "" + ChatColor.BOLD	+ "Aus"	+ ChatColor.WHITE + ChatColor.BOLD + "Craft" + ChatColor.YELLOW	+ " Servers Menu - - - - -\n";
	
	public GenericExecutor(BlivUtils instance)
	{
		plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) 
	{
		if (cmd.getName().equalsIgnoreCase("bu")) 
		{
			if (args.length == 0) 
			{
				BUtil.printInfo(sender, ChatColor.GOLD + "Use one of the following inputs:\n/bu version\n/bu reload");
			} 
			else if (args.length > 0)
			{
				if (args[0].equalsIgnoreCase("version"))
				{
					BUtil.printInfo(sender, ChatColor.GOLD + "Running BlivUtils Version " + ChatColor.WHITE + plugin.getDescription().getVersion());
				}
				if (args[0].equalsIgnoreCase("reload")) 
				{
					if (sender.hasPermission("blivutils.reload") || !(sender instanceof Player))
					{
						plugin.reloadConfig();
						BUtil.printInfo(sender, ChatColor.GOLD + "BlivUtils successfully reloaded.");
					}
					else
					{
						BUtil.printError(sender, "You don't have sufficient permissions!");
					}
				}
			}
		}
		if (cmd.getName().equalsIgnoreCase("say"))
		{
			if (sender.hasPermission("blivutils.say") || !(sender instanceof Player))
			{
				String message = "";
				if (args.length > 0) 
				{
					String as[] = args;
					int i = as.length;
					for (int j = 0; j < i; j++)
					{
						String data = as[j];
						message = (message += data + " ");
					}

					Bukkit.broadcastMessage(ChatColor.DARK_RED + "|| " + ChatColor.GRAY + "[" + ChatColor.GREEN	+ ChatColor.BOLD + "Server" + ChatColor.RESET
							+ ChatColor.GRAY + "]" + " " + ChatColor.YELLOW	+ ChatColor.ITALIC + message);
					return true;
				}
				
				return false;
			}
			
			BUtil.printError(sender, "You don't have sufficient permissions!");
			return false;
		}
		if (cmd.getName().equalsIgnoreCase("wstop")	&& !(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.GOLD + "Waiting 10 seconds before stopping the server.");
			Bukkit.broadcastMessage(ChatColor.DARK_RED + "|| "	+ ChatColor.GRAY + "[" + ChatColor.GOLD	+ ChatColor.BOLD + "Restart" + ChatColor.RESET
						+ ChatColor.GRAY + "]" + " " + ChatColor.YELLOW	+ ChatColor.ITALIC + "Server Restarting in 10 Seconds!");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "save-all");
				
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		    scheduler.scheduleSyncDelayedTask(plugin, new Runnable() 
		    {
		        @Override
		        public void run()
		        {
		        	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "kickall Restart");
		        	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ee say " + Bukkit.getServerName() + " is Restarting...");
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "stop");
		        }
		    }, 200L);
		    
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("servers"))
		{
			if(args.length == 0)
			{
				printServerMenu(sender);
			}
			else //More than 0 args.
			{
				if(args[0].equalsIgnoreCase("Hub"))
				{
					sender.sendMessage(serversHeader
									+ ChatColor.GOLD + "Hub " + ChatColor.WHITE + "is the spawning location for every player.\n"
									+ "   It contains portals to every available server, and acts as\n"
									+ "   limbo during server restarts and downtime.\n"
									+ "Connect: " + ChatColor.GOLD + "/hub" + ChatColor.WHITE + " OR " + ChatColor.GOLD + "/h");
					return true;
				}
				else if(args[0].equalsIgnoreCase("Survival"))
				{
					sender.sendMessage(serversHeader
									+ ChatColor.GOLD + "Survival " + ChatColor.WHITE + "is the main Survival world on AusCraft\n"
									+ "   Providing a challenging but forgiving experience, the Survival\n"
									+ "   server is equipped with many features to ease and enrich the vanilla experience.\n"
									+ "Connect: " + ChatColor.GOLD + "/survival" + ChatColor.WHITE + " OR " + ChatColor.GOLD + "/s");
					return true;
				}
				else if(args[0].equalsIgnoreCase("Creative"))
				{
					sender.sendMessage(serversHeader
									+ ChatColor.GOLD + "Creative " + ChatColor.WHITE + "is the creative portion of AusCraft.\n"
									+ "   Equipped with Freebuild and Large Protections.\n"
									+ "   " + ChatColor.RED + "WARNING: " + ChatColor.WHITE + "Must be " + ChatColor.GREEN + "Sheep Rank" + ChatColor.WHITE + " or Higher! (/rank sheep)\n"
									+ "Connect: " + ChatColor.GOLD + "/creative" + ChatColor.WHITE + " OR " + ChatColor.GOLD + "/c");
					return true;
				}
				else if(args[0].equalsIgnoreCase("Vanilla"))
				{
					sender.sendMessage(serversHeader
									+ ChatColor.GOLD + "Vanilla " + ChatColor.WHITE + "is a vanilla whitelist server.\n"
									+ "   Providing the vanilla experience without any of the\n"
									+ "   features of regular Survival, providing a tougher challenge.\n"
									+ "Connect: " + ChatColor.DARK_RED + "/van");
					return true;
				}
				else if(args[0].equalsIgnoreCase("Minigames"))
				{
					sender.sendMessage(serversHeader
									+ ChatColor.GOLD + "Minigames " + ChatColor.WHITE + "is a currently unreleased server.\n"
									+ "   Providing team based and solo games, Minigames\n"
									+ "   is an enjoyable break from the other servers.\n"
									+ "Connect: " + ChatColor.DARK_RED + "N/A" + ChatColor.WHITE + " OR " + ChatColor.DARK_RED + "N/A");
					return true;
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "That server doesn't exist!");
					printServerMenu(sender);
					return true;
				}
			}
			return true;
		}
		
		return false;
	}
	
	private void printServerMenu(CommandSender sender)
	{
		sender.sendMessage(serversHeader
							+ ChatColor.GRAY + "| " + ChatColor.WHITE + " - " + ChatColor.GREEN + "Hub\n"
							+ ChatColor.DARK_GRAY + "| " + ChatColor.WHITE + " - " + ChatColor.GREEN + "Survival\n"
							+ ChatColor.GRAY + "| " + ChatColor.WHITE + " - " + ChatColor.GREEN + "Creative\n"
							+ ChatColor.DARK_GRAY + "| " + ChatColor.WHITE + " - " + ChatColor.GREEN + "Vanilla\n"
							+ ChatColor.GRAY + "| " + ChatColor.WHITE + " - " + ChatColor.GREEN + "Minigames\n"
							+ ChatColor.GREEN + "Usage: /servers <server>");
	}
}
