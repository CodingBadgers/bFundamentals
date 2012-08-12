package uk.codingbadgers.bQuiet.filemanagement;

import org.bukkit.configuration.file.FileConfiguration;

import uk.codingbadgers.bQuiet.Global;
import uk.codingbadgers.bQuiet.bQuiet;

public class ConfigManager {
	
	public static int m_noofMessages = 3;
	public static int m_loginSilence = 5000;
	public static int m_repeatMessageTime = 2000;
	public static int m_chatSpeed = 200;
	public static int m_capsMessageLength = 2;
	public static int m_maxPercentageOfCaps = 80;
	public static boolean m_forceLowerCase = false;
	
	public static boolean setupConfig() {
		
		FileConfiguration config = bQuiet.getInstance().getConfig();
		
		try {
			config.addDefault("number_of_messages", 3);
			config.addDefault("number_of_seconds_silenced_on_login", 5);
			config.addDefault("number_of_seconds_between_duplicate_messages", 2);
			config.addDefault("number_of_milliseconds_between_messages", 200);
			config.addDefault("minimum_message_length_to_check_for_caps", 2);
			config.addDefault("max_percentage_of_caps_in_a_message", 80);
			config.addDefault("force_lowercase_rather_than_deny_caps_message", false);
			config.options().copyDefaults(true);
			bQuiet.getInstance().saveConfig();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		m_noofMessages = config.getInt("number_of_messages", 3);
		m_loginSilence = config.getInt("number_of_seconds_silenced_on_login", 5) * 1000;
		m_repeatMessageTime = config.getInt("number_of_seconds_between_duplicate_messages", 2) * 1000;
		m_chatSpeed = config.getInt("number_of_milliseconds_between_messages", 200);
		m_capsMessageLength = config.getInt("minimum_message_length_to_check_for_caps", 2);
		m_maxPercentageOfCaps = config.getInt("max_percentage_of_caps_in_a_message", 80);
		m_forceLowerCase = config.getBoolean("force_lowercase_rather_than_deny_caps_message", false);
				
		Global.getPlugin().saveConfig();
		
		return true;
	}

}
