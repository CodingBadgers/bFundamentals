package uk.codingbadgers.bnpcstore.trait.gui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import uk.codingbadgers.bnpcstore.trait.gui.callbacks.GuiBuySellCallback;
import uk.codingbadgers.bnpcstore.trait.gui.misc.GuiEnchantment;

public class GuiInventory implements Listener {

    private Plugin m_plugin;
    private String m_title;
    private int m_rowCount;

    private Map<String, GuiSubInventory> m_subMenus;
    private Map<String, ItemStack> m_items;
    private Map<Integer, GuiCallback> m_callbacks;
    
    protected List<String> m_subMenuNames;
    protected List<String> m_itemNames;
    private Enchantment m_highlight;

    private Inventory m_inventory;

    /**
     * Class constructor
     *
     * @param title The title of the inventory
     * @param owner The owner of the inventory
     * @param rowCount The number of rows in the inventory
     */
    public GuiInventory(Plugin plugin, String title, int rowCount) {
        m_plugin = plugin;
        m_title = title;
        m_rowCount = rowCount;

        m_plugin.getServer().getPluginManager().registerEvents(this, m_plugin);

        m_subMenus = new HashMap<String, GuiSubInventory>();
        m_items = new HashMap<String, ItemStack>();
        m_callbacks = new HashMap<Integer, GuiCallback>();
        
        m_subMenuNames = new ArrayList<String>();
        m_itemNames = new ArrayList<String>();
        createHighlightEnchantment();

        m_inventory = Bukkit.createInventory(null, m_rowCount * 9, m_title);
    }

    /**
     * Constructor to load a guiinventory from disk
     * @param plugin The owner plugin
     * @param storeConfig The store config file
     * @param subMenuConfig The submenu config file
     * @param itemConfig The itemmenu config file
     */
    public GuiInventory(Plugin plugin, FileConfiguration storeConfig, FileConfiguration subMenuConfig, FileConfiguration itemConfig) {
        m_plugin = plugin;
        
        m_plugin.getServer().getPluginManager().registerEvents(this, m_plugin);

        m_subMenus = new HashMap<String, GuiSubInventory>();
        m_items = new HashMap<String, ItemStack>();
        m_callbacks = new HashMap<Integer, GuiCallback>();
        
        m_subMenuNames = new ArrayList<String>();
        m_itemNames = new ArrayList<String>();
        createHighlightEnchantment();
        
        loadStoreConfig(storeConfig);
        m_inventory = Bukkit.createInventory(null, m_rowCount * 9, m_title);
        
        loadSubMenuConfig(subMenuConfig, itemConfig);
    }
    
