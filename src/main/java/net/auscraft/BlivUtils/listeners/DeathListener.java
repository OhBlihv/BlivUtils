package net.auscraft.BlivUtils.listeners;

import java.util.Random;
import java.util.regex.Pattern;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.minecraftdimensions.bungeesuitechat.managers.PlayerManager;

public class DeathListener implements Listener
{
	
	private Utilities util;
	
	public DeathListener(BlivUtils instance)
	{
		util = instance.getUtil();
		util.logDebug("Death Messages and Keep XP/Inventory on Death Enabled!");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		//Keep XP and Items on Death Checking ------------------
		if(player.hasPermission("blivutils.death.keepinv"))
		{
			util.logDebug("Has permission to keep inventory on death");
			event.setKeepInventory(true);
		}
		else
		{
			util.logDebug("DOESNT have permission to keep inventory on death");
			event.setKeepInventory(false);
		}
		if(player.hasPermission("blivutils.death.keepxp"))
		{
			util.logDebug("Has permission to keep xp on death");
			event.setKeepLevel(true);
		}
		else
		{
			util.logDebug("DOESNT have permission to keep xp on death");
			event.setKeepLevel(false);
		}
		
		doDeathChecking(event);
		
	}
	
	/*
	 * 	<| DEATH MESSAGES BEGIN|>
	 *  This will be super WIP until its actually done properly
	 *  Grab a list (not read in from file because I don't need to)
	 *  and roll a number from 1 -> maxLines from that mob message
	 *  print message
	 *  FIN~
	 *  
	 */
	
	public void doDeathChecking(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		//Cancel the vanilla death messages
		event.setDeathMessage(null);
		
		String target = "";
		Entity targetEntity = event.getEntity();
		boolean targetPlayer = false;
		
		//Player was killed
		if(targetEntity instanceof Player)
		{
			target = ((Player)targetEntity).getName();
			targetPlayer = true;
		}
		else //
		{
			util.logDebug("DEBUG: Entity killed was not a player");
			return;
		}
		
		//Find out the killer
		Entity killerEntity = null;
		String killerName = "";
		
		//Identifying information
		ItemStack killerWeapon = null;
		boolean killerPlayer = false;
		
		EntityDamageEvent lastDamage = player.getLastDamageCause();
		if(lastDamage instanceof EntityDamageByEntityEvent)
		{
			Entity damager = ((EntityDamageByEntityEvent)lastDamage).getDamager();
			//Killed by Player
			if(damager instanceof Player)
			{
				 killerPlayer = true;
				 killerEntity = (Player)damager;
	             killerName = ((Player)killerEntity).getDisplayName();
	             killerWeapon = ((HumanEntity) killerEntity).getItemInHand();
			}
			//If a Projectile was the source of death
			else if(damager instanceof Projectile)
			{
				Projectile proj = (Projectile)damager;
				if(proj.getShooter() instanceof Player)
				{
					killerPlayer = true;
					killerEntity = (Player)proj.getShooter();
					killerName = killerEntity.getName();
                    killerWeapon = ((HumanEntity) killerEntity).getItemInHand();
				}
				if(proj.getShooter() != null)
				{
                    killerName = proj.getShooter().getClass().getName();
				}
				else
				{
					killerName = "unknown";
				}
			}
			else if((damager instanceof Tameable) && ((Tameable)damager).isTamed())
			{
				AnimalTamer tamer = ((Tameable)damager).getOwner();
				if(tamer != null)
				{
					if(tamer instanceof Player)
					{
						killerPlayer = true;
						killerEntity = (Player)tamer;
					}
					killerName = tamer.getName();
				}
				else
				{
					killerName = damager.getType().getName();
				}
			}
			else
			{
				killerEntity = damager;
				killerName = damager.getType().getName();
			}
		}
		else if((lastDamage == null) || lastDamage.getCause() == null)
		{
			killerName = "unknown";
		}
		
		//If death was the result of another player
		/*if(targetPlayer && killerPlayer)
		{
			//TODO: Set up message printing method
			/*
			 * This method includes:
			 * targetPlayer Player who was killed
			 * killerName Name/Nickname of the killer
			 * killerWeapon Weapon the killer used to kill the targetPlayer
			 * isMelee Was the kill Melee or Ranged (Will change the output)
			 *
			String targetName = ((Player)targetEntity).getDisplayName();
			if(PlayerManager.getPlayer(targetName).hasNickname())
			{
				targetName = PlayerManager.getPlayer(targetName).getNickname();
			}
			if(PlayerManager.getPlayer(killerName).hasNickname())
			{
				killerName = PlayerManager.getPlayer(killerName).getNickname();
			}
			
			doDeathMessage(targetName, killerName, killerWeapon);
		}*/
		//Else, if it was just a player death (as the result of another entity/world death)
		/*else*/ if(targetPlayer)
		{
			if(PlayerManager.getPlayer(target).hasNickname())
			{
				target = PlayerManager.getPlayer(target).getNickname();
			}
			try
			{
				//TODO: remove this and do it above (You'll find where it is -its the last else)
				killerName = killerEntity.getType().getName();
				if(killerName == null)
				{
					throw new NullPointerException();
				}
			}
			catch(NullPointerException e)
			{
				try
				{
					killerName = ((EntityDamageEvent)lastDamage).getCause().name().toString();
					if(killerName == null)
					{
						throw new NullPointerException();
					}
				}
				catch(NullPointerException e2)
				{
					util.logDebug("No Killer Found! Defaulting to unknown");
					killerName = "unknown";
				}
			}
			util.logDebug("Killer = " + killerName);
			
			String deathMessage = doDeathMessage(target, killerName, killerWeapon);
			util.logDebug("Death Message: " + deathMessage);
			Bukkit.broadcastMessage(deathMessage);
			
		}
        
	}
	
