package uk.codingbadgers.bcreative.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

import uk.codingbadgers.bcreative.bCreative;
import uk.codingbadgers.bcreative.containers.GamemodeWorld;

public class TeleportListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		
		if (event.getTo().getWorld() == event.getFrom().getWorld())
			return;
		
		GamemodeWorld world = bCreative.getWorldManager().getWorld(event.getTo().getWorld().getName());
		GameMode defGm = world.getDefaultGamemode();
		
		if (event.getPlayer().getGameMode() == defGm)
			return;
		
		event.getPlayer().setGameMode(defGm);
	}
}