    /**
     * 
     */
    private void createHighlightEnchantment() {
        m_highlight = Enchantment.getByName("GuiEnchantment"); 
        
        if (m_highlight == null) {
            m_highlight = new GuiEnchantment();      
            try {
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null, true);
            } catch(Exception e){
                e.printStackTrace();
            }

            try {
                Enchantment.registerEnchantment(m_highlight);
            } catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Open the inventory for a specific player
     * @param player The player to open the inventory to
     */
    public void open(Player player) {
        player.openInventory(m_inventory);
    }

    /**
     * Close a players inventory
     */
    public void close(Player player) {
        if (player.getInventory() == m_inventory) {
            player.closeInventory();
        }
    }

    /**
     * Get the plugin this inventory was created by
     *
     * @return
     */
    public Plugin getPlugin() {
        return m_plugin;
    }
    
    /**
     * Get the title of the inventory
     * 
     * @return 
     */
    public String getTitle() {
        return m_title;
    }
    
    /**
     * 
     * @return 
     */
    public String getOwnerTitle() {
        return m_title;
    }

    /**
     * Add a sub menu item to the inventory. On click the sub menu inventory
     * will be opened
     *
     * @param name The name of the sub menu
     * @param icon The icon to use
     * @param subInventory The sub menu inventory
     */
    public void addSubMenuItem(String name, Material icon, List<String> details, GuiSubInventory subInventory) {

        if (m_subMenus.containsKey(name)) {
            Bukkit.getLogger().log(Level.WARNING, "A submenu called '" + name + "' already exists in '" + m_title + "'.");
            return;
        }

        ItemStack item = new ItemStack(icon);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(details);
        item.setItemMeta(meta);

        m_inventory.addItem(item);
        m_subMenus.put(name, subInventory);

    }

    /**
     * Add a menu item to the inventory. When the item is click an GuiCallback
     * onClick method will be called
     *
     * @param name The name of the item
     * @param icon The icon to use for the item
     * @param details Some details about the item
     * @param callback A callback that is called when the item is clicked
     * @throws Exception
     */
    public ItemStack addMenuItem(String name, Material icon, String[] details, GuiCallback callback) {

        int slot = 0;
        while (m_inventory.getItem(slot) != null) {
            slot++;
        }

        return addMenuItem(name, icon, details, slot, callback);

    }

    /**
     * Add a menu item to the inventory. When the item is click an GuiCallback
     * onClick method will be called
     *
     * @param name The name of the item
     * @param icon The icon to use for the item
     * @param details Some details about the item
     * @param slot The slot in the inventory to position the item
     * @param callback A callback that is called when the item is clicked
     * @throws Exception
     */
    public ItemStack addMenuItem(String name, Material icon, String[] details, int slot, GuiCallback callback) {

        return addMenuItem(name, icon, details, slot, 1, callback);
    }
    
    /**
     * Add a menu item to the inventory. When the item is click an GuiCallback
     * onClick method will be called
     *
     * @param name The name of the item
     * @param icon The icon to use for the item
     * @param details Some details about the item
     * @param slot The slot in the inventory to position the item
     * @param amount The amount of items in the itemstack
     * @param callback A callback that is called when the item is clicked
     * @throws Exception
     */
    public ItemStack addMenuItem(String name, Material icon, String[] details, int slot, int amount, GuiCallback callback) {

        return addMenuItem(name, icon, details, slot, amount, false, callback);
    }
    
    /**
     * Add a menu item to the inventory. When the item is click an GuiCallback
     * onClick method will be called
     *
     * @param name The name of the item
     * @param icon The icon to use for the item
     * @param details Some details about the item
     * @param slot The slot in the inventory to position the item
     * @param amount The amount of items in the itemstack
     * @param glow Should the item glow (enchantment glow)
     * @param callback A callback that is called when the item is clicked
     * @throws Exception
     */
    public ItemStack addMenuItem(String name, Material icon, String[] details, int slot, int amount, boolean glow, GuiCallback callback) {

        ItemStack item = new ItemStack(icon);
        item.setAmount(amount);
        
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(details));
        item.setItemMeta(meta);
        
        if (glow) {
            item.addUnsafeEnchantment(m_highlight, 1);
        }

        m_inventory.setItem(slot, item);
        if (callback != null) {
            m_callbacks.put(slot, callback);
        }
        
        m_items.put(name, item);
        
        return item;
    }

    /**
     * Handle click events within inventory's
     *
     * @param event The click event
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        
        // Do we care about this inventory?
        Inventory inventory = event.getInventory();
        if (!inventory.getName().equalsIgnoreCase(m_inventory.getName())) {
            return;
        }
        
        // Do we care about this player?
        Player player = (Player) event.getWhoClicked();

        boolean isViewer = false;
        for (HumanEntity entity : m_inventory.getViewers()) {
            if (entity.getName().equalsIgnoreCase(player.getName())) {
                isViewer = true;
                break;
            }
        }
        
        if (!isViewer) {
            return;
        }
        
        // Always cancel the event
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
        player.updateInventory();

        // If they havn't clicked an item, quit
        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getItemMeta() == null || clickedItem.getItemMeta().getDisplayName() == null) {
            return;
        }
                
        // Get the name of the item
        final String itemName = clickedItem.getItemMeta().getDisplayName();

        // If the item is a submenu, close this and open the submenu
        if (m_subMenus.containsKey(itemName)) {
            GuiInventory subMenu = m_subMenus.get(itemName);
            subMenu.open(player);
            return;
        }

        final int itemSlot = event.getSlot();

        // Item is a normal item, call its gui callback method
        if (m_callbacks.containsKey(itemSlot)) {
            GuiCallback callback = m_callbacks.get(itemSlot);
            callback.onClick(this, event);
            return;
        }

    }

    /**
     * Load the main store config
     * @param storeConfig Config to load from
     */
    private void loadStoreConfig(FileConfiguration storeConfig) {
        
        m_title = storeConfig.getString("store.name");
        m_rowCount = storeConfig.getInt("store.rows");

        List<String> submenus = storeConfig.getStringList("store.submenus");
        for (String submenu : submenus) {
            m_subMenuNames.add(submenu);
        }
        
        List<String> items = storeConfig.getStringList("store.items");
        for (String item : items) {
            m_itemNames.add(item);
        }
    }

