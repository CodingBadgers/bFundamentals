package uk.codingbadgers.bshutdown;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.server.ServerShutdownEvent.ShutdownCause;
import org.spigotmc.event.server.ServerShutdownEvent;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;

public class bShutdown extends Module implements Listener {

	@Override
	public void onEnable() {
		register(this);
	}

	@Override
	public void onDisable() {
		
	}
	
	@EventHandler
	public void servershutdown(ServerShutdownEvent event) {
		if (event.getCause() != ShutdownCause.COMMAND) {
			return;
		}
		
		event.setCancelled(true);
		ShutdownRunnable runnable = new ShutdownRunnable(60);
		runnable.runTaskTimer(bFundamentals.getInstance(), 0, 20);
	}
}
