package uk.codingbadgers.bTabOrganisation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bTabAPI.bTabAPI;

/**
 * The base bTabOrganisation module class
 *
 * @author N3wton
 */
public class bTabOrganisation extends Module implements Listener {
		 
    public void onDisable() {
    	log(Level.INFO, "bTabOrganisation disabled");
    }

    public void onLoad() {
  
    }
    
    public void onEnable() {         
        log(Level.INFO, "bTabOrganisation enabled");
        bTabAPI.Initiate(bFundamentals.getInstance());
        bTabAPI.setTabContent(0, 1, "McBadgerCraft");
        bTabAPI.setTabContent(1, 0, "THIS IS A TEST");
        updateTime();
    }
    
    private void updateTime() {
    	Bukkit.getScheduler().scheduleSyncDelayedTask(m_plugin,
    			new Runnable() {
    				public void run() {		
    					DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    					bTabAPI.setTabContent(0, 0, dateFormat.format(new Date()));
    					updateTime();
    				}
    			}, 20L
    		);  
    }
    
}
