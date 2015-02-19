package net.auscraft.BlivUtils.vote;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minecraftdimensions.bungeesuitechat.managers.PlayerManager;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.rewards.RewardContainer;
import net.auscraft.BlivUtils.utils.Utilities;

public class VoteManager 
{

	private final String bar = ChatColor.YELLOW + "------ " + ChatColor.DARK_AQUA + ChatColor.BOLD + "Aus" + ChatColor.WHITE + ChatColor.BOLD + "Vote" + ChatColor.YELLOW + " ------------------------------------\n";
	private final String barBtm = ChatColor.YELLOW + "\n--------------------------------------------------";
	private Utilities util;
	private int rewardChance;
	private int nextTrigger;
	private RewardContainer[] voteRewards = new RewardContainer[6];
	
	public VoteManager(BlivUtils instance)
	{
		util = instance.getUtil();
		
		rewardChance = 15;
		nextTrigger = 0;
		
		voteRewards[0] = new RewardContainer(40.0, "&2$&f250", "/money give % 250", null, null);
		voteRewards[1] = new RewardContainer(35.0, "&2$&f500", "/money give % 500", null, null);
		voteRewards[2] = new RewardContainer(30.0, "&2$&f750", "/money give % 750", null, null);
		//XP Pots
		voteRewards[3] = new RewardContainer(25.0, "&a1/2 Stack of EXP Potions!", "/give % 384 32", null, null);
		voteRewards[4] = new RewardContainer(20.0, "&aStack of EXP Potions!", "/give % 384 64", null, null);
		//Mob Spawner
		voteRewards[5] = new RewardContainer(10.0, "&bMob Spawner!", "/give % 52 1", null, null);
		//MageStone
		voteRewards[6] = new RewardContainer(5.0, "&6MageStone!", "/give % 99 1", null, null);
	}
	
	public boolean voteParty(CommandSender sender, Command cmd, String label, String args[])
	{
		if(((int) (System.currentTimeMillis() / 1000L) > nextTrigger))
		{
			String triggerName = sender.getName();
			int chance = 50, timeParty = 30;
			if(PlayerManager.getPlayer(sender).hasNickname())
			{
				triggerName = PlayerManager.getPlayer(sender).getNickname();
			}
			if(sender.hasPermission("blivutils.vote.chance"))
			{
				try
				{
					if(args.length == 0)
					{
						util.printError(sender, "/voteparty [chance] [time in minutes]");
						return true;
					}
					//Chance given
					if(args.length >= 1)
					{
						chance = Integer.parseInt(args[0]);
						if((chance < 33) || (chance > 75))
						{
							util.printError(sender, "Chance has to be between 33 and 75 inclusive.");
							return true;
						}
						setChance(chance);
						//How many minutes should it last for?
						if(args.length >= 2)
						{
							timeParty = Integer.parseInt(args[1]);
							if(timeParty > 30)
							{
								util.printError(sender, "The party can't run for over 30 minutes!");
								return true;
							}
						}
					}
				}
				catch(NumberFormatException e)
				{
					util.printError(sender, "/voteparty [chance] [time in minutes]");
					return true;
				}
			}
			//Else, preset Bonus of 10% for everyone
			else
			{
				setChance(20);
			}
			
			util.broadcastPlain(bar 
					+ ChatColor.YELLOW + "" + ChatColor.ITALIC + ChatColor.BOLD + ChatColor.stripColor(triggerName)
					+ ChatColor.GREEN + " has triggered a " + ChatColor.YELLOW + ChatColor.ITALIC + "Vote Party" + ChatColor.YELLOW + " for " + ChatColor.AQUA + timeParty + " minutes" + ChatColor.YELLOW + "!\n"
					+ "You are " + ChatColor.AQUA + rewardChance + "%" + ChatColor.YELLOW + " likely to receive a " + ChatColor.BOLD + "BONUS" + ChatColor.YELLOW + " upon voting!\n"
					+ ChatColor.YELLOW + ChatColor.BOLD + "            »»  " + ChatColor.RESET + ChatColor.AQUA + ChatColor.BOLD + ChatColor.ITALIC + " /vote to participate " + ChatColor.YELLOW + "  «« " + ChatColor.RESET 
					+ barBtm);
			
			//Set the time when the next party can be triggered
			//Current time in seconds + 2 hours in seconds.
			nextTrigger = ((int) (System.currentTimeMillis() / 1000L)) + 7200;
			
			// Schedule the party to end
			util.getInstance().getServer().getScheduler().runTaskLater(util.getInstance(), new Runnable()
			{
				public void run()
				{
					setChance(15);
					util.broadcastPlain(bar
							+ ChatColor.YELLOW + ChatColor.ITALIC + "The Vote Party has Ended!"
							+ barBtm);
				}
				
			}, timeParty * 60 * 20);
			
			//Schedule notifications throughout the party
			// Schedule the party to end
			// (3 messages spread out at each quarter)
			final int partyTimeFinal = timeParty;
			final int rewardChanceFinal = rewardChance;
			for(int quarter = 1;quarter < 4;quarter++)
			{
				final int finalQuarter = quarter;
				util.getInstance().getServer().getScheduler().runTaskLater(util.getInstance(), new Runnable()
				{
					public void run()
					{
					util.broadcastPlain(bar																	//OH GOD PLEASE DONT TOUCH THIS AFTER I FINISH IT
							+ ChatColor.YELLOW + ChatColor.ITALIC + " The Vote Party ends in" + getConversion((partyTimeFinal * 60) - (((partyTimeFinal * 60) / 4) * finalQuarter)) + "\n"
							+ ChatColor.AQUA + " " + rewardChanceFinal + "%" + ChatColor.YELLOW + " chance of bonus reward!\n"
							+ ChatColor.YELLOW + ChatColor.BOLD + " »»  " + ChatColor.RESET + ChatColor.AQUA + ChatColor.BOLD + ChatColor.ITALIC + " /vote to participate " + ChatColor.YELLOW + "  «« " + ChatColor.RESET 
							+ barBtm);
				}
				}, ((timeParty * 60 * 20) / 4) * quarter);
			}
			return true;
		}
		else
		{
			String timeString = getConversion(nextTrigger - ((int) (System.currentTimeMillis() / 1000L)));
			util.printError(sender, "On Cooldown! Can trigger in: " + timeString);
			return true;
		}
	}
	