	//Grab the info from above, grab a random death message from the message class, and regex it together
	public String doDeathMessage(String targetPlayer, String killerName, ItemStack killerWeapon)
	{
		Messages msgClass = new Messages();
		String msg = msgClass.getMessage(killerName);
		
		//Fill in the variables
		//Replace the targetName
		Pattern targetPattern = Pattern.compile("%d");
		msg = targetPattern.matcher(msg).replaceAll(targetPlayer);
		
		//Replace the killerName (for PvP)
		Pattern killerPattern = Pattern.compile("%k");
		msg = killerPattern.matcher(msg).replaceAll(killerName);

		//Replace the killerName (for PvP)
		//Pattern weaponPattern = Pattern.compile("%w");
		//msg = weaponPattern.matcher(msg).replaceAll(killerWeapon.getItemMeta().getDisplayName());
		
		//TODO: PvP Kills/Deaths
		//		Separated by Melee/Ranged (Is in the config, so will be here)
		
		msg = util.translateColours(msg);
		
		return msg;
	}
	
	private class Messages
	{
		//PVP
		private final String[] pvp = {
			"&6%d&f murdered %k",
		};
		
		//UNKNOWN
		private final String[] unknown = {
			"&6%d&f has died of unknown causes",
			"&6%d&f isn't causing NPEs anymore",
			"&6%d&f died from explosive diarrhoea",
			"&6%d&f was killed by invisible ninja assassins",
			"&fR.I.P. in peace &6%d&f...",
			"Aliens abducted &6%d&f...",
			"&6%d&f found their final destination",
			"&6%d&f was split in two",
			"&6%d&f divided by 0",
			"A &2Black Hole &fconsumed &6%d&f",
			"&6%d&f imploded"
		};
		
		//ENVIRONMENT
		private final String[] fire = {
		    "&6%d&f was fried to a crisp",
		    "&6%d&f burned to death",
		    "&6%d&f was overcooked",
		    "&6%d&f played with matches...",
		};
		
		private final String[] fire_tick = {
		    "&6%d&f tried to extinguish the flames, but failed",
		    "&6%d&f was burnt to a crisp",
		    "&6%d&f forgot their 'Anti-get-set-on-fire cream'",
		    "&6%d&f became a human torch"
		};
		
		private final String[] lava = {
		    "&6%d&f was no match for a pool of lava",
		    "&6%d&f melted their diamonds, and themselves",
		    "&6%d&f became molten human"
		};
		
		private final String[] fall = {
		    "&6%d&f found out what the ground feels like",
		    "&6%d&f forgot their parachute",
		    "&6%d&f took a cartoon step into mid-air",
		    "&6%d&f shot straight into the ground",
		    "&6%d&f missed a step",
		    "&6%d&f rode the gravel straight back to earth",
		    "&6%d&f forgot their long-fall boots",
		    "&6%d&f needs to practice free-running",
		    "&6%d&f needs practice at rock-climbing",
		};
		
		private final String[] contact = {
		    "&6%d&f should learn to not jump on a cactus",
		    "&6%d&f hugged a cactus",
		    "&6%d&f is still removing prickles"
		};
		
		private final String[] block_explosion = {
		    "&6%d&f has been shredded by TNT",
		    "&6%d&f cut the &9Blue Wire"
		};
		
		private final String[] primedtnt = {
		    "&6%d&f has been shredded by TNT",
		    "&6%d&f cut the &9Blue Wire"
		};
		
		private final String[] suffocation = {
		    "&6%d&f ran out of breath",
		    "&6%d&f fell victim to a cave in",
		    "&6%d&f tried to break in dirt"
		};
		
