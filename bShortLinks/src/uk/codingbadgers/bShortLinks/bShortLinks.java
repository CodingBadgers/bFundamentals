package uk.codingbadgers.bShortLinks;

import uk.codingbadgers.bFundamentals.module.Module;

/**
 * The Class bShortLinks.
 */
public class bShortLinks extends Module {
	
	/**
	 * Instantiates a new b short links.
	 */
	public bShortLinks() {
		super("bShortLinks", "1.0");
	}

	/** The m_player listener. */
	private PlayerListener m_playerListener = new PlayerListener();
	
	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.Module#onEnable()
	 */
	public void onEnable() {
		
		Global.plugin = m_plugin;
		Global.module = this;
		register(m_playerListener);
        
        Global.LoadConfig();
		
		Global.OutputConsole("bShortLinks Enabled");
		
	}
	
	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.Module#onDisable()
	 */
	public void onDisable() {
		
		Global.OutputConsole("bShortLinks Disabled");
		
	}


}
