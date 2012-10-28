package uk.codingbadgers.bHelpful.commands;

import org.bukkit.command.CommandSender;

import uk.codingbadgers.bHelpful.Configuration;

/**
 * @author james
 */
public class Vote {

	/**
	 * Display vote info.
	 *
	 * @param sender the player
	 */
	public static void displayVoteInfo(CommandSender sender) {
		
		for( int i = 0; i < Configuration.VOTE.size(); ++i ) {
    		sender.sendMessage(Configuration.VOTE.get(i));
    	}
	}
}
