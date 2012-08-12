package uk.codingbadgers.bsocial.chanels;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import uk.codingbadgers.bsocial.ChatPlayer;
import uk.codingbadgers.bsocial.config.ConfigManager;

/**
 * The ChannelManager.
 */
public class ChannelManager {

	/** The m_channels. */
	private List<ChatChannel> m_channels = new ArrayList<ChatChannel>();
	
	/**
	 * Adds a channel.
	 *
	 * @param channel the channel
	 */
	public void addChannel(ChatChannel channel) {
		m_channels.add(channel);
	}
	
	/**
	 * Gets the channel based of its name.
	 *
	 * @param name the name
	 * @return the channel
	 */
	public ChatChannel getChannel(String name) {
		Iterator<ChatChannel> itr = m_channels.iterator();
		while(itr.hasNext()) {
			ChatChannel current = itr.next();
			if (current.getChannelName().equalsIgnoreCase(name))
				return current;
		}
		return null;
	}
	
	/**
	 * Gets the default channel for the server.
	 *
	 * @return the default channel
	 */
	public ChatChannel getDefaultChannel(){
		return getChannel(ConfigManager.DEFAULT_CHANNEL);
	}

	/**
	 * Gets all the channels on the server.
	 *
	 * @return the channels
	 */
	public List<ChatChannel> getChannels() {
		return m_channels;
	}
	
	/**
	 * Checks if it is channel.
	 *
	 * @param command the command
	 * @return true, if is channel
	 */
	public boolean isChannel(String command) {
		for (ChatChannel channel : m_channels) {
			if (channel.getChannelName().equalsIgnoreCase(command))
				return true;
		}
		
		return false;
	}

	/**
	 * Adds a player to all.
	 *
	 * @param player the player
	 */
	public void addToAll(ChatPlayer player) {
		for (ChatChannel channel : m_channels) {
			channel.addPlayer(player);
		}
	}

	/**
	 * Removes the channel from the server's memory.
	 *
	 * @param channel the channel
	 */
	public void removeChannel(ChatChannel channel) {
		m_channels.remove(channel);
	}
	
	/**
	 * Clears all channels from memory.
	 */
	public void clear() {
		m_channels.clear();
	}
} 
