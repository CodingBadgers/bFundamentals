package uk.codingbadgers.bsocks.web;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import uk.codingbadgers.bsocks.bSocksModule;

import com.btbb.figadmin.events.BanEvent;
import com.btbb.figadmin.events.IpBanEvent;
import com.btbb.figadmin.events.TempBanEvent;
import com.btbb.figadmin.events.UnbanEvent;
import com.btbb.figadmin.events.WarnEvent;

@SuppressWarnings("unused")
public class AdminListener implements Listener {

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
}
