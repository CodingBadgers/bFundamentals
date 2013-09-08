package uk.codingbadgers.bFundamentals.module.events;

import org.bukkit.plugin.Plugin;

import uk.codingbadgers.bFundamentals.module.Module;

public class ModuleEnableEvent extends ModuleEvent {

	public ModuleEnableEvent(Plugin plugin, Module loadable) {
		super(plugin, loadable);
	}

}
