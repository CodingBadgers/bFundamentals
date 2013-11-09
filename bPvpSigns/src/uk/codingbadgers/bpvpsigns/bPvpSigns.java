/**
 * bPvpSigns 1.2-SNAPSHOT
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
package uk.codingbadgers.bpvpsigns;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.slipcor.pvparena.PVPArena;
import net.slipcor.pvparena.arena.Arena;
import net.slipcor.pvparena.arena.ArenaTeam;
import net.slipcor.pvparena.commands.AbstractArenaCommand;
import net.slipcor.pvparena.commands.PAG_Join;
import net.slipcor.pvparena.core.Config;
import net.slipcor.pvparena.core.Config.CFG;
import net.slipcor.pvparena.events.PAExitEvent;
import net.slipcor.pvparena.events.PAJoinEvent;
import net.slipcor.pvparena.events.PALeaveEvent;
import net.slipcor.pvparena.events.PAEndEvent;
import net.slipcor.pvparena.events.PALoseEvent;
import net.slipcor.pvparena.events.PAWinEvent;
import net.slipcor.pvparena.managers.ArenaManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;


public class bPvpSigns extends Module implements Listener {
	
	PVPArena pa = null;
	
	Map<String, Sign> signs = new HashMap<String, Sign>();
	
	/**
	 * Called when the module is disabled.
	 */
	public void onDisable() {
	}

	/**
	 * Called when the module is loaded.
	 */
	public void onEnable() {
		register(this);
		
		this.pa = (PVPArena)this.m_plugin.getServer().getPluginManager().getPlugin("PVPArena");
		
		loadSigns();
	}
	
	/**
	 * Load all signs from disk
	 */
	private void loadSigns() {

		for (File file : getDataFolder().listFiles()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			
			final String name = config.getString("name");
			final String worldName = config.getString("world");
			final int x = config.getInt("location.x");
			final int y = config.getInt("location.y");
			final int z = config.getInt("location.z");
			
			World world = Bukkit.getWorld(worldName);
			if (world == null) {
				continue;
			}
			
			Location location = new Location(world, x, y, z);
			if (location == null || location.getBlock() == null) {
				continue;
			}
			
			Block block = location.getBlock();			
			if (!(block.getState() instanceof Sign)) {
				continue;
			}
			
			signs.put(name, (Sign)block.getState());
			
		}
		
	}
	
	/**
	 * Save a given sign to disk
	 * @param arenaName The name of the arena name
	 * @param sign The sign to save
	 */
	private void saveSign(String arenaName, Sign sign) {
		
		final String path = getDataFolder() + File.separator + arenaName + ".yml";
		File file = new File(path);
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		config.set("name", arenaName);
		config.set("world", sign.getLocation().getWorld().getName());
		config.set("location.x", sign.getLocation().getBlockX());
		config.set("location.y", sign.getLocation().getBlockY());
		config.set("location.z", sign.getLocation().getBlockZ());
		
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Remove the sign for a specified arena name
	 * @param arenaName The name of the arena whose sign we should remove
	 */
	private void removeSign(String arenaName) {
		this.signs.remove(arenaName);
		
		final String path = getDataFolder() + File.separator + arenaName + ".yml";
		File file = new File(path);
		
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * Called when a sign is changed
	 */	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignChange(SignChangeEvent event) {
		
		if (!event.getLine(0).equalsIgnoreCase("[PvpArena]")) {
			return;
		}
		
		String arenaName = event.getLine(1);
		if (arenaName == null) {
			return;
		}
		
		Arena arena = ArenaManager.getArenaByName(arenaName);
		if (arena == null) {
			return;
		}
		
		Sign sign = (Sign)event.getBlock().getState();
		if (sign == null) {
			return;
		}
		
		if (this.signs.containsKey(arena))
		{
			Module.sendMessage("PvpArena", event.getPlayer(), "The specified arena already has a sign accosiated with it");
			event.getBlock().breakNaturally();
			return;
		}
		
		event.setLine(0, ChatColor.DARK_RED + "[PvpArena]");
				
		this.signs.put(arenaName, sign);
		
		// Save 
		saveSign(arenaName, sign);
		updatePvpSign(event);
	}
	
	/**
	 * Called when a player interacts with a sign
	 */	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
	
		Block block = event.getClickedBlock();
		if (block == null) {
			return;
		}

		if (!(block.getState() instanceof Sign)) {
			return;
		}
		
		Sign sign = (Sign)block.getState();

		String paLine = sign.getLine(0);
		if (paLine == null || !paLine.toLowerCase().contains("[pvparena]")) {
			return;
		}
		
		String arenaName = ChatColor.stripColor(sign.getLine(1));
		Arena arena = ArenaManager.getArenaByName(arenaName);
		if (arena == null) {
			return;
		}
		
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE && event.getAction() == Action.LEFT_CLICK_BLOCK && !arena.getArenaConfig().getBoolean(Config.CFG.GENERAL_ENABLED)) {
			removeSign(arenaName);
			return;
		}
		
		event.setCancelled(true);
		
		final AbstractArenaCommand command = new PAG_Join();
        command.commit(arena, event.getPlayer(), new String[0]);
        
        updatePvpSign(sign);
	}

	/**
	 * Called when a player joins a pa
	 */	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPAJoin(PAJoinEvent event) {
				
		Arena arena = event.getArena();
		final String name = arena.getName();
		Sign sign = this.signs.get(name);
		if (sign == null) {
			return;
		}
		
		updatePvpSign(sign);
		
	}
	
	/**
	 * Called when a player leave a pa
	 */	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPALeave(PALeaveEvent event) {
		
		Arena arena = event.getArena();
		final String name = arena.getName();
		Sign sign = this.signs.get(name);
		if (sign == null) {
			return;
		}
		
		updatePvpSign(sign);
		
	}
	
	/**
	 * Called when a player exit a pa
	 */	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPAExit(PAExitEvent event) {
		
		Arena arena = event.getArena();
		final String name = arena.getName();
		Sign sign = this.signs.get(name);
		if (sign == null) {
			return;
		}
		
		updatePvpSign(sign);
		
	}
	
	/**
	 * Called when a pa ends
	 */	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPAEnd(PAEndEvent event) {

		Arena arena = event.getArena();
		final String name = arena.getName();

		final Sign sign = this.signs.get(name);
		if (sign == null) {
			return;
		}
		
		updatePvpSign(sign);
		
	}
	
	/**
	 * Called when a pa ends
	 */	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPAWin(PAWinEvent event) {
		
		Arena arena = event.getArena();
		final String name = arena.getName();
		Sign sign = this.signs.get(name);
		if (sign == null) {
			return;
		}
		
		updatePvpSign(sign);
		
	}
	
	/**
	 * Called when a pa ends
	 */	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPALoose(PALoseEvent event) {
		
		Arena arena = event.getArena();
		final String name = arena.getName();
		Sign sign = this.signs.get(name);
		if (sign == null) {
			return;
		}
		
		updatePvpSign(sign);
		
	}

	/**
	 * updates a pa sign
	 * @param sign The sign
	 */
	private void updatePvpSign(final Sign sign) {
		

		Bukkit.getScheduler().runTaskLater(bFundamentals.getInstance(), new Runnable() {

			@Override
			public void run() {
				String arenaName = ChatColor.stripColor(sign.getLine(1));
				Arena arena = ArenaManager.getArenaByName(arenaName);
				if (arena == null) {
					return;
				}

				sign.setLine(1, ChatColor.BOLD + arenaName);
				
				final int maxPlayers = arena.getArenaConfig().getInt(CFG.READY_MAXPLAYERS);
				int noofPlayers = 0;
				
				for (ArenaTeam team : arena.getTeams()) {
					noofPlayers += team.getTeamMembers().size();
				}
				
				sign.setLine(2, noofPlayers + "/" + maxPlayers);
				sign.setLine(3, arena.isFightInProgress() ? ChatColor.RED + "FIGHTING" : ChatColor.DARK_GREEN + "WAITING");
				
				sign.update(true);
			}
			
		}, 20L);

	}
	
	/**
	 * updates a pa sign
	 * @param sign The sign
	 */
	private void updatePvpSign(SignChangeEvent sign) {
				
		String arenaName = ChatColor.stripColor(sign.getLine(1));
		Arena arena = ArenaManager.getArenaByName(arenaName);
		if (arena == null) {
			return;
		}
		
		sign.setLine(1, ChatColor.BOLD + arenaName);
		
		final int maxPlayers = arena.getArenaConfig().getInt(CFG.READY_MAXPLAYERS);
		final int noofPlayers = arena.getFighters().size();
		
		sign.setLine(2, noofPlayers + "/" + maxPlayers);
		sign.setLine(3, arena.isFightInProgress() ? ChatColor.RED + "FIGHTING" : ChatColor.DARK_GREEN + "WAITING");
		
	}
	
}
