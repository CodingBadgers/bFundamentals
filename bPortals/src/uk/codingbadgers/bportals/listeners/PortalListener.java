package uk.codingbadgers.bportals.listeners;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import uk.codingbadgers.bportals.Portal;
import uk.codingbadgers.bportals.PortalManager;
import uk.codingbadgers.bportals.bPortals;

public class PortalListener implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onEntityPortal(EntityPortalEvent event) {
		bPortals.getInstance().debugConsole("Entity portal event");
		Location dest = handlePortalEvent(event.getEntity(), event.getFrom(), event.getTo());
		
		if (dest != null) {
			event.setTo(dest);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerPortal(PlayerPortalEvent event) {
		bPortals.getInstance().debugConsole("Player portal event");
		Location dest = handlePortalEvent(event.getPlayer(), event.getFrom(), event.getTo());
		
		if (dest != null) {
			event.setTo(dest);
		}
	}

	public Location handlePortalEvent(Entity entity, Location from, Location to) {
		bPortals.getInstance().debugConsole(entity.getType() + "");
		
		if (!PortalManager.getInstance().isAllowedEntityType(entity.getType())) {
			return null;
		}

		Portal portal = PortalManager.getInstance().getPortalFromLocation(from);

		if (portal == null) {
			bPortals.getInstance().debugConsole("No portal at " + from);
			return null;
		}

		Location dest = portal.getTeleportLocation();

		if (dest == null) {
			bPortals.getInstance().getLogger().log(Level.INFO, "Portal " + portal.getId() + " does not have a destination set.");
			if (entity instanceof Player) {
				((Player) entity).sendMessage(ChatColor.DARK_PURPLE + "[bPortals] " + ChatColor.RESET + "This portal has no destination set.");
			}
			return portal.getExitLocation();
		}

		bPortals.getInstance().debugConsole(portal.getId());
		bPortals.getInstance().debugConsole(portal.getExitLocation().toString());
		bPortals.getInstance().debugConsole(portal.getTeleportLocation().toString());

		return dest;
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockPhysics(BlockPhysicsEvent event) {
		if (event.getChangedType() == Material.PORTAL || event.getBlock().getType() == Material.PORTAL) {
			if (PortalManager.getInstance().isPortal(event.getBlock().getLocation())) {
				event.setCancelled(true);
			}
		}
	}
}
