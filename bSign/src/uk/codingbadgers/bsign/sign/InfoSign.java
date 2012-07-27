package uk.codingbadgers.bsign.sign;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import uk.codingbadgers.bsign.bSignModule;


public class InfoSign extends Sign {

	public InfoSign(OfflinePlayer owner, Location signLocation) {
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
		String output = m_context.replaceAll("<<player>>", player.getName());

		player.sendMessage(output);
	}
	
	@Override
	public String getType() {
		return "info";
	}
	
}
