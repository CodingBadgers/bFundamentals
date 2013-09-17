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
		
		if (args.length == 0 || args.length > 2) {
			// show command help
			return true;
		}
		
		if (args[0].equalsIgnoreCase("list") && args.length == 1) {
			m_module.listPets(player);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("release") && args.length == 2) {
			
			return true;
		}
		
		return true;		
	}
	
}
