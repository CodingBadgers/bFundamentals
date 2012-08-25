package uk.codingbadgers.bHelpful;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;


/**
 * 
 * @author James
 */
public class Configuration {

	static private Hashtable<String, ArrayList<String>> m_help = new Hashtable<String, ArrayList<String>>();
	static public ArrayList<String> REGISTER = new ArrayList<String>();
	static public ArrayList<String> NEWS = new ArrayList<String>();
	static public ArrayList<String> MOTD = new ArrayList<String>();
	static public ArrayList<ArrayList<String>> ANNOUCNEMENTS =  new ArrayList<ArrayList<String>>();
    static public ArrayList<String> RULES = new ArrayList<String> ();
    static public ArrayList<String> VOTE = new ArrayList<String>();
    
    public static FileConfiguration config = bHelpful.MODULE.getConfig();
    
    static public File ANNOUNCEMENT_CONIFG;
    static public File NEWS_CONFIG;

    public static String NORMAL_ENABLED = null; 
    public static String STAFF_ENABLED = null;
    public static String NORMAL_DISABLED = null;
    public static String STAFF_DISABLED = null;
    
    public static boolean NORMAL_STATE = false;
    public static boolean STAFF_STATE = false;
    
    public static String m_servername = "Minecraft Server";

	public static boolean loadConfig(bHelpful plugin) {
		
		m_servername = bHelpful.PLUGIN.getServer().getServerName();

		m_help.clear();
		REGISTER.clear();
		NEWS.clear();
		MOTD.clear();
		ANNOUCNEMENTS.clear();
        RULES.clear();

        try {
        	FileConfiguration config = bHelpful.MODULE.getConfig();
        	
        	config.addDefault("maintenance.message.normal.enabled", "Maintenance Mode has Been Enabled. The server may lag. Please bare with us");
        	config.addDefault("maintenance.message.staff.enabled", "Maintenance Mode has Been Disabled. Thankyou for your paitence");
        	config.addDefault("maintenance.message.normal.disabled", "Staff Maintenance Mode Enabled");
        	config.addDefault("maintenance.message.staff.disabled", "Staff Maintenance Mode Disabled");
        	config.addDefault("maintenance.state.normal", false);
        	config.addDefault("maintenance.state.staff", false);
        	config.addDefault("announcement.timedelay", 15);
        	
        	config.options().copyDefaults(true);
                	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	return false;
        }
        
        NORMAL_ENABLED = config.getString("maintenance.message.normal.enabled", "Maintenance Mode has Been Enabled. The server may lag. Please bare with us");
        STAFF_ENABLED = config.getString("maintenance.message.staff.enabled", "Maintenance Mode has Been Disabled. Thankyou for your paitence");
        NORMAL_DISABLED = config.getString("maintenance.message.normal.disabled", "Staff Maintenance Mode Enabled");
        STAFF_DISABLED = config.getString("maintenance.message.staff.disabled", "Staff Maintenance Mode Disabled");
    	
        NORMAL_STATE = config.getBoolean("maintenance.state.normal");
        STAFF_STATE = config.getBoolean("maintenance.state.staff");
    	
    	bHelpful.MODULE.saveConfig();
        
		File folder = new File(plugin.getDataFolder() + File.separator
				+ "RankHelp");

		// if the folder doesnt exist create the folder and one default config
		if (!folder.exists())
			createDefaultHelpConfig(folder);

		// get each config from the folder each named as the rank name
		String[] configFiles = folder.list();
		if (configFiles != null) {
			for (int i = 0; i < configFiles.length; i++) {
				// Get filename of file, without its extension
				String filename = configFiles[i].substring(0,
						configFiles[i].lastIndexOf('.'));

				// load config for that file
				loadHelpConfig(folder, filename);
			}
		}

		// see if the Register.cfg exists, if not make a default one
		File registerConfig = new File(plugin.getDataFolder() + File.separator
				+ "Register.cfg");
		if (!registerConfig.exists())
			createDefaultRegisterConfig(registerConfig);

		// load in the register config file, storing it in the register member
		loadRegisterConfig(registerConfig);

		// see if the News.cfg exists, if not make a default one
		NEWS_CONFIG = new File(plugin.getDataFolder() + File.separator
				+ "News.cfg");
		if (!NEWS_CONFIG.exists())
			createDefaultNewsConfig(NEWS_CONFIG);

		// load in the news config file, storing it in the news member
		loadNewsConfig(NEWS_CONFIG);

		// see if the News.cfg exists, if not make a default one
		File motdConfig = new File(plugin.getDataFolder() + File.separator
				+ "Motd.cfg");
		if (!motdConfig.exists())
			createDefaultMotdConfig(motdConfig);

		// load in the motd config file, storing it in the news member
		loadMotdConfig(motdConfig);
		
		
		// see if the Announcement.cfg exists, if not make a default one
		ANNOUNCEMENT_CONIFG = new File(plugin.getDataFolder() + File.separator
				+ "Announcement.cfg");
		if (!ANNOUNCEMENT_CONIFG.exists())
			createDefaultAnnouncementsConfig(ANNOUNCEMENT_CONIFG);
		
		// load announcements
		LoadAnnouncemnetConfig(ANNOUNCEMENT_CONIFG);
                
		// see if the Rules.cfg exists, if not make a default one
		File rulesConfig = new File(plugin.getDataFolder() + File.separator
				+ "Rules.cfg");
		if (!rulesConfig.exists())
			createDefaultRulesConfig(rulesConfig);
           
		 // load rules
		loadRulesConfig(rulesConfig);   
		
		// see if the vote.cfg exists, if not make a default one
		File voteConfig = new File(plugin.getDataFolder() + File.separator
				+ "Vote.cfg");
		if (!voteConfig.exists())
			createDefaultVoteConfig(voteConfig);
		                 		
		// load vote info
		loadVoteConfig(voteConfig);
		return true;
	}

