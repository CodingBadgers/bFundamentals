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
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.bFundamentals;

public class SimpleBungeeMessenger implements BungeeMessenger {

    private List<byte[]> queue = null;
    
    public SimpleBungeeMessenger() {
        queue = new ArrayList<byte[]>();
        Bukkit.getPluginManager().registerEvents(new BungeePlayerListener(this), bFundamentals.getInstance());
    }
    
    @Override
    public boolean sendRawMessage(byte[] message) {
        return sendRawMessage(message, true);
    }

    @Override
    public boolean sendRawMessage(byte[] message, boolean shouldQueue) {
        if (Bukkit.getOnlinePlayers().length > 0) {
            Player p = Bukkit.getOnlinePlayers()[0];
            p.sendPluginMessage(bFundamentals.getInstance(), "BungeeCord", message);
            return true;
        } else if (shouldQueue){
            queue.add(message);
        }
        
        return false;
    }
    
    @Override
    public void sendRawPlayerMessage(Player player, byte[] message) {
        if (player.isOnline()) {
            player.sendPluginMessage(bFundamentals.getInstance(), "BungeeCord", message);
        }
    }

    @Override
    public void sendPlayerCommand(String command, Player player, String... args) {
        byte[] message = new byte[0];
        
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            
            out.writeUTF(command);
            for (String arg : args) {
                out.writeUTF(arg);
            }
            
            message = b.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
        sendRawPlayerMessage(player, message);
    }
    
    @Override
    public void sendBungeeCommand(String command, String... args) {
        byte[] message = new byte[0];
        
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            
            out.writeUTF(command);
            for (String arg : args) {
                out.writeUTF(arg);
            }
            
            message = b.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
        sendRawMessage(message);
    }
    
    @Override
    public void sendQueuedCommands() {
        List<byte[]> cache = new ArrayList<byte[]>(queue);
        
        for (byte[] message : cache) {
            if (sendRawMessage(message, false)) {
                queue.remove(message);
            }
            
        }
    }
    
    @Override
    public void forwardMessage(String servers, String subchannel, ByteArrayOutputStream data) {
        byte[] message = new byte[0];
        
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("FORWARD");
            out.writeUTF(servers);
            out.writeUTF(subchannel);
            
            out.writeShort(data.toByteArray().length);
            out.write(data.toByteArray());
            
            message = b.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
        sendRawMessage(message);
    }
    
    @Override
    public void sendMessage(String player, String message) {
        sendBungeeCommand("Message", player, message);
    }
    
    @Override
    public void connect(String player, String server) {
        sendBungeeCommand("ConnectOther", player, server);
    }
    
    @Override
    public void getPlayerCount(String server) {
        sendBungeeCommand("PlayerCount", server);
    }
    
    @Override
    public void getPlayerList(String server) {
        sendBungeeCommand("PlayerList", server);
    }
    
    @Override
    public void getIP(Player player) {
        sendPlayerCommand("IP", player);
    }
}
