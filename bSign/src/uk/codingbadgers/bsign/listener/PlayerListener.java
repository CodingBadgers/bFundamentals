package uk.codingbadgers.bsign.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import uk.codingbadgers.bsign.bSignModule;
import uk.codingbadgers.bsign.sign.Sign;

/**
 * The listener interface for receiving player events.
 * The class that is interested in processing a player
 * event implements this interface. When
 * the player event occurs, that object's appropriate
 * method is invoked.
 */
public class PlayerListener implements Listener {
	
	/**
	 * On player interact. Used to call the interact command on a bSign
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {

		// we only want right click events
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		// see if we are dealing with a sign
		Block block = event.getClickedBlock();
		if (!(block.getType() == Material.SIGN || block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST))
			return;
		
		// try to find the bsign from all our stored signs
		Sign contextSign = null;
		for (Sign sign : bSignModule.SIGNS) {
			if (sign.getLocation().equals(block.getLocation())) {
				contextSign = sign;
				break;
			}
		}
		
		// its a sign, but not a bsign
		if (contextSign == null)
			return;
				
		contextSign.interact(event.getPlayer());		
	}
	
	/**
	 * On chat interact. Used to intercept the next chat message a bSign creator
	 * says, so that we can set the context of a bSign.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onChatInteract(PlayerChatEvent event) {
		
		// get the person that is speaking
		Player chatter = (Player)event.getPlayer();
		
		// spin through all signs, to find a sign that does not have a context
		// and the creator is the person who is talking
		Sign contextSign = null;
		for (Sign sign : bSignModule.SIGNS) {
			if (sign.getContext() != null)
				continue;
			
			if (!sign.getCreator().getName().equalsIgnoreCase(chatter.getName()))
				continue;
			
			contextSign = sign;
			break;
		}
		
		// if we didn't find a sign, leave now, and let the chat event continue
		if (contextSign == null)
			return;
		
		// found a sign, so get the message, and cancel the event.
		String context = event.getMessage();
		event.setCancelled(true);
		
		// if it is a cancel command, destroy the bsign and break the actual sign
		if (context.equalsIgnoreCase("bsigncancel"))
		{
			contextSign.getLocation().getBlock().breakNaturally();
			bSignModule.SIGNS.remove(contextSign);
			return;
		}
		
		// Attempt to initialize the sign based upon the message.
		if (contextSign.init(event.getMessage())) {
			bSignModule.sendMessage("bSign", chatter, "Sign context succesfully set");
			
			String type = contextSign.getType(); 
			String location = contextSign.getLocation().getX() + "," + contextSign.getLocation().getY() + "," + contextSign.getLocation().getZ() + "," + contextSign.getLocation().getWorld().getName();
			
			String addSign = "INSERT INTO bSign " +
					"VALUES ('" +
					type + "', '" +
					contextSign.getContext() + "', '" +
					contextSign.getCreator().getName() + "', '" +
					location +
					"')";
			bSignModule.DATABASE.Query(addSign);
			return;
		}
		
		bSignModule.sendMessage("bSign", chatter, "The message you entered is invalid, please try again, so enter 'bsigncancel'.");
		
	}

	
}
