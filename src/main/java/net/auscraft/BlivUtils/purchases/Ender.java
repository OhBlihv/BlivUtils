package net.auscraft.BlivUtils.purchases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.BUtil;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Ender implements CommandExecutor
{
	
	@Getter
	private HashMap<String, Boolean> xpClaim;
	@Getter
	private HashMap<String, Boolean> fixClaim;
	
	
	public Ender()
	{
		this.xpClaim = BlivUtils.getXpClaim();
		this.fixClaim = BlivUtils.getFixClaim();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
	{
		// /lore command -- Allows players to set the lore on an item
		if (cmd.getName().equalsIgnoreCase("lore") && (sender.hasPermission("blivutils.ender.lore")))
		{
			if(args.length == 0)
			{
				sender.sendMessage(ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- "
						+ ChatColor.BLUE + "" + ChatColor.BOLD	+ "Aus"	+ ChatColor.WHITE + ChatColor.BOLD + "Craft" + ChatColor.YELLOW	+ " Lore "
						+ ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- "
						+ ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- "
						+ ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- \n"
						+ ChatColor.GREEN + "| " + ChatColor.AQUA + "/lore setname <name>" + ChatColor.WHITE + " - Set the displayname of an item\n"
						+ ChatColor.DARK_GREEN + "| " + ChatColor.AQUA + "/lore setlore <lore>" + ChatColor.WHITE + " - Set the lore of an item\n"
						+ ChatColor.GREEN + "| " + ChatColor.AQUA + "/lore removename" + ChatColor.WHITE + " - Remove the name of an item\n"
						+ ChatColor.DARK_GREEN + "| " + ChatColor.AQUA + "/lore removelore" + ChatColor.WHITE + " - Remove lore from an item\n"
						+ ChatColor.GREEN + "| " + ChatColor.AQUA + ChatColor.ITALIC + "PROTIP: " + ChatColor.RESET + " Separate lines in lore using '|'\n"
						+ ChatColor.DARK_GREEN + "| " + ChatColor.AQUA + "Example: " + ChatColor.RESET + "/lore setlore Line1|Line2|Line3\n"
						+ ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- "
						+ ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- "
						+ ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- "
						+ ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- "
						+ ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- "
						+ ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- " + ChatColor.GREEN + "- " + ChatColor.DARK_GREEN + "- ");
				return true;
			}
			else if(args.length == 1)
			{
				Player p = (Player)sender;
				ItemStack item = p.getItemInHand();
				Material material = item.getType();
				if (material.isBlock() || material.getMaxDurability() < 1)
				{
					BUtil.printError(sender, "You can only apply names/lore to tools or armour.");
					return true;
				}
				
				ItemMeta meta = item.getItemMeta();
				
				//Remove the name of the item
				if(args[0].equalsIgnoreCase("removename"))
				{
					meta.setDisplayName(null);
					item.setItemMeta(meta);
					p.setItemInHand(item);
					BUtil.printSuccess(sender, "Removed display name of item");
				}
				//Remove the meta of the item
				else if(args[0].equalsIgnoreCase("removelore"))
				{
					meta.setLore(null);
					item.setItemMeta(meta);
					p.setItemInHand(item);
					BUtil.printSuccess(sender, "Removed lore name of item");
				}
				return true;
			}
			else if(args.length >= 2)
			{
				Player player = (Player)sender;
				ItemStack item = player.getItemInHand();
				Material material = item.getType();
				if (material.isBlock() || material.getMaxDurability() < 1)
				{
					BUtil.printError(sender, "You can only apply names/lore to tools or armour.");
					return true;
				}
				
				ItemMeta meta = item.getItemMeta();
				//Set the name of the item
				if(args[0].equalsIgnoreCase("setname"))
				{
					//Join the string together
					String msg = "";
					for(int i = 1;i < args.length;i++)
					{
						msg += args[i] + " ";
					}
					String displayName = BUtil.translateColours(msg);
					BUtil.logDebug(BUtil.translateColours(msg));
					meta.setDisplayName(displayName);
					List<String> lore = meta.getLore();
					//If item already has data that it has been edited by the player
					try
					{
						if(lore.contains("Lore edited by"))
						{
							lore = lore.subList(0, (lore.size() - 1));
							lore.add((BUtil.translateColours("&5Lore edited by &o" + sender.getName())));
						}
						else
						{
							lore.add("Lore edited by " + sender.getName());
						}
					}
					catch(NullPointerException e)
					{
						lore = Arrays.asList((BUtil.translateColours("&5Lore edited by &o" + sender.getName())));
					}
					
					meta.setLore(lore);
					item.setItemMeta(meta);
					player.setItemInHand(item);
					BUtil.printSuccess(sender, "Successfully updated display name of item");
					return true;
				}
				//Set the meta of the item
				else if(args[0].equalsIgnoreCase("setlore"))
				{
					//Join the string together
					String msg = "";
					for(int i = 1;i < args.length;i++)
					{
						msg += args[i] + " ";
					}
					msg = BUtil.translateColours(msg);
					String[] tempMsgArr = msg.split("[|]");
					int msgArrLength = tempMsgArr.length + 1;
					if(msgArrLength > 4)
					{
						msgArrLength = 4;
					}
					//BUtil.logDebug("Length of tempmsgArr = " + tempMsgArr.length);
					//BUtil.logDebug("Length of msgArrLength = " + msgArrLength);
					String[] msgArr = new String[msgArrLength];
					//BUtil.logDebug("msgArr.length = " + msgArr.length);
					int tempMsgArrLength = tempMsgArr.length;
					if(tempMsgArrLength > 3)
					{
						tempMsgArrLength = 3;
					}
					for(int i = 0;i < tempMsgArrLength;i++)
					{
						msgArr[i] = BUtil.translateColours(tempMsgArr[i]);
						BUtil.logDebug("Line " + i + " of tempMsgArr = " + tempMsgArr[i]);
					}
					msgArr[msgArr.length - 1] = BUtil.translateColours("&5Lore edited by &o" + sender.getName());
					
					List<String> lore = new ArrayList<String>(Arrays.asList(msgArr));
					meta.setLore(lore);
					item.setItemMeta(meta);
					player.setItemInHand(item);
					BUtil.printSuccess(sender, "Updated lore name of item");
					return true;
				}
				else
				{
					BUtil.printError(sender, "Syntax: /lore <name|lore|removename|removelore> [string]");
				}
			}
			else
			{
				BUtil.printError(sender, "Syntax: /lore <name|lore|removename|removelore> [string]");
			}
			return true;
		}
		//Once a day xp claim
		else if (cmd.getName().equalsIgnoreCase("xpClaim") && (sender.hasPermission("blivutils.ender.xpclaim")))
		{
			if(!(xpClaim.containsKey(sender.getName())))
			{
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "xp 500 " + sender.getName());
				BUtil.printSuccess(sender, "500 XP claimed to your account");
				xpClaim.put(sender.getName(), true);
			}
			else
			{
				BUtil.printError(sender, "You've already redeemed your xp bonus today!");
			}
			
		}
		//Once a day item fix
		else if (cmd.getName().equalsIgnoreCase("fixClaim")	&& (sender.hasPermission("blivutils.ender.fixclaim")))
		{
			if(!(fixClaim.containsKey(sender.getName())))
			{
				Player p = (Player)sender;
				ItemStack item = p.getItemInHand();
				final Material material = Material.getMaterial(item.getTypeId());
				if (material.isBlock() || material.getMaxDurability() < 1)
				{
					BUtil.printError(sender, "That item is not repairable! Try a tool for instance.");
					return true;
				}

				if (item.getDurability() == 0)
				{
					BUtil.printError(sender, "That item is already repaired!");
					return true;
				}

				item.setDurability((short)0);
				BUtil.printSuccess(sender, "Your " + item.getType().toString() + " has been successfully repaired.");
				fixClaim.put(sender.getName(), true);
				
			}
			else
			{
				BUtil.printError(sender, "You've already redeemed your free item fix today! Come back tomorrow.");
			}
		}
		//Ender rank upgrade clumping
		//Adds the per-rank upgrades in one simple command
		else if (cmd.getName().equalsIgnoreCase("enderperm")	&& (sender.hasPermission("blivutils.purch"))) //Only for Console
		{
			/*
		    EnderDragon $15-$29.99
			/firework
			Once a Restart /fixclaim (DONE)
			Once a Restart /xpclaim (DONE)
			30HP (1 1/2 Bars)

			Wither $30.00+
			/lore (Add lore or a name to a weapon) (DONE)
			40HP (2 Bars)
			 */
			
			// /enderperm <add|remove> <player> <rank>
			if(args.length >= 2)
			{
				if(args[0].equalsIgnoreCase("add"))
				{
					PermissionUser user = PermissionsEx.getUser(args[1]);
					if(user != null)
					{
						if(args[2].equalsIgnoreCase("EnderDragon"))
						{
							user.addPermission("blivutils.ender.fixclaim");
							user.addPermission("blivutils.ender.xpclaim");
							user.addPermission("essentials.firework");
							user.addPermission("blivutils.ender.hearts");
							user.addPermission("blivutils.ender.hearts.oneandahalf");
						}
						else if(args[2].equalsIgnoreCase("Wither"))
						{
							user.addPermission("blivutils.ender.fixclaim");
							user.addPermission("blivutils.ender.xpclaim");
							user.addPermission("essentials.firework");
							user.addPermission("blivutils.ender.lore");
							user.addPermission("blivutils.ender.hearts");
							user.addPermission("blivutils.ender.hearts.double");
						}
						else
						{
							BUtil.printError(sender, "That rank does not exist for this command");
						}
						return true;
					}
					
					BUtil.printError(sender, "Player does not exist");
					return true;
					
				}
				else if(args[0].equalsIgnoreCase("remove"))
				{
					PermissionUser user = PermissionsEx.getUser(args[1]);
					if(user != null)
					{
						if(args[2].equalsIgnoreCase("EnderDragon"))
						{
							user.removePermission("blivutils.ender.fixclaim");
							user.removePermission("blivutils.ender.xpclaim");
							user.removePermission("essentials.firework.*");
							user.removePermission("blivutils.ender.hearts.oneandahalf");
						}
						else if(args[2].equalsIgnoreCase("Wither"))
						{
							user.removePermission("blivutils.ender.fixclaim");
							user.removePermission("blivutils.ender.xpclaim");
							user.removePermission("essentials.firework.*");
							user.removePermission("blivutils.ender.hearts.oneandahalf");
							user.removePermission("blivutils.ender.lore");
							user.removePermission("blivutils.ender.hearts.oneandahalf");
						}
						else
						{
							BUtil.printError(sender, "That rank does not exist for this command");
						}
						return true;
					}
					
					BUtil.printError(sender, "Player does not exist");
					return true;
				}
			}
			else
			{
				BUtil.printError(sender, "/enderperm <add|remove> <player> <rank>");
				return true;
			}
		}
		//Base ender rank command
		else if (cmd.getName().equalsIgnoreCase("enderrank") && (sender.hasPermission("blivutils.purch"))) //Only for Console
		{
			
		}
		else
		{
			BUtil.printError(sender, "You don't have permission to use that Ender Rank perk!");
		}
		return false;
	}
	
	public static boolean enderRank(String[] args)
	{
		/* Package Names ---------------------
		 * 
		 * Pets:
		 * EnderPetsPassive
		 * EnderPetsNeutral
		 * EnderPetsHostile
		 * EnderPetsALL
		 * 
		 * Trails:
		 * EnderTrailsSET1
		 * EnderTrailsSET2
		 * EnderTrailsALL
		 * 
		 * Disguises:
		 * EnderDisguisesPassive
		 * EnderDisguisesHostile
		 * EnderDisguisesALL
		 * EnderDisguisesALLEntity
		 * 
		 * Cooldowns:
		 * EnderCooldowns
		 * 
		 * Warps:
		 * EnderWarps
		 * 
		 * XP/Keep Inventory/Levels on Death
		 * EnderDoubleXP
		 * EnderKeepInv
		 * EnderKeepLevel
		 * EnderDoubleXPALL
		 * 
		 * mcMMO:
		 * mcMMOSmall
		 * mcMMOMedium
		 * mcMMOLarge
		 * 
		 * -----------------------------------
		 * 
		 * SYNTAX: /enderrank <player> <package>
		 */
		
		if(args.length == 2)
		{
			if(args[0].equals("wipe"))
			{
				BUtil.wipePackages(args[1]);
				BUtil.logInfo("Wiped all packages from " + args[1] + "'s account");
				return true;
			}
			
			PermissionUser user = PermissionsEx.getUser(args[0]);
			if(BUtil.getTimeLeft(args[0], "EnderRank") <= 0 || user.getPermissions(null).contains("blivutils.ender.purchasebypass"))
			{
				BUtil.logError("EnderRank not active -- Perks not added");
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						"mail send " + args[0] + " Your perk: " + args[1] + " could not be applied due to an inactive Ender Rank.");
				return true;
			}
			
			switch(args[1])
			{
				case "EnderPetsPassive": case "EnderPetsNeutral": case "EnderPetsHostile": case "EnderPetsALL":
				case "EnderTrailsSET1": case "EnderTrailsSET2": case "EnderTrailsALL":
				case "EnderDisguisesPassive": case "EnderDisguisesHostile": case "EnderDisguisesALL": case "EnderDisguisesALLEntity":
				case "EnderCooldowns": case "EnderWarps":
				case "EnderDoubleXP": case "EnderKeepInv": case "EnderKeepLevel": case "EnderDoubleXPKeepALL":
				case "mcMMOSmall": case "mcMMOMedium": case "mcMMOLarge":
					
					long currentLength = 0;
					try
					{
						currentLength = Long.parseLong(user.getOption("group-" + args[1] + "-until"));
					}
					catch(NullPointerException | NumberFormatException e)
					{
						//Current Length = 0
					}
					
					if(currentLength <= (System.currentTimeMillis() / 1000L))
					{
						currentLength = (System.currentTimeMillis() / 1000L);
					}
					long newLength = currentLength + 2592000;
					//util.logDebug("Current: " + currentLength + " |30Days: " + thirtyDays + " |New: " + newLength);
					user.setOption("group-" + args[1] + "-until", newLength + "");
					user.addGroup(args[1]);
					BUtil.logInfo("Package " + args[1] + " successfully added to " + args[0] + "'s account");
					return true;
				default:
					//Stay Silent, otherwise console will be spammed if a player doesnt use a variable
					return true;
			}
			
			
		}
		// /enderrank <player> <time> <packages>
		// <packages> is a comma delimited list of packages which are subscribed to, or need updating.
		else if(args.length >= 3)
		{
			BUtil.updatePackages(args[0], args[1], args[2]);
		}
		return true;
	}

}
