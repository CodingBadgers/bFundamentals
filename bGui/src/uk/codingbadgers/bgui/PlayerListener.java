/**
 * bGui 1.2-SNAPSHOT
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
package uk.codingbadgers.bgui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;

import uk.codingbadgers.bgui.gui.Gui;
import uk.codingbadgers.bgui.gui.GuiHandler;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) {
           return;
        }

        Player player = event.getPlayer();
        Gui gui = GuiHandler.getGui();

        if (gui == null) {
            return; // something is really fucked up, #blameemma
        }
        
        if (!gui.getWorld().equals(player.getWorld())) {
            return;
        }

        event.setCancelled(gui.handleClick(player, player.getInventory().getHeldItemSlot()));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();

        if (inv != null) {
            int slot = event.getSlot();
            Gui gui = GuiHandler.getGui();
            Player player = (Player) event.getWhoClicked();

            if (gui == null) {
                return; // something is really fucked up, #blameemma
            }

            if (!gui.getWorld().equals(player.getWorld())) {
                return;
            }

            event.setCancelled(gui.handleClick(player, slot));
        }
    }
    
    @EventHandler // update each join, incase we changed something
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Gui gui = GuiHandler.getGui();

        if (gui == null) {
            return; // something is really fucked up, #blameemma
        }

        if (!gui.getWorld().equals(player.getWorld())) {
            return;
        }

        gui.displayInventory(player);
    }
 
    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        
    }
 
    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        
    }
}
