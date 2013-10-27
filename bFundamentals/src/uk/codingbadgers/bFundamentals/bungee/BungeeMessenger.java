/**
 * bFundamentals 1.2-SNAPSHOT
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
package uk.codingbadgers.bFundamentals.bungee;

import java.io.ByteArrayOutputStream;

import org.bukkit.entity.Player;

/**
 * The BungeeMessenger interface, provides easy access to the bungee plugin
 * channel api.
 */
public interface BungeeMessenger {

    /**
     * Send a raw message, and queue it if required.
     *
     * @param message the message to send
     * @return true, if message was sent, false if no players were online to
     *          send it and so it was queued.
     */
    public boolean sendRawMessage(byte[] message);
    
    /**
     * Send raw message, and queue it if required.
     *
     * @param message the message to send
     * @param queue if the message should be queued to send later
     * @return true, if successful false if no players were online to
     *          send it.
     */
    public boolean sendRawMessage(byte[] message, boolean queue);

    /**
     * Send a raw message through a player.
     *
     * @param player the player to send the message through
     * @param message the message to send
     */
    public void sendRawPlayerMessage(Player player, byte[] message);

    /**
     * Send a command through a player.
     *
     * @param command the command to send
     * @param player the player to send it through
     * @param args the arguments to go with the command
     */
    public void sendPlayerCommand(String command, Player player, String... args);
    
    /**
     * Send bungee command.
     *
     * @param command the command to send
     * @param args the arguments to go with the command
     */
    public void sendBungeeCommand(String command, String... args);

    /**
     * Send all currently queued commands if possible.
     */
    public void sendQueuedCommands();

    /**
     * Forward a message to another server.
     *
     * @param servers the servers to send the message to
     * @param subchannel the subchannel to send it along
     * @param data the data to send as the message
     */
    public void forwardMessage(String servers, String subchannel, ByteArrayOutputStream data);

    /**
     * Send message to a player on another server.
     *
     * @param player the player to send the message to
     * @param message the message to send to the player
     */
    public void sendMessage(String player, String message);

    /**
     * Connect a player to another server.
     *
     * @param player the player to send to another server
     * @param server the server to send them to
     */
    public void connect(String player, String server);

    /**
     * Gets the player count of another server.
     *
     * @param server the server to get the count for
     */
    public void getPlayerCount(String server);

    /**
     * Gets the player list on another server.
     *
     * @param server the server to get the playerlist for
     */
    public void getPlayerList(String server);

    /**
     * Gets the real ip of a player.
     *
     * @param player the player to get the ip of
     */
    public void getIP(Player player);

}
