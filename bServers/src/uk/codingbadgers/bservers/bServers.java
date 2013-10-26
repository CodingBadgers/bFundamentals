/**
 * bGui 1.2-SNAPSHOT
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
package uk.codingbadgers.bservers;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

public class bServers extends Module implements PluginMessageListener {

    @Override
    public void onEnable() {
        Bukkit.getMessenger().registerIncomingPluginChannel(bFundamentals.getInstance(), "BungeeCord", this);
        bFundamentals.getBungeeMessenger().sendBungeeCommand("GetServers"); // get all servers to register commands for
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
            String subchannel = in.readUTF();
            
            if (subchannel.equals("GetServers")) {
                String[] serverList = in.readUTF().split(", ");
                
                for (String string : serverList) {
                    log(Level.INFO, "Registering command for " + string);
                    
                    ModuleCommand cmd = new ServerCommand(string);
                    registerCommand(cmd);
                }
            } 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
