package uk.codingbadgers.bcreative.containers;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.player.BasePlayer;
import uk.codingbadgers.bcreative.bCreative;

public class GamemodePlayer extends BasePlayer{

	private boolean m_monitor = false;
	
	public GamemodePlayer(Player player) {
		super(player);
		m_monitor = player.getGameMode() == getWorld().getMonitoredGM() && getWorld().isMonitored();
	}
	
	public GamemodeWorld getWorld() {
		return bCreative.getWorldManager().getWorld(m_player.getWorld().getName());
	}
	
	public GameMode getGamemode() {
		return m_player.getGameMode();
	}
	
	public void setGamemode(GameMode gamemode) {
		if (getWorld().isGamemodeEnabled(gamemode)) {
			m_player.setGameMode(gamemode);
			
			if (getWorld().getMonitoredGM().equals(gamemode) && getWorld().isMonitored()) 
				m_monitor = true;
			
		} else {
			sendMessage("Sorry that gamemode is not aloud in this world");
		}
	}
	
	public void sendMessage(String message) {
		bCreative.sendMessage(bCreative.getInstance().getDesciption().getName(), m_player, message);
	}

	public boolean hasPermission(String node, boolean verbose) {
		return bCreative.hasPermission(m_player, node);
	}

	public boolean isMonitor() {
		return m_monitor;
	}
	
	public void setMonitor(boolean monitor) {
		m_monitor = monitor;
	}
}
