package uk.codingbadgers.bHelpful.commands;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import uk.codingbadgers.bHelpful.Output;

public class RegisterCommand extends ConfigCommand {

	private List<String> message = new ArrayList<String>();
	
	public RegisterCommand() {
		super("register");
	}

	@Override
	protected void handleCommand(CommandSender sender, String label, String[] args) {
    	for( int i = 0; i < message.size(); ++i ) {
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
