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

import org.bukkit.Location;

import com.sk89q.worldedit.regions.Region;

/**
 * The Class Portal.
 */
public class Portal {

	private final String id;
	private Region region;
	private Location exit;
	private Location teleport;
	
	/**
	 * Instantiates a new portal.
	 *
	 * @param id the portal id
	 */
	public Portal(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the portal's id.
	 *
	 * @return the portal's id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Gets the portal region.
	 *
	 * @return the portal region
	 */
	public Region getPortalRegion() {
		return region;
	}

	/**
	 * Sets the region.
	 *
	 * @param selection the new region
	 */
	public void setRegion(Region selection) {
		region = selection;
	}
	
	/**
	 * Gets the exit location, the location a player gets teleported to when a player exit's this portal.
	 *
	 * @return the exit location
	 */
	public Location getExitLocation() {
		return exit;
	}

	/**
	 * Sets the exit location, the location a player gets teleported to when a player exit's this portal.
	 *
	 * @param location the new exit location
	 */
	public void setExitLocation(Location location) {
		exit = location.clone();
	}
	
	/**
	 * Sets the teleport location, the location this portal teleports to.
	 *
	 * @param portal the new teleport location
	 */
	public void setTeleportLocation(Portal portal) {
		teleport = portal.getExitLocation();
	}
	
	/**
	 * Sets the teleport location, the location this portal teleports to.
	 *
	 * @param location the new teleport location
	 */
	public void setTeleportLocation(Location location) {
		teleport = location;
	}
	
	/**
	 * Gets the teleport location, the location this portal teleports to.
	 *
	 * @return the teleport location
	 */
	public Location getTeleportLocation() {
		return teleport;
	}

	/**
	 * Checks for a teleport location, the location this portal teleports to.
	 *
	 * @return true, if this portal has a teleport location set
	 */
	public boolean hasTeleportLocation() {
		return teleport != null;
	}
}
