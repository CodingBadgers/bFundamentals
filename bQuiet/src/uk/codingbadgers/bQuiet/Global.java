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
