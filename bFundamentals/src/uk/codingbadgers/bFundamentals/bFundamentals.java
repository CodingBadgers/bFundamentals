package uk.codingbadgers.bFundamentals;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import n3wton.me.BukkitDatabaseManager.BukkitDatabaseManager;
import n3wton.me.BukkitDatabaseManager.BukkitDatabaseManager.DatabaseType;
import n3wton.me.BukkitDatabaseManager.Database.BukkitDatabase;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import uk.codingbadgers.bFundamentals.module.Module;

public class bFundamentals extends JavaPlugin {
	
	private static bFundamentals m_instance = null;
	private static final Logger log = Logger.getLogger("bFundamentals");
	private static BukkitDatabase m_database = null;	
	private static Permission m_permissions = null;
	private ConfigurationManager m_configuration = null;
	private ModuleLoader m_moduleLoader = null;
	
	@Override
	public void onEnable() {
		m_instance = this;
		
		// load the configuration into the configuration manager
		m_configuration = new ConfigurationManager();
		m_configuration.loadConfiguration(m_instance);
		
		// load the modules in
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
	
	public ConfigurationManager getConfigurationManager() {
		return m_configuration;
	}
	
	public static BukkitDatabase getBukkitDatabase() {
		if (m_database == null) {
			m_database = BukkitDatabaseManager.CreateDatabase("bFundamentals", m_instance, DatabaseType.SQLite);
		}
		return m_database;
	}
	
	public static Permission getPermissions() {
		if (m_permissions == null) {
			RegisteredServiceProvider<Permission> permissionProvider = m_instance.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		    if (permissionProvider != null) {
		    	m_permissions = permissionProvider.getProvider();
		    }
		}
	    return m_permissions;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("bfundamentals")) {
			handleCommand(sender, cmd, label, args);
			return true;
		}
		
		if (label.equalsIgnoreCase("modules")) {
			handleModulesCommand(sender);
			return true;
		}
		
		return m_moduleLoader.onCommand(sender, cmd, label, args);
	}

	private void handleModulesCommand(CommandSender sender) {
		List<Module> modules = m_moduleLoader.getModules();
		String moduleString = ChatColor.GREEN + "Modules: ";
		boolean first = true;
		
		for (Module module : modules) {
			moduleString += (first ? "" : ", ") + module.getName();
			first = false;
		}
		
		sender.sendMessage(moduleString);
	}

	private void handleCommand(CommandSender sender, Command cmd, String label,	String[] args) {
		if (args.length < 1) {
			sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "/bFundamentals");			
			return;
		}
		
		if (args[0].equalsIgnoreCase("reload")) {
			this.getPluginLoader().disablePlugin(this);
			this.getPluginLoader().enablePlugin(this);
			sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Reloading plugin");
			return;
		}
		
		if (args[0].equalsIgnoreCase("module")) {
			if (args.length < 2) {
				sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "use /bFundamentals <reload/load>");
				return;
			}
			
			if (args[1].equalsIgnoreCase("unload")) {
				m_moduleLoader.unload();
				sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "UnLoaded all modules");			
				return;
			}
			
			if (args[1].equalsIgnoreCase("load")) {
				m_moduleLoader.load();
				m_moduleLoader.enable();
				sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Loaded all modules");			
				return;
			}
			
			if (args[1].equalsIgnoreCase("reload")) {
				m_moduleLoader.unload();
				m_moduleLoader.load();
				sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Reloaded all modules");			
				return;
			}
			
			sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "use /bFundamentals <reload/load>");
			return;
		}
	}
	
	public void disable(Module module) {
		m_moduleLoader.unload(module);
	}

}
