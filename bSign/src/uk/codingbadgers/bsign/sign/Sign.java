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
