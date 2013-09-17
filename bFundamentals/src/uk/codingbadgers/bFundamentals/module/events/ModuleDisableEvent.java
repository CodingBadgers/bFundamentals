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

import org.bukkit.plugin.Plugin;

import uk.codingbadgers.bFundamentals.module.Module;

/**
 * The ModuleDisableEvent, called when a module is disabled via 
 * {@link Module#setEnabled(boolean)}.
 */
public class ModuleDisableEvent extends ModuleEvent {

	/**
	 * Instantiates a new module disable event.
	 *
	 * @param plugin the plugin
	 * @param loadable the loadable
	 */
	public ModuleDisableEvent(Plugin plugin, Module loadable) {
		super(plugin, loadable);
	}

}
