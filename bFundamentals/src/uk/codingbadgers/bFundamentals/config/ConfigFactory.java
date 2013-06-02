package uk.codingbadgers.bFundamentals.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigFactory {

	public static void loadConfig(Class<? extends ConfigFile> clazz, File configfile) throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException {
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);
		Field[] fields = clazz.getFields();
		
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers()) || !Modifier.isPublic(field.getModifiers())) {
				continue;
			}
			
			if (field.isAnnotationPresent(Element.class)) {
				String name = field.getName();
				
				if (field.getAnnotation(Element.class).value().length() > 0) {
					name = field.getAnnotation(Element.class).value();
				}
				
				field.setAccessible(true);
			    Field modifiersField = Field.class.getDeclaredField("modifiers");
			    modifiersField.setAccessible(true);
			    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			    field.set(null, config.get(name));
			}
		}
	}
	
	public static void createDefaultConfig(Class<? extends ConfigFile> clazz, File configfile) throws IOException, IllegalArgumentException, IllegalAccessException {
		if (!configfile.exists()) {
			configfile.createNewFile();
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);
		Field[] fields = clazz.getFields();
		
		for (Field field : fields) {
			if (!Modifier.isStatic(field.getModifiers()) || !Modifier.isPublic(field.getModifiers())) {
				continue;
			}
			
			if (field.isAnnotationPresent(Element.class)) {
				String name = field.getName();
				
				if (field.getAnnotation(Element.class).value().length() > 0) {
					name = field.getAnnotation(Element.class).value();
				}
				
				System.out.println(name + ' ' + field.get(null));
				config.addDefault(name, field.get(null));
			}
		}
		
		config.options().copyDefaults(true);
		config.save(configfile);
	}
}
