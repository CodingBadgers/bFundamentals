package uk.codingbadgers.bShortLinks;

import uk.codingbadgers.bFundamentals.module.Module;

/**
 * The Class bShortLinks.
 */
public class bShortLinks extends Module {
	
	/** The name of the module. */
	public static String NAME = "bShortLinks";
	
	/** The version of the module. */
	public static String VERSION = "1.0";
	
	/** The m_player listener. */
	final private PlayerListener m_playerListener = new PlayerListener();
	
	/**
	 * Instantiates a new bShortLinks.
	 */
	public bShortLinks() {
		super(NAME, VERSION);
	}
	
	/**
	 * Called when the module is enabled.
	 */
	public void onEnable() {
		Global.PLUGIN = m_plugin;
		Global.MODULE = this;
		register(m_playerListener);
        
        Global.LoadConfig();
		
		Global.OutputConsole("Module Enabled");
	}
	
	/**
	 * Called when the module is disabled.
	 */
	public void onDisable() {
		Global.OutputConsole("Module Disabled");
	}

}
