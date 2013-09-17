/**
 * bFundamentals 1.2-SNAPSHOT
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
package uk.codingbadgers.bFundamentals.module;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.commands.ModuleCommandHandler;
import uk.codingbadgers.bFundamentals.config.ConfigFactory;
import uk.codingbadgers.bFundamentals.config.ConfigFile;
import uk.codingbadgers.bFundamentals.config.annotation.Element;
import uk.codingbadgers.bFundamentals.module.annotation.ModuleInfo;
import uk.codingbadgers.bFundamentals.module.events.ModuleDisableEvent;
import uk.codingbadgers.bFundamentals.module.events.ModuleEnableEvent;
import uk.codingbadgers.bFundamentals.module.loader.ClassFinder;
import uk.codingbadgers.bFundamentals.module.loader.ModuleClassLoader;
import uk.codingbadgers.bFundamentals.module.loader.ModuleLoader;
import uk.codingbadgers.bFundamentals.update.UpdateThread;
import uk.codingbadgers.bFundamentals.update.Updater;
import uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase;

/**
 * The base Module class any module should extend this, it also provides helper
 * methods for the module, modules should be annotated with {@link ModuleInfo}
 * to save the information of the module.
 */
public abstract class Module implements Listener {

	protected final BukkitDatabase m_database;
	protected final bFundamentals m_plugin;
	protected File m_configFile;
	protected FileConfiguration m_config;
	
	private boolean m_debug = false;
	private boolean loadedLanguageFile;
	private boolean m_enabled;
	private File m_file;
	private JarFile m_jar;
	private File m_dataFolder;
	private ModuleLogger m_log;
	private UpdateThread m_updater;
	private ModuleDescription m_description;
	private List<Class<? extends ConfigFile>> m_configFiles;
	private List<Listener> m_listeners = new ArrayList<Listener>();
	private Map<String, String> m_languageMap = new HashMap<String, String>();
	private ModuleClassLoader m_loader;

	/**
	 * Instantiates a new module with default settings.
	 */
	public Module() {
		m_plugin = bFundamentals.getInstance();
		m_database = bFundamentals.getBukkitDatabase();
		m_debug = bFundamentals.getConfigurationManager().isDebugEnabled();
	}

	/**
	 * Initialise this module
	 */
	public final void init() {
		m_log = new ModuleLogger(this);
	}

	protected final void setUpdater(Updater updater) {
		m_updater = new UpdateThread(updater);
		log(Level.INFO, "Set new updater to " + m_updater.getUpdater().getUpdater());
	}

	/**
	 * If enabled, check if this module needs a update
	 */
	public final void update() {
		if (m_updater == null) {
			log(Level.INFO, "Updater is null, cannot check for updates");
			return;
		}

		m_updater.start();
	}

