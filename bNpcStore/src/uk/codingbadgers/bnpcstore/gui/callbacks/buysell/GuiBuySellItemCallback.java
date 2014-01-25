/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.bnpcstore.gui.callbacks.buysell;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bnpcstore.bNpcStore;
import uk.codingbadgers.bnpcstore.gui.GuiCallback;
import uk.codingbadgers.bnpcstore.gui.GuiInventory;

/**
 *
 * @author N3wton
 */
public class GuiBuySellItemCallback implements GuiCallback {
    
    String name;
    Material item;
    int amount;
    double buyPrice;
    double sellPrice;

    public GuiBuySellItemCallback(String name, Material item, int amount, double buyPrice, double sellPrice) {
        this.name = name;
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
        
        bNpcStore module = bNpcStore.getInstance();
        Economy eco = module.getEconomy();
        
        final Double totalPrice = this.amount * this.buyPrice;
        
        if (!eco.has(player.getName(), totalPrice)) {
            Module.sendMessage(title, player, "You cant afford " + amount + " " + this.name + (amount != 1 ? "s" : ""));
            return;
        }
        
        PlayerInventory invent = player.getInventory();
        if (invent.firstEmpty() == -1) {
            Module.sendMessage(title, player, "Your inventory is full. Please try again when you have some room.");
            return;
        }
        
        // Take the money first, i'd rather have a pissed off player than an exploit
        eco.withdrawPlayer(player.getName(), totalPrice);
        
        // give the items to the player
        ItemStack boughtItem = new ItemStack(this.item, amount);
        player.getInventory().addItem(boughtItem);
        player.updateInventory();
        
        Module.sendMessage(title, player, "Thankyou for buying " + amount + " " + this.name + (amount != 1 ? "s" : "") + " for " + eco.format(totalPrice));
        
        // Log the purchase (TODO)
    }

    private void sellItem(String title, Player player) {
        
        bNpcStore module = bNpcStore.getInstance();
        Economy eco = module.getEconomy();
        
        final Double totalPrice = this.amount * this.sellPrice;
        
        // Take the items first, i'd rather have a pissed off player than an exploit
        PlayerInventory invent = player.getInventory();
        ItemStack[] contents = invent.getContents();
        int playerItemCount = 0;
        
        for (ItemStack item : contents) {
            
            if (item == null || item.getType() != this.item) {
                continue;
            }
            
            playerItemCount += item.getAmount();
            
        }
        
        if (playerItemCount < this.amount) {
            Module.sendMessage(title, player, "You don't have " + amount + " " + this.name + (amount != 1 ? "s" : "") + " in your inventory");
            return;
        }
        
        int amountToSell = this.amount;
        
        for (ItemStack item : contents) {
            
            if (item == null || item.getType() != this.item) {
                continue;
            }
            
            int stackSize = item.getAmount();
            if (stackSize > amountToSell) {
                item.setAmount(stackSize - amountToSell);
                break;
            }
            else if (stackSize == amountToSell) {
                invent.removeItem(item);
                break;
            }
            else {
                amountToSell -= stackSize;
                invent.removeItem(item);
            }
            
        }
        player.updateInventory();
        
        // Give the money to the player
        eco.depositPlayer(player.getName(), totalPrice);
        
        Module.sendMessage(title, player, "Thankyou for selling " + amount + " " + this.name + (amount != 1 ? "s" : "") + " to me for " + eco.format(totalPrice));
        
        // Log the sale (TODO)
        
    }
    
}
