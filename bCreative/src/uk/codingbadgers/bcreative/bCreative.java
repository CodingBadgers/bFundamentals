package uk.codingbadgers.bcreative;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bFundamentals.update.BadgerUpdater;
import uk.codingbadgers.bcreative.containers.PlayerManager;
import uk.codingbadgers.bcreative.containers.WorldManager;
import uk.codingbadgers.bcreative.listeners.BlockListener;
import uk.codingbadgers.bcreative.listeners.GamemodePlayerListener;
import uk.codingbadgers.bcreative.listeners.PlayerListener;
import uk.codingbadgers.bcreative.listeners.TeleportListener;

public class bCreative extends Module {

	public static final String NAME = "bCreative";
	public static final String VERSION = "1.2";
	
	private static bCreative m_instance = null;
	
	private static ConfigManager m_configManager = null;
	private static WorldManager m_worldManager = null;
	private static PlayerManager m_playerManager = null;
	private static CommandHandler m_commmandHandler = null;
	
	private PlayerListener m_playerListener = null;
	private TeleportListener m_tpListener = null;
	private BlockListener m_blockListener = null;
	private GamemodePlayerListener m_gmPlayerListener = null;
	
	public bCreative() {
		super(NAME, VERSION);
	}
	
	public void onLoad() {
		m_instance = this;
	}
	
	@Override
	public void onEnable() {
		m_configManager = new ConfigManager();
		m_worldManager = new WorldManager();
		m_playerManager = new PlayerManager();
		m_commmandHandler = new CommandHandler();

		m_configManager.loadConfig();
		
		m_playerListener = new PlayerListener();
		m_tpListener = new TeleportListener();
		m_blockListener = new BlockListener();
		m_gmPlayerListener = new GamemodePlayerListener();
				
		register(m_playerListener);
		register(m_tpListener);
		register(m_blockListener);
		register(m_gmPlayerListener);
		
		registerCommand((new ModuleCommand("bcreative", "/bcreative")).setHelp("Access all bCreative commands"));
		
		setUpdater(new BadgerUpdater(this));
		
		log(Level.INFO, NAME + " has been enabled successfuly");
	}

	@Override
	public void onDisable() {
		log(Level.INFO, NAME + " has been disabled successfuly");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, String commandLabel, String[] args) {
		return m_commmandHandler.onCommand(sender, commandLabel, args);
	}

	public static bCreative getInstance() {
		return m_instance;
	}
	
	public static ConfigManager getConfigmanager() {
		return m_configManager;
	}

	public static WorldManager getWorldManager() {
		return m_worldManager;
	}
	
	public static PlayerManager getPlayerManager() {
		return m_playerManager;
	}

}
