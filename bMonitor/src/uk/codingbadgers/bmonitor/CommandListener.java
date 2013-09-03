/**
 * bFundamentalsBuild 1.2-SNAPSHOT
 * Copyright (C) 2013  CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
		
		/** The player who used the command. */
		public String m_player;
		
		/** The command used. */
		public String m_command;
		
		/** The arguments that went with the command. */
		public String m_args;
	}
	
	/**
	 * The Class ChatMessage.
	 */
	public static class ChatMessage {
		
		/** The player who sent the message. */
		public String m_player;
		
		/** The message sent. */
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
