package uk.codingbadgers.bHelpful.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bHelpful.Config;
import uk.codingbadgers.bHelpful.Output;
import uk.codingbadgers.bHelpful.bHelpful;

public class AnnouncementCommand extends ConfigCommand {

	private List<List<String>> announcements = null;
	private int lastAnnouncement = -1;
	private BukkitTask task = null;

	public AnnouncementCommand() {
		super(Config.ANNOUNCE_LABEL, "/" + Config.ANNOUNCE_LABEL + " <list/broadcast/add/remove>", true);
		announcements = new ArrayList<List<String>>();
	}

	@Override
	protected void handleCommand(CommandSender sender, String label, String[] args) {

		if (args.length < 1) {
			Output.player(sender, "[bHelpful]", m_usage);
			return;
		}

		if (args[0].equalsIgnoreCase("list")) {
			if (sender instanceof Player && !bHelpful.hasPermission((Player) sender, "bhelpful.announcement.list")) {
				Output.noPermission(sender);
				return;
			}

			for (int i = 0; i < announcements.size(); i++) {
				String index = String.valueOf(i + 1);
				String announcement = announcements.get(i).toString();
				Output.player(sender, ChatColor.GREEN + index + ".", ChatColor.WHITE + announcement);
			}

			return;
		}

		if (args[0].equalsIgnoreCase("add")) {
			if (sender instanceof Player && !bHelpful.hasPermission((Player) sender, "bhelpful.announcement.add")) {
				Output.noPermission(sender);
				return;
			}

			String trimedAnnouncement = "";

			for (int i = 1; i < args.length; i++) {
				trimedAnnouncement += args[i] + " ";
			}

			trimedAnnouncement = trimedAnnouncement.trim();
			addAnnouncement(trimedAnnouncement);

			Output.player(sender, "[bHelpful]", "Announcement added.");
			return;
		}

		if (args[0].equalsIgnoreCase("remove")) {
			if (sender instanceof Player && !bHelpful.hasPermission((Player) sender, "bhelpful.announcement.remove")) {
				Output.noPermission(sender);
				return;
			}

			if (args.length != 2) {
				Output.playerWarning(sender, "/announce remove <id>");
				return;
			}

			int index = 0;
			try {
				index = Integer.parseInt(args[1]);
			} catch (NumberFormatException ex) {
				Output.player(sender, "[bHelpful]", "There was a error parsing the number you enterd");
			}

			if (index > announcements.size()) {
				Output.player(sender, "[bHelpful]", "That announcement does not exist");
				return;
			}

			removeAnnouncement(index - 1);
			Output.player(sender, "[bHelpful]", "Announcement removed");
			return;
		}

		if (args[0].equalsIgnoreCase("broadcast")) {
			if (sender instanceof Player && !bHelpful.hasPermission((Player) sender, "bhelpful.announcement.broadcast")) {
				Output.noPermission(sender);
				return;
			}

			if (args.length != 2) {
				Output.playerWarning(sender, "/announce broadcast <id>");
				return;
			}

			int index = 0;
			try {
				index = Integer.parseInt(args[1]);
			} catch (NumberFormatException ex) {
				Output.player(sender, "[bHelpful]", "There was a error parsing the number you enterd");
			}

			if (index > announcements.size()) {
				Output.player(sender, "[bHelpful]", "That announcement does not exist");
				return;
			}

			broadcast(index - 1);
			Output.player(sender, "[bHelpful]", "Announcement broadcasted");
			return;
		}
	}

	@Override
	protected void loadConfig() throws IOException {
		if (!m_file.exists()) {
			Output.log(Level.SEVERE, "Config file Announcement.cfg does not exist");
			return;
		}

		if (task != null) {
			task.cancel();
		}
		announcements.clear();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(m_file.getPath()));

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
					if (line.equalsIgnoreCase("~ENDANNOUNCEMENT~")) {
						break;
					}
					newAnnouncement.add(replaceColors(line));
				}

				announcements.add(newAnnouncement);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int interval = Config.ANNOUNCEMENT_INTERVAL * 20;

		task = new AnnouncementRunnable().runTaskTimer(bFundamentals.getInstance(), interval, interval);
	}

	public class AnnouncementRunnable extends BukkitRunnable {
		@Override
		public void run() {
			int rand = -1;
			if (announcements.size() > 0) {
				Random random = new Random();
				do {
					rand = random.nextInt(announcements.size());
				} while (rand == lastAnnouncement);

				lastAnnouncement = rand;
				broadcast(rand);
			}
		}
	}

	private void broadcast(List<String> announcement) {
		for (int i = 0; i < announcement.size(); ++i) {
			bHelpful.PLUGIN.getServer().broadcastMessage(ChatColor.DARK_AQUA + "[Announcement] " + ChatColor.WHITE + announcement.get(i));
		}
	}

	private void broadcast(int id) {
		broadcast(announcements.get(id));
	}

	private void addAnnouncement(String announcement) {
		List<List<String>> announcements = this.announcements;
		ArrayList<String> announceArray = new ArrayList<String>();

		for (String part : announcement.split(";")) {
			announceArray.add(part.trim());
		}

		announcements.add(announceArray);

		File announcementConfig = m_file;

		try {
			FileWriter fstream = new FileWriter(announcementConfig.getPath(), true);
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
	
	private void removeAnnouncement(int id) {
		List<List<String>> announcements = this.announcements;
		announcements.remove(id);

		File announcementConfig = m_file;

		try {
			FileWriter fstream = new FileWriter(announcementConfig.getPath());
			BufferedWriter writer = new BufferedWriter(fstream);

			for (int i = 0; i < announcements.size(); i++) {
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
}