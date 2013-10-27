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

public interface BungeeMessenger {

    public boolean sendRawMessage(byte[] message);
    
    public boolean sendRawMessage(byte[] message, boolean queue);

    public void sendRawPlayerMessage(Player player, byte[] message);

    public void sendPlayerCommand(String command, Player player, String... args);
    
    public void sendBungeeCommand(String command, String... args);

    public void sendQueuedCommands();

    public void forwardMessage(String servers, String subchannel, ByteArrayOutputStream data);

    public void sendMessage(String player, String message);

    public void connect(String player, String server);

    public void getPlayerCount(String server);

    public void getPlayerList(String server);

    public void getIP(Player player);

}
