package uk.codingbadgers.bHelpful;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bHelpful.commands.Announcement;
import uk.codingbadgers.bHelpful.commands.Help;
import uk.codingbadgers.bHelpful.commands.Maintenance;
import uk.codingbadgers.bHelpful.commands.Motd;
import uk.codingbadgers.bHelpful.commands.News;
import uk.codingbadgers.bHelpful.commands.Output;
import uk.codingbadgers.bHelpful.commands.PlayerList;
import uk.codingbadgers.bHelpful.commands.Register;
import uk.codingbadgers.bHelpful.commands.Rules;
import uk.codingbadgers.bHelpful.commands.Vote;

/**
 * The base bHelpful module class
 *
 * @author James
 */
public class bHelpful extends Module {

	/**
	 * Instantiates a new b helpful.
	 */
	public bHelpful() {
		super("bHelpful", "1.1");
	}
	
	/** The plugin instance. */
	public static bFundamentals PLUGIN = null;
	
	/** The module instance. */
	public static bHelpful MODULE = null;
    
    /** The announcement thread */
    private Announcement m_announcement = new Announcement(this);
    
    /** if spout is enabled. */
    public static boolean spoutEnabled = false;
    
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
        Configuration.loadConfig(this);
        
        register(new BadgerDocsListener(this));
        
        spoutEnabled = m_plugin.getServer().getPluginManager().getPlugin("Spout") != null;
        
        // lol really not thread safe :D will improve stability later...
        if (!m_announcement.isAlive()) {
        	m_announcement.start();
        }
        
        if (Configuration.NORMAL_STATE) {
        	Maintenance.setMaintenance(true);
        }
        
        if (Configuration.STAFF_STATE) {
        	Maintenance.setStaffMaintenance(true);
        }     
        
        registerCommand(new ModuleCommand("news", "/news").setHelp("shows the latest news."));
        registerCommand(new ModuleCommand("announce", "/announce <subcmd>").setHelp("announcement commands"));
        registerCommand(new ModuleCommand("rankhelp", "/rankhelp").setHelp("shows the help documentation."));
        registerCommand(new ModuleCommand("maintenance", "/maintenance <staff>").addAliase("mm").setHelp("toggles maintenance mode"));
        registerCommand(new ModuleCommand("motd", "/motd").setHelp("shows the motd"));
        registerCommand(new ModuleCommand("register", "/register").setHelp("shows info on how to get first rank."));
        registerCommand(new ModuleCommand("rules", "/rules").setHelp("shows the rules"));
        registerCommand(new ModuleCommand("vote", "/vote").setHelp("shows the vote info"));
        registerCommand(new ModuleCommand("bHelpful", "/bhelpful <reload/disable/version>").setHelp("bHelpful admin commands"));
        registerCommand(new ModuleCommand("list", "/list").addAliase("players").addAliase("who").setHelp("List all players online on the server"));
        
        log(Level.INFO, "bHelpful enabled");
        		
    }
	
	/* (non-Javadoc)
	 * @see uk.codingbadgers.bFundamentals.module.Module#onCommand(org.bukkit.entity.Player, java.lang.String, java.lang.String[])
	 */
	public boolean onCommand(Player sender, String command, String[] args) {
		 // /rankhelp cmd
        if (command.equalsIgnoreCase("rankhelp")) {
            Help.displayHelp(sender);     
            return true;
        }
        
        // /news cmd
        if (News.onCommand(sender, command, args)) {
            return true;
        }
            
        // /register cmd
        if (command.equalsIgnoreCase("register")) {
            Register.displayRegister(sender);
            return true;
        }
        
		// /rules cmd
        if (command.equalsIgnoreCase("rules")) {
            Rules.displayRules(sender);
            return true;
        }
        
        // /maintenance cmd
        if (Maintenance.maintenanceCommand(sender, command, args)) {
        	return true;
        }
        
        // /announce cmds
        if (Announcement.onCommand(sender, command, args)) {
        	return true;
        }
        
        // /motd cmds
        if (command.equalsIgnoreCase("motd")) {
        	Motd.displayMotd(sender);
        	return true;
        }

        // /vote cmd
        if (command.equalsIgnoreCase("vote")) {
        	Vote.displayVoteInfo(sender);
        	return true;
        }
        
        if (command.equalsIgnoreCase("list") || command.equalsIgnoreCase("who") || command.equalsIgnoreCase("players")) {
        	PlayerList.displayList(sender);
        	return true;
        }
        
        // admin cmds
        if (command.equalsIgnoreCase("bhelpful")) {
            
            if (args.length > 1 || args.length ==0) {
                Output.playerWarning(sender, "/bhelpful <reload/version/disable>");
                return true;
            }
            
            if (args[0].equalsIgnoreCase("reload")) {
                if(hasPermission(sender, "bhelpful.admin.reload") || sender.isOp()) {
                	m_plugin.reloadModule(this);
                	Output.player(sender, "[bHelpful] ", "Reloaded Plugin");
                	return true;
                }
            }
            
            
            if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver")) {
                Output.player(sender, "[bHelpful] ", "version " + getVersion());
                return true;
            }
            
            if (args[0].equalsIgnoreCase("disable")) {
                if(hasPermission(sender, "bhelpful.admin.disable") || sender.isOp()) {
                	sender.sendMessage(ChatColor.RED + "bHelpful disabling");
                	m_plugin.disableModule(this);
                	return true;
                }
            }
            return true;
        }
        
        return false;
	}
    
}
