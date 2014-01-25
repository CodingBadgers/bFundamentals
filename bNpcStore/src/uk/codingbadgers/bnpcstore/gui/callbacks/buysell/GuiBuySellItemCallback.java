/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.bnpcstore.gui.callbacks.buysell;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bnpcstore.gui.GuiCallback;
import uk.codingbadgers.bnpcstore.gui.GuiInventory;

/**
 *
 * @author N3wton
 */
public class GuiBuySellItemCallback implements GuiCallback {
    
    Material item;
    int amount;
    double buyPrice;
    double sellPrice;

    public GuiBuySellItemCallback(Material item, int amount, double buyPrice, double sellPrice) {
        this.item = item;
        this.amount = amount;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    @Override
    public void onClick(GuiInventory inventory, InventoryClickEvent clickEvent) {
        
        final Player player = (Player)clickEvent.getWhoClicked();
        final InventoryAction action = clickEvent.getAction();

        if (action == InventoryAction.PICKUP_ALL) {
            buyItem(inventory.getOwnerTitle(), player);
        } else if (action == InventoryAction.PICKUP_HALF) {
            sellItem(inventory.getOwnerTitle(), player);
        }
        
    }

    private void buyItem(String title, Player player) {
        Module.sendMessage(title, player, "Buy x" + amount + " of " + item.name());
    }

    private void sellItem(String title, Player player) {
        Module.sendMessage(title, player, "Sell x" + amount + " of " + item.name());
    }
    
}
