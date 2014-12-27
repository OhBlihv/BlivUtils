package net.auscraft.BlivUtils.config;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.rewards.RewardContainer;
import net.auscraft.BlivUtils.utils.Utilities;

public class ConfigAccessor {
	
	private FileConfiguration config;
	private String user, pass, url; //MySQL Setup Variables
	private RewardContainer[][] rewardsTable;
	private Utilities util;
	private BlivUtils b;
	
	public ConfigAccessor(BlivUtils instance)
	{
		util = instance.getUtil();
		config = instance.getConfig();
	}

	public RewardContainer[][] loadRewards()
	{
		reloadConfig();
		
		int ii = 0; //Counts number of rewards in a pool.
		int iii = 0; //Counts total number of rewards
		
		//Reset the rewards table, as this function will be used to reload the table too.
		RewardContainer[][] rewardsTable = new RewardContainer[5][50]; //Sorry for the hardcoded numbers. We have 32GB of RAM, I don't really care about saving space right now.
		
		//Retrieve the list of rewards for each category.
		//Only pool one exists right now, so just do pool 1. :)
		int rewardPoolNum = getInt("options.rewards.count");
		util.logInfo("Pool Count: " + rewardPoolNum);
		
		try
		{
		//Loop through each pool
		for(int j = 0;j < rewardPoolNum;j++)
		{
			ii = 0;
			List<String> pool = getStringList("options.rewards.pool" + (j+1));
			//log.info("Reading Pool: " + (j+1));
			for(String i : pool)
			{
				//log.info("Entry Number: " + ii);
				String name = "", itemid = "", lore = "";
				String enchantsList = "";
				double chance = 1.0;
				int[][] enchants = {{-1,-1,-1},{-1,-1,-1},{-1,-1,-1},{-1,-1,-1},{-1,-1,-1}}; //5 Enchantments per item, 3 Available Enchantment Variables
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
					String[] rewards = i.split("[,]");
					
					if(rewards.length == 5) //4 Variables
					{
						chance = Double.parseDouble(rewards[0]);
						name = rewards[1];
						itemid = rewards[2];
					    enchantsList = rewards[3];
					    
					    
					    if(!enchantsList.contains("-"))
					    {
					    	String[] encSets = enchantsList.split("[|]");
					    	//log.info("encSets.length = " + encSets.length);
					    	//log.info("Reading Enchantments...");
					    	/*for(int encAmount = 0;encAmount < encSets.length;encAmount++)
					    	{
					    		log.info("encSets[" + encAmount + "] = " + encSets[encAmount]);
					    	}*/
					    	int encCount = 0;
					    	//One set of enchantment variables.
					    	//log.info("encSets.length = " + encSets.length);
					    	String[] encVars;
					    	for(int encGroup = 0;encGroup < (encSets.length);encGroup++)
					    	{
					    		//log.info("Enchantment Number: " + (encGroup + 1) + " / " + (encSets.length));
					    		encVars = encSets[encGroup].split("[:]");
					    		//log.info("encVars[0] = " + encVars[0]);
					    		//log.info("encVars.length = " + encVars.length);
					    		//log.info("" + Integer.parseInt(encVars[encCount]));
					    		enchants[encGroup][0] = Integer.parseInt(encVars[encCount++]);
					    		//log.info("" + Integer.parseInt(encVars[encCount]));
					    		enchants[encGroup][1] = Integer.parseInt(encVars[encCount++]);
					    		//log.info("" + Integer.parseInt(encVars[encCount]));
					    		enchants[encGroup][2] = Integer.parseInt(encVars[encCount++]);
					    		
					    		//log.info(enchants[encGroup][0] + ":" + enchants[encGroup][1] + ":" + enchants[encGroup][2]);
					    		encCount = 0;
					    	}
					    }
					    else
					    {
					    	enchants = null;
					    	//log.info("No Enchantments Defined. Skipping...");
					    }
					    
					    if(!rewards[3].contains("-"))
					    {
						    lore = rewards[4];
					    }
					    else
					    {
					    	lore = "LEFT_BLANK";
					    }
					    
					}
					else //2 variables are required to input into the table. Might as well cut them off and make them fix it.
					{
						util.logError("[2] Syntax error in rewards config. Disabling rewards features.");
						rewardsTable = null;
						return null;
					}
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					util.logError("[3] Syntax error in rewards config. Disabling rewards features.");
					rewardsTable = null;
					e.printStackTrace();
					return null;
				}
			    
				if(enchants != null)
				{
					int encLoop = 0;
					int[] defaultEnc = {0,0,0};
					while((enchants[encLoop] != defaultEnc) && (encLoop < 4))
					{
						enchantsList += enchants[encLoop][0] + ":" + enchants[encLoop][1] + ":" + enchants[encLoop][2] + " ";
						encLoop++;
					}
				}
				else
				{
					enchantsList = "NONE_GIVEN";
				}
				
				//log.info("Entry " + ii + ": " + chance + " " + name + " " + itemid + " " + enchantsList + " " + lore);
				RewardContainer reward = new RewardContainer(chance, name, itemid, enchants, lore);
				rewardsTable[j][ii] = reward;
				
				ii++;
			    
			    //Reset the strings, or wrong values may be passed back.
			    name = "";
			    itemid = "";
			    enchantsList = "";
			    if(enchants != null)
			    {
			    	enchants = null;
			    }
			    lore = "";
	
			}
			iii += ii;
			
		}
		
		if(iii > 0)
		{
			util.logInfo("Loaded " + iii + " rewards.\n-----------------------------");
		}
		else
		{
			util.logInfo("No Rewards Loaded. Config invalid, or are there no rewards?");
		}
		}
		catch(Exception e)
		{
			//fallback to let the plugin load properly.
			e.printStackTrace();
		}
		/*int poolloop = 0, rewardloop = 0;
		try
		{
			for(poolloop = 0;poolloop < rewardsTable.length;poolloop++)
			{
				for(rewardloop = 0;rewardloop < rewardsTable[poolloop].length;rewardloop++)
				{
					try
					{
						log.info(rewardsTable[poolloop][rewardloop].getName());
						for(int enchantLoop = 0;enchantLoop < rewardsTable[poolloop][rewardloop].getEnchants().length;enchantLoop++)
						{
							log.info(" - " + rewardsTable[poolloop][rewardloop].getEnchants()[enchantLoop][0]);
							log.info(" - " + rewardsTable[poolloop][rewardloop].getEnchants()[enchantLoop][1]);
							log.info(" - " + rewardsTable[poolloop][rewardloop].getEnchants()[enchantLoop][2]);
						}
						
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
	
	public int[] getEnabledCommands()
	{
		//Disabled by default (in-case of outdated config or lack of config).
		int[] toggle = {0,0,0,0,0};
		
		toggle[0] = getInt("options.toggle.rankpromotion");
		toggle[1] = getInt("options.toggle.credits");
		toggle[2] = getInt("options.toggle.presents");
		
		return toggle;
	}
	
	public void reloadConfig()
	{
		b.getConfigSetup().reloadConfig();
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