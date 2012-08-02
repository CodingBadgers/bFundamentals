package uk.codingbadgers.bHelpful;

import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author James
 */
public class Output{
    
    static public void log(Level INFO, String msg){
    	bHelpful.module.log(Level.INFO, ChatColor.GREEN + "[bHelpful] " + ChatColor.WHITE + msg);
    }
    
    static public void player(Player player, String msg1, String msg2) {
        player.sendMessage(ChatColor.GREEN + msg1 + ChatColor.WHITE + " " + msg2);
    }
    
    static public void playerWarning(Player player, String msg) {
        player.sendMessage(ChatColor.GREEN + "[bHelpful]" + ChatColor.RED + " " + msg);
    }
    
    static public void server(String msg1, String msg2) {
    	bHelpful.m_plugin.getServer().broadcastMessage(ChatColor.GREEN + msg1 + " " + ChatColor.WHITE + msg2);
    }
    
    static public void noPermission(Player player) {
    	player.sendMessage(ChatColor.RED + "You do not have permission to do this.");
    }
}
