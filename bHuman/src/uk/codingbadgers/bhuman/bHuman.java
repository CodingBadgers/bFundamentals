package uk.codingbadgers.bhuman;

import java.util.logging.Level;

import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

public class bHuman extends Module {

	private static final String NAME = "bHuman";
	private static final String VERSION = "1.0";
	
	public static bHuman MODULE = null;
	
	public bHuman() {
		super(NAME, VERSION);
	}

	@Override
	public void onEnable() {
		MODULE = this;
		
		registerCommand(new ModuleCommand("bHuman", "/bHuman"));
		
		log(Level.INFO, "bHuman has been enabled");
	}

	@Override
	public void onDisable() {
		log(Level.INFO, "bHuman has been disabled");
	}
	
	public boolean onCommand(Player sender, String label, String[] args) {
		return false;
	}

}
