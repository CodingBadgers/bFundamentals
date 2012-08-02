package uk.codingbadgers.bHelpful;

import org.bukkit.entity.Player;

/**
 *
 * @author James
 */
public class Register {
    
    static public void displayRegister(Player player) {
    	
    	for( int i = 0; i < Configuration.register.size(); ++i ) {
    		player.sendMessage(Configuration.register.get(i));
    	}
    	    	
    }
    
}
