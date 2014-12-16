package net.auscraft.BlivUtils.rewards;

public class RewardContainer
{
	private String name;
	private String action;
	private int[][] enchantmentOpts = null;
	private String lore = null;
	
	//Everything
	public RewardContainer(String inName, String inAction, int[][] inEnchants, String inLore)
	{
		name = inName;
		action = inAction;
		//If no enchants/lore on the item, don't set them up!
		if(inEnchants != null)
		{
			enchantmentOpts = inEnchants;
		}
		if(inLore != null)
		{
			lore = inLore;
		}
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