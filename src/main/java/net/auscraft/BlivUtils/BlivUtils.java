package net.auscraft.BlivUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import org.bukkit.scheduler.BukkitScheduler;

public final class BlivUtils extends JavaPlugin {
	
    private Logger log;
    private static BlivUtils plugin;
    private static HashMap<String, Integer> promoteCount = new HashMap<String, Integer>();
    private static HashMap<String, String> colourSave = new HashMap<String, String>();
    private static PermissionManager pex;
	private FileConfiguration Config = null;
	private File ConfigFile = null;
    
    @Override
    public void onEnable()
    {
    	//Command Declaration
    	getCommand("bu").setExecutor(new GenericExecuter());
        getCommand("rank").setExecutor(new RankHelpExecuter());
        getCommand("say").setExecutor(new GenericExecuter());
        getCommand("wstop").setExecutor(new GenericExecuter());
        getCommand("sudo").setExecutor(new GenericExecuter());
        getCommand("buyrank").setExecutor(new PromoteExecuter(this));
        getCommand("promoadmin").setExecutor(new PromoteExecuter(this));
        getCommand("promoteme").setExecutor(new PromoteExecuter(this));
        getCommand("timeleft").setExecutor(new PromoteExecuter(this));
        getCommand("chat").setExecutor(new ColourExecutor(this));
        getCommand("colourme").setExecutor(new ColourExecutor(this));
        //Other Stuff
        log = getLogger();
        pex = PermissionsEx.getPermissionManager();
        plugin = this;
        setupConfig();
        //Scheduling
        checkScheduler();
    }

    @Override
	public void onDisable()
    {
    }
    
    public void checkScheduler() {
    	int enabled = getInt("options.scheduler.enabled");
    	//log.info("Scheduler enabled: " + enabled);
        if((enabled != -1) && (enabled != 0)) {
        	log.info("Rank Checking Scheduler enabled");
        	startScheduler();
        }
        else {
        	log.info("Rank Checking Scheduler disabled");
        }
    }
    
