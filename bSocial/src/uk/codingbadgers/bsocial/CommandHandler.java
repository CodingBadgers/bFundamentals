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
package uk.codingbadgers.bsocial;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import uk.codingbadgers.bsocial.chanels.ChatChannel;
import uk.codingbadgers.bsocial.config.ConfigManager;
import uk.codingbadgers.bsocial.events.ChannelChatEvent;
import uk.codingbadgers.bsocial.events.PrivateMessageEvent;
import uk.codingbadgers.bsocial.players.ChatPlayer;

/**
 * The Class CommandHandler.
 */
public class CommandHandler {

	// bSocial.sendMessage(bSocial.MODULE.getName(), sender, "Channel command");
	/**
	 * Base command handling method.
	 *
	 * @param sender the sender
	 * @param command the command
	 * @param args the args
	 * @return true, if successful
	 */
	public static boolean onCommand(Player sender, String command, String[] args) {
		
		if (command.equalsIgnoreCase("chat")) {
			
			if (args.length < 1 || args[0].equalsIgnoreCase("help")) { 
				displayHelp(sender, args);
				return true;
			}
			
			
			ChatPlayer player = bSocial.getPlayerManager().findPlayer(sender);
			
			if (player == null) {
				bSocial.sendMessage(bSocial.MODULE.getName(), sender, "Sorry an error has occured and we cannot let you use this command, please tell the owner");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("join")) {
				handleJoinCommand(player, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("leave")) {		
				handleLeaveCommand(player, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("focus")) {			
				handleFocusCommand(player, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("list")) {
				handleListCommand(player, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("create")) {
				handleChannelCreateCommand(player, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("remove")) {
				handleChannelRemoveCommand(player, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("edit")) {
				handleChannelEditCommand(player, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("mute")) {
				handleMuteCommand(player, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("unmute")) {
				handleUnMuteCommand(player, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("info")) {
				handleInfoCommand(player, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("who")) {
				handleWhoCommand(player, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("reload")){
				handleReloadCommand(player, args);
				return true;
			}
			
			return true;
		}
		
		if (command.equalsIgnoreCase("pm")) {
			handlePrivateMessage(sender, args);
			return true;
		}
		
		if (bSocial.getChannelManager().isChannel(command)) {
			
			handleMessageCommand(sender, command, args);
			return true;
		}
		
		return false;
	}

	/**
	 * Handle message command.
	 *
	 * @param sender the sender
	 * @param command the command
	 * @param args the args
	 */
	private static void handleMessageCommand(Player sender, String command, String[] args) {
		if (args.length < 1) {
			bSocial.sendMessage(bSocial.MODULE.getName(), sender, "/" + command + " <message>");
			return;
		}
		
		ChatChannel channel = bSocial.getChannelManager().getChannel(command);
		ChatPlayer player = bSocial.getPlayerManager().findPlayer(sender);
		
		if (channel == null || player == null) {
			bSocial.sendMessage(bSocial.MODULE.getName(), sender, "Something is wrong, please tell the sever owner");
			return;
		}
		
		if (!player.isPartOf(channel)) {
			bSocial.sendMessage(bSocial.MODULE.getName(), sender, "Sorry you have to join the channel before you can speak in it");
			return;
		}
		
		// get the message
		String message = "";
		
		for (String word : args) 
			message += word + " ";
	
		message = message.trim();
		
		ChannelChatEvent chatEvent = new ChannelChatEvent(player, channel, message);
		bSocial.PLUGIN.getServer().getPluginManager().callEvent(chatEvent);
		
		if (chatEvent.isCancelled())
			return;
		
		chatEvent.getPlayer().sendMessage(chatEvent.getChannel(), chatEvent.getMessage());
		
		return;
	}

	/**
	 * Handle private message.
	 *
	 * @param sender the sender
	 * @param args the args
	 */
	private static void handlePrivateMessage(Player sender, String[] args) {
		if (args.length < 2) {
			bSocial.sendMessage(bSocial.MODULE.getName(), sender, "/pm <player> <message>");
			return;
		}
		
		ChatPlayer player = bSocial.getPlayerManager().findPlayer(sender);
		ChatPlayer to = bSocial.getPlayerManager().findPlayer(bSocial.PLUGIN.getServer().getPlayer(args[0]));
		
		if (player == null) {
			bSocial.sendMessage(bSocial.MODULE.getName(), sender, "Something is wrong, please tell the sever owner");
			return;
		}
		
		if (!player.hasPermission("bsocial.message", true))
			return;
		
		if (to == null) {
			bSocial.sendMessage(bSocial.MODULE.getName(), sender, "Sorry that player is not online");
			return;
		}
		
		// get the message
		String message = "";
					
		for (int i = 1; i < args.length; i ++) 
			message += args[i] + " ";
				
		message = message.trim();
					
		PrivateMessageEvent event = new PrivateMessageEvent(player, to, message);
		bSocial.PLUGIN.getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled())
			return;
		
		event.getPlayer().sendPrivateMessage(event.getReciever(), event.getMessage());
		event.getReciever().recievePrivateMessage(event.getPlayer(), event.getMessage());
		return;
	}

	/**
	 * Handle reload command.
	 *
	 * @param player the player
	 * @param args the args
	 */
	private static void handleReloadCommand(ChatPlayer player, String[] args) {
		if (!player.hasPermission("bsocial.command.admin.reload", true))
			return;
		
		bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "Sorry you cannot reload this modules at the moment via commands");
		// reload the plugin, will sort out later
	}

	/**
	 * Handle who command.
	 *
	 * @param player the player
	 * @param args the args
	 */
	private static void handleWhoCommand(ChatPlayer player, String[] args) {
		if (!player.hasPermission("bsocial.command.player.who", true))
			return;
					
		ChatChannel channel = player.getActiveChannel();
		if (args.length == 2) {
			channel = bSocial.getChannelManager().getChannel(args[1]);
		}
		
		List<ChatPlayer> players = channel.getPlayers();
		
		bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), channel.getChannelName() + "(" + players.size() + ")");
		
		StringBuilder message = new StringBuilder();
		boolean first = true;
		
		for (ChatPlayer cPlayer : players) {
			message.append(first ? "" : ", ").append(cPlayer.getPlayer().getName());
			first = false;
		}
		
		player.getPlayer().sendMessage(message.toString());
	}

	/**
	 * Handle info command.
	 *
	 * @param player the player
	 * @param args the args
	 */
	private static void handleInfoCommand(ChatPlayer player, String[] args) {
		if (!player.hasPermission("bsocial.command.player.info", true))
			return;
					
		ChatChannel channel = player.getActiveChannel();
		if (args.length == 2) {
			channel = bSocial.getChannelManager().getChannel(args[1]);
		}
		
		bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), channel.getChannelName() + ":");
		player.getPlayer().sendMessage(ChatColor.GREEN + "Nick: " + ChatColor.WHITE + channel.getNick());
		player.getPlayer().sendMessage(ChatColor.GREEN + "Format: " + ChatColor.WHITE + channel.getFormat());
		player.getPlayer().sendMessage(ChatColor.GREEN + "Colour: " + ChatColor.WHITE + channel.getColour() + ConfigManager.convertChatColour(channel.getColour()).toLowerCase());
	}

	/**
	 * Handle mute command.
	 *
	 * @param player the player
	 * @param args the args
	 */
	private static void handleMuteCommand(ChatPlayer player, String[] args) {
		if (!player.hasPermission("bsocial.command.admin.mute", true))
			return;
		
		if (args.length != 2) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "/chat mute <name>");
			return;	
		}
		
		ChatPlayer target = bSocial.getPlayerManager().findPlayer(bSocial.PLUGIN.getServer().getPlayer(args[1]));
		
		if (target == null) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "Sorry that player is not online");
			return;
		}
		
		if (!target.isMuted()) {
			target.setMuted(true);
		} else {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), target.getPlayer().getName() + " is already muted");
			return;
		}
		
		bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), target.getPlayer().getName() + " has been muted");
		bSocial.sendMessage(bSocial.MODULE.getName(), target.getPlayer(), "You have been muted by " + player.getPlayer().getName());
		
	}
	
	/**
	 * Handle un mute command.
	 *
	 * @param player the player
	 * @param args the args
	 */
	private static void handleUnMuteCommand(ChatPlayer player, String[] args) {
		if (!player.hasPermission("bsocial.command.admin.unmute", true))
			return;
		
		if (args.length != 2) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "/chat unmute <name>");
			return;	
		}
		
		ChatPlayer target = bSocial.getPlayerManager().findPlayer(bSocial.PLUGIN.getServer().getPlayer(args[1]));
		
		if (target == null) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "Sorry that player is not online");
			return;
		}
		
		if (target.isMuted()) {
			target.setMuted(false);
		} else {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), target.getPlayer().getName() + " not muted");
			return;
		}
		
		bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), target.getPlayer().getName() + " has been unmuted");
		bSocial.sendMessage(bSocial.MODULE.getName(), target.getPlayer(), "You have been unmuted by " + player.getPlayer().getName());
		
	}

	/**
	 * Handle list command.
	 *
	 * @param player the player
	 * @param args the args
	 */
	private static void handleListCommand(ChatPlayer player, String[] args) {
		
		if (!player.hasPermission("bsocial.command.player.list", true))
			return;
		
		List<ChatChannel> channels = bSocial.getChannelManager().getChannels();
		bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "Channels:");
		
		for (int i = 0; i < channels.size(); i++) {
			ChatChannel channel = channels.get(i);
			player.getPlayer().sendMessage(ChatColor.DARK_PURPLE + "" + (i+1) + ". " + channel.getColour() + (player.isPartOf(channel) ? "*" : "")+ channel.getChannelName() + "[" + channel.getNick() + "]");			
		}
	}

	/**
	 * Handle join command.
	 *
	 * @param player the player
	 * @param args the args
	 */
	private static void handleJoinCommand(ChatPlayer player, String[] args) {
		if (!player.hasPermission("bsocial.command.player.join", true))
			return;
		
		if (args.length != 2) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "/chat join <channel>");
			return;
		}
		
		ChatChannel channel = bSocial.getChannelManager().getChannel(args[1]);
		
		if (channel == null) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "Sorry that channel does not exist");
			return;
		}
		
		if (!player.hasPermission("bsocial.join." + channel.getChannelName(), true))
			return;
		
		if (player.isPartOf(channel)) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "You are already in that channel, and cannot join it again");
			return;
		}
		
		player.joinChannel(channel);
		bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "You have joined " + channel.getChannelName());	
	}

	/**
	 * Handle leave command.
	 *
	 * @param player the player
	 * @param args the args
	 */
	private static void handleLeaveCommand(ChatPlayer player, String[] args) {
		
		if (player.hasPermission("bsocial.command.player.leave", true))
			return;
		
		if (args.length != 2) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "/chat leave <channel>");
			return;
		}
		
		ChatChannel channel = bSocial.getChannelManager().getChannel(args[1]);
		
		if (channel == null) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "Sorry that channel does not exist");
			return;
		}
		
		if (!player.hasPermission("bsocial.leave." + channel.getChannelName(), true))
			return;
		
		if (!player.isPartOf(channel)) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "You are not in that channel, so you cant leave it");
			return;
		}
		
		player.leaveChannel(channel);
		bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "You have left " + channel.getChannelName());			
	}

	/**
	 * Handle focus command.
	 *
	 * @param player the player
	 * @param args the args
	 */
	private static void handleFocusCommand(ChatPlayer player, String[] args) {
		
		if (!player.hasPermission("bsocial.command.player.focus", true))
			return;
		
		if (args.length != 2) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "/chat focus <channel>");
			return;
		}
		
		ChatChannel channel = bSocial.getChannelManager().getChannel(args[1]);
		
		if (channel == null) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "Sorry that channel does not exist");
			return;
		}
		
		player.focusOn(channel);
		bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "You have focused on " + channel.getChannelName());					
	}
	
	/**
	 * Handle channel create command.
	 *
	 * @param player the player
	 * @param args the args
	 */
	private static void handleChannelCreateCommand(ChatPlayer player, String[] args) {
		
		if (!player.hasPermission("bsocial.command.admin.create", true))
			return;
		
		if (args.length < 2) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "/chat create <channel> [colour] [nick] [tweet]");
			return;
		}
		
		String name = args[1];
		ChatColor colour = ChatColor.WHITE;
		String nick = args[1];
		boolean tweet = false;
		
		if (args.length >= 3)
			colour = ChatColor.valueOf(args[2].toUpperCase());
		if (args.length >= 4)
			nick = args[3];
		if (args.length >= 5)
			tweet = Boolean.valueOf(args[4]);
		
		ChatChannel channel = new ChatChannel(nick, name, colour, "[default]");
		channel.setTweet(tweet);
		
		bSocial.getChannelManager().addChannel(channel);
		bSocial.getConfigManager().createChannelConfig(channel);
		bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "Channel " + name + " added to the game");
	}
	
	/**
	 * Handle channel remove command.
	 *
	 * @param player the player
	 * @param args the args
	 */
	private static void handleChannelRemoveCommand(ChatPlayer player, String[] args) {
		
		if (!player.hasPermission("bsocial.command.admin.remove", true))
			return;
		
		if (args.length != 2) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "/chat remove <channel>");
			return;
		}
		
		ChatChannel channel = bSocial.getChannelManager().getChannel(args[1]);
		
		if (channel == null) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "That channel doesn't exist so you can't remove it");
			return;
		}
		
		bSocial.getChannelManager().removeChannel(channel);
		bSocial.getConfigManager().removeChannel(channel);
		bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "You have removed " + channel.getChannelName());
	}
	
	/**
	 * Handle channel edit command.
	 *
	 * @param player the player
	 * @param args the args
	 */
	private static void handleChannelEditCommand(ChatPlayer player, String[] args) {
		if (!player.hasPermission("bsocial.command.admin.remove", true))
			return;
		
		if (args.length != 4) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "/chat edit <channel> <option> <value>");
			return;
		}
		
		ChatChannel channel = bSocial.getChannelManager().getChannel(args[1]);
		
		if (channel == null) {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "That channel doesn't exist so you can't edit it");
			return;
		}
		
		String option = args[2];
		String value = args[3];
		
		if (option.equalsIgnoreCase("nick")) {
			channel.setNick(value);
		} else if (option.equalsIgnoreCase("colour")) {
			try {
				channel.setColour(ChatColor.valueOf(value.toUpperCase()));
			} catch (Exception ex) {
				bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "Sorry that is not a valid colour");
				return;
			}
		} else if (option.equalsIgnoreCase("tweet")) {
			channel.setTweet(Boolean.valueOf(value));
		} else {
			bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "That is not a valid option, use on of: nick(nickname), colour or tweet");
			return;
		}
		
		bSocial.sendMessage(bSocial.MODULE.getName(), player.getPlayer(), "You have set " + option + " to " + value + " in " + channel.getChannelName());
		
	}

	/**
	 * Display help.
	 *
	 * @param sender the sender
	 * @param args the args
	 */
	private static void displayHelp(Player sender, String[] args) {
		int pages = 2;
		
		int page = 1;
		if (args.length == 2) 
			page = Integer.parseInt(args[1]);
		
		if (page > page)
			page = 1;
		
		sender.sendMessage(ChatColor.DARK_PURPLE + "-------<bSocial help (" + page + "/" + pages +")>-------");
		if (page == 1) {
			sender.sendMessage(ChatColor.GREEN + "/chat leave <channel>");
			sender.sendMessage(ChatColor.GREEN + "/chat focus <channel>");
			sender.sendMessage(ChatColor.GREEN + "/chat list");
			sender.sendMessage(ChatColor.GREEN +  "/chat who <channel>");
			sender.sendMessage(ChatColor.GREEN +  "/chat create <channel>" + ChatColor.GRAY + " [colour] [nick] [tweet]");
			sender.sendMessage(ChatColor.GREEN +  "/chat remove <channel>");
			sender.sendMessage(ChatColor.GREEN +  "/chat edit <channel> <option> <value>");
		} else if (page == 2) {
			sender.sendMessage(ChatColor.GREEN + "/chat mute <player>");
			sender.sendMessage(ChatColor.GREEN + "/chat unmute <player>");
			sender.sendMessage(ChatColor.GREEN + "/chat info <channel>");
			sender.sendMessage(ChatColor.GREEN + "/chat reload");
		}

	}
}
