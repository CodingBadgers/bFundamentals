/**
 * bHelpful 1.2-SNAPSHOT
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
package uk.codingbadgers.bHelpful.commands;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.config.ConfigFile;
import uk.codingbadgers.bFundamentals.config.annotation.Element;
import uk.codingbadgers.bHelpful.Output;
import uk.codingbadgers.bHelpful.bHelpful;

public class MaintenanceCommand extends ConfigCommand {

	public MaintenanceCommand() {
		super("motd", "/motd", false);
	}

	@Override
	protected void handleCommand(CommandSender sender, String label, String[] args) {

		// maintenance cmd
        if (label.equalsIgnoreCase("maintenance") || label.equalsIgnoreCase("mm")) {
        	if (!bHelpful.hasPermission(sender, "bhelpful.maintenance")) {
        		Output.player(sender, "[bhelpful]", "You do not have permission to do this");
    			return;
    		}

        	if (args.length > 1) {
        		Output.playerWarning(sender, "/maintenance [staff]");
        		return;
        	}

        	if (args.length == 0 || args[0].equalsIgnoreCase("normal")) {
        		if (Maintenance.NORMAL_MAINTENANCE) {
        			Maintenance.NORMAL_MAINTENANCE = false;
        			Output.player(sender, "[bHelpful] ", "Maintenance mode disabled");
        			Output.server("[bHelpful] ", "Maintenance Mode has Been Disabled. Thankyou for your paitence");
        		} else {
        			Maintenance.NORMAL_MAINTENANCE = true;
        			Output.player(sender, "[bHelpful]", "Maintenance mode enabled");
        			Output.server("[bHelpful] ", "Maintenance Mode has Been Enabled. The server may lag. Please bare with us");
        		}
        		return;
        	}

        	if (args[0].equalsIgnoreCase("staff")) {
        		if (Maintenance.STAFF_MAINTENANCE) {
        			Maintenance.STAFF_MAINTENANCE = false;
        			Output.player(sender, "[bHelpful] ", "Staff maintenance mode disabled");

        			Player[] oPlayer = bHelpful.PLUGIN.getServer().getOnlinePlayers();

        			for (int i = 0; i<oPlayer.length; i++) {
        				if (bHelpful.hasPermission(oPlayer[i], "bhelpful.staff")) {
        					Output.player(oPlayer[i], "[bHelpful] ", "Staff maintenance mode disabled");
        				}
        			}

        		} else {
        			Maintenance.STAFF_MAINTENANCE = true;
        			Output.player(sender, "[bHelpful] ", "Staff maintenance mode enabled");

        			Player[] oPlayer = bHelpful.PLUGIN.getServer().getOnlinePlayers();

        			for (int i = 0; i < oPlayer.length; i++) {
        				if (bHelpful.hasPermission(oPlayer[i], "bhelpful.staff")) {
        					Output.player(oPlayer[i], "[bHelpful] ", "Staff maintenance mode enabled");
        				} else {
        					oPlayer[i].kickPlayer("Maintenance Enabled");
        				}
        			}
        		}
        		return;
        	}
        }

		return;
	}

	@Override
	protected void loadConfig() throws IOException {
		bHelpful.MODULE.registerConfig(Maintenance.class);
	}
	
	public static class Maintenance implements ConfigFile {		
		@Element("normal")
		public static boolean NORMAL_MAINTENANCE = false;
		@Element("staff")
		public static boolean STAFF_MAINTENANCE = false;
	}

}
