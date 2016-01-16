package net.auscraft.BlivUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.auscraft.BlivUtils.util.BUtil;

public class RemovedCommand implements CommandExecutor
{
	
	//-------------------------------------------//
	//Any command that is disabled through config//
	//will instead link to this class, which will//
	//simply print that the command is disabled, //
	//and then quit.							 //
	//											 //
	//It's better than just a blank/no printout. //
	//-------------------------------------------//
	
	private static RemovedCommand instance = null;
	public static RemovedCommand getInstance()
	{
		if(instance == null)
		{
			instance = new RemovedCommand();
		}
		return instance;
	}
	
	private RemovedCommand()
	{
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
	{
		BUtil.printError(sender, "This command is disabled on this server!");
		return true;
	}

}
