package uk.codingbadgers.bFundamentals.player;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
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
	protected HashMap<Class<? extends PlayerData>, PlayerData> m_playerData = new HashMap<Class<? extends PlayerData>, PlayerData>();
	
	/**
	 * Instantiates a new base player.
	 *
	 * @param player the bukkit player
	 */
	public FundamentalPlayer(Player player) {
		m_player = player;
	}
	
	/**
	 * Gets the bukkit player associated to this FundamentalPlayer.
	 *
	 * @return the bukkit player
	 * @see Player
	 */
	public Player getPlayer() {
		return m_player;
	}
	
	/**
	 * Send message, will split at new line characters '\n' and send each part as a seperate message.
	 *
	 * @param msg the message to send
	 */
	public void sendMessage(String msg) {
		String[] messages = msg.split("\n");
		
		for (String message : messages) {
			m_player.sendMessage(ChatColor.DARK_PURPLE + "[bFundamentals] " + ChatColor.RESET + message);
		}
	}
	
	/**
	 * Send plugin message.
	 *
	 * @param channel the plugin channel
	 * @param message the message, will be split up into separate messages with 
	 * 					header if the message is longer than 1024 bytes long
	 * @return true if successful, false if channel was not registered
	 */
	public boolean sendClientMessage(String channel, String message) {
		byte[] bytes = message.getBytes();
		
		if (!Bukkit.getMessenger().isOutgoingChannelRegistered(bFundamentals.getInstance(), channel)) {
			return false;
		}
		
		if (bytes.length > 1024) {
			String header = "Client Message; length:" + Math.ceil((double) bytes.length / 1024D);
			m_player.sendPluginMessage(bFundamentals.getInstance(), channel, header.getBytes());
			
			while(true) {
				byte[] toSend = Arrays.copyOf(bytes, 1024);
				m_player.sendPluginMessage(bFundamentals.getInstance(), channel, toSend);
				
				try {
					bytes = Arrays.copyOfRange(bytes, 1024, bytes.length);
				} catch (Exception ex) {
					break;
				}
			}
		} else {
			m_player.sendPluginMessage(bFundamentals.getInstance(), channel, bytes);
		}
		
		return true;
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
	 * @param dataID the data type to lookup
	 * @return the player data if it exists, else null.
	 * @see PlayerData
	 */	
	@SuppressWarnings("unchecked")
	public <T extends PlayerData> T getPlayerData(Class<? extends T> dataID) {
		return (T) m_playerData.get(dataID);
	}
	
	/**
	 * Adds some player data, removing any old instances of the player data
	 * 
	 * @param data the data to add to this player
	 * @see PlayerData
	 */	
	public void addPlayerData(PlayerData data) {
		final Class<? extends PlayerData> dataID = data.getClass();
		removePlayerData(dataID);		
		m_playerData.put(dataID, data);
	}
	
	/**
	 * Removes any data with the given data id
	 * 
	 * @param dataID the data type to remove
	 * @see PlayerData
	 */	
	public void removePlayerData(Class<?> dataID) {
		if (m_playerData.containsKey(dataID)) {
			m_playerData.remove(dataID);
		}
	}

	/**
	 * Release any stored data about the player, called on leave.
	 */	
	public void destroy() {
		m_playerData.clear();
		m_player = null;
	}
	
}
