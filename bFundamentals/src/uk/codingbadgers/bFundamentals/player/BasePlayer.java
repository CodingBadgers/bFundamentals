package uk.codingbadgers.bFundamentals.player;

import org.bukkit.entity.Player;

/**
 * The Basic Player class for all modules.
 */
public class BasePlayer {

	/** The Bukkit player. */
	protected Player m_player = null;
	
	/**
	 * Instantiates a new base player.
	 *
	 * @param player the bukkit player
	 */
	public BasePlayer(Player player) {
		m_player = player;
	}
	
	/**
	 * Gets the bukkit player.
	 *
	 * @return the bukkit player
	 */
	public Player getPlayer() {
		return m_player;
	}
}
