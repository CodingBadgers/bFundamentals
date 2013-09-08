/**
 * bHelpful 1.2-SNAPSHOT
 * Copyright (C) 2013  CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
