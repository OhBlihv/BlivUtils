package net.auscraft.BlivUtils.rewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.config.ConfigAccessor;
import net.auscraft.BlivUtils.config.ConfigSetup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;


public class ChristmasExecutor implements CommandExecutor
{
	
	public static final String ChristmasPrefix = ChatColor.DARK_RED + "" + ChatColor.ITALIC + 	"C"
										+ ChatColor.DARK_GREEN + "" + ChatColor.ITALIC + 		"H"
										+ ChatColor.DARK_RED + "" + ChatColor.ITALIC +			"R"
										+ ChatColor.DARK_GREEN + "" + ChatColor.ITALIC +		"I"
										+ ChatColor.DARK_RED + "" + ChatColor.ITALIC + 			"S"
										+ ChatColor.DARK_GREEN + "" + ChatColor.ITALIC + 		"T"
										+ ChatColor.DARK_RED + "" + ChatColor.ITALIC + 			"M"
										+ ChatColor.DARK_GREEN + "" + ChatColor.ITALIC + 		"A"
										+ ChatColor.DARK_RED + "" + ChatColor.ITALIC + 			"S"; //What does that spell? CHRISTMAS!
	//The above string spells out "Christmas" in alternating Red/Green italicised colours.
	
	
	private RewardContainer[][] rewardsTable;
	private BlivUtils b;
	private Logger log;
	private ConfigAccessor cfg;
	
	//Gift specific global variables
	private int[] totalGiftPool = {0,0,0,0,0}; //Initialize the counts to 0, and add to them in the constructor.
	
	public ChristmasExecutor(BlivUtils instance)
	{
		b = instance;
		log = b.getLogger();
		cfg = b.getCfg();		
		
		loadRewards(); 
	}
	
