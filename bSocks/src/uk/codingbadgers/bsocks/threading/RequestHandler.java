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
package uk.codingbadgers.bsocks.threading;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import uk.codingbadgers.bsocks.bSocksModule;
import uk.codingbadgers.bsocks.commands.WebCommand;
import uk.codingbadgers.bsocks.commands.WebCommandHandler;

public class RequestHandler extends Thread {
	
	final private Socket m_sock;
	final private String m_passHash;
	final private JavaPlugin m_plugin;
	final private bSocksModule m_module;

	/**
	 * Instantiates a new request handler.
	 *
	 * @param plugin the plugin instance
	 * @param passHash the password as an md5 hash
	 * @param sock the socket the connection was made on
	 * @param module the module instance
	 */
	public RequestHandler(JavaPlugin plugin, String passHash, Socket sock, bSocksModule module) {
		m_sock = sock;
		m_plugin = plugin;
		m_passHash = passHash;
		m_module = module;
	}

	/**
	 * Run the request handler thread
	 */	
	public void run() {
		
		JSONParser parser = new JSONParser();
		try {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(m_sock.getInputStream()));
			
			JSONObject json = null;
			String line = br.readLine();
			try {
				json = (JSONObject)parser.parse(line);
			} catch (ParseException pe) {
				m_module.log(Level.WARNING, "Error Parsing: " + line);
				m_module.log(Level.WARNING, pe.toString());
				br.close();
				m_sock.close();
				return;
			}
            
			if (json == null) {
				m_module.log(Level.SEVERE, "Invalid request from " + m_sock.getInetAddress().getHostAddress());
                br.close();
                m_sock.close();
                return;
			}

    		if (json.get("password") == null || !json.get("password").equals(m_passHash)) {
    			m_module.log(Level.SEVERE, "Wrong password from " + m_sock.getInetAddress().getHostAddress());
                br.close();
                m_sock.close();
                return;
            }
    		
    		String type = (String)json.get("command");

			if (type.equals("message")) {
				handleMessage(json);	
			} else if (type.equals("serverstats")) {
				handleServerStatsCommand(json, m_sock);
			} else if (type.equals("lookup")) {
				handleLookup(json, m_sock);
			} else if (type.equals("executeCommand")) {
				handleExecuteCommand(json);
			} else {
				for (WebCommand command : WebCommandHandler.getCommands()) {
					if (command.getLabel().equalsIgnoreCase(type)) {
						JSONObject responce = command.handleCommand(json);
						
						if (responce != null) {
							BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(m_sock.getOutputStream()));
							bw.write(responce.toJSONString() + "\n");
							bw.flush();
							bw.close();
						}
						
						break;
					}
				}
			}
			br.close();
			m_sock.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	

