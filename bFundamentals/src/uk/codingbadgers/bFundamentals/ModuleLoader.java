package uk.codingbadgers.bFundamentals;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.nodinchan.ncbukkit.loader.Loader;

import uk.codingbadgers.bFundamentals.module.Module;

/**
 * The ModuleLoader.
 */
public class ModuleLoader {
	
	/** The List of modules. */
	private final List<Module> m_modules;

	/**
	 * Instantiates a new module loader.
	 */
	public ModuleLoader() {
		m_modules = new LinkedList<Module>();
		if (getModuleDir().mkdir())
			bFundamentals.log(Level.INFO, "Creating Module Directory...");
	}
	
	/**
	 * Gets the directory of the modules.
	 *
	 * @return the module dir
	 */
	public File getModuleDir() {
		return new File(bFundamentals.getInstance().getDataFolder(), "modules");
	}
	
	/**
	 * Loads the modules.
	 */
	public void load() {
		Loader<Module> loader = new Loader<Module>(bFundamentals.getInstance(), getModuleDir());
		m_modules.addAll(loader.sort(loader.load()));
		
		bFundamentals.log(Level.INFO, "Loaded " + m_modules.size() + " modules.");
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
	 * @param module the module
	 */
	public void unload(Module module) {
		module.onDisable();
		m_modules.remove(module);
	}
	
	/**
	 * Run on enable in all modules.
	 */
	public void enable() {
		for (Module module : m_modules) {
			module.onEnable();
		}
	}
	
	/**
	 * run on disable in all modules.
	 */
	public void disable() {
		for (Module module : m_modules) {
			module.onDisable();
		}
	}
	
	/**
	 * On command.
	 *
	 * @param sender the sender
	 * @param cmd the cmd
	 * @param label the label
	 * @param args the args
	 * @return true, if successful
	 */
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		for (Module module : m_modules) {
			if (module.onCommand(sender, cmd, label, args))
				return true;
			else 
				continue;
		}
		
		return false;
	}

	/**
	 * Gets the modules.
	 *
	 * @return the modules
	 */
	public List<Module> getModules() {
		return m_modules;
	}

}