    /**
     * Load the sub menu config
     * @param subMenuConfig The sub menu config to load
     */
    protected void loadSubMenuConfig(FileConfiguration subMenuConfig, FileConfiguration itemConfig) {
        
        if (this.m_subMenuNames.isEmpty()) {
            loadItemConfig(itemConfig);
            return;
        }

        int subMenuIndex = 0;
        String subMenu = this.m_subMenuNames.get(subMenuIndex);
        while (this.m_subMenus.get(subMenu) == null) {
            
            String nodePath = "submenu." + subMenu;
            
            String name = subMenuConfig.getString(nodePath + ".name");
            Material icon = Material.valueOf(subMenuConfig.getString(nodePath + ".icon"));
            int rows = subMenuConfig.getInt(nodePath + ".rows");
            List<String> details = subMenuConfig.getStringList(nodePath + ".details");
            
            List<String> submenus = subMenuConfig.getStringList(nodePath + ".submenus");
            List<String> items = subMenuConfig.getStringList(nodePath + ".items");

            GuiInventorySubMenu subMenuInventory = new GuiInventorySubMenu(this, m_title + " - " + name, rows);
            subMenuInventory.addSubmenus(submenus);
            subMenuInventory.addItems(items);
            subMenuInventory.loadSubMenuConfig(subMenuConfig, itemConfig);
            
            this.addSubMenuItem(name, icon, details, subMenuInventory);

            subMenuIndex++;
            if (subMenuIndex >= this.m_subMenuNames.size()) {
                break;
            }
            subMenu = this.m_subMenuNames.get(subMenuIndex);
        }
        
        loadItemConfig(itemConfig);
        
    }

    private void loadItemConfig(FileConfiguration itemConfig) {
        
        if (this.m_itemNames.isEmpty()) {
            return;
        }

        int itemIndex = 0;
        String item = this.m_itemNames.get(itemIndex);
        while (this.m_items.get(item) == null) {
            
            String nodePath = "item." + item;
            
            try {
                String name = itemConfig.getString(nodePath + ".name");
                Material icon = Material.valueOf(itemConfig.getString(nodePath + ".icon"));
                int row = itemConfig.getInt(nodePath + ".row");
                int column = itemConfig.getInt(nodePath + ".column");
                List<String> details = itemConfig.getStringList(nodePath + ".details");
                
                
                GuiCallback onClickCallback = null;
                if (itemConfig.contains(nodePath + ".onclick")) {
                    
                    // BuySellCallback
                    if (itemConfig.contains(nodePath + ".onclick.buysell")) {
                        double buyPrice = itemConfig.getDouble(nodePath + ".onclick.buysell.buyprice", 100.0);
                        double sellPrice = itemConfig.getDouble(nodePath + ".onclick.buysell.sellprice", 100.0);
                        
                        details.add(" ");
                        details.add("Buy for £" + buyPrice + " each.");
                        details.add("Sell for £" + sellPrice + " each.");
                        
                        onClickCallback = new GuiBuySellCallback(this, name, icon, buyPrice, sellPrice);
                    }
                    // Other standard callbacks? Message? Back? Teleport?
                    // else if () { 
                }
                
                String[] detailsArray = new String[details.size()];
                details.toArray(detailsArray);
                this.addMenuItem(name, icon, detailsArray, (row * 9) + column, onClickCallback);
                
            } catch(Exception ex) {
                Bukkit.getLogger().log(Level.WARNING, "Failed to load item - " + item, ex);
            }
            
            itemIndex++;
            if (itemIndex >= this.m_itemNames.size()) {
                break;
            }
            item = this.m_itemNames.get(itemIndex);
        }
        
    }
    
    /**
     * 
     * @param subMenus 
     */
    protected void addSubmenus(List<String> subMenus) {
        this.m_subMenuNames.addAll(subMenus);
    }
    
    /**
     * 
     * @param items 
     */
    protected void addItems(List<String> items) {
        this.m_itemNames.addAll(items);
    }

}
