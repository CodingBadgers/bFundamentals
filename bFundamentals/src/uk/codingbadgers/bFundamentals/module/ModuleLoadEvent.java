package uk.codingbadgers.bFundamentals.module;

import java.util.jar.JarFile;

import org.bukkit.plugin.Plugin;

import uk.codingbadgers.bFundamentals.module.loader.LoadEvent;

public class ModuleLoadEvent extends LoadEvent<Module> {

	public ModuleLoadEvent(Plugin plugin, Module loadable, JarFile jarFile) {
		super(plugin, loadable, jarFile);
	}

}
