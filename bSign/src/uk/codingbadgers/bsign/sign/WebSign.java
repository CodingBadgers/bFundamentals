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
