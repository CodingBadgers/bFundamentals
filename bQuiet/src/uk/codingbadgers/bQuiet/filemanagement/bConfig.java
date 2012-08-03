package uk.codingbadgers.bQuiet.filemanagement;

import org.bukkit.configuration.file.FileConfiguration;

import uk.codingbadgers.bQuiet.bGlobal;
import uk.codingbadgers.bQuiet.bQuiet;

public class bConfig {
	
	public static int noofMessages = 3;
	public static int loginSilence = 5000;
	public static int repeatMessageTime = 2000;
	public static int chatSpeed = 200;
	public static int capsMessageLength = 2;
	public static int maxPercentageOfCaps = 80;
	public static boolean forceLowerCase = false;
	
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
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		noofMessages = config.getInt("number_of_messages", 3);
		loginSilence = config.getInt("number_of_seconds_silenced_on_login", 5) * 1000;
		repeatMessageTime = config.getInt("number_of_seconds_between_duplicate_messages", 2) * 1000;
		chatSpeed = config.getInt("number_of_milliseconds_between_messages", 200);
		capsMessageLength = config.getInt("minimum_message_length_to_check_for_caps", 2);
		maxPercentageOfCaps = config.getInt("max_percentage_of_caps_in_a_message", 80);
		forceLowerCase = config.getBoolean("force_lowercase_rather_than_deny_caps_message", false);
				
		bGlobal.getPlugin().saveConfig();
		
		return true;
	}

}
