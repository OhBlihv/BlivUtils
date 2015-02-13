package net.auscraft.BlivUtils.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigSetup {
	
	private FileConfiguration Config = null;
	private File ConfigFile = null;
	private BlivUtils b;
	private Utilities util;

	public ConfigSetup(BlivUtils instance)
	{
		b = instance;
		util = instance.getUtil();
	}
	
	// Config Setup Time

		public boolean setupConfig()
		{
			boolean success = false;
			FileConfiguration a = getConfig();
			saveDefaultConfig();
			
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
				util.logSevere("Could not save config to " + ConfigFile + ex);
			}
		}

		public void saveDefaultConfig()
		{
			if (ConfigFile == null) {
				ConfigFile = new File(b.getDataFolder(), "config.yml");
			}
			if (!ConfigFile.exists()) {
				b.saveResource("config.yml", false);
			}
		}

		public void reloadConfig() 
		{
			if (ConfigFile == null)
			{
				ConfigFile = new File(b.getDataFolder(), "config.yml");
			}
			Config = YamlConfiguration.loadConfiguration(ConfigFile);

			// Look for defaults in the jar
			Reader defConfigStream = new InputStreamReader(b.getResource("config.yml"));
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
				util.logInfo("Config Saved.");
			}
			catch(IOException e)
			{
				util.logSevere("Error saving config");
				e.printStackTrace();
			}
		}
}		