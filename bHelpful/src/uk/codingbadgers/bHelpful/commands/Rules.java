package uk.codingbadgers.bHelpful.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import uk.codingbadgers.bHelpful.Configuration;

/**
 *
 * @author James
 */
public class Rules {
    
    
    /**
     * Display rules.
     *
     * @param sender the player
     */
    static public void displayRules(CommandSender sender) {
    	
    	for( int i = 0; i < Configuration.RULES.size(); ++i ) {
            
    		String rulenum = Integer.toString(i + 1);
    		sender.sendMessage(ChatColor.RED + rulenum + ". "+ ChatColor.WHITE + Configuration.RULES.get(i));
    	}    	
    	
    }
    
}
