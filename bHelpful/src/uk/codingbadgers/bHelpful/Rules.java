package uk.codingbadgers.bHelpful;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author James
 */
public class Rules {
    
    
    static public void displayRules(Player player) {
    	
    	for( int i = 0; i < Configuration.rules.size(); ++i ) {
            
    		String rulenum = Integer.toString(i + 1);
    		player.sendMessage(ChatColor.RED + rulenum + ". "+ ChatColor.WHITE + Configuration.rules.get(i));
    	}    	
    	
    }
    
}
