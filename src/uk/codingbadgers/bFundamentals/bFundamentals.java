package uk.codingbadgers.bFundamentals;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class bFundamentals extends JavaPlugin {
	
	private static bFundamentals m_instance;
	private static final Logger log = Logger.getLogger("bFundamentals");
	
	private ModuleLoader m_moduleLoader = null;
	
	@Override
	public void onEnable() {
		m_instance = this;
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		m_moduleLoader = new ModuleLoader();
		m_moduleLoader.load();
		
		bFundamentals.log(Level.INFO, "bFundamentals Loaded.");
	}
	
	@Override
	public void onDisable() {
		bFundamentals.log(Level.INFO, "bFundamentals Disabled.");
	}
	
	public static bFundamentals getInstance() {
		return m_instance;
	}
	
	public static void log(Level level, String msg) {
		log.log(level, "[" + m_instance.getDescription().getName() + " " + m_instance.getDescription().getVersion() + "] " + msg);
	}

}
