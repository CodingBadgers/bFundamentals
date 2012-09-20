package uk.codingbadgers.bcreative.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import uk.codingbadgers.bcreative.bCreative;
import uk.codingbadgers.bcreative.containers.GamemodePlayer;
import uk.codingbadgers.bcreative.containers.GamemodeWorld;

public class BlockListener implements Listener{

	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockBreakEvent event) {
		GamemodePlayer player = bCreative.getPlayerManager().getPlayer(event.getPlayer().getName());
	
		if (player == null) {
			bCreative.getInstance().debugConsole("Player " + event.getPlayer().getName() + " is not in the array");
			return;
		}
		
		if (!player.isMonitor())
			return;
		
		GamemodeWorld world = player.getWorld();
		
		if (!world.isBreakBlackListed(event.getBlock()))
			return;
		
		bCreative.getInstance().debugConsole(player.getPlayer().getName() + " has tryed to break a blacklisted block");
		
		if (player.hasPermission("bcreative.blacklist.break." + world.getWorld().getName(), false))
			return;
		
		player.sendMessage("Sorry that block is blacklisted in this world");
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event) {
		GamemodePlayer player = bCreative.getPlayerManager().getPlayer(event.getPlayer().getName());
	
		if (player == null) {
			bCreative.getInstance().debugConsole("Player " + event.getPlayer().getName() + " is not in the array");
			return;
		}
		
		if (player.isMonitor())
			return;
		
		GamemodeWorld world = player.getWorld();
		
		if (!world.isPlaceBlackListed(event.getBlock()))
			return;
		
		bCreative.getInstance().debugConsole(player.getPlayer().getName() + " has tryed to place a blacklisted block");
		
		if (player.hasPermission("bcreative.blacklist.place." + world.getWorld().getName(), false))
			return;
		
		player.sendMessage("Sorry that block is blacklisted in this world");
		event.setCancelled(true);
	}
}
