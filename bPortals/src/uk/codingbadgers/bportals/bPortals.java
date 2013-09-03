package uk.codingbadgers.bportals;

import java.util.logging.Level;

import org.bukkit.Bukkit;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bportals.commands.PortalCommand;
import uk.codingbadgers.bportals.listeners.PortalListener;

public class bPortals extends Module {

	private static bPortals instance;
	private static WorldEditPlugin worldedit;

	/**
	 * Called when the module is disabled.
	 */
	public void onDisable() {
		PortalManager.getInstance().destroy();
		
		instance = null;
		worldedit = null;
	}

	/**
	 * Called when the module is loaded.
	 */
	public void onEnable() {
		instance = this;
		
		if (!Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
			setEnabled(false);
			log(Level.SEVERE, "Could not find worldedit, disabling plugin.");
			return;
		}
		
		worldedit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		
		PortalManager.setInstance(new PortalManager());
		register(new PortalListener());
		registerCommand(new PortalCommand());
	}

	public static bPortals getInstance() {
		return instance;
	}
	
	public static WorldEditPlugin getWorldEdit() {
		return worldedit;
	}
	
}
