package uk.codingbadgers.bsocial;

import java.util.logging.Level;

import net.milkbowl.vault.chat.Chat;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bsocial.chanels.ChannelManager;
import uk.codingbadgers.bsocial.chanels.ChatChannel;
import uk.codingbadgers.bsocial.config.ConfigManager;
import uk.codingbadgers.bsocial.listeners.ChatListener;

/**
 * The Class bSocial.
 */
public class bSocial extends Module{

	/** The Constant NAME. */
	public static final String NAME = "bSocial";
	
	/** The Constant VERSION. */
	public static final String VERSION = "1.0";
	
	/** The plugin. */
	public static bFundamentals PLUGIN = null;
	
	/** The module. */
	public static bSocial MODULE = null;
	
	/** The chat instance. */
	private static Chat m_chat = null;
	
	/** The channel manager. */
	private static ChannelManager m_channelManager = null;
	
	/** The config manager. */
	private static ConfigManager m_configManager = null;
	
	/**
	 * Instantiates a new b social.
	 */
	public bSocial() {
		super(NAME, VERSION);
	}

	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.Module#onEnable()
	 */
	@Override
	public void onEnable() {
		MODULE = this;
		PLUGIN = m_plugin;
		
		setupChat();
		
		m_channelManager = new ChannelManager();
		
		m_configManager = new ConfigManager();
		m_configManager.loadConfig();
		
		registerCommand(new ModuleCommand("chat", "/chat <command>"));
		registerCommand(new ModuleCommand("pm", "/pm <player> <message>"));
		
		for (ChatChannel channel : m_channelManager.getChannels()) {
			registerCommand(new ModuleCommand(channel.getChannelName(), "/" + channel.getChannelName() + " <message>"));
		}
		
		register(new ChatListener());
		
		log(Level.INFO, NAME + " v: " + VERSION + " is enabled");
	}

	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.Module#onDisable()
	 */
	@Override
	public void onDisable() {
		m_channelManager.clear();
		log(Level.INFO, NAME + " v: " + VERSION + " is disabled");
	}
	
	 /**
 	 * Setup chat.
 	 *
 	 * @return true, if successful
 	 */
 	private boolean setupChat() {
	       RegisteredServiceProvider<Chat> rsp = m_plugin.getServer().getServicesManager().getRegistration(Chat.class);
	       m_chat = rsp.getProvider();
	       return m_chat != null;
	 }
	 
	 /**
 	 * Gets the vault chat.
 	 *
 	 * @return the vault chat
 	 */
 	public static Chat getVaultChat() {
		 return m_chat;
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
	 
	 /* (non-Javadoc)
 	 * @see uk.codingbadgers.bFundamentals.module.Module#onCommand(org.bukkit.entity.Player, java.lang.String, java.lang.String[])
 	 */
 	public boolean onCommand(Player sender, String command, String[] args) {
		 return CommandHandler.onCommand(sender, command, args);
	 }

}
