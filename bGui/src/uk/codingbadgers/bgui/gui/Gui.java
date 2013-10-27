/**
 * bGui 1.2-SNAPSHOT
 * Copyright (C) 2013  CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.codingbadgers.bgui.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import uk.codingbadgers.bgui.click.ActionType;
import uk.codingbadgers.bgui.click.ClickHandler;
import uk.codingbadgers.bgui.exception.GuiFormatException;

public final class Gui {

    private final String name;
    private final int size;
    private final World world;
    private Map<Integer, GuiItem> items;

    public Gui(JSONObject json) {
        size = Integer.parseInt((String) json.get("size"));
        
        if (size > 9) {
            throw new GuiFormatException("Maxium size for a gui is 9");
        }
        
        name = (String) json.get("name");
        world = Bukkit.getWorld((String) json.get("world"));
        items = loadItems((JSONArray)json.get("items"));
    }

    private Map<Integer, GuiItem> loadItems(JSONArray jsonArray) {
        Map<Integer, GuiItem> items = new HashMap<Integer, GuiItem>();
        
        for (Object obj : jsonArray) {
            if (!(obj instanceof JSONObject)) {
                continue;
            }
            
            JSONObject json = (JSONObject) obj;

            int slot = Integer.parseInt((String) json.get("slot"));
            
            Material mat = Material.getMaterial((String) json.get("item"));
            String name = (String) json.get("name");
            short data = ((Number) json.get("data")).shortValue();
            String[] lore = convertLore((JSONArray) json.get("lore"));
            
            ItemStack stack = new ItemStack(mat, 1, data);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore));
            stack.setItemMeta(meta);
            
            ClickHandler handler = ActionType.getClickHandler((JSONObject) json.get("onclick"));
            
            items.put(slot, new GuiItem(stack, handler));
        }
        return items;
    }
    
    private String[] convertLore(JSONArray jsonArray) {
        String[] lore = new String[jsonArray.size()];
        int i = 0;
        for (Object object : jsonArray) {
            lore[i] = (String) object;
            i++;
        }
        return lore;
    }

    public Inventory displayInventory(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.clear();
        
        for (Map.Entry<Integer, GuiItem> entry : this.items.entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue().getItem());
        }
        
        return inv;
    }
    
    public boolean handleClick(Player player, int slot) {
        if (items.containsKey(slot)) {
            GuiItem item = items.get(slot);
            
            if (item == null || !item.getItem().isSimilar(player.getItemInHand())) {
                return false;
            }
            
            items.get(slot).handleClick(player);
            return true;
        }
        
        return false;
    }

    public String getName() {
        return name;
    }
    
    public World getWorld() {
        return world;
    }

}
