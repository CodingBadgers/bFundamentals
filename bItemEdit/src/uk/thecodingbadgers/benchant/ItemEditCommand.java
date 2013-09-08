/**
 * bFundamentalsBuild 1.2-SNAPSHOT
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
package uk.thecodingbadgers.benchant;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import uk.codingbadgers.bFundamentals.commands.ModuleChildCommand;
import uk.codingbadgers.bFundamentals.commands.ModuleCommand;

public class ItemEditCommand extends ModuleCommand {

	public ItemEditCommand() {
		super("itemedit", "/itemedit <name/desc/owner/colour> <value>");
		setPermission("bitemedit.commands.edit");
		setDescription("Allows you to edit custom data for items");
		addAliase("edititem");
		
		addChildCommand(new EditColourCommand(this));
		addChildCommand(new EditNameCommand(this));
		addChildCommand(new EditLoreCommand(this));
		addChildCommand(new EditOwnerCommand(this));
	}
	
	public class EditColourCommand extends ModuleChildCommand {
		public EditColourCommand(ModuleCommand parent) {
			super(parent, "colour");
			addAliase("color");
		}
		
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			if (!(sender instanceof Player)) {
				sendMessage(sender, "You have to be a player to use this command.");
				return true;
			}

			Player player = (Player) sender;

			if (args.length < 1) {
				sendMessage(player, "/editItem colour <colour/<r>,<g>,<b>>");
				return true;
			}
			
			ItemStack item = player.getItemInHand();
			ItemMeta meta = item.getItemMeta();
			
			if (!(meta instanceof LeatherArmorMeta)) {
				sendMessage(player, "You cannot colour that item, only leather armour can be coloured");
				return true;
			}
			
			LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
			Color color = matchColour(args[0]);
			
			if (color == null) {
				sendMessage(player, "The colour string " + args[0] + " is not a valid colour.\nUse either a colour name (not all colours are currently supported) or a rgb value in the format <r>,<g>,<b>");
				return true;
			}
			
			armorMeta.setColor(color);
			item.setItemMeta(armorMeta);
			player.setItemInHand(item);
			sendMessage(player, "Set your armour to " + color.toString());
			return true;
		}
	}
	
	public class EditNameCommand extends ModuleChildCommand {
		public EditNameCommand(ModuleCommand parent) {
			super(parent, "name");
		}
		
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			if (!(sender instanceof Player)) {
				sendMessage(sender, "You have to be a player to use this command.");
				return true;
			}

			Player player = (Player) sender;

			if (args.length < 1) {
				sendMessage(player, "/editItem name <name>");
				return true;
			}
			
			ItemStack item = player.getItemInHand();
			ItemMeta meta = item.getItemMeta();	
			
			String name = args[0];		
			meta.setDisplayName(parseColour(name));
			item.setItemMeta(meta);
			player.setItemInHand(item);
			sendMessage(player, "Set your item's name to " + name);
			return true;
		}
	}
	
	public class EditLoreCommand extends ModuleChildCommand {
		public EditLoreCommand(ModuleCommand parent) {
			super(parent, "lore");
		}
		
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			if (!(sender instanceof Player)) {
				sendMessage(sender, "You have to be a player to use this command.");
				return true;
			}

			Player player = (Player) sender;

			if (args.length < 1) {
				sendMessage(player, "/editItem lore <text>");
				return true;
			}
			
			ItemStack item = player.getItemInHand();
			ItemMeta meta = item.getItemMeta();		
			
			String lore = parseColour(args[0]);
			meta.setLore(Arrays.asList(lore.split(";")));	
			item.setItemMeta(meta);	
			player.setItemInHand(item);
			sendMessage(player, "Set your item's name to " + lore);
			return true;
		}
	}
	
	public class EditOwnerCommand extends ModuleChildCommand {
		public EditOwnerCommand(ModuleCommand parent) {
			super(parent, "owner");
		}
		
		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			if (!(sender instanceof Player)) {
				sendMessage(sender, "You have to be a player to use this command.");
				return true;
			}

			Player player = (Player) sender;

			if (args.length < 1) {
				sendMessage(player, "/editItem owner <name>");
				return true;
			}
			
			ItemStack item = player.getItemInHand();
			ItemMeta meta = item.getItemMeta();		
			
			if (!(meta instanceof SkullMeta)) {
				sendMessage(player, "You can only set the owner of skulls");
				return true;
			}
			
			SkullMeta skullmeta = (SkullMeta) meta;
			skullmeta.setOwner(args[0]);
			item.setItemMeta(skullmeta);
			player.setItemInHand(item);
			sendMessage(player, "Set your skull's owner to " + args[0]);
			return true;
		}
	}

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
		
		if (split.length != 3) {
			return null;
		}
		
		return Color.fromBGR(Integer.parseInt(split[2]), Integer.parseInt(split[1]), Integer.parseInt(split[0]));
	}
	
	private String parseColour(String trim) {
		String formattedMessage = trim;
				
		while(formattedMessage.indexOf("&") != -1) {				
			String code = formattedMessage.substring(formattedMessage.indexOf("&") + 1, formattedMessage.indexOf("&") + 2);
			formattedMessage = formattedMessage.substring(0, formattedMessage.indexOf("&")) +  ChatColor.getByChar(code) + formattedMessage.substring(formattedMessage.indexOf("&") + 2);				
		}
		
		return formattedMessage;
	}
}
