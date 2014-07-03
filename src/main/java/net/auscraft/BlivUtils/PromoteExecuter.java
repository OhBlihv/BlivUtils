package net.auscraft.BlivUtils;

import java.util.HashMap;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class PromoteExecuter implements CommandExecutor {

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
                sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("Choose from the following ranks:\n").append(ChatColor.WHITE).append(" - ").append(ChatColor.GREEN).append(aa).append("$").append("25,000 ").append(ChatColor.RED).append(a).append("MagmaSlime\n").append(ChatColor.WHITE).append(" - ").append(ChatColor.GREEN).append(bb).append("$").append("50,000 ").append(ChatColor.RED).append(b).append("Blaze\n").append(ChatColor.WHITE).append(" - ").append(ChatColor.GREEN).append(cc).append("$").append("75,000 ").append(ChatColor.RED).append(c).append("PigZombie\n").append(ChatColor.WHITE).append(" - ").append(ChatColor.GREEN).append(dd).append("$").append("100,000 ").append(ChatColor.RED).append(d).append("Ghast\n").append(ChatColor.GOLD).append("/buyrank <rank>").toString());
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
                        sender.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("You can't buy this at your rank! You can buy it at ").append(ChatColor.BLUE).append("Ocelot").toString());
                        return false;
                    }
                if(args[0].equalsIgnoreCase("Blaze"))
                    if(sender.hasPermission("blivutils.promote.blaze") && !sender.hasPermission("blivutils.promote.pigzombie") && !sender.hasPermission("blivutils.promote.ghast") || !(sender instanceof Player))
                    {
                        Player player = (Player)sender;
                        return rankQuery(player, 2);
                    } else
                    {
                        sender.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("You can't buy this at your rank! You can buy it at ").append(ChatColor.RED).append("MagmaSlime only!").toString());
                        return false;
                    }
                if(args[0].equalsIgnoreCase("PigZombie"))
                    if(sender.hasPermission("blivutils.promote.pigzombie") && !sender.hasPermission("blivutils.promote.ghast") || !(sender instanceof Player))
                    {
                        Player player = (Player)sender;
                        return rankQuery(player, 3);
                    } else
                    {
                        sender.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("You can't buy this at your rank! You can buy it at ").append(ChatColor.RED).append("Blaze").toString());
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
                        sender.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("You can't buy this at your rank! You can buy it at ").append(ChatColor.RED).append("PigZombie").toString());
                        return false;
                    }
                } else
                {
                    sender.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("You didnt specify a valid rank!").toString());
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
                    sender.sendMessage((new StringBuilder()).append("You have been promoted to ").append(ChatColor.RED).append(rankString).toString());
                    String groups[] = perms.getPlayerGroups(null, player);
                    for(int i = 0; i < groups.length; i++)
                        perms.playerRemoveGroup(null, player, groups[i]);

                    perms.playerAddGroup(null, player, rankString);
                    log.info((new StringBuilder()).append("Player").append(sender.getName()).append(" has been promoted to ").append(rankString).toString());
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
            price = (new StringBuilder()).append(b).append("$").append(ChatColor.GREEN).append(a).append("25,000").toString();
        else
        if(rank == 2)
            price = (new StringBuilder()).append(b).append("$").append(ChatColor.GREEN).append(a).append("50,000").toString();
        else
        if(rank == 3)
            price = (new StringBuilder()).append(b).append("$").append(ChatColor.GREEN).append(a).append("75,000").toString();
        else
        if(rank == 4)
        {
            price = (new StringBuilder()).append(b).append("$").append(ChatColor.GREEN).append(a).append("100,000").toString();
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
            player.sendMessage((new StringBuilder()).append("Are you sure you want to spend ").append(price).append(ChatColor.RESET).append(" on ").append(ChatColor.RED).append(rankName).append("?").toString());
            player.sendMessage((new StringBuilder()).append("Type ").append(ChatColor.GREEN).append("/promoteme").append(ChatColor.RESET).append(" to Accept").toString());
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

    private static BlivUtils bplugin;
    private Permission perms;
    private Economy econ;
    private HashMap promoteCount;
    private static final Logger log = Logger.getLogger("Minecraft");

}
