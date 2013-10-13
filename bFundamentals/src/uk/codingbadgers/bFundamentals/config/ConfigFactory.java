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
package uk.codingbadgers.bFundamentals.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import uk.codingbadgers.bFundamentals.config.annotation.Catagory;
import uk.codingbadgers.bFundamentals.config.annotation.Element;
import uk.codingbadgers.bFundamentals.config.deserialiser.ConfigMapper;

/**
 * A factory for load Config classes, for internal use only for a module use
 * {@link Module#registerConfig(Class)} to load a config.
 * 
 * @author James Fitzpatrick
 * @see ConfigFile
 */
public class ConfigFactory {

	/**
	 * Load a config class from a config file located in the datafolder of a 
	 * module.
	 *
	 * @param clazz the config class
	 * @param folder the data folder of the module
	 * @throws Exception the exception
	 */
	public static void load(Class<? extends ConfigFile> clazz, File folder) throws Exception {
		File configFile = new File(folder, clazz.getSimpleName().toLowerCase() + ".yml");
		
		if (!configFile.exists()) {
			configFile.createNewFile();
			createDefaultConfig(clazz, configFile);
		}
		
		loadConfig(clazz, configFile);
	}
	
	/**
	 * Load config from a specified file.
	 *
	 * @param clazz the config class
	 * @param configfile the config file
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws SecurityException the security exception
	 * @throws NoSuchFieldException the no such field exception
	 * @throws InstantiationException the instantiation exception
	 */
	public static void loadConfig(Class<? extends ConfigFile> clazz, File configfile) throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException, InstantiationException {
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

		Class<?>[] catagories = clazz.getClasses();
		catagories = Arrays.copyOf(catagories, catagories.length + 1);
		catagories = insertAtBegining(catagories, clazz);
		
		for (Class<?> catagory : catagories) {
			if (!catagory.isAnnotationPresent(Catagory.class) && catagory != clazz) {
				continue;
			}
			
			String catName = catagory.getSimpleName() + ".";
			
			if (catagory == clazz) {
				catName = "";
			} else if (catagory.getAnnotation(Catagory.class).value().length() != 0){
				catName = catagory.getAnnotation(Catagory.class).value() + ".";
			}
			Field[] elements = catagory.getFields();
			
			for (Field field : elements) {
				if (!Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				
				if (field.isAnnotationPresent(Element.class)) {
					String name = field.getName();
					
					Element element = field.getAnnotation(Element.class);
					
					if (element.value().length() > 0) {
						name = element.value();
					}
					
					removeModifier(field, Modifier.FINAL);
					ConfigMapper<?> mapper = element.mapper().newInstance();
					field.set(null, mapper.deserialise(config.get(catName + name, field.get(null))));
				}
			}
		}
	}
	
	private static Class<?>[] insertAtBegining(Class<?>[] catagories, Class<?> clazz) {
		Class<?>[] buffer = new Class<?>[catagories.length];
		buffer[0] = clazz;
		
		for (int i = 0; i< catagories.length - 1; i++) {
			buffer[i + 1] = catagories[i];
		}
		return buffer;
	}
	
	private static void removeModifier(Field field, int mod) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);
	    Field modifiersField = Field.class.getDeclaredField("modifiers");
	    modifiersField.setAccessible(true);
	    modifiersField.setInt(field, field.getModifiers() & ~mod);
	}
 
	/**
	 * Creates a new default config in a file from a specified config class.
	 *
	 * @param clazz the config class
	 * @param configfile the config file
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws InstantiationException the instantiation exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void createDefaultConfig(Class<? extends ConfigFile> clazz, File configfile) throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException {
		if (!configfile.exists()) {
			configfile.createNewFile();
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

		Class<?>[] catagories = clazz.getClasses();
		catagories = Arrays.copyOf(catagories, catagories.length + 1);
		catagories = insertAtBegining(catagories, clazz);
		
		for (Class<?> catagory : catagories) {
			if (!catagory.isAnnotationPresent(Catagory.class) && catagory != clazz) {
				continue;
			}
			
			String catName = catagory.getSimpleName() + ".";
			
			if (catagory == clazz) {
				catName = "";
			} else if (catagory.getAnnotation(Catagory.class).value().length() != 0){
				catName = catagory.getAnnotation(Catagory.class).value() + ".";
			}
			Field[] elements = catagory.getFields();
			
			for (Field field : elements) {
				if (!Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				
				if (field.isAnnotationPresent(Element.class)) {
					String name = field.getName();

					Element element = field.getAnnotation(Element.class);
					
					if (element.value().length() > 0) {
						name = element.value();
					}

					ConfigMapper mapper = element.mapper().newInstance();
					config.addDefault(catName + name, mapper.serialise(field.get(null)));
				}
			}
		}
		
		config.options().copyDefaults(true);
		config.save(configfile);
	}
}
