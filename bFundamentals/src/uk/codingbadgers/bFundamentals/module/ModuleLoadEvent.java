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
package uk.codingbadgers.bFundamentals.module;

import java.util.jar.JarFile;

import org.bukkit.plugin.Plugin;

import uk.codingbadgers.bFundamentals.module.loader.LoadEvent;

public class ModuleLoadEvent extends LoadEvent<Module> {

	public ModuleLoadEvent(Plugin plugin, Module loadable, JarFile jarFile) {
		super(plugin, loadable, jarFile);
	}

}
