package uk.codingbadgers.bcreative;

import org.bukkit.GameMode;

public class GameModeHelper {

	public static GameMode getGamemode(String string) {
		for (GameMode mode : GameMode.values()) { 
			if (string.equalsIgnoreCase(mode.name()))
				return mode;
			if (string.equalsIgnoreCase(String.valueOf(mode.getValue())))
				return mode;
		}
		return GameMode.SURVIVAL;
	}

}
