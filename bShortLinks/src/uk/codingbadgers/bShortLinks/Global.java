package uk.codingbadgers.bShortLinks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import uk.codingbadgers.bFundamentals.module.Module;

public class Global {
	
	static public Plugin plugin = null;
	static public Permission permission = null;
	static public Module module = null;
	
	//! Configuration members
	static public String API = "adfly";
	static public String AfdlyAPIKey = "";
	static public String AfdlyAPIUid = "";
	static public ChatColor UrlColour = ChatColor.DARK_AQUA;
	static public Boolean MaskWithTinyUrl = false;
	static public Boolean DEBUG = false;
	static public ArrayList<String> BlackList = new ArrayList<String>();
	
	/**
	 * Output a message to the console.
	 *
	 * @param msg the message to output
	 */
	static public void OutputConsole(String msg) {
		module.log(Level.INFO, msg);
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
		if (permission != null && permission.has(player, node))
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
		
		FileConfiguration config = module.getConfig();
		
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
		AfdlyAPIKey = config.getString("adfly_api_key");
		AfdlyAPIUid = config.getString("adfly_api_uid");
		MaskWithTinyUrl = config.getBoolean("mask_with_tinyurl");
		UrlColour = ChatColor.getByChar(config.getString("url_colour"));
		DEBUG = config.getBoolean("debug");
		
		module.saveConfig();
		
		// create and load the black list
		File blackList = new File(module.getDataFolder() + File.separator + "blacklist.cfg");
		
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

				BlackList.add(line);

			}
			
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
