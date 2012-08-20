package uk.codingbadgers.bsign.sign;

import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import uk.codingbadgers.bsign.bSignModule;


public class InfoSign extends Sign {

	public InfoSign(OfflinePlayer owner, Location signLocation) {
		super(owner, signLocation);
	}

	@Override
	public boolean init(String context) {
		m_context = replaceChatColours(context);
		
		return true;
	}

	/**
	 * Replace colour code symbols with chat colour representations.
	 * 
	 * @param message The original message which may contain colour codes to replace
	 * @return A message with colour codes inserted
	 */
	private String replaceChatColours(String message) {
		String buffer = message;
		
		// we'll replace the &<c> as we go, so looop through all of them
		while(buffer.indexOf("&") != -1) {
			
			// get the colour code first as a string, then convert the hex based char into an integer
			String code = buffer.substring(buffer.indexOf("&") + 1, buffer.indexOf("&") + 2);
			
			// get all the text up to the first '&'
			// get the chat colour from our colour code
			// skip the &<c> and reattach the rest of the string
			buffer = buffer.substring(0, buffer.indexOf("&")) +  ChatColor.getByChar(code) + buffer.substring(buffer.indexOf("&") + 2);					
		}
		
		return buffer;
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
