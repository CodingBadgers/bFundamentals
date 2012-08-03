package uk.codingbadgers.bQuiet.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import uk.codingbadgers.bQuiet.containers.bPlayer;
import uk.codingbadgers.bQuiet.bGlobal;

public class bPlayerListener implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		bPlayer player = new bPlayer(event.getPlayer());
		bGlobal.addPlayer(player);
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		bGlobal.removePlayer(bGlobal.getPlayer(event.getPlayer()));
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerKickEvent event) {
		bGlobal.removePlayer(bGlobal.getPlayer(event.getPlayer()));
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChat(PlayerChatEvent event) {
		
		if (bGlobal.hasPerms(event.getPlayer(), "bQuiet.exclude"))
			return;
				
		bPlayer player = bGlobal.getPlayer(event.getPlayer());
		if (player == null)
			return;
		
		if (!player.canTalk()) {
			event.setCancelled(true);
			return;
		}
		
		if (player.isSpamming(event.getMessage())) {
			event.setCancelled(true);
			return;
		}
		
		if (player.isAdvertising(event.getMessage())) {
			event.setCancelled(true);
			return;
		}
		
		event.setMessage(player.getLastMessage());
		
	}

}
