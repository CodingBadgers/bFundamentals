package uk.codingbadgers.bFundamentals.module;

import org.bukkit.event.Listener;

import com.nodinchan.ncbukkit.loader.Loadable;

import uk.codingbadgers.bFundamentals.bFundamentals;

public class Module extends Loadable implements Listener {

	protected final bFundamentals plugin;
	
	public Module(String name) {
		super(name);
		this.plugin = bFundamentals.getInstance();
	}
	
	public final void register(Listener listener) {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}
}