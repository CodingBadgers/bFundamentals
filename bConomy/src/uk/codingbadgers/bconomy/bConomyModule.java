package uk.codingbadgers.bconomy;

import java.util.logging.Level;

import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bconomy.account.PlayerAccounts;
import uk.codingbadgers.bconomy.config.Config;
import uk.codingbadgers.bconomy.config.DatabaseManager;

public class bConomyModule extends Module {

	/** The Constant NAME of the module. */
	final public static String NAME = "bConomy";
	
	/** The Constant VERSION of the module. */
	final public static String VERSION = "1.00";

	/** The player listener. */
	private final PlayerListener m_playerListener = new PlayerListener();
	
	/**
	 * The bConomy module constructor
	 * passing the name and version to its base class
	 */
	public bConomyModule() {
		super(NAME, VERSION);
	}

	/**
	 * This is called when the module is unloaded
	 */
	@Override
	public void onDisable() {
		log(Level.INFO, NAME + " has been disabled successfuly");
	}

	/**
	 * Called when the module is loaded.
	 */
	@Override
	public void onEnable() {
		Global.setPlugin(m_plugin);
		Global.setModule(this);
		Global.setAccounts(new PlayerAccounts());
		
		loadLanguageFile();
		
		Global.outputToConsole("Initialising bConomy");
		register(m_playerListener);
		
		registerCommand(new ModuleCommand("money", "/money <args>").setHelp("Access all money commands"));

		if (Global.setupPermissions()) {
			Global.outputToConsole(getLanguageValue("VAULT-FOUND"));
		}
		
		// setup config and database
		Config.setupConfig();
		DatabaseManager.setupDatabase(m_plugin);
		
		log(Level.INFO, NAME + " has been enabled successfuly");
	}
	
	/**
	 * Called on command
	 */
	@Override
	public boolean onCommand(Player sender, String commandLabel, String[] args) {
		return CommandManager.onCommand(sender, commandLabel, args);
	}
	
}
