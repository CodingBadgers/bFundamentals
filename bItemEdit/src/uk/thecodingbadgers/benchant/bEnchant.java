package uk.thecodingbadgers.benchant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Dispenser;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

public class bEnchant extends Module {

	@Override
	public void onEnable() {
		registerCommand(new ModuleCommand("Enchant", "/enchant <add/set> <enchantment> <level>").setHelp("The base command for bEnchant, allowing you to add or set enchantments for any item"));
		registerCommand(new ModuleCommand("Itemedit", "/editItem <name/desc/owner> <value>").setHelp("Allows you to set values for items name or description").addAliase("editItem"));
		registerCommand(new ModuleCommand("Firework", "/firework [-ft] <color> <type> [fade]").setHelp("Allows you to edit fireworks properties and effects"));
		registerCommand(new ModuleCommand("fillDispenser", "/fillDispenser").setHelp("Allows you to fill a dispenser"));
		registerCommand(new ModuleCommand("spawner", "/fillDispenser").setHelp("Allows you to edit the properties of a mobspawner"));
		
		this.log(Level.INFO, getName() + " v: " + getDesciption().getVersion() + " has been enabled successfuly");
	}

	@Override
	public void onDisable() {
		this.log(Level.INFO, getName() + " v: " + getDesciption().getVersion() + " has been disabled successfuly");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, String command, String[] args) {
		
		if (command.equalsIgnoreCase("enchant")) {
			handleEnchant(sender, command, args);
			return true;
		}
		
		if (command.equalsIgnoreCase("editItem") || command.equalsIgnoreCase("itemedit")) {
			handleEditItem(sender, command, args);
			return true;
		} else if (command.equalsIgnoreCase("firework")) {
			handleFirework(sender, command, args);
			return true;
		} else if (command.equalsIgnoreCase("fillDispenser")) {
			handleFillDispenser(sender, command, args);
			return true;
		} else if (command.equalsIgnoreCase("spawner")) {
			handleMobSpawner(sender, command, args);
		}
		
		return false;
	}
	
