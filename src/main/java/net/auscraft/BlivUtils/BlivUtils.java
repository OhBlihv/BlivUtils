package net.auscraft.BlivUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.auscraft.BlivUtils.executors.ColourExecutor;
import net.auscraft.BlivUtils.executors.GenericExecutor;
import net.auscraft.BlivUtils.executors.RankHelpExecutor;
import net.auscraft.BlivUtils.listeners.ColourListener;
import net.auscraft.BlivUtils.listeners.DeathListener;
import net.auscraft.BlivUtils.listeners.HealthListener;
import net.auscraft.BlivUtils.listeners.JoinListener;
import net.auscraft.BlivUtils.listeners.PromotionListener;
import net.auscraft.BlivUtils.listeners.XPListener;
import net.auscraft.BlivUtils.promotions.PromoteExecutor;
import net.auscraft.BlivUtils.purchases.Broadcast;
import net.auscraft.BlivUtils.purchases.Ender;
import net.auscraft.BlivUtils.rewards.Rewards;
import net.auscraft.BlivUtils.utils.BUtil;
import net.auscraft.BlivUtils.utils.FlatFile;
import net.auscraft.BlivUtils.vote.Vote;
import net.auscraft.BlivUtils.vote.VoteManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import org.bukkit.scheduler.BukkitScheduler;

public final class BlivUtils extends JavaPlugin
{
	//Temporary storage
	@Getter
	private static HashMap<String, String> colourSave;
	
	//Ender Rank bonuses (Temporary Storage)
	@Getter
	private static HashMap<String, Boolean> xpClaim;
	@Getter
	private static HashMap<String, Boolean> fixClaim;
	
	@Getter
	private static BlivUtils instance;
	@Getter
	private static PermissionManager pex;
	@Getter
	private FlatFile cfg;
	@Getter
	private VoteManager voteMan;
	BukkitScheduler scheduler = Bukkit.getServer().getScheduler();


