package uk.codingbadgers.bHelpful;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author James
 */
public class Rules {
    
    
    /**
     * Display rules.
     *
     * @param player the player
     */
    static public void displayRules(Player player) {
    	
    	for( int i = 0; i < Configuration.RULES.size(); ++i ) {
            
    		String rulenum = Integer.toString(i + 1);
    		player.sendMessage(ChatColor.RED + rulenum + ". "+ ChatColor.WHITE + Configuration.RULES.get(i));
    	}    	
    	
    }
    
}
