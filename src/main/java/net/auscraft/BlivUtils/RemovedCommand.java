package net.auscraft.BlivUtils;

import net.auscraft.BlivUtils.utils.Utilities;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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

	private Utilities util;
	
	public RemovedCommand(BlivUtils instance)
	{
		util = instance.getUtil();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
	{
		util.printError(sender, "This command is disabled on this server!");
		return true;
	}

}
