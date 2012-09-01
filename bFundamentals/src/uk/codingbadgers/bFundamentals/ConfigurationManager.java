package uk.codingbadgers.bFundamentals;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * The Class ConfigurationManager.
 * Handles loading of the configuration and access to
 * each configuration option.
 */
public class ConfigurationManager {
	
	/** The language. */
	private String m_language = "UK";
	
	/** Should we auto update modules. */
	private boolean m_autoUpdate = false;
	
	/** whether modules should start in debug mode */
	private boolean m_debug = false;

	/**
	 * Load configuration for the plugin.
	 *
	 * @param plugin the main bFundamentals plugin
	 */
	public void loadConfiguration(bFundamentals plugin) {
		FileConfiguration config = plugin.getConfig();
		
		config.addDefault("general.language", "UK");
		config.addDefault("general.auto-update", false);
		config.addDefault("general.debug", false);
		
        config.addDefault("database.driver", "SQLite");
        config.addDefault("database.host", "localhost");
        config.addDefault("database.name", "bFundamentals");
        config.addDefault("database.tablePrefix", "bFundamentals_");
        config.addDefault("database.user", "root");
        config.addDefault("database.password", "");
        config.addDefault("database.port", 3306);
        config.addDefault("database.updateRate", 5);
		
		config.options().copyDefaults(true);
		plugin.saveConfig();
		
		m_language = config.getString("general.language");
		m_autoUpdate = config.getBoolean("general.auto-update");
		m_debug = config.getBoolean("general.debug");
	}
	
	/**
	 * Gets the language.
	 *
	 * @return the language
	 */
	public String getLanguage() {
		return m_language;
	}
	
	/**
	 * Gets whether auto update is enabled.
	 *
	 * @return true if auto update is enabled, false if not
	 */
	public boolean getAutoUpdate() {
		return m_autoUpdate;
	}
	
	/**
	 * Gets whether modules should start in debug mode
	 * 
	 * @return true if modules should start in debug, false if not
	 * @return
	 */
	public boolean getDebug() {
		return m_debug;
	}

}
