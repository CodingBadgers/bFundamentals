/**
 * bQuiet 1.2-SNAPSHOT
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
package uk.codingbadgers.bQuiet.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import uk.codingbadgers.bQuiet.containers.bPlayer;
import uk.codingbadgers.bQuiet.Global;
import uk.codingbadgers.bQuiet.bQuiet;

public class bPlayerListener implements Listener {
	
	/**
	 * Handle a player join event
	 *
	 * @param event The player join event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		bPlayer player = new bPlayer(event.getPlayer());
		Global.addPlayer(player);
	}
	
	/**
	 * Handle a player quit event
	 *
	 * @param event The player quit event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Global.removePlayer(Global.getPlayer(event.getPlayer()));
	}
	
	/**
	 * Handle a player kick event
	 *
	 * @param event The player kick event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerKick(PlayerKickEvent event) {
		Global.removePlayer(Global.getPlayer(event.getPlayer()));
	}
	
	/**
	 * Handle a player join chat
	 *
	 * @param event The player chat event
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		
		if (bQuiet.hasPermission(event.getPlayer(), "bQuiet.exclude"))
			return;
				
		bPlayer player = Global.getPlayer(event.getPlayer());
		if (player == null)
			return;
		
		// see if a player can talk
		if (!player.canTalk()) {
			event.setCancelled(true);
			return;
		}
		
		// see if they are spamming
		if (player.isSpamming(event.getMessage())) {
			event.setCancelled(true);
			return;
		}
		
		// see if they are advertising
		if (player.isAdvertising(event.getMessage())) {
			event.setCancelled(true);
			return;
		}
		
		event.setMessage(player.getLastMessage());
		
	}

}
