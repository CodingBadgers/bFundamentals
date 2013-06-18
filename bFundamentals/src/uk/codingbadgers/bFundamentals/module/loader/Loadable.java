package uk.codingbadgers.bFundamentals.module.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/*     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Loadable - Base for loadable classes.
 *
 * @author NodinChan
 */
public class Loadable implements Cloneable {

	private String name;
	private File configFile;
	private FileConfiguration config;
	private LoadableDescriptionFile description;
	private JarFile jar;
	private File dataFolder;
	private File file;

	/**
	 * Instantiates a new loadable.
	 *
	 * @param name the name
	 * @deprecated use a {@link LoadableDescriptionFile} instead
	 */
	public Loadable(String name) {
		this.name = name;
	}

	/**
	 * Instantiates a new loadable.
	 */
	public Loadable() {
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Loadable clone() {
		Loadable loadable = new Loadable(name);
		loadable.config = YamlConfiguration.loadConfiguration(configFile);
		loadable.configFile = configFile;
		loadable.dataFolder = dataFolder;
		loadable.file = file;
		loadable.jar = jar;
		loadable.description = description;
		return loadable;
	}

	/**
	 * Set the datafolder.
	 *
	 * @param dataFolder the data folder
	 * @return the file
	 */
	public File setDatafolder(File dataFolder) {
		dataFolder.mkdirs();
		return this.dataFolder = dataFolder;
	}

	/**
	 * Set the file for this loadable.
	 *
	 * @param file the file
	 * @return the file
	 */
	public File setFile(File file) {
		return this.file = file;
	}

	/**
	 * Set the jar file.
	 *
	 * @param jar the jar
	 * @return the jar file
	 */
	public JarFile setJarFile(JarFile jar) {
		return this.jar = jar;
	}

	/**
	 * Set the {@link LoadableDescriptionFile} for this module.
	 *
	 * @param ldf the loadable description file
	 * @return the loadable description file
	 */
	public LoadableDescriptionFile setDesciption(LoadableDescriptionFile ldf) {
		this.description = ldf;
		this.name = description.getName();
		return description;
	}

	/**
	 * Initialises the loadable, called on loading the module.
	 */
	public void init() {}

	/**
	 * Gets the config.
	 *
	 * @return The config
	 */
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();

		return config;
	}

	/**
	 * Gets the data folder of this.
	 *
	 * @return The directory of this
	 */
	public File getDataFolder() {
		return dataFolder;
	}

	/**
	 * Gets the file of the loadable.
	 *
	 * @return the jar file as a {@link File}
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Gets the name of the Loadable.
	 *
	 * @return The name
	 */
	public final String getName() {
		return getDesciption().getName();
	}

	/**
	 * Gets an embedded resource in this plugin.
	 *
	 * @param name File name of the resource
	 * @return InputStream of the file if found, otherwise null
	 */
	public InputStream getResource(String name) {
		ZipEntry entry = jar.getEntry(name);

		if (entry == null)
			return null;

		try {
			return jar.getInputStream(entry);
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Reloads the config.
	 */
	public void reloadConfig() {
		if (configFile == null)
			configFile = new File(getDataFolder(), "config.yml");

		config = YamlConfiguration.loadConfiguration(configFile);

		InputStream defConfigStream = getResource("config.yml");

		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}

	/**
	 * Saves the config.
	 */
	public void saveConfig() {
		if (config == null || configFile == null)
			return;

		try {
			config.save(configFile);
		} catch (IOException e) {
		}
	}

	/**
	 * Called when the Loadable is unloaded.
	 */
	public void unload() {
	}

	/**
	 * Gets the desciption.
	 *
	 * @return the desciption
	 */
	public LoadableDescriptionFile getDesciption() {
		return this.description;
	}

	/**
	 * The Class LoadResult.
	 */
	public static final class LoadResult {

		/** The result. */
		private final Result result;

		/** The reason. */
		private final String reason;

		/**
		 * Instantiates a new load result.
		 */
		public LoadResult() {
			this(Result.SUCCESS, "");
		}

		/**
		 * Instantiates a new load result.
		 *
		 * @param failReason the fail reason
		 */
		public LoadResult(String failReason) {
			this(Result.FAILURE, failReason);
		}

		/**
		 * Instantiates a new load result.
		 *
		 * @param result the result
		 * @param reason the reason
		 */
		public LoadResult(Result result, String reason) {
			this.result = result;
			this.reason = reason;
		}

		/**
		 * Gets the reason.
		 *
		 * @return the reason
		 */
		public String getReason() {
			return reason;
		}

		/**
		 * Gets the result.
		 *
		 * @return the result
		 */
		public Result getResult() {
			return result;
		}

		/**
		 * The Result for loading.
		 */
		public enum Result {
			
			/** If the loadable didn't load successfully. */
			FAILURE,
			/** If the loadable loaded successfully. */
			SUCCESS
		}
	}
}