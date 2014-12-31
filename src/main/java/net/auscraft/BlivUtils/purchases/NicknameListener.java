package net.auscraft.BlivUtils.purchases;

import java.util.HashMap;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class NicknameListener implements Listener
{
	
	private HashMap<Player, Integer> nick;
	private Utilities util;
	
	public NicknameListener(BlivUtils instance)
	{
		util = instance.getUtil();
		nick = util.getMap();
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent event)
	{
		Player p = event.getPlayer();
		if(!p.hasPlayedBefore())
		{
			nick.put(p, 1);
			util.logInfo("Player " + p.getName() + " successfully added to One-Time Nickname map.");
		}
		else
		{
			util.logInfo("Player " + p.getName() + " has played before. Will not be put in the map.");
		}
	}
	
}
