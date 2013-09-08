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

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import uk.codingbadgers.bportals.portal.Portal;

import com.google.common.collect.ImmutableSet;
import com.sk89q.worldedit.regions.Region;

import static uk.codingbadgers.bportals.utils.LocationUtils.*;

public class PortalManager {

	private Set<Portal> portals = null;

	public PortalManager() {
		portals = new HashSet<Portal>();
	}

	public Portal getPortalFromLocation(Location loc) {
		for (Portal portal : portals) {
			
			// Check everything
			if (portal.getPortalRegion().contains(convertLocationToVector(loc))) {
				return portal;
			}

			if (portal.getPortalRegion().contains(convertLocationToFloorVector(loc))) {
				return portal;
			}

			if (portal.getPortalRegion().contains(convertLocationToCeilVector(loc))) {
				return portal;
			}
		}

		return null;
	}

	public Portal getPortalById(String string) {
		for (Portal portal : portals) {
			if (portal.getId().equalsIgnoreCase(string)) {
				return portal;
			}
		}

		return null;
	}

	public void createPortal(Region selection, String id, Player sender) {
		Portal portal = new Portal(id);
		portal.setRegion(selection);
		portal.setExitLocation(sender.getLocation().clone());
		registerPortal(portal);
	}

	public void registerPortal(Portal portal) {
		portals.add(portal);
	}

	public boolean isAllowedEntityType(EntityType entityType) {
		return entityType == EntityType.PLAYER;
	}

	public boolean portalExists(String id) {
		return getPortalById(id) != null;
	}

	public boolean isPortal(Location location) {
		return getPortalFromLocation(location) != null;
	}

	void destroy() {
		portals.clear();
		portals = null;
	}

	public Set<Portal> getPortals() {
		return new ImmutableSet.Builder<Portal>().addAll(portals).build();
	}

}
