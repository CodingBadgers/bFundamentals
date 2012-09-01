package uk.codingbadgers.bsocial.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.events.ChannelChatEvent;
import uk.codingbadgers.bsocial.players.ChatPlayer;

/**
 * @author James
 */
@SuppressWarnings("deprecation")
public class ChatListener implements Listener{

	/**
	 * On player chat.
	 *
	 * @param event the chat event
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerChat(PlayerChatEvent event) {
		ChatPlayer player = bSocial.getPlayerManager().findPlayer(event.getPlayer());
		
		if(player == null)
			return;
		
		String message = event.getMessage();
		
		// make it have events :D
		ChannelChatEvent chatEvent = new ChannelChatEvent(player, player.getActiveChannel(), message);
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
		bSocial.getPlayerManager().addPlayer(player);
	}
	
	/**
	 * On player leave.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeave(PlayerQuitEvent event) {
		bSocial.getConfigManager().savePlayer(bSocial.getPlayerManager().findPlayer(event.getPlayer()));
		bSocial.getPlayerManager().removePlayer(bSocial.getPlayerManager().findPlayer(event.getPlayer()));
	}
	
	/**
	 * On player leave.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeave(PlayerKickEvent event) {
		bSocial.getConfigManager().savePlayer(bSocial.getPlayerManager().findPlayer(event.getPlayer()));
		bSocial.getPlayerManager().removePlayer(bSocial.getPlayerManager().findPlayer(event.getPlayer()));
	}
}
