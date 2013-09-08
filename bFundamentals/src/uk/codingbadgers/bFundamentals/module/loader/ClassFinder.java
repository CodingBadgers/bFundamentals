package uk.codingbadgers.bFundamentals.module.loader;

import java.net.URL;
import java.util.Set;

import org.bukkit.event.Listener;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bFundamentals.module.ModuleClassLoader;

public class ClassFinder {

	public static Set<Class<? extends Module>> findModules(ModuleClassLoader loader, URL[] url) {
		Reflections reflect = new Reflections(new ConfigurationBuilder().addUrls(url).addClassLoader(loader).addClassLoader(ClassFinder.class.getClassLoader()));
		return reflect.getSubTypesOf(Module.class);
	}
	
	public static Set<Class<? extends Listener>> findListeners(ModuleClassLoader loader, URL[] url) {
		Reflections reflect = new Reflections(new ConfigurationBuilder().addUrls(url).addClassLoader(loader).addClassLoader(ClassFinder.class.getClassLoader()));
		return reflect.getSubTypesOf(Listener.class);
	}

}