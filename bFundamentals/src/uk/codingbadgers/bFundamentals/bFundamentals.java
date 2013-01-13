package uk.codingbadgers.bFundamentals;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import uk.codingbadgers.bFundamentals.commands.CommandListener;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bFundamentals.module.ModuleLoader;
import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager;
import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager.DatabaseType;
import uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase;

public class bFundamentals extends JavaPlugin {
	
	private static final Logger m_log = Logger.getLogger("bFundamentals");
	private static bFundamentals m_instance = null;
	private static BukkitDatabase m_database = null;
	
	private static Permission m_permissions = null;
	private static Chat m_chat = null;
	private static Economy m_economy = null;
	
	private final CommandListener m_commandListener = new CommandListener();
	
	private static ModuleLoader m_moduleLoader = null;
	private static ConfigurationManager m_configuration = null;
	
	/**
	 * Called on loading. This is called before onEnable.
	 * Store the instance here, to do it as early as possible.
	 */
	@Override
	public void onLoad() {
		m_instance = this;
		log(Level.INFO, "bFundamentals Loading");
	}
	
	/**
	 * Called when the plugin is being enabled
	 * Load the configuration and all modules
	 * Register the command listener
	 */
	@Override
	public void onEnable() {
		
		// load the configuration into the configuration manager
		m_configuration = new ConfigurationManager();
		m_configuration.loadConfiguration(m_instance);
		
		// load the modules in
		m_moduleLoader = new ModuleLoader();
		m_moduleLoader.load();
		m_moduleLoader.enable();
		
		// check if any of the modules need updating
		if (m_configuration.getAutoUpdate()) 
			m_moduleLoader.update();
		
		// register the command listener
		this.getServer().getPluginManager().registerEvents(m_commandListener, this);
		
		bFundamentals.log(Level.INFO, "bFundamentals Loaded.");
	}

	/**
	 * Called when the plugin is being disabled
	 * Here we disable the module and thus all modules
	 */
	@Override
	public void onDisable() {
		bFundamentals.log(Level.INFO, "bFundamentals Disabled.");
		m_moduleLoader.disable();
		m_database.End();
	}
	
	/**
	 * Handle commands in the modules or plugin.
	 * @return True if the command was handled, False otherwise
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (label.equalsIgnoreCase("bfundamentals")) {
			handleCommand(sender, cmd, label, args);
			return true;
		}
		
		if (label.equalsIgnoreCase("modules")) {
			handleModulesCommand(sender);
			return true;
		}
		
		return false;
	}
	

	/**
	 * Disable a specific module
	 */
	public void disableModule(Module module) {
		m_moduleLoader.unload(module);
	}

	/**
	 * Reloads a specific module
	 */
	public void reloadModule(Module module) {
		m_moduleLoader.unload(module);
		m_moduleLoader.load(module.getFile());
		m_moduleLoader.getModule(module.getName()).onEnable();
	}

	/**
	 * Get the bFundamentals plugin instance.
	 * @return the instance
	 */
	public static bFundamentals getInstance() {
		return m_instance;
	}
	
	/**
	 * Access to the logging methods
	 */
	public static void log(Level level, String msg) {
		m_log.log(level, "[" + m_instance.getDescription().getName() +"] " + msg);
	}
	
	/**
	 * Get the configuration manager
	 */
	public static ConfigurationManager getConfigurationManager() {
		return m_configuration;
	}
	
	/**
	 * Access to the bukkit database
	 */
	public static BukkitDatabase getBukkitDatabase() {
		if (m_database == null) {
			m_database = bDatabaseManager.CreateDatabase("bFundamentals", m_instance, DatabaseType.SQLite);
		}
		return m_database;
	}
	
	/**
	 * Access to vaults permission mananger
	 */
	public static Permission getPermissions() {
		if (m_permissions == null) {
			RegisteredServiceProvider<Permission> permissionProvider = m_instance.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		    if (permissionProvider != null) {
		    	m_permissions = permissionProvider.getProvider();
		    }
		}
	    return m_permissions;
	}
	
	/**
	 * Access to vaults chat mananger
	 */
	public static Chat getChat() {
		if (m_chat == null) {
			RegisteredServiceProvider<Chat> chatProvider = m_instance.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
		    if (chatProvider != null) {
		    	m_chat = chatProvider.getProvider();
		    }
		}
	    return m_chat;
	}
	
	/**
	 * Access to vaults economy mananger
	 */
	public static Economy getEconomy() {
		if (m_economy == null) {
			RegisteredServiceProvider<Economy> economyProvider = m_instance.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		    if (economyProvider != null) {
		    	m_economy = economyProvider.getProvider();
		    }
		}
	    return m_economy;
	}
	
	/**
	 * Gets the module loader
	 */
	public static ModuleLoader getModuleLoader(){	
		return m_moduleLoader;
	}

