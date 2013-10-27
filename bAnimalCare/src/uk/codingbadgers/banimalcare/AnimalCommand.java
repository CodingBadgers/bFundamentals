/**
 * bAnimalCare 1.2-SNAPSHOT
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
package uk.codingbadgers.banimalcare;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

public class AnimalCommand extends ModuleCommand {
	
	final private bAnimalCare m_module;

	public AnimalCommand(Module module) {
		super("pet", "/pet list | /pet release <id>");
		m_module = (bAnimalCare) module;
	}

	/**
	 * Called when the 'pet' command is executed.
	 */
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		
		if (!(sender instanceof Player))
			return true;
		
		Player player = (Player)sender;
		
		if (args.length == 0) {
			// show command help
			Module.sendMessage(m_module.getName(), player, "/pet list");
			Module.sendMessage(m_module.getName(), player, "/pet release <id>");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("list")) {
			if (args.length == 1) {
				m_module.listPets(player, player);
			}
			else if (args.length == 2) {
				if (!Module.hasPermission(player, "bAnimalCare.list.other")) {
					Module.sendMessage(m_module.getName(), player, "You don't have permission to list other players pets...");
					return true;
				}
				String playerName = args[1];
				OfflinePlayer owner = Bukkit.getOfflinePlayer(playerName);
				m_module.listPets(player, owner);
			}
			else {
				Module.sendMessage(m_module.getName(), player, "Invalid command usage: /pet list [player]");
			}
			return true;
		}
		
		if (args[0].equalsIgnoreCase("release")) {
			if (args.length == 2) {
				m_module.releasePet(player, args[1]);
			}
			if (args.length == 3) {
				OfflinePlayer owner = Bukkit.getOfflinePlayer(args[1]);
				if (!owner.hasPlayedBefore()) {
					Module.sendMessage(m_module.getName(), player, "Invalid command usage: /pet release <owner> <id>");
					return true;
				}
				
				m_module.releasePet(owner, args[2]);
			}
			else {
				Module.sendMessage(m_module.getName(), player, "Invalid command usage: /pet release <id>");
			}
			return true;
		}
		
		if (args[0].equalsIgnoreCase("tp")) {
			
			if (!Module.hasPermission(player, "bAnimalCare.tp.self")) {
				Module.sendMessage(m_module.getName(), player, "You don't have permission to teleport to pets...");
				return true;
			}
			
			if (args.length == 2) {
				m_module.tpToPet(player, args[1], player);
			}
			else if (args.length == 3) {
				if (!Module.hasPermission(player, "bAnimalCare.tp.other")) {
					Module.sendMessage(m_module.getName(), player, "You don't have permission to teleport to pets...");
					return true;
				}
				String playerName = args[2];
				OfflinePlayer owner = Bukkit.getOfflinePlayer(playerName);
				m_module.tpToPet(player, args[1], owner);
			}
			else {
				Module.sendMessage(m_module.getName(), player, "Invalid command usage: /pet tp <id> [player]");
			}
			return true;
		}
		
		Module.sendMessage(m_module.getName(), player, "Unknown command");
		
		return true;		
	}
	
}
