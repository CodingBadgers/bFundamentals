package uk.codingbadgers.bFundamentals.commands;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
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
			
		CommandSender sender = event.getPlayer();
		String command = event.getMessage().substring(1, event.getMessage().indexOf(' ') != -1 ? event.getMessage().indexOf(' ') : event.getMessage().length());
		String[] args = event.getMessage().indexOf(' ') != -1 ? event.getMessage().substring(event.getMessage().indexOf(' ') + 1).split(" ") : new String[0];
		
		List<Module> modules = bFundamentals.getModuleLoader().getModules();
		for (Module module : modules) {
			if (module.isCommandRegistered(command)) {
				
				if (bFundamentals.getConfigurationManager().getDebug()){
					bFundamentals.log(Level.INFO, "[DEBUG] " + sender.getName() + " sent command " + command + " to " + module.getName());
				}
				
				if (module.onCommand(sender, command, args)) {
					if (bFundamentals.getConfigurationManager().getDebug()){
						bFundamentals.log(Level.INFO, "[DEBUG] command executed successfuly");
					}
					
					event.setCancelled(true);
					return;
				} else {
					if (bFundamentals.getConfigurationManager().getDebug()){
						bFundamentals.log(Level.INFO, "[DEBUG] command failed whilst executing");
					}
					
					sender.sendMessage("/" + command);
					event.setCancelled(true);
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
		if (event.getCommand() == null || event.getCommand().length() <= 0)
			return;
		
		CommandSender sender = event.getSender();
		String command = event.getCommand().substring(0, event.getCommand().indexOf(' ') != -1 ? event.getCommand().indexOf(' ') : event.getCommand().length());
		String[] args = event.getCommand().indexOf(' ') != -1 ? event.getCommand().substring(event.getCommand().indexOf(' ') + 1).split(" ") : new String[0];
		
		List<Module> modules = bFundamentals.getModuleLoader().getModules();
		for (Module module : modules) {
			if (module.isCommandRegistered(command)) {
				if (module.onCommand(sender, command, args)) {
					// need to find a way to cancel the event
					return;
				} else {
					sender.sendMessage("/" + command);
				}
			}
		}
	}
}
