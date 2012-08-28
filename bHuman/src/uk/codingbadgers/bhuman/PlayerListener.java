package uk.codingbadgers.bhuman;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.topcat.npclib.entity.HumanNPC;
import com.topcat.npclib.nms.NPCEntity;

public class PlayerListener implements Listener {

	private static ArrayList<NpcPlayer> players = new ArrayList<NpcPlayer>();
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		NpcPlayer player = new NpcPlayer(event.getPlayer());
		players.add(player);
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerLeave(PlayerQuitEvent event) {
		players.remove(getPlayer(event.getPlayer().getName()));
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerLeave(PlayerKickEvent event) {
		players.remove(getPlayer(event.getPlayer().getName()));
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEntityEvent event) {
		NpcPlayer player = getPlayer(event.getPlayer().getName());
		
		if (!(event.getRightClicked() instanceof NPCEntity))
			return;
		
		NPCEntity entity = (NPCEntity)event.getRightClicked();
		String id = bHuman.getNPCManager().getNPCIdFromEntity((Entity)entity);
		HumanNPC npc = (HumanNPC)bHuman.getNPCManager().getNPC(id);
		
		player.setTarget(npc);
		bHuman.sendMessage(bHuman.NAME, player.getPlayer(), "");
	}
	
	public static NpcPlayer getPlayer(String player) {
		Iterator<NpcPlayer> itr = players.iterator();
		
		while(itr.hasNext()) {
			NpcPlayer current = itr.next();
			if (current.getPlayer().getName().equalsIgnoreCase(player))
				return current;
		}
		
		return null;
	}
}
