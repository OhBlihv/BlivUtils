package net.auscraft.BlivUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.auscraft.BlivUtils.config.ConfigAccessor;
import net.auscraft.BlivUtils.config.ConfigSetup;
import net.auscraft.BlivUtils.credits.CreditManager;
import net.auscraft.BlivUtils.executors.ColourExecutor;
import net.auscraft.BlivUtils.executors.GenericExecutor;
import net.auscraft.BlivUtils.executors.RankHelpExecutor;
import net.auscraft.BlivUtils.listeners.DeathListener;
import net.auscraft.BlivUtils.listeners.HealthListener;
import net.auscraft.BlivUtils.listeners.HubListener;
import net.auscraft.BlivUtils.listeners.XPListener;
import net.auscraft.BlivUtils.promotions.PromoteExecuter;
import net.auscraft.BlivUtils.purchases.Broadcast;
import net.auscraft.BlivUtils.purchases.Ender;
import net.auscraft.BlivUtils.rewards.Rewards;
import net.auscraft.BlivUtils.timed.TimedCommands;
import net.auscraft.BlivUtils.utils.Utilities;
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
	private static HashMap<String, Integer> promoteCount = new HashMap<String, Integer>();
	private static HashMap<String, String> colourSave = new HashMap<String, String>();
	
	//Ender Rank bonuses (Temporary Storage)
	private static HashMap<String, Boolean> xpClaim = new HashMap<String, Boolean>();
	private static HashMap<String, Boolean> fixClaim = new HashMap<String, Boolean>();
	
	private static PermissionManager pex;
	private ConfigSetup configSetup;
	private ConfigAccessor cfg;
	private Utilities util;
	private FileConfiguration configFile;
	private VoteManager voteMan;
	BukkitScheduler scheduler = Bukkit.getServer().getScheduler();


	@Override
	public void onEnable()
	{
		//Get in there early to claim the economy!
		
		// Other Stuff
		pex = PermissionsEx.getPermissionManager();
		util = new Utilities(this);
		configSetup = new ConfigSetup(this);
		configFile = configSetup.getConfig();
		cfg = new ConfigAccessor(this);
		voteMan = new VoteManager(this);
		// Scheduling
		util.checkRankScheduler();
		//MySQL Setup
		setupMySQL();
		//loadRewards();
		
		
		// Some of the commands require the above set up before they can appear to function.
		// Command Declaration
		
		//Always enabled commands
		getCommand("bu").setExecutor(new GenericExecutor(this));
		getCommand("rank").setExecutor(new RankHelpExecutor(this));
		getCommand("say").setExecutor(new GenericExecutor(this));
		getCommand("wstop").setExecutor(new GenericExecutor(this));
		getCommand("servers").setExecutor(new GenericExecutor(this));
		getCommand("purch").setExecutor(new Broadcast(this));
		getCommand("lore").setExecutor(new Ender(this));
		getCommand("xpClaim").setExecutor(new Ender(this));
		getCommand("fixClaim").setExecutor(new Ender(this));
		getCommand("enderperm").setExecutor(new Ender(this));
		getCommand("enderrank").setExecutor(new Ender(this));
		getCommand("health").setExecutor(new HealthListener(this));
		getCommand("voteprint").setExecutor(new Vote(this));
		getCommand("voteclaim").setExecutor(new VoteManager(this));
		getCommand("voteparty").setExecutor(new Vote(this));
		getCommand("timedadd").setExecutor(new TimedCommands(this));
		getCommand("timeleft").setExecutor(new PromoteExecuter(this));
		getCommand("prefix").setExecutor(new PromoteExecuter(this));
		getCommand("chat").setExecutor(new ColourExecutor(this));
		getCommand("colourme").setExecutor(new ColourExecutor(this));
		getCommand("promoadmin").setExecutor(new PromoteExecuter(this));
		getCommand("updateadmin").setExecutor(new PromoteExecuter(this));
		getCommand("updatetime").setExecutor(new PromoteExecuter(this));
		
		//Listeners
		getServer().getPluginManager().registerEvents(new HubListener(), this);
		getServer().getPluginManager().registerEvents(new XPListener(this), this);
		getServer().getPluginManager().registerEvents(new DeathListener(this), this);
		getServer().getPluginManager().registerEvents(new HealthListener(this), this);
		
		//Toggleable commands
		int toggle[] = cfg.getEnabledCommands();
		
		if(toggle[0] == 1)
		{
			getCommand("buyrank").setExecutor(new PromoteExecuter(this));
			getCommand("promoteme").setExecutor(new PromoteExecuter(this));
			util.logInfo("Rank Purchasing Enabled.");
		}
		else
		{
			getCommand("buyrank").setExecutor(new RemovedCommand(this));
			getCommand("promoteme").setExecutor(new RemovedCommand(this));
			util.logInfo("Rank Purchasing Disabled.");
		}
		
		if(toggle[1] == 1)
		{
			getCommand("credits").setExecutor(new CreditManager(this));
			util.logInfo("Credit System Enabled.");
		}
		else
		{
			getCommand("credits").setExecutor(new RemovedCommand(this));
			util.logInfo("Credits System Disabled.");
		}
		
		if(toggle[2] == 1)
		{
			getCommand("present").setExecutor(new Rewards(this));
			util.logInfo("Rewards Enabled...");
		}
		else
		{
			getCommand("present").setExecutor(new RemovedCommand(this));
			util.logInfo("Rewards Disabled.");
		}
		
		//Vote Reward checker
		if(this.getCfg().getInt("options.voting.rewards") == 1)
		{
			this.doVoteRewardCheck();
		}
		else
		{
			util.logInfo("Voting Rewards disabled.");
		}
		
	}

	@Override
	public void onDisable()
	{
		util.logInfo("BlivUtils disabling...");
	}
	
	//Setup for MySQL
	private void setupMySQL()
	{
		//Read these in from the config so I dont have to put the database info on GitHub :L
		String user = cfg.getString("options.mysql.user");
		String pass = cfg.getString("options.mysql.pass");
		String url = cfg.getString("options.mysql.url");
		
		String setup = cfg.getString("options.mysql.setup");
		if(setup.equals("0"))
		{
			util.logInfo("Table not set up. First run.");
			//Set the setup variable to 1, so it attempts to setup the CreditsDB table
			configSetup.setValue("options.mysql.setup", "1");
			util.logInfo("setup should now be 1");
		}
		else if(setup.equals("1"))
		{
			try
			{
				util.logInfo("Setting up Credits Table...");
				//Initial creation of Table
				Connection conn = DriverManager.getConnection(url, user, pass);
				PreparedStatement sampleQueryStatement = conn.prepareStatement("CREATE TABLE IF NOT EXISTS CreditsDB (playerName VARCHAR(50) PRIMARY KEY, value INT)");
				sampleQueryStatement.executeUpdate();
				sampleQueryStatement.close();
				conn.close();
				util.logInfo("Successfully set up Credits Table");
				//Set the setup variable to 2, so it doesn't attempt to setup the CreditsDB table anymore.
				configSetup.setValue("options.mysql.setup", "2");
			}
			catch(SQLException e)
			{
				e.printStackTrace(); //For now, I'll fix this later.
				util.logSevere("Failed setting up Credits Table");
				util.logSevere("Disabling...");
				Bukkit.getPluginManager().disablePlugin(this);
			}
		}
		else  //Setup = 2
		{
			util.logInfo("Credits table already set up. Skipping...");
		}

		
	}

	public void startRankScheduler()
	{
		String unit = cfg.getString("options.scheduler.unit");
		int looptime = util.getConversion(unit, "options.scheduler.time");
		scheduler.scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				for (Player p : getServer().getOnlinePlayers())
				{
					PermissionUser user = pex.getUser(p);
					String rankString = util.getActiveRanks(p.getName());
					if (rankString != "")
					{
						String ranks[] = rankString.split(",");
						for(String rank : ranks)
						{
							if ((Integer.parseInt(user.getOption("group-" + rank + "-until", null)) < ((int) (System.currentTimeMillis() / 1000L))))
							{
								user.removeGroup(rank, null);
								user.setOption("group-" + rank + "-until", ""); // Reset the option's value so that the scheduler doesn't get past the first if.
								if(rank.equals("EnderRank"))
								{
									String EnderRankValue = user.getOption("EnderRankValue");
									util.wipePackages(p.getName());
									switch(EnderRankValue)
									{
										case "1":
											rank = "Enderman";
											break;
										case "2":
											rank = "EnderDragon";
											Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "enderperm remove " + p.getName() + " EnderDragon");
											break;
										case "3":
											rank = "Wither";
											Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "enderperm remove " + p.getName() + " Wither");
											break;
										default:
											break;
									}
								}
								Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"mail send " + p.getName() + " Your " + rank + " has EXPIRED!");
								util.printInfo(p, ChatColor.GOLD + "Your " + rank + " has " + ChatColor.DARK_RED + "EXPIRED!");
								util.logInfo("Player " + p.getName() + "'s " + rank + " has EXPIRED!");
							}
						}
						
					}
				}
			}
		}, 0L, looptime);
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
						util.printInfo(Bukkit.getPlayer(player), ChatColor.GREEN + "You have an unclaimed voting reward! Type " + ChatColor.AQUA + "/voteclaim" + ChatColor.GREEN + " to claim");
					}
					catch(NullPointerException e)
					{
						//
					}
				}
			}
		}, 0L, /*6000L*/ 200L); //5 Minutes
	}

	public Permission setupPermissions()
	{
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		Permission perms = (Permission) rsp.getProvider();
		return perms;
	}

	public Economy setupEconomy()
	{
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp != null)
			;
		Economy econ = (Economy) rsp.getProvider();
		return econ;
	}

	public static HashMap<String, Integer> getPromote()
	{
		return promoteCount;
	}

	public HashMap<String, String> getSuffixColour() 
	{
		return colourSave;
	}
	
	public HashMap<String, Boolean> getXPClaim() 
	{
		return xpClaim;
	}
	
	public HashMap<String, Boolean> getFixClaim() 
	{
		return fixClaim;
	}

	public BlivUtils getPlugin()
	{
		return this;
	}
	
	public VoteManager getVoteManager()
	{
		return voteMan;
	}
	
	public ConfigSetup getConfigSetup()
	{
		return configSetup;
	}
	
	public ConfigAccessor getCfg()
	{
		return cfg;
	}
	
	public Utilities getUtil()
	{
		return util;
	}

}
