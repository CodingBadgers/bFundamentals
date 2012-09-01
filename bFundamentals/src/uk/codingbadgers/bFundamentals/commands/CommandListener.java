package uk.codingbadgers.bFundamentals.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

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
		String[] args = event.getMessage().indexOf(' ') != -1 ? event.getMessage().substring(event.getMessage().indexOf(' ') + 1).split(" ") : new String[0];
		
		List<Module> modules = bFundamentals.getModuleLoader().getModules();
		for (Module module : modules) {
			if (module.isCommandRegistered(command)) {
				if (module.onCommand(sender, command, args)) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}
	
	/**
	 * On player command pre process.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerCommandPreProcess(ServerCommandEvent event) {
		CommandSender sender = event.getSender();
		String command = event.getCommand().substring(1, event.getCommand().indexOf(' ') != -1 ? event.getCommand().indexOf(' ') : event.getCommand().length());
		String[] args = event.getCommand().indexOf(' ') != -1 ? event.getCommand().substring(event.getCommand().indexOf(' ') + 1).split(" ") : new String[0];
		
		List<Module> modules = bFundamentals.getModuleLoader().getModules();
		for (Module module : modules) {
			if (module.isCommandRegistered(command)) {
				if (module.onCommand(sender, command, args)) {
					// need to find a way to cancel the event
					return;
				}
			}
		}
	}
}
