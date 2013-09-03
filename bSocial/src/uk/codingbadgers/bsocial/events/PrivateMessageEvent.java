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

import uk.codingbadgers.bsocial.players.ChatPlayer;

/**
 * PrivateMessageEvent called whenever a pm is sent to another player
 */
public class PrivateMessageEvent extends bSocialEvent{

	/** The recipient of the private message. */
	private ChatPlayer m_to;
	
	/** The message. */
	private String m_message;

	/** The handlers. */
	private HandlerList m_handlers = new HandlerList();

	/**
	 * Instantiates a new private message event.
	 *
	 * @param player the player
	 * @param to the to
	 * @param message the message
	 */
	public PrivateMessageEvent(ChatPlayer player, ChatPlayer to, String message) {
		super(player);
		setReciever(to);
		m_message = message;
	}

	/**
	 * Gets the reciever.
	 *
	 * @return the reciever
	 */
	public ChatPlayer getReciever() {
		return m_to;
	}

	/**
	 * Sets the receiver.
	 *
	 * @param to the new receiver
	 */
	public void setReciever(ChatPlayer to) {
		m_to = to;
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
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		m_message = message;
	}

	/* (non-Javadoc)
	 * @see org.bukkit.event.Event#getHandlers()
	 */
	@Override
	public HandlerList getHandlers() {
		return m_handlers;
	}

}
