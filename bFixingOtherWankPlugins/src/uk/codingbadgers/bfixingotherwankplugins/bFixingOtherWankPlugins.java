package uk.codingbadgers.bfixingotherwankplugins;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.plugin.Plugin;

import uk.codingbadgers.bFundamentals.module.Module;

public class bFixingOtherWankPlugins extends Module implements Listener {

	public static Plugin PLUGIN = null;
	
	private ArrayList<bWorld> m_worlds = new ArrayList<bWorld>();
	
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
		PLUGIN = this.m_plugin;
	}
	
	/**
	 * Called on world load
	 */	
	@EventHandler(priority = EventPriority.LOW)
	public void onWorldLoad(WorldLoadEvent event) {
		
		// Get the world name
		final String worldName = event.getWorld().getName();
		
		// Get and create (if needed) a config file.
		File worldFile = new File(this.getDataFolder() + File.separator + "WorldConfigs" + File.separator + worldName + ".yml");
		boolean newFile = false;
		if (!worldFile.exists()) {
			try {
				worldFile.createNewFile();
				newFile = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Create a File Configuration 
		FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(worldFile);
		
		if (newFile) {
			String[] defaultConsoleCommands = {"gamemode 0 <<name>>"};
			fileConfiguration.addDefault("onWorldEnter.execute.console", Arrays.asList(defaultConsoleCommands));
			String[] defaultPlayerCommands = {"land"};
			fileConfiguration.addDefault("onWorldEnter.execute.player", Arrays.asList(defaultPlayerCommands));
		}
		
		bWorld world = new bWorld(worldName);
		world.setOnEnterWorldCommands(fileConfiguration.getStringList("onWorldEnter.execute.console"), fileConfiguration.getStringList("onWorldEnter.execute.player"));
		m_worlds.add(world);
	}
	
	/**
	 * Called player teleport
	 */	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		
		if (event.isCancelled())
			return;
		
		final Player player = event.getPlayer();
		final Location destLocation = event.getTo();
		
		if (!destLocation.getWorld().getName().equalsIgnoreCase(player.getLocation().getWorld().getName())) {
			
			bWorld world = getWorld(destLocation.getWorld().getName());
			if (world == null)
				return;
			
			world.executeOnEnter(player);			
		}
		
	}
	
	/**
	 * Called when a player attacks something
	 */	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerAttack(EntityDamageByEntityEvent event) {
		
	}
	
	/**
	 * Get a world from its name
	 */	
	private bWorld getWorld(final String name) {
		
		for (bWorld world : m_worlds) {
			if (world.getName().equalsIgnoreCase(name))
				return world;
		}
		
		return null;
	}
	
}
