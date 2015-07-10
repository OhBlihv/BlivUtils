package net.auscraft.BlivUtils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.auscraft.BlivUtils.utils.BUtil;

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
	
	public RemovedCommand()
	{
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
	{
		BUtil.printError(sender, "This command is disabled on this server!");
		return true;
	}

}
