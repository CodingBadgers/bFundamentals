package uk.codingbadgers.bFundamentals;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.nodinchan.ncbukkit.loader.Loader;

import uk.codingbadgers.bFundamentals.module.Module;

public class ModuleLoader {
	
	private final List<Module> m_modules;

	public ModuleLoader() {
		m_modules = new LinkedList<Module>();
		if (getModuleDir().mkdir())
			bFundamentals.log(Level.INFO, "Creating Module Directory...");
	}
	
	/**
	* Gets the directory of the modules
	*
	* @return
	*/
	public File getModuleDir() {
		return new File(bFundamentals.getInstance().getDataFolder(), "modules");
	}
	
	/**
	* Loads the modules
	*/
	public void load() {
		Loader<Module> loader = new Loader<Module>(bFundamentals.getInstance(), getModuleDir());
		m_modules.addAll(loader.sort(loader.load()));
		
		bFundamentals.log(Level.INFO, "Loaded " + m_modules.size() + " modules.");
	}
	
	/**
	* Unloads the modules
	*/
	public void unload() {
		m_modules.clear();
	}
	
	public void enable() {
		for (Module module : m_modules) {
			module.onEnable();
		}
	}
	
	public void disable() {
		for (Module module : m_modules) {
			module.onDisable();
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		for (Module module : m_modules) {
			if (module.onCommand(sender, cmd, label, args))
				return true;
			else 
				continue;
		}
		
		return false;
	}

}
