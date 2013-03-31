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
		String command = m_context.replaceAll("<<player>>", player.getName());
		
		
		PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, command);
		Bukkit.getServer().getPluginManager().callEvent(event);
		
		if (event.isCancelled())
			return;
		
		Bukkit.getServer().dispatchCommand(event.getPlayer(), event.getMessage());
	}
	
	@Override
	public String getType() {
		return "command";
	}
	
}
