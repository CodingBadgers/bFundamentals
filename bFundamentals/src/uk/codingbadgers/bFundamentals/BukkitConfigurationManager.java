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

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager.DatabaseType;

/**
 * The Class ConfigurationManager.
 * Handles loading of the configuration and access to
 * each configuration option.
 */
public class BukkitConfigurationManager implements ConfigManager {
	
	protected String m_language = "UK";
	protected boolean m_autoUpdate = false;
	protected boolean m_autoDownload = false;
	protected boolean m_autoApply = false;
	protected boolean m_debug = false;
	protected String m_logPrefix = null;
	protected DatabaseSettings m_databaseSettings = null;
	protected String m_crashPass;
	protected File m_moduleDir;

	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.ConfigManager#loadConfiguration(java.io.File)
	 */
	@Override
	public void loadConfiguration(File configFile) throws IOException {
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		
		config.options().header("bFundamentals configuration");
		
		config.addDefault("general.language", "UK");
		config.addDefault("general.debug", false);
        config.addDefault("general.crash.password", "Password");
		
        config.addDefault("module.loading.dir", "${plugindir}/modules");
        
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

		config.options().copyHeader(true);
		config.options().copyDefaults(true);
		config.save(configFile);
		
		m_language = config.getString("general.language");
		m_debug = config.getBoolean("general.debug");
		m_crashPass = config.getString("general.crash.password", "Password");
		
		m_moduleDir = parseDirectory(config.getString("module.loading.dir"));
		
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
	
	private File parseDirectory(String string) {
		string = string.replace("${plugindir}", bFundamentals.getInstance().getDataFolder().getAbsolutePath());
		string = string.replace("${basedir}", new File(".").getAbsolutePath());
		return new File(string);
	}

	@Override
	public DatabaseSettings getDatabaseSettings() {
		return m_databaseSettings;
	}
	
	@Override
	public String getLanguage() {
		return m_language;
	}
	
	@Override
	public boolean isDebugEnabled() {
		return m_debug;
	}
	
	@Override
	public boolean isAutoUpdateEnabled() {
		return m_autoUpdate;
	}
	
	@Override
	public boolean isAutoDownloadEnabled() {
		return m_autoDownload;
	}
	
	@Override
	public boolean isAutoInstallEnabled() {
		return m_autoApply;
	}
	
	@Override
	public String getLogPrefix() {
		return m_logPrefix;
	}

	@Override
	public String getCrashPassword() {
		return m_crashPass;
	}

	@Override
	public File getModuleDirectory() {
		return m_moduleDir;
	}

}
