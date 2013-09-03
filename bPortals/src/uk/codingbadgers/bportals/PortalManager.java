package uk.codingbadgers.bportals;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

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
			if (portal.getPortalRegion().contains(new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))) {
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
