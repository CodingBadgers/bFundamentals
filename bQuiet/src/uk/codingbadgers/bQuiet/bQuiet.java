package uk.codingbadgers.bQuiet;

import java.util.logging.Level;

import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bQuiet.filemanagement.ConfigManager;
import uk.codingbadgers.bQuiet.listeners.bPlayerListener;

public class bQuiet extends Module {

	/** The Constant NAME. */
	private static final String NAME = "bQuiet";
	
	/** The Constant VERSION. */
	private static final String VERSION = "1.0";
	
	public bQuiet() {
		super(NAME, VERSION);
	}
	
	private bPlayerListener m_playerListener = new bPlayerListener();
	private static bQuiet instance;
	
	public void onEnable() {
		
		Global.setPlugin(m_plugin);
		instance = this;
		
		register(m_playerListener);
		ConfigManager.setupConfig();
		
		log(Level.INFO, "bQuiet enabled");
		
	}
	
	public void onDisable() {
		log(Level.INFO, "bQuiet disabled");
	}

	public static Module getInstance() {
		return instance;
	}
	
}
