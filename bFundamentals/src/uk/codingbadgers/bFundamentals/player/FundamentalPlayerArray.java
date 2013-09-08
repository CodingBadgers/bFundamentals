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

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.entity.Player;

/**
 * The BasePlayer Array.
 */
public class FundamentalPlayerArray extends ArrayList<FundamentalPlayer> {

	/** Auto generated UID. */
	private static final long serialVersionUID = -5142104058202469125L;

	/**
	 * Gets the player based of a bukkit player.
	 *
	 * @param player the bukkit player
	 * @return the player
	 */
	public FundamentalPlayer getPlayer(Player player) {
		return getPlayer(player.getName());
	}
	
	/**
	 * Gets the player based of their name.
	 *
	 * @param player the player's name
	 * @return the player
	 */
	public FundamentalPlayer getPlayer(String player) {
		Iterator<FundamentalPlayer> itr = iterator();
		
		while(itr.hasNext()) {
			FundamentalPlayer current = itr.next();
			if (current.getPlayer().getName().equalsIgnoreCase(player)) {
				return current;
			}
		}
		
		return null;
	}
	
	/**
	 * Removes the player from the array.
	 *
	 * @param player the bukkit player
	 */
	public void removePlayer(Player player) {
		FundamentalPlayer bPlayer = getPlayer(player);
		if (bPlayer != null) {
			bPlayer.destroy();
			remove(bPlayer);
		}
	}
	
}
