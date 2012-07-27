package uk.codingbadgers.bsign.sign;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public abstract class Sign {
	
	protected OfflinePlayer m_creator = null;
	protected String m_context = null;
	protected Location m_location = null;
	
	public Sign(OfflinePlayer owner, Location signLocation) {
		m_creator = owner;
		m_location = signLocation;
	}
	
	public abstract boolean init(String context);

	public abstract void interact(Player player);
	
	public OfflinePlayer getCreator() {
		return m_creator;
	}
	
	public String getContext() {
		return m_context;
	}
	
	public Location getLocation() {
		return m_location;
	}

	public abstract String getType();
	
}
