package net.auscraft.BlivUtils;

import lombok.Getter;
import net.auscraft.BlivUtils.executors.ColourExecutor;
import net.auscraft.BlivUtils.executors.GenericExecutor;
import net.auscraft.BlivUtils.executors.PromoteExecutor;
import net.auscraft.BlivUtils.executors.RankHelpExecutor;
import net.auscraft.BlivUtils.listeners.*;
import net.auscraft.BlivUtils.purchases.Broadcast;
import net.auscraft.BlivUtils.purchases.Ender;
import net.auscraft.BlivUtils.rewards.Rewards;
import net.auscraft.BlivUtils.util.BUtil;
import net.auscraft.BlivUtils.util.FlatFile;
import net.auscraft.BlivUtils.vote.Vote;
import net.auscraft.BlivUtils.vote.VoteManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.HashMap;

public final class BlivUtils extends JavaPlugin
{

	@Getter
	private static BlivUtils instance;

	//Temporary storage
	@Getter
	private static HashMap<String, String> colourSave;
	
	//Ender Rank bonuses (Temporary Storage)
	@Getter
	private static HashMap<String, Boolean> xpClaim;
	@Getter
	private static HashMap<String, Boolean> fixClaim;

	@Getter
	private static PermissionManager pex;
	@Getter
	private FlatFile cfg;
	BukkitScheduler scheduler = Bukkit.getServer().getScheduler();


	@Override
	public void onEnable()
	{
		instance = this;
		
		pex = PermissionsEx.getPermissionManager();
		cfg = FlatFile.getInstance();
		BUtil.checkRankScheduler();
		doScheduledCommands();
		
		fixClaim = new HashMap<>();
		xpClaim = new HashMap<>();
		colourSave = new HashMap<>();
		
		// Some of the commands require the above set up before they can appear to function.
		// Command Declaration
		
		//Always enabled commands
		getCommand("bu").setExecutor(new GenericExecutor());
		getCommand("rank").setExecutor(new RankHelpExecutor());
		getCommand("say").setExecutor(new GenericExecutor());
		getCommand("sayplain").setExecutor(new GenericExecutor());
		getCommand("wstop").setExecutor(new GenericExecutor());
		getCommand("purch").setExecutor(new Broadcast());
		getCommand("lore").setExecutor(new Ender());
		getCommand("enderperm").setExecutor(new Ender());
		getCommand("enderrank").setExecutor(new Ender());
		getCommand("voteprint").setExecutor(Vote.getInstance());
		getCommand("voteclaim").setExecutor(Vote.getInstance());
		getCommand("voteparty").setExecutor(Vote.getInstance());
		getCommand("timeleft").setExecutor(new PromoteExecutor());
		getCommand("prefix").setExecutor(new PromoteExecutor());
		getCommand("chat").setExecutor(new ColourExecutor());
		getCommand("promoadmin").setExecutor(new PromoteExecutor());
		getCommand("updateadmin").setExecutor(new PromoteExecutor());
		getCommand("updatetime").setExecutor(new PromoteExecutor());
		
		//Commands disabled on Vanilla
		if(cfg.getBoolean("options.vanilla"))
		{
			getCommand("xpClaim").setExecutor(RemovedCommand.getInstance());
			getCommand("fixClaim").setExecutor(RemovedCommand.getInstance());
			getCommand("health").setExecutor(RemovedCommand.getInstance());
		}
		else
		{
			getCommand("xpClaim").setExecutor(new Ender());
			getCommand("fixClaim").setExecutor(new Ender());
			getCommand("health").setExecutor(HealthListener.getInstance());
			getServer().getPluginManager().registerEvents(HealthListener.getInstance(), this);
			getServer().getPluginManager().registerEvents(new XPListener(), this);
			getServer().getPluginManager().registerEvents(new DeathListener(), this);
		}
		
		//Listeners
		getServer().getPluginManager().registerEvents(new ColourListener(), this);
		getServer().getPluginManager().registerEvents(new PromotionListener(), this);
		getServer().getPluginManager().registerEvents(new JoinListener(), this);
		
		
		if(cfg.getInt("options.toggle.rankpromotion") == 1)
		{
			getCommand("buyrank").setExecutor(new PromoteExecutor());
			BUtil.logInfo("Rank Purchasing Enabled.");
		}
		else
		{
			getCommand("buyrank").setExecutor(RemovedCommand.getInstance());
			BUtil.logInfo("Rank Purchasing Disabled.");
		}
		
		if(cfg.getInt("options.toggle.presents") == 1)
		{
			getCommand("present").setExecutor(new Rewards());
			BUtil.logInfo("Rewards Enabled...");
		}
		else
		{
			getCommand("present").setExecutor(RemovedCommand.getInstance());
			BUtil.logInfo("Rewards Disabled.");
		}
		
		//Vote Reward checker
		if(this.getCfg().getInt("options.voting.rewards") == 1)
		{
			this.doVoteRewardCheck();
		}
		else
		{
			BUtil.logInfo("Voting Rewards disabled.");
		}
	}

