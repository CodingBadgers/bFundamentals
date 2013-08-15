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
	private static final File backupDir = new File(bFundamentals.getInstance().getDataFolder() + File.separator + "backups" + File.separator);

	static {
		if (!backupDir.exists()) {
			backupDir.mkdir();
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
			File file = new File(backupDir, world + File.separatorChar + playerName + ".json");
			
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
