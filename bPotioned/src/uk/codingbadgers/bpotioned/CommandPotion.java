package uk.codingbadgers.bpotioned;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

public class CommandPotion extends ModuleCommand {

	final private bPotioned m_module;
	
	public CommandPotion(bPotioned instance) {
		super("potion", "/potion <effect> [length] [strength]");
		m_module = instance;
	}
	
	private void listPotions(Player player) {
		Module.sendMessage("bPotioned", player, "The following potions are availible...");
		for (PotionEffectType potion : PotionEffectType.values()) {
			if (potion == null) {
				continue;
			}
			Module.sendMessage("bPotioned", player, " - " + potion.getName().toLowerCase());
		}
	}
	
	/**
	 * Called when the 'potion' command is executed.
	 */
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		
		if (!(sender instanceof Player))
			return true;
		
		Player player = (Player)sender;
		
		if (!Module.hasPermission(player, "bpotion.self")) {
			return true;
		}
		
		if (args.length == 0 || args.length > 3) {
			listPotions(player);
			return true;
		}
		
		final PotionEffectType effectType = PotionEffectType.getByName(args[0].toUpperCase());
		if (effectType == null) {
			if (args[0].equalsIgnoreCase("clear")) {
				for (PotionEffectType potion : PotionEffectType.values()) {
					if (potion == null) {
						continue;
					}
					player.removePotionEffect(potion);
				}
				return true;
			}
			listPotions(player);
			return true;
		}		
		
		int length = 72000;
		if (args.length >= 2) {
			length = Integer.parseInt(args[1]);
			if (length == -1) {
				length = 72000;
			}
			else {
				length = length * 20;
			}
		}
		
		int level = 0;
		if (args.length >= 3) {
			level = Integer.parseInt(args[2]);
		}
		
		PotionEffect effect = new PotionEffect(effectType, length, level, true);		
		player.addPotionEffect(effect, true);
		
		Module.sendMessage("bPotioned", player, "You have been given...");
		Module.sendMessage("bPotioned", player, effectType.getName().toLowerCase() + ", lvl " + level + " for " + (length/20) + " seconds.");
		
		return true;		
	}

}