	private void handleMobSpawner(CommandSender sender, String command, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.DARK_PURPLE + "[bEnchant]" + ChatColor.WHITE + " sorry you have to be a player do do this");
		}
		
		Player player = (Player)sender;
		
		if (!hasPermission(player, "bEnchant.enchant")) {
			sendMessage(getName(), player, "Sorry you do not have permission to do this");
			return;
		}
		
		// entity,spawnDelay,nearbyEntities,playreRange,spawnCount,spawnRange
		if (args.length < 1) {
			sendMessage(getName(), player, "/mobspaner <entity> [player range] [near entities] [spawn delay] [spawn count] [spawn range]");
			return;
		}
		
		Block target = player.getTargetBlock(null, 100);
		
		// entity
		String entityName = args[0];
		SpawnerEntity entity = EntityHelper.createEntity(entityName);
		
		if (target.getType().equals(Material.CHEST)) {
			Inventory inv = ((Chest)target.getState()).getBlockInventory();
			
			entity.setEquiment(SpawnerEntity.HELMET, inv.getItem(0));
			entity.setEquiment(SpawnerEntity.CHEST_PLATE, inv.getItem(1));
			entity.setEquiment(SpawnerEntity.LEGGINGS, inv.getItem(2));
			entity.setEquiment(SpawnerEntity.BOOTS, inv.getItem(3));
			entity.setEquiment(SpawnerEntity.HAND, inv.getItem(4));
			
			for(int i = 9; i < 17; i++) {
				if (inv.getItem(i) == null || !inv.getItem(i).getType().equals(Material.POTION)) {
					continue;
				}
				
				Potion potion = Potion.fromItemStack(inv.getItem(i));
				for (PotionEffect effect : potion.getEffects()) {
					entity.addEffect(effect);
				}
			}
			
			target.breakNaturally();
			target.setType(Material.MOB_SPAWNER);
		}
		
		if (!target.getType().equals(Material.MOB_SPAWNER)) {
			sendMessage(getName(), player, "Sorry you have to either be looking at a chest or a mobspawner");
			return;
		}
		
		SpawnerInfo info = new SpawnerInfo((CreatureSpawner)target.getState(), entity);
		
		if (args.length >= 2) {
			info.setRequiredPlayerRange(Integer.parseInt(args[1]));
		}
		
		if (args.length >= 3) {
			info.setMaxNearbyEntities(Integer.parseInt(args[1]));
		}
		
		if (args.length >= 4) {
			info.setDelay(Integer.parseInt(args[1]));
		}
		
		if (args.length >= 5) {
			info.setSpawnCount(Integer.parseInt(args[1]));
		}

		if (args.length >= 6) {
			info.setSpawnRange(Integer.parseInt(args[1]));
		}
		
		info.apply();
		sendMessage(getName(), player, "Set the properties of the mobspawner correctly");
	}

	private void handleEnchant(CommandSender sender, String command, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.DARK_PURPLE + "[bEnchant]" + ChatColor.WHITE + " sorry you have to be a player do do this");
		}
		
		Player player = (Player)sender;
		
		if (!hasPermission(player, "bEnchant.enchant")) {
			sendMessage(getName(), player, "Sorry you do not have permission to do this");
			return;
		}
		
		if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			
			for (Enchantment ench : Enchantment.values()) {
				sb.append(first ? "" : ", ").append(ench.getName().toLowerCase());
				first = false;
			}
			
			sendMessage(getName(), player, "Possible enchantments: " + sb.toString());
			return;
		}
		
		if (!(args.length == 3 || args.length == 4)) {
			sendMessage(getName(), player, "/enchant <add/set> <enchantment> <level>");
			return;
		}
		
		boolean add = args[0].equalsIgnoreCase("add");
		Enchantment enchantment = Enchantment.getByName(args[1].toUpperCase());
		int level = Integer.parseInt(args[2]);
		boolean unsafe = args.length == 4 ? args[3].equalsIgnoreCase("unsafe") : false;
		
		if (enchantment == null) {
			sendMessage(getName(), player, "Sorry that enchantment does not exist");
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			
			for (Enchantment ench : Enchantment.values()) {
				sb.append(first ? "" : ", ").append(ench.getName().toLowerCase());
				first = false;
			}
			
			sendMessage(getName(), player, "Possible enchantments: " + sb.toString());
			return;
		}

		ItemStack item = player.getItemInHand();
		
		if (item == null || item.getType().equals(Material.AIR)) {
			sendMessage(getName(), player, "Sorry you cannot edit that item");
			return;
		}
		
		if (add) {
			Map<Enchantment, Integer> enchantments = item.getEnchantments();
			
			if (enchantments.containsKey(enchantments)) {
				item.removeEnchantment(enchantment);
				
				addEnchantment(item, enchantment, level, unsafe, player);
			} else {
				addEnchantment(item, enchantment, level, unsafe, player);
			}
			sendMessage(getName(), player, "Added " + enchantment.getName().toLowerCase() + " to your item");
		} else {
			for (Map.Entry<Enchantment, Integer> ench : item.getEnchantments().entrySet()) {
				item.removeEnchantment(ench.getKey());
			}
			
			addEnchantment(item, enchantment, level, unsafe, player);
			
			sendMessage(getName(), player, "Set " + enchantment.getName().toLowerCase() + " to the only enchantment on your item");
		}
		
	}

	private void handleEditItem(CommandSender sender, String command, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.DARK_PURPLE + "[bEnchant]" + ChatColor.WHITE + " sorry you have to be a player do do this");
		}
		
		Player player = (Player)sender;
		
		if (!hasPermission(player, "bEnchant.edit.item")) {
			sendMessage(getName(), player, "Sorry you do not have permission to do this");
			return;
		}
		
		if (args.length < 2) {
			sendMessage(getName(), player, "/editItem <name/desc/owner/colour> <value>");
			return;
		}
		
		ItemStack item = player.getItemInHand();
		
		if (item == null || item.getType().equals(Material.AIR)) {
			sendMessage(getName(), player, "Sorry you cannot edit that item");
			return;
		}
	
		if (args[0].equalsIgnoreCase("owner")) {
			
			if (!item.getType().equals(Material.SKULL_ITEM)) {
				sendMessage(getName(), player, "Sorry you can only set the owners of skulls");
				return;
			}
			
			String owner = args[1];
			
			SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
			skullMeta.setOwner(owner);
			item.setItemMeta(skullMeta);
			
			sendMessage(getName(), player, "Your skull now has the head of " + owner);
			
			// allow American spelling just incase
		} else if (args[0].equalsIgnoreCase("colour") || args[0].equalsIgnoreCase("color")) {
			
			if (!isLeatherArmour(item)) {
				sendMessage(getName(), player, "Sorry you can only set the colour of leather armour");
				return;
			}
			
			if (args.length < 2) {
				sendMessage(getName(), player, "/editItem colour <colour/<r><g><b>>");
				return;
			}
			
			LeatherArmorMeta armorMeta = (LeatherArmorMeta)item.getItemMeta();
			

			Color colour;
			
			if (args.length == 2) {
				colour = matchColour(args[1]);
			} else if (args.length == 4) {
				
				int red = Integer.parseInt(args[1]);
				int green = Integer.parseInt(args[2]);
				int blue = Integer.parseInt(args[3]);
				colour = Color.fromRGB(red, green, blue);
			} else {
				sendMessage(getName(), player, "/editItem colour <colour/<r><g><b>>");
				return;
			}
			
			armorMeta.setColor(colour);
			item.setItemMeta(armorMeta);
			
			sendMessage(getName(), player, "Set item colour to " + colour.toString());
		} else {
			
			ItemMeta meta = item.getItemMeta();
			
			String value = "";
			
			for (int i = 1; i < args.length; i++) {
				value += args[i] + " ";
			}
			
			value = parseColour(value.trim());
			
			if (args[0].equalsIgnoreCase("name")) {
				meta.setDisplayName(value);
				sendMessage(getName(), player, "Your item has had its name updated");
			
			} else if (args[0].equalsIgnoreCase("desc") || args[0].equalsIgnoreCase("description")) {
				List<String> lore;
				if (meta.hasLore()) {
					lore = meta.getLore();
				} else {
					lore = new ArrayList<String>();
				}
				
				lore.add(value);
				meta.setLore(lore);
					
				sendMessage(getName(), player, "Your item has had its description updated");
			} else {
				sendMessage(getName(), player, "Invalid option use either name,colour,owner or description");
			}
			
			item.setItemMeta(meta);
		}
		
	}

	private void handleFirework(CommandSender sender, String command, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.DARK_PURPLE + "[bEnchant]" + ChatColor.WHITE + " sorry you have to be a player do do this");
		}
		
		Player player = (Player)sender;
		
		if (!hasPermission(player, "bEnchant.edit.firework")) {
			sendMessage(getName(), player, "Sorry you do not have permission to do this");
			return;
		}
		
		if (args.length < 2) {
			sendMessage(getName(), player, "/firework [-ft] <color> [type] [fade]");
			return;
		}
		
		ItemStack item = player.getItemInHand();
		
		if (item == null || !item.getType().equals(Material.FIREWORK)) {
			sendMessage(getName(), player, "Sorry you cannot edit that item");
			return;
		}
		
		Builder builder = FireworkEffect.builder();
		
		List<String> flags = new ArrayList<String>();
		int startArg = 0;
		if (args[0].startsWith("-")) {
			for(int i = 1; i < args[0].length()-1; i++) {
				flags.add(args[0].substring(i, i+1));
			}
			startArg++;
		}
		
		boolean flicker = flags.contains("f");
		boolean trail = flags.contains("t");
		
		Color color = matchColour(args[startArg]);
		Type type = Type.valueOf(args[startArg+1].toUpperCase());
		
		Color fadeTo = null;
		if (args.length >= startArg + 3) {
			fadeTo = matchColour(args[startArg+2]);
		}
		
		int power = 0;
		if (args.length == startArg + 4) {
			power = Integer.parseInt(args[startArg+3]);
		} else {
			power = ((FireworkMeta)item.getItemMeta()).getPower();
		}
		
		builder.flicker(flicker);
		builder.trail(trail);
		builder.withColor(color);
		
		if (fadeTo != null) {
			builder.withFade(fadeTo);
		}
		if (type != null) {
			builder.with(type);
		}
		
		FireworkEffect effect = builder.build();
		
		FireworkMeta meta = (FireworkMeta) item.getItemMeta();
		meta.clearEffects();
		meta.addEffect(effect);
		meta.setPower(power);
		item.setItemMeta(meta);
		
		sendMessage(getName(), player, "Added effect to firework");
	}

	private void handleFillDispenser(CommandSender sender, String command, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.DARK_PURPLE + "[bEnchant]" + ChatColor.WHITE + " sorry you have to be a player do do this");
		}
		
		Player player = (Player)sender;
		
		if (!hasPermission(player, "bEnchant.edit.fill")) {
			sendMessage(getName(), player, "Sorry you do not have permission to do this");
			return;
		}
		
		Block block = player.getTargetBlock(null, 100);
		
		if (block.getType() != Material.DISPENSER) {
			sendMessage(getName(), player, "Sorry you cannot fill that block");
			return;
		}
		
		boolean fromPlayer = false;
		
		if (args.length >= 1 && args[0].equalsIgnoreCase("-i")) {
			fromPlayer = true;
		}
		
		Dispenser dispenser = (Dispenser) block.getState();
		
		Inventory inv = dispenser.getInventory();
		ItemStack item = fromPlayer ? player.getItemInHand() : inv.getItem(0);
		
		if (item == null) {
			sendMessage(getName(), player, "You tried to fill the dispenser with air");
			return;
		}
		
		item.setAmount(64);
		
		for (int i = 0; i < inv.getSize(); i++) {
			inv.setItem(i, item);
		}
		
		sendMessage(getName(), player, "Filled that dispenser with " + item.getType().toString().toLowerCase());
	}

	/**
	 * Match a Craftbukkit colour based of a string input
	 * 
	 * @param string the string input
	 * @return the corresponding colour
	 */
	private Color matchColour(String string) {
		if (string.equalsIgnoreCase("AQUA")) {
			return Color.AQUA;
		} else if (string.equalsIgnoreCase("BLACK")) {
			return Color.BLACK;
		} else if (string.equalsIgnoreCase("BLUE")) {
			return Color.BLUE;
		} else if (string.equalsIgnoreCase("FUCHSIA")) {
			return Color.FUCHSIA;
		} else if (string.equalsIgnoreCase("GRAY")) {
			return Color.GRAY;
		} else if (string.equalsIgnoreCase("GREEN")) {
			return Color.GREEN;
		} else if (string.equalsIgnoreCase("MAROON")) {
			return Color.MAROON;
		} else if (string.equalsIgnoreCase("NAVY")) {
			return Color.NAVY;
		} else if (string.equalsIgnoreCase("OLIVE")) {
			return Color.OLIVE;
		} else if (string.equalsIgnoreCase("ORANGE")) {
			return Color.ORANGE;
		} else if (string.equalsIgnoreCase("PURPLE")) {
			return Color.PURPLE;
		} else if (string.equalsIgnoreCase("RED")) {
			return Color.RED;
		} else if (string.equalsIgnoreCase("SILVER")) {
			return Color.SILVER;
		} else if (string.equalsIgnoreCase("TEAL")) {
			return Color.TEAL;
		} else if (string.equalsIgnoreCase("WHITE")) {
			return Color.WHITE;
		} else if (string.equalsIgnoreCase("YELLOW")) {
			return Color.YELLOW;
		} else {
			return parseRGBColour(string);
		}
	}

	private Color parseRGBColour(String string) {
		String[] split = string.split(",");
		return Color.fromBGR(Integer.parseInt(split[2]), Integer.parseInt(split[1]), Integer.parseInt(split[0]));
	}

	private void addEnchantment(ItemStack item, Enchantment enchantment, int level, boolean unsafe, Player player) {
		if (unsafe) {
			item.addUnsafeEnchantment(enchantment, level);
		} else {
			try {
				item.addEnchantment(enchantment, level);
			} catch (Exception ex){
				sendMessage(getName(), player, ex.getMessage());
			}
		}
	}

	private String parseColour(String trim) {
		String formattedMessage = trim;
				
		while(formattedMessage.indexOf("&") != -1) {
					
			String code = formattedMessage.substring(formattedMessage.indexOf("&") + 1, formattedMessage.indexOf("&") + 2);
			formattedMessage = formattedMessage.substring(0, formattedMessage.indexOf("&")) +  ChatColor.getByChar(code) + formattedMessage.substring(formattedMessage.indexOf("&") + 2);
										
		}
		
		return formattedMessage;
	}

	private boolean isLeatherArmour(ItemStack item) {
		return item.getType().equals(Material.LEATHER_BOOTS) ||
				item.getType().equals(Material.LEATHER_CHESTPLATE) ||
				item.getType().equals(Material.LEATHER_HELMET) ||
				item.getType().equals(Material.LEATHER_LEGGINGS);
	}

}
