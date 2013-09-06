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
import org.bukkit.util.NumberConversions;

import com.google.common.collect.ImmutableSet;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.Region;

public class PortalManager {

	private static PortalManager instance = null;

	public static PortalManager getInstance() {
		return instance;
	}

	public static void setInstance(PortalManager instance) {
		PortalManager.instance = instance;
	}

	public static void createPortal(Region selection, String id, Player sender) {
		Portal portal = new Portal(id);
		portal.setRegion(selection);
		portal.setExitLocation(sender.getLocation().clone());
		getInstance().registerPortal(portal);
	}

	private Set<Portal> portals = null;

	public PortalManager() {
		portals = new HashSet<Portal>();
	}

	public Portal getPortalFromLocation(Location loc) {
		for (Portal portal : portals) {
			
			// Check everything
			if (portal.getPortalRegion().contains(new Vector(loc.getX(), 
																loc.getY(),
																loc.getZ()))) {
				bPortals.getInstance().debugConsole("Standard location");
				return portal;
			}
			
			if (portal.getPortalRegion().contains(new Vector(NumberConversions.ceil(loc.getX()), 
																NumberConversions.ceil(loc.getY()),
																NumberConversions.ceil(loc.getZ())))) {
				bPortals.getInstance().debugConsole("Ceil location");
				return portal;
			}
			
			if (portal.getPortalRegion().contains(new Vector(NumberConversions.floor(loc.getX()),
																NumberConversions.floor(loc.getY()),
																NumberConversions.floor(loc.getZ())))) {
				bPortals.getInstance().debugConsole("Floor location");
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
