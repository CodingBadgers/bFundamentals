package uk.codingbadgers.bQuiet.containers;

public class bChatMessage {
	
	private String m_message = null;
	private long m_time = 0;

	public bChatMessage(String message) {
		m_message = message;
		m_time = System.currentTimeMillis();
	}
	
	public String getMessage() {
		return m_message;
	}
	
	public long getTime() {
		return m_time;
	}

	public void forceLowerCase() {
		m_message = m_message.toLowerCase();
	}

}
