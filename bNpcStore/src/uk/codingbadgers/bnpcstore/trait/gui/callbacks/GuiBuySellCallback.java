/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.bnpcstore.trait.gui.callbacks;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import uk.codingbadgers.bnpcstore.trait.gui.GuiCallback;
import uk.codingbadgers.bnpcstore.trait.gui.GuiInventory;
import uk.codingbadgers.bnpcstore.trait.gui.GuiInventorySubMenu;

public class GuiBuySellCallback implements GuiCallback {
    
    private final GuiInventory m_previousMenu;
    private final GuiInventorySubMenu m_buySellMenu;
    
    public GuiBuySellCallback(GuiInventory previousMenu, String name, Material item, int buyPrice, int sellPrice) {
        m_previousMenu = previousMenu;
        m_buySellMenu = new GuiInventorySubMenu(m_previousMenu, "Buy/Sell " + name, 5);
     
        // Buy
        int row = 1;
        int amount = 64;
        for (int column = 1; column < 8; ++column) {
            ItemStack itemStack = m_buySellMenu.addMenuItem(name, item, new String[] {"Buy x" + amount + " " + name, "Price: £" + (buyPrice * amount)}, (row * 9) + column, amount, true, null);
            amount = (amount >> 1);
        }

        // Sell
        row = 2;
        amount = 64;
        for (int column = 1; column < 8; ++column) {
            m_buySellMenu.addMenuItem(name, item, new String[] {"Sell x" + amount + " " + name, "Price: £" + (sellPrice * amount)}, (row * 9) + column, amount, null);
            amount = (amount >> 1);
        }
    }

    @Override
    public void onClick(GuiInventory inventory, InventoryClickEvent clickEvent) {
        
        m_buySellMenu.open((Player)clickEvent.getWhoClicked());
        
    }
    
}
