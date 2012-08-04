package uk.codingbadgers.bFundamentals.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL) 
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		BasePlayer bPlayer = new BasePlayer(player);
		
		PlayerUtils.players.add(bPlayer);
	}
	
	@EventHandler(priority = EventPriority.NORMAL) 
	public void onPlayerQuit(PlayerQuitEvent event) {
		PlayerUtils.players.removePlayer(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.NORMAL) 
	public void onPlayerKick(PlayerKickEvent event) {
		PlayerUtils.players.removePlayer(event.getPlayer());
	}
}
