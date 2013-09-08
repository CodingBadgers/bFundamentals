/**
 * bFundamentalsBuild 1.2-SNAPSHOT
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
package uk.codingbadgers.bmonitor;

import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;

import n3wton.me.BukkitDatabaseManager.BukkitDatabaseManager.DatabaseType;

import uk.codingbadgers.bFundamentals.module.Module;

// TODO: Auto-generated Javadoc
/**
 * The base bMonitor class.
 */
public class bMonitor extends Module {
	
	/** The Constant NAME. */
	private static final String NAME = "bMonitor";
	
	/** The Constant VERSION. */
	private static final String VERSION = "1.0";
	
	/** The m_player listener. */
	private CommandListener m_playerListener = new CommandListener();
	
	/** The instance. */
	public static bMonitor INSTANCE;
	
	/**
	 * The Class DatabaseInfo.
	 */
	public static class DatabaseInfo {
		
		/** The database driver. */
		public DatabaseType m_driver = null;
		
		/** The the hostname. */
		public String m_host = null;
		
		/** The teh database name. */
		public String m_dbname = null;
		
		/** The table prefix. */
		public String m_tablePrefix = null;
		
		/** The database username. */
		public String m_user = null;
		
		/** The the database password. */
		public String m_password = null;
		
		/** The database port. */
		public int m_port = 3306;
		
		/** The update time. */
		public int m_update = 20;
	}
	
	/**
	 * The Class Logging.
	 */
	public static class Logging {
		
		/** Logging player join. */
		public boolean m_playerJoin;
		
		/** Logging player leave. */
		public boolean m_playerLeave;
		
		/** Logging chat. */
		public boolean m_chat;
		
		/** Logging commands. */
		public boolean m_commands;		
	}
	
	/** The Database info instance */
	public static DatabaseInfo m_dbInfo;
	
	/** The Logging instance. */
	public static Logging m_logging;
	
	/**
	 * Instantiates a new b monitor.
	 */
	public bMonitor() {
		super(NAME, VERSION);
	}
	
	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.Module#onEnable()
	 */
	public void onEnable() {
		
		INSTANCE = this;
		
		register(m_playerListener);
		
		setupConfig();
		DatabaseManager.setupDatabase(m_plugin);
		
		log(Level.INFO, "bMonitor enabled");
	}
	
	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.Module#onDisable()
	 */
	public void onDisable() {
		log(Level.INFO, "bMonitor disabled");
	}
	
	/**
	 * Setup config.
	 */
	public void setupConfig() {
		
		FileConfiguration config = getConfig();
		
		try {
			// database config
			config.addDefault("database.driver", "SQLite");
			config.addDefault("database.host", "localhost");
			config.addDefault("database.dbname", "bMonitor");
			config.addDefault("database.tablePrefix", "bMonitor_");
			config.addDefault("database.user", "root");
			config.addDefault("database.password", "");
			config.addDefault("database.port", 3306);
			config.addDefault("database.updateTime", 2);
			
			config.addDefault("logging.player.join", true);
			config.addDefault("logging.player.leave", true);
			config.addDefault("logging.chat", true);
			config.addDefault("logging.commands", true);
			
			config.options().copyDefaults(true);
			saveConfig();
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		
		m_dbInfo = new DatabaseInfo();
		m_dbInfo.m_driver = DatabaseType.valueOf(config.getString("database.driver", "SQLite"));
		m_dbInfo.m_host = config.getString("database.host", "localhost");
		m_dbInfo.m_dbname = config.getString("database.dbname", "bMonitor");
		m_dbInfo.m_tablePrefix = config.getString("database.tablePrefix", "bMonitor_");
		m_dbInfo.m_user = config.getString("database.user", "root");
		m_dbInfo.m_password = config.getString("database.password", "");
		m_dbInfo.m_port = config.getInt("database.port", 3306);
		m_dbInfo.m_update = config.getInt("database.updateTime", 2);
		
		m_logging = new Logging();
		m_logging.m_playerJoin = config.getBoolean("logging.player.join", true);
		m_logging.m_playerLeave = config.getBoolean("logging.player.leave", true);
		m_logging.m_chat = config.getBoolean("logging.chat", true);
		m_logging.m_commands = config.getBoolean("logging.commands", true);
		
	}
}
