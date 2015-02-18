package net.auscraft.BlivUtils.listeners;

import net.auscraft.BlivUtils.BlivUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class XPListener  implements Listener
{
	//private Utilities util;
	
	public XPListener(BlivUtils instance)
	{
		//util = instance.getUtil();
	}
	
	@EventHandler
	public void onXpPickup(PlayerExpChangeEvent event)
    {
        int amount = event.getAmount();
        if(amount <= 0.0)
        {
            return;
        }
        Player p = event.getPlayer();
        double multiplier = 1.0;
        if(p.hasPermission("blivutils.xp.2"))
        {
        	multiplier = 2.0;
        }
        //util.logDebug("Original XP: " + amount + " | Multiplied by " + multiplier + " = " + ((int) Math.floor(event.getAmount() * multiplier)));
        event.setAmount((int) Math.floor(event.getAmount() * multiplier));
    }
}
