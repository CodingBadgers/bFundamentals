package uk.codingbadgers.bHelpful.commands.rw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.bHelpful.Configuration;
import uk.codingbadgers.bHelpful.Output;
import uk.codingbadgers.bHelpful.bHelpful;

public class MotdCommand extends ConfigCommand {

	private List<String> message = new ArrayList<String>();

	public MotdCommand() {
		super("motd");
	}

	@Override
	protected void handleCommand(CommandSender sender, String label, String[] args) {

		// minimap perms
		String minimapPerms = "&0&0";
		if (sender instanceof Player && bHelpful.hasPermission((Player)sender, "bhelpful.minimap.cavemapping"))
			minimapPerms += "&1";
		if (sender instanceof Player && bHelpful.hasPermission((Player)sender, "bhelpful.minimap.player"))
			minimapPerms += "&2";
		if (sender instanceof Player && bHelpful.hasPermission((Player)sender, "bhelpful.minimap.animal"))
			minimapPerms += "&3";
		if (sender instanceof Player && bHelpful.hasPermission((Player)sender, "bhelpful.minimap.mob"))
			minimapPerms += "&4";
		if (sender instanceof Player && bHelpful.hasPermission((Player)sender, "bhelpful.minimap.slime"))
			minimapPerms += "&5";
		if (sender instanceof Player && bHelpful.hasPermission((Player)sender, "bhelpful.minimap.squid"))
			minimapPerms += "&6";
		if (sender instanceof Player && bHelpful.hasPermission((Player)sender, "bhelpful.minimap.other"))
			minimapPerms += "&7";
		minimapPerms += "&e&f";
		
		sender.sendMessage(Configuration.replaceColors(minimapPerms));
		
		for (int i = 0; i < message.size(); i++) {
			sender.sendMessage(message.get(i));
		}
	}

	@Override
	protected void loadConfig() throws IOException {
		if (!m_file.exists()) {
			Output.log(Level.SEVERE, "Config file Motd.cfg does not exist");
			return;
		}

		Output.log(Level.INFO, "Loading config file 'Motd.cfg'");

		message.clear();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(m_file));

			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				// ignore comments and empty lines
				if (line.startsWith("#") || line.length() == 0) {
					continue;
				}

				message.add(replaceColors(line));
			}
			
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
