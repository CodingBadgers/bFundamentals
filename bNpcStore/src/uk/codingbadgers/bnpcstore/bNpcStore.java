/**
 * bNpcStore 1.2-SNAPSHOT Copyright (C) 2013 CodingBadgers
 * <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package uk.codingbadgers.bnpcstore;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import net.citizensnpcs.api.trait.TraitFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bnpcstore.trait.NpcStoreTrait;
import uk.codingbadgers.bnpcstore.trait.gui.GuiInventory;

public class bNpcStore extends Module implements Listener {

    /** Static plugin module instance */
    static bNpcStore instance;
    
    /** A map of inventories and there names */
    private Map<String, GuiInventory> stores;
    
    /**
     * Called when the module is disabled.
     */
    @Override
    public void onDisable() {

    }

    /**
     * Called when the module is loaded.
     */
    @Override
    public void onEnable() {
        
        bNpcStore.instance = this;
        register(this);
        
        // Register trait
	TraitFactory factory = net.citizensnpcs.api.CitizensAPI.getTraitFactory();
	factory.registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(NpcStoreTrait.class).withName("npcstore"));
        
        // Load store guis
        loadStoreGuis();
    }
    
    /**
     * Output a message to someone
     * @param destination The thing to send the message to
     * @param message The message to send
     */
    public void output(CommandSender destination, String message) {
        Module.sendMessage(this.getName(), destination, message);
    }
    
    /**
     * Access to the module instance
     * @return 
     */
    public static bNpcStore getInstance() {
        return bNpcStore.instance;
    }

    /**
     * Load all store gui configs
     */
    private void loadStoreGuis() {
        
        this.stores = new HashMap<String, GuiInventory>();
        
        File storeFolder = new File(this.getDataFolder() + File.separator + "stores");
        if (!storeFolder.exists()) {
            storeFolder.mkdirs();
            createDefaultStore(storeFolder);
        }
        
        for (File folder : storeFolder.listFiles()) {
            
            if (!folder.isDirectory()) {
                continue;
            }
            
            FileConfiguration storeConfig = createConfigFile(folder + File.separator + "store.yml");
            FileConfiguration subMenuConfig = createConfigFile(folder + File.separator + "submenus.yml");
            FileConfiguration itemConfig = createConfigFile(folder + File.separator + "items.yml");
            
            GuiInventory inventory = new GuiInventory(m_plugin, storeConfig, subMenuConfig, itemConfig);
            this.stores.put(inventory.getTitle(), inventory);
        }
        
    }

    /**
     * Create a default store as an example
     */
    private void createDefaultStore(File storeFolder) {
   
        storeFolder = new File(storeFolder + File.separator + "default");
 
        FileConfiguration storeConfig = createConfigFile(storeFolder + File.separator + "store.yml");
        FileConfiguration subMenuConfig = createConfigFile(storeFolder + File.separator + "submenus.yml");
        FileConfiguration itemConfig = createConfigFile(storeFolder + File.separator + "items.yml");
        
        // Setup the default store
        storeConfig.set("store.name", "Default Store");
        storeConfig.set("store.rows", 1);
        storeConfig.set("store.submenus", new String[] {"Example1", "Example2", "Example3"});
        storeConfig.set("store.items", new String[] {"TestItem"});
        
        // Setup the submenus
        for (int exampleIndex = 1; exampleIndex <= 3; ++exampleIndex) {
            String nodePath = "submenu.Example" + exampleIndex;
            subMenuConfig.set(nodePath + ".name", "Example " + exampleIndex);
            subMenuConfig.set(nodePath + ".icon", Material.GOLDEN_APPLE.name());
            subMenuConfig.set(nodePath + ".rows", exampleIndex + 1);
            subMenuConfig.set(nodePath + ".details", new String[] {"This is", "an example", "submenu"});
            subMenuConfig.set(nodePath + ".submenus", new String[] {});
            subMenuConfig.set(nodePath + ".items", new String[] {"Apple", "Potato", "Carrot"});
        }
        
        // Setup the items
        String[] items = new String[] {"Apple", "Potato", "Carrot", "Example Item"};
        Material[] icons = new Material[] {Material.APPLE, Material.POTATO, Material.CARROT, Material.DIAMOND};
        for (int exampleIndex = 0; exampleIndex < 4; ++exampleIndex) {
            String nodePath = "item." + items[exampleIndex];
            itemConfig.set(nodePath + ".name", items[exampleIndex]);
            itemConfig.set(nodePath + ".icon", icons[exampleIndex].name());
            itemConfig.set(nodePath + ".row", 0);
            itemConfig.set(nodePath + ".column", exampleIndex);
            itemConfig.set(nodePath + ".cost", exampleIndex * 17);
            itemConfig.set(nodePath + ".details", new String[] {"This is", "an example", "item"});
        }
        
        try {
            storeConfig.save(new File(storeFolder + File.separator + "store.yml"));
            subMenuConfig.save(new File(storeFolder + File.separator + "submenus.yml"));
            itemConfig.save(new File(storeFolder + File.separator + "items.yml"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Create a configuration file at a given path
     * @param path The path to the config file.
     * @return A FileConfiguration for a file at the given path
     */
    private FileConfiguration createConfigFile(String path) {
        
        File file = new File(path);
        
        try  {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (IOException ex) {
            return null;
        }
        
        return YamlConfiguration.loadConfiguration(file);
    }

    public GuiInventory getInventory(String storeName) {
        
        if (!this.stores.containsKey(storeName)) {
            Bukkit.getLogger().log(Level.WARNING, "Could not find a store with the name '" + storeName + "'");
            return null;
        }
        
        return this.stores.get(storeName);
    }
    
}
