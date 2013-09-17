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
package uk.codingbadgers.bFundamentals.module.loader;

import java.net.URL;
import java.util.Set;

import org.bukkit.event.Listener;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bFundamentals.module.ModuleClassLoader;

public class ClassFinder {

	public static Set<Class<? extends Module>> findModules(ModuleClassLoader loader, URL[] url) {
		Reflections reflect = new Reflections(new ConfigurationBuilder().addUrls(url).addClassLoader(loader).addClassLoader(ClassFinder.class.getClassLoader()));
		return reflect.getSubTypesOf(Module.class);
	}
	
	public static Set<Class<? extends Listener>> findListeners(ModuleClassLoader loader, URL[] url) {
		Reflections reflect = new Reflections(new ConfigurationBuilder().addUrls(url).addClassLoader(loader).addClassLoader(ClassFinder.class.getClassLoader()));
		return reflect.getSubTypesOf(Listener.class);
	}

}