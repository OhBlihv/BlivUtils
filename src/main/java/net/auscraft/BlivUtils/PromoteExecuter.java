package net.auscraft.BlivUtils;

import java.util.HashMap;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
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
        if(cmd.getName().equalsIgnoreCase("buyrank"))
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
                }
                sender.sendMessage(ChatColor.GOLD + "Choose from the following ranks:\n"
                				+ ChatColor.WHITE + " - " + ChatColor.GREEN + aa + "$" + "25,000 " + ChatColor.RED + a + "MagmaSlime\n"
                				+ ChatColor.WHITE + " - " + ChatColor.GREEN + bb + "$" + "50,000 " + ChatColor.RED + b + "Blaze\n"
                				+ ChatColor.WHITE + " - " + ChatColor.GREEN + cc + "$" + "75,000 " + ChatColor.RED + c + "PigZombie\n"
                				+ ChatColor.WHITE + " - " + ChatColor.GREEN + dd + "$" + "100,000 " + ChatColor.RED + d + "Ghast\n"
                				+ ChatColor.GOLD + "/buyrank <rank>");
                return true;
            }
            if(args.length > 0)
            {
                if(args[0].equalsIgnoreCase("MagmaSlime"))
                    if(sender.hasPermission("blivutils.promote.magmaslime") && !sender.hasPermission("blivutils.promote.blaze") && !sender.hasPermission("blivutils.promote.pigzombie") && !sender.hasPermission("blivutils.promote.ghast") || !(sender instanceof Player))
                    {
                        Player player = (Player)sender;
                        return rankQuery(player, 1);
                    } else
                    {
                        sender.sendMessage(ChatColor.GREEN + "You can't buy this at your rank! You can buy it at " + ChatColor.BLUE + "Ocelot");
                        return false;
                    }
                if(args[0].equalsIgnoreCase("Blaze"))
                    if(sender.hasPermission("blivutils.promote.blaze") && !sender.hasPermission("blivutils.promote.pigzombie") && !sender.hasPermission("blivutils.promote.ghast") || !(sender instanceof Player))
                    {
                        Player player = (Player)sender;
                        return rankQuery(player, 2);
                    } else
                    {
                        sender.sendMessage(ChatColor.GREEN + "You can't buy this at your rank! You can buy it at " + ChatColor.RED + "MagmaSlime only!");
                        return false;
                    }
                if(args[0].equalsIgnoreCase("PigZombie"))
                    if(sender.hasPermission("blivutils.promote.pigzombie") && !sender.hasPermission("blivutils.promote.ghast") || !(sender instanceof Player))
                    {
                        Player player = (Player)sender;
                        return rankQuery(player, 3);
                    } else
                    {
                        sender.sendMessage(ChatColor.GREEN + "You can't buy this at your rank! You can buy it at " + ChatColor.RED + "Blaze");
                        return false;
                    }
                if(args[0].equalsIgnoreCase("Ghast"))
                {
                    if(sender.hasPermission("blivutils.promote.ghast") && !sender.hasPermission("blivutils.promote.done") || !(sender instanceof Player))
                    {
                        Player player = (Player)sender;
                        return rankQuery(player, 4);
                    } else
                    {
                        sender.sendMessage(ChatColor.GREEN + "You can't buy this at your rank! You can buy it at " + ChatColor.RED + "PigZombie");
                        return false;
                    }
                } else
                {
                    sender.sendMessage(ChatColor.GREEN + "You didnt specify a valid rank!");
                    return false;
                }
            }
        }
        if(cmd.getName().equalsIgnoreCase("promoteme"))
        {
            String playerName = sender.getName();
            if(promoteCount.containsKey(playerName) && promoteCount.get(playerName) != null)
            {
                int rank = ((Integer)promoteCount.get(playerName)).intValue();
                double price = 0.0D;
                price = getRankPrice(rank);
                Player player = (Player)sender;
                String rankString = getRankName(rank);
                if(econ.has(player, price))
                {
                    econ.withdrawPlayer(player, price);
                    sender.sendMessage("You have been promoted to " + ChatColor.RED + rankString);
                    String groups[] = perms.getPlayerGroups(null, player);
                    for(int i = 0; i < groups.length; i++)
                        perms.playerRemoveGroup(null, player, groups[i]);

                    perms.playerAddGroup(null, player, rankString);
                    log.info("Player" + sender.getName() + " has been promoted to " + rankString);
                    promoteCount.put(playerName, Integer.valueOf(0));
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
        } else
        {
            return false;
        }
    }

    private void changePlayerState(Player player, Integer rank)
    {
        String playerName = player.getName();
        promoteCount.put(playerName, rank);
    }

    private String getRankName(int rank)
    {
        String name;
        if(rank == 1)
            name = "MagmaSlime";
        else
        if(rank == 2)
            name = "Blaze";
        else
        if(rank == 3)
            name = "PigZombie";
        else
        if(rank == 4)
        {
            name = "Ghast";
        } else
        {
            name = "null";
            log.severe("Rank entered doesnt match a rank listed!");
        }
        return name;
    }

    private double getRankPrice(int rank)
    {
        double price = 0.0D;
        if(rank == 1)
            price = 25000D;
        else
        if(rank == 2)
            price = 50000D;
        else
        if(rank == 3)
            price = 75000D;
        else
        if(rank == 4)
            price = 100000D;
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
        if(rank == 1)
            price =  b + "$" + ChatColor.GREEN + a + "25,000";
        else
        if(rank == 2)
            price =  b + "$" + ChatColor.GREEN + a + "50,000";
        else
        if(rank == 3)
            price =  b + "$" + ChatColor.GREEN + a + "75,000";
        else
        if(rank == 4)
        {
            price =  b + "$" + ChatColor.GREEN + a + "100,000";
        } else
        {
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
        if(a = true)
        {
            String rankName = getRankName(rank);
            String price = getRankPriceRead(rank, false);
            player.sendMessage("Are you sure you want to spend " + price + ChatColor.RESET + " on " + ChatColor.RED + rankName + "?");
            player.sendMessage("Type " + ChatColor.GREEN + "/promoteme" + ChatColor.RESET + " to Accept");
            changePlayerState(player, Integer.valueOf(rank));
            return true;
        }
        if(a = false)
        {
            player.sendMessage("Could not purchase rank.");
            return false;
        } else
        {
            return false;
        }
    }

}
