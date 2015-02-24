package net.auscraft.BlivUtils.executors;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class RankHelpExecutor implements CommandExecutor 
{
	
	private Utilities util;
	private final String banner = ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Aus" + ChatColor.WHITE + "" + ChatColor.BOLD + "Craft Rank Help"
								+ ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.GREEN + " - \n";
	private final String footer = ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.GREEN + "- "
								+ ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.GREEN + "- "
								+ ChatColor.DARK_GREEN + "- "
								+ ChatColor.GREEN + "- ";
			//&a- &2- &a- &2- &a- &9&lAus&f&lCraft Rank Help &2- &a- &2- &a- &2- &a- &2- &a-

	public RankHelpExecutor(BlivUtils instance)
	{
		util = instance.getUtil();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
	{
		if (cmd.getName().equalsIgnoreCase("rank"))
		{
			if (sender instanceof Player)
			{
				if (args.length == 0) 
				{
					sender.sendMessage(banner + ""
							+ ChatColor.DARK_GREEN + "| " + ChatColor.GOLD + "Choose from the following ranks:\n"
							+ ChatColor.GREEN + "| " + ChatColor.WHITE + "Free: " + ChatColor.GREEN + "Squid, Chicken, Sheep, Pig\n"
							+ ChatColor.DARK_GREEN + "| " + ChatColor.WHITE + "Free: " + ChatColor.DARK_AQUA + "Cow, Mooshroom, Slime, Ocelot\n"
							+ ChatColor.GREEN + "| " + ChatColor.WHITE + "Nether: " + ChatColor.RED + "MagmaSlime, Blaze, PigZombie, Ghast\n"
							+ ChatColor.DARK_GREEN + "| " + ChatColor.WHITE + "Ender: " + ChatColor.DARK_PURPLE + "Endermite, Enderman," + ChatColor.DARK_RED + " EnderDragon, "
							+ ChatColor.GRAY + "Wither\n"
							+ ChatColor.GREEN + "| " + ChatColor.GOLD + "Usage: /rank <rank>\n"
							+ footer);
					return true;
				}
				if (args.length > 0)
				{
					if (args[0].equalsIgnoreCase("Squid"))
					{
						sender.sendMessage(banner
								+ ChatColor.DARK_GREEN + "Squid: " + ChatColor.GRAY + "(Starting Rank" + ChatColor.GRAY + ")\n"
								+ ChatColor.AQUA + "Very Basic Commands for movement\nNo access to Teleportation\n"
								+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE	+ " wiki.aus-craft.net/ranks#Squid < Click Me!\n"
								+ footer);
						return true;
					} 
					else if (args[0].equalsIgnoreCase("Chicken"))
					{
						sender.sendMessage(banner
								+ ChatColor.DARK_GREEN + "Chicken: " + ChatColor.GRAY + "(2 Minutes Playtime)\n"
								+ ChatColor.AQUA + "Generic Defaults\nmcMMO Access\nTeleportation and Warping Access\nPreciousStones (Area Protection)\nAccess to basic kits: /kit\n"
								+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " wiki.aus-craft.net/ranks#Chicken < Click Me!\n"
								+ footer);
						return true;
					} 
					else if (args[0].equalsIgnoreCase("Sheep"))
					{
						sender.sendMessage(banner
								+ ChatColor.DARK_GREEN + "Sheep: " + ChatColor.GRAY + "(12 Hours Playtime)\n"	
								+ ChatColor.AQUA + "Access to CraftBook\nAccess to Tree Lopping\nBuild Access in the Creative Server\n"
								+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " wiki.aus-craft.net/ranks#Sheep < Click Me!\n"
								+ footer);
						return true;
					}
					else if (args[0].equalsIgnoreCase("Pig"))
					{
						sender.sendMessage(banner
								+ ChatColor.DARK_GREEN + "Pig: "+  ChatColor.GRAY + "(1 Day Playtime)\n"
								+ ChatColor.AQUA + "Access to Colours Codes in Chat\nAble to use Colour on Signs\nAble to make Containers in Creative\n"
								+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " wiki.aus-craft.net/ranks#Pig < Click Me!\n"
								+ footer);
						return true;
					} 
					else if (args[0].equalsIgnoreCase("Cow")) 
					{
						sender.sendMessage(banner
								+ ChatColor.DARK_GREEN + "Cow: " + ChatColor.GRAY + "(2 Days Playtime)\n"
								+ ChatColor.AQUA + "Access to Disguises\nType '/d' for help\nAll future ranks get their rank as a disguise.\n"
								+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " wiki.aus-craft.net/ranks#Cow < Click Me!\n"
								+ footer);;
						return true;
					}
					else if (args[0].equalsIgnoreCase("Mooshroom")) 
					{
						sender.sendMessage(banner
								+ ChatColor.DARK_GREEN + "Mooshroom: "+  ChatColor.GRAY + "(3 Days Playtime)\n"
								+ ChatColor.AQUA + "Able to place liquids in Creative\nAble to make Warps!\n(1 Public and 2 Private)\n"
								+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " wiki.aus-craft.net/ranks#Mooshroom < Click Me!\n"
								+ footer);
						return true;
					}
					else if (args[0].equalsIgnoreCase("Slime"))
					{
						sender.sendMessage(banner
								+ ChatColor.DARK_GREEN + "Slime: " + ChatColor.GRAY + "(4 Days Playtime)\n"
								+ ChatColor.AQUA+  "Access to wool kits - /kit\nAdditional Warps (2 Public and 3 Private)\n"
								+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " wiki.aus-craft.net/ranks#Slime < Click Me!\n"
								+ footer);
						return true;
					} 
					else if (args[0].equalsIgnoreCase("Ocelot"))
					{
						sender.sendMessage(banner
								+ ChatColor.DARK_GREEN + "Ocelot: " + ChatColor.GRAY + "(5 Days Playtime)\n"
								+ ChatColor.AQUA + "Able to return to a death point using /back\nAdditional Warps (2 Public and 4 Private)\nCan purchase MagmaSlime Rank\n"
								+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " wiki.aus-craft.net/ranks#Ocelot < Click Me!\n"
								+ footer);
						return true;
					} 
					else if (args[0].equalsIgnoreCase("MagmaSlime")) 
					{
						sender.sendMessage(banner
								+ ChatColor.DARK_GREEN + "MagmaSlime: " + ChatColor.GRAY + "$25,000 ingame at " + ChatColor.BLUE + "Ocelot Rank " + ChatColor.GRAY + "(/buyrank MagmaSlime)\n"
								+ ChatColor.AQUA + "Able to change nickname (including colours!) - /nick\nAble to purchase Blaze\nAdditional Warps (4 Public and 4 Private)\n"
								+ ChatColor.DARK_GREEN + "More Info:"
								+ ChatColor.WHITE + " wiki.aus-craft.net/ranks#MagmaSlime < Click Me!\n"
								+ footer);
						return true;
					}
					else if (args[0].equalsIgnoreCase("Blaze"))
					{
						sender.sendMessage(banner
								+ ChatColor.DARK_GREEN + "Blaze: " + ChatColor.GRAY + "$50,000 ingame at " + ChatColor.RED + "MagmaSlime Rank  " + ChatColor.GRAY + "(/buyrank Blaze)\n"
								+ ChatColor.AQUA + "Access to /feed\nAbility to add a /hat to your player\nAble to change the colour of your chat:\n/warp colourshop\n"
								+ ChatColor.DARK_GREEN + "More Info:"
								+ ChatColor.WHITE + " wiki.aus-craft.net/ranks#Blaze < Click Me!\n"
								+ footer);
						return true;
					} 
					else if (args[0].equalsIgnoreCase("PigZombie"))
					{
						sender.sendMessage(banner
								+ ChatColor.DARK_GREEN + "PigZombie: " + ChatColor.GRAY	+ "$75,000 ingame at " + ChatColor.RED + "Blaze Rank  " + ChatColor.GRAY + " (/buyrank PigZombie)\n"
								+ ChatColor.AQUA+  "Access to:\nA portable Crafting Table - /craft\nA portable Ender Chest - /enderchest\nChanging your personal time - /ptime\n"
								+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " wiki.aus-craft.net/ranks#PigZombie < Click Me!\n"
								+ footer);
						return true;
					}
					else if (args[0].equalsIgnoreCase("Ghast")) 
					{
						sender.sendMessage(banner
								+ ChatColor.DARK_GREEN + "Ghast: " + ChatColor.GRAY + "$100,000 ingame at " + ChatColor.RED + "PigZombie Rank  " + ChatColor.GRAY + " (/buyrank Ghast)\n"
								+ ChatColor.AQUA + "Access to instant teleportation - /tp\nReduced Cooldowns/Warmups on spawn teleportation and warping!\n"
								+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " wiki.aus-craft.net/ranks#Ghast < Click Me!\n"
								+ footer);
						return true;
					} 
					else if (args[0].equalsIgnoreCase("Endermite")) 
					{
						sender.sendMessage(banner
								+ ChatColor.DARK_GREEN + "Endermite: " + ChatColor.GRAY + "$100,000 ingame every 15 days at " + ChatColor.RED + "Ghast Rank\n"
								+ ChatColor.GRAY + " (/buyrank Endermite)\n"
								+ ChatColor.AQUA + "Access to most Ender rank features,\nThe list can be found below.\n"
								+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " wiki.aus-craft.net/ranks#Endermite < Click Me!\n"
								+ footer);
						return true;
					}
					else if (args[0].equalsIgnoreCase("Enderman"))
					{
						sender.sendMessage(banner
								+ ChatColor.DARK_GREEN + "Enderman: " + ChatColor.GRAY + "$5 AUD per month\n"
								+ ChatColor.AQUA + "Inherits all Free rank perks, also includes:\n"
								+ ChatColor.AQUA + "/tp, /weather, /jumpto, /fly, /speed\n"
								+ ChatColor.AQUA + "+ 200% Mob Drops + 10% Bonus Voting Reward Chance\n"
								+ ChatColor.AQUA + "+ /kit dtools + /kit darmour | And 3 Free Trails\n"
								+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " wiki.aus-craft.net/ranks#Enderman < Click Me!\n"
								+ footer);
						return true;
					} 
					else if (args[0].equalsIgnoreCase("EnderDragon")) 
					{
						sender.sendMessage(banner
								+ ChatColor.DARK_GREEN + "EnderDragon: " + ChatColor.GRAY + "Enderman + $5AUD of perks per month\n"
								+ ChatColor.AQUA + "Inherits all free and Enderman perks, also includes:\n"
								+ ChatColor.AQUA + "/firework, /fixclaim, /xpclaim\n"
								+ ChatColor.AQUA + "10HP Extra (1 1/2 Bars Total)\n"
								+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " wiki.aus-craft.net/ranks#EnderDragon < Click Me!\n"
								+ footer);
						return true;
					}
					else if (args[0].equalsIgnoreCase("Wither")) 
					{
						sender.sendMessage(banner
								+ ChatColor.DARK_GREEN + "Wither: " + ChatColor.GRAY + "Enderman + $25AUD of perks per Month\n"
								+ ChatColor.AQUA + "Inherits all free and EnderDragon perks, also includes:\n"
								+ ChatColor.AQUA + "/lore (Edit any items name and description)\n"
								+ ChatColor.AQUA + "20HP Extra (2 Bars Total)\n"
								+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " wiki.aus-craft.net/ranks#Wither < Click Me!\n"
								+ footer);
						return true;
					} 
					else 
					{
						util.printInfo(sender, "That rank doesn't, or doesn't yet have documentation.");
						return true;
					}
				}
				return true;
			}
			else
			{
				util.printError(sender, "M8, u rnt a playr. Git owt.");
				return true;
			}
		}
		else 
		{
			return false;
		}
	}
}
