package net.auscraft.BlivUtils.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HubListener implements Listener
{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if(Bukkit.getServer().getServerId().equals("Hub"))
		{
			Player p = event.getPlayer();
			PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2);
			PotionEffect jump = new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 2);
			p.addPotionEffect(speed);
			p.addPotionEffect(jump);
		}
	}
	
}
