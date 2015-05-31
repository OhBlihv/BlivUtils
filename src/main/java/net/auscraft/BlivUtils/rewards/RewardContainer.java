package net.auscraft.BlivUtils.rewards;

public class RewardContainer
{
	private double chance;
	private String name;
	private String action;
	private int[][] enchantmentOpts = null;
	private String lore = null;
	
	//Everything
	public RewardContainer(double inChance, String inName, String inAction, int[][] inEnchants, String inLore)
	{
		chance = inChance;
		name = inName;
		action = inAction;
		//If no enchants/lore on the item, don't set them up!
		try
		{
			if((inEnchants != null))
			{
				enchantmentOpts = inEnchants;
			}
		}
		catch(NullPointerException e)
		{
			enchantmentOpts = null;
		}
		
		try
		{
			if((inLore == null) || (inLore.equals("LEFT_BLANK")))
			{
				lore = inLore;
			}
		}
		catch(NullPointerException e)
		{
			enchantmentOpts = null;
		}
	}
	
	public double getChance()
	{
		return chance;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getAction()
	{
		return action;
	}
	
	public int[][] getEnchants()
	{
		return enchantmentOpts;
	}
	
	public String getLore()
	{
		return lore;
	}
}