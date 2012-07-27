package uk.codingbadgers.bsocks;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;

import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bsocks.threading.ListenerThread;

public class bSocksModule extends Module {

	/** The listener thread. */
	private ListenerThread m_listenerThread = null;
	
	/** The listen sock. */
	private ServerSocket m_listenSock = null;
	
	/** The port. */
	private int m_port = 5598;
	
	/** The password. */
	private String m_password = "mypassword";
	
	/**
	 * Instantiates a new bSocksModule.
	 */
	public bSocksModule() {
		super("bSocks", "1.0");
	}
 
	/**
	 * Called when the module is disabled. Here we need to kill the listener thread
	 */
	public void onDisable() {
		m_listenerThread.kill();		
		log(Level.INFO, "Disabled.");
	}

	/**
	 * Called when the module is loaded. Here we create our listener thread which will
	 * receive socket connections and create handler threads.
	 */
	public void onEnable() {
		loadConfig();
		
		try {
			m_listenSock = new ServerSocket(m_port);
			m_listenerThread = new ListenerThread(plugin, m_listenSock, m_password, this);
			m_listenerThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		log(Level.INFO, "Enabled.");
	}
	
	/**
	 * Load bSocks configuration, which contains the port number and password.
	 */
	private void loadConfig() {
		try {
			FileConfiguration config = getConfig();
			config.addDefault("port", "5598");	
			config.addDefault("password", "mypassword");
			config.options().copyDefaults(true);
			m_port = config.getInt("port");
			m_password = config.getString("password");
			saveConfig();
			
			log(Level.INFO, "Excepting connections on port : " + m_port);
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}
