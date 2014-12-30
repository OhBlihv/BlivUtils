package net.auscraft.BlivUtils.credits;

public class CreditContainer {
	
	private String playerName;
	private int value;
	
	public CreditContainer(String inPlayer, int inValue)
	{
		playerName = inPlayer;
		value = inValue;
	}
	
	public void nullPlayer()
	{
		playerName = null;
	}
	
	public String getPlayer()
	{
		return playerName;
	}
	
	public int getValue()
	{
		return value;
	}
	
	
	
}
