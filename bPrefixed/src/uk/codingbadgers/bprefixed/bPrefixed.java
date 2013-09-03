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
package uk.codingbadgers.bprefixed;

import java.sql.ResultSet;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import uk.codingbadgers.bFundamentals.module.Module;

public class bPrefixed extends Module implements Listener {

	private HashMap<String, String> m_prefixes = new HashMap<String, String>();
	
	/**
	 * Called when the module is disabled.
	 */
	public void onDisable() {
	}

	/**
	 * Called when the module is loaded.
	 */
	public void onEnable() {
		register(this);		
		setupDatabase();
		loadPrefixes();
		registerCommand(new CommandPrefix(this));
	}
	
	/**
	 * Called when a player interacts with an entity
	 */	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		final String playerName = player.getName();
		final String prefix = m_prefixes.get(playerName);
		
		if (prefix != null) {
			final String prefixedName = prefix  + " " + player.getName();
			player.setDisplayName(prefixedName);
		}
		
	}
	
	/**
	 * Make sure the database table exists, else create it
	 */	
	private void setupDatabase() {
		
		if (m_database.tableExists("bPrefixed"))
			return;
		
		final String createQuery = 
		"CREATE TABLE bPrefixed " +
		"(" +
			"Player varchar(32)," +
			"Prefix varchar(32)" +
		")";

		m_database.query(createQuery, true);
		
	}
	
	/**
	 * Load all entities into memory for a quick lookup
	 */		
	private void loadPrefixes() {
		
		final String selectAll = "Select * FROM bPrefixed";
		
		ResultSet result = m_database.queryResult(selectAll);
		if (result != null) {
			
			try {
				while (result.next()) {
					
					String playername = result.getString("Player");
					String prefix = result.getString("Prefix");
					m_prefixes.put(playername, prefix);
					
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				m_database.freeResult(result);
				return;
			}
			
			m_database.freeResult(result);
			
		}
		
	}
	
	/**
	 * Add a new pet to the database
	 */	
	public void setPrefix(String player, String prefix) {
		
		boolean add = m_prefixes.containsKey(player) == false;
		
		if (add) {
			String addPrefix = 
				"INSERT INTO bPrefixed " +
					"VALUES ('" +
					player + "', '" +
					prefix +
				"')";
			m_database.query(addPrefix);
		}
		else {
			m_prefixes.remove(player);
			String updatePrefix = 
					"UPDATE bPrefixed " +
						"SET Prefix='" +
						prefix + 
						"' WHERE Player='" +
						player +
					"')";
			m_database.query(updatePrefix);
		}
		
		m_prefixes.put(player, prefix);
		
	}
	
	public void removePrefix(String player) {
		
		m_prefixes.remove(player);

		String removePprefix = 
				"DELETE FROM bPrefixed " +
					"WHERE Player='" +
					player + 
				"'";
			
		m_database.query(removePprefix);
		
	}

}
