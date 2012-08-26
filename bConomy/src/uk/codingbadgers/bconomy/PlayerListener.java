package uk.codingbadgers.bconomy;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import uk.codingbadgers.bconomy.account.Account;
import uk.codingbadgers.bconomy.config.Config;
import uk.codingbadgers.bconomy.config.DatabaseManager;

public class PlayerListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		Account account = Global.getAccounts().get(player);
		if (account == null) {
			// new player, create an account
			account = new Account(Global.getNextId(), player);
			Global.getAccounts().add(account);
			DatabaseManager.addAccount(account);
		}		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		Player player = event.getPlayer();
		Account account = Global.getAccounts().get(player);
		if (account == null)
			return;
		
		if (account.getBalance() == Config.m_startingBalance) {
			// default balance so delete them from the database
			DatabaseManager.removeAccount(account);	
			return;
		}
		
		DatabaseManager.updateAccount(account);
		
	}
	
}
