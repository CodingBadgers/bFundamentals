/**
 * bHelpful 1.2-SNAPSHOT
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
package uk.codingbadgers.bHelpful.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bHelpful.Config;
import uk.codingbadgers.bHelpful.bHelpful;

public class PlayerListCommand extends ConfigCommand {

	public PlayerListCommand() {
		super(Config.LIST_LABEL, "/list", false);
		HelpfulCommandHandler.registerCommand(this);
	}

	@Override
	protected void handleCommand(CommandSender sender, String label, String[] args) {
		if (sender instanceof ConsoleCommandSender) {
			return;
		}
		
		listPlayers(sender);
	}
	
	public static void displayList(CommandSender sender)  {
		StringBuilder out = new StringBuilder();

		out.append(listPlayers(sender));
		
	    String[] lines = out.toString().split("\n");

	    for (String line : lines) {
	    	sender.sendMessage(line);
	    }
	}
	
	private static String getRank(World world, String group) {
        
        String prefix = bFundamentals.getChat().getGroupPrefix(world, group);
        
        while(prefix.indexOf("&") != -1) {
			String code = prefix.substring(prefix.indexOf("&") + 1, prefix.indexOf("&") + 2);
			prefix = prefix.substring(0, prefix.indexOf("&")) +  ChatColor.getByChar(code) + prefix.substring(prefix.indexOf("&") + 2);								
		}
        
        while(prefix.indexOf("[") != -1) {
			prefix = prefix.substring(0, prefix.indexOf("[")) + prefix.substring(prefix.indexOf("[") + 1);						
		}
        
        while(prefix.indexOf("]") != -1) {
			prefix = prefix.substring(0, prefix.indexOf("]")) + prefix.substring(prefix.indexOf("]") + 1);
		}
        
        while(prefix.indexOf("<") != -1) {
			prefix = prefix.substring(0, prefix.indexOf("<")) + prefix.substring(prefix.indexOf("<") + 1);						
		}
        
        while(prefix.indexOf(">") != -1) {
			prefix = prefix.substring(0, prefix.indexOf(">")) + prefix.substring(prefix.indexOf(">") + 1);
		}
        
        return prefix.length() > 0 ? prefix : group;
	}
	
	private static String listPlayers(CommandSender sender) {
		StringBuilder out = new StringBuilder();
		Player[] players = bFundamentals.getInstance().getServer().getOnlinePlayers();
		
		List<Player> hidden = new ArrayList<Player>();
        Map<String, List<Player>> groups = new HashMap<String, List<Player>>();

        for (Player player : players) {
            String[] playerGroups = bFundamentals.getPermissions().getPlayerGroups(player);
            String group = playerGroups.length > 0 ? playerGroups[0] : "";

            if (sender instanceof Player && !((Player)sender).canSee(player)){
            	hidden.add(player);
            }
             
            if (groups.containsKey(group)) {
                groups.get(group).add(player);
            } else {
                List<Player> list = new ArrayList<Player>();
                list.add(player);
                groups.put(group, list);
            }         
        }
        
        int onlinePlayers = players.length;
        
        if (!bHelpful.hasPermission(sender, "bhelpful.list.showvanish"))
        	onlinePlayers = onlinePlayers - hidden.size();
        
        // if the server is full
        if (onlinePlayers == Bukkit.getServer().getMaxPlayers()) {
        	// display current users online and max users in gold
        	out.append(ChatColor.GOLD + Bukkit.getServer().getServerName() + ": " + ChatColor.GRAY + "Online " + ChatColor.GOLD + "(" + ChatColor.GOLD);
        	out.append(onlinePlayers);
            out.append("/");
            out.append(Bukkit.getServer().getMaxPlayers());
            out.append(") ");
            out.append(ChatColor.WHITE);
        } else {
        	// display current users online and max users
        	out.append(ChatColor.GOLD + Bukkit.getServer().getServerName() + ": " + ChatColor.GRAY + "Online (");
        	out.append(onlinePlayers);
            out.append("/");
            out.append(Bukkit.getServer().getMaxPlayers());
            out.append(") ");
            out.append(ChatColor.WHITE);
        }
       
        // no point continuing as no-one is online, can only be reached by the console
        if (onlinePlayers == 0) {
        	return out.toString();
        }
        
        if (bFundamentals.getPermissions().getName().equalsIgnoreCase("PermissionsEx")) {
        	Map<String, List<Player>> sortGroups = sortGroups(groups);
        	groups = sortGroups;
        } else {
        	Map<String, List<Player>> sortGroups = new TreeMap<String, List<Player>>(groups);
        	groups = sortGroups;
        }
        
        int inGroup = 0;
        List<Player> hiddenGroup = new ArrayList<Player>();
        
        for (Map.Entry<String, List<Player>> entry : groups.entrySet()) {
	        
        	inGroup = entry.getValue().size();
        	
        	for (int i = 0; i < entry.getValue().size(); i++) {
        		if (sender instanceof Player && !((Player)sender).canSee(entry.getValue().get(i)) && !bHelpful.hasPermission(sender, "bhelpful.list.showvanish")) {
        			hiddenGroup.add(entry.getValue().get(i));
        		}
        	}
        	
        	inGroup -= hiddenGroup.size();
        	
        	if (inGroup == 0) {
        		continue;
        	}
        	
            out.append("\n");
            out.append(getRank(sender instanceof Player ? ((Player)sender).getWorld() : Bukkit.getWorlds().get(0), entry.getKey()).trim());
            out.append(ChatColor.WHITE + ": ");

            // To keep track of commas
            boolean first = true;

            for (Player player : entry.getValue()) {
                /* //TODO Need to find a away to do this
                 *  if (PerkUtils.getPlayer(player).isAfk()) {
                	if (!first) {
                        out.append(", ");
                    }
                	out.append(ChatColor.GRAY + player.getName()).append(ChatColor.WHITE);
                	
                	//TODO and a better way of doing this
                } else */if (sender instanceof Player && !((Player)sender).canSee(player)) {
                	
                	if (bHelpful.hasPermission(sender, "bhelpful.list.showvanish")) {
                		
                		if (!first) {
	                        out.append(", ");
	                    }
                		out.append(ChatColor.GOLD + player.getName()).append(ChatColor.WHITE);
                		
                	} else {
                		
                		continue;
                	}
                } else {
                	
                	if (!first) {
                        out.append(", ");
                    }
                	
                	out.append(player.getDisplayName()).append(ChatColor.WHITE);
                }
                first = false;
            }
        }
		
        return out.toString();
	}
	
	private static Map<String, List<Player>> sortGroups(Map<String, List<Player>> players){
		
		Set<String> groups = players.keySet();
		Set<PermissionGroup> pexGroups = new HashSet<PermissionGroup>();
		
		for (String group : groups) {
			pexGroups.add(PermissionsEx.getPermissionManager().getGroup(group));
		}
		
		TreeMap<Integer, PermissionGroup> rank = new TreeMap<Integer, PermissionGroup>();
		
		for (PermissionGroup group : pexGroups) {
			rank.put(group.getRank(), group);
		}
		
		NavigableMap<Integer, PermissionGroup> sortedRank = rank.descendingMap();
		Map<String, List<Player>> sorted = new LinkedHashMap<String, List<Player>>();
		
		for (Map.Entry<Integer, PermissionGroup> entry : sortedRank.entrySet()) {
			sorted.put(entry.getValue().getName(), getPlayers(players, entry.getValue()));
		}
		return sorted;
	}
	
	private static List<Player> getPlayers(Map<String, List<Player>> players, PermissionGroup group) {
		List<Player> value = null;
		for (Map.Entry<String, List<Player>> entry : players.entrySet()) {
			if (entry.getKey().equalsIgnoreCase(group.getName())) {
				value = entry.getValue();
				break;
			}
		}
		return value;
	}
	
	@Override
	protected void loadConfig() throws IOException {
	}

}
