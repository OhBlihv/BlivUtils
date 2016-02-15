package net.auscraft.BlivUtils.purchases;

import net.auscraft.BlivUtils.util.BUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.regex.Pattern;

public class Broadcast implements CommandExecutor
{

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) 
	{
		if (cmd.getName().equalsIgnoreCase("purch")	&& (sender.hasPermission("blivutils.purch"))) //Only for Console
		{
			
			if (args.length > 1) // /purch <isrank> <name> <package> <price>
			{
				//String as[] = args;
				Player p = null;
				try
				{
					p = (Player) Bukkit.getOfflinePlayer(args[1]);
				}
				catch(Exception e)
				{
					BUtil.logError("Player " + args[1] + " is not online.");
				}
				String message = "% &r&ahas purchased &6@ &afor &2$&f#&a!";
				String packageName = args[2];
				
				if(args[3].equals("0.00")) //If the package is free, change the wording to suit 'redeeming'
				{
					message = "% &r&ahas redeemed &6@&a.";
				}
				
				if(packageName.equalsIgnoreCase("donate"))
				{
					message = "% &r&ahas donated $&f#&a.";
				}
				/*else if(packageName.contains("EnderRank"))
				{
					
				}*/
				
				if(p != null)
				{
					message = translateVariables(message, p, packageName, args[3]);
				}
				else
				{
					message = translateVariables(message, args[1], packageName, args[3]);
				}
				
				message = BUtil.translateColours(message);
				
				Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "|| " + ChatColor.GRAY + "[" + ChatColor.GREEN	+ ChatColor.BOLD + "Thanks!" + ChatColor.RESET
						+ ChatColor.GRAY + "]" + " " + ChatColor.YELLOW	+ ChatColor.ITALIC + ChatColor.BOLD + message);
				return true;
			}
			
			return false;
		}
		
		BUtil.printError(sender, "You can't do this!");
		return true;
	}
	
	public static String addEnderRank(String[] args)
	{
		PermissionUser user = PermissionsEx.getUser(args[1]);
		String packageName;
		//Player gets [Enderman] rank
		if(Double.parseDouble(args[3]) < 15.00)
		{
			//If the user has a prefix, don't set their prefix
			if(!(user.getPrefix().length() == 0 || user.getPrefix() == null))
			{
				user.setPrefix("&7[&5Enderman&7] ", null);
			}
			packageName = ChatColor.DARK_PURPLE + "Enderman";
			user.setOption("EnderRankValue", "1");
		}
		//Player gets [EnderDragon]
		else if(Double.parseDouble(args[3]) < 30.00)
		{
			if(!(user.getPrefix().length() == 0 || user.getPrefix() == null))
			{
				user.setPrefix("&7[&4EnderDragon&7] ", null);
			}
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "enderperm add " + args[1] + " EnderDragon");
			packageName = ChatColor.DARK_RED + "EnderDragon";
			user.setOption("EnderRankValue", "2");
		}
		//Player gets [Wither] >= $30/month
		else
		{
			if(!(user.getPrefix().length() == 0 || user.getPrefix() == null))
			{
				user.setPrefix("&7[&8Wither&7] ", null);
			}
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "enderperm add " + args[1] + " Wither");
			packageName = ChatColor.DARK_GRAY + "Wither";
			user.setOption("EnderRankValue", "3");
		}
		
		return packageName;
	}

	private static final Pattern    PACKAGE_PATTERN = Pattern.compile("[@]"),
									SPACE_PATTERN = Pattern.compile("[_]"),
									NAME_PATTERN = Pattern.compile("[%]"),
									PRICE_PATTERN = Pattern.compile("[#]");
	
	private String translateVariables(String fixedString, Player player, String packageName, String price)
	{
		//Package name is first, since the second function replaces underscores, and the player name has not been added yet.
		//Replace @ with package name
		fixedString = PACKAGE_PATTERN.matcher(fixedString).replaceAll(packageName);
				
		//Replace underscores with spaces (for multi worded packages).
		fixedString = SPACE_PATTERN.matcher(fixedString).replaceAll(" ");
		
		//Replace % with player name
		String playerName = player.getName();
		if(player.getDisplayName() != null && player.getDisplayName().length() != 0)
		{
			playerName = player.getDisplayName();
		}
		fixedString = NAME_PATTERN.matcher(fixedString).replaceAll(playerName);
		
		//Replace # with price (if applicable).
		fixedString = PRICE_PATTERN.matcher(fixedString).replaceAll(price);
		
		
		return fixedString;
	}
	
	private String translateVariables(String fixedString, String p, String packageName, String price)
	{
		//Package name is first, since the second function replaces underscores, and the player name has not been added yet.
		//Replace @ with package name
		fixedString = PACKAGE_PATTERN.matcher(fixedString).replaceAll(packageName);
				
		//Replace underscores with spaces (for multi worded packages).
		fixedString = SPACE_PATTERN.matcher(fixedString).replaceAll(" ");
		
		//Replace % with player name
		fixedString = NAME_PATTERN.matcher(fixedString).replaceAll(p);
		
		//Replace # with price (if applicable).
		fixedString = PRICE_PATTERN.matcher(fixedString).replaceAll(price);
		
		return fixedString;
	}
}