	public static void createDefaultAnnouncementsConfig(File announcementConfig) {
		
		try {
			announcementConfig.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(announcementConfig.getPath()));

			writer.write("~ANNOUNCEMENT~\n");
			writer.write("This server is running bHelpful!\n");
			writer.write("~ENDANNOUNCEMENT~");

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void LoadAnnouncemnetConfig(File announcementConfig) {
		
		if (!announcementConfig.exists()) {
			Output.log(Level.SEVERE, "Config file Announcement.cfg does not exist");
			return;
		}

		Output.log(Level.INFO, "Loading config file 'Announcement.cfg'");
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(announcementConfig.getPath()));

			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				// ignore comments and empty lines
				if (line.startsWith("#") || line.length() == 0)
					continue;
				
				if (!line.equalsIgnoreCase("~ANNOUNCEMENT~")) {
					reader.close();
					return;
				}
				
				ArrayList<String> newAnnouncement = new ArrayList<String>();
				
				while ((line = reader.readLine()) != null) {
					
					if (line.equalsIgnoreCase("~ENDANNOUNCEMENT~"))
						break;
					
					newAnnouncement.add(replaceColors(line));
				}
								
				ANNOUCNEMENTS.add(newAnnouncement);
			}
			
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void loadMotdConfig(File motdConfig) {

		if (!motdConfig.exists()) {
			Output.log(Level.SEVERE, "Config file Motd.cfg does not exist");
			return;
		}

		Output.log(Level.INFO, "Loading config file 'Motd.cfg'");

		try {
			BufferedReader reader = new BufferedReader(new FileReader(motdConfig.getPath()));

			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				// ignore comments and empty lines
				if (line.startsWith("#") || line.length() == 0)
					continue;

				// store the line into the array of help, which will be added to
				// the help HashMap
				MOTD.add(replaceColors(line));
			}
			
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createDefaultMotdConfig(File motdConfig) {

		try {
			motdConfig.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					motdConfig.getPath()));

			writer.write(ChatColor.RED + "Welcome to " + m_servername + "!\n");

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void loadNewsConfig(File newsConfig) {

		if (!newsConfig.exists()) {
			Output.log(Level.SEVERE, "Config file News.cfg does not exist");
			return;
		}

		Output.log(Level.INFO, "Loading config file 'News.cfg'");

		try {
			BufferedReader reader = new BufferedReader(new FileReader(newsConfig.getPath()));

			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				// ignore comments and empty lines
				if (line.startsWith("#") || line.length() == 0)
					continue;

				// store the line into the array of help, which will be added to
				// the help HashMap
				NEWS.add(replaceColors(line));
			}

			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void createDefaultNewsConfig(File newsConfig) {

		try {
			newsConfig.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					newsConfig.getPath()));

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();

			writer.write(ChatColor.RED + "[" + dateFormat.format(date) + "] "
					+ ChatColor.YELLOW + "Added bHelpful Plugin.\n");

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void loadRegisterConfig(File registerConfig) {

		if (!registerConfig.exists()) {
			Output.log(Level.SEVERE, "Config file Register.cfg does not exist");
			return;
		}

		Output.log(Level.INFO, "Loading config file 'Register.cfg'");

		try {
			BufferedReader reader = new BufferedReader(new FileReader(registerConfig.getPath()));

			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				// ignore comments and empty lines
				if (line.startsWith("#") || line.length() == 0)
					continue;

				// store the line into the array of help, which will be added to
				// the help HashMap
				REGISTER.add(replaceColors(line));
			}
			
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void createDefaultRegisterConfig(File registerConfig) {

		try {
			registerConfig.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(registerConfig.getPath()));

			writer.write(ChatColor.GOLD + "How to get your first rank\n");

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void createDefaultHelpConfig(File folder) {

		if (!folder.mkdirs())
			return;

		File defaultHelpConfig = new File(folder + File.separator
				+ "Default.cfg");

		try {
			defaultHelpConfig.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					defaultHelpConfig.getPath()));

			writer.write("# A default help example file\n");
			writer.write("# Simply list help as you would expect it to appear in game\n");

			writer.write("You are rank [Default].\n");
			writer.write("To get your first official rank please type '/register' into chat.\n");

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static boolean loadHelpConfig(File folder, String rank) {

		File config = new File(folder + File.separator + rank + ".cfg");

		if (!config.exists()) {
			Output.log(Level.SEVERE, "Config file " + rank
					+ ".cfg does not exist");
			return false;
		}

		Output.log(Level.INFO, "Loading config file '" + rank + ".cfg'");

		try {
			BufferedReader reader = new BufferedReader(new FileReader(config.getPath()));

			ArrayList<String> help = new ArrayList<String>();

			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				// ignore comments and empty lines
				if (line.startsWith("#") || line.length() == 0)
					continue;

				// store the line into the array of help, which will be added to
				// the help HashMap
				help.add(replaceColors(line));

			}

			// Hashmaps are case sensitive so just make everything lower case to
			// be safe
			m_help.put(rank.toLowerCase(), help);
			
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static String replaceColors(String message) {
			
		// Store the message in a temp buffer
		String formattedMessage = message;
		
		// we'll replace the &<c> as we go, so looop through all of them
		while(formattedMessage.indexOf("&") != -1) {
			
			// get the colour code first as a string, then convert the hex based char into an integer
			String code = formattedMessage.substring(formattedMessage.indexOf("&") + 1, formattedMessage.indexOf("&") + 2);
			
			// get all the text up to the first '&'
			// get the chat colour from our colour code
			// skip the &<c> and reattach the rest of the string
			formattedMessage = formattedMessage.substring(0, formattedMessage.indexOf("&")) +  ChatColor.getByChar(code) + formattedMessage.substring(formattedMessage.indexOf("&") + 2);
								
		}
		
		return formattedMessage;
	}

	public static ArrayList<String> getHelp(String rank) {
		return m_help.get(rank.toLowerCase());
	}
        
	public static void createDefaultRulesConfig(File rulesConfig) {

		try {
			rulesConfig.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					rulesConfig.getPath()));

			writer.write("# Add your rules to this file\n");
			writer.write("# put one on each line without numbers\n");
			writer.write("# as they are automaticaly generated\n");
			writer.write("Add your first rule here\n");
			writer.write("And your second here, and so on\n");

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void loadRulesConfig(File rulesConfig) {

		if (!rulesConfig.exists()) {
			Output.log(Level.SEVERE, "Config file Rules.cfg does not exist");
			return;
		}

		Output.log(Level.INFO, "Loading config file 'Rules.cfg'");

		try {
			BufferedReader reader = new BufferedReader(new FileReader(rulesConfig.getPath()));

			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				// ignore comments and empty lines
				if (line.startsWith("#") || line.length() == 0)
					continue;

				// store the line into the array of help, which will be added to
				// the help HashMap
				RULES.add(replaceColors(line));
			}
			
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public static void createDefaultVoteConfig(File voteConfig) {

		try {
			voteConfig.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					voteConfig.getPath()));

			writer.write("# This is the vote info config\n");

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void loadVoteConfig(File voteConfig) {

		if (!voteConfig.exists()) {
			Output.log(Level.SEVERE, "Config file Vote.cfg does not exist");
			return;
		}

		Output.log(Level.INFO, "Loading config file 'Vote.cfg'");

		try {
			BufferedReader reader = new BufferedReader(new FileReader(voteConfig.getPath()));

			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				// ignore comments and empty lines
				if (line.startsWith("#") || line.length() == 0)
					continue;

				// store the line into the array of help, which will be added to
				// the help HashMap
				VOTE.add(replaceColors(line));
			}
			
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
