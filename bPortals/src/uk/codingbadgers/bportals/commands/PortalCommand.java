/**
 * bPortals 1.2-SNAPSHOT
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
package uk.codingbadgers.bportals.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.regions.Region;

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bportals.bPortals;
import uk.codingbadgers.bportals.portal.Portal;
import static uk.codingbadgers.bportals.utils.LocationUtils.*;

/*
 * TODO Note this is a temporary command for testing and should not be released.
 */
public class PortalCommand extends ModuleCommand {

	public PortalCommand() {
		super("portal", "/portal <create/link/destination>");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command can only be executed as a player");
			return true;
		}
		
		Player player = (Player) sender;
		
		if (args.length < 1) {
			showHelp(sender);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("create")) {
			createPortal(player, args);
			return true;
		}

		if (args[0].equalsIgnoreCase("link")) {
			linkPortals(player, args);
			return true;
		}

		if (args[0].equalsIgnoreCase("list")) {
			listPortals(player, args);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("destination")) {
			setDestination(player, args);
			return true;
		}
		
		return false;
	}

	private void showHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_PURPLE + "--[-- bPortals --]--");
		sender.sendMessage(ChatColor.DARK_PURPLE + "create" + ChatColor.RESET + " - create a portal");
		sender.sendMessage(ChatColor.DARK_PURPLE + "link" + ChatColor.RESET + " - link a portal with another portal");
		sender.sendMessage(ChatColor.DARK_PURPLE + "list" + ChatColor.RESET + " - list all created portals");
		sender.sendMessage(ChatColor.DARK_PURPLE + "destination" + ChatColor.RESET + " - set the destination for a portal");
	}

	private void createPortal(Player sender, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(ChatColor.DARK_PURPLE + "/portal create <portal id>");
			return;
		}

		String id = args[1];
		
		if (bPortals.getPortalManager().portalExists(id)) {
			sender.sendMessage(ChatColor.DARK_PURPLE + "[bPortals] " + ChatColor.RESET + "Portal " + args[1] + " already exists");
			return;
		}
		
		try {
			LocalPlayer player = bPortals.getWorldEdit().wrapPlayer(sender);
			LocalSession localSession = bPortals.getWorldEdit().getWorldEdit().getSession(player);
			EditSession session = localSession.createEditSession(player);
			
			Region sel = localSession.getSelection(player.getWorld());
			
			bPortals.getInstance().debugConsole(sel.getMinimumPoint().toString());
			bPortals.getInstance().debugConsole(sel.getMaximumPoint().toString());
			
			bPortals.getPortalManager().createPortal(sel, id, sender);

			try {
				session.setBlocks(localSession.getSelection(player.getWorld()), new BaseBlock(90));
			} catch (MaxChangedBlocksException e) {
				sender.sendMessage(ChatColor.DARK_PURPLE + "[bPortals] " + ChatColor.RESET + "The portal is too big to be filled via worldedit, please fill it with portal manually");
			} 
			
			sender.sendMessage(ChatColor.DARK_PURPLE + "[bPortals] " + ChatColor.RESET + "Portal " + id + " created");
		} catch (IncompleteRegionException e) {
			sender.sendMessage(ChatColor.DARK_PURPLE + "[bPortals] " + ChatColor.RESET + e.getMessage());
			return;
		}
	}

	private void linkPortals(Player sender, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(ChatColor.DARK_PURPLE + "/portal create <portal 1> <portal 2>");
			return;
		}
		
		Portal portal1 = bPortals.getPortalManager().getPortalById(args[1]);
		Portal portal2 = bPortals.getPortalManager().getPortalById(args[2]);
		
		if (portal1 == null || portal2 == null) {	
			sender.sendMessage(ChatColor.DARK_PURPLE + "[bPortals] " + ChatColor.RESET + (portal1 == null ? args[1] : args[2]) + " does not exist.");
			return;
		}
		
		portal1.setTeleportLocation(portal2);
		portal2.setTeleportLocation(portal1);
		sender.sendMessage(ChatColor.DARK_PURPLE + "[bPortals] " + ChatColor.RESET + "Successfully linked " + args[1] + " with " + args[2]);
	}

	private void listPortals(Player sender, String[] args) {
		sender.sendMessage(ChatColor.DARK_PURPLE + "--[-- bPortals --]--");
		for (Portal portal : bPortals.getPortalManager().getPortals()) {
			sender.sendMessage(ChatColor.BLUE + portal.getId() + ChatColor.AQUA + " " + convertLocationToFloorVector(portal.getExitLocation()) + " " + (portal.hasTeleportLocation() ? convertLocationToFloorVector(portal.getTeleportLocation()) : ""));
		}
	}

	private void setDestination(Player sender, String[] args) {
		
	}
	

}
