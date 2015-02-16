package net.auscraft.BlivUtils.promotions;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PromoteExecuter implements CommandExecutor
{
	
	private Permission perms;
	private Economy econ;
	private HashMap<String, Integer> promoteCount;
	private Utilities util;

	public PromoteExecuter(BlivUtils instance)
	{
		promoteCount = BlivUtils.getPromote();
		perms = instance.setupPermissions();
		econ = instance.setupEconomy();
		util = instance.getUtil();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
	{
		if (cmd.getName().equalsIgnoreCase("buyrank") && (sender instanceof Player))
		{
			if (args.length == 0)
			{
				ChatColor a = ChatColor.RED;
				ChatColor b = a;
				ChatColor c = a;
				ChatColor d = a;
				ChatColor aa = ChatColor.GREEN;
				ChatColor bb = aa;
				ChatColor cc = aa;
				ChatColor dd = aa;
				// Endermite perms, unlocks at Ghast
				ChatColor e = ChatColor.STRIKETHROUGH;
				ChatColor ee = ChatColor.STRIKETHROUGH;
				
				if (!sender.hasPermission("blivutils.promote.magmaslime"))
				{
					a = ChatColor.STRIKETHROUGH;
					aa = a;
				}
				if (!sender.hasPermission("blivutils.promote.blaze"))
				{
					b = ChatColor.STRIKETHROUGH;
					bb = a;
				}
				if (!sender.hasPermission("blivutils.promote.pigzombie"))
				{
					c = ChatColor.STRIKETHROUGH;
					cc = c;
				}
				if (!sender.hasPermission("blivutils.promote.ghast"))
				{
					d = ChatColor.STRIKETHROUGH;
					dd = d;
				}
				if (sender.hasPermission("blivutils.promote.blaze"))
				{
					a = ChatColor.STRIKETHROUGH;
					aa = a;
				}
				if (sender.hasPermission("blivutils.promote.pigzombie"))
				{
					a = ChatColor.STRIKETHROUGH;
					b = a;
					aa = a;
					bb = a;
				}
				if (sender.hasPermission("blivutils.promote.ghast"))
				{
					a = ChatColor.STRIKETHROUGH;
					b = a;
					c = a;
					aa = a;
					bb = a;
					cc = a;
				}
				if (sender.hasPermission("blivutils.promote.done")) 
				{
					a = ChatColor.STRIKETHROUGH;
					b = a;
					c = a;
					d = a;
					aa = a;
					bb = a;
					cc = a;
					dd = a;
					// Endermite unlocking!
					e = ChatColor.DARK_PURPLE;
					ee = ChatColor.GREEN;
				}
				
				sender.sendMessage(ChatColor.GOLD + "Choose from the following ranks:\n"
						+ ChatColor.WHITE + " - " + ChatColor.GREEN + aa + "$" + "25,000 " + ChatColor.RED + a + "MagmaSlime\n"
						+ ChatColor.WHITE + " - " + ChatColor.GREEN + bb + "$" + "50,000 " + ChatColor.RED + b + "Blaze\n"
						+ ChatColor.WHITE + " - " + ChatColor.GREEN + cc + "$" + "75,000 " + ChatColor.RED + c + "PigZombie\n"
						+ ChatColor.WHITE + " - " + ChatColor.GREEN + dd + "$" + "100,000 " + ChatColor.RED + d + "Ghast\n"
						+ ChatColor.WHITE + " - " + ChatColor.GREEN + ee + "$" + "100,000 " + ChatColor.DARK_PURPLE + e
						+ "Endermite (15 Days)\n" + ChatColor.GOLD + "/buyrank <rank>");
				return true;
			}
			
			if (args.length > 0) 
			{
				if(sender instanceof Player)
				{
					int rankValue = -1;
					if((args[0].equalsIgnoreCase("MagmaSlime") && (sender.hasPermission("blivutils.promote.magmaslime"))))
					{
						rankValue = 1;
					}
					else if((args[0].equalsIgnoreCase("Blaze") && (sender.hasPermission("blivutils.promote.blaze"))))
					{
						rankValue = 2;
					}
					else if((args[0].equalsIgnoreCase("PigZombie") && (sender.hasPermission("blivutils.promote.pigzombie"))))
					{
						rankValue = 3;
					}
					else if((args[0].equalsIgnoreCase("Ghast") && (sender.hasPermission("blivutils.promote.Ghast"))))
					{
						rankValue = 4;
					}
					else if((args[0].equalsIgnoreCase("Endermite") && (sender.hasPermission("blivutils.promote.done"))))
					{
						rankValue = 5;
					}
					
					if(rankValue == -1)
					{
						String reqGroup = "";
						if(args[0].equalsIgnoreCase("MagmaSlime"))
						{
							reqGroup = "Ocelot";
						}
						else if(args[0].equalsIgnoreCase("Blaze"))
						{
							reqGroup = "MagmaSlime";
						}
						else if(args[0].equalsIgnoreCase("PigZombie"))
						{
							reqGroup = "Blaze";
						}
						else if(args[0].equalsIgnoreCase("Ghast"))
						{
							reqGroup = "PigZombie";
						}
						else if(args[0].equalsIgnoreCase("Endermite"))
						{
							reqGroup = "Ghast";
						}
						
						util.printError(sender, "You can't buy this at your rank! Required rank: " + reqGroup);
					}
					else
					{
						rankQuery((Player) sender, rankValue);
					}
				}
			}
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

						//Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + args[0]	+ " group add Admin \"\" " + length);
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

		if (cmd.getName().equalsIgnoreCase("promoteme") && (sender instanceof Player))
		{
			String playerName = sender.getName();
			if (promoteCount.containsKey(playerName) && (promoteCount.get(playerName) != 0) && ((promoteCount.get(playerName) == 1) || (promoteCount.get(playerName) == 2)
													|| (promoteCount.get(playerName) == 3) || (promoteCount.get(playerName) == 4) || (promoteCount.get(playerName) == 5)))
			{
				int rank = promoteCount.get(playerName);
				double price = 0.0;
				price = getRankPrice(rank);
				Player player = (Player) sender;
				String rankString = getRankName(rank);

				if (econ.has(player, price)) 
				{
					econ.withdrawPlayer(player, price);
					String[] groups = perms.getPlayerGroups(null, player);
					// Don't Demote the Endermite Renters!
					if (rank != 5) 
					{
						for (int i = 0; i < groups.length; i++) 
						{
							perms.playerRemoveGroup(null, player, groups[i]);
						}
						perms.playerAddGroup(null, player, rankString);
					}
					else if (rank == 5) 
					{
						PermissionUser user = PermissionsEx.getUser(sender.getName());
						user.addGroup("Endermite", null);
						user.setOption("group-Endermite-until", "1296000");
					}
					util.logInfo("Player " + sender.getName() + " has been promoted to " + rankString);
					util.logtoFile("Player " + sender.getName() + " has been promoted to " + rankString, null);
					promoteCount.put(playerName, 0);
					if ((rank != 5))
					{
						util.printSuccess(sender, "You have been promoted to " + ChatColor.RED + rankString);
					} 
					else if (rank == 5) 
					{
						util.printSuccess(sender, "You have been promoted to " + ChatColor.RED + rankString + ChatColor.WHITE + " for " + ChatColor.GREEN + "15 days");
					}
					return true;

				}
				else 
				{
					util.printError(sender, "You dont have sufficient funds to be promoted!");
					return false;
				}
			}
			else
			{
				util.printInfo(sender, "You havent specified a rank to purchase!");
				return false;
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

	private String getRankName(int rank)
	{
		String name;
		if (rank == 1) 
		{
			name = "MagmaSlime";
		}
		else if (rank == 2)
		{
			name = "Blaze";
		} 
		else if (rank == 3)
		{
			name = "PigZombie";
		} 
		else if (rank == 4)
		{
			name = "Ghast";
		} 
		else if (rank == 5)
		{
			name = "Endermite";
		}
		else 
		{
			name = "null";
			util.logSevere("Rank entered doesnt match a rank listed!");
		}
		return name;
	}

	private double getRankPrice(int rank)
	{
		double price = 0.0;
		switch(rank)
		{
			case 1: price = 25000.0;
			break;
			case 2: price = 50000.0;
			break;
			case 3: price = 75000.0;
			break;
			case 4: case 5: price = 10000.0;
			break;
			default: price = -1.0;
			break;
		}
		return price;
	}

	private String getRankPriceRead(int rank, boolean strike)
	{
		ChatColor a = ChatColor.STRIKETHROUGH;
		ChatColor b = a;
		if (strike = true) 
		{
			a = ChatColor.GREEN;
			b = ChatColor.WHITE;
		}
		String price;
		if (rank == 1) 
		{
			price = b + "$" + ChatColor.GREEN + a + "25,000";
		}
		else if (rank == 2)
		{
			price = b + "$" + ChatColor.GREEN + a + "50,000";
		} 
		else if (rank == 3)
		{
			price = b + "$" + ChatColor.GREEN + a + "75,000";
		}
		else if (rank == 4) 
		{
			price = b + "$" + ChatColor.GREEN + a + "100,000";
		}
		else if (rank == 5)
		{
			price = b + "$" + ChatColor.GREEN + a + "100,000";
		}
		else
		{
			price = "null";
			util.logSevere("Rank entered doesnt match a rank listed!");
		}
		return price;
	}

	private boolean checkifNull(Player player) 
	{
		if (perms == null || econ == null)
		{
			player.sendMessage("Whoops! Report this to the Musketeers.");
			util.logSevere("Permissions or Economy Null -- Restart Vault/BlivUtils.");
			return false;
		}
		return true;
	}

	private boolean rankQuery(Player player, int rank) 
	{
		if (checkifNull(player)) {
			String rankName = getRankName(rank);
			String price = getRankPriceRead(rank, false);
			if ((rankName == "null") || (price == "-1"))
			{
				player.sendMessage("Could not purchase rank");
				util.logSevere("Player " + player.getName() + " tried to purchase rank " + rankName + " but failed. (Due to rankname/price being null)");
			}
			else if ((rank == 1) || (rank == 2) || (rank == 3) || (rank == 4)) 
			{
				player.sendMessage("Sure you want to spend " + price + ChatColor.RESET + " on " + ChatColor.RED + rankName + "?"
								+ "Type " + ChatColor.GREEN + "/promoteme"	+ ChatColor.RESET + " to Accept");
				promoteCount.put(player.getName(), rank);
				return true;
			}
			else if (rank == 5)
			{
				player.sendMessage("Sure you want to spend " + price + ChatColor.RESET + " on " + ChatColor.DARK_PURPLE	+ rankName + " (15 Days)?"
								+ "Type " + ChatColor.GREEN + "/promoteme"	+ ChatColor.RESET + " to Accept");
				promoteCount.put(player.getName(), rank);
				return true;
			} 
			else 
			{
				player.sendMessage("Could not purchase rank");
				util.logSevere("Player " + player.getName() + " tried to purchase rank " + rankName + " but failed.");
				return false;
			}
		}
		// if(!checkifNull(player))
		else
		{
			player.sendMessage("Could not purchase rank.");
			util.logSevere("Player " + player.getName() + " tried to purchase rank, but permissions/economy was not active.");
			return true;
		}
		return false;
	}
	
}
