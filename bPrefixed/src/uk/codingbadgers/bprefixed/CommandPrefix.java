package uk.codingbadgers.bprefixed;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

public class CommandPrefix extends ModuleCommand {

	final private bPrefixed m_module;
	
	public CommandPrefix(bPrefixed module) {
		super("prefix", "/prefix <player> <prefix>");
		m_module = module;
	}
	
	/**
	 * Called when the 'prefix' command is executed.
	 */
	public boolean onCommand(CommandSender sender, String label, String[] args) {

		if (args.length != 2) {
			return false;
		}
		
		if ((sender instanceof Player) && !Module.hasPermission((Player)sender, "bprefixed.set")) {
			return true;
		}
		
		final String playerName = args[0];
		String prefix = args[1];
				
		OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
		
		if (!player.hasPlayedBefore()) {
			Module.sendMessage("bPrefixed", sender, "The player '" + playerName + "' seems not to exist.");		
			return true;
		}
		
		if (prefix.equalsIgnoreCase("clear")) {
			m_module.removePrefix(playerName);
			Player onlinePlayer = player.getPlayer();
			onlinePlayer.setDisplayName(onlinePlayer.getName());
			return true;
		}
		
		prefix = ChatColor.translateAlternateColorCodes('&', prefix);
		
		Module.sendMessage("bPrefixed", sender, "Setting '" + playerName + "s' prefix to '" + prefix + ChatColor.WHITE + "'.");		
		
		m_module.setPrefix(playerName, prefix);
		
		if (player.isOnline()) {
			Player onlinePlayer = player.getPlayer();
			final String prefixedName = prefix + " " + onlinePlayer.getName();
			onlinePlayer.setDisplayName(prefixedName);
		}
		
		return true;
	}

}
