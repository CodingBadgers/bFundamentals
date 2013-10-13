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

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.common.base.Splitter;

public class DatabaseUtils {

	public Location getFromString(String location) {
		Iterable<String> split = Splitter.on(',').trimResults().omitEmptyStrings().split(location);
		Iterator<String> itr = split.iterator();
		String world = itr.next();
		double x = Double.valueOf(itr.next());
		double y = Double.valueOf(itr.next());
		double z = Double.valueOf(itr.next());
		return new Location(Bukkit.getWorld(world), x, y, z);
	}
	
	public String putToString(Location loc) {
		return loc.getWorld() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
	}
}
