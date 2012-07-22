package uk.codingbadgers.bFundamentals.module;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;

import com.nodinchan.ncbukkit.loader.Loadable;

import uk.codingbadgers.bFundamentals.bFundamentals;

public class Module extends Loadable implements Listener {

	protected final bFundamentals plugin;
	protected FileConfiguration config;
	protected File configFile;
	private String version;
	
	public Module(String name, String version) {
		super(name);
		this.version = version;
		this.plugin = bFundamentals.getInstance();
		configFile = new File(getDataFolder() + File.separator  + "config.yml");
	}
	
	public void log(Level level, String string) {
		bFundamentals.log(level, "[" + super.getName() + "] " + string);
	}
	
	public final void register(Listener listener) {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}
	
	public void onEnable(){}
	public void onDisable(){}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return false;
	}
	
	public String getVersion() {
		return version;
	}

}