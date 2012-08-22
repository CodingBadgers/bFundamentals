package uk.codingbadgers.bportalshelper.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
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
		
		final Player player = event.getPlayer();
		if (player == null)
			return;
		
		ItemStack itemInHand = player.getItemInHand();
		if (itemInHand.getType() != Material.BOW)
			return;
		
		PortalPlayer pPlayer = bPortalsHelper.getOrCreatePlayer(player);
		if (pPlayer == null)
			return;
		
		WorldEditPlugin we = bPortalsHelper.getWorldEdit();
		if (we == null)
			return;
		
		Selection portalSelection = we.getSelection(player);		
		pPlayer.addPortal(portalSelection);
		
	}

}
