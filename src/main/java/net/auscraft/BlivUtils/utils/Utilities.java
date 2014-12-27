package net.auscraft.BlivUtils.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import net.auscraft.BlivUtils.BlivUtils;

public class Utilities {
	
	private BlivUtils b;
	private Logger log;
	
	public Utilities(BlivUtils instance)
	{
		b = instance;
		log = b.getLogger();
	}
	
	//------------------------------------------------------------------------------------------------------
	//String Translation
	//------------------------------------------------------------------------------------------------------
	
	public String translateColours(String toFix)
	{
		Pattern chatColorPattern = Pattern.compile("(?i)&([0-9A-Fa-f-l-oL-OrR])"); // Credit to t3hk0d3 in ChatManager(With slight edits)
		String fixedString = chatColorPattern.matcher(toFix).replaceAll("\u00A7$1"); // And here too
		return fixedString;
	}
	
	//------------------------------------------------------------------------------------------------------
	//Logging
	//------------------------------------------------------------------------------------------------------
	
	public void logtoFile(String message)
	{
		try
		{
			File dataFolder = b.getDataFolder();

			if (!dataFolder.exists())
			{
				dataFolder.mkdir();
			}

			File saveTo = new File(b.getDataFolder(), "log.txt");

			if (!saveTo.exists())
			{
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
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void logReward(String message)
	{
		try
		{
			File dataFolder = b.getDataFolder();

			if (!dataFolder.exists())
			{
				dataFolder.mkdir();
			}

			File saveTo = new File(b.getDataFolder(), "rewardslog.txt");

			if (!saveTo.exists())
			{
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
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	//------------------------------------------------------------------------------------------------------
	//Permissions
	//------------------------------------------------------------------------------------------------------
	
	public int getTimeLeft(String playerName, String rank)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		if ((user.getOption("group-" + rank + "-until") != null) && (user.getOption("group-" + rank + "-until") != "")) 
		{
			int timeleft = ((Integer.parseInt(user.getOption("group-" + rank
					+ "-until", null))) - ((int) (System.currentTimeMillis() / 1000L)));
			return timeleft;
		} 
		else 
		{
			return -1;
		}
	}
	
	public String getActiveRank(String playerName)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		String rank = "";
		if ((user.getOption("group-Endermite-until") != null) && (user.getOption("group-Endermite-until") != ""))
		{
			rank = "Endermite";
		} 
		else if ((user.getOption("group-Enderman-until") != null) && (user.getOption("group-Enderman-until") != ""))
		{
			rank = "Enderman";
		} 
		else if ((user.getOption("group-Enderdragon-until") != null) && (user.getOption("group-Enderdragon-until") != ""))
		{
			rank = "Enderdragon";
		} 
		else if ((user.getOption("group-Admin-until") != null) && (user.getOption("group-Admin-until") != ""))
		{
			rank = "Admin";
		} 
		else {
			rank = "";
		}
		return rank;
	}
	
	public void checkRankScheduler() {
		int enabled = b.getCfg().getInt("options.scheduler.enabled");
		if ((enabled != -1) && (enabled != 0))
		{
			log.info("Rank Checking Scheduler enabled");
			b.startRankScheduler();
		}
		else
		{
			log.info("Rank Checking Scheduler disabled");
		}
	}
	
	public int getConversion(String unit, String yamlStructure)
	{
		int conversion;
		int time = b.getCfg().getInt(yamlStructure);
		if (unit.equals("seconds"))
		{
			conversion = time * 20;
			return conversion;
		} 
		else if (unit.equals("minutes"))
		{
			conversion = time * 60 * 20;
			return conversion;
		} 
		else if (unit.equals("hours"))
		{
			conversion = time * 60 * 60 * 20;
			return conversion;
		} 
		else
		{
			conversion = 72000;
			return conversion;
		}

	}
	
	//------------------------------------------------------------------------------------------------------
	//Printing
	//------------------------------------------------------------------------------------------------------
	
	public void printSuccess(CommandSender sender, String message)
	{
		sender.sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "SUCCESS: " + ChatColor.GREEN + message);
	}
	
	public void printInfo(CommandSender sender, String message)
	{
		sender.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "INFO: " + ChatColor.BLUE + message);
	}
	
	public void printError(CommandSender sender, String message)
	{
		sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "ERROR: " + ChatColor.RED + message);
	}
	
	//------------------------------------------------------------------------------------------------------
	//Logging
	//------------------------------------------------------------------------------------------------------
	
	public void logSuccess(String message)
	{
		log.log(Level.INFO, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "SUCCESS: " + ChatColor.GREEN + message);
	}
	
	public void logInfo(String message)
	{
		log.log(Level.INFO, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "INFO: " + ChatColor.BLUE + message);
	}
	
	public void logError(String message)
	{
		log.log(Level.WARNING, ChatColor.DARK_RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "ERROR: " + ChatColor.RED + message);
	}
	
	public void logSevere(String message)
	{
		log.log(Level.SEVERE, ChatColor.DARK_RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "SEVERE: " + ChatColor.RED + message);
	}
	
}
