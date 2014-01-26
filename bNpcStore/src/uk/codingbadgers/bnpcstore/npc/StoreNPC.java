/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.bnpcstore.npc;

import java.sql.ResultSet;
import java.sql.SQLException;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import uk.codingbadgers.bnpcstore.bNpcStore;
import uk.codingbadgers.bnpcstore.database.NPCData;
import uk.codingbadgers.bnpcstore.gui.GuiInventory;
import uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase;
import uk.thecodingbadgers.bDatabaseManager.DatabaseTable.DatabaseTable;

/**
 *
 * @author N3wton
 */
public class StoreNPC {

    private NPC npc = null;
    private NPCData data = null;
    private boolean savedOnce = false;
    
    public StoreNPC(NPC npc, boolean isNew) {
        this.npc = npc;
        
        this.data = new NPCData();
        this.data.storeName = "Default Store";
        this.data.npcID = this.npc.getId();
        this.savedOnce = !isNew;
    }
    
    public void save() {
        
        bNpcStore module = bNpcStore.getInstance();
        
        BukkitDatabase database = module.getDatabaseManager().getDatabase();
        DatabaseTable npcTable = module.getDatabaseManager().getNpcStoreTable();
        
        if (!savedOnce) {
            npcTable.insert(this.data, NPCData.class, true);
            savedOnce = true;
            return;
        }
        
		npcTable.update(this.data, NPCData.class, "npcID='" + data.npcID + "'", false);
        
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
                    this.data.storeName = requestedName;
                    module.updateNpcStore(npc, this.data.storeName);
                    module.output(player, "Changed " + npc.getName() + "'s store to " + this.data.storeName);
                    save();
                } else {
                    module.output(player, "There is no store with the name '" + requestedName + "'.");
                }
            }            
            return;
        }
        
        GuiInventory tokenShopInventory = module.getInventory(this.data.storeName);
        if (tokenShopInventory != null) {
            tokenShopInventory.open(player);
        }
        
    }

    public void setStore(String storeName) {
        this.data.storeName = storeName;
    }
    
}
