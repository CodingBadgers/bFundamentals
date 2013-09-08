/**
 * bFundamentals 1.2-SNAPSHOT
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
package uk.codingbadgers.bFundamentals.backup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import uk.codingbadgers.bFundamentals.bFundamentals;

@SuppressWarnings("unchecked")
public class PlayerBackup {

	private String name = null;
	private File backup = null;
	
	private ItemStack[] invContents = null;
	private ItemStack[] armourContents = null;
	private int xp = 0;
	private String gamemode = null;
	
	/**
	 * Get the players armour inventory stack
	 *
	 * @param player the player to query
	 * @return A itemstack array of the players armour
	 */
	private ItemStack[] getPlayerArmour(Player player) {
		
		final PlayerInventory invent = player.getInventory();
		ItemStack[] armour = new ItemStack[4];
		
		armour[0] = invent.getHelmet();
		armour[1] = invent.getChestplate();
		armour[2] = invent.getLeggings();
		armour[3] = invent.getBoots();
		
		return armour;
	}
	
	/**
	 * Set the players armour inventory stack
	 *
	 * @param player the player to query
	 */
	private void setPlayerArmour(Player player, ItemStack[] armour) {
		
		if (armour.length != 4) {
			bFundamentals.log(Level.SEVERE, player.getName() + "s armour player backup does not have 4 elements! It only has " + armour.length);
			return;
		}
		
		final PlayerInventory invent = player.getInventory();
		
		invent.setHelmet(armour[0]);
		invent.setChestplate(armour[1]);
		invent.setLeggings(armour[2]);
		invent.setBoots(armour[3]);
	}
	
	/**
	 * Create a player backup from a given player and write it to a specified file
	 *
	 * @param backupFile The file where the player backup should be written
	 * @param player The player to backup
	 */
	public PlayerBackup(File backupFile, Player player) throws IOException {
				
		this.name = player.getName();
		this.invContents = player.getInventory().getContents();
		this.armourContents = getPlayerArmour(player);
		this.xp = player.getTotalExperience();
		this.gamemode = player.getGameMode().name();
		this.backup = backupFile;
		
		File backupFolder = backupFile.getParentFile();
		if (!backupFolder.exists()) {
			backupFolder.mkdirs();
		}
		
		writeToFile();
	}
	
	/**
	 * Parse a given file to create a player backup object
	 *
	 * @param backupFile The file where the player backup should be read from
	 */
	public PlayerBackup(File file) throws IOException {
		Validate.notNull(file, "Backup file cannot be null");
		Validate.isTrue(file.exists(), "Backup file must exist");
		
		this.backup = file;
		
		readFromFile();
	}
	
	/**
	 * Get the player name this player backup represents
	 *
	 * @return The player name this backup represents
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Restore a players backup
	 *
	 * @param player The player who we are restoring too
	 */
	public void restore(Player player) {
		if (!player.getName().equalsIgnoreCase(name)) {
			return;
		}

		player.getInventory().setContents(invContents);
		setPlayerArmour(player, armourContents);
		player.setTotalExperience(xp);
		player.setGameMode(GameMode.valueOf(gamemode));
	}
	
	/**
	 * Write a player backup to file
	 */
	public void writeToFile() throws IOException {
		
		if (!backup.exists()) {
			backup.createNewFile();
		}
		
		JSONObject obj = new JSONObject();
		
		obj.put("name", name);
		obj.put("level", String.valueOf(xp));
		obj.put("inventory", ItemStackArrayToJSONArray(invContents));
		obj.put("armour", ItemStackArrayToJSONArray(armourContents));
		obj.put("gamemode", gamemode);

		FileWriter stream = null;
		BufferedWriter writer = null;
		
		try {
			stream = new FileWriter(backup);
			writer = new BufferedWriter(stream);
			writer.write(obj.toJSONString());
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}
	}
	
	/**
	 * Read a player backup from file
	 */
	public void readFromFile() throws IOException {

		if (backup == null || !backup.exists()) {
			return;
		}
		
		FileReader reader = null;
		
		try {
			JSONParser parser = new JSONParser();
			reader = new FileReader(backup);
			JSONObject object = (JSONObject) parser.parse(reader);
			
			name = (String) object.get("name");
			invContents = JSONArrayToItemStackArray((JSONArray) object.get("inventory"));
			armourContents = JSONArrayToItemStackArray((JSONArray) object.get("armour"));
			xp = Integer.valueOf((String) object.get("level"));
			gamemode = (String) object.get("gamemode");

		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	/**
	 * Convert an itemstack array into a json array
	 *
	 * @param items The itemstack array to convert
	 * @return A Json array of the given itemstack array
	 */
	@SuppressWarnings("deprecation")
	private JSONArray ItemStackArrayToJSONArray(ItemStack[] items) {

		JSONArray inv = new JSONArray();
		
		for (ItemStack stack : items) {
			JSONObject item = new JSONObject();
			
			if (stack == null) {
				stack = new ItemStack(Material.AIR, 1);
			}
			
			item.put("type", stack.getType().name());
			item.put("amount", String.valueOf(stack.getAmount()));
			item.put("durability", String.valueOf(stack.getDurability()));
			item.put("data", String.valueOf(stack.getData().getData()));
			
			JSONArray enchants = new JSONArray();
			for (Map.Entry<Enchantment, Integer> entry : stack.getEnchantments().entrySet()) {
				JSONObject enchantment = new JSONObject();
				enchantment.put("id", entry.getKey().getName());
				enchantment.put("level", String.valueOf(entry.getValue()));
				enchants.add(enchantment);
			}
			item.put("enchantment", enchants);
			
			ItemMeta itemMeta = stack.getItemMeta();
			if (itemMeta != null) {
				
				if (itemMeta.hasDisplayName() || itemMeta.getLore() != null) {
					
					JSONObject metadata = new JSONObject();
	
					if (itemMeta.hasDisplayName()) {
						metadata.put("displayname", itemMeta.getDisplayName());
					}
					
					List<String> lores = itemMeta.getLore();
					if (lores != null) {
						JSONArray itemLore = new JSONArray();
						for (String loreString : lores) {
							itemLore.add(loreString);
						}
						metadata.put("lores", itemLore);
					}
					
					item.put("metadata", metadata);
				}
			}
			
			inv.add(item);
		}
		
		return inv;
	}
	
	/**
	 * Convert a json array into an itemstack array
	 *
	 * @param array The json array to convert
	 * @return An itemstack array of the given json array
	 */
	@SuppressWarnings("deprecation")
	private ItemStack[] JSONArrayToItemStackArray(JSONArray array) {
		
		List<ItemStack> items = new ArrayList<ItemStack>(array.size());
		
		for (Object itemObject : array) {
			
			if (!(itemObject instanceof JSONObject)) {
				continue;
			}
			
			JSONObject jsonItem = (JSONObject) itemObject;
			
			// Parse item
			ItemStack item = new ItemStack(Material.valueOf((String) jsonItem.get("type")));
			item.setAmount(Integer.valueOf((String) jsonItem.get("amount")));
			item.setDurability(Short.valueOf((String) jsonItem.get("durability")));
			item.getData().setData(Byte.valueOf((String) jsonItem.get("data")));
			
			// Parse enchantments
			JSONArray enchantments = (JSONArray)jsonItem.get("enchantment");
			for (Object enchantmentObject : enchantments ) {

				if (!(enchantmentObject instanceof JSONObject)) {
					continue;
				}
				
				JSONObject jsonEnchantment = (JSONObject) enchantmentObject;
				Enchantment enchantment = Enchantment.getByName((String) jsonEnchantment.get("id"));
				int enchantmentLevel = Integer.valueOf((String) jsonEnchantment.get("level"));
				item.addEnchantment(enchantment, enchantmentLevel);
			}
			
			// Parse metadata
			if (jsonItem.containsKey("metadata")) {
				
				JSONObject metaData = (JSONObject) jsonItem.get("metadata");
				
				ItemMeta itemMeta = item.getItemMeta();
				
				if (metaData.containsKey("displayname")) {
					itemMeta.setDisplayName((String) metaData.get("displayname"));
				}
				
				if (metaData.containsKey("lores")) {
					List<String> lores = new ArrayList<String>();
					JSONArray jsonLores = (JSONArray) metaData.get("lores");
					for (Object loreObject : jsonLores) {
						String lore = (String) loreObject;
						lores.add(lore);						
					}
					itemMeta.setLore(lores);
				}
				
				item.setItemMeta(itemMeta);
			}
			
			items.add(item);
		}
		
		int itemIndex = 0;
		ItemStack[] itemArray = new ItemStack[items.size()];
		for (ItemStack item : items) {
			itemArray[itemIndex] = item;
			itemIndex++;
		}
		
		return itemArray;
	}
	
	/**
	 * Delete the backup file
	 * 
	 * @return True if the file was deleted, false otherwise
	 */
	public boolean deleteFile() {
		return backup.delete();
	}
}
