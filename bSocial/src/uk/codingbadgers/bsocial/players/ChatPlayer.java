package uk.codingbadgers.bsocial.players;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.chanels.ChatChannel;

/**
 * The ChatPlayer object.
 */
public class ChatPlayer {

	/** The bukkit player. */
	private Player m_player = null;
	
	/** The current channel the player is in. */
	private ChatChannel m_current = null;
	
	/** All the channels the player is in. */
	private List<ChatChannel> m_channels = new ArrayList<ChatChannel>();
	
	/** Whether they are muted. */
	private boolean m_muted = false;
	
	/**
	 * Instantiates a new chat player.
	 *
	 * @param player the player
	 */
	public ChatPlayer(Player player) {
		m_player = player;
	}
	
	/**
	 * Gets the bukkit player.
	 *
	 * @return the bukkit player
	 */
	public Player getPlayer() {
		return m_player;
	}
	
	/**
	 * Send a message in the players current channel.
	 *
	 * @param message the message
	 */
	public void sendMessage(String message) {
		if (m_muted) {
			m_player.sendMessage(ChatColor.LIGHT_PURPLE + "[bSocial] " + ChatColor.WHITE + " sorry you are muted and cannot speak");
			return;
		}
		
		m_current.sendMessage(m_player, message);
	}
	
	/**
	 * Send message in a specified channel.
	 *
	 * @param channel the channel
	 * @param message the message
	 */
	public void sendMessage(ChatChannel channel, String message) {
		if (m_muted) {
			m_player.sendMessage(ChatColor.LIGHT_PURPLE + "[bSocial] " + ChatColor.WHITE + " sorry you are muted and cannot speak");
			return;
		}
		
		channel.sendMessage(m_player, message);
	}
	
	/**
	 * Checks if they are muted.
	 *
	 * @return true, if is muted
	 */
	public boolean isMuted() {
		return m_muted;
	}
	
	/**
	 * Sets if they are muted.
	 *
	 * @param muted the new muted
	 */
	public void setMuted(boolean muted) {
		m_muted = muted;
	}
	 
	/**
	 * Gets the channels that this player is part of.
	 *
	 * @return the channels
	 */
	public List<ChatChannel> getChannels() {
		return m_channels;
	}
	
	/**
	 * Gets their active channel.
	 *
	 * @return the active channel
	 */
	public ChatChannel getActiveChannel() {
		return m_current;
	}
	
	/**
	 * Focus on a specific channel.
	 *
	 * @param channel the channel
	 */
	public void focusOn(ChatChannel channel) {
		m_current = channel;
	}
	
	/**
	 * Join a channel.
	 *
	 * @param channel the channel
	 */
	public void joinChannel(ChatChannel channel) {
		channel.addPlayer(this);
		m_channels.add(channel);
	}
	
	/**
	 * Leave a channel.
	 *
	 * @param channel the channel
	 */
	public void leaveChannel(ChatChannel channel) {
		channel.removePlayer(this);
		m_channels.remove(channel);
	}

	/**
	 * Checks if is part of a channel
	 *
	 * @param channel the channel
	 * @return true, if is part of
	 */
	public boolean isPartOf(ChatChannel channel) {
		return m_channels.contains(channel);
	}
	
	/**
	 * Send a private message to a player.
	 *
	 * @param reciever the reciever
	 * @param message the message
	 */
	public void sendPrivateMessage(ChatPlayer reciever, String message) {
		m_player.sendMessage(ChatColor.GOLD + "[PM] " + ChatColor.GREEN +  "to " + reciever.getPlayer().getName() + ":" + ChatColor.WHITE + message);
	}
	
	/**
	 * Recieve a private message from a player.
	 *
	 * @param reciever the reciever
	 * @param message the message
	 */
	public void recievePrivateMessage(ChatPlayer reciever, String message) {
		m_player.sendMessage(ChatColor.GOLD + "[PM] " + ChatColor.GREEN +  "from " + reciever.getPlayer().getName() + ":" + ChatColor.WHITE + message);
	}
	
	/**
	 * Checks for permission.
	 *
	 * @param permission the permission
	 * @param verbose the verbose
	 * @return true, if successful
	 */
	public boolean hasPermission(String permission, boolean verbose) {
		if (bSocial.hasPermission(m_player, permission)) 
			return true;
		
		if (verbose)
			bSocial.sendMessage(bSocial.MODULE.getName(), m_player, "Sorry you do not have permission to do this");
		
		return false;
	}
}
