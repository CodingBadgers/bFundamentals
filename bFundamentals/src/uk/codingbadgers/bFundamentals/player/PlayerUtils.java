package uk.codingbadgers.bFundamentals.player;

import org.bukkit.entity.Player;

public class PlayerUtils {

	/** The stored player array. */
	private static BasePlayerArray m_players = new BasePlayerArray();
	
	/**
	 * Get the stored array of players
	 *
	 * @return The array of players
	 */
	public static BasePlayerArray getPlayerArray() {
		return m_players;
	}
	
	/**
	 * Gets the player based of their name.
	 *
	 * @param name The name of the player to find
	 * @return A player
	 */
	public static BasePlayer getPlayer(String name) {
		return m_players.getPlayer(name);
	}
	
	/**
	 * Gets the player based of their bukkit player.
	 *
	 * @param player the player object
	 * @return A player
	 */
	public static BasePlayer getPlayer(Player player) {
		return m_players.getPlayer(player);
	}
	
}
