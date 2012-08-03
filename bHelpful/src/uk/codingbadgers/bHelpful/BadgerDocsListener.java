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

	/** The m_plugin. */
	@SuppressWarnings("unused")
	private bHelpful m_plugin = null;

	/**
	 * Instantiates a new badger docs listener.
	 *
	 * @param plugin the plugin
	 */
	public BadgerDocsListener(bHelpful plugin) {
		m_plugin = plugin;
	}

	/**
	 * On player join event.
	 *
	 * @param event the event
	 */
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
	
	/**
	 * Checks if the player has played before.
	 *
	 * @param player the player
	 * @return true, if they have
	 */
	private boolean hasPlayedBefore(Player player) {
		return player.hasPlayedBefore();
	}

	/**
	 * On player login event.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLogin(PlayerLoginEvent event) {
		
		Player player = event.getPlayer();		
		if (Maintenance.isStaffMaintenance()) {
			if (!bHelpful.hasPermission(player, "bhelpful.staff")) {
				event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Maintance Mode Enabled");
			}
		}
		
	}

}
