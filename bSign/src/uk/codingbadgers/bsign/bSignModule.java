package uk.codingbadgers.bsign;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import n3wton.me.BukkitDatabaseManager.Database.BukkitDatabase;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bsign.listener.BlockListener;
import uk.codingbadgers.bsign.listener.PlayerListener;
import uk.codingbadgers.bsign.listener.WorldListener;
import uk.codingbadgers.bsign.sign.CommandSign;
import uk.codingbadgers.bsign.sign.InfoSign;
import uk.codingbadgers.bsign.sign.Sign;
import uk.codingbadgers.bsign.sign.WebSign;

/**
 * The Class bSignModule.
 * Main entry point to the module
 */
public class bSignModule extends Module {
	
	/** The Constant NAME of the module. */
	final public static String NAME = "bSign";
	
	/** The Constant VERSION of the module. */
	final public static String VERSION = "1.10";
	
	/** The player listener. */
	PlayerListener m_playerListener = new PlayerListener();
	
	/** The block listener. */
	BlockListener m_blockListener = new BlockListener();
	
	/** The world listener. */
	WorldListener m_worldListener = new WorldListener();
	
	/** Access to the bukkit plugin. */
	public static Plugin PLUGIN = null;
	
	/** Access to the module. */
	public static bSignModule MODULE = null;
	
	/** A list of all the bSigns */
	public static ArrayList<Sign> SIGNS = new ArrayList<Sign>();
	
	/** Access to the bFundamental database. */
	public static BukkitDatabase DATABASE = null;
	
	/**
	 * The bSign module constructor
	 * passing the name and version to its base class
	 */
	public bSignModule() {
		super(NAME, VERSION);
	}

	/**
	 * This is called when the module is unloaded
	 */
	@Override
	public void onDisable() {
		SIGNS.clear();
		log(Level.INFO,  "Module Version " + VERSION + " disabled.");
	}

	/**
	 * Called when the module is loaded.
	 * Allowing us to register the player and block listeners
	 */
	@Override
	public void onEnable() {
		
		MODULE = this;
		PLUGIN = m_plugin;
		DATABASE = m_database;
		loadLanguageFile();

		if (!DATABASE.TableExists("bSign")) {
			// the bSign table doesn't exist, create one
			String createTable = "CREATE TABLE bSign " +
				"(" +
				"Type varchar(32)," +
				"Context varchar(255)," +
				"Creator varchar(32)," +
				"Location varchar(1024)" +
				")";
			DATABASE.Query(createTable, true);
		}
		else
		{
			LoadSignsFromDatabase(null);
		}
		
		register(m_playerListener);
		register(m_blockListener);	
		register(m_worldListener);
		
		log(Level.INFO,  "Module Version " + VERSION + " enabled.");
	}

	public static void LoadSignsFromDatabase(String worldName) {
		
		int noofSignsInWorld = 0;
		
		// load the signs from the table
		String selectAllSigns = "Select * FROM bSign";
		ResultSet result = bSignModule.DATABASE.QueryResult(selectAllSigns);
		
		try {
			while (result.next()) {
				String signType = result.getString("Type");
				String signContext = result.getString("Context");
				String ownerName = result.getString("Creator");
				String locationString = result.getString("Location");
				
				OfflinePlayer owner = bSignModule.PLUGIN.getServer().getOfflinePlayer(ownerName);
				Location location = getLocationFromString(locationString, worldName);
				if (location == null)
					continue;
				
				Sign loadedSign = null;
				if (signType.equalsIgnoreCase("web")) {
					loadedSign = new WebSign(owner, location);
				} else if (signType.equalsIgnoreCase("command")) {
					loadedSign = new CommandSign(owner, location);
				} else if (signType.equalsIgnoreCase("info")) {
					loadedSign = new InfoSign(owner, location);
				} else {
					bSignModule.MODULE.log(Level.WARNING, "Unknown sign type " + signType);
					continue;
				}
				loadedSign.init(signContext);
				bSignModule.SIGNS.add(loadedSign);
				noofSignsInWorld++;
			}
		} catch (SQLException e) {
			bSignModule.MODULE.log(Level.SEVERE, "Failed to load signs, Database may be corrupt!");
		}
		
		if (worldName != null)
			bSignModule.MODULE.log(Level.INFO, "Loaded " + noofSignsInWorld + " signs in world " + worldName + ".");
		else
			bSignModule.MODULE.log(Level.INFO, "Loaded " + noofSignsInWorld + " signs.");
	}
	
	
	/**
	 * @param locationString A string of a location loaded from the database
	 * @return A location from a formatted string
	 */
	private static Location getLocationFromString(String locationString, String worldName) {
		
		double locX = Double.parseDouble(locationString.substring(0, locationString.indexOf(",") - 1));
		locationString = locationString.substring(locationString.indexOf(",") + 1);
		
		double locY = Double.parseDouble(locationString.substring(0, locationString.indexOf(",") - 1));
		locationString = locationString.substring(locationString.indexOf(",") + 1);

		double locZ = Double.parseDouble(locationString.substring(0, locationString.indexOf(",") - 1));
		locationString = locationString.substring(locationString.indexOf(",") + 1);
		
		World locWorld = PLUGIN.getServer().getWorld(locationString);
		
		if (locWorld != null && worldName != null && locWorld.getName() != worldName)
			return null;

		return new Location(locWorld, locX, locY, locZ);
	}
}
