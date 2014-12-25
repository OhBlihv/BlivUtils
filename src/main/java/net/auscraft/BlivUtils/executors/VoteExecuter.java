package net.auscraft.BlivUtils.executors;

import java.util.logging.Logger;

import net.auscraft.BlivUtils.BlivUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.minecraftdimensions.bungeesuitechat.managers.PlayerManager;
import com.minecraftdimensions.bungeesuitechat.objects.BSPlayer;

public class VoteExecuter implements CommandExecutor
{

	private BlivUtils b;
	Logger log;
	
	public VoteExecuter(BlivUtils instance)
	{
		b = instance;
		log = b.getLogger();
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
					log.info("Player " + args[0] + " is offline");
				}
				
				Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.GOLD	+ ChatColor.BOLD + "AusVote" + ChatColor.RESET
						+ ChatColor.GRAY + "]" + " " + ChatColor.BLUE + ChatColor.ITALIC + playerName + ChatColor.DARK_GREEN + " voted at " + ChatColor.GOLD
						+ ChatColor.ITALIC + args[2] + ChatColor.RESET + ". " + ChatColor.DARK_GREEN + "Earned $" + ChatColor.RESET + args[1] + ChatColor.DARK_GREEN + "!");
				//"&7[&6AusVote&7] &9&o%target%&r &2voted at &6&o" + serviceShrt + ".&r &2Earned &2$&f750&2!"
				return true;
			}
			else
			{
				b.printError(sender, "Invalid Syntax: /voteprint <name> <reward> <service>");
				return true;
			}
		}
		
		
		return false;
	}

}
