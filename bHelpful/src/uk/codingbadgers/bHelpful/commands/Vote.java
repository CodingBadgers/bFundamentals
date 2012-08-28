package uk.codingbadgers.bHelpful.commands;

import org.bukkit.entity.Player;

import uk.codingbadgers.bHelpful.Configuration;

/**
 * @author james
 */
public class Vote {

	/**
	 * Display vote info.
	 *
	 * @param player the player
	 */
	public static void displayVoteInfo(Player player) {
		
		for( int i = 0; i < Configuration.VOTE.size(); ++i ) {
    		player.sendMessage(Configuration.VOTE.get(i));
    	}
	}
}
