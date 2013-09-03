/**
 * bFundamentalsBuild 1.2-SNAPSHOT
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
package uk.codingbadgers.bShortLinks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import uk.codingbadgers.bFundamentals.module.Module;

public class Global {
	
	/** The bFundamentals plugin. */
	static public Plugin PLUGIN = null;
	
	/** The bSHorLinks module. */
	static public Module MODULE = null;
	
	//! Configuration members
	/** The api to use. */
	static public String API = "adfly";
	
	/** The afdly api key. */
	static public String AFDLYAPIKEY = "";
	
	/** The afdly api uid. */
	static public String AFDLYAPIUID = "";
	
	/** The colour to use for urls in chat. */
	static public ChatColor URLCOLOUR = ChatColor.DARK_AQUA;
	
	/** Should we hide afdly links with tiny url links. */
	static public Boolean MASKWITHTINYURL = false;
	
	/** Add extra debug information. */
	static public Boolean DEBUG = false;
	
	/** A blacklist of words that cannot exist in a web pages header. */
	static public ArrayList<String> BLACKLIST = new ArrayList<String>();
	
	/**
	 * Output a message to the console.
	 *
	 * @param msg the message to output
	 */
	static public void OutputConsole(String msg) {
		MODULE.log(Level.INFO, msg);
	}
		
	/**
	 * Checks for permission.
	 *
	 * @param player the player
	 * @param node the permission node
	 * @return true, if the player has permission
	 */
	static public boolean HasPermission(Player player, String node) {
				
		// If they have vault, check with that
		if (MODULE.getPermissions() != null && MODULE.getPermissions().has(player, node))
			return true;
		
		// try bukkit perms...
		if (player.hasPermission(node))
			return true;
		
		// are they an op?
		if (player.isOp())
			return true;
		
		return false;
		
	}

	/**
	 * Load the config.yml and parse the blacklist.cfg
	 */
	public static void LoadConfig() {
		
		FileConfiguration config = MODULE.getConfig();
		
		try {
			config.addDefault("shorten_api", "adfly");
			config.addDefault("adfly_api_key", "");
			config.addDefault("adfly_api_uid", "");
			config.addDefault("mask_with_tinyurl", false);
			config.addDefault("url_colour", ChatColor.DARK_AQUA.getChar());
			config.addDefault("debug", false);
			config.options().copyDefaults(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		API = config.getString("shorten_api", "adfly");
		AFDLYAPIKEY = config.getString("adfly_api_key");
		AFDLYAPIUID = config.getString("adfly_api_uid");
		MASKWITHTINYURL = config.getBoolean("mask_with_tinyurl");
		URLCOLOUR = ChatColor.getByChar(config.getString("url_colour"));
		DEBUG = config.getBoolean("debug");
		
		MODULE.saveConfig();
		
		// create and load the black list
		File blackList = new File(MODULE.getDataFolder() + File.separator + "blacklist.cfg");
		
		if (!blackList.exists()) {
			try {
				blackList.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(blackList.getPath()));
				
				writer.write("# Add your blacklist words in here\n");
				writer.write("# If a webpage contains these words within the <head> tags the link will be ignored\n");
				
				writer.write("porn\n");
				writer.write("sex\n");
				writer.write("crack\n");
				
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(blackList.getPath()));

			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();

				// ignore comments and empty lines
				if (line.startsWith("#") || line.length() == 0)
					continue;

				BLACKLIST.add(line);

			}
			
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
