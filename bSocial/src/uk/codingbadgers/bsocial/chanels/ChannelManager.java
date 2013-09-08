/**
 * bFundamentalsBuild 1.2-SNAPSHOT
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
package uk.codingbadgers.bsocial.chanels;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import uk.codingbadgers.bsocial.config.ConfigManager;
import uk.codingbadgers.bsocial.players.ChatPlayer;

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
