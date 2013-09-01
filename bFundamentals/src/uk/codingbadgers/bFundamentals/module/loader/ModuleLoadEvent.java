package uk.codingbadgers.bFundamentals.module.loader;

import java.util.jar.JarFile;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import uk.codingbadgers.bFundamentals.module.Module;

public class ModuleLoadEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();
	private final Plugin plugin;
	private final Module loadable;
	private final JarFile jarFile;
	
	public ModuleLoadEvent(Plugin plugin, Module loadable, JarFile jarFile) {
		this.plugin = plugin;
		this.loadable = loadable;
		this.jarFile = jarFile;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	/**
	 * Gets the JAR file of the loaded loadable
	 * 
	 * @return The JAR file
	 */
	public JarFile getJarFile() {
		return jarFile;
	}
	
	/**
	 * Gets the loaded Module
	 * 
	 * @return The Loadable
	 */
	public Module getModule() {
		return loadable;
	}
	
	/**
	 * Gets the plugin calling this event
	 * 
	 * @return The plugin calling the event
	 */
	public Plugin getPlugin() {
		return plugin;
	}
}