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
package uk.codingbadgers.bmcbadgerhub;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import uk.codingbadgers.bFundamentals.module.Module;

public class bMcBadgerHub extends Module implements Listener {

	/**
	 * Called when the module is disabled.
	 */
    @Override
	public void onDisable() {

	}

	/**
	 * Called when the module is loaded.
	 */
    @Override
	public void onEnable() {
		register(this);
	}
    
    /**
     * Called when a player logs into the server
     * @param event The player login event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        
        final Player player = event.getPlayer();
        final World mainWorld = Bukkit.getWorld("world");
        final Location mainSpawn = mainWorld.getSpawnLocation();
            
		// Delay the teleport a little, else it spazzes out sometimes
		Bukkit.getScheduler().runTaskLater(m_plugin, new Runnable() {

			@Override
			public void run() {
				player.teleport(mainSpawn);
			}
			
		}, 5L);
        
    }
    
    /**
     * Called when a player drops and item
     * @param event The item drop event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
     
        event.setCancelled(true);
		
    }
	
    /**
     * Called when a player moves
     * @param event The player move event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
     
        final Location location = event.getTo();
		
		if (location.getY() < 55.0) {
			
			final Player player = event.getPlayer();
			
			Vector velocity = player.getVelocity();
			velocity.setY(10.0);
			player.setVelocity(velocity);
			
		}
		
    }
    
}
