package uk.codingbadgers.bFundamentals.module.loader;

import java.net.URL;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import uk.codingbadgers.bFundamentals.module.Module;

public class ModuleClassFinder {

	public static Set<Class<? extends Module>> findModules(URL[] url) {
		Reflections reflect = new Reflections(new ConfigurationBuilder().addUrls(url));
		return reflect.getSubTypesOf(Module.class);
	}

}
