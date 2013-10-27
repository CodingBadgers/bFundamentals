/**
 * bFundamentals 1.2-SNAPSHOT
 * Copyright (C) 2013  CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.codingbadgers.bFundamentals.module.events;

import java.util.jar.JarFile;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import uk.codingbadgers.bFundamentals.module.Module;

/**
 * The ModuleLoadEvent, called when a module is loaded via 
 * {@link ModuleLoader#loadModule(java.io.File)}.
 */
public class ModuleLoadEvent extends ModuleEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	
	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.events.ModuleEvent#getHandlers()
	 */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	/**
	 * Gets the handler list.
	 *
	 * @return the handler list
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	private final JarFile jarFile;

	/**
	 * Instantiates a new module load event.
	 *
	 * @param plugin the plugin
	 * @param loadable the loadable
	 * @param jarFile the jar file
	 */
	public ModuleLoadEvent(Plugin plugin, Module loadable, JarFile jarFile) {
		super(plugin, loadable);
		this.jarFile = jarFile;
	}

	/**
	 * Gets the jar file.
	 *
	 * @return the jar file
	 */
	public JarFile getJarFile() {
		return jarFile;
	}
}