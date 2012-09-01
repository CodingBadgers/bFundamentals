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
