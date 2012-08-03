package uk.codingbadgers.bQuiet.containers;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;

import uk.codingbadgers.bQuiet.bGlobal;
import uk.codingbadgers.bQuiet.filemanagement.bConfig;

public class bPlayer {
	
	private Player m_player = null;
	private long m_loginTime = 0;
	private ArrayList<bChatMessage> m_message = new ArrayList<bChatMessage>();
	private final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
											"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
											"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
											"([01]?\\d\\d?|2[0-4]\\d|25[0-5])" +
											"(:[0-9999999]$";
	
	public bPlayer(Player player) {
		m_player = player;
		m_loginTime = System.currentTimeMillis();
	}
	
	public Player getPlayer() {
		return m_player;
	}
	
	public boolean canTalk() {
		if (System.currentTimeMillis() - m_loginTime > bConfig.loginSilence) {
			return true;
		}
		
		bGlobal.sendMessage(m_player, "You can not talk for the first " + (bConfig.loginSilence * 0.001) + " seconds of logging in.");		
		return false;
	}

	public boolean isSpamming(String message) {
		
		bChatMessage chatMessage = new bChatMessage(message);
		
		m_message.add(chatMessage);
		
		if (m_message.size() > bConfig.noofMessages) {
			m_message.remove(0);
		}

		for (int i = 0; i < m_message.size(); ++i) {
			
			bChatMessage currentMessage = m_message.get(i);
			
			if (currentMessage == chatMessage)
				continue;
			
			if (currentMessage.getMessage().equalsIgnoreCase(chatMessage.getMessage())) {
				
				if (chatMessage.getTime() - currentMessage.getTime() < bConfig.repeatMessageTime) {
					bGlobal.sendMessage(m_player, "Please do not spam the chat.");
					return true;
				}
			}
			
			if (chatMessage.getTime() - currentMessage.getTime() < bConfig.chatSpeed) {
				bGlobal.sendMessage(m_player, "You are speaking too quickly...");
				bGlobal.sendMessage(m_player, "Please do not spam the chat.");
				return true;
			}
						
		}
		
		
		if (chatMessage.getMessage().length() > bConfig.capsMessageLength) {
			
			int noofCaps = 0;
			int noofValidChars = 0;
			for (char c : chatMessage.getMessage().toCharArray()) {
				if (Character.isLetter(c)) {
					if (Character.isUpperCase(c))
						noofCaps++;		
					noofValidChars++;
				}
			}
			
			bGlobal.sendMessage(m_player, "noofCaps : " + noofCaps);
			bGlobal.sendMessage(m_player, "noofValidChars : " + noofValidChars);
			
			if (noofCaps == 0 || noofCaps <= bConfig.capsMessageLength)
				return false;

			int percentage = (100 / noofValidChars) * noofCaps;
			
			bGlobal.sendMessage(m_player, "percentage : " + percentage);
			
			if (percentage >= bConfig.maxPercentageOfCaps) {
				if (bConfig.forceLowerCase) {
					chatMessage.forceLowerCase();
					return false;
				} else {
					bGlobal.sendMessage(m_player, "Please don't speak in all caps.");
					return true;
				}
			}
			
		}
		
		return false;
	}

	public String getLastMessage() {
		return m_message.get(m_message.size()-1).getMessage();
	}

	public boolean isAdvertising(String message) {
		String[] words = message.split(" ");
		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
		boolean ip;
		
		for (String word : words) {
		    Matcher matcher = pattern.matcher(word);
			ip = matcher.matches();
			
			if (ip) {
				bGlobal.sendMessage(m_player, "Please dont advertise other servers");
				return true;
			}
		}
		
		return false;
	}
}
