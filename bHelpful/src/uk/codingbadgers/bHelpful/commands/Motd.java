package uk.codingbadgers.bHelpful.commands;

import org.bukkit.entity.Player;

import uk.codingbadgers.bHelpful.Configuration;
import uk.codingbadgers.bHelpful.bHelpful;

public class Motd {

	/**
	 * Display motd.
	 *
	 * @param player the player
	 */
	public static void displayMotd(Player player) {
		
		// minimap perms
		String minimapPerms = "&0&0";
		if (bHelpful.hasPermission(player, "bhelpful.minimap.cavemapping"))
			minimapPerms += "&1";
		if (bHelpful.hasPermission(player, "bhelpful.minimap.player"))
			minimapPerms += "&2";
		if (bHelpful.hasPermission(player, "bhelpful.minimap.animal"))
			minimapPerms += "&3";
		if (bHelpful.hasPermission(player, "bhelpful.minimap.mob"))
			minimapPerms += "&4";
		if (bHelpful.hasPermission(player, "bhelpful.minimap.slime"))
			minimapPerms += "&5";
		if (bHelpful.hasPermission(player, "bhelpful.minimap.squid"))
			minimapPerms += "&6";
		if (bHelpful.hasPermission(player, "bhelpful.minimap.other"))
			minimapPerms += "&7";
		minimapPerms += "&e&f";
		
		player.sendMessage(Configuration.replaceColors(minimapPerms));
		
		for (int i = 0; i<Configuration.MOTD.size(); i++) {
			player.sendMessage(Configuration.MOTD.get(i));
		}
		
	}
	
}
