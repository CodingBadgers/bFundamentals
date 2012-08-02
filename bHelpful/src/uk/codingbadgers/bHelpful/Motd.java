package uk.codingbadgers.bHelpful;

import org.bukkit.entity.Player;

public class Motd {

	public static void displayMotd(Player player) {
		
		// minimap perms
		String minimapPerms = "&0&0";
		if (bHelpful.hasPerms(player, "bhelpful.minimap.cavemapping"))
			minimapPerms += "&1";
		if (bHelpful.hasPerms(player, "bhelpful.minimap.player"))
			minimapPerms += "&2";
		if (bHelpful.hasPerms(player, "bhelpful.minimap.animal"))
			minimapPerms += "&3";
		if (bHelpful.hasPerms(player, "bhelpful.minimap.mob"))
			minimapPerms += "&4";
		if (bHelpful.hasPerms(player, "bhelpful.minimap.slime"))
			minimapPerms += "&5";
		if (bHelpful.hasPerms(player, "bhelpful.minimap.squid"))
			minimapPerms += "&6";
		if (bHelpful.hasPerms(player, "bhelpful.minimap.other"))
			minimapPerms += "&7";
		minimapPerms += "&e&f";
		
		player.sendMessage(Configuration.replaceColors(minimapPerms));
		
		for (int i = 0; i<Configuration.motd.size(); i++) {
			player.sendMessage(Configuration.motd.get(i));
		}
		
	}
	
}
