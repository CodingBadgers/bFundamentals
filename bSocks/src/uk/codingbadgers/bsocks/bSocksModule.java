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
package uk.codingbadgers.bsocks;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
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
	private static String m_baseWebsite = "http://example.com/";
	
	/** The password. */
	private static String m_password = "mypassword";

	/** The instance of the module. */
	private static bSocksModule m_instance = null;
	
	/**
	 * Called when the module is disabled. Here we need to kill the listener thread
	 */
	public void onDisable() {
		m_listenerThread.kill();		
		log(Level.INFO,  "Module Version " + getVersion() + " disabled.");
	}

	/**
	 * Called when the module is loaded. Here we create our listener thread which will
	 * receive socket connections and create handler threads and register all sub listeners
	 * if they are required.
	 */
	public void onEnable() {
		m_instance = this;
		
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
	 * Load bSocks configuration, which contains the port number and password.
	 */
	private void loadConfig() {
		try {
			FileConfiguration config = getConfig();
			config.addDefault("port", 5598);	
			config.addDefault("password", "mypassword");
			config.addDefault("baseWebsite", "http://example.com/");
			config.options().copyDefaults(true);
			m_port = config.getInt("port");
			m_password = config.getString("password");
			m_baseWebsite = config.getString("baseWebsite");
			
			if (!m_baseWebsite.endsWith("/"))
				m_baseWebsite += "/";
			
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
	public static WebHandler getPostHandler(String page) throws MalformedURLException {
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
	public static WebHandler getGetHandler(String page) throws MalformedURLException {
		return new WebHandler(m_baseWebsite + page, m_password, RequestType.GET);
	}
	
	/**
	 * Gets the single instance of bSocksModule.
	 *
	 * @return single instance of bSocksModule
	 */
	public static bSocksModule getInstance() {
		return m_instance;
	}
	

}
