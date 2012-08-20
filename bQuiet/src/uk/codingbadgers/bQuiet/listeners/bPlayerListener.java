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
