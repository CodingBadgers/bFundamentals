package uk.codingbadgers.bFundamentals;

import org.bukkit.configuration.file.FileConfiguration;

import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager.DatabaseType;

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
	
	/** Whether to automatically download updates. */
	private boolean m_autoDownload = false;
	
	/** Whether to automatically apply updates. */
	private boolean m_autoApply = false;
	
	/** whether modules should start in debug mode. */
	private boolean m_debug = false;
	
	/** The logger prefix. */
	private String m_logPrefix = null;
	
	/** The database settings. */
	private DatabaseSettings m_databaseSettings = null;

	/**
	 * Load configuration for the plugin.
	 *
	 * @param plugin the main bFundamentals plugin
	 */
	public void loadConfiguration(bFundamentals plugin) {
		FileConfiguration config = plugin.getConfig();
		
		config.addDefault("general.language", "UK");
		config.addDefault("general.debug", false);
		
		config.addDefault("module.update.enabled", false);
		config.addDefault("module.update.download", false);
		config.addDefault("module.update.apply", false);
		
		config.addDefault("module.logging.prefix", "[Module]");
		
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
		m_debug = config.getBoolean("general.debug");
		
		m_autoUpdate = config.getBoolean("module.update.enabled");
		m_autoDownload = config.getBoolean("module.update.download");
		m_autoApply = config.getBoolean("module.update.apply");
		
		m_logPrefix = config.getString("module.logging.prefix");
		
		m_databaseSettings = new DatabaseSettings();
		m_databaseSettings.type = config.getString("database.driver").equalsIgnoreCase("sqlite") ? DatabaseType.SQLite : DatabaseType.SQL;
		m_databaseSettings.host = config.getString("database.host", "localhost");
		m_databaseSettings.name = config.getString("database.name", "bFundamentals");
		m_databaseSettings.prefix = config.getString("database.tablePrefix", "bFundamentals_");
		m_databaseSettings.user = config.getString("database.user", "root");
		m_databaseSettings.password = config.getString("database.password", "");
		m_databaseSettings.port = config.getInt("database.port", 3306);
		m_databaseSettings.updaterate =  config.getInt("database.updateRate", 5);
		
	}
	
	/**
	 * Get the database settings for bFundamentals and all modules
	 * 
	 * @return the database settings
	 */
	public DatabaseSettings getDatabaseSettings() {
		return m_databaseSettings;
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
	 * Gets whether modules should start in debug mode.
	 *
	 * @return true if modules should start in debug, false if not
	 * @deprecated  {@link #isDebugEnabled()}
	 */
	public boolean getDebug() {
		return m_debug;
	}
	
	/**
	 * Gets whether modules should start in debug mode.
	 *
	 * @return true if modules should start in debug, false if not
	 */
	public boolean isDebugEnabled() {
		return m_debug;
	}
	
	/**
	 * Gets whether auto update is enabled.
	 *
	 * @return true if auto update is enabled, false if not
	 * @deprecated {@link #isAutoUpdateEnabled()}
	 */
	public boolean getAutoUpdate() {
		return m_autoUpdate;
	}
	
	/**
	 * Gets whether auto update is enabled.
	 *
	 * @return true if auto update is enabled, false if not
	 */
	public boolean isAutoUpdateEnabled() {
		return m_autoUpdate;
	}
	
	/**
	 * Gets whether auto download for the updater is enabled.
	 *
	 * @return true if auto download is enabled, false if not
	 * @deprecated {@link #isAutoDownloadEnabled()}
	 */
	public boolean getAutoDownload() {
		return m_autoDownload;
	}
	
	/**
	 * Gets whether auto download for the updater is enabled.
	 *
	 * @return true if auto download is enabled, false if not
	 */
	public boolean isAutoDownloadEnabled() {
		return m_autoDownload;
	}
	
	/**
	 * Gets whether auto applying of updates is enabled.
	 *
	 * @return true if auto apply is enabled, false if not
	 * @deprecated {@link #isAutoInstallEnabled()}
	 */
	public boolean getAutoApply() {
		return m_autoApply;
	}
	
	/**
	 * Gets whether auto installing of updates is enabled.
	 *
	 * @return true if auto install is enabled, false if not
	 */
	public boolean isAutoInstallEnabled() {
		return m_autoApply;
	}
	
	/**
	 * Gets the module log prefix.
	 *
	 * @return the log prefix
	 */
	public String getLogPrefix() {
		return m_logPrefix;
	}

}
