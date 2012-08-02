package uk.codingbadgers.bHelpful;

import java.util.logging.Level;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;

/**
 *
 * @author James
 */
public class bHelpful extends Module {

	public bHelpful() {
		super("bHelpful", "1.1");
	}
	
	public static bFundamentals m_plugin = null;
	public static bHelpful module = null;
    private Announcement m_announcement = new Announcement(this);
    public static boolean spoutEnabled = false;
    public static Permission m_permission = null;
    
    public void onDisable() {
    	log(Level.INFO, "bHelpful disabled");
    }

    public void onEnable() {

        module = this;
        m_plugin = plugin;
        
		if (plugin.getServer().getPluginManager().getPlugin("Vault") != null) {	
	        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
	        m_permission = rsp.getProvider();
		}
        
        Configuration.loadConfig(module);
        
        register(new BadgerDocsListener(this));
        
        spoutEnabled = plugin.getServer().getPluginManager().getPlugin("Spout") != null;
        
        // lol really not thread safe :D will improve stability later...
        if (!m_announcement.isAlive()) {
        	m_announcement.start();
        }
        
        if (Configuration.normalState) {
        	Maintenance.setMaintenance(true);
        }
        
        if (Configuration.staffState) {
        	Maintenance.setStaffMaintenance(true);
        }     
        
        registerCommand("news");
        registerCommand("announce");
        registerCommand("rankhelp");
        registerCommand("maintenance");
        registerCommand("motd");
        registerCommand("register");
        registerCommand("rules");
        registerCommand("vote");
        
        log(Level.INFO, "bHelpful enabled");
        		
    }

	public static boolean hasPerms(Player player, String node) {
		
		if (m_permission == null)
			return false;
		
		if (m_permission.has(player, node))
			return true;
		
		return false;
	}   
	
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
        
        // admin cmds
        if (command.equalsIgnoreCase("bhelpful")) {
            
            if (args.length > 1 || args.length ==0) {
                Output.playerWarning(sender, "/bhelpful <reload/version/disable>");
                return true;
            }
            
            /* Can't yet do with bFundamentals without reloading all modules
            if (args[0].equalsIgnoreCase("reload")) {
                if(bHelpful.hasPerms(player, "bhelpful.admin.reload") || player.isOp()) {
                	reload();
                	Output.player(player, "[bHelpful] ", "Reloaded Plugin");
                	return true;
                }
            }
            */
            
            if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver")) {
                Output.player(sender, "[bHelpful] ", "version " + getVersion());
                return true;
            }
            
            if (args[0].equalsIgnoreCase("disable")) {
                if(bHelpful.hasPerms(sender, "bhelpful.admin.disable") || sender.isOp()) {
                	sender.sendMessage(ChatColor.RED + "bHelpful disabling");
                	plugin.disable(this);
                	return true;
                }
            }
            return true;
        }
        
        return false;
	}
    
}
