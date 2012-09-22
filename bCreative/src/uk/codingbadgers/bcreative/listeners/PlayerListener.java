package uk.codingbadgers.bcreative.listeners;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import uk.codingbadgers.bcreative.bCreative;
import uk.codingbadgers.bcreative.containers.GamemodePlayer;
import uk.codingbadgers.bcreative.containers.GamemodeWorld;

public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerGamemodeChange(PlayerGameModeChangeEvent event) {
		Player player = event.getPlayer();
		
		GamemodeWorld world = bCreative.getWorldManager().getWorld(player.getWorld().getName());
		GameMode newGamemode = event.getNewGameMode();
		
		if (world.isGamemodeEnabled(newGamemode))
			return;
		
		event.setCancelled(true);
		bCreative.sendMessage(bCreative.NAME, player, "Sorry that gamemode is not enabled in this world");
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDrop(PlayerDropItemEvent event) {
		GamemodePlayer player = bCreative.getPlayerManager().getPlayer(event.getPlayer().getName());
		
		if (player == null) {
			bCreative.getInstance().debugConsole("Player " + event.getPlayer().getName() + " is not in the array");
			return;
		}
		
		if (!player.isMonitor())
			return;
		
		if (player.hasPermission("bcreative.drop." + player.getWorld().getWorld().getName(), false))
			return;
		
		event.setCancelled(true);
		player.sendMessage("Sorry you are not aloud to do that");
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		GamemodePlayer player = bCreative.getPlayerManager().getPlayer(event.getPlayer().getName());
		
		if (player == null) {
			bCreative.getInstance().debugConsole("Player " + event.getPlayer().getName() + " is not in the array");
			return;
		}
		
		if (!player.isMonitor())
			return;
		
		GamemodeWorld world = player.getWorld();
		
		if (isRedstone(event.getClickedBlock())) {
			String permission = "bcreative.redstone." + event.getClickedBlock().toString().toLowerCase().replaceAll("_", "") + "." + player.getWorld().getWorld().getName();
			bCreative.getInstance().debugConsole(permission);
			
			if (player.hasPermission(permission, false))
				return;
			
			event.setCancelled(true);
			player.sendMessage("Sorry you are not aloud to do that");
			return;
		}
				
		if (bCreative.getConfigmanager().isMonitor(world, event.getAction())) {		
			String permission = "bcreative.interact." + event.getAction().toString().toLowerCase().replaceAll("_", "") + "." + player.getWorld().getWorld().getName();
			bCreative.getInstance().debugConsole(permission);
			
			if (player.hasPermission(permission, false))
				return;
			
			event.setCancelled(true);
			player.sendMessage("Sorry you are not aloud to do that");
			return;
		}
	}

	private boolean isRedstone(Block clickedBlock) {
		switch (clickedBlock.getType().getId()) {
			case 69:
			case 70:
			case 72:
			case 75:
			case 85:
			case 132:
			case 134:
				return true;
			default:
				return false;
		}
	}
}
