package uk.codingbadgers.bportalshelper.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldedit.bukkit.selections.Selection;

import uk.codingbadgers.bportalshelper.PortalPlayer;
import uk.codingbadgers.bportalshelper.bPortalsHelper;

public class PlayerListener implements Listener {
	
	/**
	 * Handle a player interact event
	 *
	 * @param event The player interact event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		
		// get the player who is interacting
		final Player player = event.getPlayer();
		if (player == null)
			return;
		
		// see if they have permission
		if (!bPortalsHelper.hasPermission(player, "bportalshelper.use"))
			return;
		
		// only handle right clicks
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		// see if they are using a bow
		ItemStack itemInHand = player.getItemInHand();
		if (itemInHand.getType() != Material.SPIDER_EYE)
			return;
		
		// get or create the portal player
		PortalPlayer pPlayer = bPortalsHelper.getOrCreatePlayer(player);
		if (pPlayer == null)
			return;
		
		// get the world edit selection
		Selection portalSelection = bPortalsHelper.getWorldEdit().getSelection(player);
		
		pPlayer.addPortal(portalSelection);
		
	}

}
