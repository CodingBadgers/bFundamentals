package uk.codingbadgers.bFundamentals.module.loader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.error.ExceptionHandler;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bFundamentals.module.ModuleClassLoader;
import uk.codingbadgers.bFundamentals.module.ModuleDescription;
import uk.codingbadgers.bFundamentals.module.ModuleHelpTopic;
import uk.codingbadgers.bFundamentals.module.annotation.ModuleInfo;
import uk.codingbadgers.bFundamentals.module.events.ModuleLoadEvent;

/**
 * The ModuleLoader.
 */
public class ModuleLoader {

	private Map<String, ModuleClassLoader> loaders = new HashMap<String, ModuleClassLoader>();
	private Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
	private List<Module> m_modules;

	/**
	 * Instantiates a new module loader.
	 */
	public ModuleLoader() {
		m_modules = new LinkedList<Module>();
		if (bFundamentals.getConfigurationManager().getModuleDirectory().mkdir()) {
			bFundamentals.log(Level.INFO, "Creating Module Directory...");
		}
	}

	/**
	 * Gets the directory of the modules.
	 * 
	 * @return the module dir
	 * @deprecated {@link uk.codingbadgers.bFundamentals.ConfigManager.getModuleDirectory)}
	 */
	public File getModuleDir() {
		return bFundamentals.getConfigurationManager().getModuleDirectory();
	}

	/**
	 * Loads all the modules in the base modules directory.
	 */
	public void load() {

		List<File> files = Arrays.asList(bFundamentals.getConfigurationManager().getModuleDirectory().listFiles(new FileExtensionFilter(".jar")));
		for (File file : files) {
			loadModule(file);
		}

		sort();

		for (Module module : m_modules) {
			try {
				module.onLoad();
				module.log(Level.INFO, module.getName() + " v:" + module.getVersion() + " has been loaded successfuly");
			} catch (Exception ex) {
				ExceptionHandler.handleException(ex);
			}
		}

		bFundamentals.log(Level.INFO, "Loaded " + m_modules.size() + " modules.");
	}

	public Module loadModule(File file) {
		Module result = null;

		try {
			URL[] urls = new URL[] { file.toURI().toURL() };

			ModuleClassLoader loader = new ModuleClassLoader(this, urls, getClass().getClassLoader());

			JarFile jarFile = new JarFile(file);
			ModuleDescription moduledescription = null;
			Set<Class<? extends Module>> modules = new HashSet<Class<? extends Module>>();

			modules.addAll(ClassFinder.findModules(loader, urls));
			
			// Old system, left for backwards compatibility
			if (jarFile.getEntry("path.yml") != null) {
				JarEntry element = jarFile.getJarEntry("path.yml");
				moduledescription = new ModuleDescription(jarFile.getInputStream(element));
			}

			if (modules.size() == 0) {
				jarFile.close();
				throw new ClassNotFoundException("Could not find a main class in jar " + file.getName() + ".");
			}

			if (bFundamentals.getConfigurationManager().isDebugEnabled()) {
				getLogger().log(Level.INFO, "Loading " + modules.size() + " modules for jar " + file.getName());
			}
			
			for (Class<? extends Module> clazz : modules) {

				if (bFundamentals.getConfigurationManager().isDebugEnabled()) {
					getLogger().log(Level.INFO, "Loading clazz " + clazz.getName());
				}
				
				ModuleDescription description = moduledescription;
				
				if (clazz.isAnnotationPresent(ModuleInfo.class)) {
					ModuleInfo info = clazz.getAnnotation(ModuleInfo.class);
					description = new ModuleDescription(info, clazz.getName());
				}
				
				if (description == null) {
					throw new IOException("Description not found for module " + file.getName() + ".");
				}
				
				result = clazz.newInstance();

				if (m_modules.contains(result)) {
					getLogger().log(Level.WARNING, "The loadable " + file.getName() + " is already loaded, make sure to disable the module first");
					getLogger().log(Level.WARNING, "The JAR file " + file.getName() + " failed to load");
					jarFile.close();
					return null;
				}

				result.setFile(file);
				result.setDesciption(description);
				result.setJarFile(jarFile);
				result.setDatafolder(new File(bFundamentals.getConfigurationManager().getModuleDirectory(), result.getName()));
				result.setClassLoader(loader);
				result.init();

				ModuleLoadEvent event = new ModuleLoadEvent(bFundamentals.getInstance(), result, jarFile);
				Bukkit.getServer().getPluginManager().callEvent(event);

				m_modules.add(result);
				loaders.put(result.getName(), loader);
			}

		} catch (ClassCastException e) {
			e.printStackTrace();
			getLogger().log(Level.WARNING, "The JAR file " + file.getName() + " is in the wrong directory.");
			getLogger().log(Level.WARNING, "The JAR file " + file.getName() + " failed to load.");
		} catch (ClassNotFoundException e) {
			//getLogger().log(Level.WARNING, "Invalid path.yml."); Path.yml's are now deprecated
			getLogger().log(Level.WARNING, e.getMessage());
			getLogger().log(Level.WARNING, "The JAR file " + file.getName() + " failed to load.");
		} catch (Exception e) {
			ExceptionHandler.handleException(e);
			getLogger().log(Level.WARNING, "Unknown cause.");
			getLogger().log(Level.WARNING, e.getMessage());
			getLogger().log(Level.WARNING, "The JAR file " + file.getName() + " failed to load.");
		}

		return result;
	}

