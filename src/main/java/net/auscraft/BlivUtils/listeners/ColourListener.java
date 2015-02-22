package net.auscraft.BlivUtils.listeners;

import java.util.Arrays;
import java.util.List;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
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

public class ColourListener implements Listener 
{
	
	private Utilities util;
	private Economy econ;
	
	public ColourListener(BlivUtils instance)
	{
		util = instance.getUtil();
		econ = instance.setupEconomy();
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack ConfirmPurchase(String colour)
	{
		ItemStack item = null;
		ItemMeta meta = null;
        switch(colour)
        {
	        case "Dark Grey": item = new ItemStack(Material.WOOL, 1, DyeColor.BLACK.getData()); meta = item.getItemMeta(); meta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Dark Grey")); break;
			case "Gold": item = new ItemStack(Material.WOOL, 1, DyeColor.ORANGE.getData()); meta = item.getItemMeta(); meta.setLore(Arrays.asList(ChatColor.GOLD + "Gold")); break;
			case "Aqua": item = new ItemStack(Material.WOOL, 1, DyeColor.LIGHT_BLUE.getData()); meta = item.getItemMeta(); meta.setLore(Arrays.asList(ChatColor.AQUA + "Aqua")); break;
			case "Yellow": item = new ItemStack(Material.WOOL, 1, DyeColor.YELLOW.getData()); meta = item.getItemMeta(); meta.setLore(Arrays.asList(ChatColor.YELLOW + "Yellow")); break;
			case "Dark Green": item = new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getData()); meta = item.getItemMeta(); meta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Dark Green")); break;
			case "Dark Purple": item = new ItemStack(Material.WOOL, 1, DyeColor.PURPLE.getData()); meta = item.getItemMeta(); meta.setLore(Arrays.asList(ChatColor.DARK_PURPLE + "Dark Purple")); break;
			case "Grey": item = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getData()); meta = item.getItemMeta(); meta.setLore(Arrays.asList(ChatColor.GRAY + "Grey")); break;
			case "Dark Aqua": item = new ItemStack(Material.WOOL, 1, DyeColor.BLUE.getData()); meta = item.getItemMeta(); meta.setLore(Arrays.asList(ChatColor.DARK_AQUA + "Dark Aqua")); break;
			case "Blue": item = new ItemStack(Material.LAPIS_BLOCK, 1); meta = item.getItemMeta(); meta.setLore(Arrays.asList(ChatColor.BLUE + "Blue")); break;
			case "Green": item = new ItemStack(Material.WOOL, 1, DyeColor.LIME.getData()); meta = item.getItemMeta(); meta.setLore(Arrays.asList(ChatColor.GREEN + "Green")); break;
			case "Red": item = new ItemStack(Material.WOOL, 1, DyeColor.RED.getData()); meta = item.getItemMeta(); meta.setLore(Arrays.asList(ChatColor.RED + "Red")); break;
			case "Light Purple": item = new ItemStack(Material.WOOL, 1, DyeColor.PINK.getData()); meta = item.getItemMeta(); meta.setLore(Arrays.asList(ChatColor.LIGHT_PURPLE + "Light Purple")); break;
			case "White": item = new ItemStack(Material.WOOL, 1, DyeColor.WHITE.getData()); meta = item.getItemMeta(); meta.setLore(Arrays.asList("White")); break;
        }
        List<String> lore = meta.getLore();
        lore.add(ChatColor.DARK_GREEN + "$" + ChatColor.WHITE + "200 to apply");
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.GREEN + "Confirm Purchase");
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
		if(event.getInventory().getTitle().contains("Chat Colour"))
		{
			event.setCancelled(true);
			Player p = (Player)event.getWhoClicked();
			if(p.hasPermission("blivutils.chat"))
			{
				//Slot was empty
				if(event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR))
				{
	                return;
				}
				else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_GRAY + "Dark Grey"))
				{
					doConfirmationDialog("Dark Grey", p);
				}
				else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Gold"))
				{
					doConfirmationDialog("Gold", p);
				}
				else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Aqua"))
				{
					doConfirmationDialog("Aqua", p);
				}
				else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Yellow"))
				{
					doConfirmationDialog("Yellow", p);
				}
				else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_GREEN + "Dark Green"))
				{
					doConfirmationDialog("Dark Green", p);
				}
				else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Dark Purple"))
				{
					doConfirmationDialog("Dark Purple", p);
				}
				else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Grey"))
				{
					doConfirmationDialog("Grey", p);
				}
				else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_AQUA + "Dark Aqua"))
				{
					doConfirmationDialog("Dark Aqua", p);
				}
				else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Blue"))
				{
					doConfirmationDialog("Blue", p);
				}
				else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Green"))
				{
					doConfirmationDialog("Green", p);
				}
				else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Red"))
				{
					doConfirmationDialog("Red", p);
				}
				else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "Dark Red"))
				{
					doConfirmationDialog("Dark Red", p);
				}
				else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.LIGHT_PURPLE + "Light Purple"))
				{
					doConfirmationDialog("Light Purple", p);
				}
				else if(event.getCurrentItem().getItemMeta().getDisplayName().contains("White"))
				{
					doConfirmationDialog("White", p);
				}
				else
				{
					p.sendMessage("What did you do to get here?");
				}
			}
			else
			{
				util.printError((CommandSender) p, "You dont have permission to change your colour! (Required: Blaze Rank)");
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
			if(event.getCurrentItem().getType().equals(Material.BARRIER))
			{
				p.closeInventory();
				return;
			}
			String woolType = event.getCurrentItem().getItemMeta().getLore().get(0);
			if(woolType.contains("Dark Grey"))
			{
				colourMe(p, "&8", "Dark Grey");
			}
			else if(woolType.contains("Gold"))
			{
				colourMe(p, "&6", "Gold");
			}
			else if(woolType.contains("Aqua"))
			{
				colourMe(p, "&b", "Aqua");
			}
			else if(woolType.contains("Yellow"))
			{
				colourMe(p, "&e", "Yellow");
			}
			else if(woolType.contains("Dark Green"))
			{
				colourMe(p, "&2", "Dark Green");
			}
			else if(woolType.contains("Dark Purple"))
			{
				colourMe(p, "&5", "Dark Purple");
			}
			else if(woolType.contains("Grey"))
			{
				colourMe(p, "&7", "Grey");
			}
			else if(woolType.contains("Dark Aqua"))
			{
				colourMe(p, "&3", "Dark Aqua");
			}
			else if(woolType.contains("Blue"))
			{
				colourMe(p, "&9", "Blue");
			}
			else if(woolType.contains("Green"))
			{
				colourMe(p, "&a", "Green");
			}
			else if(woolType.contains("Red"))
			{
				colourMe(p, "&c", "Red");
			}
			else if(woolType.contains("Light Purple"))
			{
				colourMe(p, "&d", "Light Purple");
			}
			else if(woolType.contains("White"))
			{
				colourMe(p, "&f", "White");
			}
			else
			{
				p.sendMessage("What did you do to get here?");
			}
			p.closeInventory();
		}
	}
	
	private void colourMe(Player p, String colour, String readable)
	{
		if (econ.has(p, 200))
		{
			econ.withdrawPlayer(p, 200);
			PermissionUser user = PermissionsEx.getUser(p);
			user.setSuffix(colour, null);
			util.printSuccess((CommandSender) p, "Set to " + readable);
			util.logInfo(ChatColor.GOLD + "Successfully set " + p.getName()	+ "'s chat colour to " + readable);
		} 
		else
		{
			util.printError((CommandSender) p, "You need at least $200 to buy that colour!");
		}
	}
	
	private void doConfirmationDialog(String colour, Player p)
	{
		Inventory inv = Bukkit.createInventory(null, 27, "Confirmation");
	    inv.setItem(12, ConfirmPurchase(colour));
	    inv.setItem(14, ConfirmQuit());
	    p.openInventory(inv);
	}
}
