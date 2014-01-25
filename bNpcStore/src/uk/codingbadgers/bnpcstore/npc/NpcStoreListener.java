/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.codingbadgers.bnpcstore.npc;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.codingbadgers.bnpcstore.bNpcStore;
import uk.codingbadgers.bnpcstore.commands.NpcStoreCommand;

/**
 *
 * @author N3wton
 */
public class NpcStoreListener implements Listener {

    public NpcStoreListener() {

    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        
        final Player player = event.getPlayer();
        
        NPC npc = CitizensAPI.getNPCRegistry().getNPC(event.getRightClicked());
        if (npc != null) {
            StoreNPC storeNpc = bNpcStore.getInstance().findStoreNPC(npc);
            if (storeNpc != null) {
                storeNpc.handleClick(player); 
            }
        }
        
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        
        final Player player = event.getPlayer();
        final ItemStack itemInHand = player.getItemInHand();
        final ItemMeta meta = itemInHand.getItemMeta();
        
        if (meta == null || !meta.hasDisplayName()) {
            return;
        }
        
        final String itemName = meta.getDisplayName();
        if (!itemName.equalsIgnoreCase(NpcStoreCommand.WANDNAME)) {
            return;
        }
        
        // Using the wand, make an npc store
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Default Store");
        npc.spawn(player.getLocation());
  
        StoreNPC storeNPC = new StoreNPC(npc);
        storeNPC.save();
        
        bNpcStore module = bNpcStore.getInstance();
        module.registerNewNPC(storeNPC, npc);
        
    }
    
}