	/**
	 * Load language file.
	 */
	protected void loadLanguageFile() {
		File languageFile = new File(getDataFolder() + File.separator + getName() + "_" + bFundamentals.getConfigurationManager().getLanguage() + ".lang");

		if (!languageFile.exists()) {
			log(Level.SEVERE, "Missing language file '" + languageFile.getAbsolutePath() + "'!");

			boolean foundLangFile = false;
			InputStream stream = null;
			FileOutputStream fstream = null;

			try {
				stream = getClass().getResourceAsStream("/" + languageFile.getName());

				// if default file exists in jar, copy it out to the right
				// directory
				if (stream != null) {
					fstream = new FileOutputStream(languageFile);

					foundLangFile = true;
					IOUtils.copy(stream, fstream);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (stream != null) {
						stream.close();
					}

					if (fstream != null) {
						fstream.close();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			if (foundLangFile) {
				log(Level.INFO, "Copied default language file from jar file");
			} else {
				return;
			}
		}

		log(Level.INFO, "Loading Language File: " + languageFile.getName());

		FileInputStream fstream = null;
		DataInputStream in = null;
		BufferedReader br = null;

		try {
			fstream = new FileInputStream(languageFile);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			String line = null;
			String key = null;
			while ((line = br.readLine()) != null) {

				if (line.isEmpty() || line.startsWith("//"))
					continue;

				if (line.startsWith("#")) {
					key = line.substring(1);
					continue;
				}

				if (key == null) {
					log(Level.WARNING, "Trying to parse a language value, with no key set!");
					continue;
				}

				m_languageMap.put(key.toLowerCase(), line);
			}

			loadedLanguageFile = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fstream != null) {
					fstream.close();
				}
				if (in != null) {
					in.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Gets the language value for the current loaded language, case
	 * insensitive, all keys are forced to be in lower case.
	 * 
	 * @param key
	 *            the language key
	 * @return the language value, if available, the key with hyphens removed
	 *         and in lower case otherwise
	 */
	public String getLanguageValue(String key) {
		Validate.notNull(key, "Language key cannot be null");

		if (!loadedLanguageFile) {
			log(Level.SEVERE, "Cannot get language value before loading language file");
		}

		String value = m_languageMap.get(key.toLowerCase());

		if (value == null) {
			value = key.toLowerCase().replace("-", " ");
		}

		return value;
	}

	/**
	 * Log a message console via this modules logger.
	 * 
	 * @param level
	 *            the Log level
	 * @param string
	 *            the message
	 */
	public void log(Level level, String string) {
		m_log.log(Level.INFO, string);
	}

	/**
	 * Get the logger associated with this module
	 * 
	 * @return this modules logger
	 */
	public Logger getLogger() {
		return m_log;
	}

	/**
	 * Register a bukkit event listener.
	 * 
	 * @param listener
	 *            the bukkit event listener
	 */
	public final void register(Listener listener) {
		if (m_listeners.contains(listener)) {
			return;
		}
		
		m_plugin.getServer().getPluginManager().registerEvents(listener, m_plugin);
		m_listeners.add(listener);
	}

	/**
	 * Gets the vault permissions instance.
	 * 
	 * @return the vault permissions instance
	 */
	public final Permission getPermissions() {
		return bFundamentals.getPermissions();
	}

	/**
	 * Gets the vault chat instance.
	 * 
	 * @return the vault chat instance
	 */
	public final Chat getChat() {
		return bFundamentals.getChat();
	}

	/**
	 * Gets the vault economy instance.
	 * 
	 * @return the vault economy instance
	 */
	public final Economy getEconomy() {
		return bFundamentals.getEconomy();
	}
	
	/**
	 * The enable method for this module, called on enabling the module via 
	 * {@link #setEnabled(boolean)} this is used to register commands, events
	 * and any other things that should be registered on enabling the module.
	 */
	public void onEnable() {}

	/**
	 * The disable method for this module, called on disabling the module via
	 * {@link #setEnabled(boolean)} this is used to clean up after the module when
	 * it is disabled.
	 */
	public void onDisable() {}

	/**
	 * The load method for this module, called on loading the module via the
	 * {@link ModuleLoader} this is called before any module in that load batch
	 * is loaded. 
	 */
	public void onLoad() {}
	
	/**
	 * Sets the module enabled status, will call {@link #onEnable()} if the 
	 * module isn't already enabled and you want to enable it and will call
	 * {@link #onDisable()} if the module isn't already disabled and you want
	 * to disable it.
	 * 
	 * @param enabled if you want to enable or disable the module
	 */
	public final void setEnabled(boolean enabled) {
		if (enabled) {
			if (m_enabled) {
				return;
			}

			ModuleEnableEvent event = new ModuleEnableEvent(bFundamentals.getInstance(), this);
			Bukkit.getServer().getPluginManager().callEvent(event);

			onEnable();
			m_enabled = true;

			try {
				Set<Class<? extends Listener>> listeners = ClassFinder.findListeners(m_loader, new URL[] {m_file.toURI().toURL()});
				debugConsole("Loading " + listeners.size() + " listeners");
				
				for (Class<? extends Listener> clazz : listeners) {
					try {
						debugConsole("Loading Listener (" + clazz.getName() + ")");
						register(clazz.newInstance());
					} catch (Exception ex) {
						log(Level.WARNING, clazz.getName() + " does not have atleast one public no argument constructor.");
					}
				}
			} catch (Exception ex) {
			}
			
		} else {
			if (!m_enabled) {
				return;
			}

			ModuleDisableEvent event = new ModuleDisableEvent(bFundamentals.getInstance(), this);
			Bukkit.getServer().getPluginManager().callEvent(event);

			onDisable();
			ModuleCommandHandler.deregisterCommand(this);
			for (Listener listener : getListeners()) {
				HandlerList.unregisterAll(listener);
			}
			m_enabled = false;
		}
	}
	
	/**
	 * Returns the current state of the module, if it is enabled or disabled.
	 * 
	 * @return if the module is enabled
	 */
	public final boolean isEnabled() {
		return m_enabled;
	}

	/**
	 * The command handing method for this module, this is only called if the
	 * command handing for that {@link ModuleCommand} returns false, preferably
	 * the {@link ModuleCommand#onCommand(CommandSender, String, String[])}
	 * should be used, this is just left for backwards comparability.
	 * 
	 * @param sender
	 *            the command sender
	 * @param label
	 *            the command label used
	 * @param args
	 *            the arguments for the command
	 * @return true, if the command has been handled, false if it hasn't
	 */
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		return false;
	}

	/**
	 * Register a command to this module.
	 * 
	 * @param command
	 *            the command
	 */
	protected void registerCommand(ModuleCommand command) {
		ModuleCommandHandler.registerCommand(this, command);
	}

	/**
	 * Get all commands registered to this module
	 * 
	 * @return the commands
	 * @Deprecated {@link ModuleCommandHandler#getCommands(Module)}
	 */
	public List<ModuleCommand> getCommands() {
		return ModuleCommandHandler.getCommands(this);
	}

	/**
	 * Get all the listeners registered to this module, for cleaning up on
	 * disable
	 * 
	 * @return a list of all listeners
	 */
	public final List<Listener> getListeners() {
		return m_listeners;
	}

	/**
	 * Is debug mode enabled on this module
	 * 
	 * @return if debug is enabled
	 */
	public final boolean isDebug() {
		return m_debug;
	}

	/**
	 * Set the debug mode for this module
	 * 
	 * @param debug
	 *            whether debug is on or not
	 */
	public final void setDebug(boolean debug) {
		m_debug = debug;
	}

	/**
	 * Output a message to console if debug mode is on
	 * 
	 * @param message
	 *            the message to output
	 */
	public final void debugConsole(String message) {
		if (!m_debug)
			return;

		log(Level.INFO, "[Debug] " + message);
	}

	/**
	 * Registers a config class as a config and loads it, class must extend
	 * {@link ConfigFile} and each element that is going to be included in the
	 * file should be {@code static} and have a {@link Element}
	 * annotation associated with it.
	 * 
	 * @param clazz the config class
	 */
	public final void registerConfig(Class<? extends ConfigFile> clazz) {
		if (m_configFiles == null) {
			m_configFiles = new ArrayList<Class<? extends ConfigFile>>();
		}

		log(Level.INFO, "Load config file for " + clazz.getName());

		try {
			ConfigFactory.load(clazz, getDataFolder());
		} catch (Exception e) {
			e.printStackTrace();
		}
		m_configFiles.add(clazz);
	}
	
	/* Start Loading specific methods */
	
	/**
	 * Set the datafolder.
	 *
	 * @param dataFolder the data folder
	 */
	public final void setDatafolder(File dataFolder) {
		if (m_dataFolder != null) {
			throw new IllegalStateException("Data folder already set");
		}
		dataFolder.mkdirs();
		this.m_dataFolder = dataFolder;
	}

	/**
	 * Set the file for this loadable.
	 *
	 * @param file the file
	 */
	public final void setFile(File file) {
		if (m_file != null) {
			throw new IllegalStateException("File already set");
		}
		this.m_file = file;
	}

	/**
	 * Set the jar file.
	 *
	 * @param jar the jar
	 */
	public final void setJarFile(JarFile jar) {
		if (m_jar != null) {
			throw new IllegalStateException("Jar file already set");
		}
		this.m_jar = jar;
	}

	/**
	 * Set the {@link ModuleDescription} for this module.
	 *
	 * @param ldf the loadable description file
	 */
	public final void setDesciption(ModuleDescription ldf) {
		if (m_description != null) {
			throw new IllegalStateException("Description already set");
		}
		this.m_description = ldf;
	}

	/**
	 * Sets the {@link ModuleClassLoader} for this module
	 * 
	 * @param loader the current class loader for this module
	 */
	public final void setClassLoader(ModuleClassLoader loader) {
		if (m_loader != null) {
			throw new IllegalStateException("Class loader already set");
		}
		this.m_loader = loader;
	}

	/* End Loading specific methods */

	/**
	 * Gets the config.
	 *
	 * @return The config
	 */
	public final FileConfiguration getConfig() {
		if (m_config == null) {
			reloadConfig();
		}

		return m_config;
	}

	/**
	 * Gets the data folder of this.
	 *
	 * @return The directory of this
	 */
	public final File getDataFolder() {
		return m_dataFolder;
	}

	/**
	 * Gets the file of the loadable.
	 *
	 * @return the jar file as a {@link File}
	 */
	public final File getFile() {
		return m_file;
	}

	/**
	 * Gets the name of the Loadable.
	 *
	 * @return The name
	 */
	public final ModuleClassLoader getClassLoader() {
		return m_loader;
	}

	/**
	 * Gets the name of the Loadable.
	 *
	 * @return The name
	 */
	public final String getName() {
		return getDescription().getName();
	}

	/**
	 * Gets the version of this module loaded from the path.yml file.
	 * 
	 * @return the module version
	 */
	public final String getVersion() {
		return getDescription().getVersion();
	}
	
	/**
	 * Gets an embedded resource in this plugin.
	 *
	 * @param name File name of the resource
	 * @return InputStream of the file if found, otherwise null
	 */
	public final InputStream getResource(String name) {
		ZipEntry entry = m_jar.getEntry(name);

		if (entry == null)
			return null;

		try {
			return m_jar.getInputStream(entry);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Reloads the config.
	 */
	public final void reloadConfig() {
		if (m_configFile == null) {
			m_configFile = new File(getDataFolder(), "config.yml");
		}

		m_config = YamlConfiguration.loadConfiguration(m_configFile);

		InputStream defConfigStream = getResource("config.yml");

		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			m_config.setDefaults(defConfig);
		}
	}

	/**
	 * Saves the config.
	 * 
	 * @return if the config was saved successfully
	 */
	public final boolean saveConfig() {
		if (m_config == null || m_configFile == null) {
			return false;
		}

		try {
			m_config.save(m_configFile);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public final ModuleDescription getDescription() {
		return this.m_description;
	}
	
	/**
	 * Checks if a player has a specific permission.
	 * 
	 * @param player
	 *            the player to check
	 * @param node
	 *            the permission node
	 * @return true, if the player has the permission
	 */
	public static boolean hasPermission(final Player player, final String node) {
		if (bFundamentals.getPermissions().has(player, node)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if a player has a specific permission.
	 * 
	 * @param player
	 *            the player to check
	 * @param node
	 *            the permission node
	 * @return true, if the player has the permission
	 */
	public static boolean hasPermission(final CommandSender player, final String node) {
		if (bFundamentals.getPermissions().has(player, node)) {
			return true;
		}
		return false;
	}

	/**
	 * Send message to a player formated in the default style.
	 * 
	 * @param name
	 *            the name of the module
	 * @param player
	 *            the player to send to
	 * @param message
	 *            the message
	 */
	public static void sendMessage(String name, CommandSender player, String message) {
		player.sendMessage(ChatColor.DARK_PURPLE + "[" + name + "] " + ChatColor.RESET + message);
	}

}