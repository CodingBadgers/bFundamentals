package uk.codingbadgers.bFundamentals;

import java.io.File;
import java.io.IOException;

public interface ConfigManager {

	/**
	 * Load configuration for the plugin.
	 *
	 * @param plugin the main bFundamentals plugin
	 * @throws IOException 
	 */
	public abstract void loadConfiguration(File configFile) throws IOException;

	/**
	 * Get the database settings for bFundamentals and all modules
	 * 
	 * @return the database settings
	 */
	public abstract DatabaseSettings getDatabaseSettings();

	/**
	 * Gets the language.
	 *
	 * @return the language
	 */
	public abstract String getLanguage();

	/**
	 * Gets whether modules should start in debug mode.
	 *
	 * @return true if modules should start in debug, false if not
	 */
	public abstract boolean isDebugEnabled();

	/**
	 * Gets whether auto update is enabled.
	 *
	 * @return true if auto update is enabled, false if not
	 */
	public abstract boolean isAutoUpdateEnabled();

	/**
	 * Gets whether auto download for the updater is enabled.
	 *
	 * @return true if auto download is enabled, false if not
	 */
	public abstract boolean isAutoDownloadEnabled();

	/**
	 * Gets whether auto installing of updates is enabled.
	 *
	 * @return true if auto install is enabled, false if not
	 */
	public abstract boolean isAutoInstallEnabled();

	/**
	 * Gets the module log prefix.
	 *
	 * @return the log prefix
	 */
	public abstract String getLogPrefix();

	/**
	 * Gets the password for the crash reporting.
	 *
	 * @return the password for the crash reporting.
	 */
	public abstract String getCrashPassword();

}