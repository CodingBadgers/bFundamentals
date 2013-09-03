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
}
