/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.bnpcstore.npc;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uk.codingbadgers.bnpcstore.bNpcStore;
import uk.codingbadgers.bnpcstore.gui.GuiInventory;

/**
 *
 * @author N3wton
 */
public class StoreNPC {
    
    private String storeName = "Default Store";
    private NPC npc = null;
    
    public StoreNPC(NPC npc) {
        this.npc = npc;
    }
    
    public void load() {
        
    }
    
    public void save() {
        
    }
    
    /**
     * Handle a player clicking the npc
     * @param player The player who clicked
     */
    public void handleClick(Player player) {
        
        bNpcStore module = bNpcStore.getInstance();
        
        ItemStack item = player.getItemInHand();
        if (item != null && item.getType() == Material.NAME_TAG) {
            if (player.hasPermission("bNpcStore.ChangeShop")) {
                final String requestedName = item.getItemMeta().getDisplayName();
                if (module.getInventory(requestedName) != null) {
                    this.storeName = requestedName;
                    module.updateNpcStore(npc, this.storeName);
                    module.output(player, "Changed " + npc.getName() + "'s store to " + this.storeName);
                } else {
                    module.output(player, "There is no store with the name '" + requestedName + "'.");
                }
            }            
            return;
        }
        
        GuiInventory tokenShopInventory = module.getInventory(this.storeName);
        if (tokenShopInventory != null) {
            tokenShopInventory.open(player);
        }
        
    }
    
    public String getStore() {
        return this.storeName;
    }
    
    public void setStore(String store) {
        this.storeName = store;
    }
    
}
