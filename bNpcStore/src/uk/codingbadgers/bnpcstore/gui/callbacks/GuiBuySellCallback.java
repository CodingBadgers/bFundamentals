/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.bnpcstore.gui.callbacks;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import uk.codingbadgers.bnpcstore.bNpcStore;
import uk.codingbadgers.bnpcstore.gui.GuiCallback;
import uk.codingbadgers.bnpcstore.gui.GuiInventory;
import uk.codingbadgers.bnpcstore.gui.GuiInventorySubMenu;
import uk.codingbadgers.bnpcstore.gui.callbacks.buysell.GuiBuySellItemCallback;

public class GuiBuySellCallback implements GuiCallback {
    
    private final GuiInventory m_previousMenu;
    private final GuiInventorySubMenu m_buySellMenu;
    
    public GuiBuySellCallback(GuiInventory previousMenu, String name, Material item, double buyPrice, double sellPrice) {
        m_previousMenu = previousMenu;
        m_buySellMenu = new GuiInventorySubMenu(m_previousMenu, "Buy/Sell " + name, 2);
     
        bNpcStore module = bNpcStore.getInstance();
        Economy eco = module.getEconomy();
        
        // Buy
        int row = 0;
        int amount = 64;
        for (int column = 1; column < 8; ++column) {
            GuiBuySellItemCallback callback = new GuiBuySellItemCallback(name, item, amount, buyPrice, sellPrice);
            String[] details = new String[] 
            {
                ChatColor.DARK_GREEN + "Quantity: " + ChatColor.GREEN + ChatColor.BOLD + amount + " " + name + (amount != 1 ? "s" : ""), 
                ChatColor.GOLD + "Buy Price: " + ChatColor.YELLOW + ChatColor.BOLD + eco.format(buyPrice * amount), 
                ChatColor.GOLD + "Sell Price: " + ChatColor.YELLOW + ChatColor.BOLD + eco.format(sellPrice * amount),
                ChatColor.RED + "" + ChatColor.UNDERLINE + "Left Click Buy. Right Click Sell."
            };
            
            m_buySellMenu.addMenuItem(name, item, details, (row * 9) + column, amount, false, callback);
            amount = (amount >> 1);
        }
    }

    @Override
    public void onClick(GuiInventory inventory, InventoryClickEvent clickEvent) {
        m_buySellMenu.open((Player)clickEvent.getWhoClicked());
    }
}
