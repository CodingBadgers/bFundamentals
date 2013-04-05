package uk.codingbadgers.bHelpful.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.bHelpful.Configuration;
import uk.codingbadgers.bHelpful.Output;
import uk.codingbadgers.bHelpful.bHelpful;

public class Announcement extends Thread {
	
	/** Access to the plugin object. */
	bHelpful m_plugin = null;
	
	/** The flag storing whether we are running. */
	boolean m_running = false;
	
	/**
	 * Instantiates a new announcement.
	 *
	 * @param plugin the plugin
	 */
	public Announcement(bHelpful plugin) {
		m_plugin = plugin;
		m_running = true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
		int lastRand = -1;
		int rand = -1;
		
		double noofMinutes = bHelpful.MODULE.getConfig().getDouble("announcement.timedelay");
		
		final int sleepTime = (int)((noofMinutes * 60) * 1000);
		
		while(m_running) {
			
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (Configuration.ANNOUCNEMENTS.size() > 0) {
				Random random = new Random();
				while(rand == lastRand)
					rand = random.nextInt(Configuration.ANNOUCNEMENTS.size());
					
				ArrayList<String> announcemnet = Configuration.ANNOUCNEMENTS.get(rand);
				lastRand = rand;

				broadcast(announcemnet);
			}
						
		}
	}
	
	/**
	 * Kill the thread.
	 */
	public void kill() {
		m_running = false;
	}

	/**
	 * Restart the thread.
	 */
	public void restart() {
		m_running = true;
		run();
	}
	
	/**
	 * Adds the announcement.
	 *
	 * @param announcement the announcement
	 */
	public static void addAnnouncement(String announcement) {
		
		List<ArrayList<String>> announcements = Configuration.ANNOUCNEMENTS;
		ArrayList<String> announceArray = new ArrayList<String>();
			
		for(String part : announcement.split(";")) {
			announceArray.add(part.trim());
		}
		
		announcements.add(announceArray);
		
		File announcementConfig = Configuration.ANNOUNCEMENT_CONIFG;	
    	
    	try {
    		FileWriter fstream = new FileWriter(announcementConfig.getPath(),true);
			BufferedWriter writer = new BufferedWriter(fstream);

			writer.write("\n~ANNOUNCEMENT~\n");
			for (String part : announceArray) {
				writer.write(part + "\n");
			}
			writer.write("~ENDANNOUNCEMENT~\n");

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Removes the announcement.
	 *
	 * @param id the id
	 */
	public static void removeAnnouncement(int id)  {
		
		List<ArrayList<String>> announcements = Configuration.ANNOUCNEMENTS;

		announcements.remove(id);
		
		File announcementConfig = Configuration.ANNOUNCEMENT_CONIFG;	
		
		try {
			FileWriter fstream = new FileWriter(announcementConfig.getPath());
			BufferedWriter writer = new BufferedWriter(fstream);
			
			for (int i = 0; i<announcements.size(); i++) {		
				
				String announcement = "";
				
				for (int j = 0; j < announcements.get(i).size(); j++) {
					announcement += announcements.get(i).get(j) + "\n";
				}
				
				writer.write("~ANNOUNCEMENT~\n");
				writer.write(announcement);
				writer.write("~ENDANNOUNCEMENT~\n");
			}
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Broadcast.
	 *
	 * @param id the id
	 */
	public static void broadcast(int id) {
		broadcast(Configuration.ANNOUCNEMENTS.get(id));
	}
	
	/**
	 * Broadcast.
	 *
	 * @param announcement the announcement
	 */
	public static void broadcast(ArrayList<String> announcement) {
		
		for (int i = 0; i < announcement.size(); ++i) {
			bHelpful.PLUGIN.getServer().broadcastMessage(ChatColor.DARK_AQUA + "[Announcement] " + ChatColor.WHITE + announcement.get(i));
		}
	}
	
	/**
	 * On command.
	 *
	 * @param sender the sender
	 * @param commandLabel the command label
	 * @param args the args
	 * @return true, if successful
	 */
	public static boolean onCommand(CommandSender sender, String commandLabel, String[] args) {
        
        if (commandLabel.equalsIgnoreCase("announce")) {
        	
        	if (args.length < 1) {
        		Output.player(sender, "[bHelpful]", "/Announce <list/broadcast/add/remove>");
        		return true;
        	}
        	
        	if (args[0].equalsIgnoreCase("list")) {
        		
        		if (sender instanceof Player && !bHelpful.hasPermission((Player)sender, "bhelpful.announcement.list")) {
        			Output.noPermission(sender);
        			return true;
        		}
        		
        		for (int i = 0; i<Configuration.ANNOUCNEMENTS.size(); i++) {
        			String index = String.valueOf(i + 1);
        			String announcement = Configuration.ANNOUCNEMENTS.get(i).toString();
        			Output.player(sender, ChatColor.GREEN + index + ".", ChatColor.WHITE + announcement);
        		}
        		
        		return true;
        		
        	}
        	
        	if (args[0].equalsIgnoreCase("add")) {
        		
        		if (sender instanceof Player && !bHelpful.hasPermission((Player)sender, "bhelpful.announcement.add")) {
        			Output.noPermission(sender);
        			return true;
        		}
        		
        		String trimedAnnouncement = "";
        		
        		for (int i = 1; i<args.length; i++) {
        			
        			trimedAnnouncement += args[i] + " ";
        			
        		}
        		
        		trimedAnnouncement.trim();
        		
        		trimedAnnouncement.replace(",", "\n ");
        		addAnnouncement(trimedAnnouncement);
        		
        		Output.player(sender, "[bHelpful]", "Announcement added.");
        		
        		return true;
        		
        	}
        	
        	if (args[0].equalsIgnoreCase("remove")) {
        		
        		if (sender instanceof Player && !bHelpful.hasPermission((Player)sender, "bhelpful.announcement.remove")) {
        			Output.noPermission(sender);
        			return true;
        		}
        		
        		if (args.length != 2) {
        			Output.playerWarning(sender, "/announce remove <id>");
        			return true;
        		}
        		
        		int index = 0;
        		try {
        			index = Integer.parseInt(args[1]);
        		} catch (NumberFormatException ex) {
        			Output.player(sender, "[bHelpful]", "There was a error parsing the number you enterd");
        		}
        		
        		if (index > Configuration.ANNOUCNEMENTS.size()){
        			Output.player(sender, "[bHelpful]", "That announcement does not exist");
        			return true;
        		}
        		
        		removeAnnouncement(index - 1);
        		
        		Output.player(sender, "[bHelpful]", "Announcement removed");
        		
        		return true;
        	}
        	
        	if (args[0].equalsIgnoreCase("broadcast")) {
        		
        		if (sender instanceof Player && !bHelpful.hasPermission((Player)sender, "bhelpful.announcement.broadcast")) {
        			Output.noPermission(sender);
        			return true;
        		}
        		
        		if (args.length != 2) {
        			Output.playerWarning(sender, "/announce broadcast <id>");
        			return true;
        		}
        		
        		int index = 0;
        		try {
        			index = Integer.parseInt(args[1]);
        		} catch (NumberFormatException ex) {
        			Output.player(sender, "[bHelpful]", "There was a error parsing the number you enterd");
        		}
        		
        		if (index > Configuration.ANNOUCNEMENTS.size()){
        			Output.player(sender, "[bHelpful]", "That announcement does not exist");
        			return true;
        		}
        		
        		broadcast(index - 1);
        		
        		Output.player(sender, "[bHelpful]", "Announcement broadcasted");
        		
        		return true;
        		
        	}

        }
		return false;
	}
	
}
