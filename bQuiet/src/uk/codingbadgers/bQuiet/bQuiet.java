/**
 * bQuiet 1.2-SNAPSHOT
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
package uk.codingbadgers.bQuiet;

import java.util.logging.Level;

import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bQuiet.filemanagement.ConfigManager;
import uk.codingbadgers.bQuiet.listeners.bPlayerListener;

public class bQuiet extends Module {
	
	/** The player listener. */
	private bPlayerListener m_playerListener = new bPlayerListener();
	
	/** The module instance. */
	private static bQuiet instance = null;

	/**
	 * Called when the module is enabled.
	 * Register the player listener and setup the configuration
	 */
	public void onEnable() {
		
		Global.setPlugin(m_plugin);
		instance = this;
		
		register(m_playerListener);
		ConfigManager.setupConfig();
		
		log(Level.INFO, "bQuiet enabled");
		
	}
	
	/**
	 * Called when the module is disabled
	 */
	public void onDisable() {
		log(Level.INFO, "bQuiet disabled");
	}

	/**
	 * Get the static module instance
	 */
	public static Module getInstance() {
		return instance;
	}
	
}
