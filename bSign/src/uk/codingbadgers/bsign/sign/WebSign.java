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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import uk.codingbadgers.bsign.bSignModule;


public class WebSign extends Sign {

	public WebSign(OfflinePlayer owner, Location signLocation) {
		super(owner, signLocation);
	}

	@Override
	public boolean init(String context) {
		
		if (!context.contains("tinyurl")) {
			try {
				URL urlRequest = new URL("http://tinyurl.com/api-create.php?url=" + context);
				BufferedReader in = new BufferedReader(new InputStreamReader(urlRequest.openStream()));
				m_context = in.readLine();
				in.close();
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		
		m_context = context;
		return true;
	}

	@Override
	public void interact(Player player) {
		
		// see if they have permissions to use signs
		if (!bSignModule.hasPermission(player, "bfundamental.bsign.use.web"))
			return;
		
		bSignModule.sendMessage("bSign", player, "Please click " + ChatColor.AQUA + m_context);
	}

	@Override
	public String getType() {
		return "web";
	}

}
