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
package uk.codingbadgers.bportals.portal;

import org.bukkit.Location;

import com.sk89q.worldedit.regions.Region;

/**
 * A class representing a portal that cannot be modified, useful for storing a
 * portal's state for future use.
 */
public class ImmutablePortal extends Portal {

	/**
	 * Instantiates a new immutable portal.
	 *
	 * @param portal the portal
	 */
	public ImmutablePortal(Portal portal) {
		super(portal.getId());
	}
	
	@Override
	public void setRegion(Region selection) {
		throw new UnsupportedOperationException("Cannot modify a immutable portal.");
	}

	@Override
	public void setExitLocation(Location location) {
		throw new UnsupportedOperationException("Cannot modify a immutable portal.");
	}

	@Override
	public void setTeleportLocation(Portal portal) {
		throw new UnsupportedOperationException("Cannot modify a immutable portal.");
	}

	@Override
	public void setTeleportLocation(Location location) {
		throw new UnsupportedOperationException("Cannot modify a immutable portal.");
	}

}
