package uk.codingbadgers.bHelpful;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import uk.codingbadgers.bHelpful.commands.MaintenanceCommand;
import uk.codingbadgers.bHelpful.commands.MotdCommand;
import uk.codingbadgers.bHelpful.commands.NewsCommand;
import uk.codingbadgers.bHelpful.commands.PlayerListCommand;

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
		
		MotdCommand.displayMotd(player);		
		NewsCommand.displayNews(player, 4);
		
		// if maintenance is on, then display the message
		if (MaintenanceCommand.Maintenance.NORMAL_MAINTENANCE) {
			Output.player(player, "[bHelpful] ", "Maintenance Mode has Been Enabled. The server may lag. Please bare with us");
		}
		
		if (MaintenanceCommand.Maintenance.STAFF_MAINTENANCE) {
			Output.player(player, "[bHelpful]", "Staff maintenance mode enabled");
		}

		if (!hasPlayedBefore(player)) {
			Output.server("[bHelpful]", "Please welcome " + player.getName() + " to the server");
		}
	
		PlayerListCommand.displayList(player);
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
		if (MaintenanceCommand.Maintenance.STAFF_MAINTENANCE) {
			if (!bHelpful.hasPermission(player, "bhelpful.staff")) {
				event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Maintance Mode Enabled");
			}
		}
		
	}

}
