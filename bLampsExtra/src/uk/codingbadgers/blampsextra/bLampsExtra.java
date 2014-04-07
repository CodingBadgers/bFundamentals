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
package uk.codingbadgers.blampsextra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.scheduler.BukkitScheduler;
import uk.codingbadgers.bFundamentals.module.Module;

/**
 * The base module class for bLogin.
 */
public class bLampsExtra extends Module implements Listener {

        private BukkitScheduler scheduler = null;
        private Map<World, List<LightBlock>> blocks = null;
        private Options options = null;
        private int scheduleId = -1;
        private LampCommand lampCommand = null;
        
	@Override
	public void onEnable() {
            
            register(this);
            
            this.lampCommand = new LampCommand();
            registerCommand(this.lampCommand);
            
            this.options = new Options();
            this.blocks = new HashMap<World, List<LightBlock>>();
            for (World world : Bukkit.getWorlds()) {
                this.blocks.put(world, new ArrayList<LightBlock>());
            }
            
            loadConfig();
            
            this.scheduler = Bukkit.getScheduler();
            performForceLights();
            
	}

	@Override
	public void onDisable() {
	
            this.scheduler.cancelTask(scheduleId);
            
	}
        
        /**
         * 
         */
        private void loadConfig() {
            
            log(Level.INFO, "Loading bLampsExtra Config...");
            
            FileConfiguration config = this.getConfig();            
            this.options.timeOn = config.getInt("Time_on");
            this.options.timeOff = config.getInt("Time_off");
            
            log(Level.INFO, "Time range: " + this.options.timeOn + " -> " + this.options.timeOff);
            
            int noofLamps = 0;
            List<String> lights = config.getStringList("lights");
            for (String light : lights)
            {
                String[] parts = light.split(",");
                World world = Bukkit.getWorld(parts[4]);
                if (world == null) {
                    continue;
                }          
                Location location = new Location(world, Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
                LightBlock newBlock = new LightBlock(location.getBlock(), Integer.parseInt(parts[5]) == 1, parts[0]);
                
                List<LightBlock> worldBlocks = this.blocks.get(world);
                worldBlocks.add(newBlock);                
                this.blocks.put(world, worldBlocks);
                noofLamps++;
            }

            log(Level.INFO, "Loaded " + noofLamps + " lamps.");
        }
        
        private void saveBlockConfig() {
            
            FileConfiguration config = this.getConfig();            
            config.set("Time_on", this.options.timeOn);
            config.set("Time_off", this.options.timeOff);

            List<String> allLights = new ArrayList<String>();
            for (World world : Bukkit.getWorlds()) {                
                List<LightBlock> lightBlocks = blocks.get(world);
                for (LightBlock light : lightBlocks) {
                    allLights.add(light.owner + "," 
                        + light.block.getX() + "," 
                        + light.block.getY() + "," 
                        + light.block.getZ() + "," 
                        + light.block.getWorld().getName() + ","
                        + (light.force ? "1" : "0") 
                    );
                }                
            }
            
            config.set("lights", allLights);
            
            this.saveConfig();
        }
        
        /**
         * 
         */
        private void performForceLights() {
            
            this.scheduleId = this.scheduler.scheduleSyncRepeatingTask(m_plugin, 
            new Runnable() {

                @Override
                public void run() {

                    for (World world : Bukkit.getWorlds()) {
                        
                        long time = world.getTime();
                        boolean isNight = time >= options.timeOn && time <= options.timeOff;

                        List<LightBlock> lightBlocks = blocks.get(world);
                        for (LightBlock light : lightBlocks) {
                            light.isLit = false;
                            if (isNight || light.force) {
                                light.block.setType(Material.REDSTONE_LAMP_ON);
                                light.isLit = true;
                            }
                        }
                    }
                    
                }
                
            }, 
            20L, 60L);      
            
        }
        
        /**
         * 
         * @param event 
         */
        @EventHandler
        public void onBlockRedstone(BlockRedstoneEvent event) {
        
            final Block block = event.getBlock();
            final World world = block.getWorld();
            List<LightBlock> lightBlocks = this.blocks.get(world);
            
            for (LightBlock light : lightBlocks) {
                if (block.equals(light.block)) {
                    if (light.isLit) {
                        event.setNewCurrent(100);
                    }                    
                    return;
                }                
            }
            
        }
        
        /**
         * 
         * @param event 
         */
        @EventHandler
        public void onBlockBreak(BlockBreakEvent event) {
            
            final Player player = event.getPlayer();
            
            final boolean makeLamps = this.lampCommand.activePlayers.contains(player);
            final boolean forceOnLamps = this.lampCommand.activeForcePlayers.contains(player);
            
            if (makeLamps || forceOnLamps) {
                
                final Block block = event.getBlock();
                List<LightBlock> lightBlocks = this.blocks.get(block.getWorld());
                LightBlock newBlock = new LightBlock(block, forceOnLamps, player.getName());
                lightBlocks.add(newBlock);
                this.blocks.put(block.getWorld(), lightBlocks);
                
                this.saveBlockConfig();
                event.setCancelled(true);
                
                Module.sendMessage("bLampsExtra", player, "Added new lamp");
            }
            
        }
        
        /**
         * 
         */
        private class Options {
            public int timeOn;
            public int timeOff;
        }
	
        /**
         * 
         */
        private class LightBlock {
            public Block block;
            public boolean force;
            public String owner;
            public boolean isLit;
            
            public LightBlock(Block block, boolean force, String owner) {
                this.block = block;
                this.force = force;
                this.owner = owner;
                this.isLit = false;
            }
        }
}
