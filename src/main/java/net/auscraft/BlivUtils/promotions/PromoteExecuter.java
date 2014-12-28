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
			if (args.length == 0) {
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
			
			//TODO: Fix this up into one method with variable imports.
			if (args.length > 0) 
			{
				if (args[0].equalsIgnoreCase("MagmaSlime"))
				{
					if (sender.hasPermission("blivutils.promote.magmaslime") && !sender.hasPermission("blivutils.promote.blaze") 
							&& !sender.hasPermission("blivutils.promote.pigzombie")	&& !sender.hasPermission("blivutils.promote.ghast")
							|| !(sender instanceof Player))
					{
						Player player = (Player) sender;
						rankQuery(player, 1);
					} 
					else 
					{
						util.printInfo(sender, ChatColor.GREEN + "You can't buy this at your rank! You can buy it at " + ChatColor.BLUE + "Ocelot");
						return false;
					}
				}
				else if (args[0].equalsIgnoreCase("Blaze"))
				{
					if (sender.hasPermission("blivutils.promote.blaze")	&& !sender.hasPermission("blivutils.promote.pigzombie")	&& !sender.hasPermission("blivutils.promote.ghast")
							|| !(sender instanceof Player)) 
					{
						Player player = (Player) sender;
						rankQuery(player, 2);
					}
					else 
					{
						util.printInfo(sender, ChatColor.GREEN	+ "You can't buy this at your rank! You can buy it at " + ChatColor.RED + "MagmaSlime only!");
						return false;
					}
				}
				else if (args[0].equalsIgnoreCase("PigZombie"))
				{
					if (sender.hasPermission("blivutils.promote.pigzombie")	&& !sender.hasPermission("blivutils.promote.ghast")	|| !(sender instanceof Player)) {
						Player player = (Player) sender;
						rankQuery(player, 3);
					}
					else
					{
						util.printInfo(sender, ChatColor.GREEN + "You can't buy this at your rank! You can buy it at " + ChatColor.RED + "Blaze");
						return false;
					}
				}
				else if (args[0].equalsIgnoreCase("Ghast"))
				{
					if (sender.hasPermission("blivutils.promote.ghast")	&& !sender.hasPermission("blivutils.promote.done") || !(sender instanceof Player)) {
						Player player = (Player) sender;
						rankQuery(player, 4);
					} 
					else
					{
						util.printInfo(sender, ChatColor.GREEN + "You can't buy this at your rank! You can buy it at " + ChatColor.RED + "PigZombie");
						return false;
					}
				} 
				else if (args[0].equalsIgnoreCase("Endermite")) 
				{
					if ((sender.hasPermission("blivutils.promote.done") || !(sender instanceof Player)))
					{
						Player player = (Player) sender;
						rankQuery(player, 5);
					} 
					else 
					{
						util.printInfo(sender, ChatColor.GREEN + "You can't rent this at your rank! You can rent it at " + ChatColor.RED + "Ghast");
						return false;
					}
				}
				else
				{
					util.printInfo(sender, ChatColor.GREEN + "You didnt specify a valid rank!");
					return false;
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
					if (args[1] != null)
					{
						int length = 0;
						String numberPlusTimeframe = "";
						if (args.length >= 3) 
						{
							if (args[2].contains("day"))
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
						util.logtoFile("Player " + args[0] + " promoted to Admin for " + numberPlusTimeframe);
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
					util.logtoFile("Player " + sender.getName() + " has been promoted to " + rankString);
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
			if (args.length == 0) // Self
			{ 
				Player p = (Player) sender;
				String rank = util.getActiveRank(p.getName());
				String outputMessage;

				if (rank != "") 
				{
					outputMessage = timeleftGeneric(p, rank, false);
					sender.sendMessage(outputMessage);
					return true;
				} 
				else
				{
					util.printInfo(sender, ChatColor.GOLD + "You dont have an active rank!");
					return true;
				}
			}
			else if (args.length >= 1) // Other Player
			{
				if ((sender.hasPermission("blivutils.promote.admin.check")))
				{
					OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]); // Shut up, I don't care for now.
					
					if (p != null) 
					{
						String rank = util.getActiveRank(p.getName());
						String outputMessage;

						if (rank != "")
						{
							outputMessage = timeleftGeneric(p, rank, true);
							sender.sendMessage(outputMessage);
							return true;
						} 
						else 
						{
							util.printInfo(sender, ChatColor.GOLD + p.getName()	+ " doesn't have an active rank!");
							return true;
						}
					}
					else 
					{
						util.printError(sender, ChatColor.RED + "Player does not exist!");
					}
				} 
				else 
				{
					util.printError(sender, ChatColor.RED + "You don't have permission to check other player's time!");
				}
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
						rank = "Enderdragon";
					}
					else if(args[1].equalsIgnoreCase("Admin"))
					{
						rank = "Admin";
					}
					else
					{
						//Rank not found
						util.printError(sender, "That is not a valid timed rank!");
						return true;
					}
					
					int timeLeft = Integer.parseInt(user.getOption("group-" + rank + "-until"));
					int length = 0;
					String timeFormat = "";

					if (args[2].contains("day"))
					{
						// Seconds multiplied by that smaller number to get args[1] day(s)
						length = (Integer.parseInt(args[1])) * 86400;
						if (args[1] == "1")
						{
							timeFormat = "1 day";
						} 
						else 
						{
							timeFormat = args[1] + " days";
						}
					} 
					else if (args[2].contains("month")) 
					{
						// Seconds multiplied by that giant number to get args[1] month(s)
						length = (Integer.parseInt(args[1])) * 2592000;
						if (args[1] == "1")
						{
							timeFormat = "1 month";
						}
						else
						{
							timeFormat = args[1] + " months";
						}
					}

					timeLeft += length;
					String sTimeLeft = "" + timeLeft;
					user.setOption("group-" + rank + "-until", sTimeLeft);
					util.printSuccess(sender, ChatColor.GREEN + "Updated " + args[0] + "'s " + rank + " time by " + timeFormat);

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
						util.logtoFile(playerName + "'s prefix has been set to " + enteredPrefix);
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

	private String timeleftGeneric(OfflinePlayer p, String rank, boolean otherPlayer) // Oh man, he just used a control flag. Raise the pitchforks.
	{
		int allseconds = util.getTimeLeft(p.getName(), rank);
		String timeString = "", otherPlayerString = "", print;

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
				timeString += day + "Day (s) ";
			}
			if (hour != 0)
			{
				timeString += hour + " Hour(s) ";
			}
			if (minute != 0) 
			{
				timeString += minute + " Minutes(s) ";
			}
			if (second != 0) 
			{
				timeString += second + " Second(s) ";
			}

			if (otherPlayer) // Flag if the check is for the player themselves, or another player. If so, change the output.
			{
				otherPlayerString = ChatColor.DARK_GREEN + " for " + ChatColor.GREEN + p.getName();
			}

			// String print = ChatColor.GOLD + "" + days + hours + minutes + seconds + ChatColor.DARK_GREEN + "\nRemaining of " + ChatColor.GREEN + rank;
			print = ChatColor.GOLD + "" + timeString + ChatColor.DARK_GREEN + "\nRemaining of " + ChatColor.GREEN + rank + otherPlayerString;
		}
		else 
		{
			print = ChatColor.RED + "Your rank has expired!";
		}
		return print;
	}

	// Rank Values
	// 1 = MagmaSlime
	// 2 = Blaze
	// 3 = PigZombie
	// 4 = Ghast
	// 5 = Endermite
	private void changePlayerState(Player player, Integer rank)
	{
		String playerName = player.getName();
		promoteCount.put(playerName, rank);
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
		int price = 0;
		if (rank == 1)
		{
			price = 25000;
		} 
		else if (rank == 2)
		{
			price = 50000;
		}
		else if (rank == 3) 
		{
			price = 75000;
		}
		else if (rank == 4)
		{
			price = 100000;
		} 
		else if (rank == 5) 
		{
			price = 100000;
		} 
		else 
		{
			price = -1;
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
		boolean a = true;
		if (perms == null || econ == null)
		{
			player.sendMessage("Whoops! Report this to the Musketeers.");
			util.logSevere("Permissions or Economy Null -- Restart Vault/BlivUtils.");
			a = false;
		}
		return a;
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
				player.sendMessage("Sure you want to spend " + price + ChatColor.RESET + " on " + ChatColor.RED + rankName + "?");
				player.sendMessage("Type " + ChatColor.GREEN + "/promoteme"	+ ChatColor.RESET + " to Accept");
				changePlayerState(player, rank);
				return true;
			}
			else if (rank == 5)
			{
				player.sendMessage("Sure you want to spend " + price + ChatColor.RESET + " on " + ChatColor.DARK_PURPLE	+ rankName + " (15 Days)?");
				player.sendMessage("Type " + ChatColor.GREEN + "/promoteme"	+ ChatColor.RESET + " to Accept");
				changePlayerState(player, rank);
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
