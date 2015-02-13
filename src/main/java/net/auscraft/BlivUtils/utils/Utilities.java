package net.auscraft.BlivUtils.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import net.auscraft.BlivUtils.BlivUtils;

public class Utilities {
	
	private BlivUtils b;
	private final String prefix = ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "BlivUtils" + ChatColor.WHITE + "] ";
	
	public Utilities(BlivUtils instance)
	{
		b = instance;
	}
	
	//------------------------------------------------------------------------------------------------------
	//String Translation
	//------------------------------------------------------------------------------------------------------
	
	public String translateColours(String toFix)
	{
		//Convert every single colour code and formatting code, excluding 'magic' (&k), capitals and lowercase are converted.
		Pattern chatColorPattern = Pattern.compile("(?i)&([0-9A-Fa-f-l-oL-OrR])"); // Credit to t3hk0d3 in ChatManager(With slight edits)
		String fixedString = chatColorPattern.matcher(toFix).replaceAll("\u00A7$1"); // And here too
		return fixedString;
	}
	
	//------------------------------------------------------------------------------------------------------
	//Logging
	//------------------------------------------------------------------------------------------------------
	
	public void logtoFile(String message, String logName)
	{
		try
		{
			File dataFolder = b.getDataFolder();

			if (!dataFolder.exists())
			{
				dataFolder.mkdir();
			}
			
			File saveTo = null;
			if(logName != null)
			{
				saveTo = new File(b.getDataFolder(), logName + ".txt");
			}
			else
			{
				saveTo = new File(b.getDataFolder(), "log.txt");
			}

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
			int timeleft = ((Integer.parseInt(user.getOption("group-" + rank + "-until", null))) - ((int) (System.currentTimeMillis() / 1000L)));
			return timeleft;
		} 
		else
		{
			return -1;
		}
	}
	
	public int getRankTime(String playerName, String rank)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		if ((user.getOption("group-" + rank + "-until") != null) && (user.getOption("group-" + rank + "-until") != "")) 
		{
			return Integer.parseInt(user.getOption("group-" + rank + "-until", null));
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
		if ((user.getOption("group-Admin-until") != null) && (user.getOption("group-Admin-until") != ""))
		{
			rank = "Admin";
		}
		else if ((user.getOption("group-EnderDragon-until") != null) && (user.getOption("group-EnderDragon-until") != ""))
		{
			rank = "EnderDragon";
		}
		else if ((user.getOption("group-Enderman-until") != null) && (user.getOption("group-Enderman-until") != ""))
		{
			rank = "Enderman";
		} 
		else if ((user.getOption("group-Endermite-until") != null) && (user.getOption("group-Endermite-until") != ""))
		{
			rank = "Endermite";
		} 
		else
		{
			rank = "";
		}
		return rank;
	}
	
