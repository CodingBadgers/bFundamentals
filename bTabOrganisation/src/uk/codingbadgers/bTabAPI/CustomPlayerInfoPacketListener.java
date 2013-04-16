package uk.codingbadgers.bTabAPI;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class CustomPlayerInfoPacketListener extends PacketAdapter {

	HashMap<String, Boolean> m_allowPackets = new HashMap<String, Boolean>();
	
	public CustomPlayerInfoPacketListener(Plugin plugin) {
		super(
			plugin,
			ConnectionSide.SERVER_SIDE, ListenerPriority.NORMAL,
			Packets.Server.PLAYER_INFO
		);
	}
	
	public void setAllowPackets(Player player, boolean allowed) {
		final String playerName = player.getName();
		
		if (m_allowPackets.containsKey(playerName))
		{
			m_allowPackets.remove(playerName);
		}
		
		m_allowPackets.put(playerName, allowed);
	}
	
	@Override
	public void onPacketSending(PacketEvent event) {
		
		final String playerName = event.getPlayer().getName();
		
		if (!m_allowPackets.containsKey(playerName))
		{
			event.setCancelled(true);
			return;
		}
		
		final Boolean allowPackets = m_allowPackets.get(playerName);
		event.setCancelled(!allowPackets);
	}
	
}
