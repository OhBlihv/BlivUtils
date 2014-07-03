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
                    sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("Choose from the following ranks:\n").append(ChatColor.WHITE).append("Free: ").append(ChatColor.GREEN).append("Squid, Chicken, Sheep, Pig\n").append(ChatColor.WHITE).append("Free: ").append(ChatColor.DARK_AQUA).append("Cow, Mooshroom, Slime,Ocelot\n").append(ChatColor.WHITE).append("Nether: ").append(ChatColor.RED).append("MagmaSlime, Blaze, PigZombie, Ghast\n").append(ChatColor.WHITE).append("Ender: ").append(ChatColor.DARK_PURPLE).append("Enderman,").append(ChatColor.DARK_RED).append(" EnderDragon\n").append(ChatColor.GOLD).append("Usage: /rank <rank>").toString());
                    return true;
                }
                if(args.length > 0)
                {
                    if(args[0].equalsIgnoreCase("Squid"))
                    {
                        sender.sendMessage((new StringBuilder()).append(ChatColor.YELLOW).append("----- ").append(ChatColor.GOLD).append("AusCraft Rank Help").append(ChatColor.YELLOW).append(" -----\n").append(ChatColor.DARK_GREEN).append("Squid: ").append(ChatColor.GRAY).append("(Starting Rank").append(ChatColor.GRAY).append(")\n").append(ChatColor.GREEN).append("Very Basic Commands for movement\nNo access to Teleportation\n").append(ChatColor.DARK_GREEN).append("More Info:").append(ChatColor.WHITE).append(" http://wiki.aus-craft.net/ranks#Squid < Click Me!").toString());
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("Chicken"))
                    {
                        sender.sendMessage((new StringBuilder()).append(ChatColor.YELLOW).append("----- ").append(ChatColor.GOLD).append("AusCraft Rank Help").append(ChatColor.YELLOW).append(" -----\n").append(ChatColor.DARK_GREEN).append("Chicken: ").append(ChatColor.GRAY).append("(2 Minutes Playtime)\n").append(ChatColor.GREEN).append("Generic Defaults\nmcMMO Access\nTeleportation and Warping Access\nPreciousStones (Area Protection)\nAccess to basic kits: /kit\n").append(ChatColor.DARK_GREEN).append("More Info:").append(ChatColor.WHITE).append(" http://wiki.aus-craft.net/ranks#Chicken < Click Me!").toString());
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("Sheep"))
                    {
                        sender.sendMessage((new StringBuilder()).append(ChatColor.YELLOW).append("----- ").append(ChatColor.GOLD).append("AusCraft Rank Help").append(ChatColor.YELLOW).append(" -----\n").append(ChatColor.DARK_GREEN).append("Sheep: ").append(ChatColor.GRAY).append("(12 Hours Playtime)\n").append(ChatColor.GREEN).append("Access to CraftBook\nAccess to Tree Lopping\nBuild Access in the Creative Server\n").append(ChatColor.DARK_GREEN).append("More Info:").append(ChatColor.WHITE).append(" http://wiki.aus-craft.net/ranks#Sheep < Click Me!").toString());
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("Pig"))
                    {
                        sender.sendMessage((new StringBuilder()).append(ChatColor.YELLOW).append("----- ").append(ChatColor.GOLD).append("AusCraft Rank Help").append(ChatColor.YELLOW).append(" -----\n").append(ChatColor.DARK_GREEN).append("Pig: ").append(ChatColor.GRAY).append("(1 Day Playtime)\n").append(ChatColor.GREEN).append("Access to Colours Codes in Chat\nAble to use Colour on Signs\nAble to make Containers in Creative\n").append(ChatColor.DARK_GREEN).append("More Info:").append(ChatColor.WHITE).append(" http://wiki.aus-craft.net/ranks#Pig < Click Me!").toString());
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("Cow"))
                    {
                        sender.sendMessage((new StringBuilder()).append(ChatColor.YELLOW).append("----- ").append(ChatColor.GOLD).append("AusCraft Rank Help").append(ChatColor.YELLOW).append(" -----\n").append(ChatColor.DARK_GREEN).append("Cow: ").append(ChatColor.GRAY).append("(2 Days Playtime)\n").append(ChatColor.GREEN).append("Access to Disguises\nType '/d' for help\nAll future ranks get their rank as a disguise.\n").append(ChatColor.DARK_GREEN).append("More Info:").append(ChatColor.WHITE).append(" http://wiki.aus-craft.net/ranks#Cow < Click Me!").toString());
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("Mooshroom"))
                    {
                        sender.sendMessage((new StringBuilder()).append(ChatColor.YELLOW).append("----- ").append(ChatColor.GOLD).append("AusCraft Rank Help").append(ChatColor.YELLOW).append(" -----\n").append(ChatColor.DARK_GREEN).append("Mooshroom: ").append(ChatColor.GRAY).append("(3 Days Playtime)\n").append(ChatColor.GREEN).append("Able to place liquids in Creative\nAble to make Warps!\n(1 Public and 2 Private)\n").append(ChatColor.DARK_GREEN).append("More Info:").append(ChatColor.WHITE).append(" http://wiki.aus-craft.net/ranks#Mooshroom < Click Me!").toString());
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("Slime"))
                    {
                        sender.sendMessage((new StringBuilder()).append(ChatColor.YELLOW).append("----- ").append(ChatColor.GOLD).append("AusCraft Rank Help").append(ChatColor.YELLOW).append(" -----\n").append(ChatColor.DARK_GREEN).append("Slime: ").append(ChatColor.GRAY).append("(4 Days Playtime)\n").append(ChatColor.GREEN).append("Access to wool kits - /kit\nAdditional Warps (2 Public and 3 Private)\n").append(ChatColor.DARK_GREEN).append("More Info:").append(ChatColor.WHITE).append(" http://wiki.aus-craft.net/ranks#Slime < Click Me!").toString());
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("Ocelot"))
                    {
                        sender.sendMessage((new StringBuilder()).append(ChatColor.YELLOW).append("----- ").append(ChatColor.GOLD).append("AusCraft Rank Help").append(ChatColor.YELLOW).append(" -----\n").append(ChatColor.DARK_GREEN).append("Ocelot: ").append(ChatColor.GRAY).append("(5 Days Playtime)\n").append(ChatColor.GREEN).append("Able to return to a death point using /back\nAdditional Warps (2 Public and 4 Private)\nCan purchase MagmaSlime Rank\n").append(ChatColor.DARK_GREEN).append("More Info:").append(ChatColor.WHITE).append(" http://wiki.aus-craft.net/ranks#Ocelot < Click Me!").toString());
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("MagmaSlime"))
                    {
                        sender.sendMessage((new StringBuilder()).append(ChatColor.YELLOW).append("----- ").append(ChatColor.GOLD).append("AusCraft Rank Help").append(ChatColor.YELLOW).append(" -----\n").append(ChatColor.DARK_GREEN).append("MagmaSlime: ").append(ChatColor.GRAY).append("$25,000 ingame (/buyrank MagmaSlime)\n").append(ChatColor.GREEN).append("Able to change nickname (including colours!) - /nick\nAble to purchase Blaze\nAdditional Warps (4 Public and 4 Private)\n").append(ChatColor.DARK_GREEN).append("More Info:").append(ChatColor.WHITE).append(" http://wiki.aus-craft.net/ranks#MagmaSlime < Click Me!").toString());
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("Blaze"))
                    {
                        sender.sendMessage((new StringBuilder()).append(ChatColor.YELLOW).append("----- ").append(ChatColor.GOLD).append("AusCraft Rank Help").append(ChatColor.YELLOW).append(" -----\n").append(ChatColor.DARK_GREEN).append("Blaze: ").append(ChatColor.GRAY).append("$50,000 ingame (/buyrank Blaze)\n").append(ChatColor.GREEN).append("Access to /feed\nAbility to add a /hat to your player\nAble to change the colour of your chat:\n/warp colourshop\n").append(ChatColor.DARK_GREEN).append("More Info:").append(ChatColor.WHITE).append(" http://wiki.aus-craft.net/ranks#Blaze < Click Me!").toString());
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("PigZombie"))
                    {
                        sender.sendMessage((new StringBuilder()).append(ChatColor.YELLOW).append("----- ").append(ChatColor.GOLD).append("AusCraft Rank Help").append(ChatColor.YELLOW).append(" -----\n").append(ChatColor.DARK_GREEN).append("PigZombie: ").append(ChatColor.GRAY).append("$75,000 ingame (/buyrank PigZombie)\n").append(ChatColor.GREEN).append("Access to:\nA portable Crafting Table - /craft\nA portable Ender Chest - /enderchest\nChanging your personal time - /ptime\n").append(ChatColor.DARK_GREEN).append("More Info:").append(ChatColor.WHITE).append(" http://wiki.aus-craft.net/ranks#PigZombie < Click Me!").toString());
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("PigZombie"))
                    {
                        sender.sendMessage((new StringBuilder()).append(ChatColor.YELLOW).append("----- ").append(ChatColor.GOLD).append("AusCraft Rank Help").append(ChatColor.YELLOW).append(" -----\n").append(ChatColor.DARK_GREEN).append("Ghast: ").append(ChatColor.GRAY).append("$100,000 ingame (/buyrank Ghast)\n").append(ChatColor.GREEN).append("Access to instant teleportation - /tp\nReduced Cooldowns/Warmups on spawn teleportation and warping!\n").append(ChatColor.DARK_GREEN).append("More Info:").append(ChatColor.WHITE).append(" http://wiki.aus-craft.net/ranks#PigZombie < Click Me!").toString());
                        return true;
                    }
                }
                return true;
            } else
            {
                sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("M8, u rnt a playr. Git owt.").toString());
                return false;
            }
        } else
        {
            return false;
        }
    }
}