	public String getActiveRanks(String playerName)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		String ranks = "";
		if ((user.getOption("group-Admin-until") != null) && (user.getOption("group-Admin-until") != ""))
		{
			ranks += "Admin,";
		}
		if ((user.getOption("group-EnderRank-until") != null) && (user.getOption("group-EnderRank-until") != ""))
		{
			ranks += "EnderRank,";
		}
		if ((user.getOption("group-EnderDragon-until") != null) && (user.getOption("group-EnderDragon-until") != ""))
		{
			ranks += "EnderDragon,";
		}
		if ((user.getOption("group-Enderman-until") != null) && (user.getOption("group-Enderman-until") != ""))
		{
			ranks += "Enderman,";
		} 
		if ((user.getOption("group-Endermite-until") != null) && (user.getOption("group-Endermite-until") != ""))
		{
			ranks += "Endermite,";
		}
		return ranks;
	}
	
	public String getActivePackages(String playerName)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		String packages = "";
		//Pets -----------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderPetsPassive-until") != null) && (user.getOption("group-EnderPetsPassive-until") != ""))
		{
			packages += "Passive Pets, ";
		}
		if ((user.getOption("group-EnderPetsNeutral-until") != null) && (user.getOption("group-EnderPetsNeutral-until") != ""))
		{
			packages += "Neutral Pets, ";
		}
		if ((user.getOption("group-EnderPetsHostile-until") != null) && (user.getOption("group-EnderPetsHostile-until") != ""))
		{
			packages += "Hostile Pets, ";
		}
		if ((user.getOption("group-EnderPetsALL-until") != null) && (user.getOption("group-EnderPetsALL-until") != ""))
		{
			packages += "ALL Pets, ";
		}
		//Trails ---------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderTrailsSET1-until") != null) && (user.getOption("group-EnderTrailsSET1-until") != ""))
		{
			packages += "Set 1 Trails, ";
		}
		if ((user.getOption("group-EnderTrailsSET2-until") != null) && (user.getOption("group-EnderTrailsSET2-until") != ""))
		{
			packages += "Set 2 Trails, ";
		}
		if ((user.getOption("group-EnderTrailsALL-until") != null) && (user.getOption("group-EnderTrailsALL-until") != ""))
		{
			packages += "ALL Trails, ";
		}
		//Disguises -------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderDisguisesPassive-until") != null) && (user.getOption("group-EnderDisguisesPassive-until") != ""))
		{
			packages += "Passive Mob Disguises, ";
		}
		if ((user.getOption("group-EnderDisguisesHostile-until") != null) && (user.getOption("group-EnderDisguisesHostile-until") != ""))
		{
			packages += "Hostile Mob Disguises, ";
		}
		if ((user.getOption("group-EnderDisguisesALL-until") != null) && (user.getOption("group-EnderDisguisesALL-until") != ""))
		{
			packages += "ALL Mob Disguises, ";
		}
		if ((user.getOption("group-EnderDisguisesALLEntity-until") != null) && (user.getOption("group-EnderDisguisesALLEntity-until") != ""))
		{
			packages += "ALL Mob Disguises + Entity Disguises, ";
		}
		//Cooldowns -------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderCooldowns-until") != null) && (user.getOption("group-EnderCooldowns-until") != ""))
		{
			packages += "No Cooldowns, ";
		}
		//Warps -----------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderWarps-until") != null) && (user.getOption("group-EnderWarps-until") != ""))
		{
			packages += "20 Available Warps, ";
		}
		//mcMMO -----------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-mcMMOSmall-until") != null) && (user.getOption("group-mcMMOSmall-until") != ""))
		{
			packages += "mcMMO: Small Perk Pack, ";
		}
		if ((user.getOption("group-mcMMOMedium-until") != null) && (user.getOption("group-mcMMOMedium-until") != ""))
		{
			packages += "mcMMO: Medium Perk Pack, ";
		}
		if ((user.getOption("group-mcMMOLarge-until") != null) && (user.getOption("group-mcMMOLarge-until") != ""))
		{
			packages += "mcMMO: Large Perk Pack, ";
		}

		return packages;
	}
	
	public void updatePackages(String playerName, String time)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		//Pets -----------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderPetsPassive-until") != null) && (user.getOption("group-EnderPetsPassive-until") != ""))
		{
			user.setOption("group-EnderPetsPassive-until", time);
		}
		if ((user.getOption("group-EnderPetsNeutral-until") != null) && (user.getOption("group-EnderPetsNeutral-until") != ""))
		{
			user.setOption("group-EnderPetsNeutral-until", time);
		}
		if ((user.getOption("group-EnderPetsHostile-until") != null) && (user.getOption("group-EnderPetsHostile-until") != ""))
		{
			user.setOption("group-EnderPetsHostile-until", time);
		}
		if ((user.getOption("group-EnderPetsALL-until") != null) && (user.getOption("group-EnderPetsALL-until") != ""))
		{
			user.setOption("group-EnderPetsALL-until", time);
		}
		//Trails ---------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderTrailsSET1-until") != null) && (user.getOption("group-EnderTrailsSET1-until") != ""))
		{
			user.setOption("group-EnderTrailsSET1-until", time);
		}
		if ((user.getOption("group-EnderTrailsSET2-until") != null) && (user.getOption("group-EnderTrailsSET2-until") != ""))
		{
			user.setOption("group-EnderTrailsSET2-until", time);
		}
		if ((user.getOption("group-EnderTrailsALL-until") != null) && (user.getOption("group-EnderTrailsALL-until") != ""))
		{
			user.setOption("group-EnderTrailsALL-until", time);
		}
		//Disguises -------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderDisguisesPassive-until") != null) && (user.getOption("group-EnderDisguisesPassive-until") != ""))
		{
			user.setOption("group-EnderDisguisesPassive-until", time);
		}
		if ((user.getOption("group-EnderDisguisesHostile-until") != null) && (user.getOption("group-EnderDisguisesHostile-until") != ""))
		{
			user.setOption("group-EnderDisguisesHostile-until", time);
		}
		if ((user.getOption("group-EnderDisguisesALL-until") != null) && (user.getOption("group-EnderDisguisesALL-until") != ""))
		{
			user.setOption("group-EnderDisguiseALL-until", time);
		}
		if ((user.getOption("group-EnderDisguisesALLEntity-until") != null) && (user.getOption("group-EnderDisguisesALLEntity-until") != ""))
		{
			user.setOption("group-EnderDisguisesALLEntity-until", time);
		}
		//Cooldowns -------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderCooldowns-until") != null) && (user.getOption("group-EnderCooldowns-until") != ""))
		{
			user.setOption("group-EnderCooldowns-until", time);
		}
		//Warps -----------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderWarps-until") != null) && (user.getOption("group-EnderWarps-until") != ""))
		{
			user.setOption("group-EnderWarps-until", time);
		}
		//mcMMO -----------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-mcMMOSmall-until") != null) && (user.getOption("group-mcMMOSmall-until") != ""))
		{
			user.setOption("group-mcMMOSmall-until", time);
		}
		if ((user.getOption("group-mcMMOMedium-until") != null) && (user.getOption("group-mcMMOMedium-until") != ""))
		{
			user.setOption("group-mcMMOMedium-until", time);
		}
		if ((user.getOption("group-mcMMOLarge-until") != null) && (user.getOption("group-mcMMOLarge-until") != ""))
		{
			user.setOption("group-mcMMOLarge-until", time);
		}
	}
	
	public void wipePackages(String playerName)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		//Pets -----------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderPetsPassive-until") != null) && (user.getOption("group-EnderPetsPassive-until") != ""))
		{
			user.setOption("group-EnderPetsPassive-until", "");
		}
		if ((user.getOption("group-EnderPetsNeutral-until") != null) && (user.getOption("group-EnderPetsNeutral-until") != ""))
		{
			user.setOption("group-EnderPetsNeutral-until", "");
		}
		if ((user.getOption("group-EnderPetsHostile-until") != null) && (user.getOption("group-EnderPetsHostile-until") != ""))
		{
			user.setOption("group-EnderPetsHostile-until", "");
		}
		if ((user.getOption("group-EnderPetsALL-until") != null) && (user.getOption("group-EnderPetsALL-until") != ""))
		{
			user.setOption("group-EnderPetsALL-until", "");
		}
		//Trails ---------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderTrailsSET1-until") != null) && (user.getOption("group-EnderTrailsSET1-until") != ""))
		{
			user.setOption("group-EnderTrailsSET1-until", "");
		}
		if ((user.getOption("group-EnderTrailsSET2-until") != null) && (user.getOption("group-EnderTrailsSET2-until") != ""))
		{
			user.setOption("group-EnderTrailsSET2-until", "");
		}
		if ((user.getOption("group-EnderTrailsALL-until") != null) && (user.getOption("group-EnderTrailsALL-until") != ""))
		{
			user.setOption("group-EnderTrailsALL-until", "");
		}
		//Disguises -------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderDisguisesPassive-until") != null) && (user.getOption("group-EnderDisguisesPassive-until") != ""))
		{
			user.setOption("group-EnderDisguisesPassive-until", "");
		}
		if ((user.getOption("group-EnderDisguisesHostile-until") != null) && (user.getOption("group-EnderDisguisesHostile-until") != ""))
		{
			user.setOption("group-EnderDisguisesHostile-until", "");
		}
		if ((user.getOption("group-EnderDisguisesALL-until") != null) && (user.getOption("group-EnderDisguisesALL-until") != ""))
		{
			user.setOption("group-EnderDisguiseALL-until", "");
		}
		if ((user.getOption("group-EnderDisguisesALLEntity-until") != null) && (user.getOption("group-EnderDisguisesALLEntity-until") != ""))
		{
			user.setOption("group-EnderDisguisesALLEntity-until", "");
		}
		//Cooldowns -------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderCooldowns-until") != null) && (user.getOption("group-EnderCooldowns-until") != ""))
		{
			user.setOption("group-EnderCooldowns-until", "");
		}
		//Warps -----------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderWarps-until") != null) && (user.getOption("group-EnderWarps-until") != ""))
		{
			user.setOption("group-EnderWarps-until", "");
		}
		//mcMMO -----------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-mcMMOSmall-until") != null) && (user.getOption("group-mcMMOSmall-until") != ""))
		{
			user.setOption("group-mcMMOSmall-until", "");
		}
		if ((user.getOption("group-mcMMOMedium-until") != null) && (user.getOption("group-mcMMOMedium-until") != ""))
		{
			user.setOption("group-mcMMOMedium-until", "");
		}
		if ((user.getOption("group-mcMMOLarge-until") != null) && (user.getOption("group-mcMMOLarge-until") != ""))
		{
			user.setOption("group-mcMMOLarge-until", "");
		}
	}
	
	public void checkRankScheduler() 
	{
		int enabled = b.getCfg().getInt("options.scheduler.enabled");
		if ((enabled != -1) && (enabled != 0))
		{
			logInfo("Rank Checking Scheduler enabled");
			b.startRankScheduler();
		}
		else
		{
			logInfo("Rank Checking Scheduler disabled");
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
			logError("Unit not valid. Defaulting to 1 hour.");
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
	
	public void printPlain(CommandSender sender, String message)
	{
		sender.sendMessage(message);
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
	//Broadcasting
	//------------------------------------------------------------------------------------------------------
	
	public void broadcastPlain(String message)
	{
		Bukkit.broadcastMessage(message);
	}
	
	
	//------------------------------------------------------------------------------------------------------
	//Logging
	//------------------------------------------------------------------------------------------------------
	
	public void logSuccess(String message)
	{
		//b.getServer().getConsoleCommandSender().sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "SUCCESS: " + ChatColor.GREEN + message);
		b.getServer().getConsoleSender().sendMessage(prefix + ChatColor.DARK_GREEN + "SUCCESS: " + ChatColor.GREEN + message);
	}
	
	public void logPlain(String message)
	{
		b.getServer().getConsoleSender().sendMessage(prefix + message);
	}
	
	public void logInfo(String message)
	{
		b.getServer().getConsoleSender().sendMessage(prefix + ChatColor.DARK_AQUA + "" + "INFO: " + ChatColor.BLUE + message);
	}
	
	public void logError(String message)
	{
		b.getServer().getConsoleSender().sendMessage(prefix + ChatColor.DARK_RED + "ERROR: " + ChatColor.RED + message);
	}
	
	public void logSevere(String message)
	{
		b.getServer().getConsoleSender().sendMessage(prefix + ChatColor.DARK_RED + "SEVERE: " + ChatColor.RED + message);
	}
	
	//------------------------------------------------------------------------------------------------------
	//Miscellaneous
	//------------------------------------------------------------------------------------------------------
	public BlivUtils getInstance()
	{
		return b;
	}
	
	
	
}
