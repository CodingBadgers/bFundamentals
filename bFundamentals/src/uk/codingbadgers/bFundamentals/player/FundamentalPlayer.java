package uk.codingbadgers.bFundamentals.player;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.bFundamentals;

/**
 * The Basic Player class for all modules.
 */
public class FundamentalPlayer {

	/** The Bukkit player. */
	protected Player m_player = null;
	
	/** A map of player data */
	protected HashMap<Class<?>, PlayerData> m_playerData = new HashMap<Class<?>, PlayerData>();
	
	/**
	 * Instantiates a new base player.
	 *
	 * @param player the bukkit player
	 */
	public FundamentalPlayer(Player player) {
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
	 * Gets the location of the player.
	 *
	 * @return the location
	 */
	public Location getLocation() {
		return m_player.getLocation();
	}
	
	/**
	 * Gets any player data of the given data ID.
	 *
	 * @return the player data if it exists, else null.
	 */	
	public PlayerData getPlayerData(Class<?> dataID) {
		return m_playerData.get(dataID);
	}
	
	/**
	 * Adds some player data, removing any old instances of the player data
	 */	
	public void addPlayerData(PlayerData data) {
		final Class<?> dataID = data.getClass();
		removePlayerData(dataID);		
		m_playerData.put(dataID, data);
	}
	
	/**
	 * Removes any data with the given data id
	 */	
	public void removePlayerData(Class<?> dataID) {
		if (m_playerData.containsKey(dataID)) {
			m_playerData.remove(dataID);
		}
	}

	/**
	 * Release any stored data about the player
	 */	
	public void destroy() {
		m_playerData.clear();
		m_player = null;
	}
	
}
