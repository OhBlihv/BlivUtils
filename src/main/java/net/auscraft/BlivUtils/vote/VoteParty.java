package net.auscraft.BlivUtils.vote;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.config.ConfigAccessor;
import net.auscraft.BlivUtils.utils.Utilities;

public class VoteParty 
{

	//TODO: Populate
	//Currently Barebones
	
	private Utilities util;
	private ConfigAccessor cfg;
	
	private int taskID;
	
	private int counter;
	private int trigger;
	private boolean party = false;
	
	public VoteParty(BlivUtils instance)
	{
		util = instance.getUtil();
		cfg = instance.getCfg();
		counter = 0;
		trigger = cfg.getInt("options.vote.trigger");
		
		//TODO:
		//Schedule a method that checks if the vote count is above a certain amount, trigger the party.
	}
	
	public void increment()
	{
		counter++;
		if(counter >= trigger)
		{
			party = true;
			util.broadcastPlain("Vote Party Active!");
		}
	}
	
	private void cancelParty()
	{
		Bukkit.getServer().getScheduler().cancelTask(taskID);
	}
	
	public void votePartyDecrement()
	{
		final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		final String unit = cfg.getString("options.vote.check.unit");
		int looptime = util.getConversion(unit, "options.vote.check.time");
		taskID = scheduler.scheduleSyncRepeatingTask(util.getInstance(), new Runnable()
		{
			public void run()
			{
				util.broadcastPlain(counter + " " + unit + " left of the vote party!");
				counter--;
				if(counter == 0)
				{
					cancelParty();
				}
			}
		}, 0L, looptime);
	}
}