		private final String[] thorns = {
			    "&6%d&f was killed by their own blade",
			    "The last thing &6%d&f felt was thorns"
			};
		
		private final String[] starvation = {
		    "&6%d&f should learn to eat",
		    "&6%d&f didn't learn Eating 101",
		    "&6%d&f couldn't find a pig in time"
		};
		
		private final String[] lightning = {
		    "&6%d&f was struck down by a bolt of lightning",
		    "&6%d&f was smited by the thundergods"
		};
		
		private final String[] suicide = {
		    "hari kari suited &6%d&f",
		    "&6%d&f chose the ignoble way out",
		    "&6%d&f ran into a neck-high piano wire"
		};
		
		private final String[] drowning = {
		    "&6%d&f needs to learn to swim",
		    "Dog paddling wasn't enough for &6%d&f",
		    "&2Water&f was the end of poor &6%d&f",
		    "&6%d&f forgot their scuba gear!",
		    "&6%d&f came in contact with DHMO",
		    "&6%d&f inhaled too much water",
		    "&6%d&f slacked off at swimming lessons"
		};
		
		private final String[] voidDeath = {
		    "The &8void&f has claimed &6%d&f",
		    "&6%d&f is now forever falling..."
		};
		
		private final String[] creeper = {
		    "A &2creeper&f blast vaporized &6%d&f",
		    "&6%d&f tried to befriend the explosive beast",
		    "&2Creepers &fare now an enemy of &6%d&f",
		    "&6%d&f was denied a hug",
		    "&6%d&f was blown away by a creeper"
		};
		
		private final String[] zombie = {
		    "&6%d&f has left this world as a &2zombie",
		    "&2Zombies&f have devoured &6%d&f",
		    "The &2zombie&f horde have claimed &6%d&f",
		    "&6%d&f became a cannibal",
		    "&6%d&f became the Walking Dead"
		};
		
		private final String[] skeleton = {
		    "&6%d&f was pierced by a &7skeleton&f arrow",
		    "&6%d&f was no match for the &7skeleton&f",
		    "&6%d&f found out the accuracy of the undead",
		    "&6%d&f was hit by an arrow in the dark",
		    "&6%d&f delved into the Skeleton Kings lair",
		    "The skeleton was too spooky for &6%d"
		};
		
		private final String[] spider = {
		    "Spiders have drained &6%d&f",
		    "&6%d&f had a reason to fear spiders",
		    "&6%d&f met their nightmare. Spiders."
		};
		
		private final String[] cavespider = {
		    "Cavespiders have swarmed &6%d&f",
		    "&6%d&f couldn't find the antidote"
		};
		
		private final String[] enderdragon = {
		    "The &8EnderDragon&f has annihilated &6%d&f",
		    "&6%d&f isn't the &6Dragon-Born..."
		};
		
		private final String[] blaze = {
		    "The &4blaze&f has crisped &6%d&f",
		    "&6%d&f can't dodge fireballs"
		};
		
		private final String[] silverfish = {
		    "&6%d&f was overwhelmed by silverfish",
		    "&6%d&f forgot to spring clean"
		};
		
		private final String[] enderman = {
		    "&8Endermen&f have taken &6%d&f to the End",
		    "&6%d&f looked into the eyes of the &8Enderman",
		    "&6%d&f lost the stare off with an &8Enderman",
		    "&6%d&f enraged the purple beast"
		};
		
		private final String[] snowman = {
		    "&6%d&f was made into a snowcone"
		};
		
		private final String[] ocelot = {
		    "The cute ocelot showed its power to &6%d&f",
		    "&6%d&f discovered that ocelots have fangs"
		};
		
		private final String[] fallingsand = {
		    "&6%d&f should check for falling pianos.",
		    "&6%d&f was crushed. Oh well."
		};
		
		private final String[] wolf = {
		    "&6%d&f was hunted down by the pack",
		    "&6%d&f was devored by wolves"
		};
		
		private final String[] giant = {
		    "&6%d&f was smashed by a Giant",
		    "A giant has flattened &6%d&f"
		};
		
		private final String[] slime = {
		    "&6%d&f was liquified by a &aSlime",
		    "&6%d&f was enveloped by &aSlime",
		    "&6%d&f was turned to jelly by a &aSlime"
		};
		
		private final String[] ghast = {
		    "&6%d&f was burned by ghasts"
		};
		
		private final String[] pigzombie = {
		    "The PigZombie horde has claimed &6%d&f",
		    "&6%d&f died to a freak hybrid zombie-pig!",
		    "&6%d&f regrets punching that PigZombie"
		};
		
