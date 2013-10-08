/**
 * bHelpful 1.2-SNAPSHOT
 * Copyright (C) 2013  CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

	private List<String> messages = null;

	public RulesCommand() {
		super("rules");
		this.messages = new ArrayList<String>();
		HelpfulCommandHandler.registerCommand(this);
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
