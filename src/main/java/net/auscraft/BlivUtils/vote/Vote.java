package net.auscraft.BlivUtils.vote;

import net.auscraft.BlivUtils.rewards.RewardContainer;
import net.auscraft.BlivUtils.util.BUtil;
import net.auscraft.BlivUtils.util.FlatFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Vote implements CommandExecutor
{
	
	private static Vote instance = null;
	public static Vote getInstance()
	{
		if(instance == null)
		{
			instance = new Vote();
		}
		return instance;
	}
	
	private Vote()
	{
		
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
			return VoteManager.getInstance().voteParty(sender, args);
		}
		
		
		//Converts the raw vote data into a nice message
		//Also translates the vanilla name into the global nickname, if available.
		// /voteprint <name> <reward> <service>
		else if (cmd.getName().equalsIgnoreCase("voteprint") && (sender.hasPermission("blivutils.vote.print")))
		{
			if (args.length >= 3)
			{
				OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
				//Assuming that the player exists, as is already checked in the vote script.
				String playerName = args[0];
				if(player == null)
				{
					return true;
				}
				else if(player.hasPlayedBefore() || player.isOnline())
				{
					try
					{
						playerName = player.isOnline() ? player.getPlayer().getDisplayName() : player.getName();
					}
					catch(NullPointerException e)
					{
						//
					}
				}
				
				Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.GOLD	+ ChatColor.BOLD + "AusVote" + ChatColor.RESET
						+ ChatColor.GRAY + "]" + " " + ChatColor.BLUE + ChatColor.ITALIC + playerName + ChatColor.GREEN + " voted at " + ChatColor.GOLD
						+ ChatColor.ITALIC + args[2] + ChatColor.RESET + ". " + ChatColor.GREEN + "Earned" + ChatColor.DARK_GREEN + " $" + ChatColor.RESET + args[1] + ChatColor.DARK_GREEN + "!");
				if(FlatFile.getInstance().getInt("options.voting.rewards") == 1)
				{
					VoteManager.getInstance().rollBonusGift(args[0]);
				}
				return true;
			}
			
			BUtil.printError(sender, "Invalid Syntax: /voteprint <name> <reward> <service>");
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("voteclaim"))
		{
			if(VoteManager.getVoteClaim().containsKey(sender.getName().toLowerCase()))
			{
				RewardContainer[] rewardArr = VoteManager.getVoteClaim().get(sender.getName().toLowerCase());
				String claimString = "";
				try
				{
					String action;
					for(RewardContainer reward : rewardArr)
					{
						action = reward.getAction().substring(1, reward.getAction().length());
						action = action.replaceAll("%", sender.getName()); //Replace % with players name
						Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), action);
						BUtil.logInfo("Command: '" + action + "'");
						claimString += " - " + reward.getName() + "\n";
					}
					VoteManager.getVoteClaim().remove(sender.getName().toLowerCase());
				}
				catch(NullPointerException e)
				{
					BUtil.logDebug("Reached end of reward bank.");
				}
				BUtil.printSuccess(sender, BUtil.translateColours("Your rewards have been claimed:\n" + claimString));
				VoteManager.getVoteClaim().remove(sender.getName().toLowerCase());
			}
			else
			{
				BUtil.printError(sender, "You don't have any unclaimed rewards!");
			}
			return true;
		}
		
		
		return false;
	}
}
