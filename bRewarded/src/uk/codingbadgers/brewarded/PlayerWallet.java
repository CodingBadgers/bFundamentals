package uk.codingbadgers.brewarded;

import org.bukkit.entity.Player;

public class PlayerWallet {

	private final String m_owner;
	
	private int m_amount = 0;
	
	public PlayerWallet(Player owner)
	{
		m_owner = owner.getName();
	}
	
	public String getOwner()
	{
		return m_owner;
	}
	
	public int getAmount()
	{
		return m_amount;
	}
	
	public void giveAmount(int amount)
	{
		m_amount += amount;
	}
	
}
