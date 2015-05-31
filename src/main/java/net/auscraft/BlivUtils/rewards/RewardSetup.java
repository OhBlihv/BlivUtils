package net.auscraft.BlivUtils.rewards;

import java.util.List;

import net.auscraft.BlivUtils.utils.FlatFile;

public class RewardSetup
{

	public static RewardContainer[][] loadRewards(FlatFile cfg)
	{	
		int ii = 0; //Counts number of rewards in a pool.
		
		//Retrieve the list of rewards for each category.
		int rewardPoolNum = cfg.getInt("options.rewards.count");
		//util.logInfo("Pool Count: " + rewardPoolNum);
		
		//Reset the rewards table, as this function will be used to reload the table too.
		RewardContainer[][] rewardsTable = new RewardContainer[rewardPoolNum][50]; //Hardcoded 50 reward max.
		
		try
		{
			//Loop through each pool
			for(int j = 0;j < rewardPoolNum;j++)
			{
				ii = 0;
				List<String> pool = cfg.getStringList("options.rewards.pool" + (j+1));
				for(String i : pool)
				{
					String name = "", itemid = "", lore = "";
					String enchantsList = "";
					double chance = 1.0;
					int[][] enchants = {{-1,-1,-1},{-1,-1,-1},{-1,-1,-1},{-1,-1,-1},{-1,-1,-1}}; //5 Enchantments per item, 3 Available Enchantment Variables
					
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
						    	int encCount = 0;
						    	String[] encVars;
						    	for(int encGroup = 0;encGroup < (encSets.length);encGroup++)
						    	{
						    		encVars = encSets[encGroup].split("[:]");
						    		enchants[encGroup][0] = Integer.parseInt(encVars[encCount++]);
						    		enchants[encGroup][1] = Integer.parseInt(encVars[encCount++]);
						    		enchants[encGroup][2] = Integer.parseInt(encVars[encCount++]);
						    		
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
							//util.logError("[2] Syntax error in rewards config. Disabling rewards features.");
							rewardsTable = null;
							return null;
						}
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
						//util.logError("[3] Syntax error in rewards config. Disabling rewards features.");
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
			}
		}
		catch(Exception e)
		{
			//fallback to let the plugin load properly.
			e.printStackTrace();
		}
		return rewardsTable;
	}
	
}
