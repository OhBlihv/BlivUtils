package net.auscraft.BlivUtils.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import net.auscraft.BlivUtils.BlivUtils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FlatFile 
{

	private File saveFile = null;
	private FileConfiguration save = null;
	private BlivUtils instance;
	private String fileName;
	
	public FlatFile(BlivUtils instance, String fileName)
	{
		this.instance = instance;
		this.fileName = fileName;
		saveDefaultConfig();
		getSave();
	}
	
	public FileConfiguration getSave() 
	{
	    if (save == null)
	    {
	        reloadFile();
	    }
	    return save;
	}
	
	public void reloadFile() 
	{
	    if (saveFile == null) 
	    {
	    	saveFile = new File(instance.getDataFolder(), fileName);
	    }
	    save = YamlConfiguration.loadConfiguration(saveFile);
	 
	    // Look for defaults in the jar
	    Reader defConfigStream = null;
		try 
		{
			defConfigStream = new InputStreamReader(instance.getResource(fileName), "UTF8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
	    if (defConfigStream != null)
	    {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        save.setDefaults(defConfig);
	    }
	}
	
	public void saveDefaultConfig() 
	{
	    if (saveFile == null)
	    {
	        saveFile = new File(instance.getDataFolder(), fileName);
	    }
	    if (!saveFile.exists())
	    {            
	         instance.saveResource(fileName, false);
	    }
	}
	
	public void saveToFile() 
	{
	    if (save == null || saveFile == null) 
	    {
	        return;
	    }
	    try
	    {
	        getSave().save(saveFile);
	    }
	    catch (IOException ex) 
	    {
	        instance.getLogger().log(Level.SEVERE, "Could not save config to " + saveFile, ex);
	    }
	}
	
	public void removeEntry(String path)
	{
		this.getSave().set(path, null);
	}
	
	public void saveEntry(String path, String entry)
	{
		this.getSave().set(path, entry);
		saveToFile();
	}

	public String loadEntry(String path)
	{
		String value = this.save.getString(path);
		return value;
	}
	
	public Set<String> getChildren(String path)
	{
		Set<String> children = this.save.getConfigurationSection(path).getKeys(true);
		return children;
	}
	
	public String getString(String path)
	{
		String values = this.save.getString(path);
		return values;
	}
	
	public List<String> getStringList(String path)
	{
		List<String> values = this.save.getStringList(path);
		return values;
	}
	
	public int getInt(String path)
	{
		int value = this.save.getInt(path);
		return value;
	}
	
	public boolean getBoolean(String path)
	{
		boolean value = this.save.getBoolean(path);
		return value;
	}
	
	public long getLong(String path)
	{
		long value = this.save.getLong(path);
		return value;
	}
	
	public void saveValue(String path, Object value)
	{
		this.save.set(path, value);
		saveToFile();
	}

	public double getDouble(String path)
	{
		double value = this.save.getDouble(path);
		return value;
	}
	
}
