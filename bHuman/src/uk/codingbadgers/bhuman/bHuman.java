package uk.codingbadgers.bhuman;

import java.util.logging.Level;

import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

/**
 * The base module class for bHuman
 */
public class bHuman extends Module {

	/** The Constant NAME. */
	private static final String NAME = "bHuman";
	
	/** The Constant VERSION. */
	private static final String VERSION = "1.0";
	
	/** The module instance. */
	public static bHuman MODULE = null;
	
	/**
	 * Instantiates a new bHuman module.
	 */
	public bHuman() {
		super(NAME, VERSION);
	}

	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.Module#onEnable()
	 */
	@Override
	public void onEnable() {
		MODULE = this;
		
		registerCommand(new ModuleCommand("bHuman", "/bHuman"));
		
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
	public boolean onCommand(Player sender, String label, String[] args) {
		return false;
	}

}
