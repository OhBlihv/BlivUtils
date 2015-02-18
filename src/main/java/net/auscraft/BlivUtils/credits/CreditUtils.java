package net.auscraft.BlivUtils.credits;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.auscraft.BlivUtils.utils.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreditUtils {
	
	private Utilities util;
	private String user, pass, url; //MySQL Variables, read in from config.
	
	
	public CreditUtils(Utilities inUtil)
	{
		util = inUtil;
		String[] vars = util.getInstance().getCfg().getSQLVars();
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
						util.printSuccess((CommandSender) p, "You have been " + creditDebit + ChatColor.WHITE + Math.abs(creditChange) + ChatColor.GREEN + " credits.");
					}
					catch(NullPointerException e)
					{
						e.printStackTrace();
					}
				}
			}
			catch(SQLException e)
			{
				util.logSevere("Could not update credit values in CreditsDB for " + playerName);
				e.printStackTrace();
			}
		}
		else
		{
			util.logSevere("MySQL Error: Player does not exist in database, and passed the player creation section.");
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
					util.printSuccess((CommandSender) p, "Your credit value is now: " + ChatColor.WHITE + value);
				}
				catch(NullPointerException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch(SQLException e)
		{
			util.logSevere("Could not set credit values in CreditsDB for " + playerName);
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
				util.printError(sender, "An unexpected error occured.");
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
					util.logSevere("Could not add new player data to CreditsDB for " + playerName);
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
			util.printError(sender, "Problem getting data");
		}
	}
	
	public CreditContainer[] getAllInDB()
	{
		CreditContainer[] playerData = new CreditContainer[100];
		try
		{
			Connection conn = DriverManager.getConnection(url, user, pass);
			Statement st = conn.createStatement();
			
			//Grab all player data
			ResultSet rs = st.executeQuery("SELECT playerName, value FROM CreditDB");
			
			int i = 0;
			while(rs.next())
			{
				playerData[i] = new CreditContainer(rs.getString("playerName"), rs.getInt("value"));
				i++;
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		
		return playerData;
	}
	
	public void cleanPlayers()
	{
		try
		{
		Connection conn = DriverManager.getConnection(url, user, pass);
		Statement st = conn.createStatement();
		
		//Delete any players who do not have valid entries.
		st.executeQuery("DELETE FROM CreditsDB WHERE value <= 0");
		
		//TODO: Clean players who have not played Minigames before.
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
}
 