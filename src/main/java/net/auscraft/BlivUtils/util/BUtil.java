package net.auscraft.BlivUtils.util;

import net.auscraft.BlivUtils.BlivUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BUtil
{

	private static final String prefix = "&f[&bBlivUtils&f] ";
	private static String playerPrefix = "";
	private static final java.util.logging.Logger log = Bukkit.getLogger();
	private static boolean debug = true;
	
	//------------------------------------------------------------------------------------------------------
	//Logging
	//------------------------------------------------------------------------------------------------------
	
	public static void logtoFile(String message, String logName)
	{
		try
		{
			File dataFolder = BlivUtils.getInstance().getDataFolder();
			
			if (!dataFolder.exists())
			{
				dataFolder.mkdir();
			}
			
			File saveTo;
			if(logName != null)
			{
				saveTo = new File(BlivUtils.getInstance().getDataFolder(), logName + ".txt");
			}
			else
			{
				saveTo = new File(BlivUtils.getInstance().getDataFolder(), "log.txt");
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
	
	public static int getTimeLeft(String playerName, String rank)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		if (user.getOption("group-" + rank + "-until") != null && user.getOption("group-" + rank + "-until").length() != 0) 
		{
			return (int) ((Long.parseLong(user.getOption("group-" + rank + "-until", null))) - ((int) (System.currentTimeMillis() / 1000L)));
		}
		
		return -1;
	}
	
	public static int getRankTime(String playerName, String rank)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		if (user.getOption("group-" + rank + "-until") != null && user.getOption("group-" + rank + "-until").length() != 0)
		{
			return Integer.parseInt(user.getOption("group-" + rank + "-until", null));
		}
		
		return -1;
	}
	
	public static String getActiveRank(String playerName)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		if (user.getOption("group-Admin-until") != null && user.getOption("group-Admin-until").length() != 0)
		{
			return "Admin";
		}
		else if (user.getOption("group-EnderRank-until") != null && user.getOption("group-EnderRank-until").length() != 0)
		{
			return "EnderRank";
		}
		else if (user.getOption("group-EnderDragon-until") != null && user.getOption("group-EnderDragon-until").length() != 0)
		{
			return "EnderDragon";
		}
		else if (user.getOption("group-Enderman-until") != null && user.getOption("group-Enderman-until").length() != 0)
		{
			return "Enderman";
		} 
		else if (user.getOption("group-Endermite-until") != null && user.getOption("group-Endermite-until").length() != 0)
		{
			return "Endermite";
		} 
		return "";
	}
	
	public static String getActiveRanks(String playerName)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		String ranks = "";
		if (user.getOption("group-Admin-until") != null && user.getOption("group-Admin-until").length() != 0)
		{
			ranks += "Admin,";
		}
		if (user.getOption("group-EnderRank-until") != null && user.getOption("group-EnderRank-until").length() != 0)
		{
			ranks += "EnderRank,";
		}
		if (user.getOption("group-EnderDragon-until") != null && user.getOption("group-EnderDragon-until").length() != 0)
		{
			ranks += "EnderDragon,";
		}
		if (user.getOption("group-Enderman-until") != null && user.getOption("group-Enderman-until").length() != 0)
		{
			ranks += "Enderman,";
		} 
		if (user.getOption("group-Endermite-until") != null && user.getOption("group-Endermite-until").length() != 0)
		{
			ranks += "Endermite,";
		}
		if(ranks.length() > 0)
		{
			return ranks.substring(0, ranks.length() - 1);
		}
		return "";
	}
	
	public static ArrayList<String> getActivePackages(String playerName)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		ArrayList<String> packages = new ArrayList<>();
		String readableString = "";
		
		for(String packageName : packageList)
		{
			switch(packageName)
			{
				case "EnderPetsPassive": readableString = "Passive Pets"; break;
				case "EnderPetsNeutral": readableString = "Neutral Pets"; break;
				case "EnderPetsHostile": readableString = "Hostile Pets"; break;
				case "EnderPetsALL": readableString = "ALL Pets"; break;
				case "EnderTrailsSET1": readableString = "Set 1 Trails"; break;
				case "EnderTrailsSET2": readableString = "Set 2 Trails"; break;
				case "EnderTrailsALL": readableString = "ALL Trails"; break;
				case "EnderDisguisesPassive": readableString = "Passive Mob Disguises"; break;
				case "EnderDisguisesHostile": readableString = "Hostile Mob Disguises"; break;
				case "EnderDisguisesALL": readableString = "ALL Mob Disguises"; break;
				case "EnderDisguisesALLEntity": readableString = "ALL Mob Disguises + Entity Disguises"; break;
				case "EnderCooldowns": readableString = "No Cooldowns"; break;
				case "EnderWarps": readableString = "20 Available Warps"; break;
				case "mcMMOSmall": readableString = "mcMMO: Small Perk Pack"; break;
				case "mcMMOMedium": readableString = "mcMMO: Medium Perk Pack"; break;
				case "mcMMOLarge": readableString = "mcMMO: Large Perk Pack"; break;
				case "EnderDoubleXP": readableString = "2x Vanilla Experience"; break;
				case "EnderKeepInv": readableString = "Keep Inventory on Death"; break;
				case "EnderKeepLevels": readableString = "Keep EXP Levels on Death"; break;
				case "EnderDoubleXPKeepALL": readableString = "Double XP/Keep Inv & Levels on Death"; break;
				default:
					break;
			}
			
			if (user.getOption("group-" + packageName + "-until") != null && user.getOption("group-" + packageName + "-until").length() != 0)
			{
				packages.add(readableString + "|" + packageName);
			}
		}

		return packages;
	}
	
	public static void updatePackages(String playerName, String time, String packagesList)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		String[] packages = packagesList.split(",");
		for(String packageName : packages)
		{
			if(packageName.length() != 0)
			{
				if ((user.getOption(packageName) != null) && (user.getOption(packageName).length() != 0))
				{
					user.setOption(packageName, time);
					logDebug("Added " + time + " to " + playerName + "'s package " + packageName);
				}
			}
		}
	}
	
	private static final List<String> packageList = Arrays.asList("EnderPetsPassive", "EnderPetsNeutral", "EnderPetsHostile", "EnderPetsALL", "EnderTrailsSET1", "EnderTrailsSET2",
			"EnderTrailsALL", "EnderDisguisesPassive", "EnderDisguisesHostile", "EnderDisguisesALL", "EnderDisguisesALLEntity", "EnderCooldowns", "EnderWarps", "EnderWarps",
			"mcMMOMedium", "mcMMOLarge", "EnderDoubleXP", "EnderKeepInv", "EnderKeepLevels", "EnderDoubleXPKeepALL");
	
	public static void wipePackages(String playerName)
	{
		PermissionUser user = PermissionsEx.getUser(playerName);
		
		for(String packageName : packageList)
		{
			if (user.getOption("group-" + packageName + "-until") != null && user.getOption("group-" + packageName + "-until").length() != 0)
			{
				user.setOption("group-" + packageName + "-until", "");
				user.removeGroup(packageName);
			}
		}
	}
	
	public static void checkRankScheduler() 
	{
		int enabled = FlatFile.getInstance().getInt("options.scheduler.enabled");
		if ((enabled != -1) && (enabled != 0))
		{
			logInfo("Rank Checking Scheduler enabled");
			BlivUtils.startRankScheduler();
		}
		else
		{
			logInfo("Rank Checking Scheduler disabled");
		}
	}
	
	public static int getConversion(String unit, String yamlStructure)
	{
		int conversion;
		int time = FlatFile.getInstance().getInt(yamlStructure);
		switch(unit)
		{
			case "seconds":
				conversion = time * 20;
				return conversion;
			case "minutes":
				conversion = time * 60 * 20;
				return conversion;
			case "hours":
				conversion = time * 60 * 60 * 20;
				return conversion;
			default:
				logError("Unit not valid. Defaulting to 1 hour.");
				conversion = 72000;
				return conversion;
		}
	}
	
	// ------------------------------------------------------------------------------------------------------
	// String Translation
	// ------------------------------------------------------------------------------------------------------

	public static List<String> translateVariable(List<String> lines, String variable, String content)
	{
		if(lines == null) { return null; }
		
		if(lines.size() > 0)
		{
			List<String> transLines = new ArrayList<>();
			for(String line : lines)
			{
				transLines.add(line.replace(variable, content));
			}
			return transLines;
		}
		return lines;
	}
	
	//&7[&e{server}&7] &f[&7{color}{player}&f]: &e{message}
	public static String translateChatVariables(String line, String server, String color, String player, String message)
	{
		if(line == null)
		{
			return "";
		}
		
		return translateColours(line.replace("{server}", server).replace("{color}", color).replace("{player}", player).replace("{message}", message));
	}
	
	public static String stripColours(String toFix)
	{
		return Pattern.compile("[&](.)").matcher(toFix).replaceAll("");
	}

	public static String translateConsoleColours(String toFix)
	{
		toFix = Pattern.compile("(?i)(&|�)([a])").matcher(toFix).replaceAll("\u001B[32m\u001B[1m"); // Light Green
		toFix = Pattern.compile("(?i)(&|�)([b])").matcher(toFix).replaceAll("\u001B[36m"); // Aqua
		toFix = Pattern.compile("(?i)(&|�)([c])").matcher(toFix).replaceAll("\u001B[31m"); // Red
		toFix = Pattern.compile("(?i)(&|�)([d])").matcher(toFix).replaceAll("\u001B[35m\u001B[1m"); // Pink
		toFix = Pattern.compile("(?i)(&|�)([e])").matcher(toFix).replaceAll("\u001B[33m\u001B[1m"); // Yellow
		toFix = Pattern.compile("(?i)(&|�)([f])").matcher(toFix).replaceAll("\u001B[0m"); // White
		toFix = Pattern.compile("(?i)(&|�)([0])").matcher(toFix).replaceAll("\u001B[30m"); // Black
		toFix = Pattern.compile("(?i)(&|�)([1])").matcher(toFix).replaceAll("\u001B[34m"); // Dark Blue
		toFix = Pattern.compile("(?i)(&|�)([2])").matcher(toFix).replaceAll("\u001B[32m"); // Dark Green
		toFix = Pattern.compile("(?i)(&|�)([3])").matcher(toFix).replaceAll("\u001B[34m\u001B[1m"); // Light Blue
		toFix = Pattern.compile("(?i)(&|�)([4])").matcher(toFix).replaceAll("\u001B[31m"); // Dark Red
		toFix = Pattern.compile("(?i)(&|�)([5])").matcher(toFix).replaceAll("\u001B[35m"); // Purple
		toFix = Pattern.compile("(?i)(&|�)([6])").matcher(toFix).replaceAll("\u001B[33m"); // Gold
		toFix = Pattern.compile("(?i)(&|�)([7])").matcher(toFix).replaceAll("\u001B[37m"); // Light Grey
		toFix = Pattern.compile("(?i)(&|�)([8])").matcher(toFix).replaceAll("\u001B[30m\u001B[1m"); // Dark Grey
		toFix = Pattern.compile("(?i)(&|�)([9])").matcher(toFix).replaceAll("\u001B[34m"); // Dark Aqua
		toFix = Pattern.compile("(?i)(&|�)([r])").matcher(toFix).replaceAll("\u001B[0m");
		toFix += "\u001B[0m"; // Stop colour from overflowing to the next line with a reset code

		return toFix;
	}

	private static final Pattern colourPattern = Pattern.compile("(?i)&([0-9A-Fa-f-l-oL-OrR])");

	public static String translateColours(String toFix)
	{
		// Convert every single colour code and formatting code, excluding
		// 'magic' (&k), capitals and lowercase are converted.
		return colourPattern.matcher(toFix).replaceAll("\u00A7$1");
	}
	public static List<String> translateColours(List<String> lines)
	{
		if (lines == null || lines.size() == 0)
		{
			return null;
		}

		//list.add(i, colourPattern.matcher(list.get(i)).replaceAll("\u00A7$1"));
		return lines.stream().map(line -> colourPattern.matcher(line).replaceAll("\u00A7$1")).collect(Collectors.toList());
	}

	// ------------------------------------------------------------------------------------------------------
	// Printing
	// ------------------------------------------------------------------------------------------------------

	public static void printSuccess(CommandSender sender, String message)
	{
		sender.sendMessage(playerPrefix + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "SUCCESS: " + ChatColor.GREEN + translateColours(message));
	}

	public static void printPlain(CommandSender sender, String message)
	{
		sender.sendMessage(playerPrefix + translateColours(message));
	}

	public static void printInfo(CommandSender sender, String message)
	{
		sender.sendMessage(playerPrefix + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "INFO: " + ChatColor.BLUE + translateColours(message));
	}

	public static void printError(CommandSender sender, String message)
	{
		sender.sendMessage(playerPrefix + ChatColor.DARK_RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "ERROR: " + ChatColor.RED + translateColours(message));
	}

	// ------------------------------------------------------------------------------------------------------
	// Broadcasting
	// ------------------------------------------------------------------------------------------------------

	public static void broadcastPlain(String message)
	{
		Bukkit.broadcastMessage(message);
	}

	// ------------------------------------------------------------------------------------------------------
	// Logging
	// ------------------------------------------------------------------------------------------------------

	public static void logSuccess(String message)
	{
		log.log(Level.INFO, translateConsoleColours(prefix + "&2SUCCESS: &a" + message));
	}

	public static void logPlain(String message)
	{
		log.log(Level.INFO, translateConsoleColours(prefix + message));
	}

	public static void logInfo(String message)
	{
		log.log(Level.INFO, translateConsoleColours(prefix + "&9INFO: &b" + message));
	}

	public static void logError(String message)
	{
		log.log(Level.WARNING, translateConsoleColours(prefix + "&4ERROR: &c" + message));
	}

	public static void logDebug(String message)
	{
		if (debug)
		{
			log.log(Level.INFO, translateConsoleColours(prefix + "&2DEBUG: &a" + message));
		}
	}

	public static void logSevere(String message)
	{
		log.log(Level.SEVERE, translateConsoleColours(prefix + "&4SEVERE: &c" + message));
	}
}
