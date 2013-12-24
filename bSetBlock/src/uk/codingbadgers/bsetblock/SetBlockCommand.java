package uk.codingbadgers.bsetblock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

public class SetBlockCommand extends ModuleCommand {

	public SetBlockCommand() {
		super("setblock", "/setblock <x> <y> <z> <block> <data>");
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (args.length < 4) {
			return false;
		}
		
		if (!(sender instanceof Player || sender instanceof BlockCommandSender)) {
			Module.sendMessage("bSetBlock", sender, "This command can only be executed by players or command blocks");
		}
		
		int x = Integer.valueOf(args[0]);
		int y = Integer.valueOf(args[1]);
		int z = Integer.valueOf(args[2]);
		
		Material mat = getMaterial(args[3]);
		
		byte data = 0;
		
		if (args.length == 5) {
			data = Byte.valueOf(args[4]);
		}
		
		if (mat == null) {
			Module.sendMessage("bSetBlock", sender, "The block " + args[3] + " is not known to bukkit");
			return true;
		}
		
		Location loc = new Location(getWorld(sender), x, y, z);
		Block block = loc.getBlock();
		
		block.setType(mat);
		block.setData((byte) data);
		Module.sendMessage("bSetBlock", sender, "The block at (" + x + "," + y + "," + z + ") has been set to " + mat.name().toLowerCase());
		
		return true;
	}

	private World getWorld(CommandSender sender) {
		if (sender instanceof BlockCommandSender) {
			return ((BlockCommandSender) sender).getBlock().getWorld();
		} else if (sender instanceof Player) {
			return ((Player) sender).getWorld();
		}
		
		return null;
	}

	@SuppressWarnings("deprecation")
	private Material getMaterial(String string) {
		try {
			int id = Integer.parseInt(string);
			return Material.getMaterial(id);
		} catch (NumberFormatException ex) {
			return Material.getMaterial(string.toUpperCase());
		}
	}

}
