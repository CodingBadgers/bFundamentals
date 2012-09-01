package uk.codingbadgers.bHelpful.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bHelpful.bHelpful;

/**
 * list command
 * 
 * @author james
 */
public class PlayerList {

	/**
	 * Display list info for a player
	 * 
	 * @param sender the command sender
	 */
	public static void displayList(Player sender) {
		if (sender == null)
			return;
		
		StringBuilder out = new StringBuilder();

		out.append(listPlayers(sender));
		
	    String[] lines = out.toString().split("\n");

	    for (String line : lines) {
	    	sender.getPlayer().sendMessage(line);
	    }
	}
	
	/**
	 * Get a groups prefix
	 * 
	 * @param world the world the player is in
	 * @param group the group to lookup
	 * @return the prefix striped of all colour codes and brackets
	 */
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
        
        return prefix.length() > 0 ? prefix : group;
	}
	
	/**
	 * List all players online
	 * 
	 * @param sender the command sender
	 * @return the string to display 
	 */
	private static String listPlayers(Player sender) {
		StringBuilder out = new StringBuilder();
		Player[] players = bFundamentals.getInstance().getServer().getOnlinePlayers();
		
		List<Player> hidden = new ArrayList<Player>();
        Map<String, List<Player>> groups = new HashMap<String, List<Player>>();

        for (Player player : players) {
            String[] playerGroups = bFundamentals.getPermissions().getPlayerGroups(player);
            String group = playerGroups.length > 0 ? playerGroups[0] : "";

            if (!sender.canSee(player)){
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
        		if (!sender.canSee(entry.getValue().get(i)) && !bHelpful.hasPermission(sender, "belpful.list.showvanish")) {
        			hiddenGroup.add(entry.getValue().get(i));
        		}
        	}
        	
        	inGroup -= hiddenGroup.size();
        	
        	if (inGroup == 0) {
        		continue;
        	}
        	
            out.append("\n");
            out.append(getRank(sender.getWorld(), entry.getKey()));
            out.append(ChatColor.WHITE + ": ");

            // To keep track of commas
            boolean first = true;

            for (Player player : entry.getValue()) {
                /* TODO Need to find a away to do this
                 *  if (PerkUtils.getPlayer(player).isAfk()) {
                	if (!first) {
                        out.append(", ");
                    }
                	out.append(ChatColor.GRAY + player.getName()).append(ChatColor.WHITE);
                	
                } else */if (!sender.canSee(player)) {
                	
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
	
	/**
	 * Sort the groups based on pex rank, depends on PermissionsEx
	 * 
	 * @param players all players online and their group
	 * @return the map of players and groups sorted nicely
	 */
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
	
	/**
	 * Get the players from a group, depends on pex
	 * 
	 * @param players the players map
	 * @param group the group to get for
	 * @return a list of players in that group
	 */
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
}
