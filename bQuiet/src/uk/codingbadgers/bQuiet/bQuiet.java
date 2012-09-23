package uk.codingbadgers.bQuiet;

import java.util.logging.Level;

import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bQuiet.filemanagement.ConfigManager;
import uk.codingbadgers.bQuiet.listeners.bPlayerListener;

public class bQuiet extends Module {
	
	/** The player listener. */
	private bPlayerListener m_playerListener = new bPlayerListener();
	
	/** The module instance. */
	private static bQuiet instance = null;

	/**
	 * Called when the module is enabled.
	 * Register the player listener and setup the configuration
	 */
	public void onEnable() {
		
		Global.setPlugin(m_plugin);
		instance = this;
		
		register(m_playerListener);
		ConfigManager.setupConfig();
		
		log(Level.INFO, "bQuiet enabled");
		
	}
	
	/**
	 * Called when the module is disabled
	 */
	public void onDisable() {
		log(Level.INFO, "bQuiet disabled");
	}

	/**
	 * Get the static module instance
	 */
	public static Module getInstance() {
		return instance;
	}
	
}
