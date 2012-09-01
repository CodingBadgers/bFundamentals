package uk.codingbadgers.bsocial.events;

import org.bukkit.event.HandlerList;

import uk.codingbadgers.bsocial.players.ChatPlayer;

/**
 * PrivateMessageEvent called whenever a pm is sent to another player
 */
public class PrivateMessageEvent extends bSocialEvent{

	/** The recipient of the private message. */
	private ChatPlayer m_to;
	
	/** The message. */
	private String m_message;

	/** The handlers. */
	private HandlerList m_handlers = new HandlerList();

	/**
	 * Instantiates a new private message event.
	 *
	 * @param player the player
	 * @param to the to
	 * @param message the message
	 */
	public PrivateMessageEvent(ChatPlayer player, ChatPlayer to, String message) {
		super(player);
		setReciever(to);
		m_message = message;
	}

	/**
	 * Gets the reciever.
	 *
	 * @return the reciever
	 */
	public ChatPlayer getReciever() {
		return m_to;
	}

	/**
	 * Sets the receiver.
	 *
	 * @param to the new receiver
	 */
	public void setReciever(ChatPlayer to) {
		m_to = to;
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
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		m_message = message;
	}

	/* (non-Javadoc)
	 * @see org.bukkit.event.Event#getHandlers()
	 */
	@Override
	public HandlerList getHandlers() {
		return m_handlers;
	}

}
