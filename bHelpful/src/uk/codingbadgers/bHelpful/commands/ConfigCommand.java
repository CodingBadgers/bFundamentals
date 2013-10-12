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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bHelpful.Output;
import uk.codingbadgers.bHelpful.bHelpful;

/**
 * The Class ConfigCommand, represents a command which holds its output in a
 * config.
 * 
 * @see ModuleCommand
 */
public abstract class ConfigCommand extends ModuleCommand {

	/** If the command uses config file. */
	protected final boolean m_config;

	/** If the command is loaded. */
	private boolean m_loaded;

	/** The config file for the command. */
	protected File m_file;

	/**
	 * Instantiates a new config command.
	 * 
	 * @param label
	 *            the command label
	 * @param usage
	 *            the command usage
	 * @param config
	 *            if the command uses a config file on disk
	 */
	public ConfigCommand(String label, String usage, boolean config) {
		super(label, usage);
		this.m_config = config;
		this.m_loaded = false;
		if (config) {
			this.m_file = new File(bHelpful.MODULE.getDataFolder(), getClass().getSimpleName().substring(0, getClass().getSimpleName().indexOf("Command")) + ".cfg");
		}
	}

	/**
	 * Instantiates a new config command, which defaults to having a config.
	 * 
	 * @param label
	 *            the command label
	 * @param usage
	 *            the command usage
	 * @see ConfigCommand#ConfigCommand(String, String, boolean)
	 */
	public ConfigCommand(String label, String usage) {
		this(label, usage, true);
	}

	/**
	 * Instantiates a new config command, which defaults to having a config and
	 * the usage being the same as the label.
	 * 
	 * @param label
	 *            the command label
	 * @see ConfigCommand#ConfigCommand(String, String, boolean)
	 */
	public ConfigCommand(String label) {
		this(label, "/" + label, true);
	}

	/**
	 * Instantiates a new config command, which defaults to the usage being the
	 * same as the label.
	 * 
	 * @param label
	 *            the command label
	 * @param config
	 *            if the command uses a config
	 */
	public ConfigCommand(String label, boolean config) {
		this(label, "/" + label, config);
	}

	/**
	 * Handle the command.
	 * 
	 * @param sender
	 *            the sender
	 * @param label
	 *            the label
	 * @param args
	 *            the args
	 */
	protected abstract void handleCommand(CommandSender sender, String label, String[] args);

	/**
	 * Load the config.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	protected abstract void loadConfig() throws IOException;

	/**
	 * Load the command, creates the default config if the file doesn't exist
	 * and loads the config.
	 */
	public void loadCommand() {
		if (m_loaded) {
			return;
		}

		if (m_config) {
			Output.log(Level.INFO, "Loading config for command " + m_label + " (" + m_file.getAbsolutePath() + ")");
		} else {
			Output.log(Level.INFO, "Loading config for command " + m_label);
		}
		
		try {
			if (m_config && !m_file.exists()) {
				if (!m_file.createNewFile()) {
					// should never be called
					throw new IOException("Unkown error in creating default config file for " + m_label);
				}

				Output.log(Level.INFO, "Creating default config for command " + m_label + " (" + m_file.getAbsolutePath() + ")");

				InputStream ins = getClass().getResourceAsStream("config" + File.separator + m_file.getName());
				BufferedWriter writer = new BufferedWriter(new FileWriter(m_file));
				SimpleDateFormat format = new SimpleDateFormat("d/M/y h:m:s");
				writer.write('#' + m_file.getName() + " generated on " + format.format(new Date()) + "\n");

				BufferedReader reader = null;

                String line = "";
				try {
					if (ins != null) {
						reader = new BufferedReader(new InputStreamReader(ins));
						while ((line = reader.readLine()) != null) {
							writer.write(replaceFileMacros(line));
						}
					}
				} finally {
					if (writer != null) {
						writer.close();
					}
					if (reader != null) {
						reader.close();
					}
				}
			}

			loadConfig();
		} catch (IOException ex) {
			bHelpful.MODULE.getLogger().log(Level.WARNING, "Exception loading " + m_label + "'s config file (" + m_file.getName() + ")", ex);
			m_loaded = false;
			return;
		}

		m_loaded = true;
	}

	private String replaceFileMacros(String line) {
		line = line.replaceAll("%server%", Bukkit.getServerName());
		line = line.replaceAll("%d", String.valueOf(Calendar.getInstance().get(Calendar.DATE)));
		line = line.replaceAll("%m", String.valueOf(Calendar.getInstance().get(Calendar.MONTH + 1)));
		line = line.replaceAll("%y", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
		line = line.replaceAll("%h", String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
		line = line.replaceAll("%m", String.valueOf(Calendar.getInstance().get(Calendar.MINUTE)));
		line = line.replaceAll("%s", String.valueOf(Calendar.getInstance().get(Calendar.SECOND)));
		return line;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.codingbadgers.bFundamentals.commands.ModuleCommand#onCommand(org.bukkit
	 * .command.CommandSender, java.lang.String, java.lang.String[])
	 */
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		if (!m_loaded) {
			throw new CommandException("Command is not loaded");
		}

		handleCommand(sender, label, args);
		return true;
	}

	/**
	 * Replace minecraft colours in a string.
	 * 
	 * @param message
	 *            the message
	 * @return the string with colours replaced
	 */
	protected static String replaceColors(String message) {
		while (message.indexOf("&") != -1) {
			String code = message.substring(message.indexOf("&") + 1, message.indexOf("&") + 2);
			message = message.substring(0, message.indexOf("&")) + ChatColor.getByChar(code) + message.substring(message.indexOf("&") + 2);
		}
		return message;
	}
}
