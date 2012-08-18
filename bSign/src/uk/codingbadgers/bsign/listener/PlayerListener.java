package uk.codingbadgers.bsign.listener;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
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
	
	private ArrayList<Location> m_visitedLocations = new ArrayList<Location>();
	
	/**
	 * Handles when a player right clicks a bSign
	 * @param event The interact event to handle with a sign interaction
	 */
	private void handleSignInteract(final Block block, final Player player)
	{		
		// try to find the bsign from all our stored signs
		Sign contextSign = null;
		for (Sign sign : bSignModule.SIGNS) {
			
			if (sign == null)
				continue;
			
			if (sign.getLocation().equals(block.getLocation())) {
				contextSign = sign;
				break;
			}
			
		}
		
		// its a sign, but not a bsign
		if (contextSign == null)
			return;
				
		contextSign.interact(player);	
	}
	
	/**
	 * See if a block is valid restone
	 * @param block THe block to validate
	 * @return true if the block is redstone, false otherwise
	 */
	private boolean isRedStone(Block block) {
		if (block.getType() == Material.REDSTONE_WIRE)
			return true;
		return false;
	}
	
	/**
	 * See if a block is a sign
	 * @param block The block to validate
	 * @return true if the block is a sign, false otherwise
	 */
	private boolean findSignBlock(Block block) {
		if (block.getType() == Material.SIGN || block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)
			return true;		
		return false;
	}
	
	/**
	 * Find connected sign.
	 *
	 * @param startBlock the start block
	 * @return the block
	 */
	private Block findConnectedSign(Block startBlock)
	{
		Location right = startBlock.getLocation();
		Location left = startBlock.getLocation();
		Location up = startBlock.getLocation();
		Location down = startBlock.getLocation();
		
		if (findSignBlock(right.add(1.0, 0.0, 0.0).getBlock()))
			return right.getBlock();
		
		if (findSignBlock(left.add(-1.0, 0.0, 0.0).getBlock()))
			return left.getBlock();
		
		if (findSignBlock(up.add(0.0, 0.0, 1.0).getBlock()))
			return up.getBlock();
		
		if (findSignBlock(down.add(0.0, 0.0, -1.0).getBlock()))
			return down.getBlock();
		
		return null;
	}
	
	/**
	 * @param lastBlock
	 * @return
	 */
	private Block findConnectedRedstone(Block lastBlock) {
				
		for (int y = -1; y <= 1; ++y) {
			Location right = lastBlock.getLocation();
			Location left = lastBlock.getLocation();
			Location up = lastBlock.getLocation();
			Location down = lastBlock.getLocation();
			
			if (isRedStone(right.add(1.0, y, 0.0).getBlock())) {
				if (!m_visitedLocations.contains(right.getBlock().getLocation())) {
					return right.getBlock();
				}
			}
			
			if (isRedStone(left.add(-1.0, y, 0.0).getBlock())) {
				if (!m_visitedLocations.contains(left.getBlock().getLocation())) {
					return left.getBlock();
				}
			}
			
			if (isRedStone(up.add(0.0, y, 1.0).getBlock())) {
				if (!m_visitedLocations.contains(up.getBlock().getLocation())) {
					return up.getBlock();
				}
			}
			
			if (isRedStone(down.add(0.0, y, -1.0).getBlock())) {
				if (!m_visitedLocations.contains(down.getBlock().getLocation())) {
					return down.getBlock();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Handle redstone sign interaction
	 * @param event The interact event to handle with a sign interaction
	 */
	private void handleRedStoneInteract(PlayerInteractEvent event) 
	{
		if (!(event.getPlayer() instanceof Player))
			return;
		
		Block block = event.getClickedBlock();
		if (block.getType() != Material.STONE_PLATE)
			return;
			
		m_visitedLocations.clear();
		int powerLevel = 15;
		
		do {
			Block sign = null;
			if ((sign = findConnectedSign(block)) != null) {
				handleSignInteract(sign, event.getPlayer());
				return;
			}
			
			m_visitedLocations.add(block.getLocation());
			if ((block = findConnectedRedstone(block)) == null) {
				return;
			}
			powerLevel = powerLevel - 1;
		
		} while (powerLevel > 1);
		
	}
	
	/**
	 * On player interact. Used to call the interact command on a bSign
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {

		if (bSignModule.SIGNS == null)
			return;
				
		// If we right clicked, then handle the sign interaction
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			// see if we are dealing with a sign
			final Block block = event.getClickedBlock();
			if (findSignBlock(block)) {
				handleSignInteract(block, event.getPlayer());
				return;
			}
		}
		
		// Handle redstone events
		if (event.getAction() == Action.PHYSICAL) {
			handleRedStoneInteract(event);
			return;
		}
			
	}
	
	/**
	 * On chat interact. Used to intercept the next chat message a bSign creator
	 * says, so that we can set the context of a bSign.
	 *
	 * @param event the event
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onChatInteract(AsyncPlayerChatEvent event) {
		
		if (bSignModule.SIGNS == null)
			return;
		
		// get the person that is speaking
		Player chatter = (Player)event.getPlayer();
		if (chatter == null)
			return;
		
		// spin through all signs, to find a sign that does not have a context
		// and the creator is the person who is talking
		Sign contextSign = null;
		for (Sign sign : bSignModule.SIGNS) {
			if (sign == null)
				continue;
				
			if (sign.getContext() != null)
				continue;
			
			if (sign.getCreator() == null)
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
			bSignModule.sendMessage("bSign", chatter, bSignModule.MODULE.getLanguageValue("SIGN-CONTEXT-SET"));
			
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
		
		bSignModule.sendMessage("bSign", chatter, bSignModule.MODULE.getLanguageValue("INVALID-SIGN-CONTEXT"));
		
	}
}
