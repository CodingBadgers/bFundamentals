package uk.codingbadgers.bFundamentals.commands;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;

/**
 *
 * @see CommandEvent
 */
public class CommandListener implements Listener {

	/**
	 * On player command pre process.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent event) {
		Player sender = event.getPlayer();
		String command = event.getMessage().substring(1, event.getMessage().indexOf(' ') != -1 ? event.getMessage().indexOf(' ') : event.getMessage().length());
		String[] args = event.getMessage().indexOf(' ') != -1 ? event.getMessage().substring(event.getMessage().indexOf(' ')).split(" ") : new String[0];
	
		List<Module> modules = bFundamentals.getInstance().m_moduleLoader.getModules();
		for (Module module : modules) {
			if (module.isCommandRegistered(command)) {
				if (module.onCommand(sender, command, args)) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}
}
