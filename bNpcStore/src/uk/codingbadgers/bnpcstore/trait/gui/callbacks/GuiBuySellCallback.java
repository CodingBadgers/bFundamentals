/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.bnpcstore.trait.gui.callbacks;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bnpcstore.bNpcStore;
import uk.codingbadgers.bnpcstore.trait.gui.GuiCallback;
import uk.codingbadgers.bnpcstore.trait.gui.GuiInventory;
import uk.codingbadgers.bnpcstore.trait.gui.GuiInventorySubMenu;

public class GuiBuySellCallback implements GuiCallback {
    
    private final GuiInventory m_previousMenu;
    private final GuiInventorySubMenu m_buySellMenu;
    
    public GuiBuySellCallback(GuiInventory previousMenu, String name, Material item, double buyPrice, double sellPrice) {
        m_previousMenu = previousMenu;
        m_buySellMenu = new GuiInventorySubMenu(m_previousMenu, "Buy/Sell " + name, 5);
     
        // Buy
        int row = 1;
        int amount = 64;
        for (int column = 1; column < 8; ++column) {
            GuiBuyItemCallback callback = new GuiBuyItemCallback(item, amount, buyPrice);
            ItemStack itemStack = m_buySellMenu.addMenuItem(name, item, new String[] {"Buy x" + amount + " " + name, "Price: £" + (buyPrice * amount)}, (row * 9) + column, amount, true, callback);
            amount = (amount >> 1);
        }

        // Sell
        row = 2;
        amount = 64;
        for (int column = 1; column < 8; ++column) {
            GuiSellItemCallback callback = new GuiSellItemCallback(item, amount, sellPrice);
            m_buySellMenu.addMenuItem(name, item, new String[] {"Sell x" + amount + " " + name, "Price: £" + (sellPrice * amount)}, (row * 9) + column, amount, callback);
            amount = (amount >> 1);
        }
    }

    @Override
    public void onClick(GuiInventory inventory, InventoryClickEvent clickEvent) {
        
        m_buySellMenu.open((Player)clickEvent.getWhoClicked());
        
    }
    
    class GuiBuyItemCallback implements GuiCallback {
        
        final Material itemMaterial;
        final int amount;
        final double price;
        
        /**
         * Class constructor
         * @param item
         * @param amount
         * @param buyPrice
         * @param sellPrice 
         */
        public GuiBuyItemCallback(Material item, int amount, double price) {
            this.itemMaterial = item;
            this.amount = amount;
            this.price = price;
        }

        @Override
        public void onClick(GuiInventory guiinventory, InventoryClickEvent clickEvent) {
            
            final Player player = (Player)clickEvent.getWhoClicked();
            
            bNpcStore plugin = bNpcStore.getInstance();
            Economy economy = plugin.getEconomy();
            
            if (economy == null) {
                Module.sendMessage(guiinventory.getOwnerTitle(), player, "An error with the economy has occured. Please inform a member of staff.");
                return;
            }
            
            if (!economy.has(player.getName(), price)) {
                Module.sendMessage(guiinventory.getOwnerTitle(), player, "I'm sorry " + player.getName() + " you can't afford that...");
                return;
            }
            
            economy.withdrawPlayer(player.getName(), price);
            
            final PlayerInventory invent = player.getInventory();
            
            ItemStack item = new ItemStack(this.itemMaterial);
            item.setAmount(this.amount);
            invent.addItem(item);
            
            Module.sendMessage(guiinventory.getOwnerTitle(), player, "You have purchased " + this.amount + " " + this.itemMaterial.name() + " for £" + (this.amount * this.price));
            
        } 
    }
    
    class GuiSellItemCallback implements GuiCallback {
        
        final Material itemMaterial;
        final int amount;
        final double price;
        
        /**
         * Class constructor
         * @param item
         * @param amount
         * @param buyPrice
         * @param sellPrice 
         */
        public GuiSellItemCallback(Material item, int amount, double price) {
            this.itemMaterial = item;
            this.amount = amount;
            this.price = price;
        }

        @Override
        public void onClick(GuiInventory guiinventory, InventoryClickEvent clickEvent) {
            
            final Player player = (Player)clickEvent.getWhoClicked();
            final PlayerInventory invent = player.getInventory();
            
            bNpcStore plugin = bNpcStore.getInstance();
            Economy economy = plugin.getEconomy();
            
            if (economy == null) {
                Module.sendMessage(guiinventory.getOwnerTitle(), player, "An error with the economy has occured. Please inform a member of staff.");
                return;
            }
            
            boolean foundItem = false;
            ItemStack[] items = invent.getContents();
            for (ItemStack item : items) {
                if (item == null) {
                    continue;
                }
                
                if (item.getType() == this.itemMaterial) {
                    foundItem = true;
                    final int currentAmount = item.getAmount();
                    final int remaining = currentAmount - this.amount;
                    final double amountToCharge = price * this.amount;
                    
                    if (remaining > 0) {
                        item.setAmount(currentAmount - this.amount);
                        economy.depositPlayer(player.getName(), amountToCharge);
                    }
                    else if (remaining == 0) {
                        invent.remove(item);
                        economy.depositPlayer(player.getName(), amountToCharge);
                    }
                    else {
                        // not enough to sell
                        Module.sendMessage(guiinventory.getOwnerTitle(), player, "You don't have enough " + this.itemMaterial.name() + "s to sell...");
                    }
                    player.updateInventory();
                    break;
                }
            }

            if (!foundItem) {
                // You dont have any to sell
                Module.sendMessage(guiinventory.getOwnerTitle(), player, "You don't have any " + this.itemMaterial.name() + "s to sell...");
            }
        }
    }
}
