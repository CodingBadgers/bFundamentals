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
import java.util.logging.Level;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
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
import ru.tehkode.permissions.events.PermissionEntityEvent;
import ru.tehkode.permissions.events.PermissionEntityEvent.Action;
import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;

public class bRanks extends Module implements Listener, PluginMessageListener {

	private ArrayList<String> m_players = new ArrayList<String>();
	private HashMap<String, Team> m_rankScorboards = new HashMap<String, Team>();
	
	private boolean m_useCitizens = false;
	
	/**
	 * Called when the module is disabled.
	 */
	public void onDisable() {		
		for (Team team : m_rankScorboards.values())
		{
			team.unregister();
		}
		m_rankScorboards.clear();
		m_players.clear();
	}

	/**
	 * Called when the module is loaded.
	 */
	public void onEnable() {
		register(this);		
		
		Messenger messenger = Bukkit.getServer().getMessenger();
		messenger.registerOutgoingPluginChannel(m_plugin, "BungeeCord");
		messenger.registerIncomingPluginChannel(m_plugin, "BungeeCord", this);
		
		HashMap<String, String> rankMap = loadRankMappings();
		
		// Create a scoreboard for each rank
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard mainScoreboard = manager.getMainScoreboard();
		
		for (String group : this.getPermissions().getGroups())
		{
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
		
		m_players.clear();
		
		for (Player player : Bukkit.getOnlinePlayers())	{
			addPlayerToTeam(player, true);
		}
		
		m_useCitizens = Bukkit.getPluginManager().getPlugin("Citizens") != null;
		if (m_useCitizens) {
			for (NPC npc : CitizensAPI.getNPCRegistry()) {
				LivingEntity entity = npc.getBukkitEntity();
				if (entity == null || !(entity instanceof Player))
					return;
	
				addPlayerToTeam((Player)entity, false);	
			}
		}
		
		requestServerPlayers();
	}
	
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

		final Player player = event.getPlayer();
		
		final String playerName = player.getPlayerListName();
		if (playerName.length() > 13) {
			player.setPlayerListName(playerName.substring(0, 13));
		}
		
		addPlayerToTeam(player, true);
	}
	
	/**
	 * Called when a player quits
	 */	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerLeave(PlayerQuitEvent event) {

		final Player player = event.getPlayer();
		m_players.remove(player.getName());
		updateTabList();
		
	}

	/**
	 * Called when a player is kicked
	 */	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerKick(PlayerKickEvent event) {

		final Player player = event.getPlayer();
		m_players.remove(player.getName());
		updateTabList();
		
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
		
		addPlayerToTeam(player, true);		
	}
	
	/**
	 * Called when a citizens NPC is spawned
	 */
	@EventHandler
	public void onNPCSpawn(NPCSpawnEvent event) {

		NPC npc = event.getNPC();
		LivingEntity entity = npc.getBukkitEntity();
		if (entity == null || !(entity instanceof Player))
			return;
			
		Player player = (Player)entity;
		addPlayerToTeam(player, false);		
	}
	
	/**
	 * Add a given player to a team based upon their pex rank
	 */
	private void addPlayerToTeam(Player player, boolean isPlayer) {
		final String rank = this.getPermissions().getPrimaryGroup(player);
		Team team = m_rankScorboards.get(rank);
		if (team != null) {
			team.addPlayer(Bukkit.getOfflinePlayer(player.getPlayerListName()));
			if (isPlayer) broadcastPlayerToTeam(player);
			updateTabList();
		}
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
			bFundamentals.log(Level.INFO, "Revcieved SubChannel - " + subchannel);
			if (subchannel.equalsIgnoreCase("bRanksPlayer")) {
				String playername = in.readUTF();
				m_players.add(playername);
				updateTabList();
			}
			else if (subchannel.equalsIgnoreCase("bRanksRequest")) {
				for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
					broadcastPlayerToTeam(onlineplayer);				
				}
			}			
		} catch (IOException e) {}
		
	}
	
	/**
	 * Broadcast a player added to team message across bungee
	 */
	private void broadcastPlayerToTeam(Player player) {
		m_players.add(player.getName());
		
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteOutput);
		
		bFundamentals.log(Level.INFO, "Broadcasting - " + player.getName());
		 
		try {
			out.writeUTF("Forward");
			out.writeUTF("ALL");
			out.writeUTF("bRanksPlayer");
			out.writeUTF(player.getName());
		} catch (IOException e) {}
		
		player.sendPluginMessage(m_plugin, "BungeeCord", byteOutput.toByteArray());
		
	}
	
	/**
	 * Broadcast a player added to team message across bungee
	 */
	private void requestServerPlayers() {

		Bukkit.getScheduler().scheduleSyncDelayedTask(m_plugin, new Runnable() {
			@Override
			public void run() {
				bFundamentals.log(Level.INFO, "Broadcasting - requestServerPlayers");
				 
				try {
					ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
					DataOutputStream out = new DataOutputStream(byteOutput);

					out.writeUTF("Forward");
					out.writeUTF("ALL");			
					out.writeUTF("bRanksRequest");
					out.writeUTF("bRanksRequest");

					Player player = Bukkit.getOnlinePlayers()[0];
					if (player != null) {
						player.sendPluginMessage(m_plugin, "BungeeCord", byteOutput.toByteArray());				
					}
				} catch (IOException e) {}
			}
		}, 20L);
		
	}
	
	/**
	 * Update tabAPI tab list
	 */
	private void updateTabList() {
				
		Bukkit.getScheduler().scheduleSyncDelayedTask(m_plugin, new Runnable() {

			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					TabAPI.setPriority(m_plugin, player, 2);
					TabAPI.clearTab(player);
					TabAPI.updatePlayer(player);
				}
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					updateTabList(player);
				}
			}
			
		}, 20L);
	}
	
	/**
	 * Update a given players tabAPI tab list
	 */
	private void updateTabList(Player player) {
		
		int x = 0;
		int y = 0;
		
		for (String playerName : m_players) {
			TabAPI.setTabString(m_plugin, player, y, x, playerName);
			x++;
			if (x >= 3) {
				x = 0;
				y++;
			}
		}
		TabAPI.updatePlayer(player);
		
	}
	
}
