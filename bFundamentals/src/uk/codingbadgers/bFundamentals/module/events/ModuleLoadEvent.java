package uk.codingbadgers.bFundamentals.module.events;

import java.util.jar.JarFile;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import uk.codingbadgers.bFundamentals.module.Module;

public class ModuleLoadEvent extends ModuleEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	private final JarFile jarFile;

	public ModuleLoadEvent(Plugin plugin, Module loadable, JarFile jarFile) {
		super(plugin, loadable);
		this.jarFile = jarFile;
	}

	public JarFile getJarFile() {
		return jarFile;
	}
}