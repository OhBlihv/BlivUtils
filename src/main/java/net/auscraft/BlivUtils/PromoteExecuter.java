package net.auscraft.BlivUtils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class PromoteExecuter implements CommandExecutor {
	
    private static BlivUtils bplugin;
    private Permission perms;
    private Economy econ;
    private HashMap<String, Integer> promoteCount;
    private static final Logger log = Logger.getLogger("Minecraft");

    public PromoteExecuter(BlivUtils instance)
    {
        promoteCount = BlivUtils.getPromote();
        bplugin = instance;
        perms = bplugin.setupPermissions();
        econ = bplugin.setupEconomy();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
    {
        if(cmd.getName().equalsIgnoreCase("buyrank") && (sender instanceof Player))
        {
            if(args.length == 0)
            {
                ChatColor a = ChatColor.RED;
                ChatColor b = a;
                ChatColor c = a;
                ChatColor d = a;
                ChatColor aa = ChatColor.GREEN;
                ChatColor bb = aa;
                ChatColor cc = aa;
                ChatColor dd = aa;
                // Endermite perms, unlocks at Ghast
                ChatColor e = ChatColor.STRIKETHROUGH;
                ChatColor ee = ChatColor.STRIKETHROUGH;
                if(!sender.hasPermission("blivutils.promote.magmaslime"))
                {
                    a = ChatColor.STRIKETHROUGH;
                    aa = a;
                }
                if(!sender.hasPermission("blivutils.promote.blaze"))
                {
                    b = ChatColor.STRIKETHROUGH;
                    bb = a;
                }
                if(!sender.hasPermission("blivutils.promote.pigzombie"))
                {
                    c = ChatColor.STRIKETHROUGH;
                    cc = c;
                }
                if(!sender.hasPermission("blivutils.promote.ghast"))
                {
                    d = ChatColor.STRIKETHROUGH;
                    dd = d;
                }
                if(sender.hasPermission("blivutils.promote.blaze"))
                {
                    a = ChatColor.STRIKETHROUGH;
                    aa = a;
                }
                if(sender.hasPermission("blivutils.promote.pigzombie"))
                {
                    a = ChatColor.STRIKETHROUGH;
                    b = a;
                    aa = a;
                    bb = a;
                }
                if(sender.hasPermission("blivutils.promote.ghast"))
                {
                    a = ChatColor.STRIKETHROUGH;
                    b = a;
                    c = a;
                    aa = a;
                    bb = a;
                    cc = a;
                }
                if(sender.hasPermission("blivutils.promote.done"))
                {
                    a = ChatColor.STRIKETHROUGH;
                    b = a;
                    c = a;
                    d = a;
                    aa = a;
                    bb = a;
                    cc = a;
                    dd = a;
                    //Endermite unlocking!
                    e = ChatColor.DARK_PURPLE;
                    ee = ChatColor.GREEN;
                    
                }
                sender.sendMessage(ChatColor.GOLD + "Choose from the following ranks:\n"
                				+ ChatColor.WHITE + " - " + ChatColor.GREEN + aa + "$" + "25,000 " + ChatColor.RED + a + "MagmaSlime\n"
                				+ ChatColor.WHITE + " - " + ChatColor.GREEN + bb + "$" + "50,000 " + ChatColor.RED + b + "Blaze\n"
                				+ ChatColor.WHITE + " - " + ChatColor.GREEN + cc + "$" + "75,000 " + ChatColor.RED + c + "PigZombie\n"
                				+ ChatColor.WHITE + " - " + ChatColor.GREEN + dd + "$" + "100,000 " + ChatColor.RED + d + "Ghast\n"
                				+ ChatColor.WHITE + " - " + ChatColor.GREEN + ee + "$" + "100,000 " + ChatColor.DARK_PURPLE + e + "Endermite (15 Days)\n"
                				+ ChatColor.GOLD + "/buyrank <rank>");
                return true;
            }
            if(args.length > 0)
            {
                if(args[0].equalsIgnoreCase("MagmaSlime"))
                    if(sender.hasPermission("blivutils.promote.magmaslime") && !sender.hasPermission("blivutils.promote.blaze") && !sender.hasPermission("blivutils.promote.pigzombie") && !sender.hasPermission("blivutils.promote.ghast") || !(sender instanceof Player))
                    {
                        Player player = (Player)sender;
                        rankQuery(player, 1);
                    } else
                    {
                        sender.sendMessage(ChatColor.GREEN + "You can't buy this at your rank! You can buy it at " + ChatColor.BLUE + "Ocelot");
                        return false;
                    }
                else if(args[0].equalsIgnoreCase("Blaze"))
                    if(sender.hasPermission("blivutils.promote.blaze") && !sender.hasPermission("blivutils.promote.pigzombie") && !sender.hasPermission("blivutils.promote.ghast") || !(sender instanceof Player))
                    {
                        Player player = (Player)sender;
                        rankQuery(player, 2);
                    } else
                    {
                        sender.sendMessage(ChatColor.GREEN + "You can't buy this at your rank! You can buy it at " + ChatColor.RED + "MagmaSlime only!");
                        return false;
                    }
                else if(args[0].equalsIgnoreCase("PigZombie"))
                    if(sender.hasPermission("blivutils.promote.pigzombie") && !sender.hasPermission("blivutils.promote.ghast") || !(sender instanceof Player))
                    {
                        Player player = (Player)sender;
                        rankQuery(player, 3);
                    } else
                    {
                        sender.sendMessage(ChatColor.GREEN + "You can't buy this at your rank! You can buy it at " + ChatColor.RED + "Blaze");
                        return false;
                    }
                else if(args[0].equalsIgnoreCase("Ghast"))
                {
                    if(sender.hasPermission("blivutils.promote.ghast") && !sender.hasPermission("blivutils.promote.done") || !(sender instanceof Player))
                    {
                        Player player = (Player)sender;
                        rankQuery(player, 4);
                    } else
                    {
                        sender.sendMessage(ChatColor.GREEN + "You can't buy this at your rank! You can buy it at " + ChatColor.RED + "PigZombie");
                        return false;
                    }
                }
                else if(args[0].equalsIgnoreCase("Endermite"))
                {
                    if((sender.hasPermission("blivutils.promote.done") || !(sender instanceof Player)))
                    {
                        Player player = (Player)sender;
                        rankQuery(player, 5);
                    } else
                    {
                        sender.sendMessage(ChatColor.GREEN + "You can't rent this at your rank! You can rent it at " + ChatColor.RED + "Ghast");
                        return false;
                    }
                } else
                {
                    sender.sendMessage(ChatColor.GREEN + "You didnt specify a valid rank!");
                    return false;
                }
            }
        }
        if(cmd.getName().equalsIgnoreCase("promoteme") && (sender instanceof Player))
        {
            String playerName = sender.getName();
            if(promoteCount.containsKey(playerName) && (promoteCount.get(playerName) != 0) && ((promoteCount.get(playerName) == 1) || (promoteCount.get(playerName) == 2) ||
            		(promoteCount.get(playerName) == 3) || (promoteCount.get(playerName) == 4) || (promoteCount.get(playerName) == 5)))
            {
                int rank = promoteCount.get(playerName);
                double price = 0.0;
                price = getRankPrice(rank);
                Player player = (Player)sender;
                String rankString = getRankName(rank);
                if(econ.has(player, price))
                {
                    econ.withdrawPlayer(player, price);
                    String[] groups = perms.getPlayerGroups(null, player);
                    // Don't Demote the Endermite Renters!
                    if(rank != 5) {
                    	for(int i = 0; i < groups.length; i++) {
                    		perms.playerRemoveGroup(null, player, groups[i]);
                    	}
                    	perms.playerAddGroup(null, player, rankString);
                    }
                    else if(rank == 5){
                    	Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + player.getName() + " group add Endermite \"\" 1296000");
                    }
                    log.info("Player " + sender.getName() + " has been promoted to " + rankString);
                    promoteCount.put(playerName, 0);
                    if((rank != 5)) {
                    	sender.sendMessage("You have been promoted to " + ChatColor.RED + rankString);
                    }
                    else if(rank == 5) {
                    	sender.sendMessage("You have been promoted to " + ChatColor.RED + rankString + ChatColor.WHITE + " for " + ChatColor.GREEN + "15 days");
                    	
                    }
                    return true;
                } else
                {
                    sender.sendMessage("You dont have sufficient funds to be promoted!");
                    return false;
                }
            } else
            {
                sender.sendMessage("You havent specified a rank to purchase!");
                return false;
            }
        }
        if(cmd.getName().equalsIgnoreCase("timeleft") && (sender instanceof Player))
        {
        	Player p = (Player) sender;
        	String rank = BlivUtils.getActiveRank(p);
        	log.info("Rank = " + rank);
        	if(rank != "") {
        		int allseconds = BlivUtils.getTimeLeft(p, rank);
            	//Just shamelessly ripped this code, I don't even care: http://stackoverflow.com/questions/11357945/java-convert-seconds-into-day-hour-minute-and-seconds-using-timeunit
            	//Source: First Comment. (And I also edited it a bit, so its not just blatantly ripped from StackOverflow...)
            	String days = "" ,hours = "" ,minutes = "" ,seconds = "";
            	if (allseconds >= 0) {
            		int day = (int)TimeUnit.SECONDS.toDays(allseconds);        
                	long hour = TimeUnit.SECONDS.toHours(allseconds) - (day *24);
                	long minute = TimeUnit.SECONDS.toMinutes(allseconds) - (TimeUnit.SECONDS.toHours(allseconds)* 60);
                	long second = TimeUnit.SECONDS.toSeconds(allseconds) - (TimeUnit.SECONDS.toMinutes(allseconds) *60);
                	//String them together, then cut them down if they're 0
                	if(day != 0) {
                		days = day + " Day(s) ";
                	}
                	if(hour != 0) {
                		hours = hour + " Hour(s) ";
                	}
                	if(minute != 0) {
                		minutes = minute + " Minutes(s) ";
                	}
                	if(second != 0) {
                		seconds = second + " Second(s) ";
                	}
                	String print = ChatColor.GOLD + "" + days + hours + minutes + seconds + "Remaining of " + rank;
                	sender.sendMessage(print);
                	return true;
            	}
            	else {
            		sender.sendMessage(ChatColor.GOLD + "You dont have an active rank!");
            		return true;
            	}
        	}
        	else {
        		sender.sendMessage(ChatColor.GOLD + "You dont have an active rank!");
        		return true;
        	}
        }
        else
        {
            return false;
        }
    }
    //Rank Values
    //1 = MagmaSlime
    //2 = Blaze
    //3 = PigZombie
    //4 = Ghast
    //5 = Endermite
    private void changePlayerState(Player player, Integer rank)
    {
        String playerName = player.getName();
        promoteCount.put(playerName, rank);
    }

    private String getRankName(int rank)
    {
        String name;
        if(rank == 1) {
            name = "MagmaSlime";
        }
        else if(rank == 2) {
            name = "Blaze";
        }
        else if(rank == 3) {
            name = "PigZombie";
        }
        else if(rank == 4) {
            name = "Ghast";
        }
        else if(rank == 5) {
            name = "Endermite";
        }
        else {
            name = "null";
            log.severe("Rank entered doesnt match a rank listed!");
        }
        return name;
    }

    private double getRankPrice(int rank)
    {
        int price = 0;
        if(rank == 1) {
            price = 25000;
        }
        else if(rank == 2) {
            price = 50000;
        }
        else if(rank == 3) {
            price = 75000;
        }
        else if(rank == 4) {
            price = 100000;
        }
        else if(rank == 5) {
            price = 100000;
        }
        else{
        	price = -1;
        }
        return price;
    }

    private String getRankPriceRead(int rank, boolean strike)
    {
        ChatColor a = ChatColor.STRIKETHROUGH;
        ChatColor b = a;
        if(strike = true)
        {
            a = ChatColor.GREEN;
            b = ChatColor.WHITE;
        }
        String price;
        if(rank == 1) {
            price =  b + "$" + ChatColor.GREEN + a + "25,000";
        }
        else if(rank == 2) {
            price =  b + "$" + ChatColor.GREEN + a + "50,000";
        }
        else if(rank == 3) {
            price =  b + "$" + ChatColor.GREEN + a + "75,000";
        }
        else if(rank == 4) {
            price =  b + "$" + ChatColor.GREEN + a + "100,000";
        } 
        else if(rank == 5) {
            price =  b + "$" + ChatColor.GREEN + a + "100,000";
        }
        else {
            price = "null";
            log.severe("Rank entered doesnt match a rank listed!");
        }
        return price;
    }

    private boolean checkifNull(Player player)
    {
        boolean a = true;
        if(perms == null || econ == null)
        {
            player.sendMessage("Whoops! Report this to the Musketeers.");
            log.severe("Permissions or Economy Null -- Restart Vault/BlivUtils.");
            a = false;
        }
        return a;
    }

    private boolean rankQuery(Player player, int rank)
    {
        boolean a = checkifNull(player);
        log.info("Rank = " + rank);
        if(a = true)
        {
            String rankName = getRankName(rank);
            String price = getRankPriceRead(rank, false);
            if((rankName == "null") || (price == "-1")) {
            	player.sendMessage("Could not purchase rank");
            	log.severe("Player " + player.getName() + " tried to purchase rank " +  rankName + " but failed. (Due to rankname/price being null)");
            }
            else if((rank == 1) || (rank == 2) || (rank == 3) || (rank == 4)) {
            	player.sendMessage("Sure you want to spend " + price + ChatColor.RESET + " on " + ChatColor.RED + rankName + "?");
            	player.sendMessage("Type " + ChatColor.GREEN + "/promoteme" + ChatColor.RESET + " to Accept");
            	changePlayerState(player, rank);
            	return true;
            }
            else if(rank == 5) {
            	player.sendMessage("Sure you want to spend " + price + ChatColor.RESET + " on " + ChatColor.DARK_PURPLE + rankName + " (15 Days)?");
                player.sendMessage("Type " + ChatColor.GREEN + "/promoteme" + ChatColor.RESET + " to Accept");
                changePlayerState(player, rank);
                return true;
            }
            else{
            	player.sendMessage("Could not purchase rank");
            	log.severe("Player " + player.getName() + " tried to purchase rank " +  rankName + " but failed.");
            	return false;
            }
        }
        if(a = false)
        {
            player.sendMessage("Could not purchase rank.");
            log.severe("Player " + player.getName() + " tried to purchase rank, but permissions/economy was not active.");
            return false;
        } else
        {
            return false;
        }
    }

}
