package net.auscraft.BlivUtils.promotions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PromoteExecuter implements CommandExecutor
{
	
	private Utilities util;

	public PromoteExecuter(BlivUtils instance)
	{
		util = instance.getUtil();
	}

	private ItemStack MagmaSlime(boolean hasPermission)
	{
		ItemStack item = new ItemStack(Material.MAGMA_CREAM, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "MagmaSlime");
        String hasPerm = ChatColor.RED + "NO";
        if(hasPermission == true)
        {
        	hasPerm = ChatColor.GREEN + "YES";
        }
        List<String> lore = Arrays.asList(ChatColor.DARK_GREEN + "$" + ChatColor.WHITE + "25,000","Can Buy: " + hasPerm);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
	}
	
	private ItemStack Blaze(boolean hasPermission)
	{
		ItemStack item = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Blaze");
        String hasPerm = ChatColor.RED + "NO";
        if(hasPermission == true)
        {
        	hasPerm = ChatColor.GREEN + "YES";
        }
        List<String> lore = Arrays.asList(ChatColor.DARK_GREEN + "$" + ChatColor.WHITE + "50,000","Can Buy: " + hasPerm);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
	}
	
	private ItemStack PigZombie(boolean hasPermission)
	{
		ItemStack item = new ItemStack(Material.GOLD_SWORD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "PigZombie");
        String hasPerm = ChatColor.RED + "NO";
        if(hasPermission == true)
        {
        	hasPerm = ChatColor.GREEN + "YES";
        }
        List<String> lore = Arrays.asList(ChatColor.DARK_GREEN + "$" + ChatColor.WHITE + "75,000","Can Buy: " + hasPerm);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
	}
	
	private ItemStack Ghast(boolean hasPermission)
	{
		ItemStack item = new ItemStack(Material.GHAST_TEAR, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Ghast");
        String hasPerm = ChatColor.RED + "NO";
        if(hasPermission == true)
        {
        	hasPerm = ChatColor.GREEN + "YES";
        }
        List<String> lore = Arrays.asList(ChatColor.DARK_GREEN + "$" + ChatColor.WHITE + "100,000","Can Buy: " + hasPerm);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
	}
	
	private ItemStack Endermite(boolean hasPermission)
	{
		ItemStack item = new ItemStack(Material.ENDER_PEARL, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Endermite");
        String hasPerm = ChatColor.RED + "NO";
        if(hasPermission == true)
        {
        	hasPerm = ChatColor.GREEN + "YES";
        }
        List<String> lore = Arrays.asList(ChatColor.DARK_GREEN + "$" + ChatColor.WHITE + "100,000 (Every 15 Days)","Can Rent: " + hasPerm);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
	{
		if (cmd.getName().equalsIgnoreCase("buyrank") && (sender instanceof Player))
		{
			Player p = (Player) sender;
			Inventory inv = Bukkit.createInventory(null, 27, "/buyrank");
	        inv.setItem(11, MagmaSlime(p.hasPermission("blivutils.promote.magmaslime")));
	        inv.setItem(12, Blaze(p.hasPermission("blivutils.promote.blaze")));
	        inv.setItem(13, PigZombie(p.hasPermission("blivutils.promote.pigzombie")));
	        inv.setItem(14, Ghast(p.hasPermission("blivutils.promote.ghast")));
	        inv.setItem(15, Endermite(p.hasPermission("blivutils.promote.done")));
	        ((Player) sender).openInventory(inv);
		}

		if (cmd.getName().equalsIgnoreCase("promoadmin") && (sender instanceof Player))
		{
			if ((sender.hasPermission("blivutils.promote.admin")))
			{
				if (args.length >= 3)
				{
					// Two variables are /required/, while the last is optional
					String numberPlusTimeframe = "";
					int length = 0;
					if (args[1] != null)
					{
						if (args[2].contains("hour"))
						{
							// Seconds multiplied by that smaller number to get args[1] hour(s)
							length = (Integer.parseInt(args[1])) * 3600;
						}
						else if (args[2].contains("day"))
						{
							// Seconds multiplied by that smaller number to get args[1] day(s)
							length = (Integer.parseInt(args[1])) * 86400;
						}
						else if (args[2].contains("month"))
						{
							// Seconds multiplied by that giant number to get args[1] month(s)
							length = (Integer.parseInt(args[1])) * 2592000;
						}
						
						numberPlusTimeframe = pluralTimeframe(args[2], args[1]);
					}
					else
					{
						if (Integer.parseInt(args[1]) == 1)
						{
							numberPlusTimeframe = "1 month";
						} 
						else 
						{
							numberPlusTimeframe = args[1] + " months";
						}
					}

						PermissionUser user = PermissionsEx.getUser(args[0]);
						String sLength = "" + length; //Lazy way of Int -> String, but eh, I'll fix later.
					
						user.addGroup("Admin");
						user.setOption("group-Admin-until", sLength);
						util.printSuccess(sender, "Player " + args[0] + " promoted for " + numberPlusTimeframe);
						util.logtoFile("Player " + args[0] + " promoted to Admin for " + numberPlusTimeframe, null);
						return true;
					}
					else
					{
						util.printError(sender, "You didnt specify a time in months!");
						return false;
					}
			}
			else
			{
				util.printError(sender, "Yeah, good luck with that...");
			}
		}
		if (cmd.getName().equalsIgnoreCase("timeleft")) 
		{
			// ------
			String playerName = null;
			boolean other = false;
			boolean bypassother = false;
			try
			{
				if(args.length == 0) //Self Check
				{
					playerName = sender.getName();
				}
				else //Other player check
				{
					OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
					if(p.getName() != null)
					{
						playerName = p.getName();
					}
					other = true;
					if(sender.hasPermission("blivutils.timeleft.bypass"))
					{
						bypassother = true;
					}
					
				}
			}
			catch(NullPointerException e)
			{
				util.printError(sender, "Player requested does not exist.");
			}
				
			String rank = util.getActiveRanks(playerName);
			String packages = null;
			if(rank.contains("EnderRank"))
			{
				packages = util.getActivePackages(playerName);
			}
			String outputMessage = "";

			if (rank != "")
			{
				ChatColor colour = ChatColor.DARK_PURPLE;
				String ranks[] = rank.split(",");
				String packageString = "", packagePrefix = "";
				for(String sRank : ranks)
				{
					String actualRank = sRank;
					switch(sRank)
					{
						//Already purple.
						//case Endermite:
						//case Enderman:
						case "EnderRank":
							//Already Purple
							util.logInfo("Is EnderRank");
							String sPackages[] = packages.split(",");
							util.logInfo(packages);
							PermissionUser user = PermissionsEx.getUser(playerName);
							//Change the rank based on the pex option
							String EnderRankOption = user.getOption("EnderRankValue");
							switch(EnderRankOption)
							{
								case "1":
									sRank = "Enderman";
									break;
								case "2":
									sRank = "EnderDragon";
									colour = ChatColor.DARK_RED;
									break;
								case "3":
									sRank = "Wither";
									colour = ChatColor.DARK_GRAY;
									break;
								default:
									break;
							}
							if(((!packages.isEmpty()) && bypassother == true) || ((!packages.isEmpty()) && (other != true)))
							{
								util.logInfo("Packages is not empty");
								packagePrefix = ChatColor.GOLD + "Included Packages:\n ";
								for(String sPackage : sPackages)
								{
									packageString += ChatColor.WHITE + "" + ChatColor.ITALIC + sPackage + "\n";
								}
								//Remove the trailing newline character
								packageString = packageString.substring(0, (packageString.length() - 2));
							}
							
							break;
						case "EnderDragon": colour = ChatColor.DARK_RED;
							break;
						case "Admin": colour = ChatColor.GOLD;
							break;
						default: colour = ChatColor.DARK_PURPLE;
							break;
					}
					
					outputMessage += colour + sRank + timeleftGeneric(playerName, actualRank) + "\n" + packagePrefix + packageString;
				}
				if(!outputMessage.equals(""))
				{
					if(other == false)
					{
						sender.sendMessage(ChatColor.GREEN + "Timeleft on remaining ranks:\n" + outputMessage);
					}
					else
					{
						sender.sendMessage(ChatColor.GREEN + "Timeleft on remaining ranks for " + ChatColor.AQUA + playerName + ChatColor.GREEN + ":\n" + outputMessage);
					}
					
					
				}
				else
				{
					util.printInfo(sender, ChatColor.GOLD + "You dont have an active rank!");
				}
				return true;
			}
			else
			{
				util.printInfo(sender, ChatColor.GOLD + "You dont have an active rank!");
				return true;
			}
		}
		
		//This will grab the player's current rank timeleft, and add time to it.
		//If the expiry date has already passed, or the rank was never active, it will not update.
		//Merged from /updateadmin, in order to save space.
		if (cmd.getName().equalsIgnoreCase("updatetime") && (sender instanceof Player))
		{
			if (sender.hasPermission("blivutils.promote.admin.update"))
			{
				if (args.length == 4) // 3 Arguments - /updatetime <name> <rank> <#> <day(s)/month(s)>
				{
					PermissionUser user = PermissionsEx.getUser(args[0]);
					String rank = "";
					
					//Should probably mush this all together, but it doesnt really hurt to do this.
					if(args[1].equalsIgnoreCase("Endermite"))
					{
						rank = "Endermite";
					}
					else if(args[1].equalsIgnoreCase("Enderman"))
					{
						rank = "Enderman";
					}
					else if(args[1].equalsIgnoreCase("EnderDragon"))
					{
						rank = "EnderDragon";
					}
					else if(args[1].equalsIgnoreCase("Admin"))
					{
						rank = "Admin";
					}
					else if(args[1].equalsIgnoreCase("EnderRank"))
					{
						rank = "EnderRank";
					}
					else
					{
						//Rank not found
						util.printError(sender, "That is not a valid timed rank!");
						return true;
					}
					
					int timeLeft = util.getRankTime(args[0], rank);
					int length = 0;
					String timeFormat = "";

					if (args[3].contains("minute"))
					{
						try
						{
							// Seconds multiplied by that smaller number to get args[1] day(s)
							length = (Integer.parseInt(args[2])) * 60;
							if (args[2] == "1")
							{
								timeFormat = "1 minute";
							} 
							else 
							{
								timeFormat = args[2] + " minutes";
							}
						}
						catch(NumberFormatException e)
						{
							util.printError(sender, "Invalid Format");
							return false;
						}
					} 
					else if (args[3].contains("hour"))
					{
						try
						{
							// Seconds multiplied by that smaller number to get args[1] day(s)
							length = (Integer.parseInt(args[2])) * 3600;
							if (args[2] == "1")
							{
								timeFormat = "1 hour";
							} 
							else 
							{
								timeFormat = args[2] + " hours";
							}
						}
						catch(NumberFormatException e)
						{
							util.printError(sender, "Invalid Format");
							return false;
						}
					} 
					else if (args[3].contains("day"))
					{
						try
						{
							// Seconds multiplied by that smaller number to get args[1] day(s)
							length = (Integer.parseInt(args[2])) * 86400;
							if (args[2] == "1")
							{
								timeFormat = "1 day";
							} 
							else 
							{
								timeFormat = args[2] + " days";
							}
						}
						catch(NumberFormatException e)
						{
							util.printError(sender, "Invalid Format");
							return false;
						}
					} 
					else if (args[3].contains("month")) 
					{
						try
						{
							// Seconds multiplied by that giant number to get args[1] month(s)
							length = (Integer.parseInt(args[2])) * 2592000;
							if (args[2] == "1")
							{
								timeFormat = "1 month";
							}
							else
							{
								timeFormat = args[2] + " months";
							}
						}
						catch(NumberFormatException e)
						{
							util.printError(sender, "Invalid Format");
							return false;
						}
					}

					if(util.getTimeLeft(args[0], rank) > 0)
					{
						timeLeft += length;
						String sTimeLeft = "" + timeLeft;
						user.setOption("group-" + rank + "-until", sTimeLeft);
						util.printSuccess(sender, ChatColor.GREEN + "Updated " + args[0] + "'s " + rank + " time by " + timeFormat);
						return true;
					}
					else
					{
						user.setOption("group-" + rank + "-until", "" + length);
						util.printSuccess(sender, ChatColor.GREEN + "Set " + args[0] + "'s " + rank + " to " + timeFormat);
						//util.printError(sender, args[0] + "'s rank has expired!");
						return true;
					}
				} 
				else 
				{
					util.printError(sender, "Invalid format!");
					return false;
				}
			}
			else
			{
				util.printError(sender, "You don't have permission to do this!");
				return true;
			}
		}

		// This is in here because it requires PEX, and I don't want to import where I don't need to.
		if (cmd.getName().equalsIgnoreCase("prefix") && (sender instanceof Player))
		{
			if (sender.hasPermission("blivutils.prefix"))
			{
				if (args.length > 0) 
				{
					String enteredPrefix = "", playerName = "", successfulSet = "";
					if (args.length > 1) // If /prefix <name> <prefix>
					{
						if (sender.hasPermission("blivutils.prefix.other")) 
						{
							playerName = args[0];
							enteredPrefix = args[1];
							successfulSet = ChatColor.GREEN + "Successfully set " + playerName + ChatColor.GREEN + "'s prefix to ";
							
						}
						else
						{
							util.printError(sender, "You don't have permission to change other's prefixes!");
							return true;
						}
					} 
					else // Just /prefix <prefix>
					{
						enteredPrefix = args[0];
						playerName = sender.getName();
						if(!(enteredPrefix.length() >= 50))
						{
							if((enteredPrefix.contains("Admin")) || (enteredPrefix.contains("Mod")) || (enteredPrefix.contains("Musketeer")))
							{
								if(sender.hasPermission("blivutils.prefix.other"))
								{
									successfulSet = ChatColor.GREEN + "Successfully set your prefix to ";
								}
								else
								{
									util.printError(sender, "You don't have permission to use one of those words!");
									return true;
								}
							}
							else
							{
								successfulSet = ChatColor.GREEN + "Successfully set your prefix to ";
							}
						}
						else
						{
							util.printError(sender, "Your prefix is too long! Keep it under 50 Characters.");
						}
						
					}

					if ((enteredPrefix.contains("[")) && (enteredPrefix.contains("]"))) // Correct formatting.
					{
						PermissionUser user = PermissionsEx.getUser(playerName);
						enteredPrefix = enteredPrefix + " "; // This is the extra space needed at the end because we've been doing prefixes like that for years.
						
						user.setPrefix(enteredPrefix, null);
						sender.sendMessage(successfulSet + ChatColor.WHITE + util.translateColours(enteredPrefix));
						util.logInfo(playerName + "'s prefix has been set to " + enteredPrefix);
						util.logtoFile(playerName + "'s prefix has been set to " + enteredPrefix, null);
						return true;
					} 
					else // Prefix tags not included.
					{
						util.printError(sender, "Your entered prefix didn't include brackets! '[' and ']' are required.");
						return true;
					}
				}
				else 
				{
					util.printError(sender, "Enter a prefix!");
				}
			}
			else 
			{
				util.printError(sender, "You don't have permission to change your prefix!");
				return true;
			}
		}
		else 
		{
			return false;
		}
		return false;
	}

	private String pluralTimeframe(String inputString, String stringTime) 
	{
		String numberPlusTimeframe = "";
		int time = Integer.parseInt(stringTime);

		if (inputString.contains("day")) 
		{
			String pluralTimeframe;
			if (time == 1)
			{
				pluralTimeframe = " day.";
			}
			else // More than 1
			{
				pluralTimeframe = " days.";
			}
			numberPlusTimeframe = time + pluralTimeframe;
		} 
		else if (inputString.contains("month"))
		{
			String pluralTimeframe;
			if (time == 1) 
			{
				pluralTimeframe = " month.";
			} 
			else // More than 1
			{
				pluralTimeframe = " months.";
			}
			numberPlusTimeframe = time + pluralTimeframe;
		}

		return numberPlusTimeframe;
	}

	private String timeleftGeneric(String player, String rank) // Oh man, he just used a control flag. Raise the pitchforks.
	{
		int allseconds = util.getTimeLeft(player, rank);
		String timeString = "", print;

		// Just shamelessly ripped this code, I don't even care:
		// http://stackoverflow.com/questions/11357945/java-convert-seconds-into-day-hour-minute-and-seconds-using-timeunit
		// Source: First Comment. (And I also edited it a bit, so its not just blatantly ripped from StackOverflow...)
		if (allseconds >= 0)
		{
			int day = (int) TimeUnit.SECONDS.toDays(allseconds);
			long hour = TimeUnit.SECONDS.toHours(allseconds) - (day * 24);
			long minute = TimeUnit.SECONDS.toMinutes(allseconds) - (TimeUnit.SECONDS.toHours(allseconds) * 60);
			long second = TimeUnit.SECONDS.toSeconds(allseconds) - (TimeUnit.SECONDS.toMinutes(allseconds) * 60);
			// END Ripped code ---

			// String them together, then cut them down if they're 0
			if (day != 0)
			{
				timeString += day + " Day";
				if(day > 1)
				{
					timeString += "s";
				}
			}
			if (hour != 0)
			{
				timeString += " " + hour + " Hour";
				if(hour > 1)
				{
					timeString += "s";
				}
			}
			if (minute != 0) 
			{
				timeString += " " + minute + " Minute";
				if(minute > 1)
				{
					timeString += "s";
				}
			}
			if (minute == 0) 
			{
				timeString += " " + second + " Second";
				if(second > 1)
				{
					timeString += "s";
				}
			}

			// String print = ChatColor.GOLD + "" + days + hours + minutes + seconds + ChatColor.DARK_GREEN + "\nRemaining of " + ChatColor.GREEN + rank;
			print = ": " + ChatColor.WHITE + "" + timeString;
		}
		else 
		{
			print = "";
		}
		return print;
	}
}
