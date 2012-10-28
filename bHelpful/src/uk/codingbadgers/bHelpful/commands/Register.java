package uk.codingbadgers.bHelpful.commands;

import org.bukkit.command.CommandSender;

import uk.codingbadgers.bHelpful.Configuration;

/**
 *
 * @author James
 */
public class Register {
    
    /**
     * Display register.
     *
     * @param sender the player
     */
    static public void displayRegister(CommandSender sender) {
    	
    	for( int i = 0; i < Configuration.REGISTER.size(); ++i ) {
    		sender.sendMessage(Configuration.REGISTER.get(i));
    	}
    	    	
    }
    
}
