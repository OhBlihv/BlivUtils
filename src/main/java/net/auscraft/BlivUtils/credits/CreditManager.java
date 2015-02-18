package net.auscraft.BlivUtils.credits;

import java.sql.SQLException;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreditManager implements CommandExecutor
{
	private Utilities util;
	private CreditUtils cUtils;
	
	public CreditManager(BlivUtils instance)
	{
		util = instance.getUtil();
		cUtils = new CreditUtils(util);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) 
	{
		if ((cmd.getName().equalsIgnoreCase("credits") && (sender instanceof Player)) || (cmd.getName().equalsIgnoreCase("credit") && (sender instanceof Player)))
		{
			//If no args, assume own balance output.
			if(args.length == 0)
			{
				cUtils.checkOwnBalance(sender);
			}
			
			else
			{
				//Below here are the different sub-sections of the /credits command.
				//Pretty much equates to the command checking ones of the other executors,
				//although this one is all checking args[0] to sort.
				
				//Command to check other players (or your own if no args[1]) balance of credits.
				if(args[0].equals("check"))
				{
					//Check others balance here
					if(args.length >= 2) //Other balance
					{
						try
						{
							int creditValue = cUtils.getCreditsSingle(args[1]);
							util.printSuccess(sender, args[1] + "'s "+ ChatColor.GREEN + "Balance" + ChatColor.WHITE +  ": " + creditValue + " Credits");
							//sender.sendMessage(args[1] + "'s "+ ChatColor.GREEN + "Balance" + ChatColor.WHITE +  ": " + creditValue + " Credits");
							return true;
						}
						catch(SQLException e)
						{
							e.printStackTrace();
						}
					}
					else //Self balance
					{
						cUtils.checkOwnBalance(sender);
						return true;
					}
					return true;
				}
				
				//Admin command to set a player's credits
				else if(args[0].equals("set"))
				{
					if(sender.hasPermission("blivutils.credits.admin"))
					{
						if(args.length >= 2) // /credits set <name>
						{
							if(args.length >= 3) // /credits set <name> <#>
							{
								try
								{
									int credits = Integer.parseInt(args[2]);
									if(credits < 0)
									{
										credits = 0;
									}
									cUtils.setCreditsSingle(args[1], Integer.parseInt(args[2]));
									util.printSuccess(sender, ChatColor.GREEN + "Set " + ChatColor.WHITE + args[1] + "'s" + ChatColor.GREEN +  " credits to " + ChatColor.WHITE + credits);
									//sender.sendMessage(ChatColor.GREEN + "Set " + ChatColor.WHITE + args[1] + "'s" + ChatColor.GREEN +  " credits to " + ChatColor.WHITE + credits);
								}
								catch(SQLException e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								util.printError(sender, "Enter amount to set to.");
							}
						}
						else
						{
							util.printError(sender, "Enter a player and amount to set to.");
						}
					}
					else
					{
						util.printError(sender, "You don't have permission to do this!");
					}
				}
				
				//Admin command to add or remove a player's credits.
				else if(args[0].equals("edit"))
				{
					if(sender.hasPermission("blivutils.credits.admin"))
					{
						//TODO: Check if a player has ever played AusCraft before doing SQL Stuffs.
						//The above seems impractical, since its a global credit system, and it's bad to check
						//if someone has played a server that has low players, and expect that result to be global.
						if(args.length >= 2) // /credits edit <name>
						{
							if(args.length >= 3) // /credits edit <name> <#> --> This is the intended syntax
							{
								if((args[2].equals("0")) || (args[2].equals("-0")))
								{
									util.printInfo(sender, "Nothing to be done.");
								}
								else
								{
									try
									{
										int result = cUtils.editCreditsSingle(args[1], Integer.parseInt(args[2]));
										if(result != -1)
										{
											String addSubtract = "Added";
											String toFrom = "to ";
											String creditAmount = " " + args[2];
											String fixedcreditAmount = "";
											if((Integer.parseInt(args[2])) < 0)
											{
												addSubtract = "Subtracted ";
												toFrom = "from ";
												fixedcreditAmount = creditAmount.substring(2);
											}
											else
											{
												fixedcreditAmount = creditAmount;
											}
											
											util.printSuccess(sender, addSubtract + ChatColor.WHITE + fixedcreditAmount + ChatColor.GREEN +  " credits " + toFrom + ChatColor.WHITE +  args[1] + "'s" + ChatColor.GREEN + " account");
											}
										else
										{
											util.printError(sender, "An unexpected error occured.");
										}
									}
									catch(NumberFormatException e)
									{
										util.printError(sender, "Not a valid number.");
									}
									catch(SQLException e)
									{
										e.printStackTrace();
									}
								}
							}
							else
							{
								util.printInfo(sender, "Enter amount to modify by.");
							}
						}
						else
						{
							util.printInfo(sender, "Enter a player and amount to modify by.");
						}
					}
					else
					{
						util.printError(sender, "You don't have permission to do this!");
					}
					return true;
				}
				
				//Checks if players actually exist, if not, drops them from the DB.
				//This should ONLY be run on Minigames.
				else if(args[0].equals("validate"))
				{
					if(sender.hasPermission("blivutils.credits.validate"))
					{
						if(args.length == 1)
						{
							if(Bukkit.getServer().getName().equals("Minigames"))
							{
								sender.sendMessage(ChatColor.GREEN + "Are you sure you want to possibly " + ChatColor.DARK_RED + "WIPE ALL PLAYER DATA" + ChatColor.GREEN + "?\n"
										+ ChatColor.GREEN + "/credits validate yes");
							}
							else
							{
								util.printError(sender, "This command can only be run on Minigames!");
							}
						}
						else if(args.length == 2)
						{
							if(Bukkit.getServer().getName().equals("Minigames"))
							{
								CreditContainer[] entries = cUtils.getAllInDB();
								
								for(CreditContainer i : entries)
								{
									//First check, -- Check if player has negative or zero credits.
									if(i.getValue() <= 0)
									{
										i.nullPlayer();
									}
								}
								
							}
							
						}
						else
						{
							sender.sendMessage(ChatColor.RED + "You don't know what you're doing. Please don't use this command.");
						}
					}
					else
					{
						util.printError(sender, "Nice try.");
					}
				}
				
				
				//Returns the top args[1] players with the most credits (low priority)
				else if(args[0].equals("top"))
				{
					//If user puts an amount, use it. Otherwise top 5.
					int limit = 5;
					
					if(args.length >= 2)
					{
						limit = Integer.parseInt(args[1]);
						
						if(limit > 10)
						{
							limit = 10;
						}
						else if(limit <= 1)
						{
							limit = 1;
						}
						//else
						//{
						//	limit = Integer.parseInt(args[1]);
						//}

					}
					
					try
					{
						cUtils.findTopX(sender, limit);
					}
					catch(SQLException e)
					{
						e.printStackTrace();
					}
					//SELECT amount FROM mytable ORDER BY amount DESC LIMIT 5
					return true;
				}
				
				else //Assume menu.
				{
					String creditSet = "", creditEdit = "";
					if(sender.hasPermission("blivutils.credits.admin"))
					{
						creditSet = ChatColor.WHITE + "/credits set <player> <#> - " + ChatColor.GREEN  + "Set a players credit amount\n";
						creditEdit = ChatColor.WHITE + "/credits edit <player> <#/-#> - " + ChatColor.GREEN  + "Add or Remove a players credits\n";
					}
					sender.sendMessage(ChatColor.YELLOW + "- - - " + ChatColor.BLUE + "" + ChatColor.BOLD + "Aus"	+ ChatColor.WHITE + ChatColor.BOLD + "Craft" + ChatColor.GREEN + " Credit Menu" + ChatColor.YELLOW + " - - -" + "\n"
							+ ChatColor.WHITE + "/credits - " + ChatColor.GREEN + "List your balance\n"
							+ ChatColor.WHITE + "/credits check [player] - " + ChatColor.GREEN + "Check your/another players credits\n"
							+ ChatColor.WHITE + "/credits top [#] - " + ChatColor.GREEN  + "Check the players with the most credits\n"
							//Permission based Admin command /credits set <player> <#>
							+ creditSet
							//Permission based Admin command /credits edit <player> <#>
							+ creditEdit
							);
				}
			}
		}
		
		
		return true;
	}
}