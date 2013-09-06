/**
 * bFundamentalsBuild 1.2-SNAPSHOT
 * Copyright (C) 2013  CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.codingbadgers.bportalshelper.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldedit.bukkit.selections.Selection;

import uk.codingbadgers.bportalshelper.PortalPlayer;
import uk.codingbadgers.bportalshelper.bPortalsHelper;

public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if (player == null) {
			return;
		}
		
		bPortalsHelper.createPlayer(player);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		if (player == null) {
			return;
		}
		
		bPortalsHelper.removePlayer(player);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeave(PlayerKickEvent event) {
		Player player = event.getPlayer();
		
		if (player == null) {
			return;
		}
		
		bPortalsHelper.removePlayer(player);
	}
	
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
