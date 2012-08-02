package uk.codingbadgers.bHelpful;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Maintenance {

	private static boolean maintenanceNormal = false;
	private static boolean maintenanceStaff = false;

	// set the maintenance mode for normal players
	public static void setMaintenance(boolean maintenance) {
		maintenanceNormal = maintenance;
		Configuration.config.set("maintenance.state.normal", maintenance);
		Configuration.normalState = maintenance;
	}

	// gets if the server is under maintenance
	public static boolean isMaintenance() {
		return maintenanceNormal;
	}

	// set the maintenance more for staff
	public static void setStaffMaintenance(boolean maintenance) {
		maintenanceStaff = maintenance;
		Configuration.config.set("maintenance.state.staff", maintenance);
		Configuration.staffState = maintenance;
	}

	// gets if the server is under maintenance for staff
	public static boolean isStaffMaintenance() {
		return maintenanceStaff;
	}

	public static boolean maintenanceCommand(CommandSender sender, String commandLabel, String[] args) {
		Player player = (Player) sender;

		// maintenance cmd
        if (commandLabel.equalsIgnoreCase("maintenance") || commandLabel.equalsIgnoreCase("mm")) {
        	if (!bHelpful.hasPerms(player, "bhelpful.maintenance")) {
        		Output.player(player, "[bhelpful]", "You do not have permission to do this");
    			return true;
    		}

        	if (args.length > 1) {
        		Output.playerWarning(player, "/maintenance [staff]");
        		return true;
        	}

        	if (args.length == 0 || args[0].equalsIgnoreCase("normal")) {
        		if (Maintenance.isMaintenance()) {
        			Maintenance.setMaintenance(false);
        			Output.player(player, "[bHelpful] ", "Maintenance mode disabled");
        			Output.server("[bHelpful] ", Configuration.normalDisabled);
        		} else {
        			Maintenance.setMaintenance(true);
        			Output.player(player, "[bHelpful]", "Maintenance mode enabled");
        			Output.server("[bHelpful] ", Configuration.normalEnabled);
        		}
        		return false;
        	}

        	if (args[0].equalsIgnoreCase("staff")) {
        		if (Maintenance.isStaffMaintenance()) {
        			Maintenance.setStaffMaintenance(false);
        			Output.player(player, "[bHelpful] ", "Staff maintenance mode disabled");

        			Player[] oPlayer = bHelpful.m_plugin.getServer().getOnlinePlayers();

        			for (int i = 0; i<oPlayer.length; i++) {
        				if (bHelpful.hasPerms(oPlayer[i], "bhelpful.staff")) {
        					Output.player(oPlayer[i], "[bHelpful] ", Configuration.staffDisabled);
        				}
        			}

        		} else {
        			Maintenance.setStaffMaintenance(true);
        			Output.player(player, "[bHelpful] ", "Staff maintenance mode enabled");

        			Player[] oPlayer = bHelpful.m_plugin.getServer().getOnlinePlayers();

        			for (int i = 0; i<oPlayer.length; i++) {
        				if (bHelpful.hasPerms(oPlayer[i], "bhelpful.staff")) {
        					Output.player(oPlayer[i], "[bHelpful] ", Configuration.staffEnabled);
        				}
        				else
        					oPlayer[i].kickPlayer("Maintenance Enabled");
        			}
        		}
        		return false;
        	}
        }

		return false;
	}
}
