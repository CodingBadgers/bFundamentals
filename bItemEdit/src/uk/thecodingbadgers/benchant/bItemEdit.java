package uk.thecodingbadgers.benchant;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

public class bItemEdit extends Module {

	@Override
	public void onEnable() {
		registerCommand(new EnchantCommand());
		registerCommand(new ItemEditCommand());
		//registerCommand(new ModuleCommand("Firework", "/firework [-ft] <color> <type> [fade]").setDescription("Allows you to edit fireworks properties and effects"));
		//registerCommand(new ModuleCommand("fillDispenser", "/fillDispenser").setDescription("Allows you to fill a dispenser"));
		
		this.log(Level.INFO, getName() + " v: " + getDesciption().getVersion() + " has been enabled successfuly");
	}

	@Override
	public void onDisable() {
		this.log(Level.INFO, getName() + " v: " + getDesciption().getVersion() + " has been disabled successfuly");
	}
	
	/*
	@Override
	public boolean onCommand(CommandSender sender, String command, String[] args) {
		if (command.equalsIgnoreCase("firework")) {
			handleFirework(sender, command, args);
			return true;
		} else if (command.equalsIgnoreCase("fillDispenser")) {
			handleFillDispenser(sender, command, args);
			return true;
		}
		
		return false;
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
		
		if (item == null || item.getType() == Material.AIR) {
			sendMessage(getName(), player, "You tried to fill the dispenser with air");
			return;
		}
		
		item.setAmount(64);
		
		for (int i = 0; i < inv.getSize(); i++) {
			if (inv.getItem(i) == null) {
				inv.setItem(i, item);
			} else {
				ItemStack stack = inv.getItem(i);
				stack.setAmount(64);
				inv.setItem(i, stack);
			}
		}
		
		sendMessage(getName(), player, "Filled that dispenser with " + item.getType().toString().toLowerCase());
	}

	private boolean isLeatherArmour(ItemStack item) {
		return item.getType().equals(Material.LEATHER_BOOTS) ||
				item.getType().equals(Material.LEATHER_CHESTPLATE) ||
				item.getType().equals(Material.LEATHER_HELMET) ||
				item.getType().equals(Material.LEATHER_LEGGINGS);
	}*/

}
