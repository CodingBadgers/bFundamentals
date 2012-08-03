package uk.codingbadgers.bHelpful;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Announcement extends Thread {
	
	bHelpful m_plugin = null;
	boolean m_running = false;
	
	Announcement(bHelpful plugin) {
		m_plugin = plugin;
		m_running = true;
	}
	
	public void run() {
		
		int lastRand = -1;
		int rand = -1;
		
		double noofMinutes = bHelpful.module.getConfig().getDouble("announcement.timedelay");
		
		final int sleepTime = (int)((noofMinutes * 60) * 1000);
		
		while(m_running) {
			
			if (Configuration.announcements.size() > 0) {
				Random random = new Random();
				while(rand == lastRand)
					rand = random.nextInt(Configuration.announcements.size());
					
				ArrayList<String> announcemnet = Configuration.announcements.get(rand);
				lastRand = rand;

				broadcast(announcemnet);
			}
			
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void kill() {
		m_running = false;
	}

	public void restart() {
		m_running = true;
		run();
	}
	
	public static void addAnnouncement(String announcement) {
		
		Configuration.announcements.clear();
		File announcementConfig = Configuration.announcementConfig;	
    	
    	try {
    		FileWriter fstream = new FileWriter(announcementConfig.getPath(),true);
			BufferedWriter writer = new BufferedWriter(fstream);

			writer.write("\n~ANNOUNCEMENT~\n");
			writer.write(announcement + "\n");
			writer.write("~ENDANNOUNCEMENT~\n");

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	Configuration.LoadAnnouncemnetConfig(announcementConfig);
	}
	
	public static void removeAnnouncement(int id)  {
		
		ArrayList<ArrayList<String>> announcements = Configuration.announcements;

		announcements.remove(id);
		
		File announcementConfig = Configuration.announcementConfig;	
		
		for (int i = 0; i<announcements.size(); i++) {
			
			try {
	    		FileWriter fstream = new FileWriter(announcementConfig.getPath());
				BufferedWriter writer = new BufferedWriter(fstream);
				
				String announcement = announcements.get(i).toString();				
				
				writer.write("~ANNOUNCEMENT~\n");
				writer.write(announcement + "\n");
				writer.write("~ENDANNOUNCEMENT~\n");

				writer.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static void broadcast(int id) {
		broadcast(Configuration.announcements.get(id));
	}
	
	public static void broadcast(ArrayList<String> announcement) {
		
		for (int i = 0; i < announcement.size(); ++i) {
			bHelpful.m_plugin.getServer().broadcastMessage(ChatColor.DARK_AQUA + "[Announcement] " + ChatColor.WHITE + announcement.get(i));
		}
	}
	
	public static boolean onCommand(CommandSender sender, String commandLabel, String[] args) {
		Player player = (Player) sender;
        
        if (commandLabel.equalsIgnoreCase("announce")) {
        	
        	if (args.length < 1) {
        		Output.player((Player)sender, "[bHelpful]", "/Announce <list/broadcast/add/remove>");
        		return true;
        	}
        	
        	if (args[0].equalsIgnoreCase("list")) {
        		
        		if (!bHelpful.hasPerms(player, "bhelpful.announcement.list")) {
        			Output.noPermission(player);
        			return true;
        		}
        		
        		for (int i = 0; i<Configuration.announcements.size(); i++) {
        			String index = String.valueOf(i + 1);
        			String announcement = Configuration.announcements.get(i).toString();
        			Output.player(player, ChatColor.GREEN + index + ".", ChatColor.WHITE + announcement);
        		}
        		
        		return true;
        		
        	}
        	
        	if (args[0].equalsIgnoreCase("add")) {
        		
        		if (!bHelpful.hasPerms(player, "bhelpful.announcement.add")) {
        			Output.noPermission(player);
        			return true;
        		}
        		
        		String trimedAnnouncement = "";
        		
        		for (int i = 1; i<args.length; i++) {
        			
        			trimedAnnouncement += args[i] + " ";
        			
        		}
        		
        		trimedAnnouncement.trim();
        		
        		trimedAnnouncement.replace(",", "\n ");
        		addAnnouncement(trimedAnnouncement);
        		
        		Output.player(player, "[bHelpful]", "Announcement added.");
        		
        		return true;
        		
        	}
        	
        	if (args[0].equalsIgnoreCase("remove")) {
        		
        		if (!bHelpful.hasPerms(player, "bhelpful.announcement.remove")) {
        			Output.noPermission(player);
        			return true;
        		}
        		
        		if (args.length != 2) {
        			Output.playerWarning(player, "/announce remove <id>");
        			return true;
        		}
        		
        		int index = 0;
        		try {
        			index = Integer.parseInt(args[1]);
        		} catch (NumberFormatException ex) {
        			Output.player(player, "[bHelpful]", "There was a error parsing the number you enterd");
        		}
        		
        		if (index > Configuration.announcements.size()){
        			Output.player(player, "[bHelpful]", "That announcement does not exist");
        			return true;
        		}
        		
        		removeAnnouncement(index - 1);
        		
        		Output.player(player, "[bHelpful]", "Announcement removed");
        		
        		return true;
        	}
        	
        	if (args[0].equalsIgnoreCase("broadcast")) {
        		
        		if (!bHelpful.hasPerms(player, "bhelpful.announcement.broadcast")) {
        			Output.noPermission(player);
        			return true;
        		}
        		
        		if (args.length != 2) {
        			Output.playerWarning(player, "/announce broadcast <id>");
        			return true;
        		}
        		
        		int index = 0;
        		try {
        			index = Integer.parseInt(args[1]);
        		} catch (NumberFormatException ex) {
        			Output.player(player, "[bHelpful]", "There was a error parsing the number you enterd");
        		}
        		
        		if (index > Configuration.announcements.size()){
        			Output.player(player, "[bHelpful]", "That announcement does not exist");
        			return true;
        		}
        		
        		broadcast(index - 1);
        		
        		Output.player(player, "[bHelpful]", "Announcement broadcasted");
        		
        		return true;
        		
        	}

        }
		return false;
	}
	
}
