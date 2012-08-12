package uk.codingbadgers.bsocial.events;

import org.bukkit.event.HandlerList;

import uk.codingbadgers.bsocial.ChatPlayer;
import uk.codingbadgers.bsocial.chanels.ChatChannel;

/**
 * ChannelChatEvent called whenever a player speaks in a channel
 */
public class ChannelChatEvent extends bSocialEvent{

	/** The channel the message was sent in. */
	private ChatChannel m_channel = null;
	
	/** The message sent. */
	private String m_message = null;
	
	/** The handlers for this event. */
	private HandlerList m_handlers = new HandlerList();
	
	/**
	 * Instantiates a new channel chat event.
	 *
	 * @param player the player
	 * @param channel the channel
	 * @param message the message
	 */
	public ChannelChatEvent(ChatPlayer player, ChatChannel channel, String message) {
		super(player);
		m_channel = channel;
		m_message = message;
	}
	
	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return m_message;
	}
	
	/**
	 * Gets the channel.
	 *
	 * @return the channel
	 */
	public ChatChannel getChannel() {
		return m_channel;
	}
	
	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		m_message = message;
	}
	
	/**
	 * Sets the channel.
	 *
	 * @param channel the new channel
	 */
	public void setChannel(ChatChannel channel) {
		m_channel = channel;
	}
	
	/* (non-Javadoc)
	 * @see org.bukkit.event.Event#getHandlers()
	 */
	@Override
	public HandlerList getHandlers() {
		return m_handlers;
	}

}
