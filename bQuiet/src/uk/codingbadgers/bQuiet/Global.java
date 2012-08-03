package uk.codingbadgers.bQuiet;

import java.util.ArrayList;
import java.util.Iterator;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import uk.codingbadgers.bQuiet.containers.bPlayer;

public class Global {
	
	private static Plugin m_plugin = null;
	private static ArrayList<bPlayer> m_players = new ArrayList<bPlayer>();
	private static Permission m_permission = null;
	
	public static void setPlugin(Plugin plugin) {
		m_plugin = plugin;
		
		RegisteredServiceProvider<Permission> permissionProvider = m_plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            m_permission = permissionProvider.getProvider();
        }
	}
	
	public static Plugin getPlugin() {
		return m_plugin;
	}
	
	public static Server getServer() {
		return m_plugin.getServer();
	}
	
	public static ArrayList<bPlayer> getPlayer() {
		return m_players;
	}

	public static void addPlayer(bPlayer player) {
		m_players.add(player);		
	}
	
	public static void removePlayer(bPlayer player) {
		m_players.remove(player);		
	}
	
	public static bPlayer getPlayer(Player player) {
		
		Iterator<bPlayer> pIterator = m_players.iterator();
		while (pIterator.hasNext()) {
			bPlayer bp = pIterator.next();
			if (bp.getPlayer() == player) {
				return bp;
			}
		}
		
		return null;
		
	}
	
	public static void sendMessage(Player player, String message) {
		player.sendMessage(ChatColor.DARK_RED + "[bQuiet] " + ChatColor.WHITE + message);
	}
	
	public static boolean hasPerms(Player player, String perm) {
		
		if (m_permission.has(player, perm))
			return true;
		
		return false;
	}

}
