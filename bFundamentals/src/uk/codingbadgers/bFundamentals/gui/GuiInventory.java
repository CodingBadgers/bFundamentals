package uk.codingbadgers.bFundamentals.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class GuiInventory implements Listener {

    private final Plugin m_plugin;
    private String m_title;
    private int m_rowCount;

    private final Map<String, GuiSubInventory> m_subMenus;
    private final Map<String, ItemStack> m_items;
    private final Map<Integer, GuiCallback> m_callbacks;
    private Inventory m_inventory;
    
    /**
     * Class constructor
     *
     * @param plugin The owner of the inventory
     */
    public GuiInventory(Plugin plugin) {
        m_plugin = plugin;

        m_plugin.getServer().getPluginManager().registerEvents(this, m_plugin);

        m_subMenus = new HashMap<String, GuiSubInventory>();
        m_items = new HashMap<String, ItemStack>();
        m_callbacks = new HashMap<Integer, GuiCallback>();
    }
    
    public void createInventory(String title, int rowCount) {
        m_title = title;
        m_rowCount = rowCount;
        
        m_inventory = Bukkit.createInventory(null, m_rowCount * 9, m_title);
    }
    
    public void createInventory(String title, InventoryType type) {
        m_title = title;        
        m_inventory = Bukkit.createInventory(null, type);
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
    public ItemStack addMenuItem(String name, ItemStack icon, String[] details, GuiCallback callback) {

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
    public ItemStack addMenuItem(String name, ItemStack icon, String[] details, int slot, GuiCallback callback) {

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
    public ItemStack addMenuItem(String name, ItemStack icon, String[] details, int slot, int amount, GuiCallback callback) {

        ItemStack item = icon.clone();
        item.setAmount(amount);
        
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(details));
        item.setItemMeta(meta);

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
        
        if (event.getSlotType() != SlotType.CONTAINER) {
            return;
        }
        
        InventoryView iv = event.getView();
        if (event.getRawSlot() >= iv.getTopInventory().getSize()) {
            return;
        }
        
        // If they havn't clicked an item, quit
        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getItemMeta() == null || clickedItem.getItemMeta().getDisplayName() == null) {
            return;
        }
                
        // Always cancel the event
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
        player.updateInventory();
        
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
     * 
     * @param name
     * @return 
     */
    public GuiInventory getSubMenu(String name) {
        return this.m_subMenus.get(name);
    }
    
    /**
     * 
     * @param name
     * @return 
     */
    public ItemStack getItem(String name) {
        return this.m_items.get(name);
    }

}
