/**
 * bPortals 1.2-SNAPSHOT
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
package uk.codingbadgers.bportals;

import java.util.logging.Level;

import org.bukkit.Bukkit;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bportals.commands.PortalCommand;
import uk.codingbadgers.bportals.listeners.PortalListener;

public class bPortals extends Module {

	private static bPortals instance;
	private static WorldEditPlugin worldedit;
	
	private static PortalManager portalManager;
	private static DatabaseManager dbManager;

	/**
	 * Called when the module is disabled.
	 */
	public void onDisable() {
		portalManager.destroy();
		dbManager.destroy();

		portalManager = null;
		dbManager = null;
		
		worldedit = null;
		instance = null;
	}

	/**
	 * Called when the module is loaded.
	 */
	public void onEnable() {
		instance = this;
		
		if (!Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
			setEnabled(false);
			log(Level.SEVERE, "Could not find worldedit, disabling plugin.");
			return;
		}
		
		worldedit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		
		portalManager = new PortalManager();
		dbManager = new DatabaseManager(m_database);
		
		register(new PortalListener());
		registerCommand(new PortalCommand());
	}

	public static bPortals getInstance() {
		return instance;
	}
	
	public static WorldEditPlugin getWorldEdit() {
		return worldedit;
	}
	
	public static DatabaseManager getDatabaseManager() {
		return dbManager;
	}
	
	public static PortalManager getPortalManager() {
		return portalManager;
	}
	
}
