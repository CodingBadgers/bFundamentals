package uk.codingbadgers.bsocial.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import uk.codingbadgers.bsocial.players.ChatPlayer;

/**
 * The base bSocial event class
 */
public abstract class bSocialEvent extends Event implements Cancellable{
	
	/** The player the event is based off. */
	protected ChatPlayer m_player = null;
	
	/** Wether the event is cancelled . */
	protected boolean m_canceled = false;
	
	/**
	 * Instantiates a new b social event.
	 *
	 * @param player the player
	 */
	public bSocialEvent(ChatPlayer player) {
		m_player = player;
	}
	
	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	public ChatPlayer getPlayer() {
		return m_player;
	}

	/* (non-Javadoc)
	 * @see org.bukkit.event.Cancellable#isCancelled()
	 */
	@Override
	public boolean isCancelled() {
		return m_canceled;
	}

	/* (non-Javadoc)
	 * @see org.bukkit.event.Cancellable#setCancelled(boolean)
	 */
	@Override
	public void setCancelled(boolean canceled) {
		m_canceled = canceled;
	}

}
