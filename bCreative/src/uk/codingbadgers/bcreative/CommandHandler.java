package uk.codingbadgers.bcreative;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import uk.codingbadgers.bcreative.containers.GamemodePlayer;

public class CommandHandler {

	public boolean onCommand(CommandSender sender, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("bcreative")) {
			
			if (args.length == 0) {
				sender.sendMessage(ChatColor.DARK_PURPLE + "[bCreative]" + ChatColor.WHITE + " /bcreative");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("monitor")) {
				
				if (args.length != 2) {
					sender.sendMessage(ChatColor.DARK_PURPLE + "[bCreative] " + ChatColor.WHITE + "/bcreative monitor <name>");
					return true;
				}
				
				GamemodePlayer target = bCreative.getPlayerManager().getPlayer(args[1]);
				
				if (target == null) {
					sender.sendMessage(ChatColor.DARK_PURPLE + "[bCreative] " + ChatColor.WHITE + "Sorry that player is not online");
					return true;
				}
				
				target.setMonitor(!target.isMonitor());
				sender.sendMessage(ChatColor.DARK_PURPLE + "[bCreative] " + ChatColor.WHITE + target.getPlayer().getName() + " is now " + (target.isMonitor() ? "being" : "not being") + " monitored");
				return true;
			}
		}
		
		return false;
	}
}
