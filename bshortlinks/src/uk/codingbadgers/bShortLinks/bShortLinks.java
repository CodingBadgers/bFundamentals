package uk.codingbadgers.bShortLinks;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.RegisteredServiceProvider;
import uk.codingbadgers.bFundamentals.module.Module;

public class bShortLinks extends Module {
	
	public bShortLinks() {
		super("bShortLinks", "1.0");
	}

	private PlayerListener m_playerListener = new PlayerListener();
	
	public void onEnable() {
		
		Global.plugin = plugin;
		Global.module = this;
		register(m_playerListener);
		
		RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
        	Global.permission = permissionProvider.getProvider();
        }
        
        Global.LoadConfig();
		
		Global.OutputConsole("bShortLinks Enabled");
		
	}
	
	public void onDisable() {
		
		Global.OutputConsole("bShortLinks Disabled");
		
	}


}
