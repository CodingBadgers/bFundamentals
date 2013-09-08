package uk.codingbadgers.bFundamentals.module.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import uk.codingbadgers.bFundamentals.module.Module;

public class ModuleEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	private final Plugin plugin;
	private final Module loadable;

	public ModuleEvent(Plugin plugin, Module loadable) {
		this.plugin = plugin;
		this.loadable = loadable;
	}

	public Module getModule() {
		return loadable;
	}

	public Plugin getPlugin() {
		return plugin;
	}

}
