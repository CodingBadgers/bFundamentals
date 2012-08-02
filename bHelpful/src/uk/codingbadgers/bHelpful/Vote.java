package uk.codingbadgers.bHelpful;

import org.bukkit.entity.Player;

public class Vote {

	public static void displayVoteInfo(Player player) {
		
		for( int i = 0; i < Configuration.vote.size(); ++i ) {
    		player.sendMessage(Configuration.vote.get(i));
    	}
	}
}
