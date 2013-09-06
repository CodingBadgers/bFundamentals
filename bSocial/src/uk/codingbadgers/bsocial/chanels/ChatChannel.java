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
package uk.codingbadgers.bsocial.chanels;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bsocial.config.ConfigManager;
import uk.codingbadgers.bsocial.players.ChatPlayer;

/**
 * The object for a chat channel, handles sending messages and formating them.
 * 
 * @author james
 */
public class ChatChannel {
	
	/** The nickname for the channel. */
	private String m_nick = null;
	
	/** The name of the channel. */
	private String m_name = null;
	
	/** The the colour of the channel. */
	private ChatColor m_colour = null;
	
	/** The players in the channel (online). */
	private List<ChatPlayer> m_players = null;
	
	/** The channel message format. */
	private String m_format = null;

	/** Whether to post to twitter or not if the user has permission. */
	private boolean m_tweet = false;
	
	/**
	 * Instantiates a new chat channel.
	 *
	 * @param nick the nick
	 * @param name the name
	 * @param colour the colour
	 * @param format the format
	 * @param players the players
	 */
	public ChatChannel(String nick, String name, ChatColor colour, String format, List<ChatPlayer> players) {
		m_name = name;
		m_nick = nick;
		m_colour = colour;
		m_players = players;
		if (format.equalsIgnoreCase("[default]"))
			m_format = ConfigManager.DEFAULT_FORMAT;
		else 
			m_format = format;
	}
	
	/**
	 * Instantiates a new chat channel.
	 *
	 * @param nick the nick
	 * @param name the name
	 * @param colour the colour
	 * @param format the format
	 */
	public ChatChannel(String nick, String name, ChatColor colour, String format) {
		this(nick, name, colour, format, new ArrayList<ChatPlayer>());
	}
	
	/**
	 * Instantiates a new chat channel.
	 *
	 * @param name the name
	 * @param colour the colour
	 * @param format the format
	 * @param players the players
	 */
	public ChatChannel(String name, ChatColor colour, String format, List<ChatPlayer> players) {
		this(name, name, colour, format, players);
	}
	
	/**
	 * Instantiates a new chat channel.
	 *
	 * @param name the name
	 * @param colour the colour
	 * @param format the format
	 */
	public ChatChannel(String name, ChatColor colour, String format) {
		this(name, name, colour, format, new ArrayList<ChatPlayer>());
	}

	/**
	 * Instantiates a new chat channel.
	 *
	 * @param name the name
	 * @param colour the colour
	 */
	public ChatChannel(String name, ChatColor colour) {
		this(name, name, colour, ConfigManager.DEFAULT_FORMAT, new ArrayList<ChatPlayer>());
	}
	
	/**
	 * Gets the channel name.
	 *
	 * @return the channel name
	 */
	public String getChannelName() {
		return m_name;	
	}
	
	/**
	 * Gets the colour for the channel.
	 *
	 * @return the colour for the channel
	 */
	public ChatColor getColour () {
		return m_colour;
	}
	
	/**
	 * Gets the players in this channel.
	 *
	 * @return the players in this channel
	 */
	public List<ChatPlayer> getPlayers() {
		return m_players;
	}
	
	/**
	 * Gets the format.
	 *
	 * @return the format
	 */
	public String getFormat() {
		return m_format;
	}
	
	/**
	 * Gets the nick.
	 *
	 * @return the nick
	 */
	public String getNick() {
		return m_nick;
	}
	
	/**
	 * Send message in this channel.
	 *
	 * @param sender the sender
	 * @param message the message
	 */
	public void sendMessage(Player sender, String message) {
		for (ChatPlayer player : m_players) {
			player.getPlayer().sendMessage(format(sender, message));
		}
	}
	
	/**
	 * Format a message for the channel.
	 *
	 * @param sender the sender
	 * @param raw the raw
	 * @return the string
	 */
	public String format(Player sender, String raw) { 
		String message = m_format;
		
		if (message == null)
			message = " ";
		
		message = message.replace("[nick]", "[" + m_nick + "]");
		message = message.replace("[ch]", "[" + m_name + "]");
		message = message.replace("[prefix]", bFundamentals.getChat().getPlayerPrefix(sender));
		message = message.replace("[name]", sender.getDisplayName());
		message = message.replace("[suffix]", bFundamentals.getChat().getPlayerSuffix(sender));
		message = message.replace("[colour]", m_colour.toString());
		message = parseColour(message);
		message += raw;
		
		return message;
	}

	/**
	 * Parses a message for colour codes.
	 *
	 * @param string the string to parse
	 * @return the formated string
	 */
	private String parseColour(String string) {
		// Store the message in a temp buffer
		String formattedMessage = string;
				
		// we'll replace the &<c> as we go, so looop through all of them
		while(formattedMessage.indexOf("&") != -1) {
					
			// get the colour code first as a string, then convert the hex based char into an integer
			String code = formattedMessage.substring(formattedMessage.indexOf("&") + 1, formattedMessage.indexOf("&") + 2);
					
			// get all the text up to the first '&'
			// get the chat colour from our colour code
			// skip the &<c> and reattach the rest of the string
			formattedMessage = formattedMessage.substring(0, formattedMessage.indexOf("&")) +  ChatColor.getByChar(code) + formattedMessage.substring(formattedMessage.indexOf("&") + 2);
										
		}
				
		return formattedMessage;
	}
	
	/**
	 * Adds the player.
	 *
	 * @param player the player
	 */
	public void addPlayer(ChatPlayer player) {
		m_players.add(player);
	}

	/**
	 * Removes the player.
	 *
	 * @param chatPlayer the chat player
	 */
	public void removePlayer(ChatPlayer chatPlayer) {
		m_players.remove(chatPlayer);
	}
	
	/**
	 * Sets whether posting in this channel will cause it to send a tweet as that player.
	 *
	 * @param twitter the new tweet
	 */
	public void setTweet(boolean twitter) {
		m_tweet = twitter;
	}
	
	/**
	 * Whether posting in this channel will cause it to send a tweet as that player.
	 *
	 * @return true, if successful
	 */
	public boolean postToTwitter() {
		return m_tweet;
	}

	/**
	 * Sets the nick for the channel.
	 *
	 * @param value the new nick
	 */
	public void setNick(String value) {
		m_nick = value;
	}

	/**
	 * Sets the colour for the channel
	 *
	 * @param value the new colour
	 */
	public void setColour(ChatColor value) {
		m_colour = value;
	}
} 
