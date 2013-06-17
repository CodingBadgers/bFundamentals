package uk.codingbadgers.bHelpful;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bFundamentals.update.BadgerUpdater;
import uk.codingbadgers.bHelpful.commands.*;

/**
 * The base bHelpful module class
 *
 * @author James
 */
public class bHelpful extends Module {
	
	/** The plugin instance. */
	public static bFundamentals PLUGIN = null;
	
	/** The module instance. */
	public static bHelpful MODULE = null;
    
    /* (non-Javadoc)
     * @see uk.codingbadgers.bFundamentals.module.Module#onDisable()
     */
    public void onDisable() {
    	log(Level.INFO, "bHelpful disabled");
    }

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
    public void onEnable() {   
        register(new BadgerDocsListener(this));
        
        registerCommand(new AnnouncementCommand());
        registerCommand(new MaintenanceCommand());
        registerCommand(new MotdCommand());
        registerCommand(new NewsCommand());
        registerCommand(new PlayerListCommand());
        registerCommand(new RegisterCommand());
        registerCommand(new RulesCommand());
        registerCommand(new VoteCommand());
        
        registerConfig(Config.class);
        
        this.setUpdater(new BadgerUpdater(this));
        
        log(Level.INFO, "bHelpful enabled");      		
    }

	public static boolean hasPermission(CommandSender sender, String node) {
		if (!(sender instanceof Player)) {
			return true;
		}
		
		return hasPermission((Player)sender, node);
	}
    
}
