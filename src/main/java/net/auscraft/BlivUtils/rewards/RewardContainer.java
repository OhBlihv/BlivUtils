package net.auscraft.BlivUtils.rewards;

import lombok.Getter;

public class RewardContainer
{
	@Getter
	private double chance;
	@Getter
	private String name;
	@Getter
	private String action;
	@Getter
	private int[][] enchantmentOpts = null;
	@Getter
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
}