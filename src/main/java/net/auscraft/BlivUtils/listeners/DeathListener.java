package net.auscraft.BlivUtils.listeners;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener
{
	
	private Utilities util;
	
	public DeathListener(BlivUtils instance)
	{
		util = instance.getUtil();
		util.logDebug("Keep XP/Inventory on Death Enabled!");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		//Keep XP and Items on Death Checking ------------------
		if(player.hasPermission("blivutils.death.keepinv"))
		{
			//util.logDebug("Has permission to keep inventory on death");
			event.setKeepInventory(true);
		}
		else
		{
			//util.logDebug("DOESNT have permission to keep inventory on death");
			event.setKeepInventory(false);
		}
		if(player.hasPermission("blivutils.death.keepxp"))
		{
			//util.logDebug("Has permission to keep xp on death");
			event.setKeepLevel(true);
		}
		else
		{
			//util.logDebug("DOESNT have permission to keep xp on death");
			event.setKeepLevel(false);
		}
	}
}
