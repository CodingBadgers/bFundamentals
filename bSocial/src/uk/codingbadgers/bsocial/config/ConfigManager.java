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
package uk.codingbadgers.bsocial.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import uk.codingbadgers.bsocial.bSocial;
import uk.codingbadgers.bsocial.chanels.ChatChannel;
import uk.codingbadgers.bsocial.players.ChatPlayer;

/**
 * The config manager
 */
public class ConfigManager {

	/** The channel directory. */
	private File channelDir = new File(bSocial.MODULE.getDataFolder() + File.separator + "channels" + File.separator);
	
	/** The player directory. */
	private File playerDir = new File(bSocial.MODULE.getDataFolder() + File.separator + "players" + File.separator);
	
	/** The default channel. */
	public static String DEFAULT_CHANNEL = null;
	
	/** The default format. */
	public static String DEFAULT_FORMAT = null;
	
	/**
	 * Load config.
	 */
	public void loadConfig() {
		
		FileConfiguration mainConfig = bSocial.MODULE.getConfig();
		
		try {
			mainConfig.addDefault("default.channel", "Global");
			mainConfig.addDefault("default.format", "[nick][prefix][name][suffix]: [colour]");
		
			mainConfig.options().copyDefaults(true);
			bSocial.MODULE.saveConfig();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		DEFAULT_CHANNEL = mainConfig.getString("default.channel");
		DEFAULT_FORMAT = mainConfig.getString("default.format");
		
		bSocial.MODULE.log(Level.INFO, "Default channel: " + DEFAULT_CHANNEL);
		bSocial.MODULE.log(Level.INFO, "Default format: " + DEFAULT_FORMAT);
		
		if (!channelDir.exists()) {
			channelDir.mkdir();
			createDefaultChannel();
		}
		
		if (!playerDir.exists()) {
			playerDir.mkdir();
		}
		
		loadChannels();
	}
	
	/**
	 * Load channels.
	 */
	private void loadChannels() {
		int channels = 0;
		for (File file : channelDir.listFiles()) {
			FileConfiguration config = getChannelConfig(file);
			
			String name = file.getName().substring(0, file.getName().indexOf('.'));
			String nick = config.getString("nick");
			ChatColor colour = ChatColor.valueOf(config.getString("colour"));
			String format = config.getString("format");
			boolean twitter = config.getBoolean("twitter");
			
			ChatChannel channel = new ChatChannel(nick, name, colour, format);
			channel.setTweet(twitter);
			
			bSocial.getChannelManager().addChannel(channel);
			channels++;
			bSocial.MODULE.log(Level.INFO, "Loaded: " + name + "(" + nick + ")");
		}
		
		bSocial.MODULE.log(Level.INFO, "Loaded " + channels + " channels into memory");
	}
	
	/**
	 * Creates the default channel.
	 */
	private void createDefaultChannel() {
		File channelConfig = new File(channelDir.getPath() + File.separator + "Global.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(channelConfig);
		
		try {
			config.addDefault("nick", "G");
	    	config.addDefault("colour", "WHITE");
	    	config.addDefault("format", "[default]");
	    	config.addDefault("twitter", false);
	    	
	    	config.options().copyDefaults(true);
	    	config.save(channelConfig);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Creates the channel config.
	 *
	 * @param channel the channel
	 */
	public void createChannelConfig(ChatChannel channel) {
		File channelConfig = new File(channelDir.getPath() + File.separator + channel.getChannelName() + ".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(channelConfig);
		
		try {
			config.addDefault("nick", channel.getNick());
	    	config.addDefault("colour", convertChatColour(channel.getColour()));
	    	config.addDefault("format", channel.getFormat());
	    	config.addDefault("twitter", channel.postToTwitter());
	    	
	    	config.options().copyDefaults(true);
	    	config.save(channelConfig);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
    /**
     * Gets the channel config.
     *
     * @param channelConfigFile the channel config file
     * @return the channel config
     */
    private FileConfiguration getChannelConfig(File channelConfigFile) {
    	FileConfiguration config = null;
    	
    	try {
	    	if (!channelConfigFile.exists())
	    		channelConfigFile.createNewFile();
	    	
	    	config = YamlConfiguration.loadConfiguration(channelConfigFile);
	    	
    	} catch (IOException e1 ) {
    		e1.printStackTrace();
    		return null;
    	} catch (Exception e2) {
    		e2.printStackTrace();
    		return null;
    	}
    	
        return config;
    }
    
    /**
     * Load a player.
     *
     * @param player the player
     * @return the chat player
     */
    public ChatPlayer loadPlayer(Player player){
    	File config = new File(bSocial.MODULE.getDataFolder() + File.separator + "players" + File.separator + player.getName() + ".yml");
    	FileConfiguration fileConfig = null;
    	
    	if (!config.exists()) {
    		try {
	    		config.createNewFile();
	    		
	    		fileConfig = YamlConfiguration.loadConfiguration(config);
	    		
	    		List<String> channels = new ArrayList<String>();
	    		channels.add(DEFAULT_CHANNEL);
	    		
	    		fileConfig.addDefault("active", DEFAULT_CHANNEL);
	    		fileConfig.addDefault("channels", channels);
	    		fileConfig.addDefault("muted", false);
	    		
	    		fileConfig.options().copyDefaults(true);
	    		fileConfig.save(config);
    		} catch (IOException e1) {
    			e1.printStackTrace();
    		}
    	} else {
    		fileConfig = YamlConfiguration.loadConfiguration(config);
    	}
    	 
    	String active = fileConfig.getString("active");
    	List<String> channels = fileConfig.getStringList("channels");
    	boolean muted = fileConfig.getBoolean("muted");
    	
    	ChatPlayer cPlayer = new ChatPlayer(player);
    	cPlayer.focusOn(bSocial.getChannelManager().getChannel(active));
    	cPlayer.setMuted(muted);
    	for(String channel : channels) 
    		cPlayer.joinChannel(bSocial.getChannelManager().getChannel(channel));
    	
    	return cPlayer;
    }
    
    /**
     * Save a player.
     *
     * @param player the player
     */
    public void savePlayer(ChatPlayer player) {
    	File config = new File(bSocial.MODULE.getDataFolder() + File.separator + "players" + File.separator + player.getPlayer().getName() + ".yml");
    	FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(config);
    	
    	try {
	    	fileConfig.set("active", player.getActiveChannel().getChannelName());
	    	fileConfig.set("channels", convert(player.getChannels()));
	    	fileConfig.set("muted", player.isMuted());
    	
			fileConfig.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Convert a List of channels into a list of strings for the config's
     *
     * @param channels the channels
     * @return the list of string for config
     */
    public List<String> convert(List<ChatChannel> channels) {
    	List<String> convertion = new ArrayList<String>();
    	for (ChatChannel channel: channels) {
    		convertion.add(channel.getChannelName());
    	}
    	return convertion;
    }

	/**
	 * Removes a channel.
	 *
	 * @param channel the channel
	 */
	public void removeChannel(ChatChannel channel) {
		File channelConfig = new File(channelDir.getPath() + File.separator + channel.getChannelName() + ".yml");
		channelConfig.delete();
	}
	
	/**
	 * Convert chat colour into a usage string.
	 *
	 * @param colour the colour
	 * @return the string
	 */
	public static String convertChatColour(ChatColor colour) {
		
		if (colour.equals(ChatColor.AQUA))
			return "AQUA";
		else if (colour.equals(ChatColor.BLACK))
			return "BLACK";
		else if (colour.equals(ChatColor.BLUE))
			return "BLUE";
		else if (colour.equals(ChatColor.DARK_AQUA))
			return "DARK_AQUA";
		else if (colour.equals(ChatColor.DARK_BLUE))
			return "DARK_BLUE";
		else if (colour.equals(ChatColor.DARK_GRAY))
			return "DARK_GRAY";
		else if (colour.equals(ChatColor.DARK_GREEN))
			return "DARK_GREEN";
		else if (colour.equals(ChatColor.DARK_PURPLE))
			return "DARK_PURPLE";
		else if (colour.equals(ChatColor.DARK_RED))
			return "DARK_RED";
		else if (colour.equals(ChatColor.GOLD))
			return "GOLD";
		else if (colour.equals(ChatColor.GRAY))
			return "GRAY";
		else if (colour.equals(ChatColor.GREEN))
			return "GREEN";
		else if (colour.equals(ChatColor.LIGHT_PURPLE))
			return "LIGHT_PURPLE";
		else if (colour.equals(ChatColor.RED))
			return "RED";
		else if (colour.equals(ChatColor.WHITE))
			return "WHITE";
		else if (colour.equals(ChatColor.YELLOW))
			return "YELLOW";
		else 
			return "WHITE";		
	}
}