	private Logger getLogger() {
		return bFundamentals.getInstance().getLogger();
	}

	/**
	 * Loads a module with a given name
	 * 
	 * @param fileName
	 *            the files name
	 */
	public void load(String fileName) {
		File module = new File(bFundamentals.getConfigurationManager().getModuleDirectory() + File.separator + fileName + ".jar");
		load(module);
	}

	/**
	 * Loads a module with a jar file
	 * 
	 * @param file
	 *            the jar file for this module
	 */
	public void load(File file) {
		if (getModule(file) != null) {
			throw new IllegalArgumentException("Module " + file.getName() + " is already loaded");
		}

		Module result = loadModule(file);

		if (result == null) {
			return;
		}

		m_modules.add(result);

		result.onLoad();
		result.log(Level.INFO, result.getName() + " v:" + result.getVersion() + " has been loaded successfuly");
	}

	/**
	 * Unloads the modules.
	 */
	public void unload() {
		disable();
		m_modules.clear();
	}

	/**
	 * Unload a specific module.
	 * 
	 * @param module
	 *            the module
	 */
	public void unload(Module module) {
		try {
			module.setEnabled(false);
			m_modules.remove(module);
		} catch (Exception ex) {
			ExceptionHandler.handleException(ex);
		}
	}

	/**
	 * Run on enable in all modules.
	 */
	public void enable() {
		for (Module module : m_modules) {
			try {
				module.setEnabled(true);
				Bukkit.getHelpMap().addTopic(new ModuleHelpTopic(module));
			} catch (Exception ex) {
				ExceptionHandler.handleException(ex);
			}
		}
	}

	/**
	 * run on disable in all modules.
	 */
	public void disable() {
		List<Module> modules = new ArrayList<Module>(m_modules);
		for (Module module : modules) {
			try {
				unload(module);
			} catch (Exception ex) {
				ExceptionHandler.handleException(ex);
			}
		}
	}

	/**
	 * Gets the modules.
	 * 
	 * @return the modules
	 */
	public List<Module> getModules() {
		return m_modules;
	}

	/**
	 * Update all the loaded modules if an updater is set
	 */
	public void update() {
		if (!bFundamentals.getConfigurationManager().isAutoUpdateEnabled()) {
			return;
		}

		for (Module module : m_modules) {
			module.update();
		}
	}

	/**
	 * Gets the module from its name.
	 * 
	 * @param string
	 *            the string
	 * @return the module
	 */
	public Module getModule(String string) {
		Iterator<Module> itr = m_modules.iterator();
		while (itr.hasNext()) {
			Module module = itr.next();
			if (module.getName().equalsIgnoreCase(string)) {
				return module;
			}
		}
		return null;
	}

	/**
	 * Gets the module from its file.
	 * 
	 * @param file
	 *            the file
	 * @return the module
	 */
	public Module getModule(File file) {
		Iterator<Module> itr = m_modules.iterator();
		while (itr.hasNext()) {
			Module module = itr.next();
			if (module.getFile().getPath().equalsIgnoreCase(file.getPath())) {
				return module;
			}
		}
		return null;
	}

	public Class<?> getClassByName(String name) {
		Class<?> cachedClass = classes.get(name);

		if (cachedClass != null) {
			return cachedClass;
		} else {
			for (String current : loaders.keySet()) {
				ModuleClassLoader loader = loaders.get(current);
				try {
					cachedClass = loader.findClass(name, false);
				} catch (ClassNotFoundException cnfe) {
				}

				if (cachedClass != null) {
					return cachedClass;
				}
			}
		}
		return null;
	}

	public void setClass(String name, Class<?> clazz) {
		if (!classes.containsKey(name)) {
			classes.put(name, clazz);

			if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
				Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
				ConfigurationSerialization.registerClass(serializable);
			}
		}
	}

	public void sort() {
		List<Module> sortedLoadables = new ArrayList<Module>();
		List<String> names = new ArrayList<String>();

		for (Module t : m_modules) {
			names.add(t.getName());
		}

		Collections.sort(names);

		for (String name : names) {
			for (Module t : m_modules) {
				if (t.getName().equals(name)) {
					sortedLoadables.add(t);
				}
			}
		}

		m_modules = sortedLoadables;
	}

}
