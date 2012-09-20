package uk.codingbadgers.bcreative.containers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorldManager {

	private List<GamemodeWorld> m_worlds = new ArrayList<GamemodeWorld>();
	
	public GamemodeWorld getWorld(String name) {
		Iterator<GamemodeWorld> itr = m_worlds.iterator();
		while(itr.hasNext()) {
			GamemodeWorld world = itr.next();
			if (world.getWorld().getName().equalsIgnoreCase(name)) 
				return world;
		}
		return null;
	}
	
	public List<GamemodeWorld> getWorlds() {
		return m_worlds;
	}
}
