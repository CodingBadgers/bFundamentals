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
import org.bukkit.entity.Player;

/**
 * A factory for creating Player Backup objects.
 * 
 * @see PlayerBackup
 */
public class BackupFactory {
		
	/**
	 * Creates a new Player backup.
	 *
	 * @param player the player to backup
	 * @return the player backup
	 */
	public static PlayerBackup createBackup(File backupFile, Player player) {
		Validate.notNull(player, "player cannot be null");
		Validate.notNull(backupFile, "backupFile cannot be null");
		
		try {
			return new PlayerBackup(backupFile, player);
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
	 */
	public static PlayerBackup readBackup(File backupFile) {
		Validate.isTrue(backupFile.exists(), "The given backup file does not exist");
		
		try {
			return new PlayerBackup(backupFile);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
