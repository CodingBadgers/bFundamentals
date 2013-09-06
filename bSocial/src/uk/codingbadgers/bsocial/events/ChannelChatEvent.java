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
package uk.codingbadgers.bsocial.events;

import org.bukkit.event.HandlerList;

import uk.codingbadgers.bsocial.chanels.ChatChannel;
import uk.codingbadgers.bsocial.players.ChatPlayer;

/**
 * ChannelChatEvent called whenever a player speaks in a channel
 */
public class ChannelChatEvent extends bSocialEvent{

	/** The channel the message was sent in. */
	private ChatChannel m_channel = null;
	
	/** The message sent. */
	private String m_message = null;
	
	/** The handlers for this event. */
	private HandlerList m_handlers = new HandlerList();
	
	/**
	 * Instantiates a new channel chat event.
	 *
	 * @param player the player
	 * @param channel the channel
	 * @param message the message
	 */
	public ChannelChatEvent(ChatPlayer player, ChatChannel channel, String message) {
		super(player);
		m_channel = channel;
		m_message = message;
	}
	
	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return m_message;
	}
	
	/**
	 * Gets the channel.
	 *
	 * @return the channel
	 */
	public ChatChannel getChannel() {
		return m_channel;
	}
	
	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		m_message = message;
	}
	
	/**
	 * Sets the channel.
	 *
	 * @param channel the new channel
	 */
	public void setChannel(ChatChannel channel) {
		m_channel = channel;
	}
	
	/* (non-Javadoc)
	 * @see org.bukkit.event.Event#getHandlers()
	 */
	@Override
	public HandlerList getHandlers() {
		return m_handlers;
	}

}
