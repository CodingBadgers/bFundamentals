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
package uk.codingbadgers.bsocial.players;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;


public class PlayerManager {

	/** The players. */
	private static List<ChatPlayer> players = new ArrayList<ChatPlayer>();
	
	public List<ChatPlayer> getPlayers() {
		return players;
	}
	
	/**
	 * Find a player based of the bukkit player.
	 *
	 * @param player the player
	 * @return the chat player
	 */
	public ChatPlayer findPlayer(Player player) {
		Iterator<ChatPlayer> itr = players.iterator();
		while(itr.hasNext()) {
			ChatPlayer current = itr.next();
			if (current.getPlayer().equals(player))
				return current;
		}
		return null;
	}
	
	public void addPlayer(ChatPlayer player) {
		players.add(player);
	}
	
	public void removePlayer(ChatPlayer player) {
		players.remove(player);
	}
	
	public void removePlayer(Player player) {
		players.remove(findPlayer(player));
	}
}
