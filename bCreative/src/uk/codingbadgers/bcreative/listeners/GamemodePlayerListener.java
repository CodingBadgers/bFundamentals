package uk.codingbadgers.bcreative.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import uk.codingbadgers.bcreative.bCreative;
import uk.codingbadgers.bcreative.containers.GamemodePlayer;

public class GamemodePlayerListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		bCreative.getPlayerManager().getPlayers().add(new GamemodePlayer(event.getPlayer()));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeave(PlayerKickEvent event) {
		bCreative.getPlayerManager().getPlayers().removePlayer(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeave(PlayerQuitEvent event) {
		bCreative.getPlayerManager().getPlayers().removePlayer(event.getPlayer());
	}
	
}
