/**
 * bFundamentals 1.2-SNAPSHOT
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
package uk.codingbadgers.bFundamentals.player;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.backup.BackupMap;
import uk.codingbadgers.bFundamentals.backup.PlayerBackup;

/**
 * The Basic Player class for all modules.
 */
public class FundamentalPlayer {

	protected Player m_player;
	protected BackupMap m_backups;
	protected HashMap<Class<? extends PlayerData>, PlayerData> m_playerData = new HashMap<Class<? extends PlayerData>, PlayerData>();

	/**
	 * Instantiates a new base player.
	 *
	 * @param player the bukkit player
	 */
	public FundamentalPlayer(Player player) {
		m_player = player;
		m_backups = new BackupMap(getPlayer());
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
		
		if (bytes.length > Messenger.MAX_MESSAGE_SIZE) {
			String header = "Client Message; length:" + Math.ceil((double) bytes.length / (double) Messenger.MAX_MESSAGE_SIZE);
			m_player.sendPluginMessage(bFundamentals.getInstance(), channel, header.getBytes());
			
			while(true) {
				byte[] toSend = Arrays.copyOf(bytes, Messenger.MAX_MESSAGE_SIZE);
				m_player.sendPluginMessage(bFundamentals.getInstance(), channel, toSend);
				
				try {
					bytes = Arrays.copyOfRange(bytes, Messenger.MAX_MESSAGE_SIZE, bytes.length);
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
	 * Gets the player data for a given data id
	 *
	 * @param clazz the string representation of the datatype class
	 * @return the player data if it exists, else null.
	 * @see PlayerData
	 * @throws ClassNotFoundException if the class cannot be located
	 */
	public PlayerData getPlayerdata(String clazz) throws ClassNotFoundException {
		return m_playerData.get(Class.forName(clazz));
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
		m_backups = null;
	}
	
	public PlayerBackup backupInventory() {
		return m_backups.backupPlayer(m_player.getWorld(), false);
	}

	public PlayerBackup backupInventory(boolean clearInv) {
		return m_backups.backupPlayer(m_player.getWorld(), clearInv);
	}
	
	public PlayerBackup backupInventory(World world) {
		return m_backups.backupPlayer(world, false);
	}

	public PlayerBackup backupInventory(World world, boolean clearInv) {
		return m_backups.backupPlayer(world, clearInv);
	}
	
	public boolean restoreInventory() {
		return m_backups.restorePlayer(m_player.getWorld());
	}
	
	public boolean restoreInventory(World world) {
		return m_backups.restorePlayer(world);
	}
}
