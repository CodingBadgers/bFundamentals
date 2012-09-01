package uk.codingbadgers.bmonitor;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import n3wton.me.BukkitDatabaseManager.Database.BukkitDatabase;
import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bmonitor.CommandListener.ChatMessage;
import uk.codingbadgers.bmonitor.CommandListener.Command;

public class DatabaseManager {

	private static BukkitDatabase m_database;
	
	public static void setupDatabase(JavaPlugin plugin) {
		
		m_database = bFundamentals.getBukkitDatabase();
				
		if (!m_database.TableExists(bMonitor.m_dbInfo.m_tablePrefix + "commands")) {
			String query = "CREATE TABLE " + bMonitor.m_dbInfo.m_tablePrefix + "commands (" +
							"date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
							"player VARCHAR(64)," +
							"command VARCHAR(64)," +
							"args VARCHAR(128));";
			m_database.Query(query, true);
		}
		
		if (!m_database.TableExists(bMonitor.m_dbInfo.m_tablePrefix + "chat")) {
			String query = "CREATE TABLE " + bMonitor.m_dbInfo.m_tablePrefix + "chat (" +
							"date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
							"player VARCHAR (64)," +
							"message VARCHAR (128));";
			m_database.Query(query, true);
		}
		
		if (!m_database.TableExists(bMonitor.m_dbInfo.m_tablePrefix + "join")) {
			String query = "CREATE TABLE " + bMonitor.m_dbInfo.m_tablePrefix + "join (" +
							"date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
							"player VARCHAR (64)," +
							"ip VARCHAR (11));";
			m_database.Query(query, true);
		}
		
		if (!m_database.TableExists(bMonitor.m_dbInfo.m_tablePrefix + "quit")) {
			String query = "CREATE TABLE " + bMonitor.m_dbInfo.m_tablePrefix + "quit (" +
							"date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
							"player VARCHAR (64)," +
							"ip VARCHAR (11));";
			m_database.Query(query, true);
		}
	}
	
	public static void log(Command command) {
		if (!bMonitor.m_logging.m_commands)
			return;
		
		String query = "INSERT INTO " + bMonitor.m_dbInfo.m_tablePrefix + "commands ('player', 'command', 'args')" +
						"VALUES ('" + command.m_player + "', " +
								"'" + command.m_command + "', " +
								"'" + command.m_args + "');";
		m_database.Query(query);
	}
	
	public static void log(ChatMessage message) {
		if (!bMonitor.m_logging.m_chat)
			return;
		
		String query = "INSERT INTO " + bMonitor.m_dbInfo.m_tablePrefix + "chat ('player', 'message')" +
						"VALUES ('" + message.m_player + "', " +
								"'" + message.m_message + "');";
		
		m_database.Query(query);		
	}

	public static void logQuit(Player player) {
		if (!bMonitor.m_logging.m_playerLeave)
			return;
		
		String query = "INSERT INTO " + bMonitor.m_dbInfo.m_tablePrefix + "join ('player', 'ip')" +
				"VALUES ('" + player.getName() + "', " +
						"'" + player.getAddress().getAddress().getHostAddress() + "');";

			m_database.Query(query);	
	}

	public static void logJoin(Player player) {
		if (!bMonitor.m_logging.m_playerJoin)
			return;
		
		String query = "INSERT INTO " + bMonitor.m_dbInfo.m_tablePrefix + "quit ('player', 'ip')" +
				"VALUES ('" + player.getName() + "', " +
						"'" + player.getAddress().getAddress().getHostAddress() + "');";

			m_database.Query(query);	
	}
	
}
