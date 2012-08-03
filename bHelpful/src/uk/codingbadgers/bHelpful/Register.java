package uk.codingbadgers.bHelpful;

import org.bukkit.entity.Player;

/**
 *
 * @author James
 */
public class Register {
    
    /**
     * Display register.
     *
     * @param player the player
     */
    static public void displayRegister(Player player) {
    	
    	for( int i = 0; i < Configuration.REGISTER.size(); ++i ) {
    		player.sendMessage(Configuration.REGISTER.get(i));
    	}
    	    	
    }
    
}