		private final String[] magmaslime = {
		    "&6%d&f has been MagmaSlimed!",
		    "&6%d&f melted inside the MagmaSlime"
		};
		
		private final String[] magic = {
			"&6%d&f messed with the &owrong &r&6WITCH&r!",
			"&6%d&f was mixed up in a &6Witch&f's cauldron"
		};
		
		private final String[] projectile = {
			"&6%d&f was killed from afar",
			"&6%d&f was sniped down",
		};
		
		private final String[] endermite = {
			"&6%d&f was nibbled to death by &5enderlings",
			"&6%d&f trod on an angry purple bug"
		};
		
		private final String[] guardian = {
			"&6%d&f failed to empty the water temple",
			"&6%d&f was burnt by a &5guardians &Flaser"
		};
		
		private final String[] rabbit = {
			"&6%d&f doesn't want a pet rabbit anymore",
			"&6%d&f was gnawed to death by a cute rabbit"
		};
		
		public Messages()
		{
			//Nothing here
		}
		
		
		//Randomise and return a death message for a given entity
		public String getMessage(String entity)
		{
			Random rand = new Random(System.currentTimeMillis());
			try
			{
				switch(entity)
				{
					//PLAYER KILLED
					//case "-1": return player[rand.nextInt(player.length 1)];
				
					//MOBS
					case "Creeper": return creeper[rand.nextInt(creeper.length - 1)];
					case "Zombie": return zombie[rand.nextInt(zombie.length - 1)];
					case "Skeleton": return skeleton[rand.nextInt(skeleton.length - 1)];
					case "Spider": return spider[rand.nextInt(spider.length - 1)];
					case "CaveSpider": return cavespider[rand.nextInt(cavespider.length - 1)];
					case "EnderDragon": return enderdragon[rand.nextInt(enderdragon.length - 1)];
					case "Blaze": return blaze[rand.nextInt(blaze.length - 1)];
					case "Silverfish": return silverfish[rand.nextInt(silverfish.length - 1)];
					case "Enderman": return enderman[rand.nextInt(enderman.length - 1)];
					case "Snowman": return snowman[rand.nextInt(snowman.length - 1)];
					case "Ozelot": return ocelot[rand.nextInt(ocelot.length - 1)];
					case "Wolf": return wolf[rand.nextInt(wolf.length - 1)];
					case "Giant": return giant[rand.nextInt(giant.length - 1)];
					case "Slime": return slime[rand.nextInt(slime.length - 1)];
					case "Ghast": return ghast[rand.nextInt(ghast.length - 1)];
					case "PigZombie": return pigzombie[rand.nextInt(pigzombie.length - 1)];
					case "LavaSlime": return magmaslime[rand.nextInt(magmaslime.length - 1)];
					case "Guardian": return guardian[rand.nextInt(guardian.length - 1)];
					case "Rabbit": return rabbit[rand.nextInt(rabbit.length - 1)];
					case "Endermite": return endermite[rand.nextInt(endermite.length - 1)];
					case "PROJECTILE": return projectile[rand.nextInt(projectile.length - 1)];
					
					//ENVIRONMENT
					case "MAGIC": return magic[rand.nextInt(magic.length - 1)];
					case "FallingSand": return fallingsand[rand.nextInt(fallingsand.length - 1)];
					case "SUICIDE": return suicide[rand.nextInt(suicide.length - 1)];
					case "VOID": return voidDeath[rand.nextInt(voidDeath.length - 1)];
					case "LAVA": return lava[rand.nextInt(lava.length - 1)];
					case "BLOCK_EXPLOSION": return block_explosion[rand.nextInt(block_explosion.length - 1)];
					case "PRIMEDTNT": return primedtnt[rand.nextInt(primedtnt.length - 1)];
					case "CONTACT": return contact[rand.nextInt(contact.length - 1)];
					case "SUFFOCATION": return suffocation[rand.nextInt(suffocation.length - 1)];
					case "STARVATION": return starvation[rand.nextInt(starvation.length - 1)];
					case "DROWNING": return drowning[rand.nextInt(drowning.length - 1)];
					case "FIRE_TICK": return fire_tick[rand.nextInt(fire_tick.length - 1)];
					case "FIRE": return fire[rand.nextInt(fire.length - 1)];
					case "LIGHTNING": return lightning[rand.nextInt(lightning.length - 1)];
					case "FALL": return fall[rand.nextInt(fall.length - 1)];
					case "THORNS": return thorns[rand.nextInt(thorns.length - 1)];
					
					default: return unknown[rand.nextInt(unknown.length - 1)];
				}
			}
			catch(NullPointerException e)
			{
				return unknown[rand.nextInt(unknown.length - 1)];
			}
		}
	}
	
	
	
	
	
	
	
	
}
