package uk.codingbadgers.bFundamentals.module;

import java.io.File;
import java.util.logging.Level;

import n3wton.me.BukkitDatabaseManager.Database.BukkitDatabase;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.nodinchan.ncbukkit.loader.Loadable;

import uk.codingbadgers.bFundamentals.bFundamentals;

public abstract class Module extends Loadable implements Listener {

	protected final bFundamentals plugin;
	protected FileConfiguration config;
	protected File configFile;
	private String m_version;
	
	protected static BukkitDatabase database = null;
	private static Permission permissions = null;
	
	public Module(String name, String version) {
		super(name);
		m_version = version;
		this.plugin = bFundamentals.getInstance();
		database = bFundamentals.getBukkitDatabase();
		permissions = bFundamentals.getPermissions();
		configFile = new File(getDataFolder() + File.separator  + "config.yml");
	}
	
	public void log(Level level, String string) {
		bFundamentals.log(level, "[" + super.getName() + "] " + string);
	}
	
	public final void register(Listener listener) {
		plugin.getServer().getPluginManager().registerEvents(listener, plugin);
	}
	
	public abstract void onEnable();
	
	public abstract void onDisable();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return false;
	}
	
	public String getVersion() {
		return m_version;
	}
	
	public static boolean hasPermission(final Player player, final String node) {
		if (permissions.has(player, node)) {
			return true;
		}
		return false;
	}
	
	public static void sendMessage(String name, Player player, String message) {
		player.sendMessage(ChatColor.DARK_PURPLE + "[" + name + "] " + ChatColor.RESET + message);
	}

}