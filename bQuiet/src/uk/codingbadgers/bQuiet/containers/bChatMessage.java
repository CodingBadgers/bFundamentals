package uk.codingbadgers.bQuiet.containers;

/**
 * The Class bChatMessage.
 */
public class bChatMessage {
	
	/** The message. */
	private String m_message = null;
	
	/** The time it was sent. */
	private long m_time = 0;

	/**
	 * Instantiates a new b chat message.
	 *
	 * @param message the message
	 */
	public bChatMessage(String message) {
		m_message = message;
		m_time = System.currentTimeMillis();
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
	 * Gets the time.
	 *
	 * @return the time
	 */
	public long getTime() {
		return m_time;
	}

	/**
	 * Force lower case.
	 */
	public void forceLowerCase() {
		m_message = m_message.toLowerCase();
	}

}
