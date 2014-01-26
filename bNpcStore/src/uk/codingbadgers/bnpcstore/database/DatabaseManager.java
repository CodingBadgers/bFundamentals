package uk.codingbadgers.bnpcstore.database;

import java.io.File;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bnpcstore.bNpcStore;
import uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase;
import uk.thecodingbadgers.bDatabaseManager.DatabaseTable.DatabaseTable;
import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager;
import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager.DatabaseType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author N3wton
 */
public class DatabaseManager {
    
    BukkitDatabase databaseStoreNPC;
    
    private DatabaseTable npcTable;
    
    private class LoginDetails {
        public String databaseName;
        public String host;
        public String user;
        public String password;
        public int port; 
    }
    
    private LoginDetails loginDetails;
    
    public DatabaseManager(JavaPlugin plugin) {
        
        loadConfig();
        
        databaseStoreNPC = bDatabaseManager.createDatabase(loginDetails.databaseName, plugin, DatabaseType.SQL);        
		if (databaseStoreNPC == null || !databaseStoreNPC.login(loginDetails.host, loginDetails.user, loginDetails.password, loginDetails.port)) {
			bFundamentals.log(Level.SEVERE, "Failed to setup store npc database!");
			return;
		}
        
        npcTable = databaseStoreNPC.createTable("bNpcStore-Npcs", NPCData.class);
		if (npcTable == null) {
			bFundamentals.log(Level.SEVERE, "Failed to setup npcs table!");
			return;
		}
        
    }
    
    private void loadConfig() {
        
        this.loginDetails = new LoginDetails();
        
        bNpcStore module = bNpcStore.getInstance();
        File configFile = new File(module.getDataFolder() + File.separator + "database.yml");
        
        if (!configFile.exists()) {
            
            try {
                configFile.createNewFile();
                
                FileConfiguration file = YamlConfiguration.loadConfiguration(configFile);
                file.set("database.name", "MyDatabase");
                file.set("database.host", "127.0.0.1");
                file.set("database.user", "username");
                file.set("database.password", "Pa55word");
                file.set("database.port", 3306);
                file.save(configFile);                
            } catch (Exception ex) {
                bFundamentals.log(Level.SEVERE, "Failed to create database config file", ex);
                return;
            }
            
        }
        
        FileConfiguration file = YamlConfiguration.loadConfiguration(configFile);
        
        this.loginDetails.databaseName = file.getString("database.name");
        this.loginDetails.host = file.getString("database.host");        
        this.loginDetails.user = file.getString("database.user");
        this.loginDetails.password = file.getString("database.password");
        this.loginDetails.port = file.getInt("database.port");
        
    }

    public DatabaseTable getNpcStoreTable() {
        return this.npcTable;
    }
    
    public BukkitDatabase getDatabase() {
        return this.databaseStoreNPC;
    }
    
}
