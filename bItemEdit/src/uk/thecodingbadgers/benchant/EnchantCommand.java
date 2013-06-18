package uk.thecodingbadgers.benchant;

import java.util.Map;

import org.apache.commons.lang.Validate;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import uk.codingbadgers.bFundamentals.commands.ModuleChildCommand;
import uk.codingbadgers.bFundamentals.commands.ModuleCommand;

public class EnchantCommand extends ModuleCommand {

	private enum EnchantOptions {
		ADD, SET, LIST;
	}

	public EnchantCommand() {
		super("benchant", "/benchant <add/set/list> <enchantment> <level>");
		setPermission("bitemedit.commands.enchant");
		setDescription("Allows you to add or set enchantments for any item");
		
		addChildCommand(new EnchantAddCommand(this));
		addChildCommand(new EnchantSetCommand(this));
		addChildCommand(new EnchantListCommand(this));
	}

	public class EnchantAddCommand extends ModuleChildCommand {

		public EnchantAddCommand(ModuleCommand parent) {
			super(parent, "add");
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			if (!(sender instanceof Player)) {
				sendMessage(sender, "You have to be a player to use this command.");
			}

			Player player = (Player) sender;

			if (!bItemEdit.hasPermission(player, getPermission())) {
				sendMessage(player, "Sorry you do not have permission to do this");
				return true;
			}

			if (args.length < 2) {
				bItemEdit.sendMessage(m_module.getName(), player, getUsage());
				return true;
			}

			Enchantment ench = Enchantment.getByName(args[0]);

			if (ench == null) {
				sendMessage(player, "That enchantment does not exist");
				handleEnchantment(player, EnchantOptions.LIST, null, 0);
				return true;
			}

			int level = Integer.parseInt(args[1]);

			handleEnchantment(player, EnchantOptions.ADD, ench, level);
			return true;
		}
	}

	public class EnchantSetCommand extends ModuleChildCommand {

		public EnchantSetCommand(ModuleCommand parent) {
			super(parent, "set");
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			if (!(sender instanceof Player)) {
				bItemEdit.sendMessage(m_module.getName(), sender, "You have to be a player to use this command.");
			}

			Player player = (Player) sender;

			if (!bItemEdit.hasPermission(player, getPermission())) {
				sendMessage(player, "Sorry you do not have permission to do this");
				return true;
			}

			if (args.length < 2) {
				sendMessage(player, getUsage());
				return true;
			}

			Enchantment ench = Enchantment.getByName(args[0]);

			if (ench == null) {
				sendMessage(player, "That enchantment does not exist");
				handleEnchantment(player, EnchantOptions.LIST, null, 0);
				return true;
			}

			int level = Integer.parseInt(args[1]);

			handleEnchantment(player, EnchantOptions.SET, ench, level);
			return true;
		}
	}

	public class EnchantListCommand extends ModuleChildCommand {

		public EnchantListCommand(ModuleCommand parent) {
			super(parent, "list");
		}

		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			if (!(sender instanceof Player)) {
				bItemEdit.sendMessage(m_module.getName(), sender, "You have to be a player to use this command.");
			}

			Player player = (Player) sender;

			handleEnchantment(player, EnchantOptions.LIST, null, 0);
			return true;
		}
	}

	public void handleEnchantment(Player player, EnchantOptions options, Enchantment enchantment, int level) {
		Validate.isTrue(options != EnchantOptions.LIST && enchantment != null, "Enchantment cannot be null");
		Validate.isTrue(options != EnchantOptions.LIST && level >= 0, "Enchanting level must be positive");

		ItemStack item = player.getItemInHand();

		if (item == null || item.getType().equals(Material.AIR)) {
			sendMessage(player, "Sorry you cannot edit that item");
			return;
		}

		switch (options) {
		case ADD:
			Map<Enchantment, Integer> enchantments = item.getEnchantments();

			if (enchantments.containsKey(enchantments)) {
				item.removeEnchantment(enchantment);
			}
			item.addUnsafeEnchantment(enchantment, level);

			sendMessage(player, "Added " + enchantment.getName().toLowerCase() + " to your item");
			break;
		case SET:
			for (Map.Entry<Enchantment, Integer> ench : item.getEnchantments().entrySet()) {
				item.removeEnchantment(ench.getKey());
			}

			item.addUnsafeEnchantment(enchantment, level);
			sendMessage(player, "Set " + enchantment.getName().toLowerCase() + " to the only enchantment on your item");
			break;
		case LIST:
			StringBuilder sb = new StringBuilder();
			boolean first = true;

			for (Enchantment ench : Enchantment.values()) {
				sb.append(first ? "" : ", ").append(ench.getName().toLowerCase());
				first = false;
			}

			sendMessage(player, "Possible enchantments: " + sb.toString());
			break;
		default:
			break;
		}
	}
}
