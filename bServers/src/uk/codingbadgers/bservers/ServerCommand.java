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
package uk.codingbadgers.bservers;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

public class ServerCommand extends ModuleCommand {

    private String server;

    public ServerCommand(String server) {
        super(server, "/" + server);
        this.server = server;
    }
    
    public ServerCommand(String server, List<String> list) {
        super(server, "/" + server);
        for (String string : list) {
        	addAliase(string);
        }
        this.server = server;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Module.sendMessage("bServers", sender, "This command can only be executed as a player");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("bServers.server." + server)) {
            Module.sendMessage("bServers", sender, "You do not have permission to go to " + server);
            return true;
        }
        
        bFundamentals.getBungeeMessenger().connect(sender.getName(), this.getLabel());
        return true;
    }
}
