package net.auscraft.BlivUtils.executors;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.minecraftdimensions.bungeesuitechat.managers.PlayerManager;
import com.minecraftdimensions.bungeesuitechat.objects.BSPlayer;

public class VoteExecuter implements CommandExecutor
{

	private Utilities util;
	
	public VoteExecuter(BlivUtils instance)
	{
		util = instance.getUtil();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) 
	{
		//Converts the raw vote data into a nice message
		//Also translates the vanilla name into the global nickname, if available.
		// /voteprint <name> <reward> <service>
		if (cmd.getName().equalsIgnoreCase("voteprint") && (sender.hasPermission("blivutils.vote.print")))
		{
			if (args.length >= 3)
			{
				//Assuming that the player exists, as is already checked in the vote script.
				String playerName = args[0];
				try
				{
					BSPlayer p = PlayerManager.getPlayer(args[0]);
					if(p.hasNickname())
					{
						playerName = p.getNickname();
					}
				}
				catch(Exception e) //Not entirely sure which exception it is, but it should only throw one if the player is not online/misspelt.
				{
					util.logInfo("Player " + args[0] + " is offline");
				}
				
				Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.GOLD	+ ChatColor.BOLD + "AusVote" + ChatColor.RESET
						+ ChatColor.GRAY + "]" + " " + ChatColor.BLUE + ChatColor.ITALIC + playerName + ChatColor.DARK_GREEN + " voted at " + ChatColor.GOLD
						+ ChatColor.ITALIC + args[2] + ChatColor.RESET + ". " + ChatColor.DARK_GREEN + "Earned $" + ChatColor.RESET + args[1] + ChatColor.DARK_GREEN + "!");
				return true;
			}
			else
			{
				util.printError(sender, "Invalid Syntax: /voteprint <name> <reward> <service>");
				return true;
			}
		}
		
		
		return false;
	}

}
