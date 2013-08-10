package uk.codingbadgers.bHelpful.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import uk.codingbadgers.bHelpful.Output;

public class RulesCommand extends ConfigCommand {

	private List<String> messages = new ArrayList<String>();

	public RulesCommand() {
		super("rules");
	}

	@Override
	protected void handleCommand(CommandSender sender, String label, String[] args) {
		for (int i = 0; i < messages .size(); ++i) {
			sender.sendMessage(ChatColor.RED + Integer.toString(i + 1) + ". " + ChatColor.WHITE + messages.get(i));
		}
	}

	@Override
	protected void loadConfig() throws IOException {
		if (!m_file.exists()) {
			Output.log(Level.SEVERE, "Config file Rules.cfg does not exist");
			return;
		}

		Output.log(Level.INFO, "Loading config file 'Rules.cfg'");
		messages.clear();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(m_file));

			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				// ignore comments and empty lines
				if (line.startsWith("#") || line.length() == 0) {
					continue;
				}

				messages.add(replaceColors(line));
			}
			
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
