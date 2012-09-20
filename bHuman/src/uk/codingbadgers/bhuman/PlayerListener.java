package uk.codingbadgers.bhuman;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.topcat.npclib.entity.HumanNPC;
import com.topcat.npclib.entity.NPC;
import com.topcat.npclib.nms.NPCEntity;
import com.topcat.npclib.nms.NpcEntityTargetEvent;

public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onNPCTarget(NpcEntityTargetEvent event) {
		
		if (!(event.getTarget() instanceof Player)) 
			return;
		
		Player player = (Player)event.getTarget();
		NPC npc = bHuman.getNPCManager().getNPC(bHuman.getNPCManager().getNPCIdFromEntity(event.getEntity()));
		
		if(!(npc instanceof HumanNPC))
			return;
		
		HumanNPC hNpc = (HumanNPC)npc;
		
		bHuman.MODULE.debugConsole(player.getName() + " targeted " + hNpc.getName());
		
		if (hNpc.canTalk()) {
			player.sendMessage(hNpc.getMessage());
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onNPCDeath(EntityDamageEvent event) {
		
		if (!(event.getEntity() instanceof NPCEntity))
			return;
		
		NPC bnpc = bHuman.getNPCManager().getNPC(bHuman.getNPCManager().getNPCIdFromEntity(event.getEntity()));
		
		if (!(bnpc instanceof HumanNPC))
			return;
		
		HumanNPC npc = (HumanNPC)bnpc;
		
		if (!npc.canDie())
			return;
		
		event.setCancelled(true);
	}
}
