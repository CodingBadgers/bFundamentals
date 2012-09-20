package uk.codingbadgers.bFundamentals.player;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.bFundamentals;

/**
 * The Basic Player class for all modules.
 */
public abstract class BasePlayer {

	/** The Bukkit player. */
	protected Player m_player = null;
	
	/**
	 * Instantiates a new base player.
	 *
	 * @param player the bukkit player
	 */
	public BasePlayer(Player player) {
		m_player = player;
	}
	
	/**
	 * Gets the bukkit player.
	 *
	 * @return the bukkit player
	 */
	public Player getPlayer() {
		return m_player;
	}
	
	/**
	 * Send message.
	 *
	 * @param msg the msg
	 */
	public void sendMessage(String msg) {
		m_player.sendMessage(ChatColor.DARK_PURPLE + "[bFundamentals] " + ChatColor.RESET + msg);
	}
	
	/**
	 * Send plugin message.
	 *
	 * @param channel the plugin channel
	 * @param message the message
	 */
	public void sendClientMessage(String channel, String message) {
		m_player.sendPluginMessage(bFundamentals.getInstance(), channel, message.getBytes());
	}
	
	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public Location getLocation() {
		return m_player.getLocation();
	}
}
