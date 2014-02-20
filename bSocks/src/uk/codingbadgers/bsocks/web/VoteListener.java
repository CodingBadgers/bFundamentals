/**
 * bSocks 1.2-SNAPSHOT
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
package uk.codingbadgers.bsocks.web;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import uk.codingbadgers.bsocks.bSocksModule;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class VoteListener implements Listener{

	@EventHandler
	public void onVote(VotifierEvent event) {
		Vote vote = event.getVote();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("user", vote.getUsername());
		params.put("time", vote.getTimeStamp());
		params.put("address", vote.getAddress());
		params.put("service", vote.getServiceName());
		
		try {
			WebHandler postHandler = bSocksModule.getInstance().getPostHandler("vote.php");
			postHandler.put(params);
			postHandler.start();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
	}
}
