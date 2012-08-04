package uk.codingbadgers.bmonitor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Command Logging Listener
 *
 * @author james
 */
public class CommandListener implements Listener{

	
	/**
	 * The Class Command.
	 */
	public static class Command {
		
		/** The m_player. */
		public String m_player;
		
		/** The m_command. */
		public String m_command;
		
		/** The m_args. */
		public String m_args;
	}
	
	/**
	 * The Class ChatMessage.
	 */
	public static class ChatMessage {
		
		/** The m_player. */
		public String m_player;
		
		/** The m_message. */
		public String m_message;
	}
	
	/**
	 * On command pre process.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
		String message = event.getMessage();
		
		Command command = new Command();
		command.m_player = event.getPlayer().getName();
		if ( message.indexOf(' ') != -1) {
			command.m_command = message.substring(0, message.indexOf(' '));		
			command.m_args = message.substring(message.indexOf(' '));
		} else {	
			command.m_command = message;
			command.m_args = " ";
		}
		
		DatabaseManager.log(command);
	}
	
	/**
	 * On player chat.
	 *
	 * @param event the event
	 */
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		ChatMessage message = new ChatMessage();
		message.m_player = event.getPlayer().getName();
		message.m_message = event.getMessage();
		
		DatabaseManager.log(message);
	}
	
	/**
	 * On player join.
	 *
	 * @param event the event
	 */
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		DatabaseManager.logJoin(event.getPlayer());
	}
	
	/**
	 * On player quit.
	 *
	 * @param event the event
	 */
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		DatabaseManager.logQuit(event.getPlayer());
	}
	
	/**
	 * On player quit.
	 *
	 * @param event the event
	 */
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerKickEvent event) {
		DatabaseManager.logQuit(event.getPlayer());
	}
}
