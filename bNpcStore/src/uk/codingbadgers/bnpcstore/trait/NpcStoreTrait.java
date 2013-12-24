/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.codingbadgers.bnpcstore.trait;

import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import uk.codingbadgers.bnpcstore.bNpcStore;
import uk.codingbadgers.bnpcstore.trait.gui.GuiInventory;

/**
 *
 * @author N3wton
 */
public class NpcStoreTrait extends Trait {

    final bNpcStore module;
    @Persist("storeName") String storeName = "Default Store";
    
    public NpcStoreTrait() {
        super("npcstore");
        this.module = bNpcStore.getInstance();
    }

    @EventHandler
    public void onLeftClickNPC(NPCLeftClickEvent event) {
        
        if (event.getNPC() != this.getNPC()) {
            return;
        }

        handleClick(event.getClicker());
    }
    
    @EventHandler
    public void onRightClickNPC(NPCRightClickEvent event) {

        if (event.getNPC() != this.getNPC()) {
            return;
        }

        handleClick(event.getClicker());
    }

    /**
     * Handle a player clicking the npc
     * @param player The player who clicked
     */
    private void handleClick(Player player) {
        
        ItemStack item = player.getItemInHand();
        if (item != null && item.getType() == Material.NAME_TAG) {
            if (player.hasPermission("bNpcStore.ChangeShop")) {
                this.storeName = item.getItemMeta().getDisplayName();
                this.module.output(player, "Changed " + this.npc.getName() + "'s store to " + this.storeName);
            }            
            return;
        }
        
        GuiInventory tokenShopInventory = this.module.getInventory(this.storeName);
        if (tokenShopInventory != null) {
            tokenShopInventory.open(player);
        }
        
        //tokenShopInventory.addSubMenuItem("Money", Material.GOLD_INGOT, new GuiInventorySubMenu(tokenShopInventory, "Money", 3));
    }

}
