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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.base.Splitter;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bHelpful.Config;

public class PlayerListCommand extends ConfigCommand {

	private static final Pattern STRIP_SPECIAL = Pattern.compile("[\\[\\]<>]");
	
	public PlayerListCommand() {
		super(Config.LIST_LABEL, "/list", false);
		HelpfulCommandHandler.registerCommand(this);
	}

	@Override
	protected void handleCommand(CommandSender sender, String label, String[] args) {

		if (!(sender instanceof Player)) {
			return;
		}
		
		displayList((Player) sender);
	}

	public static void displayList(Player player) {

		PlayerListMessageListener listener = new PlayerListMessageListener(player);
		Bukkit.getMessenger().registerIncomingPluginChannel(bFundamentals.getInstance(), "BungeeCord", listener);
		
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		 
		try {
			out.writeUTF("PlayerList");
			out.writeUTF("ALL");
			
			player.sendPluginMessage(bFundamentals.getInstance(), "BungeeCord", b.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String getRank(String group) {
        String prefix = bFundamentals.getChat().getGroupPrefix(Bukkit.getWorlds().get(0), group);
        prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        prefix = STRIP_SPECIAL.matcher(prefix).replaceAll("");
        
        return prefix.length() > 0 ? prefix.trim() : group.trim();
	}
	
	private static String buildPlayerList(String input) {
		Iterable<String> players = Splitter.on(',').omitEmptyStrings().trimResults().split(input);
		Map<String, List<String>> groups = new HashMap<String, List<String>>();

		int onlinePlayers = 0;
		
        for (String player : players) {
            String[] playerGroups = bFundamentals.getPermissions().getPlayerGroups("", player);
            String group = playerGroups.length > 0 ? playerGroups[0] : "";
            
            if (groups.containsKey(group)) {
                groups.get(group).add(player);
            } else {
                List<String> list = new ArrayList<String>();
                list.add(player);
                groups.put(group, list);
            }         
            
            onlinePlayers++;
        }
        
        StringBuilder out = new StringBuilder();

        // display current users online and max users
        out.append(ChatColor.GOLD + Bukkit.getServer().getServerName() + ": " + ChatColor.GRAY + "Online (");
        out.append(onlinePlayers);
        out.append("/");
        out.append(Bukkit.getServer().getMaxPlayers());
        out.append(") ");
        out.append(ChatColor.WHITE);

        // no point continuing as no-one is online, can only be reached by the console
        if (onlinePlayers == 0) {
        	return out.toString();
        }
        
        if (bFundamentals.getPermissions().getName().equalsIgnoreCase("PermissionsEx")) {
        	groups = sortGroups(groups);
        } else {
        	groups = new TreeMap<String, List<String>>(groups);
        }
        
        for (Map.Entry<String, List<String>> entry : groups.entrySet()) {
	        
            out.append("\n");
            out.append(getRank(entry.getKey()).trim());
            out.append(ChatColor.WHITE + ": ");

            // To keep track of commas
            boolean first = true;

            for (String player : entry.getValue()) {

            	if (!first) {
            		out.append(", ");
            	}

            	out.append(player).append(ChatColor.WHITE);
                first = false;
            }
        }
        
		return out.toString();
	}
	
	private static Map<String, List<String>> sortGroups(Map<String, List<String>> players){
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
		Map<String, List<String>> sorted = new LinkedHashMap<String, List<String>>();
		
		for (Map.Entry<Integer, PermissionGroup> entry : sortedRank.entrySet()) {
			sorted.put(entry.getValue().getName(), getPlayers(players, entry.getValue()));
		}
		return sorted;
	}
	
	private static List<String> getPlayers(Map<String, List<String>> players, PermissionGroup group) {
		List<String> value = null;
		for (Map.Entry<String, List<String>> entry : players.entrySet()) {
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
	
	private static class PlayerListMessageListener implements PluginMessageListener {

		private CommandSender sender;
		
		public PlayerListMessageListener(CommandSender sender) {
			this.sender = sender;
		}
		
		@Override
		public void onPluginMessageReceived(String channel, Player player, byte[] message) {
			try {
				if (!channel.equals("BungeeCord")) {
		            return;
		        }
		 
		        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
		        String subchannel = in.readUTF();
		        
		        if (subchannel.equals("PlayerList")) {
		        	in.readUTF();
					String output = buildPlayerList(in.readUTF());
					sender.sendMessage(output);
		        }
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			
			Bukkit.getMessenger().unregisterIncomingPluginChannel(bFundamentals.getInstance(), "BungeeCord", this);
		}
	}

}
