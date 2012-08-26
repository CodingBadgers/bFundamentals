package uk.codingbadgers.bconomy.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bconomy.Global;
import uk.codingbadgers.bconomy.account.Account;

import n3wton.me.BukkitDatabaseManager.BukkitDatabaseManager.DatabaseType;

public class DatabaseManager {
	
	private static boolean working = true;
	
	/** 
	 * Sets up the database and loads in all the infomation in the table
	 * 
	 * @param plugin - the java plugin used to setup the database, used to get the offline player
	 */
	public static void setupDatabase(JavaPlugin plugin) {
		
		// creates the database instance
		Global.m_database = bFundamentals.getBukkitDatabase();
		
		// login if sql 
		if (Config.m_dbInfo.driver == DatabaseType.SQL) {
			// logs in using values from the config
			if (!Global.m_database.login(Config.m_dbInfo.host, Config.m_dbInfo.user, Config.m_dbInfo.password, Config.m_dbInfo.port)) {
				working = true;
				return;
			}
		}
		
		if (!Global.m_database.TableExists(Config.m_dbInfo.tablename)) {
			
			Global.outputToConsole("Could not find 'accounts' table, creating default now.");
			
			// creates the accounts table
			String query = "CREATE TABLE " + Config.m_dbInfo.tablename + " (" +
							"id INT," +
							"username VARCHAR(64)," +
							"balance DOUBLE" +
							");";
			
			Global.m_database.Query(query, true);
		}
		
		String query = "SELECT * FROM " + Config.m_dbInfo.tablename;
		ResultSet result = Global.m_database.QueryResult(query);
		
		if (result == null)
			return;
		
		// load in the accounts
		try {
			while(result.next()) {
				int id = result.getInt("id");
				String name = result.getString("username");
				OfflinePlayer player = plugin.getServer().getOfflinePlayer(name);
				double balance = result.getDouble("balance");
				
				// create the account and then add it to the array
				Account account = new Account(id, player, balance);
				Global.addAccout(account);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a account to the database
	 * 
	 * @param account the account to add
	 */
	public static void addAccount(Account account) {
		
		if (!working)
			return;
		
		if (account == null)
			return;
		
		String query = 	"INSERT INTO " + Config.m_dbInfo.tablename + " " +
						"(`id`, `username`, `balance`) VALUES (" +
						"'" + account.getId() + "', " +
						"'" + account.getPlayer().getName() + "', " +
						"'" + account.getBalance() + "');";
				
		Global.m_database.Query(query, true);
		
		Global.outputToConsole("Account " + account.getPlayer().getName() + " has been added to the database.");
	}
	
	/**
	 * Update a account on the database
	 * 
	 * @param account the account to update
	 */
	public static void updateAccount(Account account){
		
		if (!working)
			return;
		
		if (account == null)
			return;
		
		String query = "UPDATE " + Config.m_dbInfo.tablename +
				 " SET balance='" + account.getBalance() + "' " +
				 "WHERE id='" + account.getId() + "';";
		
		Global.m_database.Query(query);
 	}
	
	/**
	 * Remove a account from the database
	 * 
	 * @param account to remove
	 */
	public static void removeAccount(Account account) {
		
		if (!working)
			return;
		
		if (account == null)
			return;
		
		String query = "DELETE FROM " + Config.m_dbInfo.tablename + " WHERE 'id'=" + account.getId() + ";";
		
		Global.m_database.Query(query);
		Global.getAccounts().remove(account);		
		Global.outputToConsole("Removed the account " + account.getPlayer().getName());
	}
	
	/**
	 * Execute a one off query 
	 * 
	 * @param query to execute
	 * @return the results
	 */
	public static ResultSet executeQuery(String query) {
		
		if (!working)
			return null;
		
		return Global.m_database.QueryResult(query);
	}
}
