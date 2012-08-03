/*
 * 
 */
package uk.codingbadgers.bHelpful;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Maintenance {

	private static boolean m_normal = false;
	private static boolean m_staff = false;

	/**
	 * Sets the maintenance.
	 *
	 * @param maintenance the new maintenance
	 */
	public static void setMaintenance(boolean maintenance) {
		m_normal = maintenance;
		Configuration.config.set("maintenance.state.normal", maintenance);
		Configuration.NORMAL_STATE = maintenance;
	}

	/**
	 * Checks if is maintenance.
	 *
	 * @return true, if is maintenance
	 */
	public static boolean isMaintenance() {
		return m_normal;
	}

	/**
	 * Sets the staff maintenance.
	 *
	 * @param maintenance the new staff maintenance
	 */
	public static void setStaffMaintenance(boolean maintenance) {
		m_staff = maintenance;
		Configuration.config.set("maintenance.state.staff", maintenance);
		Configuration.STAFF_STATE = maintenance;
	}

	/**
	 * Checks if is staff maintenance.
	 *
	 * @return true, if is staff maintenance
	 */
	public static boolean isStaffMaintenance() {
		return m_staff;
	}

	/**
	 * Maintenance command.
	 *
	 * @param sender the sender
	 * @param commandLabel the command label
	 * @param args the args
	 * @return true, if successful
	 */
	public static boolean maintenanceCommand(CommandSender sender, String commandLabel, String[] args) {
		Player player = (Player) sender;

		// maintenance cmd
        if (commandLabel.equalsIgnoreCase("maintenance") || commandLabel.equalsIgnoreCase("mm")) {
        	if (!bHelpful.hasPermission(player, "bhelpful.maintenance")) {
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
        			Output.server("[bHelpful] ", Configuration.NORMAL_DISABLED);
        		} else {
        			Maintenance.setMaintenance(true);
        			Output.player(player, "[bHelpful]", "Maintenance mode enabled");
        			Output.server("[bHelpful] ", Configuration.NORMAL_ENABLED);
        		}
        		return false;
        	}

        	if (args[0].equalsIgnoreCase("staff")) {
        		if (Maintenance.isStaffMaintenance()) {
        			Maintenance.setStaffMaintenance(false);
        			Output.player(player, "[bHelpful] ", "Staff maintenance mode disabled");

        			Player[] oPlayer = bHelpful.PLUGIN.getServer().getOnlinePlayers();

        			for (int i = 0; i<oPlayer.length; i++) {
        				if (bHelpful.hasPermission(oPlayer[i], "bhelpful.staff")) {
        					Output.player(oPlayer[i], "[bHelpful] ", Configuration.STAFF_DISABLED);
        				}
        			}

        		} else {
        			Maintenance.setStaffMaintenance(true);
        			Output.player(player, "[bHelpful] ", "Staff maintenance mode enabled");

        			Player[] oPlayer = bHelpful.PLUGIN.getServer().getOnlinePlayers();

        			for (int i = 0; i<oPlayer.length; i++) {
        				if (bHelpful.hasPermission(oPlayer[i], "bhelpful.staff")) {
        					Output.player(oPlayer[i], "[bHelpful] ", Configuration.STAFF_ENABLED);
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