	/**
	 * Handle a module command
	 */
	private void handleModulesCommand(CommandSender sender) {
		List<Module> modules = m_moduleLoader.getModules();
		String moduleString = ChatColor.GREEN + "Modules(" + modules.size() + "): ";
		boolean first = true;
		
		for (Module module : modules) {
			moduleString += (first ? "" : ", ") + module.getName();
			first = false;
		}
		
		sender.sendMessage(moduleString);
	}

	/**
	 * Handle the plugin commands
	 */
	private void handleCommand(CommandSender sender, Command cmd, String label,	String[] args) {
		
		if (args.length < 1) {
			sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "bFundamentals commands");
			sender.sendMessage(ChatColor.DARK_AQUA + "module " + ChatColor.WHITE + "- access module load/unload/reload commands");
			sender.sendMessage(ChatColor.DARK_AQUA + "debug " + ChatColor.WHITE + "- debug a given module");
			sender.sendMessage(ChatColor.DARK_AQUA + "reload " + ChatColor.WHITE + "- reload the plugin");
			return;
			
		}
		
		if (!m_permissions.has(sender, "bfundamentals.admin")) {
			sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Sorry you do not have permission to do that");
			return;
		}
		
		if (args[0].equalsIgnoreCase("reload")) {
			this.getPluginLoader().disablePlugin(this);
			this.getPluginLoader().enablePlugin(this);
			sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Reloading plugin");
			return;
		}
		
		if (args[0].equalsIgnoreCase("module")) {
			if (args.length < 1) {
				sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "bFundamentals module commands");
				sender.sendMessage(ChatColor.DARK_AQUA + "unload " + ChatColor.WHITE + "- unload a module");
				sender.sendMessage(ChatColor.DARK_AQUA + "load " + ChatColor.WHITE + "- load a module");
				sender.sendMessage(ChatColor.DARK_AQUA + "reload " + ChatColor.WHITE + "- reload a module");
				sender.sendMessage(ChatColor.DARK_AQUA + "debug " + ChatColor.WHITE + "- debug a module");
				return;
			}
			
			if (args[1].equalsIgnoreCase("unload")) {
				if (args.length == 3) {
					Module module = m_moduleLoader.getModule(args[2]);
					
					if (module == null) {
						sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Sorry that module isn't enabled on this server, do /modules for a list that are");
						return;
					}
					m_moduleLoader.unload(module);
					sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Unloaded " + args[2]);
					return;
				}
				
				m_moduleLoader.unload();
				sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "UnLoaded all modules");			
				return;
			}
			
			if (args[1].equalsIgnoreCase("load")) {
				
				if (args.length == 3) {
					m_moduleLoader.load(args[2]);
					Module module = m_moduleLoader.getModule(args[2]);
					
					if(module == null) {
						sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Error loading module " + args[2]);
						return;
					}
					
					module.onEnable();
					sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Loaded " + args[2]);
					return;
				}
				
				m_moduleLoader.load();
				m_moduleLoader.enable();
				sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Loaded all modules");			
				return;
			}
			
			if (args[1].equalsIgnoreCase("reload")) {
				
				if (args.length == 3) {
					Module module = m_moduleLoader.getModule(args[2]);
					
					if (module == null) {
						sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Sorry that module isn't enabled on this server, do /modules for a list that are");
						return;
					}
					m_moduleLoader.unload(module);
					m_moduleLoader.load(args[2]);
					module = m_moduleLoader.getModule(args[2]);
					
					if(module == null) {
						sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Error loading module " + args[2]);
						return;
					}
					
					module.onEnable();
					sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "ReLoaded " + args[2]);
					return;
				}
				
				m_moduleLoader.unload();
				m_moduleLoader.load();
				m_moduleLoader.enable();
				sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Reloaded all modules");			
				return;
			}
			
			if (args[1].equalsIgnoreCase("debug")) {
				
				if (args.length != 3) {
					sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "/bfundamentals module debug <module>");
					return;
				}
				
				Module module = m_moduleLoader.getModule(args[2]);
				
				if (module == null) {
					sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Module " + args[2] + " isn't loaded");
					return;
				}
				
				module.setDebug(!module.isDebug());
				sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + module.getName() + ": debug " + (module.isDebug() ? "enabled" : "disabled"));
				return;
			}
			
			sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "bFundamentals module commands");
			sender.sendMessage(ChatColor.DARK_AQUA + "unload " + ChatColor.WHITE + "- unload a module");
			sender.sendMessage(ChatColor.DARK_AQUA + "load " + ChatColor.WHITE + "- load a module");
			sender.sendMessage(ChatColor.DARK_AQUA + "reload " + ChatColor.WHITE + "- reload a module");
			sender.sendMessage(ChatColor.DARK_AQUA + "debug " + ChatColor.WHITE + "- debug a module");
			return;
		}

		sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "bFundamentals commands");
		sender.sendMessage(ChatColor.DARK_AQUA + "module " + ChatColor.WHITE + "- access module load/unload/reload commands");
		sender.sendMessage(ChatColor.DARK_AQUA + "debug " + ChatColor.WHITE + "- debug a given module");
		sender.sendMessage(ChatColor.DARK_AQUA + "reload " + ChatColor.WHITE + "- reload the plugin");
		return;
	}
}
