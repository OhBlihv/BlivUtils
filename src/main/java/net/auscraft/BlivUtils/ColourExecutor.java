package net.auscraft.BlivUtils;

import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import net.milkbowl.vault.economy.Economy;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ColourExecutor implements CommandExecutor {

	private BlivUtils bplugin;
	private HashMap<String, String> colourSave;
    private Economy econ;
    private static final Logger log = Logger.getLogger("Minecraft");
	
	public ColourExecutor(BlivUtils instance)
    {
        bplugin = instance;
        econ = bplugin.setupEconomy();
        colourSave = BlivUtils.getSuffixColour();
        
    }
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		if(cmd.getName().equalsIgnoreCase("chat") && (sender instanceof Player))
		{
			// If no args, assume main menu
			if(args.length == 0)
			{
				String hasPermToUse = ""; //Start this off as blank, so it wont show up if the player has permission
				if(!sender.hasPermission("blivutils.chat"))
				{
					hasPermToUse = (ChatColor.RED + "You don't have permission to use this!\n Rank " + ChatColor.RED + "Blaze " + ChatColor.RED + "and above.\n");
				}
				sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Aus" + ChatColor.WHITE + ChatColor.BOLD + "Craft" + ChatColor.YELLOW + " Chat Colour Menu\n"
								+ hasPermToUse	//Blank line here to simulate the other line being here.
								+ ChatColor.GOLD + "Cost: " + ChatColor.WHITE + "$" + ChatColor.DARK_GREEN + "200" + ChatColor.YELLOW + " per colour change\n"
								//List here
								+ ChatColor.GOLD + "| " + ChatColor.DARK_GRAY + " DarkGray" + ChatColor.GOLD + " Gold" + ChatColor.AQUA + " Aqua" + ChatColor.YELLOW + " Yellow\n"
								+ ChatColor.YELLOW + "| " + ChatColor.DARK_GREEN + " DarkGreen" + ChatColor.DARK_PURPLE + " DarkPurple" +  ChatColor.GRAY + " Gray\n"
								+ ChatColor.GOLD + "| " + ChatColor.DARK_AQUA + " DarkAqua" + ChatColor.BLUE + " Blue" + ChatColor.GREEN + " Green" +  ChatColor.RED + " Red\n"
								+ ChatColor.YELLOW + "| " + ChatColor.DARK_RED + " DarkRed" + ChatColor.LIGHT_PURPLE + " LightPurple" +  ChatColor.WHITE + " White\n"
								//No more list
								+ ChatColor.GOLD + "Usage: /chat <colour>");
				return true;
			}
			else
			{
				if(sender.hasPermission("blivutils.chat"))
				{
					String colour = "z"; //White by default, initialize here so nothing explodes.
					ChatColor choice = ChatColor.WHITE;
					//And so begins the giant spam if else train
					//And, I'm doing these in the order they appear in the menu
					if((args[0].equalsIgnoreCase("DarkGray")) || (args[0].equalsIgnoreCase("DarkGrey")))	//Cater for them muricans.
					{
						colour = "8";
						choice = ChatColor.DARK_GRAY;
					}
					else if(args[0].equalsIgnoreCase("Gold"))
					{
						colour = "6";
						choice = ChatColor.GOLD;
					}
					else if(args[0].equalsIgnoreCase("Aqua"))
					{
						colour = "b";
						choice = ChatColor.AQUA;
					}
					else if(args[0].equalsIgnoreCase("Yellow"))
					{
						colour = "e";
						choice = ChatColor.YELLOW;
					}
					//First row done.
					else if(args[0].equalsIgnoreCase("DarkGreen"))
					{
						colour = "2";
						choice = ChatColor.DARK_GREEN;
					}
					else if(args[0].equalsIgnoreCase("DarkPurple"))
					{
						colour = "5";
						choice = ChatColor.DARK_PURPLE;
					}
					else if((args[0].equalsIgnoreCase("Gray")) || (args[0].equalsIgnoreCase("Grey")))
					{
						colour = "7";
						choice = ChatColor.GRAY;
					}
					//Second row done
					else if(args[0].equalsIgnoreCase("DarkAqua"))
					{
						colour = "3";
						choice = ChatColor.DARK_AQUA;
					}
					else if(args[0].equalsIgnoreCase("Blue"))
					{
						colour = "9";
						choice = ChatColor.BLUE;
					}
					else if(args[0].equalsIgnoreCase("Green"))
					{
						colour = "a";
						choice = ChatColor.GREEN;
					}
					else if(args[0].equalsIgnoreCase("Red"))
					{
						colour = "c";
						choice = ChatColor.RED;
					}
					//Third row done
					else if(args[0].equalsIgnoreCase("DarkRed"))
					{
						colour = "2";
						choice = ChatColor.DARK_RED;
					}
					else if(args[0].equalsIgnoreCase("LightPurple"))
					{
						colour = "d";
						choice = ChatColor.LIGHT_PURPLE;
					}
					else if(args[0].equalsIgnoreCase("White"))
					{
						colour = "f";
						choice = ChatColor.WHITE;
					}
					else
					{
						sender.sendMessage(ChatColor.RED + "Thats not a valid colour! (Did you spell it right?)");
					}
					//Check if the colour was modified, and if so, follow through.
					if(colour != "z")	//z isnt a colour, lets use it.
					{
						displayChoice(colour, choice, sender);
					}
					return true;
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "You dont have permission to set a chat colour!\nBlaze rank and above only!");
					return true;
				}
			}
		}
		if(cmd.getName().equalsIgnoreCase("colourme") && (sender instanceof Player))	//Probably change the name of this command later.
		{
			Player p = (Player)sender;
			String playerName = p.getName();
			if(colourSave.containsKey(playerName))
			{
				if(econ.has(p, 200))
				{
					econ.withdrawPlayer(p, 200);
					PermissionUser user = PermissionsEx.getUser(p);
					String suffix = "&" + colourSave.get(p.getName());
					user.setSuffix(suffix, null);
					sender.sendMessage(ChatColor.GOLD + "Successfully set to " + getColour(colourSave.get(p.getName())));
					log.info(ChatColor.GOLD + "Successfully set " + p.getName() + "'s chat colour to " + getColour(colourSave.get(p.getName())));
				}
				else
				{
					sender.sendMessage(ChatColor.RED + "You can't afford this! ($200 required)");
				}
			}
			else
			{
				sender.sendMessage("The name: " + p.getName() + " was not found in the HashMap.");
				sender.sendMessage(ChatColor.RED + "You need to pick a colour first! -- Use " + ChatColor.GREEN + "/chat");
			}
			return true;
		}
		return true;
	} //END onCommand
		
	private void displayChoice(String colour, ChatColor choice, CommandSender sender)
	{
		Player p = (Player)sender;
		String prefix = translateColours(p);
		sender.sendMessage(prefix + p.getDisplayName() + ": " + choice + "Test Message -- #12345\n"
							+ ChatColor.GOLD + "Like it? Type /colourme to confirm.");
		
		colourSave.put(p.getName(), colour);
	}
	
	private String translateColours(Player p)
	{
		PermissionUser user = PermissionsEx.getUser(p);
		String toFix = user.getPrefix(), fixedString;
		Pattern chatColorPattern = Pattern.compile("(?i)&([0-9A-Fa-f])");		//Credit to t3hk0d3 in ChatManager (With slight edits)
		fixedString = chatColorPattern.matcher(toFix).replaceAll("\u00A7$1");	//And here too.
		
		return fixedString;
	}
	
	private String getColour(String colour)
	{
		String printCol;
		switch(colour)
		{
			//case "1": printCol = "Blue"; break; //I'm pretty sure this one isnt included.
			case "2": printCol = "DarkGreen"; break;
			case "3": printCol = "DarkAqua"; break;
			case "4": printCol = "DarkRed"; break;
			case "5": printCol = "DarkPurple"; break;
			case "6": printCol = "Gold"; break;
			case "7": printCol = "Gray"; break;
			case "8": printCol = "DarkGray"; break;
			case "9": printCol = "Blue"; break;
			case "a": printCol = "Green"; break;
			case "b": printCol = "Aqua"; break;
			case "c": printCol = "Red"; break;
			case "d": printCol = "LightPurple"; break;
			case "e": printCol = "Yellow"; break;
			case "f": printCol = "White"; break;
			default: printCol = "Undefined"; break;
		}
		return printCol;
	}

}
