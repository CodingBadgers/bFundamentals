package uk.codingbadgers.bplugincontrol;

import org.bukkit.event.Listener;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bplugincontrol.commands.CommandPlugin;

public class bPluginControl extends Module implements Listener {

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
		registerCommand(new CommandPlugin());
	}
}
