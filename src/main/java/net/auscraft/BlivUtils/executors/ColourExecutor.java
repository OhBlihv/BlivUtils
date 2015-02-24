package net.auscraft.BlivUtils.executors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class ColourExecutor implements CommandExecutor
{

	public ItemStack WoolColour(String colour)
	{
		ItemStack item = new ItemStack(Material.WOOL, 1);
		ItemMeta meta = item.getItemMeta();
		MaterialData woolColour = null;
		switch(colour)
		{
			case "Dark Gray": item = new ItemStack(Material.WOOL, 1, DyeColor.BLACK.getData()); meta = item.getItemMeta(); meta.setDisplayName(ChatColor.DARK_GRAY + "Dark Grey"); break;
			case "Gold": item = new ItemStack(Material.WOOL, 1, DyeColor.ORANGE.getData()); meta = item.getItemMeta(); meta.setDisplayName(ChatColor.GOLD + "Gold"); break;
			case "Aqua": item = new ItemStack(Material.WOOL, 1, DyeColor.LIGHT_BLUE.getData()); meta = item.getItemMeta(); meta.setDisplayName(ChatColor.AQUA + "Aqua"); break;
			case "Yellow": item = new ItemStack(Material.WOOL, 1, DyeColor.YELLOW.getData()); meta = item.getItemMeta(); meta.setDisplayName(ChatColor.YELLOW + "Yellow"); break;
			case "Dark Green": item = new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getData()); meta = item.getItemMeta(); meta.setDisplayName(ChatColor.DARK_GREEN + "Dark Green"); break;
			case "Dark Purple": item = new ItemStack(Material.WOOL, 1, DyeColor.PURPLE.getData()); meta = item.getItemMeta(); meta.setDisplayName(ChatColor.DARK_PURPLE + "Dark Purple"); break;
			case "Gray": item = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getData()); meta = item.getItemMeta(); meta.setDisplayName(ChatColor.GRAY + "Grey"); break;
			case "Dark Aqua": item = new ItemStack(Material.WOOL, 1, DyeColor.BLUE.getData()); meta = item.getItemMeta(); meta.setDisplayName(ChatColor.DARK_AQUA + "Dark Aqua"); break;
			case "Blue": item = new ItemStack(Material.LAPIS_BLOCK, 1); meta = item.getItemMeta(); meta.setDisplayName(ChatColor.BLUE + "Blue"); break;
			case "Green": item = new ItemStack(Material.WOOL, 1, DyeColor.LIME.getData()); meta = item.getItemMeta(); meta.setDisplayName(ChatColor.GREEN + "Green"); break;
			case "Red": item = new ItemStack(Material.WOOL, 1, DyeColor.RED.getData()); meta = item.getItemMeta(); meta.setDisplayName(ChatColor.RED + "Red"); break;
			case "Light Purple": item = new ItemStack(Material.WOOL, 1, DyeColor.PINK.getData()); meta = item.getItemMeta(); meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Light Purple"); break;
			case "White": meta.setDisplayName("White"); break;
		}
		item.setData(woolColour);
        item.setItemMeta(meta);
        return item;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) 
	{
		if (cmd.getName().equalsIgnoreCase("chat") && (sender instanceof Player))
		{
			Player p = (Player) sender;
			Inventory inv = Bukkit.createInventory(null, 45, "Chat Colour");
	        inv.setItem(29, WoolColour("Dark Gray"));
	        inv.setItem(11, WoolColour("Gold"));
	        inv.setItem(12, WoolColour("Aqua"));
	        inv.setItem(14, WoolColour("Yellow"));
	        inv.setItem(15, WoolColour("Dark Green"));
	        inv.setItem(33, WoolColour("Dark Purple"));
	        inv.setItem(30, WoolColour("Gray"));
	        inv.setItem(20, WoolColour("Dark Aqua"));
	        inv.setItem(21, WoolColour("Blue"));
	        inv.setItem(32, WoolColour("Green"));
	        inv.setItem(23, WoolColour("Red"));
	        inv.setItem(24, WoolColour("Light Purple"));
	        inv.setItem(22, WoolColour("White"));
	        ((Player) sender).openInventory(inv);
	        return true;
		}
		return true;
	}
}
