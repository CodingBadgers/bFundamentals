package uk.codingbadgers.bsign.sign;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
		String command = m_context.replaceAll("<<player>>", player.getName());
		
		bSignModule.PLUGIN.getServer().dispatchCommand(player, command);
	}
	
	@Override
	public String getType() {
		return "command";
	}
	
}
