/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.branks;

import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 *
 * @author Sam
 */
public class NpcListener implements Listener {
	
	private final bRanks m_module;
	
	/**
	 * 
	 * @param module 
	 */
	public NpcListener(bRanks module) {
		m_module = module;
	}
	
	/**
	 * Called when a citizens NPC is spawned
	 * @param event
	 */
	@EventHandler
	public void onNPCSpawn(NPCSpawnEvent event) {

		NPC npc = event.getNPC();
		Entity entity = npc.getEntity();
		if (entity == null || !(entity instanceof Player))
			return;
			
		Player player = (Player)entity;
		m_module.addPlayerToTeam(player, true);		
	}
	
}
