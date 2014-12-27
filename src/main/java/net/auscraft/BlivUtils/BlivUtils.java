package net.auscraft.BlivUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.auscraft.BlivUtils.config.ConfigAccessor;
import net.auscraft.BlivUtils.config.ConfigSetup;
import net.auscraft.BlivUtils.executors.ColourExecutor;
import net.auscraft.BlivUtils.executors.CreditExecutor;
import net.auscraft.BlivUtils.executors.GenericExecutor;
import net.auscraft.BlivUtils.executors.RankHelpExecutor;
import net.auscraft.BlivUtils.executors.VoteExecuter;
import net.auscraft.BlivUtils.promotions.PromoteExecuter;
import net.auscraft.BlivUtils.purchases.Broadcast;
import net.auscraft.BlivUtils.rewards.ChristmasExecutor;
import net.auscraft.BlivUtils.timed.TimedCommands;
import net.auscraft.BlivUtils.utils.Utilities;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import org.bukkit.scheduler.BukkitScheduler;

public final class BlivUtils extends JavaPlugin
{
	private static BlivUtils plugin;
	private static HashMap<String, Integer> promoteCount = new HashMap<String, Integer>();
	private static HashMap<String, String> colourSave = new HashMap<String, String>();
	private static PermissionManager pex;
	private ConfigSetup configSetup;
	private ConfigAccessor cfg;
	private Utilities util;
	//private FileConfiguration configfile = null;


	@Override
	public void onEnable()
	{
		//Get in there early to claim the economy!
		
		// Other Stuff
		pex = PermissionsEx.getPermissionManager();
		plugin = this;
		util = new Utilities(this);
		configSetup = new ConfigSetup(this);
		//configfile = configSetup.getConfig();
		cfg = new ConfigAccessor(this);
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
		getCommand("voteprint").setExecutor(new VoteExecuter(this));
		getCommand("timedadd").setExecutor(new TimedCommands(this));
		getCommand("timeleft").setExecutor(new PromoteExecuter(this));
		getCommand("prefix").setExecutor(new PromoteExecuter(this));
		getCommand("chat").setExecutor(new ColourExecutor(this));
		getCommand("colourme").setExecutor(new ColourExecutor(this));
		getCommand("promoadmin").setExecutor(new PromoteExecuter(this));
		getCommand("updateadmin").setExecutor(new PromoteExecuter(this));
		getCommand("updatetime").setExecutor(new PromoteExecuter(this));
		
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
			util.logInfo("Rank Purchasing Disabled.");
		}
		
		if(toggle[1] == 1)
		{
			getCommand("credits").setExecutor(new CreditExecutor(this));
			util.logInfo("Credit System Enabled.");
		}
		else
		{
			util.logInfo("Credits System Disabled.");
		}
		
		if(toggle[2] == 1)
		{
			getCommand("present").setExecutor(new ChristmasExecutor(this));
			util.logInfo("Rewards Enabled...");
		}
		else
		{
			util.logInfo("Rewards Disabled.");
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
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		String unit = cfg.getString("options.scheduler.unit");
		int looptime = util.getConversion(unit, "options.scheduler.time");
		scheduler.scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				for (Player p : getServer().getOnlinePlayers())
				{
					PermissionUser entity = pex.getUser(p);
					String rank = util.getActiveRank(p.getName());
					if (rank != "")
					{
						if ((Integer.parseInt(entity.getOption("group-" + rank + "-until", null)) < (((int) (System.currentTimeMillis() / 1000L)))))
						{
							entity.removeGroup(rank, null);
							entity.setOption("group-" + rank + "-until", ""); // Reset the option's value so that the scheduler doesn't get past the first if.
							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"mail send " + p.getName() + " Your " + rank + " has EXPIRED!");
							//p.sendMessage(ChatColor.GOLD + "Your " + rank + " has " + ChatColor.DARK_RED + "EXPIRED!");
							util.printInfo(p, ChatColor.GOLD + "Your " + rank + " has " + ChatColor.DARK_RED + "EXPIRED!");
							util.logInfo("Player " + p.getName() + "'s " + rank + " has EXPIRED!");
						}
					}
				}
			}
		}, 0L, looptime);
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

	public static BlivUtils getPlugin()
	{
		return plugin;
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
