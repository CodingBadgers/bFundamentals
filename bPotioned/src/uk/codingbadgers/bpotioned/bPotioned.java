package uk.codingbadgers.bpotioned;

import org.bukkit.event.Listener;

import uk.codingbadgers.bFundamentals.module.Module;

public class bPotioned extends Module implements Listener {
	
	/**
	 * Called when the module is disabled.
	 */
	public void onDisable() {
	}

	/**
	 * Called when the module is loaded.
	 */
	public void onEnable() {
		register(this);
		registerCommand(new CommandPotion(this));
	}
}
