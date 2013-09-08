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
package uk.codingbadgers.bQuiet;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import uk.codingbadgers.bQuiet.containers.bPlayer;

public class Global {
	
	private static Plugin m_plugin = null;
	private static ArrayList<bPlayer> m_players = new ArrayList<bPlayer>();
	
	/**
	 * Store access to the bFundamentals plugin
	 */
	public static void setPlugin(Plugin plugin) {
		m_plugin = plugin;
	}
	
	/**
	 * Get the static plugin instance
	 */
	public static Plugin getPlugin() {
		return m_plugin;
	}
	
	/**
	 * Helper to get the server
	 */
	public static Server getServer() {
		return m_plugin.getServer();
	}

	/**
	 * Add a player to the player array
	 */
	public static void addPlayer(bPlayer player) {
		m_players.add(player);		
	}
	
	/**
	 * Remove a player from the player array
	 */
	public static void removePlayer(bPlayer player) {
		m_players.remove(player);		
	}
	
	/**
	 * Get a player based upon a player object
	 */
	public static bPlayer getPlayer(Player player) {
		Iterator<bPlayer> pIterator = m_players.iterator();
		while (pIterator.hasNext()) {
			bPlayer bp = pIterator.next();
			if (bp.getPlayer() == player) {
				return bp;
			}
		}
		return null;
	}

}
