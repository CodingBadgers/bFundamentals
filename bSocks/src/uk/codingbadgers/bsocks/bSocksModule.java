/**
 * bSocks 1.2-SNAPSHOT
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
package uk.codingbadgers.bsocks;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;

import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bsocks.threading.ListenerThread;
import uk.codingbadgers.bsocks.web.AdminListener;
import uk.codingbadgers.bsocks.web.PermissionsListener;
import uk.codingbadgers.bsocks.web.RequestType;
import uk.codingbadgers.bsocks.web.VoteListener;
import uk.codingbadgers.bsocks.web.WebHandler;

/**
 * The main module class for bSocks.
 */
public class bSocksModule extends Module {
	
	/** The listener thread. */
	private ListenerThread m_listenerThread = null;
	
	/** The listen sock. */
	private ServerSocket m_listenSock = null;
	
	/** The port. */
	private int m_port = 5598;

	/** The m_base website. */
	private String m_baseWebsite = "http://example.com/";
        
        /** A list of valid pex ranks to be used if a user has multiple ranks */
        private List<String> m_validPexRanks = new ArrayList<String>(); 
        
        /** The default rank to use if a player is not within any valid rank */
        private String m_defaultRank = "player";
	
	/** The password. */
	private String m_password = "mypassword";

	/** The instance of the module. */
	private static bSocksModule m_instance = null;
	
	/**
	 * Called when the module is disabled. Here we need to kill the listener thread
	 */
	public void onDisable() {
		m_listenerThread.kill();		
                m_instance = null;
		log(Level.INFO,  "Module Version " + getVersion() + " disabled.");
	}

	/**
	 * Called when the module is loaded. Here we create our listener thread which will
	 * receive socket connections and create handler threads and register all sub listeners
	 * if they are required.
	 */
	public void onEnable() {
		m_instance = this; 
                
                m_validPexRanks.add("player"); 
                m_validPexRanks.add("moderator");
                m_validPexRanks.add("admin");
		                
		loadConfig();
		
		// register plugin event listeners
		if (isPluginEnabled("PermissionsEx")) {
			register(new PermissionsListener());
			log(Level.INFO, "Listening for Permissions Ex events");
		}
		if (isPluginEnabled("FigAdmin")) {
			register(new AdminListener());
			log(Level.INFO, "Listening for FigAdmin events");
		}
		if (isPluginEnabled("Votifier")) {
			register(new VoteListener());
			log(Level.INFO, "Listening for Votifier events");
		}
		
		// setup the listener thread
		try {
			m_listenSock = new ServerSocket(m_port);
			m_listenerThread = new ListenerThread(m_plugin, m_listenSock, m_password, this);
			m_listenerThread.setName(getName() + "-listenerThread");
			m_listenerThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		log(Level.INFO,  "Module Version " + getVersion() + " enabled.");
	}
	
	/**
	 * Checks if a plugin is loaded.
	 *
	 * @param name the name of the plugin
	 * @return true, if is plugin loaded
	 */
	public boolean isPluginEnabled(String name) {
		return m_plugin.getServer().getPluginManager().isPluginEnabled(name) ||
				m_plugin.getServer().getPluginManager().getPlugin(name) != null;
	}
        
        /**
	 * Gets the singleton instance of bSocksModule.
	 *
	 * @return singleton instance of bSocksModule
	 */
	public static bSocksModule getInstance() {
		return m_instance;
	}
	
	/**
	 * Load bSocks configuration, which contains the port number and password.
	 */
	private void loadConfig() {
		try {
			FileConfiguration config = getConfig();
			config.addDefault("port", m_port);	
			config.addDefault("password", m_password);
			config.addDefault("baseWebsite", m_baseWebsite);
                        config.addDefault("pex.validranks", m_validPexRanks);
                        config.addDefault("pex.defaultrank", m_defaultRank);
			config.options().copyDefaults(true);
                        
			m_port = config.getInt("port");
			m_password = config.getString("password");
			m_baseWebsite = config.getString("baseWebsite");
                        m_validPexRanks = config.getStringList("pex.validranks");
                        m_defaultRank = config.getString("pex.defaultrank");
			
			if (!m_baseWebsite.endsWith("/")) {
				m_baseWebsite += "/";
                        }
			
			saveConfig();
			
			log(Level.INFO, "Excepting connections on port : " + m_port);
			log(Level.INFO, "Using external webhost: " + m_baseWebsite);
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Gets a new post web handler based of a script located in the base folder of the
	 * web address given in config.
	 *
	 * @param page the page including extension
	 * @return the new post handler 
	 * @throws MalformedURLException if the base URL is in the wrong format
	 */
	public WebHandler getPostHandler(String page) throws MalformedURLException {
		return new WebHandler(m_baseWebsite + page, m_password, RequestType.POST);
	}
	
	/**
	 * Gets a new get web handler based of a script located in the base folder of the
	 * web address given in config.
	 *
	 * @param page the page including extension
	 * @return the new post handler 
	 * @throws MalformedURLException if the base URL is in the wrong format
	 */
	public WebHandler getGetHandler(String page) throws MalformedURLException {
		return new WebHandler(m_baseWebsite + page, m_password, RequestType.GET);
	}
	
        /**
         * Check to see if a rank is valid
         * @param rank The rank to test
         * @return true if the rank is contained within the config specified ranks
         */
        public boolean isValidRank(String rank) {
            
            for (String validRank : m_validPexRanks) {
                if (validRank.equalsIgnoreCase(rank)) {
                    return true;
                }
            }
            
            return false;
        }

        /**
         * Get the default rank to use if a player does not exist in any valid rank
         * @return The default rank specified from config
         */
        public String getDefaultRank() {
            return m_defaultRank;
        }
}
