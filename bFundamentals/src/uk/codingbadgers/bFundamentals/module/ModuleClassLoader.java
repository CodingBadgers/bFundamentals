package uk.codingbadgers.bFundamentals.module;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

import uk.codingbadgers.bFundamentals.module.loader.ModuleLoader;

public class ModuleClassLoader extends URLClassLoader {
	private final ModuleLoader loader;
	private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();

	public ModuleClassLoader(final ModuleLoader moduleLoader, final URL[] urls, final ClassLoader parent) {
		super(urls, parent);
		Validate.notNull(moduleLoader, "Loader cannot be null");
		this.loader = moduleLoader;
	}

	@Override
	public void addURL(URL url) {
		super.addURL(url);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return findClass(name, true);
	}

	public Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
		if (name.startsWith("org.bukkit.") || name.startsWith("net.minecraft.")) {
			throw new ClassNotFoundException(name);
		}
		Class<?> result = classes.get(name);

		if (result == null) {
			if (checkGlobal) {
				result = loader.getClassByName(name);
			}

			if (result == null) {
				result = super.findClass(name);

				if (result != null) {
					loader.setClass(name, result);
				}
			}

			classes.put(name, result);
		}

		return result;
	}

	public Set<String> getClasses() {
		return classes.keySet();
	}

}
