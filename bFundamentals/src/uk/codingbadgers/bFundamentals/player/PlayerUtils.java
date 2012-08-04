package uk.codingbadgers.bFundamentals.player;

import org.bukkit.entity.Player;

public class PlayerUtils {

	public static BasePlayerArray players = new BasePlayerArray();
	
	public static BasePlayer getPlayer(String name) {
		return players.getPlayer(name);
	}
	
	public static BasePlayer getPlayer(Player player) {
		return players.getPlayer(player);
	}
	
}
