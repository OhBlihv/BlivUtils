package net.auscraft.BlivUtils.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		if (!(user.getOption("group-" + rank + "-until") == null) && !(user.getOption("group-" + rank + "-until").length() == 0)) 
		{
			int timeleft = (int) ((Long.parseLong(user.getOption("group-" + rank + "-until", null))) - ((int) (System.currentTimeMillis() / 1000L)));
			return timeleft;
		}
		
		return -1;
	}
	
	public int getRankTime(String playerName, String rank)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		if (!(user.getOption("group-" + rank + "-until") == null) && !(user.getOption("group-" + rank + "-until").length() == 0))
		{
			return Integer.parseInt(user.getOption("group-" + rank + "-until", null));
		}
		
		return -1;
	}
	
	public String getActiveRank(String playerName)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		if (!(user.getOption("group-Admin-until") == null) && !(user.getOption("group-Admin-until").length() == 0))
		{
			return "Admin";
		}
		else if (!(user.getOption("group-EnderRank-until") == null) && !(user.getOption("group-EnderRank-until").length() == 0))
		{
			return "EnderRank";
		}
		else if (!(user.getOption("group-EnderDragon-until") == null) && !(user.getOption("group-EnderDragon-until").length() == 0))
		{
			return "EnderDragon";
		}
		else if (!(user.getOption("group-Enderman-until") == null) && !(user.getOption("group-Enderman-until").length() == 0))
		{
			return "Enderman";
		} 
		else if (!(user.getOption("group-Endermite-until") == null) && !(user.getOption("group-Endermite-until").length() == 0))
		{
			return "Endermite";
		} 
		return "";
	}
	
	public String getActiveRanks(String playerName)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		String ranks = "";
		if (!(user.getOption("group-Admin-until") == null) && !(user.getOption("group-Admin-until").length() == 0))
		{
			ranks += "Admin,";
		}
		if (!(user.getOption("group-EnderRank-until") == null) && !(user.getOption("group-EnderRank-until").length() == 0))
		{
			ranks += "EnderRank,";
		}
		if (!(user.getOption("group-EnderDragon-until") == null) && !(user.getOption("group-EnderDragon-until").length() == 0))
		{
			ranks += "EnderDragon,";
		}
		if (!(user.getOption("group-Enderman-until") == null) && !(user.getOption("group-Enderman-until").length() == 0))
		{
			ranks += "Enderman,";
		} 
		if (!(user.getOption("group-Endermite-until") == null) && !(user.getOption("group-Endermite-until").length() == 0))
		{
			ranks += "Endermite,";
		}
		return ranks.substring(0, ranks.length() - 1);
	}
	
	public ArrayList<String> getActivePackages(String playerName)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		ArrayList<String> packages = new ArrayList<String>();
		//Pets -----------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderPetsPassive-until") != null) && (user.getOption("group-EnderPetsPassive-until").length() != 0))
		{
			packages.add("Passive Pets|EnderPetsPassive");
		}
		if ((user.getOption("group-EnderPetsNeutral-until") != null) && (user.getOption("group-EnderPetsNeutral-until").length() != 0))
		{
			packages.add("Neutral Pets|EnderPetsNeutral");
		}
		if ((user.getOption("group-EnderPetsHostile-until") != null) && (user.getOption("group-EnderPetsHostile-until").length() != 0))
		{
			packages.add("Hostile Pets|EnderPetsHostile");
		}
		if ((user.getOption("group-EnderPetsALL-until") != null) && (user.getOption("group-EnderPetsALL-until").length() != 0))
		{
			packages.add("ALL Pets|EnderPetsALL");
		}
		//Trails ---------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderTrailsSET1-until") != null) && (user.getOption("group-EnderTrailsSET1-until").length() != 0))
		{
			packages.add("Set 1 Trails|EnderTrailsSET1");
		}
		if ((user.getOption("group-EnderTrailsSET2-until") != null) && (user.getOption("group-EnderTrailsSET2-until").length() != 0))
		{
			packages.add("Set 2 Trails|EnderTrailsSET2");
		}
		if ((user.getOption("group-EnderTrailsALL-until") != null) && (user.getOption("group-EnderTrailsALL-until").length() != 0))
		{
			packages.add("ALL Trails|EnderTrailsALL");
		}
		//Disguises -------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderDisguisesPassive-until") != null) && (user.getOption("group-EnderDisguisesPassive-until").length() != 0))
		{
			packages.add("Passive Mob Disguises|EnderDisguisesPassive");
		}
		if ((user.getOption("group-EnderDisguisesHostile-until") != null) && (user.getOption("group-EnderDisguisesHostile-until").length() != 0))
		{
			packages.add("Hostile Mob Disguises|EnderDisguisesHostile");
		}
		if ((user.getOption("group-EnderDisguisesALL-until") != null) && (user.getOption("group-EnderDisguisesALL-until").length() != 0))
		{
			packages.add("ALL Mob Disguises|EnderDisguisesALL");
		}
		if ((user.getOption("group-EnderDisguisesALLEntity-until") != null) && (user.getOption("group-EnderDisguisesALLEntity-until").length() != 0))
		{
			packages.add("ALL Mob Disguises + Entity Disguises|EnderDisguisesALLEntity");
		}
		//Cooldowns -------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderCooldowns-until") != null) && (user.getOption("group-EnderCooldowns-until").length() != 0))
		{
			packages.add("No Cooldowns|EnderCooldowns");
		}
		//Warps -----------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderWarps-until") != null) && (user.getOption("group-EnderWarps-until").length() != 0))
		{
			packages.add("20 Available Warps|EnderWarps");
		}
		//mcMMO -----------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-mcMMOSmall-until") != null) && (user.getOption("group-mcMMOSmall-until").length() != 0))
		{
			packages.add("mcMMO: Small Perk Pack|mcMMOSmall");
		}
		if ((user.getOption("group-mcMMOMedium-until") != null) && (user.getOption("group-mcMMOMedium-until").length() != 0))
		{
			packages.add("mcMMO: Medium Perk Pack|mcMMOMedium");
		}
		if ((user.getOption("group-mcMMOLarge-until") != null) && (user.getOption("group-mcMMOLarge-until").length() != 0))
		{
			packages.add("mcMMO: Large Perk Pack|mcMMOLarge");
		}
		//XP Multiplication and Keep Inventory/Levels on Death
		if ((user.getOption("group-EnderDoubleXP-until") != null) && (user.getOption("group-EnderDoubleXP-until").length() != 0))
		{
			packages.add("2x Vanilla Experience|EnderDoubleXP");
		}
		if ((user.getOption("group-EnderKeepInv-until") != null) && (user.getOption("group-EnderKeepInv-until").length() != 0))
		{
			packages.add("Keep Inventory on Death|EnderKeepInv");
		}
		if ((user.getOption("group-EnderKeepLevels-until") != null) && (user.getOption("group-EnderKeepLevels-until").length() != 0))
		{
			packages.add("Keep EXP Levels on Death|EnderKeepLevels");
		}
		if ((user.getOption("group-EnderDoubleXPKeepALL-until") != null) && (user.getOption("group-EnderDoubleXPKeepALL-until").length() != 0))
		{
			packages.add("Double XP/Keep Inv & Levels on Death|EnderDoubleXPKeepALL");
		}

		return packages;
	}
	
	public void updatePackages(String playerName, String time, String packagesList)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		String[] packages = packagesList.split(",");
		for(String packageName : packages)
		{
			if(!(packageName.equals("")))
			{
				if ((user.getOption(packageName) != null) && (user.getOption(packageName).length() != 0))
				{
					user.setOption(packageName, time);
					logDebug("Added " + time + " to " + playerName + "'s package " + packageName);
				}
			}
			
		}
		
	}
	
	public void wipePackages(String playerName)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		//Pets -----------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderPetsPassive-until") != null) && (user.getOption("group-EnderPetsPassive-until").length() != 0))
		{
			user.setOption("group-EnderPetsPassive-until", "");
			user.removeGroup("EnderPetsPassive");
		}
		if ((user.getOption("group-EnderPetsNeutral-until") != null) && (user.getOption("group-EnderPetsNeutral-until").length() != 0))
		{
			user.setOption("group-EnderPetsNeutral-until", "");
			user.removeGroup("EnderPetsNeutral");
		}
		if ((user.getOption("group-EnderPetsHostile-until") != null) && (user.getOption("group-EnderPetsHostile-until").length() != 0))
		{
			user.setOption("group-EnderPetsHostile-until", "");
			user.removeGroup("EnderPetsHostile");
		}
		if ((user.getOption("group-EnderPetsALL-until") != null) && (user.getOption("group-EnderPetsALL-until").length() != 0))
		{
			user.setOption("group-EnderPetsALL-until", "");
			user.removeGroup("EnderPetsALL");
		}
		//Trails ---------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderTrailsSET1-until") != null) && (user.getOption("group-EnderTrailsSET1-until").length() != 0))
		{
			user.setOption("group-EnderTrailsSET1-until", "");
			user.removeGroup("EnderTrailsSET1");
		}
		if ((user.getOption("group-EnderTrailsSET2-until") != null) && (user.getOption("group-EnderTrailsSET2-until").length() != 0))
		{
			user.setOption("group-EnderTrailsSET2-until", "");
			user.removeGroup("EnderTrailsSET2");
		}
		if ((user.getOption("group-EnderTrailsALL-until") != null) && (user.getOption("group-EnderTrailsALL-until").length() != 0))
		{
			user.setOption("group-EnderTrailsALL-until", "");
			user.removeGroup("EnderTrailsALL");
		}
		//Disguises -------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderDisguisesPassive-until") != null) && (user.getOption("group-EnderDisguisesPassive-until").length() != 0))
		{
			user.setOption("group-EnderDisguisesPassive-until", "");
			user.removeGroup("EnderDisguisesPassive");
		}
		if ((user.getOption("group-EnderDisguisesHostile-until") != null) && (user.getOption("group-EnderDisguisesHostile-until").length() != 0))
		{
			user.setOption("group-EnderDisguisesHostile-until", "");
			user.removeGroup("EnderPetsDisguisesHostile");
		}
		if ((user.getOption("group-EnderDisguisesALL-until") != null) && (user.getOption("group-EnderDisguisesALL-until").length() != 0))
		{
			user.setOption("group-EnderDisguiseALL-until", "");
			user.removeGroup("EnderDisguisesALL");
		}
		if ((user.getOption("group-EnderDisguisesALLEntity-until") != null) && (user.getOption("group-EnderDisguisesALLEntity-until").length() != 0))
		{
			user.setOption("group-EnderDisguisesALLEntity-until", "");
			user.removeGroup("EnderDisguisesALLEntity");
		}
		//Cooldowns -------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderCooldowns-until") != null) && (user.getOption("group-EnderCooldowns-until").length() != 0))
		{
			user.setOption("group-EnderCooldowns-until", "");
			user.removeGroup("EnderCooldowns");
		}
		//Warps -----------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-EnderWarps-until") != null) && (user.getOption("group-EnderWarps-until").length() != 0))
		{
			user.setOption("group-EnderWarps-until", "");
			user.removeGroup("EnderWarps");
		}
		//mcMMO -----------------------------------------------------------------------------------------------------------------
		if ((user.getOption("group-mcMMOSmall-until") != null) && (user.getOption("group-mcMMOSmall-until").length() != 0))
		{
			user.setOption("group-mcMMOSmall-until", "");
			user.removeGroup("mcMMOSmall");
		}
		if ((user.getOption("group-mcMMOMedium-until") != null) && (user.getOption("group-mcMMOMedium-until").length() != 0))
		{
			user.setOption("group-mcMMOMedium-until", "");
			user.removeGroup("mcMMOMedium");
		}
		if ((user.getOption("group-mcMMOLarge-until") != null) && (user.getOption("group-mcMMOLarge-until").length() != 0))
		{
			user.setOption("group-mcMMOLarge-until", "");
			user.removeGroup("mcMMOLarge");
		}
		//XP Multiplication and Keep Inventory/Levels on Death
		if ((user.getOption("group-EnderDoubleXP-until") != null) && (user.getOption("group-EnderDoubleXP-until").length() != 0))
		{
			user.setOption("group-EnderDoubleXP-until", "");
			user.removeGroup("EnderDoubleXP");
		}
		if ((user.getOption("group-EnderKeepInv-until") != null) && (user.getOption("group-EnderKeepInv-until").length() != 0))
		{
			user.setOption("group-EnderKeepInv-until", "");
			user.removeGroup("EnderKeepInv");
		}
		if ((user.getOption("group-EnderKeepLevels-until") != null) && (user.getOption("group-EnderKeepLevels-until").length() != 0))
		{
			user.setOption("group-EnderKeepLevels-until", "");
			user.removeGroup("EnderKeepLevels");
		}
		if ((user.getOption("group-EnderDoubleXPKeepALL-until") != null) && (user.getOption("group-EnderDoubleXPKeepALL-until").length() != 0))
		{
			user.setOption("group-EnderDoubleXPKeepALL-until", "");
			user.removeGroup("EnderDoubleXPKeepALL");
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
	
	public void logDebug(String message)
	{
		b.getServer().getConsoleSender().sendMessage(prefix + ChatColor.GREEN + "DEBUG: " + ChatColor.RED + message);
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
