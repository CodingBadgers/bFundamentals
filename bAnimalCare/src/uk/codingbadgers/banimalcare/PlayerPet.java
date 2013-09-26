/**
 * bAnimalCare 1.2-SNAPSHOT
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
package uk.codingbadgers.banimalcare;

import org.bukkit.Location;

public class PlayerPet {
	
	private String m_ownerPlayer = null;
	private Location m_lastKnownLocation;
	
	public PlayerPet(String owner) {
		m_ownerPlayer = owner;
	}
	
	public void UpdateLastKnownLocation(Location location) {
		m_lastKnownLocation = location;
	}
	
	public String GetOwner() {
		return m_ownerPlayer;		
	}
	
	public Location GetLocation() {
		return m_lastKnownLocation;
	}

}
