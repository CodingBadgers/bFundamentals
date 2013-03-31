package uk.codingbadgers.bHelpful.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.bHelpful.Configuration;
import uk.codingbadgers.bHelpful.bHelpful;

public class Motd {

	/**
	 * Display motd.
	 *
	 * @param sender the player
	 */
	public static void displayMotd(CommandSender sender) {
		
		// minimap perms
		String minimapPerms = "&0&0";
		if (sender instanceof Player && bHelpful.hasPermission((Player)sender, "bhelpful.minimap.cavemapping"))
			minimapPerms += "&1";
		if (sender instanceof Player && bHelpful.hasPermission((Player)sender, "bhelpful.minimap.player"))
			minimapPerms += "&2";
		if (sender instanceof Player && bHelpful.hasPermission((Player)sender, "bhelpful.minimap.animal"))
			minimapPerms += "&3";
		if (sender instanceof Player && bHelpful.hasPermission((Player)sender, "bhelpful.minimap.mob"))
			minimapPerms += "&4";
		if (sender instanceof Player && bHelpful.hasPermission((Player)sender, "bhelpful.minimap.slime"))
			minimapPerms += "&5";
		if (sender instanceof Player && bHelpful.hasPermission((Player)sender, "bhelpful.minimap.squid"))
			minimapPerms += "&6";
		if (sender instanceof Player && bHelpful.hasPermission((Player)sender, "bhelpful.minimap.other"))
			minimapPerms += "&7";
		minimapPerms += "&e&f";
		
		sender.sendMessage(Configuration.replaceColors(minimapPerms));
		
		for (int i = 0; i<Configuration.MOTD.size(); i++) {
			sender.sendMessage(Configuration.MOTD.get(i));
		}
		
	}
	
}
