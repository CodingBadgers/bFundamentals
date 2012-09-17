package uk.codingbadgers.bsign.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import uk.codingbadgers.bsign.bSignModule;

public class WorldListener implements Listener {
	
	/**
	 * @param event
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldLoad(WorldLoadEvent event) {
		
		final String worldName = event.getWorld().getName();
		bSignModule.LoadSignsFromDatabase(worldName);
		
	}

}
