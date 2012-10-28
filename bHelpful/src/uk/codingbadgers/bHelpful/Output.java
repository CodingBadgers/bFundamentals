package uk.codingbadgers.bHelpful;

import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


/**
 *  
 * @author James
 */
public class Output{
    
    /**
     * Log to console
     *
     * @param INFO the Log level 
     * @param msg the message
     */
    static public void log(Level INFO, String msg){
    	bHelpful.MODULE.log(INFO, msg);
    }
    
    /**
     * Output to a player
     *
     * @param player the player
     * @param msg1 the prefix
     * @param msg2 the message
     */
    static public void player(CommandSender player, String msg1, String msg2) {
        player.sendMessage(ChatColor.GREEN + msg1 + ChatColor.WHITE + " " + msg2);
    }
    
    /**
     * Player warning.
     *
     * @param player the player
     * @param msg the msg
     */
    static public void playerWarning(CommandSender player, String msg) {
        player.sendMessage(ChatColor.GREEN + "[bHelpful]" + ChatColor.RED + " " + msg);
    }
    
    /**
     * Broadcast to the server
     *
     * @param msg1 the prefix
     * @param msg2 the message
     */
    static public void server(String msg1, String msg2) {
    	bHelpful.PLUGIN.getServer().broadcastMessage(ChatColor.GREEN + msg1 + " " + ChatColor.WHITE + msg2);
    }
    
    /**
     * Send a message
     *
     * @param player the player
     */
    static public void noPermission(CommandSender player) {
    	player.sendMessage(ChatColor.RED + "You do not have permission to do this.");
    }
}
