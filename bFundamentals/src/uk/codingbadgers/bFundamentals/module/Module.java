package uk.codingbadgers.bFundamentals.module;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
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

import uk.codingbadgers.bFundamentals.ModuleCommand;
import uk.codingbadgers.bFundamentals.bFundamentals;

public abstract class Module extends Loadable implements Listener {

	protected final bFundamentals m_plugin;
	protected FileConfiguration m_config;
	protected File m_configFile = null;
	private String m_version = null;
	private String m_name = null;
	private HashMap<String, String> m_languageMap = new HashMap<String, String>();
	protected List<ModuleCommand> m_commands = new ArrayList<ModuleCommand>();
	
	protected static BukkitDatabase m_database = null;
	private static Permission m_permissions = null;
	
	public Module(String name, String version) {
		super(name);
		m_version = version;
		m_plugin = bFundamentals.getInstance();
		m_database = bFundamentals.getBukkitDatabase();
		m_permissions = bFundamentals.getPermissions();
		m_name = name;
	}
	
	protected void loadLanguageFile() {
		File languageFile = new File(getDataFolder() + File.separator + m_name + "_" + m_plugin.getConfigurationManager().getLanguage() + ".lang");
		
		if (!languageFile.exists()) {
			log(Level.SEVERE, "Missing language file '" + languageFile.getAbsolutePath() + "'!");
			return;
		}
		
		log(Level.INFO, "Loading Language File: " + languageFile.getName());
		
		try {
			FileInputStream fstream = new FileInputStream(languageFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String line = null;
			String key = null;
			while ((line = br.readLine()) != null)   {
				
				if (line.isEmpty())
					continue;
				
				if (line.startsWith("#")) {
					key = line.substring(1);
					continue;
				}
				
				if (key == null) {
					log(Level.WARNING, "Trying to parse a language value, with no key set!");
					continue;
				}
				
				log(Level.INFO, key + ", " + line);
				m_languageMap.put(key, line);				
			}
			
			br.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	public void log(Level level, String string) {
		bFundamentals.log(level, "[" + super.getName() + "] " + string);
	}
	
	public final void register(Listener listener) {
		m_plugin.getServer().getPluginManager().registerEvents(listener, m_plugin);
	}
	
	public Permission getPermissions() {
		return m_permissions;
	}
	
	public abstract void onEnable();
	
	public abstract void onDisable();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return false;
	}
	
	public boolean onCommand(Player sender, String label, String[] args){
		return false;
	}
	
	public String getVersion() {
		return m_version;
	}
	
	public static boolean hasPermission(final Player player, final String node) {
		if (m_permissions.has(player, node)) {
			return true;
		}
		return false;
	}
	
	public static void sendMessage(String name, Player player, String message) {
		player.sendMessage(ChatColor.DARK_PURPLE + "[" + name + "] " + ChatColor.RESET + message);
	}
	
	public boolean isCommandRegistered(String command) {
		for (ModuleCommand cmd : m_commands) {
			if (cmd.equals(command))
				return true;
		}
		return false;
	}

	public String getLanguageValue(String key) {
		return m_languageMap.get(key);
	}
	
	protected void registerCommand(ModuleCommand command) {
		m_commands.add(command);
	}

}