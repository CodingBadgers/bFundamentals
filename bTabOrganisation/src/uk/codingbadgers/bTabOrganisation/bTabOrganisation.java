package uk.codingbadgers.bTabOrganisation;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bTabAPI.PacketPlayerListItem;
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
