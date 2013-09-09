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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import ru.tehkode.permissions.PermissionEntity;
import ru.tehkode.permissions.events.PermissionEntityEvent;
import ru.tehkode.permissions.events.PermissionEntityEvent.Action;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;

public class bRanks extends Module implements Listener {

	private HashMap<String, Team> m_rankScorboards = new HashMap<String, Team>();
	
	/**
	 * Called when the module is disabled.
	 */
	public void onDisable() {		
		for (Team team : m_rankScorboards.values())
		{
			team.unregister();
		}
		m_rankScorboards.clear();
	}

	/**
	 * Called when the module is loaded.
	 */
	public void onEnable() {
		register(this);		
		
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
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			final String rank = this.getPermissions().getPrimaryGroup(player);
			Team team = m_rankScorboards.get(rank);
			if (team != null)
			{
				team.addPlayer(player);
			}
		}
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
		
		this.log(Level.INFO, jsonContents);
		
		JSONObject rankJSON = (JSONObject)JSONValue.parse(jsonContents);		
		HashMap<String, String> rankMap = new HashMap<String, String>();		
		
		for (String group : this.getPermissions().getGroups())
		{
			String nickname = (String)rankJSON.get(group);
			if (nickname == null)
				continue;
			
			this.log(Level.INFO, "Loading rank map for '" + group + "' -> '" + nickname + "'");
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
		if (playerName.length() >= 12) {
			player.setPlayerListName(playerName.substring(0, 11));
		}
		
		final String rank = this.getPermissions().getPrimaryGroup(player);
		Team team = m_rankScorboards.get(rank);
		if (team != null)
		{
			team.addPlayer(Bukkit.getOfflinePlayer(player.getPlayerListName()));
		}
	}
	
	@EventHandler
	public void onRankChange(PermissionEntityEvent event) {
		
		if (event.getAction() != Action.INHERITANCE_CHANGED)
			return;
		
		PermissionEntity entity = event.getEntity();
		Player player = Bukkit.getPlayer(entity.getName());
		if (player == null)
			return;
		
		final String rank = this.getPermissions().getPrimaryGroup(player);
		Team team = m_rankScorboards.get(rank);
		if (team != null)
		{
			team.addPlayer(player);
		}
		
	}

}