	/**
	 * Handle lookup. Looks for a player whos name starts with a given string
	 *
	 * @param command the command
	 * @param sock the sock
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	private void handleLookup(JSONObject command, Socket sock) throws IOException {
		JSONObject responce = new JSONObject();
		JSONArray players = new JSONArray();
		
		for (OfflinePlayer player : m_plugin.getServer().getOfflinePlayers()) {
			if (player.getName().startsWith((String) command.get("player"))) {
				players.add(player.getName());
			}
		}
		
		responce.put("players", players);
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		bw.write(responce.toJSONString() + "\n");
		bw.flush();
		bw.close();
	}

	/**
	 * Handle server stats command. Returning information about the server and its players
	 *
	 * @param command the command
	 * @param sock the sock
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	private void handleServerStatsCommand(JSONObject command, Socket sock) throws Exception {
		
		Server server = m_plugin.getServer();
		Player[] onlinePlayers = server.getOnlinePlayers();
		Runtime runtime = Runtime.getRuntime();
		
		JSONObject responce = new JSONObject();
		
		responce.put("bukkit-version", server.getBukkitVersion());
		responce.put("server-name", server.getServerName());
		responce.put("using-memory-bytes", (runtime.totalMemory() - runtime.freeMemory()));
		responce.put("max-memory-bytes", runtime.maxMemory());
		responce.put("noof-processors", runtime.availableProcessors());

		responce.put("max-players", Integer.toString(server.getMaxPlayers()));
		responce.put("online-players", Integer.toString(onlinePlayers.length));
		
		JSONArray players = new JSONArray();
		
		for (Player player : onlinePlayers) {
			JSONObject jplayer = new JSONObject();
			jplayer.put("player-name", player.getName());
			jplayer.put("player-display-name", player.getDisplayName());
			
			JSONArray groups = new JSONArray();
			for (String group : m_module.getPermissions().getPlayerGroups(player)) {
				JSONObject jgroup = new JSONObject();
				jgroup.put("group", group);
				groups.add(jgroup);
			}
			jplayer.put("rank-name", groups);
			players.add(jplayer);
		}
		
		responce.put("online-players", players);
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		bw.write(responce.toJSONString() + "\n");
		bw.flush();
		bw.close();	
	}
	
	/**
	 * Handle a message request, either sending a message to a single player, the entire server
	 * or the entire server excluding a given player.
	 *
	 * @param command the command
	 */
	private void handleMessage(JSONObject command) {
	
		String mode = (String)command.get("mode");
				
		// send a message to a single player
		if (mode.equalsIgnoreCase("sendMessage")) {
			String playerName = (String) command.get("playerName");
			final String message = formatMessage((String) command.get("context"));
			
			m_module.log(Level.INFO, "Sending message '" + message + "' to " + playerName);
			
			final Player player = m_plugin.getServer().getPlayer(playerName);
			if (player != null) {
				runSync(new Runnable() {
					public void run() {
						player.sendMessage(message);
					}
				});
			}
			
		} 
		// send a message to all players on the server
		else if (mode.equalsIgnoreCase("sendMessageAll")) {
			final String message = formatMessage((String)command.get("context"));
			
			m_module.log(Level.INFO, "Sending message '" + message + "' to all players");

			runSync(new Runnable() {
				public void run() {
					m_plugin.getServer().broadcastMessage(message);
				}
			});
			
		} 
		// message all players, excluding a given player
		else if (mode.equalsIgnoreCase("sendMessageAllEx")) {
			Player excludePlayer = m_plugin.getServer().getPlayer((String)command.get("exludedPlayer"));

			final String message = formatMessage((String) command.get("context"));
			
			m_module.log(Level.INFO, " Sending message '" + message + "' to all players apart from " + excludePlayer.getName());
			
			Player[] players = m_plugin.getServer().getOnlinePlayers();
			for (int i = 0; i < players.length; ++i) {
				final Player p = players[i];
				if (p != excludePlayer) {
					runSync(new Runnable() {
						public void run() {
							p.sendMessage(message);
						}
					});
				}				
			}
		}
		else
		{
			// We got told to send a message, but with an unknown mode.
			m_module.log(Level.WARNING, "Unknown message mode '" + mode + "'");
		}
	}
	
	/**
	 * Run a task on the main server thread.
	 * 
	 * @param runnable
	 */
	private void runSync(Runnable runnable) {
		Bukkit.getScheduler().runTask(m_plugin, runnable);
	}

	/**
	 * Handle execute commands.
	 *
	 * @param command the command to handle
	 */
	private void handleExecuteCommand(JSONObject json) {
		
		// store the name of the sender of the command
		String senderName = (String)json.get("sender");
		
		CommandSender sender = null;
		// see if its a player command or a console command
		if (senderName.equalsIgnoreCase("Console"))
			sender = m_plugin.getServer().getConsoleSender();
		else 
			sender = m_plugin.getServer().getPlayer(senderName);
		
		// couldn't find a sender, so don't process the command
		if (sender == null) {
			m_module.log(Level.SEVERE,  "Player " + senderName + " does not exist");
			return;
		}

		// get the command 
		final String command = (String)json.get("context");	
		final CommandSender csender = sender;
		
		runSync(new Runnable() {
			public void run() {
				m_plugin.getServer().dispatchCommand(csender, command);	
			}
		});	
		m_module.log(Level.INFO, "Executed command '" + command + "' as " + senderName);
	}
	
	/**
	 * Format a message.
	 *
	 * @param message the message to format
	 * @return the formatted message
	 */
	private String formatMessage(String message) {

		message = message.trim();
		message = ChatColor.translateAlternateColorCodes('&', message);
		return message;
	}
	
}
