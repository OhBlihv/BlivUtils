package net.auscraft.BlivUtils.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener
{
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		if(player.hasPermission("blivutils.death.keepinv"))
		{
			event.setKeepInventory(true);
		}
		else
		{
			event.setKeepInventory(false);
		}
		if(player.hasPermission("blivutils.death.keepxp"))
		{
			event.setKeepLevel(true);
		}
		else
		{
			event.setKeepLevel(false);
		}
	}
}
