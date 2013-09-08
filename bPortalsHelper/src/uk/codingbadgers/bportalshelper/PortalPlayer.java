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
package uk.codingbadgers.bportalshelper;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class PortalPlayer {
	
	/** The bukkit player object. */
	private final Player m_player;
	
	/** The portals being linked. */
	private Portal[] m_portals = new Portal[2];
	
	/** The current portal being set, either 1 or 2. */
	private int m_activePortal = 1;
	
	/**
	 * Create a new portal player, storing the bukkit player object
	 * 
	 * @param player The bukkit player this portal player represents
	 */
	public PortalPlayer(Player player) {
		m_player = player;
		m_portals[0] = null;
		m_portals[1] = null;
	}
	
	/**
	 * Compare a player and a portal player to see if they are equal
	 * 
	 * @param obj An object to compare
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof Player))
			return false;
		
		Player player = (Player)obj;
		if (m_player.getName().equalsIgnoreCase(player.getName()))
			return true;
		
		return false;
	}

	/**
	 * Adds a portal.
	 *
	 * @param portalSelection The portal selection
	 * @param direction The direction
	 */
	public void addPortal(Selection portalSelection) {
		
		final ProtectedRegion region = getChildRegionFromLocation(m_player);
		if (region == null) {
			bPortalsHelper.sendMessage("bPortalsHelper", m_player, "Please region this area before creating a portal too it.");	
			return;
		}
		
		m_portals[m_activePortal - 1] = new Portal(m_player, region.getId(), portalSelection);
		
		bPortalsHelper.sendMessage("bPortalsHelper", m_player, "Selected Portal " + m_activePortal + ".");	
		
		// change to the other portal
		if (m_activePortal == 1) m_activePortal = 2; else m_activePortal = 1;
		
		// see if we have two portals set
		if (m_portals[0] == null || m_portals[1] == null)
			return;
		
		// link the two portals
		final String portalOneName = m_portals[0].getWorld() + "_" + m_portals[0].getRegionName() + "_" + m_portals[1].getRegionName();
		final String portalTwoName = m_portals[1].getWorld() + "_" + m_portals[1].getRegionName() + "_" + m_portals[0].getRegionName();
		
		bPortalsHelper.sendMessage("bPortalsHelper", m_player, "Creating portals...");
		
		if (!m_portals[0].createPortal(portalOneName)) {
			bPortalsHelper.sendMessage("bPortalsHelper", m_player, "Failed to create portal " + portalOneName + ".");		
			m_portals[0] = null;
			m_portals[1] = null;			
			return;
		}
		bPortalsHelper.sendMessage("bPortalsHelper", m_player, "Created first portal...");
		
		if (!m_portals[1].createPortal(portalTwoName)) {
			bPortalsHelper.sendMessage("bPortalsHelper", m_player, "Failed to create portal " + portalTwoName + ".");		
			m_portals[0] = null;
			m_portals[1] = null;			
			return;
		}
		bPortalsHelper.sendMessage("bPortalsHelper", m_player, "Created second portal...");
		
		bPortalsHelper.sendMessage("bPortalsHelper", m_player, "Linking portals...");
		
		m_portals[0].setDestination(m_portals[1]);
		m_portals[1].setDestination(m_portals[0]);
		
		bPortalsHelper.sendMessage("bPortalsHelper", m_player, "Portals created and linked!");		
		
		m_portals[0] = null;
		m_portals[1] = null;
	}
	
	private ProtectedRegion getChildRegionFromLocation(Player player) {
		
		final Location location = player.getLocation();
		final World world = player.getWorld();
		WorldGuardPlugin wg = bPortalsHelper.getWorldGuard();
		
		ArrayList<ProtectedRegion> possibleRegions = new ArrayList<ProtectedRegion>();
		
		// loop through every region
		for (String regionName : wg.getRegionManager(world).getRegions().keySet()) {
			// get the region from its name
			ProtectedRegion currentRegion = wg.getRegionManager(world).getRegion(regionName);
			
			// create a world edit vector for the signs position
			com.sk89q.worldedit.Vector v = new com.sk89q.worldedit.Vector(
				location.getX(), 
				location.getY(), 
				location.getZ()
			);
			
			// if the current region contains the sign, add it to the list of possible regions
			if (currentRegion.contains(v)) {
				possibleRegions.add(currentRegion);
			}
		}
		
		// if we didnt get any regions, bail.
		if (possibleRegions.size() == 0)
			return null;
		
		// if we only got one region, it has to be the region we use
		if (possibleRegions.size() == 1)
			return possibleRegions.get(0);

		// work out the lowest child of the regions
		int childLevel = 0;
		ProtectedRegion lowestChild = null;
		for (int i = 0; i < possibleRegions.size(); ++i)
		{
			if (possibleRegions.get(i).getParent() != null)
			{
				ProtectedRegion tempRegion = possibleRegions.get(i);
				int tempChildLevel = 0;
				while(tempRegion.getParent() != null)
				{
					tempRegion = tempRegion.getParent();
					tempChildLevel++;
				}
				
				if (tempChildLevel > childLevel)
				{
					childLevel = tempChildLevel;
					lowestChild = possibleRegions.get(i);
				}
			}
		}
		
		// if we found the lowest child return that
		if (lowestChild != null)
			return lowestChild;

		// if we didn't find the lowest child, but found some regions, return the first region
		if (possibleRegions.size() > 0)
			return possibleRegions.get(0);
			
		// something went very wrong
		return null;
	}
}
