package net.auscraft.BlivUtils.executors;

import net.auscraft.BlivUtils.util.BUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class PromoteExecutor implements CommandExecutor
{

	public PromoteExecutor()
	{
		
	}

	private ItemStack MagmaSlime(boolean hasPermission)
	{
		ItemStack item = new ItemStack(Material.MAGMA_CREAM, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "MagmaSlime");
        String hasPerm = ChatColor.RED + "NO";
        if(hasPermission)
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
        if(hasPermission)
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
        if(hasPermission)
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
        if(hasPermission)
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
        if(hasPermission)
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
			Inventory inv = Bukkit.createInventory(null, 27, "Buy Ranks");
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
					String numberPlusTimeframe;
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
					BUtil.printSuccess(sender, "Player " + args[0] + " promoted for " + numberPlusTimeframe);
					BUtil.logtoFile("Player " + args[0] + " promoted to Admin for " + numberPlusTimeframe, null);
					return true;
				}
				
				BUtil.printError(sender, "You didnt specify a time in months!");
				return false;
			}
			
			BUtil.printError(sender, "Yeah, good luck with that...");
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
				BUtil.printError(sender, "Player requested does not exist.");
			}
				
			String rank = BUtil.getActiveRanks(playerName);
			ArrayList<String> packages = null;
			if(rank.contains("EnderRank"))
			{
				packages = BUtil.getActivePackages(playerName);
				
				for(String linePackage : packages)
				{
					BUtil.logDebug(linePackage);
				}
			}
			
			String outputMessage = "";

			if (rank.length() != 0)
			{
				String rankTimeLeft;
				ChatColor colour = ChatColor.DARK_PURPLE;
				String ranks[] = rank.split(",");
				String packageString = "", packagePrefix = "";
				for(String sRank : ranks)
				{
					String actualRank = sRank;
					rankTimeLeft = timeleftGeneric(playerName, actualRank);
					switch(sRank)
					{
						//Already purple.
						//case Endermite:
						//case Enderman:
						case "EnderRank":
							try
							{
								//Already Purple
								//util.logInfo("Is EnderRank");
								//util.logInfo(packages);
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
								//If the other player has perks/you have permission | You have perks and you are looking at your own
								if(((!packages.isEmpty()) && bypassother) || ((!packages.isEmpty()) && (!other)))
								{
									BUtil.logInfo("Packages is not empty");
									packagePrefix = ChatColor.GOLD + "Included Packages:\n";
									String sPackages[];
									String packageTimeLeft;
									String addonString = "";
									
									for(String sPackage : packages)
									{
										sPackages = sPackage.split("[|]");
										packageTimeLeft = timeleftGeneric(playerName, sPackages[1]);
										if(packageTimeLeft.equals(rankTimeLeft))
										{
											packageTimeLeft = "";
											packageString += ChatColor.WHITE + "" + ChatColor.ITALIC + sPackages[0] + ChatColor.GOLD + ChatColor.stripColor(packageTimeLeft) + "\n";
										}
										else
										{
											if(addonString.equals(""))
											{
												addonString = ChatColor.GOLD + "Addons:\n";
											}
											addonString += ChatColor.WHITE + "" + ChatColor.ITALIC + sPackages[0] + ChatColor.GOLD + ChatColor.stripColor(packageTimeLeft) + "\n";
										}
									}
									
									packageString += addonString;
								}
							}
							catch(NullPointerException e)
							{
								//One of the EnderRank variables/indicators was invalid
								//Did you try to add EnderRank to yourself incorrectly?
								break;
							}
							break;
						case "EnderDragon": colour = ChatColor.DARK_RED;
							break;
						case "Admin": colour = ChatColor.GOLD;
							break;
						default: colour = ChatColor.DARK_PURPLE;
							break;
					}
					
					outputMessage += colour + sRank + rankTimeLeft + "\n" + packagePrefix + packageString;
				}
				if(outputMessage.length() != 0)
				{
					if(!other)
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
					if(other)
					{
						BUtil.printInfo(sender, ChatColor.GOLD + "That player doesn't have an active rank!");
					}
					else
					{
						BUtil.printInfo(sender, ChatColor.GOLD + "You dont have an active rank!");
					}
					
				}
				return true;
			}
			
			BUtil.printInfo(sender, ChatColor.GOLD + "You dont have an active rank!");
			return true;
		}
		
		//This will grab the player's current rank timeleft, and add time to it.
		//If the expiry date has already passed, or the rank was never active, it will not update.
		//Merged from /updateadmin, in order to save space.
		if (cmd.getName().equalsIgnoreCase("updatetime"))
		{
			if (sender.hasPermission("blivutils.promote.admin.update"))
			{
				if (args.length == 4) // 3 Arguments - /updatetime <name> <rank> <#> <day(s)/month(s)>
				{
					PermissionUser user = PermissionsEx.getUser(args[0]);
					String rank;
					
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
						BUtil.printError(sender, "That is not a valid timed rank!");
						return true;
					}
					
					int timeLeft = BUtil.getRankTime(args[0], rank);
					int length = 0;
					String timeFormat = "";

					if (args[3].contains("minute"))
					{
						try
						{
							// Seconds multiplied by that smaller number to get args[1] day(s)
							length = (Integer.parseInt(args[2])) * 60;
							if (args[2].equals("1"))
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
							BUtil.printError(sender, "Invalid Format");
							return false;
						}
					} 
					else if (args[3].contains("hour"))
					{
						try
						{
							// Seconds multiplied by that smaller number to get args[1] day(s)
							length = (Integer.parseInt(args[2])) * 3600;
							if (args[2].equals("1"))
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
							BUtil.printError(sender, "Invalid Format");
							return false;
						}
					} 
					else if (args[3].contains("day"))
					{
						try
						{
							// Seconds multiplied by that smaller number to get args[1] day(s)
							length = (Integer.parseInt(args[2])) * 86400;
							if (args[2].equals("1"))
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
							BUtil.printError(sender, "Invalid Format");
							return false;
						}
					} 
					else if (args[3].contains("month")) 
					{
						try
						{
							// Seconds multiplied by that giant number to get args[1] month(s)
							length = (Integer.parseInt(args[2])) * 2592000;
							if (args[2].equals("1"))
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
							BUtil.printError(sender, "Invalid Format");
							return false;
						}
					}

					if(BUtil.getTimeLeft(args[0], rank) > 0)
					{
						timeLeft += length;
						String sTimeLeft = "" + timeLeft;
						user.setOption("group-" + rank + "-until", sTimeLeft);
						BUtil.printSuccess(sender, ChatColor.GREEN + "Updated " + args[0] + "'s " + rank + " time by " + timeFormat);
						return true;
					}
					
					length += ((int) (System.currentTimeMillis() / 1000L));
					user.setOption("group-" + rank + "-until", "" + length);
					BUtil.printSuccess(sender, ChatColor.GREEN + "Set " + args[0] + "'s " + rank + " to " + timeFormat);
					return true;
				}
				
				BUtil.printError(sender, "Invalid format!");
				return false;
			}
			
			BUtil.printError(sender, "You don't have permission to do this!");
			return true;
		}

		// This is in here because it requires PEX, and I don't want to import where I don't need to.
		if (cmd.getName().equalsIgnoreCase("prefix") && (sender instanceof Player))
		{
			if (!sender.hasPermission("blivutils.prefix"))
			{
				BUtil.printError(sender, "You don't have permission to change your prefix!");
				return true;
			}

			if (args.length == 0)
			{
				BUtil.printError(sender, "Enter a prefix!");
				return true;
			}

			if(!checkPrefix((Player) sender, args[0]))
			{
				return true;
			}

			String colouredPrefix = ChatColor.BOLD + FORMATTING_CODE.matcher(BUtil.translateColours(args[0])).replaceAll("$0§l");

			PermissionUser user = PermissionsEx.getUser(sender.getName());

			sender.sendMessage(ChatColor.GREEN + "Successfully set your prefix to " + ChatColor.RESET + colouredPrefix);

			colouredPrefix += getArrowColour((Player) sender) + " §l»";
			user.setPrefix(colouredPrefix, null);
			BUtil.logInfo(sender.getName() + "'s prefix has been set to " + colouredPrefix);
			BUtil.logtoFile(sender.getName() + "'s prefix has been set to " + colouredPrefix, null);
		}

		return true;
	}

	//                              Only use the section sign, since we've converted all ampersands by that time
	private static final Pattern    FORMATTING_CODE = Pattern.compile("[§][0-9a-fl-r]"),
									MAGIC_CODE = Pattern.compile("[&|§]k"),
									STRIP_COLOUR = Pattern.compile("[&|§][0-9a-fl-r]");

	private static final ArrayDeque<String> disallowedPrefixes = new ArrayDeque<>(Arrays.asList(
			"musketeer", "admin", "moderator"
	));

	private boolean checkPrefix(Player player, String prefix)
	{
		if(prefix == null || STRIP_COLOUR.matcher(prefix.toLowerCase(Locale.ENGLISH)).replaceAll("").length() <= 2)
		{
			BUtil.printError(player, "Your prefix is too short! It must be above or equal to 3 characters long.");
			return false;
		}

		if(MAGIC_CODE.matcher(prefix).find())
		{
			BUtil.printError(player, "Your prefix contains the forbidden magic formatting code.");
			return false;
		}

		if(!isAlphanumericOrAmpersand(prefix))
		{
			BUtil.printError(player, "Your prefix '" + prefix + ChatColor.RED + "' contains non-alphanumeric characters.");
			return false;
		}

		/*if(disallowedPrefixes.contains(STRIP_COLOUR.matcher(prefix.toLowerCase(Locale.ENGLISH)).replaceAll("")))
		{
			BUtil.printError(player, "Your prefix '" + prefix + ChatColor.RED + "' contained a disallowed word.");
			return false;
		}*/

		if(STRIP_COLOUR.matcher(prefix.toLowerCase(Locale.ENGLISH)).replaceAll("").length() > 20)
		{
			BUtil.printError(player, "Your prefix is too long! It must stay under 20 characters (not including formatting codes).");
			return false;
		}

		return true;
	}

	private String getArrowColour(Player player)
	{
		PermissionUser user = PermissionsEx.getUser(player);
		List<PermissionGroup> groupList = Arrays.asList(user.getGroups());

		for(PermissionGroup group : groupList)
		{
			if(group.getName().toLowerCase().equals("enderrank"))
			{
				String enderRankValue = user.getOption("EnderRankValue");
				if(enderRankValue == null)
				{
					return "§d";
				}

				switch(enderRankValue)
				{
					case "1": return "§d";
					case "2": return "§c";
					case "3": return "§7";
				}
			}
		}

		for(PermissionGroup group : groupList)
		{
			switch(group.getName().toLowerCase())
			{
				case "squid":
				case "chicken":
				case "sheep":
				case "pig":
					return "§a";
				case "cow":
				case "mooshroom":
				case "slime":
				case "ocelot":
					return "§b";
				case "magmaslime":
				case "blaze":
				case "pigzombie":
				case "ghast":
					return "§4";
				case "endermite":
					return "§d";
				case "bmod":
				case "mod":
				case "admin":
					return "§e";
				case "trusted":
					return "§6";
				case "musketeer":
					return "§5";
				default:
					break;

			}
		}
		return "§f";
	}

	private boolean isAlphanumericOrAmpersand(String input)
	{
		for(char character : input.toCharArray())
		{
			if(!Character.isLetterOrDigit(character) && character != '&')
			{
				return false;
			}
		}
		return true;
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

	private String timeleftGeneric(String player, String rank)
	{
		int allseconds = BUtil.getTimeLeft(player, rank);
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
				if(hour == 0)
				{
					timeString += " " + minute + " Minute";
					if(minute > 1)
					{
						timeString += "s";
					}
				}
				
			}
			if (minute == 0) 
			{
				if(hour == 0 && minute == 0)
				{
					timeString += " " + second + " Second";
					if(second > 1)
					{
						timeString += "s";
					}
				}
			}

			print = ": " + ChatColor.WHITE + timeString;
		}
		else 
		{
			print = "";
		}
		return print;
	}
}
