package uk.codingbadgers.bsocial;

import java.util.logging.Level;

import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bsocial.chanels.ChannelManager;
import uk.codingbadgers.bsocial.chanels.ChatChannel;
import uk.codingbadgers.bsocial.config.ConfigManager;
import uk.codingbadgers.bsocial.listeners.ChatListener;
import uk.codingbadgers.bsocial.players.PlayerManager;

/**
 * The Class bSocial.
 */
public class bSocial extends Module{
	
	/** The plugin. */
	public static bFundamentals PLUGIN = null;
	
	/** The module. */
	public static bSocial MODULE = null;
	
	/** The channel manager. */
	private static ChannelManager m_channelManager = null;
	
	/** The config manager. */
	private static ConfigManager m_configManager = null;
	
	/** The player manager. */
	private static PlayerManager m_playerManager = null;

	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.Module#onLoad()
	 */
	public void onLoad() {
		MODULE = this;
		PLUGIN = m_plugin;
	}
	
	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.Module#onEnable()
	 */
	@Override
	public void onEnable() {		
		m_channelManager = new ChannelManager();
		m_playerManager = new PlayerManager();		
		m_configManager = new ConfigManager();
		
		m_configManager.loadConfig();
		
		registerCommand(new ModuleCommand("chat", "/chat <command>"));
		registerCommand(new ModuleCommand("pm", "/pm <player> <message>"));
		
		for (ChatChannel channel : m_channelManager.getChannels()) {
			registerCommand(new ModuleCommand(channel.getChannelName(), "/" + channel.getChannelName() + " <message>"));
		}
		
		register(new ChatListener());
		
		log(Level.INFO, getName() + " v: " + getVersion() + " is enabled");
	}

	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.Module#onDisable()
	 */
	@Override
	public void onDisable() {
		m_channelManager.clear();
		log(Level.INFO, getName() + " v: " + getVersion() + " is disabled");
	}
	 
	 /**
 	  * Gets the channel manager.
 	  *
 	  * @return the channel manager
 	  */
 	public static ChannelManager getChannelManager() {
		 return m_channelManager;
	 }
	 
	 /**
 	  * Gets the config manager.
 	  *
 	  * @return the config manager
 	  */
 	public static ConfigManager getConfigManager() {
		 return m_configManager;
	}
 	
 	/**
	  * Gets the player manager.
	  *
	  * @return the player manager
	  */
	 public static PlayerManager getPlayerManager() {
 		return m_playerManager;
 	}
	 
	 /* (non-Javadoc)
 	 * @see uk.codingbadgers.bFundamentals.module.Module#onCommand(org.bukkit.entity.Player, java.lang.String, java.lang.String[])
 	 */
 	public boolean onCommand(Player sender, String command, String[] args) {
		 return CommandHandler.onCommand(sender, command, args);
	 }

}
