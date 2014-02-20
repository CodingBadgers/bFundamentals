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

/*
import com.btbb.figadmin.events.BanEvent;
import com.btbb.figadmin.events.IpBanEvent;
import com.btbb.figadmin.events.TempBanEvent;
import com.btbb.figadmin.events.UnbanEvent;
import com.btbb.figadmin.events.WarnEvent;
*/

@SuppressWarnings("unused")
public class AdminListener implements Listener {
/*
	private static final String BAN = "0";
	private static final String BAN_IP = "1";
	private static final String WARNING = "2";
	private static final String OFFLINE_WARNING = "3";
	private static final String TEMP_BAN = "4";
	private static final String UN_BAN = "5";
	
	@EventHandler 
	public void onPlayerBan(BanEvent event) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("victim", event.getName());
		params.put("reason", event.getReason());
		params.put("admin", event.getAdmin());
		params.put("type", BAN);
		
		try {
			WebHandler postHandler = bSocksModule.getPostHandler("admin.php");
			postHandler.put(params);
			postHandler.start();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
	}
	
	@EventHandler 
	public void onPlayerBan(IpBanEvent event) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("victim", event.getName());
		params.put("reason", event.getReason());
		params.put("admin", event.getAdmin());
		params.put("type", BAN_IP);
		
		try {
			WebHandler postHandler = bSocksModule.getPostHandler("admin.php");
			postHandler.put(params);
			postHandler.start();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
	}

	@EventHandler 
	public void onPlayerUnban(UnbanEvent event) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("victim", event.getPlayer());
		params.put("admin", event.getAdmin());
		params.put("type", UN_BAN);
		
		try {
			WebHandler postHandler = bSocksModule.getPostHandler("admin.php");
			postHandler.put(params);
			postHandler.start();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
	}

	@EventHandler 
	public void onPlayerTempban(TempBanEvent event) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("victim", event.getName());
		params.put("reason", event.getReason());
		params.put("admin", event.getAdmin());
		params.put("endTime", String.valueOf(event.getEndTime()));
		params.put("type", TEMP_BAN);
		
		try {
			WebHandler postHandler = bSocksModule.getPostHandler("admin.php");
			postHandler.put(params);
			postHandler.start();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
	}

	@EventHandler 
	public void onPlayerWarn(WarnEvent event) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("victim", event.getName());
		params.put("reason", event.getReason());
		params.put("admin", event.getAdmin());
		params.put("type", WARNING);
		
		try {
			WebHandler postHandler = bSocksModule.getPostHandler("admin.php");
			postHandler.put(params);
			postHandler.start();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
	}
*/
}
