package net.auscraft.BlivUtils.config;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.rewards.RewardContainer;

public class ConfigAccessor {
	
	private FileConfiguration config;
	private BlivUtils plugin;
	private String user, pass, url; //MySQL Setup Variables
	private RewardContainer[][] rewardsTable;
	private Logger log;
	
	public ConfigAccessor(BlivUtils instance)
	{
		plugin = instance;
		config = plugin.getConfig();
		log = plugin.getLogger();
	}

	public RewardContainer[][] loadRewards()
	{
		
		int ii = 0; //Counts number of rewards in a pool.
		int iii = 0; //Counts total number of rewards
		
		//Reset the rewards table, as this function will be used to reload the table too.
		RewardContainer[][] rewardsTable = new RewardContainer[5][50]; //Sorry for the hardcoded numbers. We have 32GB of RAM, I don't really care about saving space right now.
		
		//Retrieve the list of rewards for each category.
		//Only pool one exists right now, so just do pool 1. :)
		int rewardPoolNum = getInt("options.rewards.count");
		log.info("Pool Count: " + rewardPoolNum);
		
		try
		{
		//Loop through each pool
		for(int j = 0;j < rewardPoolNum;j++)
		{
			ii = 0;
			List<String> pool = getStringList("options.rewards.pool" + (j+1));
			log.info("Reading Pool: " + (j+1));
			for(String i : pool)
			{
				log.info("Entry Number: " + ii);
				String name = "", itemid = "", lore = "";
				String enchantsList = "";
				int[][] enchants = new int[1][3];; //5 Enchantments per item, 3 Available Enchantment Variables
				//Initialize the array to invalid values, so if they are not set it wont error.
				/*for(int encInit = 0;encInit < 5;encInit++)
				{
					for(int encInitIn = 0;encInitIn < 3;encInitIn++)
					{
						enchants[encInit][encInitIn] = -1;
					}
				}*/
				
				try
				{
					String[] rewards = i.split(",");
					
					if(rewards.length == 4) //4 Variables
					{
						name = rewards[0];
						itemid = rewards[1];
					    enchantsList = rewards[2];
					    
					    
					    if(!enchantsList.contains("-"))
					    {
					    	String[] encSets = enchantsList.split("[|]");
					    	enchants = new int[encSets.length][3];
					    	log.info("Reading Enchantments...");
					    	//log.info("encSets[0] = " + encSets[0]);
					    	for(int encAmount = 0;encAmount < encSets.length;encAmount++)
					    	{
					    		log.info("encSets[" + encAmount + "] = " + encSets[encAmount]);
					    	}
					    	int encCount = 0;
					    	//One set of enchantment variables.
					    	log.info("encSets.length = " + encSets.length);
					    	String[] encVars;
					    	for(int encGroup = 0;encGroup < (encSets.length / 3);encGroup++)
					    	{
					    		encVars = encSets[encGroup].split("[:]");
					    		log.info("encVars[0] = " + encVars[0]);
					    		log.info("encVars.length = " + encVars.length);
					    		enchants[encGroup][0] = Integer.parseInt(encVars[encCount++]);
					    		enchants[encGroup][1] = Integer.parseInt(encVars[encCount++]);
					    		enchants[encGroup][2] = Integer.parseInt(encVars[encCount++]);
					    		log.info(enchants[encGroup][0] + ":" + enchants[encGroup][1] + ":" + enchants[encGroup][2]);
					    	}
					    }
					    
					    if(!rewards[3].contains("-"))
					    {
						    lore = rewards[3];
					    }
					    else
					    {
					    	lore = "LEFT_BLANK";
					    }
					    
					}
					else //2 variables are required to input into the table. Might as well cut them off and make them fix it.
					{
						log.warning("[2] Syntax error in rewards config. Disabling rewards features.");
						rewardsTable = null;
						return null;
					}
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					log.warning("[3] Syntax error in rewards config. Disabling rewards features.");
					rewardsTable = null;
					e.printStackTrace();
					return null;
				}
			    
				if(enchants[0][0] != -1)
				{
					for(int encLoop = 0;encLoop < 5;encLoop++)
					{
						enchantsList += enchants[encLoop][0] + ":" + enchants[encLoop][1] + ":" + enchants[encLoop][2] + " ";
					}
				}
				else
				{
					enchantsList = "NONE_GIVEN";
				}
				
				log.info("Entry " + ii + ": " + name + " " + itemid + " " + enchantsList + " " + lore);
				RewardContainer reward = new RewardContainer(name, itemid, enchants, lore);
				rewardsTable[j][ii] = reward;
				
				ii++;
			    
			    //Reset the strings, or wrong values may be passed back.
			    name = "";
			    itemid = "";
			    enchantsList = "";
			    enchants[0][0] = -1;
			    lore = "";
	
			}
			iii += ii;
			
		}
		
		if(iii > 0)
		{
			log.info("Loaded " + iii + " rewards.");
		}
		else
		{
			log.info("No Rewards Loaded. Config invalid, or are there no rewards?");
		}
		}
		catch(Exception e)
		{
			//fallback to let the plugin load properly.
		}
		//int poolloop = 0, rewardloop = 0;
		/*try
		{
			for(poolloop = 0;poolloop < rewardsTable.length;poolloop++)
			{
				for(rewardloop = 0;rewardloop < rewardsTable[poolloop].length;rewardloop++)
				{
					try
					{
						log.info(rewardsTable[poolloop][rewardloop].getName());
					}
					catch(NullPointerException e)
					{
						rewardloop = rewardsTable[poolloop].length; //No more rewards in this pool, leave the loop.
					}
				}
				
			}
		}
		catch(NullPointerException e)
		{
			log.severe("Error accessing reward at [" + poolloop + "][" + rewardloop + "]");
		}*/
		return rewardsTable;
	}
	
	public RewardContainer[][] getRewards()
	{
		return rewardsTable;
	}
	
	public String[] getSQLVars()
	{
		String[] vars = {user, pass, url};
		return vars;
	}
	
	// Basic Configuration Alteration/Accessing
	
	

	public int getInt(String path)
	{
		int value = this.config.getInt(path);
		return value;
	}

	public String getString(String path)
	{
		String value = this.config.getString(path);
		return value;
	}

	public boolean getBoolean(String path)
	{
		boolean value = this.config.getBoolean(path);
		return value;
	}
	
	public List<String> getStringList(String path)
	{
		List<String> values = this.config.getStringList(path);
		return values;
	}

}