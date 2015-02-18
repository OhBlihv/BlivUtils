package net.auscraft.BlivUtils.timed;

public class CommandContainer {

	//Player name
	//Command to reset/toggle
	//Time to execute command (UNIX Time)
	
	private String player;
	private String command;
	private long time;
	
	public CommandContainer(String inPlayer, String inCommand, long inTime)
	{
		player = inPlayer;
		command = inCommand;
		time = inTime;
	}
	
	public String getPlayer()
	{
		return player;
	}
	
	public String getCommand()
	{
		return command;
	}
	
	public long getTime()
	{
		return time;
	}
	
	public void addTime(long inTime)
	{
		time += inTime;
	}
	
	
	
}
