package net.auscraft.BlivUtils.purchases;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minecraftdimensions.bungeesuitechat.managers.PlayerManager;
import com.minecraftdimensions.bungeesuitechat.objects.BSPlayer;

public class Broadcast implements CommandExecutor
{
	private Logger log;
	private Utilities util;
	
	public Broadcast(BlivUtils instance)
	{
		log = instance.getLogger();
		util = instance.getUtil();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) 
	{
		if (cmd.getName().equalsIgnoreCase("purch")	&& (sender.hasPermission("blivutils.purch"))) //Only for Console
		{
			
			if (args.length > 1) // /purch <isrank> <name> <package> <price>
			{
				//String as[] = args;
				Player p = (Player) Bukkit.getOfflinePlayer(args[1]);
				String message = "% &r&ahas purchased &6@ &afor &2$&f#&a!";
				
				if(args[0].equals("true")) //Is a rank upgrade, requires name colour change
				{
					Nicknames nick = new Nicknames(log);
					nick.nickPlayer(p);
				}
				
				if(args[3].equals("0.00")) //If the package is free, change the wording to suit 'redeeming'
				{
					message = "% &r&ahas redeemed &6@&a.";
				}
				
				if(args[2].equalsIgnoreCase("donate"))
				{
					message = "% &r&ahas donated $&f#&a.";
				}
				
				message = translateVariables(message, p, args[2], args[3]);
				message = util.translateColours(message);
				
				Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "|| " + ChatColor.GRAY + "[" + ChatColor.GREEN	+ ChatColor.BOLD + "Thanks!" + ChatColor.RESET
						+ ChatColor.GRAY + "]" + " " + ChatColor.YELLOW	+ ChatColor.ITALIC + ChatColor.BOLD + message);
				return true;
			}
			else 
			{
				return false;
			}
		}
		else
		{
			util.printError(sender, "You can't do this!");
		}
		return false;
	}
	
	private String translateVariables(String fixedString, Player p, String packageName, String price)
	{
		//Package name is first, since the second function replaces underscores, and the player name has not been added yet.
		//Replace @ with package name
		Pattern packagePattern = Pattern.compile("[@]");
		fixedString = packagePattern.matcher(fixedString).replaceAll(packageName);
				
		//Replace underscores with spaces (for multi worded packages).
		Pattern spacePattern = Pattern.compile("[_]");
		fixedString = spacePattern.matcher(fixedString).replaceAll(" ");
		
		//Replace % with player name
		BSPlayer bsp = PlayerManager.getPlayer(p);
		Pattern namePattern = Pattern.compile("[%]");
		String playerName = p.getName();
		if(bsp.hasNickname())
		{
			playerName = bsp.getNickname();
		}
		fixedString = namePattern.matcher(fixedString).replaceAll(playerName);
		
		//Replace # with price (if applicable).
		Pattern pricePattern = Pattern.compile("[#]");
		fixedString = pricePattern.matcher(fixedString).replaceAll(price);
		
		
		return fixedString;
	}
}
