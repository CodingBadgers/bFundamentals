package uk.codingbadgers.bportalshelper;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiversePortals.MVPortal;
import com.onarandombox.MultiversePortals.PortalLocation;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class Portal {
	
	/** The portal selection. */
	private Vector m_minimumPoint = null;
	
	/** The portal selection. */
	private Vector m_maximumPoint = null;
	
	/** The exit direction. */
	private float m_direction = 0.0f;
	
	/** The name of the region the portal resides. */
	private String m_regionName = null;
	
	/** The name of the portal. */
	private String m_name = null;
	
	/** The world. */
	private World m_world = null;
	
	/** The player creating the portal. */
	private Player m_player = null;
	
	/**
	 * Instantiates a new portal.
	 *
	 * @param selection the selection
	 * @param direction the direction
	 */
	public Portal(Player creator, String name, Selection selection) {
		m_regionName = name;
		m_world = creator.getWorld();
		m_direction = creator.getLocation().getYaw();
		m_player = creator;
		
		final Location min = selection.getMinimumPoint();
		m_minimumPoint = new Vector(min.getX(), min.getY(), min.getZ());
		
		final Location max = selection.getMaximumPoint();
		m_maximumPoint = new Vector(max.getX(), max.getY(), max.getZ());
		
		// make sure the direction is between 0 and 360
		m_direction = m_direction < 0 ? 360 + m_direction : m_direction;
	}
	
	/**
	 * Gets the direction.
	 *
	 * @return the direction
	 */
	public String getDirection() {
		
		if (m_direction > 340 || m_direction < 20)
			return "s";
		
		if (m_direction > 160 && m_direction < 200)
			return "n";
		
		if (m_direction > 70 && m_direction < 110)
			return "w";
		
		if (m_direction > 250 && m_direction < 290)
			return "e";
		
		return "n";
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getRegionName() {
		return m_regionName;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return m_name;
	}
	
	/**
	 * Gets the world.
	 *
	 * @return the world
	 */
	public String getWorld() {
		return m_world.getName();
	}
	
	/**
	 * Creates the portal.
	 *
	 * @param portalName the portal name
	 * @return true, if successful
	 */
	public boolean createPortal(String portalName) {
		
		final MultiverseWorld world = bPortalsHelper.getMVWorldManager().getMVWorld(m_world.getName());
		final PortalLocation location = new PortalLocation(m_minimumPoint, m_maximumPoint, world);
		
        if (bPortalsHelper.getPortalManager().addPortal(world, portalName, m_player.getName(), location)) {
        	m_name = portalName;
            return true;
        }
		
        return false;
	}

	/**
	 * Sets the destination.
	 *
	 * @param destination the new destination
	 */
	public void setDestination(Portal destination) {
		
		MVPortal portal = bPortalsHelper.getPortalManager().getPortal(m_name);
		if (portal == null) {
			bPortalsHelper.sendMessage("bPortalsHelper", m_player, "Could not find a portal with the name " + m_name);
			return;
		}
		
		portal.setDestination("p:" + destination.getName() + ":" + destination.getDirection());
	}

}
