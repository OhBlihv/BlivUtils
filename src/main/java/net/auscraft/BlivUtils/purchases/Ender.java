package net.auscraft.BlivUtils.purchases;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Ender implements CommandExecutor
{
	
	private Utilities util;
	private static HashMap<String, Boolean> xpClaim;
	private static HashMap<String, Boolean> fixClaim;
	
	
	public Ender(BlivUtils instance)
	{
		util = instance.getUtil();
		xpClaim = instance.getXPClaim();
		fixClaim = instance.getFixClaim();
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
					util.printError(sender, "You can only apply names/lore to tools or armour.");
					return true;
				}
				
				ItemMeta meta = item.getItemMeta();
				
				//Remove the name of the item
				if(args[0].equalsIgnoreCase("removename"))
				{
					meta.setDisplayName(null);
					item.setItemMeta(meta);
					p.setItemInHand(item);
					util.printSuccess(sender, "Removed display name of item");
				}
				//Remove the meta of the item
				else if(args[0].equalsIgnoreCase("removelore"))
				{
					meta.setLore(null);
					item.setItemMeta(meta);
					p.setItemInHand(item);
					util.printSuccess(sender, "Removed lore name of item");
				}
				return true;
			}
			else if(args.length >= 2)
			{
				Player p = (Player)sender;
				ItemStack item = p.getItemInHand();
				Material material = item.getType();
				if (material.isBlock() || material.getMaxDurability() < 1)
				{
					util.printError(sender, "You can only apply names/lore to tools or armour.");
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
					String displayName = util.translateColours(msg);
					util.logDebug(util.translateColours(msg));
					meta.setDisplayName(displayName);
					List<String> lore = meta.getLore();
					//If item already has data that it has been edited by the player
					try
					{
						if(lore.contains("Lore edited by"))
						{
							lore = lore.subList(0, (lore.size() - 1));
							lore.add((util.translateColours("&5Lore edited by &o" + sender.getName())));
						}
						else
						{
							lore.add("Lore edited by " + sender.getName());
						}
					}
					catch(NullPointerException e)
					{
						lore = Arrays.asList((util.translateColours("&5Lore edited by &o" + sender.getName())));
					}
					
					meta.setLore(lore);
					item.setItemMeta(meta);
					p.setItemInHand(item);
					util.printSuccess(sender, "Successfully updated display name of item");
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
					msg = util.translateColours(msg);
					String[] tempMsgArr = msg.split("[|]");
					int msgArrLength = tempMsgArr.length + 1;
					if(msgArrLength > 4)
					{
						msgArrLength = 4;
					}
					util.logDebug("Length of tempmsgArr = " + tempMsgArr.length);
					util.logDebug("Length of msgArrLength = " + msgArrLength);
					String[] msgArr = new String[msgArrLength];
					util.logDebug("msgArr.length = " + msgArr.length);
					int tempMsgArrLength = tempMsgArr.length;
					if(tempMsgArrLength > 3)
					{
						tempMsgArrLength = 3;
					}
					for(int i = 0;i < tempMsgArrLength;i++)
					{
						msgArr[i] = util.translateColours(tempMsgArr[i]);
						util.logDebug("Line " + i + " of tempMsgArr = " + tempMsgArr[i]);
					}
					msgArr[msgArr.length - 1] = util.translateColours("&5Lore edited by &o" + sender.getName());
					
					//TODO: Format using util.translateColours()
					List<String> lore = new ArrayList<String>(Arrays.asList(msgArr));
					meta.setLore(lore);
					item.setItemMeta(meta);
					p.setItemInHand(item);
					util.printSuccess(sender, "Updated lore name of item");
					return true;
				}
				else
				{
					util.printError(sender, "Syntax: /lore <name|lore|removename|removelore> [string]");
				}
			}
			else
			{
				util.printError(sender, "Syntax: /lore <name|lore|removename|removelore> [string]");
			}
			return true;
		}
		//Once a day xp claim
		else if (cmd.getName().equalsIgnoreCase("xpclaim") && (sender.hasPermission("blivutils.ender.xpclaim")))
		{
			if(!(xpClaim.containsKey(sender.getName())))
			{
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "xp 500 " + sender.getName());
				util.printSuccess(sender, "500 XP claimed to your account");
				xpClaim.put(sender.getName(), true);
			}
			else
			{
				util.printError(sender, "You've already redeemed your xp bonus today!");
			}
			
		}
		//Once a day item fix
		else if (cmd.getName().equalsIgnoreCase("fixClaim")	&& (sender.hasPermission("blivutils.purch")))
		{
			if(!(fixClaim.containsKey(sender.getName())))
			{
				Player p = (Player)sender;
				ItemStack item = p.getItemInHand();
				final Material material = Material.getMaterial(item.getTypeId());
				if (material.isBlock() || material.getMaxDurability() < 1)
				{
					util.printError(sender, "That item is not repairable! Try a tool for instance.");
					return true;
				}

				if (item.getDurability() == 0)
				{
					util.printError(sender, "That item is already repaired!");
					return true;
				}

				item.setDurability((short)0);
				util.printSuccess(sender, "Your " + item.getType().toString() + " has been successfully repaired.");
				fixClaim.put(sender.getName(), true);
				
			}
			else
			{
				util.printError(sender, "You've already redeemed your free item fix today! Come back tomorrow.");
			}
		}
		//Base ender rank command
		else if (cmd.getName().equalsIgnoreCase("enderrank")	&& (sender.hasPermission("blivutils.purch"))) //Only for Console
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
					util.wipePackages(args[1]);
					/*for(int i = 0;i < enderPackages.length;i++)
					{
						PermissionUser user = PermissionsEx.getUser(args[0]);
						//user.setOption("group-" + enderPackages[i] + "-until", "0");
						user.removeGroup(enderPackages[i]);
					}*/
					util.logInfo("Wiped all packages from " + args[1] + "'s account");
					return true;
				}
				else
				{
					switch(args[1])
					{
						case "EnderPetsPassive": case "EnderPetsNeutral": case "EnderPetsHostile": case "EnderPetsALL":
						case "EnderTrailsSET1": case "EnderTrailsSET2": case "EnderTrailsALL":
						case "EnderDisguisesPassive": case "EnderDisguisesHostile": case "EnderDisguisesALL": case "EnderDisguisesALLEntity":
						case "EnderCooldowns": case "EnderWarps":
						case "EnderDoubleXP": case "EnderKeepInv": case "EnderKeepLevel": case "EnderDoubleXPKeepALL":
						case "mcMMOSmall": case "mcMMOMedium": case "mcMMOLarge":
							PermissionUser user = PermissionsEx.getUser(args[0]);
							user.setOption("group-" + args[1] + "-until", ((int) (System.currentTimeMillis() / 1000L)) + "2592000");
							user.addGroup(args[1]);
							util.logInfo("Package " + args[1] + " successfully added to " + args[0] + "'s account");
							return true;
						default:
							//Stay Silent, otherwise console will be spammed if a player doesnt use a variable
							return true;
					}
				}
				
				
			}
			// /enderrank <player> <time> <packages>
			// <packages> is a comma delimited list of packages which are subscribed to, or need updating.
			else if(args.length >= 3)
			{
				util.updatePackages(args[0], args[1], args[2]);
			}
			return true;
		}
		return false;
	}

}
