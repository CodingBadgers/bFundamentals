package uk.codingbadgers.bhuman;

import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.topcat.npclib.NPCManager;

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

/**
 * The base module class for bHuman
 */
public class bHuman extends Module {
	/** The module instance. */
	public static bHuman MODULE = null;
	
	private static NPCManager m_npcManager = null;
	private static DatabaseManager m_dbManager = null;

	public void onLoad() {
		m_npcManager = new NPCManager(m_plugin);
		m_dbManager = new DatabaseManager();	
	}
	
	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.Module#onEnable()
	 */
	@Override
	public void onEnable() {
		MODULE = this;
		
		try {
			m_dbManager.loadDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		registerCommand(new ModuleCommand("npc", "/npc help").setHelp("Access all npc commands"));
		
		register(new PlayerListener());
		
		log(Level.INFO, "bHuman has been enabled");
	}

	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.Module#onDisable()
	 */
	@Override
	public void onDisable() {
		log(Level.INFO, "bHuman has been disabled");
	}
	
	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.Module#onCommand(org.bukkit.entity.Player, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		return CommandHandler.onCommand((Player)sender, label, args);
	}
	
	public static NPCManager getNPCManager() {
		return m_npcManager;
	}
	
	public static DatabaseManager getDBManager() {
		return m_dbManager;
	}

}
