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
