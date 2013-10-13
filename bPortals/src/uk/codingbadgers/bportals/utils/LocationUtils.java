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
package uk.codingbadgers.bportals.utils;

import org.bukkit.Location;
import static org.bukkit.util.NumberConversions.*;

import com.sk89q.worldedit.Vector;

public class LocationUtils {

	public static Vector convertLocationToVector(Location loc) {
		return new Vector(loc.getX(), loc.getY(), loc.getZ());
	}
	
	public static Vector convertLocationToFloorVector(Location loc) {
		return new Vector(floor(loc.getX()), floor(loc.getY()), floor(loc.getZ()));
	}
	
	public static Vector convertLocationToCeilVector(Location loc) {
		return new Vector(ceil(loc.getX()), ceil(loc.getY()), ceil(loc.getZ()));
	}
}
  