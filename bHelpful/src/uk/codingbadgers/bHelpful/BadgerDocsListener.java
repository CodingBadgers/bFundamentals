package uk.codingbadgers.bHelpful;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * 
 * @author James
 */

public class BadgerDocsListener implements Listener {

	@SuppressWarnings("unused")
	private bHelpful m_plugin = null;

	public BadgerDocsListener(bHelpful plugin) {
		m_plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		Motd.displayMotd(player);
		
		News.displayNews(player, 4);
		
		// if maintenance is on, then display the message
		if (Maintenance.isMaintenance()) {
			Output.player(player, "[bHelpful] ", "Maintenance Mode has Been Enabled. The server may lag. Please bare with us");
		}
		
		if (Maintenance.isStaffMaintenance()) {
				Output.player(player, "[bHelpful]", "Staff maintenance mode enabled");
		}

		if (!hasPlayedBefore(player))
			Output.server("[bHelpful]", "Please welcome " + player.getName() + " to the server");
	}
	
	// this should work, may change though if needed
	private boolean hasPlayedBefore(Player player) {
		return player.hasPlayedBefore();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLogin(PlayerLoginEvent event) {
		
		Player player = event.getPlayer();		
		if (Maintenance.isStaffMaintenance()) {
			if (!bHelpful.hasPerms(player, "bhelpful.staff")) {
				event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Maintance Mode Enabled");
			}
		}
		
	}

}
