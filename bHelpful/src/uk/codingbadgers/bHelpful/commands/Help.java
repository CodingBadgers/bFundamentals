package uk.codingbadgers.bHelpful.commands;

import java.util.ArrayList;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.bHelpful.Configuration;
import uk.codingbadgers.bHelpful.bHelpful;

/**
 *
 * @author James
 */
public class Help {
    
	/**
	 * Display help.
	 *
	 * @param sender the player
	 */
	public static void displayHelp(CommandSender sender) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("No help for you, :P");
			return;
		}
		
		Player player = (Player)sender;
		
		Permission permission = bHelpful.MODULE.getPermissions();
		
		if (permission == null) {
			sender.sendMessage("Help is based upon player rank.");
			sender.sendMessage("This server has no ranks.");
			return;
		}
				
		ArrayList<String> help = Configuration.getHelp(permission.getPrimaryGroup(player));
		
		if (help == null) {
			sender.sendMessage("No help exists for your rank.");
			return;
		}
		
		
		String rank = permission.getPrimaryGroup(player);
		rank = rank.substring(0, 1).toUpperCase() + rank.substring(1);
		
		sender.sendMessage(ChatColor.RED + "You are rank " + ChatColor.GRAY + "[" + rank + "]");
		
		for (int i = 0; i < help.size(); ++i ) {
			sender.sendMessage(help.get(i));
		}		
	}
      
}
