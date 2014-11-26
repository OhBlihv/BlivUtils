package net.auscraft.BlivUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GenericExecuter implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
    {
    	if(cmd.getName().equalsIgnoreCase("bu")) {
    		if(args.length == 0) {
        		sender.sendMessage(ChatColor.GOLD + "Use one of the following inputs:\n/bu version\n/bu reload");
        	}
        	else if(args.length > 0) {
        		if(args[0].equalsIgnoreCase("version")) {
        			
        			BlivUtils plugin = BlivUtils.getPlugin();
        			sender.sendMessage(ChatColor.GOLD + "Running BlivUtils Version " + plugin.getDescription().getVersion());
        		}
        		if(args[0].equalsIgnoreCase("reload")) {
        			if(sender.hasPermission("blivutils.reload") || !(sender instanceof Player)) {
        				BlivUtils plugin = BlivUtils.getPlugin();
        				plugin.reloadConfig();
        				sender.sendMessage(ChatColor.GOLD + "BlivUtils successfully reloaded\n(Although currently, this has nothing to reload)");
        			}
        		}
        	}
    	}
        if(cmd.getName().equalsIgnoreCase("say"))
            if(sender.hasPermission("blivutils.say") || !(sender instanceof Player)) {
                String message = "";
                if(args.length > 0)
                {
                    String as[] = args;
                    int i = as.length;
                    for(int j = 0; j < i; j++)
                    {
                        String data = as[j];
                        message = (message += data + " ");
                    }

                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "|| " + ChatColor.GRAY + "[" + ChatColor.GREEN + ChatColor.BOLD + "Server" + ChatColor.RESET + ChatColor.GRAY + "]" + " " + ChatColor.YELLOW + ChatColor.ITALIC + message);
                    return true;
                } else
                {
                    return false;
                }
            } 
            else {
                sender.sendMessage(ChatColor.DARK_RED + "You don't have sufficient permissions!");
                return false;
            }
        if(cmd.getName().equalsIgnoreCase("wstop") && !(sender instanceof Player))
        	try {
                sender.sendMessage(ChatColor.GOLD + "Waiting 10 seconds before stopping the server.");
                Bukkit.broadcastMessage(ChatColor.DARK_RED + "|| " + ChatColor.GRAY + "[" + ChatColor.GOLD + ChatColor.BOLD + "Restart" + ChatColor.RESET + ChatColor.GRAY + "]" + " " + ChatColor.YELLOW + ChatColor.ITALIC + "Server Restarting in 10 Seconds!");
                Thread.sleep(10000L);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "kickall Restart");
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "stop");
                return true;
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        if(cmd.getName().equalsIgnoreCase("sudo")) {
            if(sender.hasPermission("blivutils.sudo") || !(sender instanceof Player)) {
                sender.sendMessage(ChatColor.DARK_RED + "/sudo is disabled!");
                return true;
            } 
            else {
                sender.sendMessage(ChatColor.DARK_RED + "You don't have sufficient permissions!");
                return false;
            }
        }
        else {
            return false;
        }
    }
}