	@Override
	public void onEnable()
	{
		instance = this;
		
		pex = PermissionsEx.getPermissionManager();
		cfg = FlatFile.getInstance();
		voteMan = new VoteManager();
		BUtil.checkRankScheduler();
		setupMySQL();
		doScheduledCommands();
		
		fixClaim = new HashMap<String, Boolean>();
		xpClaim = new HashMap<String, Boolean>();
		colourSave = new HashMap<String, String>();
		
		// Some of the commands require the above set up before they can appear to function.
		// Command Declaration
		
		//Always enabled commands
		getCommand("bu").setExecutor(new GenericExecutor(this));
		getCommand("rank").setExecutor(new RankHelpExecutor());
		getCommand("say").setExecutor(new GenericExecutor(this));
		getCommand("wstop").setExecutor(new GenericExecutor(this));
		getCommand("servers").setExecutor(new GenericExecutor(this));
		getCommand("purch").setExecutor(new Broadcast());
		getCommand("lore").setExecutor(new Ender());
		getCommand("xpClaim").setExecutor(new Ender());
		getCommand("fixClaim").setExecutor(new Ender());
		getCommand("enderperm").setExecutor(new Ender());
		getCommand("enderrank").setExecutor(new Ender());
		getCommand("health").setExecutor(new HealthListener());
		getCommand("voteprint").setExecutor(new Vote(this));
		getCommand("voteclaim").setExecutor(new VoteManager());
		getCommand("voteparty").setExecutor(new Vote(this));
		getCommand("timeleft").setExecutor(new PromoteExecutor());
		getCommand("prefix").setExecutor(new PromoteExecutor());
		getCommand("chat").setExecutor(new ColourExecutor());
		getCommand("promoadmin").setExecutor(new PromoteExecutor());
		getCommand("updateadmin").setExecutor(new PromoteExecutor());
		getCommand("updatetime").setExecutor(new PromoteExecutor());
		
		//Listeners
		getServer().getPluginManager().registerEvents(new ColourListener(this), this);
		getServer().getPluginManager().registerEvents(new PromotionListener(this), this);
		getServer().getPluginManager().registerEvents(JoinListener.getInstance(), this);
		getServer().getPluginManager().registerEvents(new XPListener(), this);
		getServer().getPluginManager().registerEvents(new DeathListener(), this);
		getServer().getPluginManager().registerEvents(new HealthListener(), this);
		
		if(cfg.getInt("options.toggle.rankpromotion") == 1)
		{
			getCommand("buyrank").setExecutor(new PromoteExecutor());
			BUtil.logInfo("Rank Purchasing Enabled.");
		}
		else
		{
			getCommand("buyrank").setExecutor(new RemovedCommand());
			BUtil.logInfo("Rank Purchasing Disabled.");
		}
		
		if(cfg.getInt("options.toggle.presents") == 1)
		{
			getCommand("present").setExecutor(new Rewards());
			BUtil.logInfo("Rewards Enabled...");
		}
		else
		{
			getCommand("present").setExecutor(new RemovedCommand());
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
		BUtil.logInfo("BlivUtils disabling...");
	}
	
	//Setup for MySQL
	private void setupMySQL()
	{
		//Read these in from the config so I dont have to put the database info on GitHub :L
		String user = cfg.getString("options.mysql.user");
		String pass = cfg.getString("options.mysql.pass");
		String url = cfg.getString("options.mysql.url");
		
		int setup = cfg.getInt("options.mysql.setup");
		if(setup == 0)
		{
			BUtil.logInfo("Table not set up. First run.");
			//Set the setup variable to 1, so it attempts to setup the CreditsDB table
			cfg.saveValue("options.mysql.setup", "1");
			//configSetup.setValue("options.mysql.setup", "1");
			BUtil.logInfo("setup should now be 1");
		}
		else if(setup == 1)
		{
			scheduler.runTaskAsynchronously(this, new Runnable()
			{
				
				public void run()
				{
					try
					{
						BUtil.logInfo("Setting up Credits Table...");
						//Initial creation of Table
						Connection conn = DriverManager.getConnection(url, user, pass);
						PreparedStatement sampleQueryStatement = conn.prepareStatement("CREATE TABLE IF NOT EXISTS CreditsDB (playerName VARCHAR(50) PRIMARY KEY, value INT)");
						sampleQueryStatement.executeUpdate();
						sampleQueryStatement.close();
						conn.close();
						BUtil.logInfo("Successfully set up Credits Table");
						//Set the setup variable to 2, so it doesn't attempt to setup the CreditsDB table anymore.
						//configSetup.setValue("options.mysql.setup", "2");
						cfg.saveValue("options.mysql.setup", "2");
					}
					catch(SQLException e)
					{
						e.printStackTrace(); //For now, I'll fix this later.
						BUtil.logSevere("Failed setting up Credits Table");
						BUtil.logSevere("Disabling...");
						Bukkit.getPluginManager().disablePlugin(instance);
					}
				}
				
			});
		}
		else  //Setup == 2
		{
			BUtil.logInfo("Credits table already set up. Skipping...");
		}
	}

	public static void startRankScheduler()
	{
		String unit = FlatFile.getInstance().getString("options.scheduler.unit");
		int looptime = BUtil.getConversion(unit, "options.scheduler.time");
		Bukkit.getScheduler().scheduleSyncRepeatingTask(BlivUtils.getInstance(), new Runnable()
		{
			public void run()
			{
				for (Player p : Bukkit.getServer().getOnlinePlayers())
				{
					PermissionUser user = pex.getUser(p);
					String rankString = BUtil.getActiveRanks(p.getName());
					if (rankString.length() != 0)
					{
						String ranks[] = rankString.split(",");
						for(String rank : ranks)
						{
							if ((Integer.parseInt(user.getOption("group-" + rank + "-until", null)) < ((int) (System.currentTimeMillis() / 1000L))))
							{
								user.removeGroup(rank, null);
								user.setOption("group-" + rank + "-until", null); // Reset the option's value so that the scheduler doesn't get past the first if.
								if(rank.equals("EnderRank"))
								{
									String EnderRankValue = user.getOption("EnderRankValue");
									BUtil.wipePackages(p.getName());
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
											Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "enderperm remove " + p.getName() + " EnderDragon");
											break;
										case "3":
											rank = "Wither";
											if(user.getPrefix().equals("&7[&8Wither&7] "))
											{
												user.setPrefix("", null);
											}
											user.setOption("EnderRankValue", null);
											Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "enderperm remove " + p.getName() + " Wither");
											break;
										default:
											break;
									}
								}
								Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"mail send " + p.getName() + " Your " + rank + " has EXPIRED!");
								BUtil.printInfo(p, ChatColor.GOLD + "Your " + rank + " has " + ChatColor.DARK_RED + "EXPIRED!");
								BUtil.logInfo("Player " + p.getName() + "'s " + rank + " has EXPIRED!");
							}
						}
						
					}
				}
			}
		}, 0L, looptime);
	}
	
	public static void doScheduledCommands()
	{
		
		Bukkit.getScheduler().runTaskTimerAsynchronously(BlivUtils.getInstance(), new Runnable()
		{
			public void run()
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
			}
		}, 18000L, 18000L /*200L*/); //5 Minutes
	}
	
	public void doVoteRewardCheck()
	{
		scheduler.scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				for(String player : voteMan.getUnclaimedRewards())
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

	public Permission setupPermissions()
	{
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		Permission perms = rsp.getProvider();
		return perms;
	}

	public Economy setupEconomy()
	{
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp != null)
			;
		Economy econ = rsp.getProvider();
		return econ;
	}

}
