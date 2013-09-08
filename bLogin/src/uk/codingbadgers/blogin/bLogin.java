/**
 * bFundamentalsBuild 1.2-SNAPSHOT
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
package uk.codingbadgers.blogin;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import uk.codingbadgers.bFundamentals.module.Module;

/**
 * The base module class for bLogin.
 */
public class bLogin extends Module implements Listener{

	/** The login message. */
	private String m_loginMessage = "";
	
	/** The logout message. */
	private String m_logoutMessage = "";
	
	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.Module#onEnable()
	 */
	@Override
	public void onEnable() {
		register(this);
		
		loadConfig();
		
		log(Level.INFO, getName() + " has been enabled succesfully");
	}

	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.Module#onDisable()
	 */
	@Override
	public void onDisable() {
		log(Level.INFO, getName() + " has been disabled succesfully");
	}
	
	/**
	 * Load config.
	 */
	public void loadConfig() {
		FileConfiguration config = getConfig();
		
		try {
			config.addDefault("message.login", "&e%Player% has logged into the server");
			config.addDefault("message.logout", "&e%Player% has logged into the server");
			config.options().copyDefaults(true);
			saveConfig();
		} catch (Exception ex) {
			log(Level.WARNING, "Error whilst creating config");
			return;
		}
		
		m_loginMessage = config.getString("message.login");
		m_logoutMessage = config.getString("message.logout");
	}
	
	
	/**
	 * On player join.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		String message = replaceMacros(m_loginMessage, event.getPlayer());
		event.setJoinMessage(message);
	}
	
	/**
	 * On player leave.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeave(PlayerQuitEvent event) {
		String message = replaceMacros(m_logoutMessage, event.getPlayer());
		event.setQuitMessage(message);
	}
	
	/**
	 * On player leave.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeave(PlayerKickEvent event) {
		String message = replaceMacros(m_logoutMessage, event.getPlayer());
		event.setLeaveMessage(message);
	}
	
	/**
	 * Replace colours.
	 *
	 * @param message the message
	 * @return the string
	 */
	public static String replaceColours(String message) {

        String formattedMessage = message;

        while(formattedMessage.indexOf("&") != -1) {
            String code = formattedMessage.substring(formattedMessage.indexOf("&") + 1, formattedMessage.indexOf("&") + 2);
            formattedMessage = formattedMessage.substring(0, formattedMessage.indexOf("&")) +  ChatColor.getByChar(code) + formattedMessage.substring(formattedMessage.indexOf("&") + 2);
        }

        return formattedMessage;
    }
	
	/**
	 * Replace macros.
	 *
	 * @param message the message
	 * @param player the player
	 * @return the string
	 */
	public static String replaceMacros(String message, Player player) {
		return replaceColours(message.replace("%Player%", player.getName()).replace("%Server%", Bukkit.getServerName()));
	}

}