	private void loadRewards()
	{
		//Read this in from file or something based on how many there actually are.
		rewardsTable = cfg.loadRewards();
		if(rewardsTable == null)
		{
			throw new NullPointerException("Rewards Table is blank!");
		}
		try
		{
			for(int i = 0;i < rewardsTable.length;i++)
			{
				//log.info("|Loading Pool " + (i+1));
				for(int ii = 0;ii < rewardsTable[i].length;ii++)
				{
					//log.info(" - Loading Reward " + (ii+1));
					if(rewardsTable[i][ii] != null)
					{
						totalGiftPool[i]++;
						//log.info("   - " + rewardsTable[i][ii].getName());
					}
					else
					{
						break;
					}
				}
			}
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		log.info((totalGiftPool[0] + totalGiftPool[1] + totalGiftPool[2] + totalGiftPool[3] + totalGiftPool[4]) + " rewards imported");
	}
	
	//TODO: Move to its own utilities class (I use this in more than one place!)
	private String translateColours(String string)
	{
		String fixedString;
		Pattern chatColorPattern = Pattern.compile("(?i)&([0-9A-Fa-f])"); // Credit to t3hk0d3 in ChatManager(With slight edits)
		fixedString = chatColorPattern.matcher(string).replaceAll("\u00A7$1"); // And here too
		return fixedString;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) 
	{
		if (cmd.getName().equalsIgnoreCase("present"))
		{
			if(args.length >= 1)
			{
				if(args[0].equals("open") && (sender instanceof Player))
				{
					//5 Gifts will be randomized later?
					int numberGifts = rewardsTable.length;
					RewardContainer[] rolledGift = new RewardContainer[numberGifts];
					String rewardString = "";
					Random rand = new Random(System.currentTimeMillis());
					
					//Should I bother randomising if they're good or bad?
					sender.sendMessage(ChatColor.DARK_GREEN + "Merry " + ChatColor.BLUE + "" + ChatColor.BOLD + "Aus"	+ ChatColor.WHITE + ChatColor.BOLD + "Craft " + ChristmasPrefix);
					sender.sendMessage(ChatColor.GREEN + "Looks like you're on my " + ChatColor.DARK_GREEN + "nice list" + ChatColor.GREEN + " and will be receiving "
										+ ChatColor.DARK_GREEN + numberGifts + " gifts!");
					
					sender.sendMessage(ChatColor.GOLD + "Rolled Gifts: ");
					// Pools:
					// Pool One: Weapon (Enchantments equivalent to material)
					// Pool Two: Tool (Enchantments equivalent to material)
					// Pool Three: Armour (Enchantments equivalent to material)
					// Pool Four: ??? 
					// Pool Five: ???
					
					//Pool One ---------------
					for(int i = 0;i < numberGifts;i++)
					{
						try
						{
							rolledGift[i] = rewardsTable[i][rand.nextInt(totalGiftPool[i])];
							log.info(rolledGift[i].getName());
							
							rewardString += rolledGift[i].getName() + ", \n";
						}
						catch(NullPointerException e)
						{
							e.printStackTrace();
						}
					}
					
					
					//TODO: Remove the trailing comma
					sender.sendMessage(rewardString);
					
					giveRewards(sender, rolledGift);
					
					return true;
				}
				else if(args[0].equals("reload"))
				{
					if(sender.hasPermission("blivutils.rewards.admin"))
					{
						cfg.loadRewards();
						sender.sendMessage(ChatColor.GREEN + "Loaded " + ((totalGiftPool[0] + totalGiftPool[1] + totalGiftPool[2] + totalGiftPool[3] + totalGiftPool[4]) + " rewards"));
					}
				}
				return true;
			}
			return true;
		}
		
		return false;
	}
	
	private void giveRewards(CommandSender sender, RewardContainer[] rolledGift)
	{
		Player p = (Player) sender;
		for(int i = 0;i < rolledGift.length;i++)
		{
			try
			{
			log.info("Giving Reward: " + rolledGift[i].getName());
			if(rolledGift[i].getAction().startsWith("-"))	//Reward is a command
			{
				String action = rolledGift[i].getAction().substring(1, rolledGift[i].getAction().length());
				action = action.replaceAll("%", p.getName()); //Replace % with players name
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), action);
				p.sendMessage("Reward given!");
				log.info("Command: '" + action + "'");
			}
			else	// Reward is an item
			{
				Material material = Material.AIR;
				Material item = material.getMaterial(Integer.parseInt(rolledGift[i].getAction()));
				ItemStack reward = new ItemStack(item);
				
				if(rolledGift[i].getName() != null)
				{
					ItemMeta meta = reward.getItemMeta();
					
					meta.setDisplayName (translateColours(rolledGift[i].getName()));
					reward.setItemMeta(meta);
				}
				
				if((rolledGift[i].getLore() != null) && (rolledGift[i].getLore() != "NONE_GIVEN"))	//Lore is not empty
				{
					List<String> lore = new ArrayList<>();
					lore.add(translateColours(rolledGift[i].getLore()));
					ItemMeta meta = reward.getItemMeta();
					meta.setLore(lore);
					reward.setItemMeta(meta);
				}
				
				if((rolledGift[i].getEnchants() != null) && (rolledGift[i].getEnchants()[0][0] != -1)) //Enchantment listings are not empty
				{
					for(int ii = 0;ii < rolledGift[i].getEnchants().length;ii++)
					{
						int[] enchants = rolledGift[i].getEnchants()[i];
						//enchants[0] = Enchantment ID
						//enchants[1] = Base Enchantment Level
						//enchants[2] = Enchantment Variation (In Levels)
						
						//Level randomisation
						int level = enchants[1];
						boolean valid = true;
						if(Math.abs(enchants[2]) >= 1)
						{
							Random rand = new Random(System.currentTimeMillis());
							level = rand.nextInt(enchants[2]);
							log.info("Enchantment level: " + level);
							if(level <= 0)
							{
								valid = false; //If the enchantment level is below 0, or 0 itself, the enchantment cannot exist: Dont apply it.
							}
						}
						
						if(valid = true)
						{
							log.info("Enchantment " + enchants[0] + " with level " + level + " is valid.");
							/*
							0	minecraft:protection	Protection	Armor	4
							1	minecraft:fire_protection	Fire Protection	Armor	4
							2	minecraft:feather_falling	Feather Falling	Boots	4
							3	minecraft:blast_protection	Blast Protection	Armor	4
							4	minecraft:projectile_protection	Projectile Protection	Armor	4
							5	minecraft:respiration	Respiration	Helmets	3
							6	minecraft:aqua_affinity	Aqua Affinity	Helmets	1
							7	minecraft:thorns	Thorns	Armor	3
							8	minecraft:depth_strider	Depth Strider	Boots	3
							16	minecraft:sharpness	Sharpness	Swords, Axes	5
							17	minecraft:smite	Smite	Swords, Axes	5
							18	minecraft:bane_of_arthropods	Bane of Arthropods	Swords, Axes	5
							19	minecraft:knockback	Knockback	Swords	2
							20	minecraft:fire_aspect	Fire Aspect	Swords	2
							21	minecraft:looting	Looting	Swords	3
							32	minecraft:efficiency	Efficiency	Pickaxes, Shovels, Axes, Shears	5
							33	minecraft:silk_touch	Silk Touch	Pickaxes, Shovels, Axes, Shears	1
							34	minecraft:unbreaking	Unbreaking	Any Item with Durability	3
							35	minecraft:fortune	Fortune	Pickaxes, Shovels, Axes	3
							48	minecraft:power	Power	Bow	5
							49	minecraft:punch	Punch	Bow	2
							50	minecraft:flame	Flame	Bow	1
							51	minecraft:infinity	Infinity	Bow	1
							61	minecraft:luck_of_the_sea	Luck of the Sea	Fishing Rod	3
							62	minecraft:lure
							 */
							switch(enchants[0])
							{
								//PROTECTION (0),//TODO: Add break; to each case.
								case 0:
									reward.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, level);
									break;
								case 1:
									reward.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, level);
									break;
								case 2:
									reward.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, level);
									break;
								case 3:
									reward.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, level);
									break;
								case 4:
									reward.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, level);
									break;
								case 5:
									reward.addUnsafeEnchantment(Enchantment.OXYGEN, level);
									break;
								case 6:
									reward.addUnsafeEnchantment(Enchantment.WATER_WORKER, level);
									break;
								case 7:
									reward.addUnsafeEnchantment(Enchantment.THORNS, level);
									break;
								case 8:
									reward.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, level);
									break;
								case 16:
									reward.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, level);
									break;
								case 17:
									reward.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, level);
									break;
								case 18:
									reward.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, level);
									break;
								case 19:
									reward.addUnsafeEnchantment(Enchantment.KNOCKBACK, level);
									break;
								case 20:
									reward.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, level);
									break;
								case 21:
									reward.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, level);
									break;
								case 32:
									reward.addUnsafeEnchantment(Enchantment.DIG_SPEED, level);
									break;
								case 33:
									reward.addUnsafeEnchantment(Enchantment.SILK_TOUCH, level);
									break;
								case 34:
									reward.addUnsafeEnchantment(Enchantment.DURABILITY, level);
									break;
								case 35:
									reward.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, level);
									break;
								case 48:
									reward.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, level);
									break;
								case 49:
									reward.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, level);
									break;
								case 50:
									reward.addUnsafeEnchantment(Enchantment.ARROW_FIRE, level);
									break;
								case 51:
									reward.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, level);
									break;
								case 61:
									reward.addUnsafeEnchantment(Enchantment.LUCK, level);
									break;
								case 62:
									reward.addUnsafeEnchantment(Enchantment.LURE, level);
									break;
									
								default:
									break;
							}
						}
					}
				}
				else
				{
					log.info("Enchantment Space Empty/Null!");
				}
				
				
				p.getInventory().addItem(reward);
				p.sendMessage("Reward given!");
			}
			}
			catch(CommandException e)
			{
				log.severe("Error giving rewards! Check the config for misplaced ','");
				e.printStackTrace();
			}
			
		}
	}
	
}
