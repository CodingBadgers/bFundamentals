package uk.codingbadgers.bcreative.containers;

import uk.codingbadgers.bFundamentals.player.BasePlayerArray;

public class PlayerManager {

	private BasePlayerArray<GamemodePlayer> m_players = new BasePlayerArray<GamemodePlayer>();
	
	public BasePlayerArray<GamemodePlayer> getPlayers() {
		return m_players;
	}
	
	public GamemodePlayer getPlayer(String name) {
		return m_players.getPlayer(name);
	}
}
