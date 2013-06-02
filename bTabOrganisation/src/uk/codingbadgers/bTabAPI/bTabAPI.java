package uk.codingbadgers.bTabAPI;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class bTabAPI implements Listener {

	private static bTabAPI m_instance = null;
	private static CustomPlayerInfoPacketListener m_customListener = null;
	private static ProtocolManager m_manager = null;
	private static Plugin m_plugin = null;
	private static Tab[][] m_tab = null;
	
	private static int m_columns = 1;
	private static int m_rows = 10;

	public static void Initiate(Plugin plugin) {
		
		if (m_instance == null) {
			m_instance = new bTabAPI();
			
			m_manager = ProtocolLibrary.getProtocolManager();
			m_customListener = new CustomPlayerInfoPacketListener(plugin);
			m_manager.addPacketListener(m_customListener);
			m_plugin = plugin;		
			
			Bukkit.getServer().getPluginManager().registerEvents(m_instance, plugin);
		}
		
		// Default the tabs to "empty", a hack of color codes and spaces
		// take into account player count...
		m_columns = 3;
		m_rows = 25;
		
		m_tab = new Tab[m_columns][m_rows];
		
		int colorId = 1;
		int noofSpaces = 1;
		
		for (int column = 0; column < m_columns; ++column)
		{
			for (int row = 0; row < m_rows; ++row)
			{
				/*
				String uniqueEmptyTab = "" + ChatColor.getByChar((char)colorId);
				for (int spaceIndex = 0; spaceIndex < noofSpaces; ++spaceIndex)
				{
					uniqueEmptyTab += " ";
				}
				uniqueEmptyTab += ChatColor.WHITE;
				
				m_tab[column][row] = new Tab();
				m_tab[column][row].content = uniqueEmptyTab;
				m_tab[column][row].online = true;
				m_tab[column][row].ping = 1;
				
				colorId++;
				if (colorId >= 15)
				{
					colorId = 1;
					noofSpaces++;
				}
				*/
				
				m_tab[column][row] = new Tab();
				m_tab[column][row].content = column + ", " + row;
				m_tab[column][row].online = true;
				m_tab[column][row].ping = 1;
			}
		}	
		
	}
		
    @EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoin(PlayerJoinEvent event) {
    	
    	m_customListener.setAllowPackets(event.getPlayer(), false);
    	
    	Bukkit.getScheduler().scheduleSyncDelayedTask(m_plugin,
			new Runnable() {
				public void run() {				
					clearTabList();
			    	sendTabList();
				}
			}, 20L
		);   	
    	
    }
    
    @EventHandler(priority = EventPriority.LOW)
	public void onPlayerQuit(PlayerQuitEvent event) {
    	clearTabList();
    	sendTabList();
    }
    
    public static void setTabContent(int row, int column, String content) {
    	if (row < 0) return;
    	if (column < 0) return;
    	if (row >= m_rows) return;
    	if (column >= m_columns) return;
    	
    	clearTabList();
    	
    	m_tab[row][column].content = content;
    	
    	sendTabList();
    }
        
    private static void clearTabList() {
    	
    	for (Player player : Bukkit.getServer().getOnlinePlayers())
    	{
    		m_customListener.setAllowPackets(player, true);
    		
    		for (int column = 0; column < m_columns; ++column)
    		{
    			for (int row = 0; row < m_rows; ++row)
    			{
					m_tab[column][row].online = false;
					sendListPacket(player, m_tab[column][row]);
    			}
    		}
    	}
    	
    }
    
    private static void sendTabList() {
    	
    	for (Player player : Bukkit.getServer().getOnlinePlayers())
    	{
    		m_customListener.setAllowPackets(player, true);
    		
    		for (int column = 0; column < m_columns; ++column)
    		{
    			for (int row = 0; row < m_rows; ++row)
    			{
					m_tab[column][row].online = true;
					sendListPacket(player, m_tab[column][row]);
    			}
    		}
    	}
    	
    }
	
	private static void sendListPacket(Player player, Tab tab) {
    	
    	PacketPlayerListItem listPacket = new PacketPlayerListItem();  	
    	listPacket.setPlayerName(tab.content);	
    	listPacket.setOnline(tab.online);
    	listPacket.setPing(tab.ping);
	
		try {
			m_manager.sendServerPacket(player, listPacket);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
    }
	
}
