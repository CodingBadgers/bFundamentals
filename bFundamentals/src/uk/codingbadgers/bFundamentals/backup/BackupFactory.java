/**
 * bFundamentals 1.2-SNAPSHOT
 * Copyright (C) 2013  CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.codingbadgers.bFundamentals.backup;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.bFundamentals;

/**
 * A factory for creating Player Backup objects.
 * 
 * @see PlayerBackup
 */
public class BackupFactory {
	
	/** The base backup directory. */
	public static final File BACKUP_DIR = new File(bFundamentals.getInstance().getDataFolder() + File.separator + "players" + File.separator);

	static {
		if (!BACKUP_DIR.exists()) {
			BACKUP_DIR.mkdir();
		}
	}
	
	/**
	 * Creates a new Player backup.
	 *
	 * @param player the player to backup
	 * @return the player backup
	 */
	public static PlayerBackup createBackup(Player player) {
		Validate.notNull(player, "Player cannot be null");
		
		try {
			PlayerBackup backup = new PlayerBackup(
				player
			);
			
			return backup;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Read backup from file.
	 *
	 * @param playerName the player name to retrieve the backup for
	 * @return the player backup or null if there is no backup on disk
	 * @throws IllegalArguementException if the player name is null
	 * @throws IllegalArguementException if the player has never played on the server
	 * @throws IllegalArguementException if the world name is null
	 * @throws IllegalArguementException if the world doesn't exist
	 */
	public static PlayerBackup readBackup(String world, String playerName) {
		Validate.notNull(playerName, "Player name cannot be null");
		Validate.isTrue(Bukkit.getOfflinePlayer(playerName).hasPlayedBefore(), playerName + " has not played on this server");
		Validate.notNull(playerName, "World name cannot be null");
		Validate.notNull(Bukkit.getWorld(world), world + " is not a valid world");
		
		try {
			File file = new File(BACKUP_DIR, world + File.separatorChar + playerName + ".json");
			
			if (!file.exists()) {
				// no backup for this player for that world on disk PANIC
				return null;
			}
			
			PlayerBackup backup = new PlayerBackup(file);
			return backup;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
