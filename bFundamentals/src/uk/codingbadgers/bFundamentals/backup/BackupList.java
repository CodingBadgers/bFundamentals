package uk.codingbadgers.bFundamentals.backup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;

/**
 * A simple list to backup and restore players to their backed up state.
 */
public class BackupList {

	private List<PlayerBackup> backups = new ArrayList<PlayerBackup>();
	private final Player player;
	
	public BackupList(Player player) {
		this.player = player;
	}

	/**
	 * Backup a player.
	 *
	 * @param player the player to backup
	 * @return 
	 */
	public PlayerBackup backupPlayer() {
		return backupPlayer(false);
	}
	
    /**
     * Clear a players armour slots
     * 
     * @param inventory	The players inventory
     */
    private void clearArmor(){
    	player.getInventory().setHelmet(null);
    	player.getInventory().setChestplate(null);
    	player.getInventory().setLeggings(null);
    	player.getInventory().setBoots(null);
    }
	
	/**
	 * Backup a player.
	 *
	 * @param player the player to backup
	 * @param clearInv clear the players inventory after backing up
	 */
	public PlayerBackup backupPlayer(boolean clearInv) {
		PlayerBackup backup = BackupFactory.createBackup(player);
		backups.add(backup);
		
		if (clearInv) {
			player.getInventory().clear();
			clearArmor();
		}
		
		return backup;
	}
	
	/**
	 * Restore a player to their backup either in this list or on disk.
	 *
	 * @param player the player to restore
	 * @return true if successful and false otherwise
	 */
	public boolean restorePlayer() {
		player.getInventory().clear();
		clearArmor();
				
		List<PlayerBackup> backups = new ArrayList<PlayerBackup>(this.backups);
		Iterator<PlayerBackup> itr = backups.iterator();
		while(itr.hasNext()) {
			PlayerBackup current = itr.next();
			if (current.getName().equalsIgnoreCase(player.getName())) {
				current.restore(player);
				current.deleteFile();
				this.backups.remove(current);
				return true;
			}
		}
		
		PlayerBackup backup = BackupFactory.readBackup(player.getWorld().getName(), player.getName());
		
		if (backup != null) {
			backup.restore(player);
			backup.deleteFile();
			return true;
		}
		
		return false;
	}
}
