package uk.codingbadgers.bFundamentals.module.loader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import uk.codingbadgers.bFundamentals.module.Module;

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
 * Loader - Loader base for loading Loadables.
 *
 * @author NodinChan
 */
public class Loader implements Listener {
	
	/** The plugin. */
	private final Plugin plugin;
	
	/** The dir. */
	private final File dir;
	
	/** The loader. */
	private ClassLoader loader;
	
	/** The files. */
	private final List<File> files;
	
	/** The loadables. */
	private final List<Module> loadables;
	
	/**
	 * Instantiates a new loader.
	 *
	 * @param plugin the plugin
	 * @param dir the directory to load modules from
	 */
	public Loader(Plugin plugin, File dir) {
		this.plugin = plugin;
		this.dir = dir;
		this.files = Arrays.asList(dir.listFiles(new FileExtensionFilter(".jar")));
		this.loadables = new ArrayList<Module>();
		
		List<URL> urls = new ArrayList<URL>();
		
		for (File file : files) {
			try { 
				urls.add(file.toURI().toURL()); 
			} catch (MalformedURLException e) {
				e.printStackTrace(); 
			}
		}
		
		this.loader = URLClassLoader.newInstance(urls.toArray(new URL[0]), plugin.getClass().getClassLoader());
	}
	
	/**
	 * Gets the Logger.
	 *
	 * @return The Logger
	 */
	public Logger getLogger() {
		return plugin.getLogger();
	}
	
	/**
	 * Loads the all loadables in the directory specified.
	 *
	 * @return List of loaded loadables
	 */
	public final List<Module> load() {
		for (File file : files) {
			load(file);
		}
		return loadables;
	}
        
    /**
     * Load file into the loader.
     *
     * @param file the file
     * @return the list
     */
    public List<Module> load(File file) {
            try {
            	JarFile jarFile = new JarFile(file);
            	String mainClass = null;
                LoadableDescriptionFile ldf = null;
				
                if (jarFile.getEntry("path.yml") != null) {
                    JarEntry element = jarFile.getJarEntry("path.yml");
                    ldf = new LoadableDescriptionFile(jarFile.getInputStream(element));
                    mainClass = ldf.getMainClass();
                }
				
				if (mainClass != null) {
                    Class<?> clazz = Class.forName(mainClass, true, loader);
					
                    if (clazz != null) {
                    	Class<? extends Module> loadableClass = clazz.asSubclass(Module.class);
                    	@SuppressWarnings("unchecked")
						Constructor<? extends Module> constructor = (Constructor<? extends Module>) loadableClass.getConstructors()[0];
						Module loadable = constructor.newInstance();
                    	
                        if (loadables.contains(loadable)) {
                            getLogger().log(Level.WARNING, "The loadable " + file.getName() + " is already loaded and cannot be reloaded");
                            getLogger().log(Level.WARNING, "The JAR file " + file.getName() + " failed to load");
                            jarFile.close();
                            return loadables;
                        }
                        
                        loadable.setFile(file);
                        loadable.setDesciption(ldf);
                        loadable.setJarFile(jarFile);
                        loadable.setDatafolder(new File(file.getParentFile(), loadable.getName()));
                        
                        loadable.init();
                        
                        loadables.add(loadable);

                        LoadEvent<Loadable> event = new LoadEvent<Loadable>(plugin, loadable, jarFile);
                        plugin.getServer().getPluginManager().callEvent(event);
                           
                    } else { 
                        jarFile.close(); throw new ClassNotFoundException(); 
                    }

                } else { 
                   jarFile.close(); throw new ClassNotFoundException(); 
                }
				
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    getLogger().log(Level.WARNING, "The JAR file " + file.getName() + " is in the wrong directory");
                    getLogger().log(Level.WARNING, "The JAR file " + file.getName() + " failed to load");			
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    getLogger().log(Level.WARNING, "Invalid path.yml");
                    getLogger().log(Level.WARNING, "The JAR file " + file.getName() + " failed to load");
				
                } catch (Exception e) {
                    e.printStackTrace();
                    getLogger().log(Level.WARNING, "Unknown cause");
                    getLogger().log(Level.WARNING, "The JAR file " + file.getName() + " failed to load");
                }
            return loadables;
        }
	
	/**
	 * Reloads the Loader.
	 *
	 * @return the list of modules loaded
	 */
	public List<Module> reload() {
		unload();
		
		List<URL> urls = new ArrayList<URL>();
		
		for (String loadableFile : dir.list()) {
			if (loadableFile.endsWith(".jar")) {
				File file = new File(dir, loadableFile);
				files.add(file);
				
				try { urls.add(file.toURI().toURL()); } catch (MalformedURLException e) { e.printStackTrace(); }
			}
		}
		
		this.loader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]), plugin.getClass().getClassLoader());
		
		return load();
	}
	
	/**
	 * Sorts a list of Loadables by name in alphabetical order.
	 *
	 * @param loadables The list of Loadables to sort
	 * @return The sorted list of Loadables
	 */
	public List<Module> sort(List<Module> loadables) {
		List<Module> sortedLoadables = new ArrayList<Module>();
		List<String> names = new ArrayList<String>();
		
		for (Loadable t : loadables)
			names.add(t.getName());
		
		Collections.sort(names);
		
		for (String name : names) {
			for (Module t : loadables) {
				if (t.getName().equals(name))
					sortedLoadables.add(t);
			}
		}
		
		return sortedLoadables;
	}
	
	/**
	 * Sorts a map of Loadables by name in alphabetical order.
	 *
	 * @param loadables The map of Loadables to sort
	 * @return The sorted map of Loadables
	 */
	public Map<String, Module> sort(Map<String, Module> loadables) {
		Map<String, Module> sortedLoadables = new HashMap<String, Module>();
		List<String> names = new ArrayList<String>(loadables.keySet());
		
		Collections.sort(names);
		
		for (String name : names)
			sortedLoadables.put(name, loadables.get(name));
		
		return sortedLoadables;
	}
	
	/**
	 * Unloads the Loader.
	 */
	public void unload() {
		loadables.clear();
	}
}