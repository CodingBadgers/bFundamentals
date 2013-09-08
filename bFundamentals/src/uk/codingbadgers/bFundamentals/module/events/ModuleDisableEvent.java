package uk.codingbadgers.bFundamentals.module.events;

import org.bukkit.plugin.Plugin;

import uk.codingbadgers.bFundamentals.module.Module;

public class ModuleDisableEvent extends ModuleEvent {

	public ModuleDisableEvent(Plugin plugin, Module loadable) {
		super(plugin, loadable);
	}

}
