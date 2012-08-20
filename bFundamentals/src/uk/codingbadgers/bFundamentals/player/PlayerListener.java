package uk.codingbadgers.bFundamentals.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

	/**
	 * Called when a player joins the server
	 *
	 * @param The player join event
	 */
	@EventHandler(priority = EventPriority.NORMAL) 
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		BasePlayer bPlayer = new BasePlayer(player);
		
		PlayerUtils.getPlayerArray().add(bPlayer);
	}
	
	/**
	 * Called when a player quits the server
	 *
	 * @param The player quit event
	 */
	@EventHandler(priority = EventPriority.NORMAL) 
	public void onPlayerQuit(PlayerQuitEvent event) {
		PlayerUtils.getPlayerArray().removePlayer(event.getPlayer());
	}
	
	/**
	 * Called when a player is kicked from the server
	 *
	 * @param The player kick event
	 */
	@EventHandler(priority = EventPriority.NORMAL) 
	public void onPlayerKick(PlayerKickEvent event) {
		PlayerUtils.getPlayerArray().removePlayer(event.getPlayer());
	}
}
