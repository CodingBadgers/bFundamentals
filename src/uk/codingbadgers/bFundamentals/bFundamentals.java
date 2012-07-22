package uk.codingbadgers.bFundamentals;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
		m_moduleLoader.enable();
		
		bFundamentals.log(Level.INFO, "bFundamentals Loaded.");
	}
	
	@Override
	public void onDisable() {
		bFundamentals.log(Level.INFO, "bFundamentals Disabled.");
		m_moduleLoader.disable();
	}
	
	public static bFundamentals getInstance() {
		return m_instance;
	}
	
	public static void log(Level level, String msg) {
		log.log(level, "[" + m_instance.getDescription().getName() +"] " + msg);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("bfundamentals")) {
			handleCommand(sender, cmd, label, args);
		}
		
		return m_moduleLoader.onCommand(sender, cmd, label, args);
	}

	private void handleCommand(CommandSender sender, Command cmd, String label,	String[] args) {
		
	}

}
