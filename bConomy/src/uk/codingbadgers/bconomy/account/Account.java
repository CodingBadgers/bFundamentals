package uk.codingbadgers.bconomy.account;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import uk.codingbadgers.bconomy.config.Config;
import uk.codingbadgers.bconomy.config.DatabaseManager;

public class Account {
	
	private int m_id = 0;
	private OfflinePlayer m_player = null;
	private double m_balance = 0;

	/**
	 * Account construct for new accounts
	 * 
	 * @param id - the account id
	 * @param player - the offline player to use
	 */
	public Account(int id, Player player){
		m_id = id;
		m_player = player;
		m_balance = Config.m_startingBalance;
	}
	
	/**
	 * Account construct for existing accounts
	 * 
	 * @param id - the account id
	 * @param player - the offlineplayer instance
	 * @param balance - their current balance
	 */
	public Account(int id, OfflinePlayer player, double balance) {
		m_id = id;
		m_player = player;
		m_balance = balance;
	}
	
	/**
	 * get the players id
	 * 
	 * @return the player's id
	 */
	public int getId() {
		return m_id;
	}
	
	/**
	 * Get the player object
	 * 
	 * @return the offline player instance
	 */
	public OfflinePlayer getPlayer() {
		return m_player;
	}
	
	/** 
	 * check if the account owner is online
	 * 
	 * @return if the account owner is online
	 */
	public boolean isOnline() {
		return m_player.isOnline();
	}

	/**
	 * Set the account owner
	 * 
	 * @param player
	 */
	public void setPlayer(Player player) {
		m_player = player;
	}

	/**
	 * Get the accounts balance
	 * 
	 * @return the balance
	 */
	public double getBalance() {
		return m_balance;
	}

	/**
	 * Set the accounts balance
	 * 
	 * @param balance to set it to
	 */
	public void setBalance(double balance) {
		m_balance = balance;
		DatabaseManager.updateAccount(this);
	}
	
	/**
	 * Give the player an amount of money
	 * 
	 * @param amount to give them
	 */
	public void deposit(double amount) {
		m_balance += amount;
		DatabaseManager.updateAccount(this);
	}
	
	/**
	 * take money off the player
	 * 
	 * @param amount to take off them
	 */
	public void withdraw(double amount) {
		m_balance -= amount;
		DatabaseManager.updateAccount(this);
	}
	
	/** 
	 * check if the player has a amount of money
	 * 
	 * @param amount to check
	 * @return whether they have that amount
	 */
	public boolean has(double amount) {
		return amount <= m_balance;
	}

}
