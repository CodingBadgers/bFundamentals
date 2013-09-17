/**
 * bFundamentals 1.2-SNAPSHOT
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

	/**
	 * Gets the current module directory.
	 *
	 * @return the module directory
	 */
	public abstract File getModuleDirectory();

}