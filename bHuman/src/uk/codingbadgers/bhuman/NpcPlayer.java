package uk.codingbadgers.bhuman;

import org.bukkit.entity.Player;

import com.topcat.npclib.entity.HumanNPC;

public class NpcPlayer {

	private final Player m_player;
	private HumanNPC m_targeted = null;
	
	public NpcPlayer(Player player) {
		m_player = player;
	}
	
	public Player getPlayer() {
		return m_player;
	}

	public void setTarget(HumanNPC npc) {
		m_targeted = npc;
	}
	
	public HumanNPC getTarget() {
		return m_targeted;
	}
	
}
