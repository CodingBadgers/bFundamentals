package uk.codingbadgers.bQuiet.containers;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;

import uk.codingbadgers.bQuiet.Global;
import uk.codingbadgers.bQuiet.filemanagement.ConfigManager;

/**
 * The Class bPlayer.
 */
public class bPlayer {
	
	/** The Bukkit Player. */
	private Player m_player = null;
	
	/** The Login time. */
	private long m_loginTime = 0;
	
	/** The Array of sent messages. */
	private ArrayList<bChatMessage> m_message = new ArrayList<bChatMessage>();
	
	/** The ip adress pattern. */
	private final String m_ipAdressPattern = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
											"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
											"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
											"([01]?\\d\\d?|2[0-4]\\d|25[0-5])" +
											"(:[0-9999999]$";
	
	/**
	 * Instantiates a new b player.
	 *
	 * @param player the player
	 */
	public bPlayer(Player player) {
		m_player = player;
		m_loginTime = System.currentTimeMillis();
	}
	
	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	public Player getPlayer() {
		return m_player;
	}
	
	/**
	 * Can talk.
	 *
	 * @return true, if successful
	 */
	public boolean canTalk() {
		if (System.currentTimeMillis() - m_loginTime > ConfigManager.m_loginSilence) {
			return true;
		}
		
		Global.sendMessage(m_player, "You can not talk for the first " + (ConfigManager.m_loginSilence * 0.001) + " seconds of logging in.");		
		return false;
	}

	/**
	 * Checks if is spamming.
	 *
	 * @param message the message
	 * @return true, if is spamming
	 */
	public boolean isSpamming(String message) {
		
		bChatMessage chatMessage = new bChatMessage(message);
		
		m_message.add(chatMessage);
		
		if (m_message.size() > ConfigManager.m_noofMessages) {
			m_message.remove(0);
		}

		for (int i = 0; i < m_message.size(); ++i) {
			
			bChatMessage currentMessage = m_message.get(i);
			
			if (currentMessage == chatMessage)
				continue;
			
			if (currentMessage.getMessage().equalsIgnoreCase(chatMessage.getMessage())) {
				
				if (chatMessage.getTime() - currentMessage.getTime() < ConfigManager.m_repeatMessageTime) {
					Global.sendMessage(m_player, "Please do not spam the chat.");
					return true;
				}
			}
			
			if (chatMessage.getTime() - currentMessage.getTime() < ConfigManager.m_chatSpeed) {
				Global.sendMessage(m_player, "You are speaking too quickly...");
				Global.sendMessage(m_player, "Please do not spam the chat.");
				return true;
			}
						
		}
		
		
		if (chatMessage.getMessage().length() > ConfigManager.m_capsMessageLength) {
			
			int noofCaps = 0;
			int noofValidChars = 0;
			for (char c : chatMessage.getMessage().toCharArray()) {
				if (Character.isLetter(c)) {
					if (Character.isUpperCase(c))
						noofCaps++;		
					noofValidChars++;
				}
			}
			
			Global.sendMessage(m_player, "noofCaps : " + noofCaps);
			Global.sendMessage(m_player, "noofValidChars : " + noofValidChars);
			
			if (noofCaps == 0 || noofCaps <= ConfigManager.m_capsMessageLength)
				return false;

			int percentage = (100 / noofValidChars) * noofCaps;
			
			Global.sendMessage(m_player, "percentage : " + percentage);
			
			if (percentage >= ConfigManager.m_maxPercentageOfCaps) {
				if (ConfigManager.m_forceLowerCase) {
					chatMessage.forceLowerCase();
					return false;
				} else {
					Global.sendMessage(m_player, "Please don't speak in all caps.");
					return true;
				}
			}
			
		}
		
		return false;
	}

	/**
	 * Gets the last message.
	 *
	 * @return the last message
	 */
	public String getLastMessage() {
		return m_message.get(m_message.size()-1).getMessage();
	}

	/**
	 * Checks if is advertising.
	 *
	 * @param message the message
	 * @return true, if is advertising
	 */
	public boolean isAdvertising(String message) {
		String[] words = message.split(" ");
		Pattern pattern = Pattern.compile(m_ipAdressPattern);
		boolean ip;
		
		for (String word : words) {
		    Matcher matcher = pattern.matcher(word);
			ip = matcher.matches();
			
			if (ip) {
				Global.sendMessage(m_player, "Please dont advertise other servers");
				return true;
			}
		}
		
		return false;
	}
}
