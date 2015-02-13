package net.auscraft.BlivUtils.purchases;

import net.auscraft.BlivUtils.BlivUtils;
import net.auscraft.BlivUtils.utils.Utilities;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Ender implements CommandExecutor
{
	
	private Utilities util;
	
	public Ender(BlivUtils instance)
	{
		util = instance.getUtil();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
	{
		if (cmd.getName().equalsIgnoreCase("enderrank")	&& (sender.hasPermission("blivutils.purch"))) //Only for Console
		{
			/* Package Names ---------------------
			 * 
			 * Pets:
			 * EnderPetsPassive
			 * EnderPetsNeutral
			 * EnderPetsHostile
			 * EnderPetsALL
			 * 
			 * Trails:
			 * EnderTrailsSET1
			 * EnderTrailsSET2
			 * EnderTrailsALL
			 * 
			 * Disguises:
			 * EnderDisguisesPassive
			 * EnderDisguisesHostile
			 * EnderDisguisesALL
			 * EnderDisguisesALLEntity
			 * 
			 * Cooldowns:
			 * EnderCooldowns
			 * 
			 * Warps:
			 * EnderWarps
			 * 
			 * mcMMO:
			 * mcMMOSmall
			 * mcMMOMedium
			 * mcMMOLarge
			 * 
			 * -----------------------------------
			 * 
			 * SYNTAX: /enderrank <player> <package>
			 */
			
			if(args.length >= 1)
			{
				switch(args[1])
				{
				case "EnderPetsPassive": case "EnderPetsNeutral": case "EnderPetsHostile": case "EnderPetsALL":
				case "EnderTrailsSET1": case "EnderTrailsSET2": case "EnderTrailsALL":
				case "EnderDisguisesPassive": case "EnderDisguisesHostile": case "EnderDisguisesALL": case "EnderDisguisesALLEntity":
				case "EnderCooldowns": case "EnderWarps":
				case "mcMMOSmall": case "mcMMOMedium": case "mcMMOLarge":
					PermissionUser user = PermissionsEx.getUser(args[0]);
					user.setOption("group-" + args[1] + "-until", "2592000");
					user.addGroup(args[1]);
					util.logInfo("Package " + args[1] + " successfully added to " + sender.getName() + "'s account");
					return true;
				default:
					//Stay Silent, otherwise console will be spammed if a player doesnt use a variable
					return true;
				}
				
			}
			//Stay silent, otherwise console will be spammed if a player doesnt use a variable
			/*
			else
			{
				util.printError(sender, "Invalid Syntax: /enderrank <player> <package>");
			}
			*/
			
			return true;
		}
		return false;
	}

}
