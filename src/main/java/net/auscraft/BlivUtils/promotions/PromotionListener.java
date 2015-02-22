package net.auscraft.BlivUtils.promotions;

import java.util.Arrays;
import java.util.List;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PromotionListener implements Listener
{
	
	private Utilities util;
	private Economy econ;
	private Permission perms;

	public PromotionListener(BlivUtils instance)
	{
		util = instance.getUtil();
		econ = instance.setupEconomy();
		perms = instance.setupPermissions();
	}
	
	public ItemStack ConfirmPurchase(String rank)
	{
		String price = "N/A";
		ItemStack item = null;
        switch(rank)
        {
	        case "MagmaSlime": price = "25,000";
	        item = new ItemStack(Material.MAGMA_CREAM, 1);
	        break;
	        case "Blaze": price = "50,000";
	        item = new ItemStack(Material.BLAZE_ROD, 1);
	        break;
	        case "PigZombie": price = "75,000";
	        item = new ItemStack(Material.GOLD_SWORD, 1);
	        break;
	        case "Ghast": price = "100,000";
	        item = new ItemStack(Material.GHAST_TEAR, 1);
	        break;
	        case "Endermite": price = "100,000";
	        item = new ItemStack(Material.ENDER_PEARL, 1);
	        break;
        }
        
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Confirm Purchase");
        List<String> lore = Arrays.asList(ChatColor.DARK_GREEN + "$" + ChatColor.WHITE + price);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
	}
	
	public ItemStack ConfirmQuit()
	{
		ItemStack item = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Quit");
        item.setItemMeta(meta);
        return item;
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		if(event.getInventory().getTitle().contains("/buyrank"))
		{
			event.setCancelled(true);
			Player p = (Player)event.getWhoClicked();
			//Slot was empty
			if(event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR))
			{
                return;
			}
			else if(event.getCurrentItem().getType().equals(Material.MAGMA_CREAM))
			{
				doConfirmationDialog("MagmaSlime", p, p.hasPermission("blivutils.promote.magmaslime"));
			}
			else if(event.getCurrentItem().getType().equals(Material.BLAZE_ROD))
			{
				doConfirmationDialog("Blaze", p, p.hasPermission("blivutils.promote.blaze"));
			}
			else if(event.getCurrentItem().getType().equals(Material.GOLD_SWORD))
			{
				doConfirmationDialog("PigZombie", p, p.hasPermission("blivutils.promote.pigzombie"));
			}
			else if(event.getCurrentItem().getType().equals(Material.GHAST_TEAR))
			{
				doConfirmationDialog("Ghast", p, p.hasPermission("blivutils.promote.ghast"));
			}
			else if(event.getCurrentItem().getType().equals(Material.ENDER_PEARL))
			{
				doConfirmationDialog("Endermite", p, p.hasPermission("blivutils.promote.done"));
			}
		}
		else if(event.getInventory().getTitle().contains("Confirmation"))
		{
			event.setCancelled(true);
			Player p = (Player)event.getWhoClicked();
			//Slot was empty
			if(event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR))
			{
                return;
			}
			else if(event.getCurrentItem().getType().equals(Material.MAGMA_CREAM))
			{
				rankPromotion(p, "MagmaSlime");
				p.closeInventory();
			}
			else if(event.getCurrentItem().getType().equals(Material.BLAZE_ROD))
			{
				rankPromotion(p, "Blaze");
				p.closeInventory();
			}
			else if(event.getCurrentItem().getType().equals(Material.GOLD_SWORD))
			{
				rankPromotion(p, "PigZombie");
				p.closeInventory();
			}
			else if(event.getCurrentItem().getType().equals(Material.GHAST_TEAR))
			{
				rankPromotion(p, "Ghast");
				p.closeInventory();
			}
			else if(event.getCurrentItem().getType().equals(Material.ENDER_PEARL))
			{
				rankPromotion(p, "Endermite");
				p.closeInventory();
			}
			else if(event.getCurrentItem().getType().equals(Material.BARRIER))
			{
				p.closeInventory();
			}
			
		}
	}
	
	private void doConfirmationDialog(String rank, Player p, boolean hasPerm)
	{
		if(hasPerm)
		{
			Inventory inv = Bukkit.createInventory(null, 27, "Confirmation");
	        inv.setItem(12, ConfirmPurchase(rank));
	        inv.setItem(14, ConfirmQuit());
	        p.openInventory(inv);
		}
		else
		{
			if(!rank.equals("Endermite"))
			{
				util.printError((CommandSender) p, "You don't have permission to buy this rank!");
			}
			else
			{
				util.printError((CommandSender) p, "You don't have permission to rent this rank!");
			}
		}
	}
	
	private void rankPromotion(Player p, String rank)
	{
		double price = 0.0;
		price = getRankPrice(rank);

		if (econ.has(p, price)) 
		{
			econ.withdrawPlayer(p, price);
			String[] groups = perms.getPlayerGroups(null, p);
			// Don't Demote the Endermite Renters!
			if (!rank.equals("Endermite")) 
			{
				for (int i = 0; i < groups.length; i++) 
				{
					perms.playerRemoveGroup(null, p, groups[i]);
				}
				perms.playerAddGroup(null, p, rank);
				util.printSuccess((CommandSender) p, "You have been promoted to " + ChatColor.RED + rank);
			}
			else if (rank.equals("Endermite")) 
			{
				PermissionUser user = PermissionsEx.getUser(p);
				user.addGroup("Endermite", null);
				user.setOption("group-Endermite-until", "1296000");
				util.printSuccess((CommandSender) p, "You have been promoted to " + ChatColor.RED + rank + ChatColor.WHITE + " for " + ChatColor.GREEN + "15 days");
			}
			util.logInfo("Player " + p.getName() + " has been promoted to " + rank);
			util.logtoFile("Player " + p.getName() + " has been promoted to " + rank, null);
		}
		else 
		{
			util.printError((CommandSender) p, "You dont have sufficient funds to be promoted!");
		}
	}
	
	//Random reference code
	private double getRankPrice(String rank)
	{
		double price = 0.0;
		switch(rank)
		{
			case "MagmaSlime": price = 25000.0;
			break;
			case "Blaze": price = 50000.0;
			break;
			case "PigZombie": price = 75000.0;
			break;
			case "Ghast": case "Endermite": price = 10000.0;
			break;
			default: price = -1.0;
			break;
		}
		return price;
	}
	
}
