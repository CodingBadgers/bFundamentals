package uk.codingbadgers.bconomy;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.logging.Level;

import n3wton.me.BukkitDatabaseManager.Database.BukkitDatabase;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bconomy.account.Account;
import uk.codingbadgers.bconomy.account.PlayerAccounts;
import uk.codingbadgers.bconomy.config.Config;

/**
 * The Class Global.
 */
public class Global {
	
	/** The m_plugin. */
	private static bFundamentals m_plugin = null;
	
	/** The m_accounts. */
	private static PlayerAccounts m_accounts = null;
	
	/** The m_database. */
	public static BukkitDatabase m_database = null;
	
	/** The permission. */
	private static Permission m_permission = null;
	
	/** The next stored ID */
	private static int m_nextStoredID = 0;
	
	/** the module instance */
	private static bConomyModule m_module = null;

	/**
	 * get the JavaPlugin instance.
	 *
	 * @return the plugin instance
	 */
	public static bFundamentals getPlugin() {
		return m_plugin;
	}
	
	/**
	 * Set the plugin instance.
	 *
	 * @param plugin - the plugin instance to use
	 */
	public static void setPlugin(bFundamentals plugin) {
		m_plugin = plugin;
		m_accounts = new PlayerAccounts();
	}
	
	/**
	 * get the Module instance.
	 *
	 * @return the module instance
	 */
	public static bConomyModule getModule() {
		return m_module;
	}
	
	/**
	 * Set the module instance.
	 *
	 * @param plugin - the module instance to use
	 */
	public static void setModule(bConomyModule module) {
		m_module = module;
	}
	
	/**
	 * Output a message to console.
	 *
	 * @param message to output
	 */
	public static void outputToConsole(String message) {
		
		m_module.log(Level.INFO, message);
	}

	/**
	 * Get the accounts array.
	 *
	 * @return the accounts array
	 */
	public static PlayerAccounts getAccounts() {
		return m_accounts;
	}

	/**
	 * Set the accounts array.
	 *
	 * @param m_accounts - the array to set it to
	 */
	public static void setAccounts(PlayerAccounts m_accounts) {
		Global.m_accounts = m_accounts;
	}

	/**
	 * Add a account to the array.
	 *
	 * @param account to add
	 */
	public static void addAccout(Account account) {
		m_accounts.add(account);	
	}
	
	/**
	 * Format a number to a usable string.
	 *
	 * @param amount the amount
	 * @return the string
	 */
	public static String format(double amount) {
		DecimalFormat format = new DecimalFormat(Config.m_currency.format);
		String formatted = format.format(amount);
		
		if (formatted.endsWith("."))
			formatted = formatted.substring(0, formatted.length() - 1);
		
		return formatted;
	}
	
	/**
	 * Gets the server.
	 *
	 * @return the server
	 */
	public static Server getServer() {
		return m_plugin.getServer();
	}

	/**
	 * Output a formatted message to a sender (console or player).
	 *
	 * @param sender the sender
	 * @param message the message
	 */
	public static void output(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.GOLD + "[bConomy] " + ChatColor.WHITE + message);
	}
	
	/**
	 * Output a formated message to a player
	 *
	 * @param player the player
	 * @param message the message
	 */
	public static void output(OfflinePlayer player, String message) {
		if (player.isOnline()) {
			player.getPlayer().sendMessage(ChatColor.GOLD + "[bConomy] " + ChatColor.WHITE +  message);
		}
	}
	
	/**
	 * Broadcasts a formated message to all players on the server
	 * 
	 * @param message the message
	 */
	public static void broadcast(String message) {
		Bukkit.broadcastMessage(ChatColor.GOLD + "[bConomy] " + ChatColor.WHITE + message);
	}
	
	
	/**
	 * Checks for permission.
	 *
	 * @param sender the sender
	 * @param perm the perm
	 * @param verbose the verbose
	 * @return true, if successful
	 */
	public static boolean hasPermission(CommandSender sender, String perm, boolean verbose) {
		
		// Are we console?
		if (!(sender instanceof Player))
			return true;
		
		// Are we an op?
		Player player = (Player)sender;
		if (player.isOp() == true)
			return true;
		
		// use vault to check perms
		if (m_permission.has(sender, perm)){
			return true;
		}
		
		// by now they don't have perms so see if we are outputting a message
		if (verbose) {
			Global.output(sender, getLanguage("NO-PERMISSION"));
		}
		
		return false;		
	}	
	
	/**
	 * setup the vault permissions
	 * 
	 * @return whether it was successful
	 */
	public static boolean setupPermissions() {
		if (getServer().getPluginManager().getPlugin("Vault") == null)
			return false;
		
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        m_permission = rsp.getProvider();
        return m_permission != null;
    }

	public static int getNextId() {
		
		// if we have a stored ID increment and return
		if (m_nextStoredID != 0) {
			m_nextStoredID = m_nextStoredID + 1;
			return m_nextStoredID;
		}
		
		// work out the next ID by spinning through all the accounts
		int nextId = 0;
		Iterator<Account> it = m_accounts.iterator();
		
		while (it.hasNext()) {
			Account acc = it.next();
			if (acc.getId() > nextId)
				nextId = acc.getId() + 1;
		}
		
		// store the id, so we don't have to spin all the time
		m_nextStoredID = nextId;
		
		return nextId;
	}

	public static String getLanguage(String string) {
		return m_module.getLanguageValue(string);
	}
	
}
