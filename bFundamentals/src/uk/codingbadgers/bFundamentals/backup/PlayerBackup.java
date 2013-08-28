package uk.codingbadgers.bFundamentals.backup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@SuppressWarnings("unchecked")
public class PlayerBackup {

	private String name;
	private File backup;
	
	private ItemStack[] invContents = null;
	private ItemStack[] armourContents = null;
	private int xp = 0;
	private int gamemode = 0;
	
	public PlayerBackup(Player player) throws IOException {
		
		if (!BackupFactory.BACKUP_DIR.exists()) {
			BackupFactory.BACKUP_DIR.mkdirs();
		}
		
		this.name = player.getName();
		this.invContents = player.getInventory().getContents();
		this.armourContents = player.getInventory().getArmorContents();
		this.xp = player.getTotalExperience();
		this.gamemode = player.getGameMode().getValue();
		this.backup = new File(BackupFactory.BACKUP_DIR, player.getWorld().getName().replace(File.separatorChar, '_') + File.separatorChar + name.replace(File.separatorChar, '_') + ".json");
		
		if (!this.backup.getParentFile().exists()) {
			this.backup.getParentFile().mkdir();
		}
		
		writeToFile();
	}
	
	public PlayerBackup(File file) throws IOException {
		Validate.notNull(file, "Backup file cannot be null");
		Validate.isTrue(file.exists(), "Backup file must exist");
		
		this.backup = file;
		
		readFromFile();
	}
	
	public String getName() {
		return name;
	}
	
	public void restore(Player player) {
		if (!player.getName().equalsIgnoreCase(name)) {
			return;
		}
		
		player.getInventory().setContents(invContents);
		player.getInventory().setArmorContents(armourContents);
		player.setTotalExperience(xp);
		player.setGameMode(GameMode.getByValue(gamemode));
	}
	
	public void writeToFile() throws IOException {
		
		if (!backup.exists()) {
			backup.createNewFile();
		}
		
		JSONObject obj = new JSONObject();
		
		obj.put("name", name);
		obj.put("level", String.valueOf(xp));
		obj.put("inventory", convertInvToArray(invContents));
		obj.put("armour", convertInvToArray(armourContents));
		obj.put("gamemode", String.valueOf(gamemode));
		
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
	
	public void readFromFile() throws IOException {
		
		// should never come to this
		if (backup == null || !backup.exists()) {
			return;
		}
		
		FileReader reader = null;
		
		try {
			JSONParser parser = new JSONParser();
			reader = new FileReader(backup);
			JSONObject object = (JSONObject) parser.parse(reader);
			
			name = (String) object.get("name");
			invContents = convertArrayToInv((JSONArray) object.get("inventory"));
			armourContents = convertArrayToInv((JSONArray) object.get("armour"));
			xp = Integer.valueOf((String) object.get("level"));
			gamemode = Integer.valueOf((String) object.get("gamemode"));
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	private JSONArray convertInvToArray(ItemStack[] items) {

		JSONArray inv = new JSONArray();
		
		for (ItemStack stack : invContents) {
			JSONObject item = new JSONObject();
			
			if (stack == null) {
				continue;
			}
			
			item.put("id", String.valueOf(stack.getTypeId()));
			item.put("amount", String.valueOf(stack.getAmount()));
			
			JSONArray enchants = new JSONArray();
			
			for (Map.Entry<Enchantment, Integer> entry : stack.getEnchantments().entrySet()) {
				JSONObject enchantment = new JSONObject();
				enchantment.put("id", String.valueOf(entry.getKey().getId()));
				enchantment.put("level", String.valueOf(entry.getValue()));
				enchants.add(enchantment);
			}
			
			item.put("enchant", enchants);
			
			if (stack.getItemMeta().hasDisplayName()) {
				item.put("name", stack.getItemMeta().getDisplayName());
			}
			
			inv.add(item);
		}
		
		return inv;
	}
	
	private ItemStack[] convertArrayToInv(JSONArray array) {
		
		List<ItemStack> items = new ArrayList<ItemStack>(array.size());
		
		for (Object stack : array) {
			
			if (!(stack instanceof JSONArray)) {
				continue;
			}
			
			JSONObject obj = (JSONObject) stack;
			
			ItemStack item = new ItemStack(Integer.valueOf((String) obj.get("id")));
			item.setAmount(Integer.valueOf((String) obj.get("amount")));
			
			for (Object enchObj : (JSONArray) obj.get("enchant")) {

				if (!(enchObj instanceof JSONArray)) {
					continue;
				}
				
				JSONObject ench = (JSONObject) enchObj;
				
				Enchantment enchantment = Enchantment.getById(Integer.valueOf((String) ench.get("id")));
				item.addEnchantment(enchantment, Integer.valueOf((String) ench.get("level")));
			}
			
			if (obj.containsKey("name")) {
				item.getItemMeta().setDisplayName((String) obj.get("name"));
			}
			
			items.add(item);
		}
		
		return items.toArray(new ItemStack[0]);
	}
	
	public boolean deleteFile() {
		return backup.delete();
	}
}
