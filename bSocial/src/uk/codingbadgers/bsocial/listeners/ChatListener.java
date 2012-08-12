package uk.codingbadgers.bsocial.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import uk.codingbadgers.bsocial.ChatPlayer;
import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.events.ChannelChatEvent;

/**
 * @author James
 */
public class ChatListener implements Listener{

	/** The players. */
	public static List<ChatPlayer> players = new ArrayList<ChatPlayer>();
	
	/**
	 * On player chat.
	 *
	 * @param event the chat event
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		ChatPlayer player = findPlayer(event.getPlayer());
		
		if(player == null)
			return;
		
		String message = event.getMessage();
		
		// make it have events :D
		ChannelChatEvent chatEvent = new ChannelChatEvent(player, player.getActiveChannel(), message);
		// Bukkit.getServer() is thread safe
		Bukkit.getServer().getPluginManager().callEvent(chatEvent);
		
		if (chatEvent.isCancelled()) {
			return;
		}
		
		player.sendMessage(chatEvent.getChannel(), chatEvent.getMessage());
		
		// we don't want to use bukkit chat, we can use our own system
		event.setCancelled(true);
	}
	
	/**
	 * On player join.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		ChatPlayer player = bSocial.getConfigManager().loadPlayer(event.getPlayer());
		players.add(player);
	}
	
	/**
	 * On player leave.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeave(PlayerQuitEvent event) {
		bSocial.getConfigManager().savePlayer(findPlayer(event.getPlayer()));
		players.remove(findPlayer(event.getPlayer()));
	}
	
	/**
	 * On player leave.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeave(PlayerKickEvent event) {
		bSocial.getConfigManager().savePlayer(findPlayer(event.getPlayer()));
		players.remove(findPlayer(event.getPlayer()));
	}
	
	/**
	 * Find a player based of the bukkit player.
	 *
	 * @param player the player
	 * @return the chat player
	 */
	public static ChatPlayer findPlayer(Player player) {
		Iterator<ChatPlayer> itr = players.iterator();
		while(itr.hasNext()) {
			ChatPlayer current = itr.next();
			if (current.getPlayer().equals(player))
				return current;
		}
		return null;
	}
}
