package net.auscraft.BlivUtils.executors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.config.ConfigAccessor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreditExecutor implements CommandExecutor
{
	private String user, pass, url; //MySQL Variables, read in from config.
	private BlivUtils b;
	private static final Logger log = Logger.getLogger("Minecraft");
	
	public CreditExecutor(BlivUtils instance)
	{
		b = instance;
		String[] vars = b.getCfg().getSQLVars();
		user = vars[0];
		pass = vars[1];
		url = vars[2];
	}
	
	public int getCreditsSingle(String playerName) throws SQLException
	{
		int value = -1;
		//Accepts the MySQL query, and outputs the credit value of the player
		Connection conn = DriverManager.getConnection(url, user, pass);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT value FROM CreditsDB WHERE playerName LIKE '" + playerName + "'");
		if(rs.next())
		{
			value = rs.getInt(1); //Column value 1 is the value of the "value" column.
		}
		//Else, value is -1, or account doesnt exist
		conn.close();
		
		return value;
	}
	
	public int editCreditsSingle(String playerName, int creditChange) throws SQLException
	{
		int value = 0; //Can't have negative credits!
		//Accepts the MySQL query, and outputs the credit value of the player
		Connection conn = DriverManager.getConnection(url, user, pass);
		Statement st = conn.createStatement();
		
		//Check if the player already exists. If not, create an entry for them.
		try
		{
			checkExists(st, playerName);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		//Grab existing credit value
		ResultSet rs = st.executeQuery("SELECT value FROM CreditsDB WHERE playerName LIKE '" + playerName + "'");
		if(rs.next())
		{
			value = rs.getInt(1);
			value += creditChange; //Add (or remove) the credits that were asked for.
			
			try
			{
				st.execute("UPDATE CreditsDB SET value=" + value + " WHERE playerName='" + playerName + "'");
				Player p = Bukkit.getServer().getPlayer(playerName); //CBF With UUID conversion.
				//If player exists on the server, send them a message telling them they've been credited.
				if(p != null)
				{
					try
					{
						String creditDebit = "credited ";
						//String creditAmount = "" + value;
						if(creditChange < 0)
						{
							creditDebit = "debited ";
						}
						b.printSuccess((CommandSender) p, "You have been " + creditDebit + ChatColor.WHITE + Math.abs(creditChange) + ChatColor.GREEN + " credits.");
					}
					catch(NullPointerException e)
					{
						e.printStackTrace();
					}
				}
			}
			catch(SQLException e)
			{
				log.severe("Could not update credit values in CreditsDB for " + playerName);
				e.printStackTrace();
			}
		}
		else
		{
			log.info("MySQL Error: Player does not exist in database, and passed the player creation section.");
		}
		
		conn.close();
		
		return value;
	}
	
	public void setCreditsSingle(String playerName, int value) throws SQLException
	{
		//Accepts the MySQL query, and outputs the credit value of the player
		Connection conn = DriverManager.getConnection(url, user, pass);
		Statement st = conn.createStatement();
		
		//Check if the player already exists. If not, create an entry for them.
		try
		{
			checkExists(st, playerName);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
			
		//Make sure value isnt negative.
		if(value < 0)
		{
			value = 0; //If the new credit value is below 0, just set it to 0.
		}
			
		try
		{
			st.execute("UPDATE CreditsDB SET value=" + value + " WHERE playerName='" + playerName + "'");
			Player p = Bukkit.getServer().getPlayer(playerName); //CBF With UUID conversion.
			//If player exists on the server, send them a message telling them they've been credited.
			if(p != null)
			{
				try
				{
					b.printSuccess((CommandSender) p, "Your credit value is now: " + ChatColor.WHITE + value);
				}
				catch(NullPointerException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch(SQLException e)
		{
			log.severe("Could not set credit values in CreditsDB for " + playerName);
			e.printStackTrace();
		}
		
		conn.close();
	}
	
	public void checkOwnBalance(CommandSender sender)
	{
		try
		{
			int creditValue = getCreditsSingle(sender.getName());
			if(creditValue < 0)
			{
				b.printError(sender, "An unexpected error occured.");
			}
			else
			{
				sender.sendMessage(ChatColor.GREEN + "Balance" + ChatColor.WHITE +  ": " + creditValue + " Credits");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean checkExists(String playerName) throws SQLException
	{
		Connection conn = DriverManager.getConnection(url, user, pass);
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT EXISTS(SELECT 1 FROM CreditsDB WHERE playerName LIKE '" + playerName + "')");
		
		if(rs.next())
		{
			if(!rs.getBoolean(1)) //Column 0, since there is only 1 column.
			{
				return false;
			}
			return true;
		}
		return true;
	}
	
	public boolean checkExists(Statement st, String playerName) throws SQLException
	{
		ResultSet rs = st.executeQuery("SELECT EXISTS(SELECT 1 FROM CreditsDB WHERE playerName LIKE '" + playerName + "')");
		if(rs.next())
		{
			if(!rs.getBoolean(1)) //Column 0, since there is only 1 column.
			{
				try
				{
					st.execute("INSERT INTO CreditsDB VALUES ('" + playerName + "', 0)");
				}
				catch(SQLException e)
				{
					log.severe("Could not add new player data to CreditsDB for " + playerName);
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	public void findTopX(CommandSender sender, int limit) throws SQLException
	{
		Connection conn = DriverManager.getConnection(url, user, pass);
		Statement st = conn.createStatement();
		
		ResultSet rs = st.executeQuery("SELECT playerName, value FROM CreditsDB ORDER BY value DESC LIMIT " + limit);
		if(rs.next())
		{
			String topPlayers = "";
			ChatColor alternate = ChatColor.DARK_GRAY;
			int alt = 0;
			topPlayers = ChatColor.DARK_GRAY + "| " + ChatColor.GREEN + rs.getString("playerName") + ChatColor.WHITE + ": " + rs.getInt("value") + "\n";
			while(rs.next())
			{
				if(alt == 1)
				{
					alternate = ChatColor.DARK_GRAY;
					alt = 0;
				}
				else
				{
					alternate = ChatColor.GRAY;
					alt = 1;
				}
				topPlayers += alternate + "| " + ChatColor.GREEN + rs.getString("playerName") + ChatColor.WHITE + ": " + rs.getInt("value") + "\n";
			}
			sender.sendMessage(ChatColor.YELLOW + " - - - " + ChatColor.GOLD + "Top " + limit + " Players" + ChatColor.YELLOW + " - - - \n" + topPlayers);
		}
		else
		{
			b.printError(sender, "Problem getting data");
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) 
	{
		if ((cmd.getName().equalsIgnoreCase("credits") && (sender instanceof Player)) || (cmd.getName().equalsIgnoreCase("credit") && (sender instanceof Player)))
		{
			//If no args, assume own balance output.
			if(args.length == 0)
			{
				checkOwnBalance(sender);
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
							int creditValue = getCreditsSingle(args[1]);
							b.printSuccess(sender, args[1] + "'s "+ ChatColor.GREEN + "Balance" + ChatColor.WHITE +  ": " + creditValue + " Credits");
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
						checkOwnBalance(sender);
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
									setCreditsSingle(args[1], Integer.parseInt(args[2]));
									b.printSuccess(sender, ChatColor.GREEN + "Set " + ChatColor.WHITE + args[1] + "'s" + ChatColor.GREEN +  " credits to " + ChatColor.WHITE + credits);
									//sender.sendMessage(ChatColor.GREEN + "Set " + ChatColor.WHITE + args[1] + "'s" + ChatColor.GREEN +  " credits to " + ChatColor.WHITE + credits);
								}
								catch(SQLException e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								b.printError(sender, "Enter amount to set to.");
							}
						}
						else
						{
							b.printError(sender, "Enter a player and amount to set to.");
						}
					}
					else
					{
						b.printError(sender, "You don't have permission to do this!");
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
									b.printError(sender, "Nothing to be done.");
									//sender.sendMessage(ChatColor.RED + "Nothing to be done.");
								}
								else
								{
									try
									{
										int result = editCreditsSingle(args[1], Integer.parseInt(args[2]));
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
											b.printSuccess(sender, addSubtract + ChatColor.WHITE + fixedcreditAmount + ChatColor.GREEN +  " credits " + toFrom + ChatColor.WHITE +  args[1] + "'s" + ChatColor.GREEN + " account");
											//sender.sendMessage(ChatColor.GREEN + addSubtract + ChatColor.WHITE + fixedcreditAmount + ChatColor.GREEN +  " credits " + toFrom + ChatColor.WHITE +  args[1] + "'s" + ChatColor.GREEN + " account");
											}
										else
										{
											b.printError(sender, "An unexpected error occured.");
										}
									}
									catch(NumberFormatException e)
									{
										b.printError(sender, "Not a valid number.");
									}
									catch(SQLException e)
									{
										e.printStackTrace();
									}
								}
							}
							else
							{
								b.printError(sender, "Enter amount to modify by.");
								//sender.sendMessage(ChatColor.RED + "Enter an amount to modify by.");
							}
						}
						else
						{
							b.printError(sender, "Enter a player and amount to modify by.");
							//sender.sendMessage(ChatColor.RED + "Enter a name and amount to modify by.");
						}
					}
					else
					{
						b.printError(sender, "You don't have permission to do this!");
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
							sender.sendMessage(ChatColor.GREEN + "Are you sure you want to possibly " + ChatColor.DARK_RED + "WIPE ALL PLAYER DATA" + ChatColor.GREEN + "?\n"
									+ ChatColor.GREEN + "/credits validate yes");
						}
						else if(args.length == 2)
						{
							//Do stuff
							
						}
						else
						{
							sender.sendMessage(ChatColor.RED + "You don't know what you're doing. Please don't use this command.");
						}
					}
					else
					{
						b.printError(sender, "Nice try.");
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
						findTopX(sender, limit);
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