package uk.codingbadgers.bQuiet.filemanagement;

import org.bukkit.configuration.file.FileConfiguration;

import uk.codingbadgers.bQuiet.Global;
import uk.codingbadgers.bQuiet.bQuiet;

public class ConfigManager {
	
	/** Number of messages to compare. */
	public static int NOOFMESSAGES = 3;
	
	/** Number of ms a player can't speak on login. */
	public static int LOGINSILENCE = 5000;
	
	/** Minimum time between duplicate messages. */
	public static int REPEATMESSAGETIME = 2000;
	
	/** Number of ms between chat message. */
	public static int CHATSPEED = 200;
	
	/** Minimum length of a message to check for caps messages. */
	public static int CAPSMESSAGELENGTH = 2;
	
	/** Maximum percentage of caps a message can contain. */
	public static int MAXPERCENTAGEOFCAPS = 80;
	
	/** Force a caps message to lower case rather than cancelling it. */
	public static boolean FORCELOWERCASE = false;
	
	/**
	 * Load the configuration into memory
	 * 
	 * @return True if the configuration loaded, false otherwise.
	 */
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
		
		NOOFMESSAGES = config.getInt("number_of_messages", 3);
		LOGINSILENCE = config.getInt("number_of_seconds_silenced_on_login", 5) * 1000;
		REPEATMESSAGETIME = config.getInt("number_of_seconds_between_duplicate_messages", 2) * 1000;
		CHATSPEED = config.getInt("number_of_milliseconds_between_messages", 200);
		CAPSMESSAGELENGTH = config.getInt("minimum_message_length_to_check_for_caps", 2);
		MAXPERCENTAGEOFCAPS = config.getInt("max_percentage_of_caps_in_a_message", 80);
		FORCELOWERCASE = config.getBoolean("force_lowercase_rather_than_deny_caps_message", false);
				
		Global.getPlugin().saveConfig();
		
		return true;
	}

}
