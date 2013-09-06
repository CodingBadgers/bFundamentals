/**
 * bFundamentalsBuild 1.2-SNAPSHOT
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
package uk.codingbadgers.bShortLinks;

import uk.codingbadgers.bFundamentals.module.Module;

/**
 * The Class bShortLinks.
 */
public class bShortLinks extends Module {

	/** The m_player listener. */
	final private PlayerListener m_playerListener = new PlayerListener();

	/**
	 * Called when the module is enabled.
	 */
	public void onEnable() {
		Global.PLUGIN = m_plugin;
		Global.MODULE = this;
		register(m_playerListener);
        
        Global.LoadConfig();
		
		Global.OutputConsole("Module Enabled");
	}
	
	/**
	 * Called when the module is disabled.
	 */
	public void onDisable() {
		Global.OutputConsole("Module Disabled");
	}

}
