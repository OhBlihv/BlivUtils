package net.auscraft.BlivUtils.timed;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;

public class TimedCommands implements CommandExecutor
{

	private Utilities util;
	
	public TimedCommands(BlivUtils instance)
	{
		util = instance.getUtil();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
	{
		//Temporarily add a command to a player.
		//Currently supports toggle commands, but may support others later by request.
		//Syntax: /timedadd <player> <time> <second[s]/minute[s]/hour[s]/day[s]> <command>
		if (cmd.getName().equalsIgnoreCase("timedadd"))
		{
			if(sender.hasPermission("blivutils.timed.add"))
			{
				if(args.length >= 4)
				{
					//Set up the time
					int time = 0, unitTime = (Integer.parseInt(args[1]));
					String timeframe = "";
					if (args[2].contains("second"))
					{
						time = (Integer.parseInt(args[1])); //No Conversion necessary
						timeframe = "second";
					} 
					else if (args[2].contains("minute"))
					{
						// Seconds multiplied by 60 to get args[1] minute(s)
						time = (Integer.parseInt(args[1])) * 60;
						timeframe = "minute";
					}
					else if (args[2].contains("hour"))
					{
						// Seconds multiplied by 3600 to get args[1] hour(s)
						time = (Integer.parseInt(args[1])) * 3600;
						timeframe = "hour";
					}
					else if (args[2].contains("day"))
					{
						// Seconds multiplied by that 86,400 to get args[1] day(s)
						time = (Integer.parseInt(args[1])) * 86400;
						timeframe = "day";
					}
					else
					{
						util.printError(sender, "Invalid Syntax: /timedadd <player> <time> <second[s]/minute[s]/hour[s]/day[s]> <command>");
						return true;
					}
					
					
					//Set up the command string
					String command = "";
					//int length = args.length;
					//Start at the second element, first is the player name.
					for (int i = 1; i < i; i++)
					{
						command = (command += args[i] + " ");
					}
					
					timedAdd(sender, args[0], time, unitTime, timeframe, command);
				}
				else
				{
					util.printError(sender, "Invalid Syntax: /timedadd <player> <time> <second[s]/minute[s]/hour[s]/day[s]> <command>");
					return true;
				}
			}
			else
			{
				util.printError(sender, "You don't have sufficient permissions!");
			}
		}
		return true;
	}

	public void timedAdd(CommandSender sender, String player, int time, int unitTime, String timeframe, String command)
	{
		//Player should be online to receive timed command.
		Player p = Bukkit.getPlayer(player);
		
		if(p.isOnline())
		{
			if(unitTime > 1)
			{
				timeframe += "s"; //Equals second + s (no space), is 'seconds'
			}
			
			
			
			
			
			
			util.printSuccess(sender, "Added command to " + player + " for " + unitTime + " " + timeframe);
		}
		else
		{
			util.printError(sender, "Player is not online!");
		}
		
		
	}
	
	
	
	
}
