package net.auscraft.BlivUtils.vote;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.minecraftdimensions.bungeesuitechat.managers.PlayerManager;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.rewards.RewardContainer;
import net.auscraft.BlivUtils.utils.Utilities;

public class VoteManager implements CommandExecutor
{

	private final String bar = ChatColor.YELLOW + "------ " + ChatColor.DARK_AQUA + ChatColor.BOLD + "Aus" + ChatColor.WHITE + ChatColor.BOLD + "Vote" + ChatColor.YELLOW + " ------------------------------------\n";
	private final String barBtm = ChatColor.YELLOW + "\n--------------------------------------------------";
	private Utilities util;
	private int rewardChance;
	private int nextTrigger;
	private RewardContainer[] voteRewards = new RewardContainer[7];
	private static HashMap<String, RewardContainer[]> voteClaim = new HashMap<String, RewardContainer[]>();
	private Random rand = new Random(System.currentTimeMillis());
	
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
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) 
	{
		if(cmd.getName().equalsIgnoreCase("voteclaim"))
		{
			if(voteClaim.containsKey(sender.getName().toLowerCase()))
			{
				RewardContainer[] rewardArr = voteClaim.get(sender.getName().toLowerCase());
				String claimString = "";
				try
				{
					String action = null;
					for(RewardContainer reward : rewardArr)
					{
						action = reward.getAction().substring(1, reward.getAction().length());
						action = action.replaceAll("%", sender.getName()); //Replace % with players name
						Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), action);
						util.logInfo("Command: '" + action + "'");
						claimString += " - " + reward.getName() + "\n";
					}
					voteClaim.remove(sender.getName().toLowerCase());
				}
				catch(NullPointerException e)
				{
					util.logDebug("Reached end of reward bank.");
				}
				util.printSuccess(sender, util.translateColours("Your rewards have been claimed:\n" + claimString));
				voteClaim.remove(sender.getName().toLowerCase());
			}
			else
			{
				util.printError(sender, "You don't have any unclaimed rewards!");
			}
			return true;
		}
		return true;
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
	
	public String rollBonusGift(String player)
	{
		
		/*
		 * Roll out of 100 -- If number is below 15 (15% chance of winning any item) then go to next stage
		 * Roll again for each of the rewards, going in reverse order of rarest to most common.
		 * If roll number is below the chance, give reward and print message. Quit after that.
		 */
		
		int won = rand.nextInt(100);
		int rewardChanceDiff = rewardChance;
		if(PermissionsEx.getUser(player).has("blivutils.vote.ender"))
		//if(offlinePlayer.getPlayer().hasPermission("blivutils.vote.ender"))
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
			giveReward(player, rolledReward);
		}
		/*else
		{
			util.logInfo("Did not win (" + won + " not below 15)");
		}*/
		
		
		//Return nothing if reward is not won
		return null;
	}
	
	public void giveReward(String player, RewardContainer rolledGift)
	{
		try
		{
			Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + ChatColor.BOLD + "AusVote" + ChatColor.RESET
					+ ChatColor.GRAY + "] " + ChatColor.DARK_GREEN + "And a " + ChatColor.GOLD + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + "BONUS: " + "" + ChatColor.RESET + ChatColor.GOLD + util.translateColours(rolledGift.getName()));
			if(rolledGift.getName().contains("$"))
			{
				String action = rolledGift.getAction().substring(1, rolledGift.getAction().length());
				action = action.replaceAll("%", player); //Replace % with players name
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), action);
				util.logInfo("Command: '" + action + "'");
			}
			else
			{
				util.logInfo("Adding " + rolledGift.getName() + " to " + player + "'s reward bank");
				if(voteClaim.containsKey(player.toLowerCase()))
				{
					RewardContainer[] rewards = voteClaim.get(player.toLowerCase());
					int rewardCount = 0;
					while(rewards[rewardCount] != null)
					{
						rewardCount++;
					}
					rewards[rewardCount] = rolledGift;
					voteClaim.put(player.toLowerCase(), rewards);
				}
				//If player has no rewards queued up
				else
				{
					RewardContainer[] rewards = new RewardContainer[10];
					rewards[0] = rolledGift;
					voteClaim.put(player.toLowerCase(), rewards);
				}
				if(Bukkit.getPlayer(player) != null)
				{
					util.printInfo((CommandSender) Bukkit.getOfflinePlayer(player), util.translateColours(ChatColor.GREEN + "Your reward: " + rolledGift.getName() + ChatColor.GREEN + " has been added to your reward bank.\n "
							+ ChatColor.GOLD + ChatColor.ITALIC + "   »» " + ChatColor.GREEN + "Type " + ChatColor.AQUA + "/voteclaim" + ChatColor.GREEN + " to claim"));
				}
			}
			
		}
		catch(Exception e)
		{
			util.logSevere("Error giving rewards! Check the rewards for any misplaced characters");
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
	
	public Set<String> getUnclaimedRewards()
	{
		return voteClaim.keySet();
	}
}
