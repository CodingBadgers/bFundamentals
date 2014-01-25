/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.bnpcstore.commands;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bnpcstore.bNpcStore;

/**
 *
 * @author N3wton
 */
public class NpcStoreCommand extends ModuleCommand {
    
    public static final String WANDNAME = ChatColor.GOLD + "Store Maker " + ChatColor.BOLD + "Turbo Pro";

    public NpcStoreCommand() {
        super("npcstore", "/npcstore");
    }
    
    /**
	 * Called when the 'pet' command is executed.
	 */
	public boolean onCommand(CommandSender sender, String label, String[] args) {
        
		if (!(sender instanceof Player))
			return true;
        
        Player player = (Player)sender;
        
        if (args.length == 0) {
            // Show command usage
            return true;
        }
        
        bNpcStore module = bNpcStore.getInstance();
        
        if (args.length == 1) {
            
            if (args[0].equalsIgnoreCase("create")) {
                
                if (!sender.hasPermission("bstorenpc.create")) {
                    return true;
                }
                
                ItemStack storeTool = new ItemStack(Material.STICK);
                ItemMeta meta = storeTool.getItemMeta();
                meta.setDisplayName(WANDNAME);
                storeTool.setItemMeta(meta);
                player.getInventory().addItem(storeTool);
                
                List<String> storeNames = module.getStoreNames();
                for (String name : storeNames) {
                    ItemStack storeTag = new ItemStack(Material.NAME_TAG);
                    ItemMeta tagmeta = storeTag.getItemMeta();
                    tagmeta.setDisplayName(name);
                    storeTag.setItemMeta(tagmeta);
                    player.getInventory().addItem(storeTag);
                }
                
                player.updateInventory();
            }
            
        }
        
        return true;
    }
    
}
