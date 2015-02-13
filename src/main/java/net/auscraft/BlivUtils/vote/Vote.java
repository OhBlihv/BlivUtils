package net.auscraft.BlivUtils.vote;

import java.util.Random;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;
import net.auscraft.BlivUtils.rewards.RewardContainer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minecraftdimensions.bungeesuitechat.managers.PlayerManager;
import com.minecraftdimensions.bungeesuitechat.objects.BSPlayer;

public class Vote implements CommandExecutor
{

	private Utilities util;
	//private VoteParty voteParty;
	
	//Lazy implementation while I test
	private RewardContainer[] voteRewards = new RewardContainer[10];
	
	public Vote(BlivUtils instance)
	{
		util = instance.getUtil();
		//voteParty = new VoteParty(instance);
		
		voteRewards[0] = new RewardContainer(50.0, "&2$&f250", "/money give % 250", null, null);
		voteRewards[1] = new RewardContainer(45.0, "&2$&f500", "/money give % 500", null, null);
		voteRewards[2] = new RewardContainer(40.0, "&2$&f750", "/money give % 750", null, null);
		//XP Pots
		voteRewards[3] = new RewardContainer(15.0, "&aEXP Potion!", "/give % 384 1", null, null);
		//Beacon
		voteRewards[4] = new RewardContainer(15.0, "&aBeacon!", "/give % 138 1", null, null);
		//Mob Spawner
		voteRewards[5] = new RewardContainer(15.0, "&bMob Spawner!", "/give % 52 1", null, null);
		//MageStone
		voteRewards[6] = new RewardContainer(1.0, "&6MageStone!", "/give % 99 1", null, null);
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
				BSPlayer p = null;
				//Assuming that the player exists, as is already checked in the vote script.
				String playerName = args[0];
				try
				{
					p = PlayerManager.getPlayer(args[0]);
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
				//Only has a chance of winning if the player is online
				if(p != null)
				{
					if(util.getInstance().getCfg().getInt("options.voting.rewards") == 1)
					{
						//util.logInfo("Trying bonus reward...");
						rollBonusGift(p.getPlayer());
					}
					else
					{
						util.logInfo("Voting Rewards disabled.");
					}
				}
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
	
	private String rollBonusGift(Player player)
	{
		Random rand = new Random(System.currentTimeMillis());
		
		/*
		 * Roll out of 100 -- If number is below 15 (15% chance of winning any item) then go to next stage
		 * Roll again for each of the rewards, going in reverse order of rarest to most common.
		 * If roll number is below the chance, give reward and print message. Quit after that.
		 */
		
		int won = rand.nextInt(100);
		//util.logInfo("Won reward? = " + won);
		int roll = 0;
		RewardContainer rolledReward = null;
		if(won <= 15)
		{
			boolean wonPrize = false;
			//util.logInfo("Won chance!");
			for(RewardContainer reward : voteRewards)
			{
				roll = rand.nextInt(100);
				util.logInfo((int)reward.getChance() + " < " + won + " ?");
				//Win!
				if(roll <= (int)reward.getChance())
				{
					wonPrize = true;
					//util.logInfo("WON " + reward.getName());
					rolledReward = reward;
				}
				/*else
				{
					util.logInfo("Didnt win " + reward.getName());
				}*/
				
				if(wonPrize == true)
				{
					break;
				}
			}
			
			giveReward((CommandSender) player, rolledReward);
		}
		/*else
		{
			util.logInfo("Did not win (" + won + " not below 15)");
		}*/
		
		
		//Return nothing if reward is not won
		return null;
	}
	
	public void giveReward(CommandSender sender, RewardContainer rolledGift)
	{
		Player p = (Player) sender;
		try
		{
			util.logInfo("Giving Reward: " + rolledGift.getName());
			String action = rolledGift.getAction().substring(1, rolledGift.getAction().length());
			action = action.replaceAll("%", p.getName()); //Replace % with players name
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), action);
			util.logInfo("Command: '" + action + "'");
			Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + ChatColor.BOLD + "AusVote" + ChatColor.RESET
					+ ChatColor.GRAY + "] " + ChatColor.DARK_GREEN + "And a " + ChatColor.GOLD + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "BONUS: " + "" + ChatColor.RESET + ChatColor.GOLD + util.translateColours(rolledGift.getName()));
		}
		catch(Exception e)
		{
			util.logSevere("Error giving rewards! Check the config for misplaced ','");
			e.printStackTrace();
		}
	}
}
