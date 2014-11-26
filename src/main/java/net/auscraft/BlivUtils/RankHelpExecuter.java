package net.auscraft.BlivUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class RankHelpExecuter implements CommandExecutor
{

    public RankHelpExecuter()
    {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
    {
        if(cmd.getName().equalsIgnoreCase("rank"))
        {
            if(sender instanceof Player)
            {
                if(args.length == 0)
                {
                    sender.sendMessage(ChatColor.GOLD + "Choose from the following ranks:\n"
                    				+ ChatColor.WHITE + "Free: " + ChatColor.GREEN + "Squid, Chicken, Sheep, Pig\n"
                    				+ ChatColor.WHITE + "Free: " + ChatColor.DARK_AQUA + "Cow, Mooshroom, Slime,Ocelot\n"
                    				+ ChatColor.WHITE + "Nether: " + ChatColor.RED + "MagmaSlime, Blaze, PigZombie, Ghast\n"
                    				+ ChatColor.WHITE + "Ender: " + ChatColor.DARK_PURPLE + "Enderman," + ChatColor.DARK_RED + " EnderDragon\n"
                    				+ ChatColor.GOLD + "Usage: /rank <rank>");
                    return true;
                }
                if(args.length > 0)
                {
                    if(args[0].equalsIgnoreCase("Squid"))
                    {
                        sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.GOLD + "AusCraft Rank Help" + ChatColor.YELLOW + " -----\n"
                        				+ ChatColor.DARK_GREEN + "Squid: " + ChatColor.GRAY + "(Starting Rank" + ChatColor.GRAY + ")\n"
                        				+ ChatColor.GREEN + "Very Basic Commands for movement\nNo access to Teleportation\n"
                        				+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " http://wiki.aus-craft.net/ranks#Squid < Click Me!");
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("Chicken"))
                    {
                        sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.GOLD + "AusCraft Rank Help" + ChatColor.YELLOW + " -----\n"
                        				+ ChatColor.DARK_GREEN + "Chicken: " + ChatColor.GRAY + "(2 Minutes Playtime)\n"
                        				+ ChatColor.GREEN + "Generic Defaults\nmcMMO Access\nTeleportation and Warping Access\nPreciousStones (Area Protection)\nAccess to basic kits: /kit\n"
                        				+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " http://wiki.aus-craft.net/ranks#Chicken < Click Me!");
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("Sheep"))
                    {
                        sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.GOLD + "AusCraft Rank Help" + ChatColor.YELLOW + " -----\n"
                        				+ ChatColor.DARK_GREEN + "Sheep: " + ChatColor.GRAY + "(12 Hours Playtime)\n"
                        				+ ChatColor.GREEN + "Access to CraftBook\nAccess to Tree Lopping\nBuild Access in the Creative Server\n"
                        				+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " http://wiki.aus-craft.net/ranks#Sheep < Click Me!");
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("Pig"))
                    {
                        sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.GOLD + "AusCraft Rank Help" + ChatColor.YELLOW + " -----\n"
                        				+ ChatColor.DARK_GREEN + "Pig: " + ChatColor.GRAY + "(1 Day Playtime)\n"
                        				+ ChatColor.GREEN + "Access to Colours Codes in Chat\nAble to use Colour on Signs\nAble to make Containers in Creative\n"
                        				+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " http://wiki.aus-craft.net/ranks#Pig < Click Me!");
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("Cow"))
                    {
                        sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.GOLD + "AusCraft Rank Help" + ChatColor.YELLOW + " -----\n"
                        				+ ChatColor.DARK_GREEN + "Cow: " + ChatColor.GRAY + "(2 Days Playtime)\n"
                        				+ ChatColor.GREEN + "Access to Disguises\nType '/d' for help\nAll future ranks get their rank as a disguise.\n"
                        				+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " http://wiki.aus-craft.net/ranks#Cow < Click Me!");
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("Mooshroom"))
                    {
                        sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.GOLD + "AusCraft Rank Help" + ChatColor.YELLOW + " -----\n"
                        				+ ChatColor.DARK_GREEN + "Mooshroom: " + ChatColor.GRAY + "(3 Days Playtime)\n"
                        				+ ChatColor.GREEN + "Able to place liquids in Creative\nAble to make Warps!\n(1 Public and 2 Private)\n"
                        				+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " http://wiki.aus-craft.net/ranks#Mooshroom < Click Me!");
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("Slime"))
                    {
                        sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.GOLD + "AusCraft Rank Help" + ChatColor.YELLOW + " -----\n"
                        				+ ChatColor.DARK_GREEN + "Slime: " + ChatColor.GRAY + "(4 Days Playtime)\n"
                        				+ ChatColor.GREEN + "Access to wool kits - /kit\nAdditional Warps (2 Public and 3 Private)\n"
                        				+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " http://wiki.aus-craft.net/ranks#Slime < Click Me!");
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("Ocelot"))
                    {
                        sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.GOLD + "AusCraft Rank Help" + ChatColor.YELLOW + " -----\n"
                        				+ ChatColor.DARK_GREEN + "Ocelot: " + ChatColor.GRAY + "(5 Days Playtime)\n"
                        				+ ChatColor.GREEN + "Able to return to a death point using /back\nAdditional Warps (2 Public and 4 Private)\nCan purchase MagmaSlime Rank\n"
                        				+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " http://wiki.aus-craft.net/ranks#Ocelot < Click Me!");
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("MagmaSlime"))
                    {
                        sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.GOLD + "AusCraft Rank Help" + ChatColor.YELLOW + " -----\n"
                        				+ ChatColor.DARK_GREEN + "MagmaSlime: " + ChatColor.GRAY + "$25,000 ingame (/buyrank MagmaSlime)\n"
                        				+ ChatColor.GREEN + "Able to change nickname (including colours!) - /nick\nAble to purchase Blaze\nAdditional Warps (4 Public and 4 Private)\n"
                        				+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " http://wiki.aus-craft.net/ranks#MagmaSlime < Click Me!");
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("Blaze"))
                    {
                        sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.GOLD + "AusCraft Rank Help" + ChatColor.YELLOW + " -----\n"
                        				+ ChatColor.DARK_GREEN + "Blaze: " + ChatColor.GRAY + "$50,000 ingame (/buyrank Blaze)\n"
                        				+ ChatColor.GREEN + "Access to /feed\nAbility to add a /hat to your player\nAble to change the colour of your chat:\n/warp colourshop\n"
                        				+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " http://wiki.aus-craft.net/ranks#Blaze < Click Me!");
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("PigZombie"))
                    {
                        sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.GOLD + "AusCraft Rank Help" + ChatColor.YELLOW + " -----\n"
                        				+ ChatColor.DARK_GREEN + "PigZombie: " + ChatColor.GRAY + "$75,000 ingame (/buyrank PigZombie)\n"
                        				+ ChatColor.GREEN + "Access to:\nA portable Crafting Table - /craft\nA portable Ender Chest - /enderchest\nChanging your personal time - /ptime\n"
                        				+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " http://wiki.aus-craft.net/ranks#PigZombie < Click Me!");
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("Ghast"))
                    {
                        sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.GOLD + "AusCraft Rank Help" + ChatColor.YELLOW + " -----\n"
                        				+ ChatColor.DARK_GREEN + "Ghast: " + ChatColor.GRAY + "$100,000 ingame (/buyrank Ghast)\n"
                        				+ ChatColor.GREEN + "Access to instant teleportation - /tp\nReduced Cooldowns/Warmups on spawn teleportation and warping!\n"
                        				+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " http://wiki.aus-craft.net/ranks#Ghast < Click Me!");
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("Endermite"))
                    {
                        sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.GOLD + "AusCraft Rank Help" + ChatColor.YELLOW + " -----\n"
                        				+ ChatColor.DARK_GREEN + "Endermite: " + ChatColor.GRAY + "$100,000 ingame every 15 days (/buyrank Endermite)\n"
                        				+ ChatColor.GREEN + "Access to most Ender rank features,\nwhich the whole list of can be found below.\n"
                        				+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " http://wiki.aus-craft.net/ranks#Endermite < Click Me!");
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("Enderman"))
                    {
                        sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.GOLD + "AusCraft Rank Help" + ChatColor.YELLOW + " -----\n"
                        				+ ChatColor.DARK_GREEN + "Enderman: " + ChatColor.GRAY + "$5 AUD per Month\n"
                        				+ ChatColor.GREEN + "Inherits all Free rank perks,\nAlso including fly, mobdisguise, trails and pets!\n"
                        				+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " http://wiki.aus-craft.net/ranks#Enderman < Click Me!");
                        return true;
                    }
                    else if(args[0].equalsIgnoreCase("EnderDragon"))
                    {
                        sender.sendMessage(ChatColor.YELLOW + "----- " + ChatColor.GOLD + "AusCraft Rank Help" + ChatColor.YELLOW + " -----\n"
                        				+ ChatColor.DARK_GREEN + "EnderDragon: " + ChatColor.GRAY + "$10 AUD per Month\n"
                        				+ ChatColor.GREEN + "Inherits all Endermite perks,\nAlso including all remaining Pets and all remaining Trails\n"
                        				+ ChatColor.DARK_GREEN + "More Info:" + ChatColor.WHITE + " http://wiki.aus-craft.net/ranks#EnderDragon < Click Me!");
                        return true;
                    }
                    else {
                    	sender.sendMessage(ChatColor.GOLD + "That rank doesnt exist, or does not currently have documentation for it.");
                    	return true;
                    }
                }
                return true;
            } else
            {
                sender.sendMessage(ChatColor.GOLD + "M8, u rnt a playr. Git owt.");
                return false;
            }
        } else
        {
            return false;
        }
    }
}
