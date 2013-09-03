/**
 * bFundamentalsBuild 1.2-SNAPSHOT
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
package uk.codingbadgers.bsocial.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.events.ChannelChatEvent;
import uk.codingbadgers.bsocial.players.ChatPlayer;

/**
 * @author James
 */
@SuppressWarnings("deprecation")
public class ChatListener implements Listener{

	/**
	 * On player chat.
	 *
	 * @param event the chat event
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerChat(PlayerChatEvent event) {
		ChatPlayer player = bSocial.getPlayerManager().findPlayer(event.getPlayer());
		
		if(player == null)
			return;
		
		String message = event.getMessage();
		
		// make it have events :D
		ChannelChatEvent chatEvent = new ChannelChatEvent(player, player.getActiveChannel(), message);
		Bukkit.getServer().getPluginManager().callEvent(chatEvent);
		
		if (chatEvent.isCancelled()) {
			return;
		}
		
		player.sendMessage(chatEvent.getChannel(), chatEvent.getMessage());
		
		// we don't want to use bukkit chat, we can use our own system
		event.setCancelled(true);
	}
	
	/**
	 * On player join.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		ChatPlayer player = bSocial.getConfigManager().loadPlayer(event.getPlayer());
		bSocial.getPlayerManager().addPlayer(player);
	}
	
	/**
	 * On player leave.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeave(PlayerQuitEvent event) {
		bSocial.getConfigManager().savePlayer(bSocial.getPlayerManager().findPlayer(event.getPlayer()));
		bSocial.getPlayerManager().removePlayer(bSocial.getPlayerManager().findPlayer(event.getPlayer()));
	}
	
	/**
	 * On player leave.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeave(PlayerKickEvent event) {
		bSocial.getConfigManager().savePlayer(bSocial.getPlayerManager().findPlayer(event.getPlayer()));
		bSocial.getPlayerManager().removePlayer(bSocial.getPlayerManager().findPlayer(event.getPlayer()));
	}
}
