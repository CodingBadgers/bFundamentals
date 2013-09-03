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

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import uk.codingbadgers.bsocial.players.ChatPlayer;

/**
 * The base bSocial event class
 */
public abstract class bSocialEvent extends Event implements Cancellable{
	
	/** The player the event is based off. */
	protected ChatPlayer m_player = null;
	
	/** Wether the event is cancelled . */
	protected boolean m_canceled = false;
	
	/**
	 * Instantiates a new b social event.
	 *
	 * @param player the player
	 */
	public bSocialEvent(ChatPlayer player) {
		m_player = player;
	}
	
	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	public ChatPlayer getPlayer() {
		return m_player;
	}

	/* (non-Javadoc)
	 * @see org.bukkit.event.Cancellable#isCancelled()
	 */
	@Override
	public boolean isCancelled() {
		return m_canceled;
	}

	/* (non-Javadoc)
	 * @see org.bukkit.event.Cancellable#setCancelled(boolean)
	 */
	@Override
	public void setCancelled(boolean canceled) {
		m_canceled = canceled;
	}

}
