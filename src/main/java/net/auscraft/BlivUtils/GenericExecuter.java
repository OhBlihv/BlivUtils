package net.auscraft.BlivUtils;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class GenericExecuter
    implements CommandExecutor
{

    public GenericExecuter()
    {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
    {
        if(cmd.getName().equalsIgnoreCase("say"))
            if(sender.hasPermission("blivutils.say") || !(sender instanceof Player))
            {
                String message = "";
                if(args.length > 0)
                {
                    String as[] = args;
                    int i = as.length;
                    for(int j = 0; j < i; j++)
                    {
                        String data = as[j];
                        message = (new StringBuilder()).append(message).append(data).append(" ").toString();
                    }

                    Bukkit.broadcastMessage((new StringBuilder()).append(ChatColor.DARK_RED).append("|| ").append(ChatColor.GRAY).append("[").append(ChatColor.GREEN).append(ChatColor.BOLD).append("Server").append(ChatColor.RESET).append(ChatColor.GRAY).append("]").append(" ").append(ChatColor.YELLOW).append(ChatColor.ITALIC).append(message).toString());
                    return true;
                } else
                {
                    return false;
                }
            } else
            {
                sender.sendMessage((new StringBuilder()).append(ChatColor.DARK_RED).append("You don't have sufficient permissions!").toString());
                return false;
            }
        if(cmd.getName().equalsIgnoreCase("wstop") && !(sender instanceof Player))
            try
            {
                sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("Waiting 10 seconds before stopping the server.").toString());
                Bukkit.broadcastMessage((new StringBuilder()).append(ChatColor.DARK_RED).append("|| ").append(ChatColor.GRAY).append("[").append(ChatColor.GOLD).append(ChatColor.BOLD).append("Restart").append(ChatColor.RESET).append(ChatColor.GRAY).append("]").append(" ").append(ChatColor.YELLOW).append(ChatColor.ITALIC).append("Server Restarting in 10 Seconds!").toString());
                Thread.sleep(10000L);
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "kickall Restart");
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "stop");
                return true;
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        if(cmd.getName().equalsIgnoreCase("sudo"))
        {
            if(sender.hasPermission("blivutils.sudo") || !(sender instanceof Player))
            {
                sender.sendMessage((new StringBuilder()).append(ChatColor.DARK_RED).append("NICE TRY SINNY").toString());
                return true;
            } else
            {
                sender.sendMessage((new StringBuilder()).append(ChatColor.DARK_RED).append("You don't have sufficient permissions!").toString());
                return false;
            }
        } else
        {
            return false;
        }
    }
}
