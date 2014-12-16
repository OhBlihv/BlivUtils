package net.auscraft.BlivUtils.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Logger;

import net.auscraft.BlivUtils.BlivUtils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigSetup {
	
	private Logger log;
	private FileConfiguration Config = null;
	private File ConfigFile = null;
	private BlivUtils plugin;

	public ConfigSetup(BlivUtils instance)
	{
		plugin = instance;
		log = plugin.getLogger();
	}
	
	// Config Setup Time

		public boolean setupConfig()
		{
			boolean success = false;
			FileConfiguration a = getConfig();
			saveDefaultConfig();
			
			//Setting up default.yml
			//File f = new File(plugin.getDataFolder() + "default.yml");
			//YamlConfiguration yamlFile = YamlConfiguration.loadConfiguration(f);
			
			/*try 
			{
				yamlFile.save(f);
			}
			catch(IOException e) 
			{
				 e.printStackTrace();
			}*/
			
			if (a != null) 
			{
				success = true;
			}
			return success;
		}

		public FileConfiguration getConfig()
		{
			if (Config == null) {
				reloadConfig();
			}
			return Config;
		}

		public void saveConfig() {
			if (Config == null || ConfigFile == null) {
				return;
			}
			try 
			{
				getConfig().save(ConfigFile);
			}
			catch (IOException ex) 
			{
				plugin.getLogger().severe("Could not save config to " + ConfigFile + ex);
			}
		}

		public void saveDefaultConfig()
		{
			if (ConfigFile == null) {
				ConfigFile = new File(plugin.getDataFolder(), "config.yml");
			}
			if (!ConfigFile.exists()) {
				plugin.saveResource("config.yml", false);
			}
		}

		public void reloadConfig() 
		{
			if (ConfigFile == null)
			{
				ConfigFile = new File(plugin.getDataFolder(), "config.yml");
			}
			Config = YamlConfiguration.loadConfiguration(ConfigFile);

			// Look for defaults in the jar
			Reader defConfigStream = new InputStreamReader(plugin.getResource("config.yml"));
			if (defConfigStream != null)
			{
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				Config.setDefaults(defConfig);
			}
		}
		
		public void setValue(String path, String value)
		{
			this.getConfig().set(path, value);
			try
			{
				this.getConfig().save(ConfigFile);
				log.info("Config Saved.");
			}
			catch(IOException e)
			{
				log.severe("Error saving config");
				e.printStackTrace();
			}
		}
}		