    private void startScheduler() {
    	BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
    	String unit = getString("options.scheduler.unit");
    	int looptime = getConversion(unit);
    	scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
    		public void run() {
    			for(Player p : getServer().getOnlinePlayers()) {
    				PermissionUser entity = pex.getUser(p);
    				String rank = getActiveRank(p.getName());
    				//log.info("Rank = " + rank);
    				if(rank != "") {
    					//Debug. Hope this doesn't end up in the final.
        				//if((Integer.parseInt(entity.getOption("group-" + rank + "-until", null)) > (((int) (System.currentTimeMillis() / 1000L))))) {
    					//	log.info("Time left of " + rank + " for " + p.getName() + ": " + ((Integer.parseInt(entity.getOption("group-" + rank + "-until", null))) - ((int) (System.currentTimeMillis() / 1000L))));
    					//}
        				if((Integer.parseInt(entity.getOption("group-" + rank + "-until", null)) < (((int) (System.currentTimeMillis() / 1000L))))) {
    						entity.removeGroup(rank, null);
    						entity.setOption("group-" + rank + "-until", ""); // Reset the option's value so that the scheduler doesn't get past the first if.
    						Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mail send " + p.getName() + " Your " + rank + " has EXPIRED!");
    					    p.sendMessage(ChatColor.GOLD + "Your " + rank + " has " + ChatColor.DARK_RED + "EXPIRED!");
    						log.info("Player " + p.getName() + "'s " + rank + " has EXPIRED! This means it's working!");
    					}
    				}
    			}
    		}
        }, 0L, looptime);
    }
    
    public void logtoFile(String message) {
           try {
                File dataFolder = getDataFolder();
                if(!dataFolder.exists()) {
                    dataFolder.mkdir();
                }
                File saveTo = new File(getDataFolder(), "log.txt");
                if (!saveTo.exists()) {
                    saveTo.createNewFile();
                }
                FileWriter fw = new FileWriter(saveTo, true);
                PrintWriter pw = new PrintWriter(fw);
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
         	    Date date = new Date();
         	    System.out.println();
                pw.println("[" + dateFormat.format(date) + "] " + message);
                pw.flush();
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    
    private int getConversion(String unit) {
    	int conversion;
    	int time = getInt("options.scheduler.time");
    	if(unit .equals("seconds")) {
    		conversion = time * 20;
    		return conversion;
    	}
    	else if(unit .equals("minutes")) {
    		conversion = time * 60 * 20;
    		return conversion;
    	}
    	else if(unit .equals("hours")) {
    		conversion = time * 60 * 60 * 20;
    		return conversion;
    	}
    	else {
    		conversion = 72000;
    		//log.info("Chosen path: No Proper Unit!");
    		return conversion;
    	}

    }

    public Permission setupPermissions()
    {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        Permission perms = (Permission)rsp.getProvider();
        return perms;
    }

    public Economy setupEconomy()
    {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp != null);
        Economy econ = (Economy)rsp.getProvider();
        return econ;
    }

    public static HashMap<String, Integer> getPromote()
    {
        return promoteCount;
    }
    
    public static HashMap<String, String> getSuffixColour()
    {
        return colourSave;
    }

    public static BlivUtils getPlugin()
    {
        return plugin;
    }
    
    public static int getTimeLeft(String playerName, String rank) {
    	PermissionUser entity = pex.getUser(playerName);
    	if((entity.getOption("group-" + rank + "-until") != null) && (entity.getOption("group-" + rank + "-until") != "")) {
    		int timeleft = ((Integer.parseInt(entity.getOption("group-" + rank + "-until", null))) - ((int) (System.currentTimeMillis() / 1000L)));
    		return timeleft;
    	}
    	else {
    		return -1;
    	}
    }

    
    public static String getActiveRank(String playerName) {
    	PermissionUser entity = pex.getUser(playerName);
    	String rank = "";
    	if((entity.getOption("group-Endermite-until") != null) && (entity.getOption("group-Endermite-until") != "")) {
    		rank = "Endermite";
    	}
    	else if((entity.getOption("group-Enderman-until") != null) && (entity.getOption("group-Enderman-until") != "")) {
    		rank = "Enderman";
    	}
    	else if((entity.getOption("group-Enderdragon-until") != null) && (entity.getOption("group-Enderdragon-until") != "")) {
    		rank = "Enderdragon";
    	}
    	else if((entity.getOption("group-Admin-until") != null) && (entity.getOption("group-Admin-until") != "")) {
    		rank = "Admin";
    	}
    	else {
    		rank = "";
    	}
    	return rank;
    }
    
    //Config Setup Time
    
    public boolean setupConfig() {
		boolean success = false;
		FileConfiguration a = getConfig();
		saveDefaultConfig();
		if(a != null) {
			success = true;
		}
		return success;
	}
	
	public void setValue(String path, String value) {
		this.getConfig().set(path, value);
	}
	
	public int getInt(String path) {
		int value = this.getConfig().getInt(path);
		return value;
	}
	
	public String getString(String path) {
		String value = this.getConfig().getString(path);
		return value;
	}
	
	public boolean getBoolean(String path) {
		boolean value = this.getConfig().getBoolean(path);
		return value;
	}
	
	public FileConfiguration getConfig() {
	    if (Config == null) {
	        reloadConfig();
	    }
	    return Config;
	}
	
	public void saveConfig() {
	    if (Config == null || ConfigFile == null) {
	        return;
	    }
	    try {
	        getConfig().save(ConfigFile);
	    } catch (IOException ex) {
	        plugin.log.severe("Could not save config to " + ConfigFile + ex);
	    }
	}
	
	public void saveDefaultConfig() {
	    if (ConfigFile == null) {
	        ConfigFile = new File(plugin.getDataFolder(), "config.yml");
	    }
	    if (!ConfigFile.exists()) {            
	         plugin.saveResource("config.yml", false);
	     }
	}
	
	public void reloadConfig() {
	    if (ConfigFile == null) {
	    ConfigFile = new File(plugin.getDataFolder(), "config.yml");
	    }
	    Config = YamlConfiguration.loadConfiguration(ConfigFile);
	 
	    // Look for defaults in the jar
	    Reader defConfigStream = new InputStreamReader(plugin.getResource("config.yml"));
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        Config.setDefaults(defConfig);
	    }
	}

}
