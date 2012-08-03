package uk.codingbadgers.bQuiet;

import java.util.logging.Level;

import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bQuiet.filemanagement.bConfig;
import uk.codingbadgers.bQuiet.listeners.bPlayerListener;

public class bQuiet extends Module {

	public bQuiet() {
		super("bQuiet", "1.0");
	}
	
	private bPlayerListener m_playerListener = new bPlayerListener();
	private static bQuiet instance;
	
	public void onEnable() {
		
		bGlobal.setPlugin(plugin);
		instance = this;
		
		register(m_playerListener);
		bConfig.setupConfig();
		
		log(Level.INFO, "bQuiet enabled");
		
	}
	
	public void onDisable() {
		log(Level.INFO, "bQuiet disabled");
	}

	public static Module getInstance() {
		return instance;
	}
	
}
