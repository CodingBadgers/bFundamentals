/**
 * bQuiet 1.2-SNAPSHOT
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
package uk.codingbadgers.bQuiet.containers;

/**
 * The Class bChatMessage.
 */
public class bChatMessage {
	
	/** The message. */
	private String m_message = null;
	
	/** The time it was sent. */
	private long m_time = 0;

	/**
	 * Instantiates a new b chat message.
	 *
	 * @param message the message
	 */
	public bChatMessage(String message) {
		m_message = message;
		m_time = System.currentTimeMillis();
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
	 * Gets the time.
	 *
	 * @return the time
	 */
	public long getTime() {
		return m_time;
	}

	/**
	 * Force lower case.
	 */
	public void forceLowerCase() {
		m_message = m_message.toLowerCase();
	}

}