	@Override
	public void onDisable()
	{
		//BUtil.logInfo("BlivUtils disabling...");
	}

	public static void startRankScheduler()
	{
		String unit = FlatFile.getInstance().getString("options.scheduler.unit");
		int looptime = BUtil.getConversion(unit, "options.scheduler.time");
		Bukkit.getScheduler().scheduleSyncRepeatingTask(BlivUtils.getInstance(), () ->
		{
			for (Player player : Bukkit.getServer().getOnlinePlayers())
			{
				PermissionUser user = pex.getUser(player);
				String rankString = BUtil.getActiveRanks(player.getName());
				if (rankString.length() != 0)
				{
					String ranks[] = rankString.split(",");
					for(String rank : ranks)
					{
						if ((Long.parseLong(user.getOption("group-" + rank + "-until", null)) < ((System.currentTimeMillis() / 1000L))))
						{
							user.removeGroup(rank, null);
							user.setOption("group-" + rank + "-until", null); // Reset the option's value so that the scheduler doesn't get past the first if.
							if(rank.equals("EnderRank"))
							{
								String EnderRankValue = user.getOption("EnderRankValue");
								BUtil.wipePackages(player.getName());
								switch(EnderRankValue)
								{
									case "1":
										rank = "Enderman";
										if(user.getPrefix().equals("&7[&5Enderman&7] "))
										{
											user.setPrefix("", null);
										}
										user.setOption("EnderRankValue", null);
										break;
									case "2":
										rank = "EnderDragon";
										if(user.getPrefix().equals("&7[&4EnderDragon&7] "))
										{
											user.setPrefix("", null);
										}
										user.setOption("EnderRankValue", null);
										Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "enderperm remove " + player.getName() + " EnderDragon");
										break;
									case "3":
										rank = "Wither";
										if(user.getPrefix().equals("&7[&8Wither&7] "))
										{
											user.setPrefix("", null);
										}
										user.setOption("EnderRankValue", null);
										Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "enderperm remove " + player.getName() + " Wither");
										break;
									default:
										break;
								}
							}
							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"mail send " + player.getName() + " Your " + rank + " has EXPIRED!");
							BUtil.printInfo(player, ChatColor.GOLD + "Your " + rank + " has " + ChatColor.DARK_RED + "EXPIRED!");
							BUtil.logInfo("Player " + player.getName() + "'s " + rank + " has EXPIRED!");
						}
					}

				}
			}
		}, 0L, looptime);
	}
	
	public static void doScheduledCommands()
	{
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(BlivUtils.getInstance(), () ->
		{
			Bukkit.getScheduler().runTask(instance, new Runnable()
			{

				public void run()
				{
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex reload");
				}

			});
			Bukkit.getScheduler().runTask(instance, new Runnable()
			{

				public void run()
				{
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
				}

			});
		}, 18000L, 18000L /*200L*/); //5 Minutes
	}
	
	public void doVoteRewardCheck()
	{
		scheduler.scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				for(String player : VoteManager.getInstance().getUnclaimedRewards())
				{
					try
					{
						BUtil.printInfo(Bukkit.getPlayer(player), ChatColor.GREEN + "You have an unclaimed voting reward! Type " + ChatColor.AQUA + "/voteclaim" + ChatColor.GREEN + " to claim");
					}
					catch(NullPointerException e)
					{
						//
					}
				}
			}
		}, 0L, 6000L /*200L*/); //5 Minutes
	}

	public static Permission setupPermissions()
	{
		return Bukkit.getServer().getServicesManager().getRegistration(Permission.class).getProvider();
	}

	public static Economy setupEconomy()
	{
		return Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
	}

}
