package net.auscraft.BlivUtils.purchases;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import net.auscraft.BlivUtils.BlivUtils;

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
	BlivUtils b;
	Logger log;
	
	public Broadcast(BlivUtils instance)
	{
		b = instance;
		log = b.getLogger();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) 
	{
		if (cmd.getName().equalsIgnoreCase("purch")	&& (sender.hasPermission("blivutils.purch"))) //Only for Console
		{
			String message = "";
			if (args.length > 1) // /purch <isrank> <name> <word [word [word...]]> 
			{
				String as[] = args;
				Player p = (Player) Bukkit.getOfflinePlayer(as[1]);
				
				if(as[0].equals("true")) //Is a rank upgrade, requires name colour change
				{
					Nicknames nick = new Nicknames(b);
					nick.nickPlayer(p);
				}
				
				int i = as.length;
				for (int j = 2; j < i; j++)
				{
					String data = as[j];
					message = (message += data + " ");
				}
				
				message = translatePlayer(message, p);
				message = translateColours(message);
				
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
			b.printError(sender, "You can't do this!");
		}
		return false;
	}
	
	private String translateColours(String toFix)
	{
		Pattern chatColorPattern = Pattern.compile("(?i)&([0-9A-Fa-f-l-oL-OrR])"); // Credit to t3hk0d3 in ChatManager(With slight edits)
		String fixedString = chatColorPattern.matcher(toFix).replaceAll("\u00A7$1"); // And here too
		return fixedString;
	}
	
	private String translatePlayer(String toFix, Player p)
	{
		BSPlayer bsp = PlayerManager.getPlayer(p);
		Pattern namePattern = Pattern.compile("[%]");
		String playerName = p.getName();
		if(bsp.hasNickname())
		{
			playerName = bsp.getNickname();
		}
		String fixedString = namePattern.matcher(toFix).replaceAll(playerName);
		
		return fixedString;
	}
}