	private void setChance(int inChance)
	{
		rewardChance = inChance;
	}
	
	public String rollBonusGift(Player player)
	{
		Random rand = new Random(System.currentTimeMillis());
		
		/*
		 * Roll out of 100 -- If number is below 15 (15% chance of winning any item) then go to next stage
		 * Roll again for each of the rewards, going in reverse order of rarest to most common.
		 * If roll number is below the chance, give reward and print message. Quit after that.
		 */
		
		int won = rand.nextInt(100);
		int rewardChanceDiff = rewardChance;
		if(PlayerManager.getPlayer(player).getPlayer().hasPermission("blivutils.vote.ender"))
		{
			rewardChanceDiff += 10;
		}
		//util.logInfo("Won reward? = " + won);
		int roll = 0;
		RewardContainer rolledReward = null;
		util.logDebug("Roll: " + won + " | Required Roll or lower: " + rewardChanceDiff);
		if(won <= rewardChanceDiff)
		{
			boolean wonPrize = false;
			//util.logInfo("Won chance!");
			while(!wonPrize)
			{
				for(RewardContainer reward : voteRewards)
				{
					roll = rand.nextInt(100);
					//util.logInfo((int)reward.getChance() + " < " + won + " ?");
					//Win!
					try
					{
						if(roll <= (int)reward.getChance())
						{
							wonPrize = true;
							rolledReward = reward;
						}
					}
					catch(NullPointerException e)
					{
						try
						{
							util.logDebug("Reward " + reward.getName() + " has a bugged chance!");
						}
						catch(Exception e2)
						{
							util.logDebug("Oh man, just give up. That reward is bugged");
						}
					}
					if(wonPrize == true)
					{
						break;
					}
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
	
	private String getConversion(int seconds)
	{
		String timeString = "";
		int day = (int) TimeUnit.SECONDS.toDays(seconds);
		if(day < 0)
		{
			day = day * (-1);
		}
		long hour = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
		if(hour < 0)
		{
			hour = hour * (-1);
		}
		long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
		if(minute < 0)
		{
			minute = minute * (-1);
		}
		long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);
		if(second < 0)
		{
			second = second * (-1);
		}

		// String them together, then cut them down if they're 0
		if (day != 0)
		{
			timeString += " " + day + " Day";
			if(day > 1)
			{
				timeString += "s";
			}
		}
		if (hour != 0)
		{
			timeString += " " + hour + " Hour";
			if(hour > 1)
			{
				timeString += "s";
			}
		}
		if (minute != 0) 
		{
			timeString += " " + minute + " Minute";
			if(minute > 1)
			{
				timeString += "s";
			}
		}
		if (minute == 0) 
		{
			timeString += " " + second + " Second";
			if(second > 1)
			{
				timeString += "s";
			}
		}
		return timeString;
	}
}
