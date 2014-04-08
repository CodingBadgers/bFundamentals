/**
 * bRanks 1.2-SNAPSHOT
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
package uk.codingbadgers.branks;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.mcsg.double0negative.tabapi.TabAPI;
import ru.tehkode.permissions.PermissionEntity;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import ru.tehkode.permissions.events.PermissionEntityEvent;
import ru.tehkode.permissions.events.PermissionEntityEvent.Action;
import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;

public class bRanks extends Module implements Listener, PluginMessageListener {

	// TeamName and Team
	private HashMap<String, Team> m_rankScorboards = new HashMap<String, Team>();
	
	// Player Name and List Name
	private HashMap<String, String> m_allPlayers = new HashMap<String, String>();
	
	// The maximum length a players name can be
	private final int m_maxNameLength = 12;
	
	// Timer for update loop
	private int m_updateLoopTimerID = -1;
	
	// The update loop time delay
	private final Long m_updateTimeRate = 100L;
	
	// Require a tab update if true
	private boolean m_requireUpdate = true;
	
	/**
	 * Called when the module is disabled.
	 */
	public void onDisable() {		
		
		Bukkit.getScheduler().cancelTask(m_updateLoopTimerID);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			TabAPI.clearTab(player);
		}
		
		for (Team team : m_rankScorboards.values())	{
			team.unregister();
		}
		m_rankScorboards.clear();
		m_allPlayers.clear();
		
		Messenger messanger = Bukkit.getServer().getMessenger();
		messanger.unregisterOutgoingPluginChannel(m_plugin);
		messanger.unregisterIncomingPluginChannel(m_plugin);
	}

	/**
	 * Called when the module is loaded.
	 */
	public void onEnable() {
		register(this);	
		
		Messenger messanger = Bukkit.getServer().getMessenger();
		messanger.registerOutgoingPluginChannel(m_plugin, "BungeeCord");
		messanger.registerIncomingPluginChannel(m_plugin, "BungeeCord", this);
		
		m_allPlayers.clear();
		HashMap<String, String> rankMap = loadRankMappings();
		
		// Create a scoreboard for each rank
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard mainScoreboard = manager.getMainScoreboard();
		
		for (String group : this.getPermissions().getGroups()) {
			
			String prefix;
			if (rankMap != null && rankMap.containsKey(group))
				prefix = rankMap.get(group);
			else
				prefix = bFundamentals.getChat().getGroupPrefix((String)null, group);
						
			Team team = mainScoreboard.getTeam(group);
			if (team == null)
			{
				team = mainScoreboard.registerNewTeam(group);
			}
			
			prefix = ChatColor.translateAlternateColorCodes('&', prefix);
			prefix = prefix.length() > 16 ? ChatColor.stripColor(prefix) : prefix;
			prefix = prefix.length() > 16 ? prefix.substring(0, 15) : prefix;
			
			team.setPrefix(prefix);
			m_rankScorboards.put(group, team);
		}
		
		Player[] onlinePlayers = Bukkit.getOnlinePlayers();		
		for (Player player : onlinePlayers) {
			addPlayerToTeam(player, false);
		}
		
		// If citizens is installed, enable the listener and update all npcs
		if (Bukkit.getPluginManager().getPlugin("Citizens") != null) {
			this.register(new NpcListener(this));
			
			for (NPC npc : CitizensAPI.getNPCRegistry()) {

				if (npc.getEntity() == null || npc.getEntity().getType() != EntityType.PLAYER)
					continue;

				Player player = (Player)npc.getEntity();
				addPlayerToTeam(player, true);
			}
		}
		
		if (onlinePlayers.length != 0) {
			requestAllPlayers(onlinePlayers[0]);
		}
		
		updateTabList();
		
		doUpdateLoop();
	}
	
	private void doUpdateLoop() {
		
		m_updateLoopTimerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(m_plugin, new Runnable() {

			@Override
			public void run() {
				
				if (Bukkit.getOnlinePlayers().length != 0) {
					requestAllPlayers(Bukkit.getOnlinePlayers()[0]);
				}
				
			}
			
		}, m_updateTimeRate, m_updateTimeRate);
		
	}

	/**
	 * Load the rank mappings from the ranks config file
	 */
	private HashMap<String, String> loadRankMappings() {
		
		File rankMapFile = new File(this.getDataFolder() + File.separator + "ranks.json");
		if (!rankMapFile.exists()) {
			try {
				rankMapFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		String jsonContents = "";
		try {			
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(rankMapFile)));
		
			String inputLine;
            while ((inputLine = in.readLine()) != null) {
                jsonContents += inputLine;
            }
            
            in.close();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject rankJSON = (JSONObject)JSONValue.parse(jsonContents);		
		HashMap<String, String> rankMap = new HashMap<String, String>();		
		
		for (String group : this.getPermissions().getGroups())
		{
			String nickname = (String)rankJSON.get(group);
			if (nickname == null)
				continue;

			rankMap.put(group, nickname);
		}		

		return rankMap;
	}

	/**
	 * Called when a player joins
	 */	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {

		m_requireUpdate = true;
		
		final Player player = event.getPlayer();
		
		final String playerName = player.getPlayerListName();
		if (playerName.length() > m_maxNameLength) {
			player.setPlayerListName(playerName.substring(0, m_maxNameLength));
		}
		
		addPlayerToTeam(player, false);
		requestAllPlayers(player);	
		broadcastTabUpdate(player, 20L);
	}
	
	/**
	 * Called when a player leaves
	 */	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerLeave(PlayerQuitEvent event) {

		final Player player = event.getPlayer();
		m_allPlayers.remove(player.getName());
		updateTabList();
		broadcastTabUpdate(player, 0L);
		
	}
	
	/**
	 * Called when a player is kicked
	 */	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerKicked(PlayerKickEvent event) {

		final Player player = event.getPlayer();
		m_allPlayers.remove(player.getName());
		updateTabList();
		broadcastTabUpdate(player, 0L);
		
	}
	
	/**
	 * Called when a players rank changes
	 */
	@EventHandler
	public void onRankChange(PermissionEntityEvent event) {
		
		if (event.getAction() != Action.INHERITANCE_CHANGED)
			return;
		
		PermissionEntity entity = event.getEntity();
		Player player = Bukkit.getPlayer(entity.getName());
		if (player == null)
			return;
		
		addPlayerToTeam(player, false);		
		broadcastTabUpdate(player, 20L);
	}
	
	/**
	 * Add a given player to a team based upon their pex rank
	 */
	public void addPlayerToTeam(Player player, boolean isNPC) {
		final String rank = this.getPermissions().getPrimaryGroup(player);
		Team team = m_rankScorboards.get(rank);
		if (team != null) {
			team.addPlayer(Bukkit.getOfflinePlayer(player.getPlayerListName()));
			if (!isNPC) {
				m_allPlayers.put(player.getName(), player.getPlayerListName());
				updateTabList();
			}
		}
	}
	
	/**
	 * Request a list of all players on the bungee network
	 */	
	private void requestAllPlayers(final Player player) {
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(m_plugin, new Runnable() {

			@Override
			public void run() {
				ByteArrayOutputStream b = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(b);
		
				try {
					out.writeUTF("PlayerList");
					out.writeUTF("ALL");
					player.sendPluginMessage(m_plugin, "BungeeCord", b.toByteArray());
				} catch(Exception ex) {}
			}
			
		}, 20L);
	}
	
	/**
	 * Broadcast a tab update across the bungee network
	 */	
	private void broadcastTabUpdate(final Player player, Long delay) {
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(m_plugin, new Runnable() {

			@Override
			public void run() {

				ByteArrayOutputStream b = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(b);
		
				try {
					out.writeUTF("Forward");
					out.writeUTF("ALL");
					out.writeUTF("UpdateTab"); 
					
					ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
					DataOutputStream msgout = new DataOutputStream(msgbytes);
					msgout.writeUTF("SexyLemons");
					 
					out.writeShort(msgbytes.toByteArray().length);
					out.write(msgbytes.toByteArray());
					
					player.sendPluginMessage(m_plugin, "BungeeCord", b.toByteArray());

				} catch(Exception ex) {}

			}
			
		}, delay);
	}

	/**
	 * Called when a plugin message is received
	 */	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {

		if (!channel.equals("BungeeCord")) {
            return;
        }

		try {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        	String subchannel = in.readUTF();

        	if (subchannel.equalsIgnoreCase("PlayerList")) {
        		in.readUTF(); // The name of the server you got the player list of, as given in args.
        		
        		ArrayList<String> playerList = new ArrayList<String>();
        		String[] playerArray = in.readUTF().split(", ");
        		for (String playerName : playerArray) {
        			playerList.add(playerName);
        		}
        		
        		HashMap<String, String> oldPlayerList = new HashMap<String, String>();
        		oldPlayerList.putAll(m_allPlayers);

        		for (String playerName : oldPlayerList.keySet()) {
        			if (!playerList.contains(playerName)) {
        				m_allPlayers.remove(playerName);
        				m_requireUpdate = true;
        			}
        		}
        		        		        		
        		for (String playerName : playerList) {
        			        			
        			if (m_allPlayers.containsKey(playerName))
        				continue;
        			
        			m_requireUpdate = true;
        			        			
        			final String rank = this.getPermissions().getPrimaryGroup((String)null, playerName);
        			Team team = m_rankScorboards.get(rank);
        			if (team != null) {
        				
        				String listName = playerName;        				
        				if (listName.length() > m_maxNameLength) {
        					listName = listName.substring(0, m_maxNameLength);
            			}

        				team.addPlayer(Bukkit.getOfflinePlayer(listName));
        				m_allPlayers.put(playerName, listName);
        			}
        		}
        		
        		updateTabList();
        		
        	}
        	else if (subchannel.equalsIgnoreCase("UpdateTab")) {

        		Bukkit.getScheduler().scheduleSyncDelayedTask(m_plugin, new Runnable() {

					@Override
					public void run() {
						if (Bukkit.getOnlinePlayers().length != 0) {
		        			requestAllPlayers(Bukkit.getOnlinePlayers()[0]);
		        		}
					}
        			
        		}, 20L);
        		
        	}
        
		} catch(Exception ex) {}
		
	}

	/**
	 * Update the tab list for all players online
	 */	
	private void updateTabList() {
		
		if (!m_requireUpdate)
			return;
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			updateTabList(player.getName());
		}
		
		m_requireUpdate = false;
	}

	/**
	 * Update the tablist for a given player
	 */	
	private void updateTabList(final String playerToUpdateName) {
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(m_plugin, new Runnable() {

			@Override
			public void run() {
				
				Player player = Bukkit.getPlayer(playerToUpdateName);
				if (player == null)
					return;
				
				TabAPI.setPriority(m_plugin, player, 2);
				TabAPI.clearTab(player);
				
				TabAPI.setTabString(m_plugin, player, 0, 0, ChatColor.GOLD + "||============");
				TabAPI.setTabString(m_plugin, player, 0, 1, ChatColor.GOLD + Bukkit.getServerName());
				TabAPI.setTabString(m_plugin, player, 0, 2, ChatColor.GOLD + "============||");
				
				int x = 0;
				int y = 2;

				SortedMap<Integer, PermissionGroup> pexRanks = new TreeMap<Integer, PermissionGroup>(); 
				pexRanks.putAll(PermissionsEx.getPermissionManager().getRankLadder("default"));
								
				for (PermissionGroup group : pexRanks.values()) {
					
					Team team = m_rankScorboards.get(group.getName());
					if (team != null) {
						Set<OfflinePlayer> teamPlayers = team.getPlayers();
						for (OfflinePlayer teamPlayer : teamPlayers) {

							if (!m_allPlayers.containsValue(teamPlayer.getName()))
								continue;
							
							TabAPI.setTabString(m_plugin, player, y, x, teamPlayer.getName());
							x++;
							if (x >= 3) {
								y++;
								x = 0;
							}
						}
						
						/*if (teamPlayers.size() != 0 && x != 0) {
							y++;
							x = 0;
						}*/
					}
					
				}
								
				TabAPI.updatePlayer(player); 
			}

		}, 20L);
		
	}
}
