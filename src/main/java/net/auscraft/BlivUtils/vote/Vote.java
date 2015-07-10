package net.auscraft.BlivUtils.vote;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.minecraftdimensions.bungeesuitechat.managers.PlayerManager;
import com.minecraftdimensions.bungeesuitechat.objects.BSPlayer;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.BUtil;
import net.auscraft.BlivUtils.utils.FlatFile;

public class Vote implements CommandExecutor
{


	private VoteManager voteMan;
	
	public Vote(BlivUtils instance)
	{
		voteMan = instance.getVoteMan();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) 
	{
		
		/*
		 * Vote Party Trigger Command
		 * Gives a higher chance of bonus rewards from voting
		 */
		if((cmd.getName().equalsIgnoreCase("voteparty")) && (sender.hasPermission("blivutils.vote.party")))
		{
			return voteMan.voteParty(sender, args);
		}
		
		
		//Converts the raw vote data into a nice message
		//Also translates the vanilla name into the global nickname, if available.
		// /voteprint <name> <reward> <service>
		if (cmd.getName().equalsIgnoreCase("voteprint") && (sender.hasPermission("blivutils.vote.print")))
		{
			if (args.length >= 3)
			{
				BSPlayer player = null;
				//Assuming that the player exists, as is already checked in the vote script.
				String playerName = args[0];
				try
				{
					player = PlayerManager.getPlayer(args[0]);
					if(player.hasNickname())
					{
						playerName = player.getNickname();
					}
				}
				catch(Exception e) //Not entirely sure which exception it is, but it should only throw one if the player is not online/misspelt.
				{
					BUtil.logInfo("Player " + args[0] + " is offline");
				}
				
				Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.GOLD	+ ChatColor.BOLD + "AusVote" + ChatColor.RESET
						+ ChatColor.GRAY + "]" + " " + ChatColor.BLUE + ChatColor.ITALIC + playerName + ChatColor.GREEN + " voted at " + ChatColor.GOLD
						+ ChatColor.ITALIC + args[2] + ChatColor.RESET + ". " + ChatColor.GREEN + "Earned" + ChatColor.DARK_GREEN + " $" + ChatColor.RESET + args[1] + ChatColor.DARK_GREEN + "!");
				if(FlatFile.getInstance().getInt("options.voting.rewards") == 1)
				{
					voteMan.rollBonusGift(args[0]);
				}
				return true;
			}
			
			BUtil.printError(sender, "Invalid Syntax: /voteprint <name> <reward> <service>");
			return true;
		}
		
		
		return false;
	}
}
