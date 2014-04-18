/**
 * bHelpful 1.2-SNAPSHOT
 * Copyright (C) 2013  CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
public class PlayerListener implements Listener {

	/** The m_plugin. */
	@SuppressWarnings("unused")
	private bHelpful m_plugin = null;

	/**
	 * Instantiates a new badger docs listener.
	 *
	 * @param plugin the plugin
	 */
	public PlayerListener(bHelpful plugin) {
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
		PlayerListCommand.displayList(player);
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
