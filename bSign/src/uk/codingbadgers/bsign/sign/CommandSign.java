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
package uk.codingbadgers.bsign.sign;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import uk.codingbadgers.bsign.bSignModule;


public class CommandSign extends Sign {

	public CommandSign(OfflinePlayer owner, Location signLocation) {
		super(owner, signLocation);
	}

	@Override
	public boolean init(String context) {
		m_context = context;
		return true;
	}

	@Override
	public void interact(Player player) {
		
		// see if they have permissions to use signs
		if (!bSignModule.hasPermission(player, "bfundamental.bsign.use.command"))
			return;
		
		// replace macros
		String command = replaceMacros(player, getContext());
		
		bSignModule.MODULE.debugConsole("Running command " + command + " for " + player.getName());
		
		PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, command);
		//Bukkit.getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled()) {
			bSignModule.MODULE.debugConsole("Plugin canceled event");
			return;
		}

		bSignModule.MODULE.debugConsole("Running command " + event.getMessage() + " for " + player.getName());
		
		Bukkit.getServer().dispatchCommand(event.getPlayer(), command);
	}
	
	@Override
	public String getType() {
		return "command";
	}
	
}
