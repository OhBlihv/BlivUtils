package net.auscraft.BlivUtils.rewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.config.ConfigAccessor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
	
	public static final String ChristmasPrefix = ChatColor.DARK_RED + 		"C"
										+ ChatColor.DARK_GREEN +			"H"
										+ ChatColor.DARK_RED +				"R"
										+ ChatColor.DARK_GREEN + 			"I"
										+ ChatColor.DARK_RED + 				"S"
										+ ChatColor.DARK_GREEN +  			"T"
										+ ChatColor.DARK_RED + 				"M"
										+ ChatColor.DARK_GREEN + 			"A"
										+ ChatColor.DARK_RED +  			"S"; //What does that spell? CHRISTMAS!
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
		totalGiftPool[0] = 0;	//Reset the total gift pool
		totalGiftPool[1] = 0;	//If not, more rewards than
		totalGiftPool[2] = 0;	//exists are attempted to be
		totalGiftPool[3] = 0;	//picked from, resulting in
		totalGiftPool[4] = 0;	//fun NPEs for the whole family.
		
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
	
	private String translateChristmasPrefix(String string)
	{
		String fixedString;
		Pattern ChristmasPattern = Pattern.compile("[@]");
		fixedString = ChristmasPattern.matcher(string).replaceAll(ChristmasPrefix);
		return fixedString;
	}
	
	private String translateColours(String string)
	{
		String fixedString;
		Pattern chatColorPattern = Pattern.compile("(?i)&([0-9A-Fa-fl-oL-O])"); // Credit to t3hk0d3 in ChatManager(With slight edits)
		fixedString = chatColorPattern.matcher(string).replaceAll("\u00A7$1"); // And here too
		return fixedString;
	}
	
	private String translatePlayerName(CommandSender sender, String string)
	{
		String fixedString;
		Pattern ChristmasPattern = Pattern.compile("[%]");
		fixedString = ChristmasPattern.matcher(string).replaceAll(sender.getName());
		return fixedString;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) 
	{
		if (cmd.getName().equalsIgnoreCase("present"))
		{
			PermissionUser user = PermissionsEx.getUser(sender.getName());
			if(user.has("blivutils.present.christmas.done"))
			{
				b.printError(sender, "You can only open one Christmas Present!");
				return true;
			}
			else
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
						// Pools:
						// Pool One: Weapon (Enchantments equivalent to material)
						// Pool Two: Tool (Enchantments equivalent to material)
						// Pool Three: Armour (Enchantments equivalent to material)
						// Pool Four: ??? 
						// Pool Five: ???
						
						//Pool One ---------------
						try
						{
							ChatColor hyphen = ChatColor.DARK_RED;
							for(int i = 0;i < numberGifts;i++)
							{
									//Get the chance, round it down, and roll a number.
									//Example:
									// - Chance: 0.6
									// 0.6 * 100 = 60
									// Roll, if number between 0 and 60, prize is won.
									// If roll is 70-100, prize is not won.
									//    - Re-roll another gift.
									boolean won = false;
									int chance = 100, reroll = 0, numRolls = 0;
									do
									{
										rolledGift[i] = rewardsTable[i][rand.nextInt(totalGiftPool[i])];
										chance = (int) Math.ceil(( rolledGift[i].getChance() * 100));
										if(chance == 0)
										{
											//Chance is wrong.
											//Infinite loop will occur, just kill it.
											break;
										}
										reroll = rand.nextInt(100);
										//log.info("Rolled a " + reroll + " | and needed a " + chance + " or lower.");
										if(reroll < chance)
										{
											won = true;
											log.info("Reward: " + rolledGift[i].getName() + " was won! Congratulations!");
										}
										else
										{
											//log.info("Reward: " + rolledGift[i].getName() + " was not won.");
										}
										if(numRolls >= 20)
										{
											//Too many rolls.
											//TODO: Handle this situation with a special/default gift.
											//Default to the first element?
											rolledGift[i] = rewardsTable[i][0];
											won = true;
										}
										numRolls++;
									} while((!won));
									
									//if(rand.nextInt((int) Math.floor(10 - (rolledGift[i].getChance() * 10))) == 0)
									log.info(rolledGift[i].getName());
									if(hyphen == ChatColor.DARK_RED)
									{
										hyphen = ChatColor.DARK_GREEN;
									}
									else if(hyphen == ChatColor.DARK_GREEN)
									{
										hyphen = ChatColor.DARK_RED;
									}
									rewardString += hyphen + " - " + ChatColor.GOLD + rolledGift[i].getName() + "\n";
							}
							rewardString = translateColours(rewardString.substring(0, (rewardString.length() - 1)));
							
							rewardString = translateChristmasPrefix(rewardString);
							
							sender.sendMessage(ChatColor.YELLOW + "Congratulations!" + ChatColor.GREEN + " You've won:\n" + rewardString);
							b.logReward(rewardString);
							b.logReward("------------------------------------------\n" + sender.getName() + "has won:");
							
							user.addPermission("blivutils.present.christmas.done"); //Player can no longer type /present open
						}
						catch(NullPointerException e)
						{
							e.printStackTrace();
							b.logtoFile("Player " + sender.getName() + " had problems with their present.");
							b.printError(sender, "Oops! Your present had trouble opening. Send a /modreq for the Musketeers.");
						}
						
						giveRewards(sender, rolledGift);
						
						return true;
					}
					else if(args[0].equals("reload"))
					{
						if(sender.hasPermission("blivutils.rewards.admin"))
						{
							cfg.loadRewards();
							loadRewards();
							sender.sendMessage(ChatColor.GREEN + "Loaded " + ((totalGiftPool[0] + totalGiftPool[1] + totalGiftPool[2] + totalGiftPool[3] + totalGiftPool[4]) + " rewards"));
						}
					}
					return true;
				}
				return true;
			}
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
				//p.sendMessage("Reward given!");
				log.info("Command: '" + action + "'");
			}
			else	// Reward is an item
			{
				Random rand = new Random(System.currentTimeMillis()); //Define random here, so the rewards are varied, and aren't done by the same seed.
				Material material = Material.AIR;
				Material item = material.getMaterial(Integer.parseInt(rolledGift[i].getAction()));
				ItemStack reward = new ItemStack(item);
				
				if(rolledGift[i].getName() != null)
				{
					ItemMeta meta = reward.getItemMeta();
					
					String lore = translatePlayerName(sender, translateChristmasPrefix(translateColours(rolledGift[i].getName())));
					
					meta.setDisplayName(lore);
					reward.setItemMeta(meta);
				}
				
				if((rolledGift[i].getLore() != null) && (rolledGift[i].getLore() != "LEFT_BLANK"))	//Lore is not empty
				{
					List<String> lore = new ArrayList<>();
					lore.add(translatePlayerName(sender, translateColours(rolledGift[i].getLore())));
					ItemMeta meta = reward.getItemMeta();
					meta.setLore(lore);
					reward.setItemMeta(meta);
				}
				
				if(rolledGift[i].getEnchants() != null) //Enchantment listings are not empty
				{
					for(int ii = 0;ii < rolledGift[i].getEnchants().length;ii++)
					{
						int[] enchants = rolledGift[i].getEnchants()[ii];
						//enchants[0] = Enchantment ID
						//enchants[1] = Base Enchantment Level
						//enchants[2] = Enchantment Variation (In Levels)
						
						//Level randomisation
						//log.info("Enchantment #" + ii);
						//log.info("Enchantment: " + enchants[0] + ":" + enchants[1] + ":" + enchants[2]);
						int level = enchants[1], variation = 0;
						boolean valid = false;
						if(enchants[0] >= 0) //If the Enchantment ID is above 0 (Valid)
						{
							if(enchants[2] > 0) //If the variation is above 0, apply the variation possibility
							{
								//Final level = Base + (RANDOM(Variation))
								variation = rand.nextInt(enchants[2]);
								level = enchants[1] + (variation);
								//log.info("Variation: " + variation);
							}
							
							//log.info("Enchantment level: " + level);
							if(level <= 0) //If the resulting level (or base level for that matter), is below or equal to 0, do not apply.
							{
								valid = false; //If the enchantment level is below 0, or 0 itself, the enchantment cannot exist: Dont apply it.
							}
							else
							{
								valid = true;
							}
						}
						/*else
						{
							log.info(enchants[0] + " is not above 0. Skipping...");
						}*/
						
						if(valid == true)
						{
							//log.info("Enchantment " + enchants[0] + " with level " + level + " is valid.");
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
				/*else
				{
					//log.info("Enchantment Space Empty/Null!");
				}*/
				
				
				p.getInventory().addItem(reward);
				//p.sendMessage("Reward given!");
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
