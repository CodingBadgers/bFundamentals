package uk.codingbadgers.brewarded;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

public class bRewarded extends Module {

	private ArrayList<PlayerWallet>	m_playerWallets = new ArrayList<PlayerWallet>();
	
	private ArrayList<TradableItem>	m_tradableItems = new ArrayList<TradableItem>();
	
	/**
	 * Called when the module is disabled.
	 */
	public void onDisable() {

	}

	/**
	 * Called when the module is loaded.
	 */
	public void onEnable() {
		
		reloadTradbaleItems();
		registerCommand(new ModuleCommand("reward", "/reward").setHelp("Shows bRewarded Help."));
		
	}
	
	private void reloadTradbaleItems() {
		
		m_tradableItems.clear();
		
		File itemsConfig = new File(getDataFolder() + File.separator + "items.cfg");
		
		// Make a default config
		if (!itemsConfig.exists())
		{
			try {
				itemsConfig.createNewFile();
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
			
			try {
				
				BufferedWriter out = new BufferedWriter(new FileWriter(itemsConfig));
				
				out.write("name: 'Cow Egg'\n");
				out.write("\tid: '383:92'\n");
				out.write("\tquantity: '1'\n");
				out.write("\tprice: '10'\n");

				out.close();
				
			}	catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
				return;
			}
			
		}
		
		// Load in the config
		try {
			
			int id = 1;
			
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(new FileInputStream(itemsConfig));
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String strLine;
			while ((strLine = br.readLine()) != null) {
				if (strLine.startsWith("name:")) {
					
					String name = strLine.replace("name:", "").replaceAll("'", "").trim();
					String itemId = "";
					int quantity = 1;
					int price = 1;
					
					while ((strLine = br.readLine()) != null) {
						
						if (strLine.startsWith("id:")) {
							itemId = strLine.replace("id:", "").replaceAll("'", "").trim();
						}
						else if (strLine.startsWith("quantity:")) {
							String strQuantity = strLine.replace("quantity:", "").replaceAll("'", "").trim();
							quantity = Integer.parseInt(strQuantity);
						}
						else if (strLine.startsWith("price:")) {
							String strPrice = strLine.replace("price:", "").replaceAll("'", "").trim();
							price = Integer.parseInt(strPrice);
							break;
						}
						else {
							break;
						}
						
					}
					
					TradableItem tradableItem = new TradableItem(id, name, itemId, quantity, price);
					m_tradableItems.add(tradableItem);					
					id++;
				}
			}

			in.close();
			
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	
	}

	/**
	 * Handle the command /reward
	 */
	public boolean onCommand(CommandSender sender, String label, String[] args) {		
		
		if (!label.equalsIgnoreCase("reward"))
			return false;
		
		if (!(sender instanceof Player))
			return true;
		
		final Player player = (Player)sender;
		
		if (args.length == 0 || args[0].equalsIgnoreCase("help"))
		{
			//CommandHelp(player, args);
			return true;
		}
		
		if (args.length == 1 || args[0].equalsIgnoreCase("wallet"))
		{
			commandWallet(player, args);
			return true;
		}
		
		if (args.length == 1 || args[0].equalsIgnoreCase("give"))
		{
			commandGive(player, args);
			return true;
		}
		
		if (args.length == 1 || args[0].equalsIgnoreCase("remove"))
		{
			commandRemove(player, args);
			return true;
		}
		
		if (args.length == 1 || args[0].equalsIgnoreCase("items"))
		{
			commandItems(player, args);
			return true;
		}
		
		if (args.length == 1 || args[0].equalsIgnoreCase("trade"))
		{
			commandTrade(player, args);
			return true;
		}
		
		return true;
	}
	
	private void commandTrade(Player player, String[] args) {
		
		if (!Module.hasPermission(player, "brewarded.trade"))
		{
			Module.sendMessage(getName(), player, "You do not have the required permission to do this (brewarded.trade)");
			return;
		}
		
		if (args.length != 2)
		{
			Module.sendMessage(getName(), player, "Incorrect usage, /reward trade <id>");
			return;
		}
		
		int id = -1;
		try {
			id = Integer.parseInt(args[1]);
		} catch(Exception ex)
		{
			Module.sendMessage(getName(), player, "The id '" + args[1] + "' is invalid");
			return;
		}
		
		TradableItem item = findTradableItem(id);
		if (item == null)
		{
			Module.sendMessage(getName(), player, "Could not find an item with the id " + id);
			return;
		}
		
		ItemStack itemStack = new ItemStack(item.getItemId(), item.getQuantity(), (short)item.getItemData());
		
		final PlayerWallet wallet = findWallet(player);
		if (wallet == null)
			return;
		
		wallet.giveAmount(-item.getPrice());
		player.getInventory().addItem(itemStack);
	}

	private void commandItems(Player player, String[] args) {

		if (!Module.hasPermission(player, "brewarded.items"))
		{
			Module.sendMessage(getName(), player, "You do not have the required permission to do this (brewarded.items)");
			return;
		}
		
		Module.sendMessage(getName(), player, "-- Tradable Items --");
		Module.sendMessage(getName(), player, "-- ID :: Name :: Price --");
		for (TradableItem item : m_tradableItems)
		{
			Module.sendMessage(getName(), player, item.getID() + " :: " + item.getName() + " :: " + item.getPrice());
		}
		
	}

	private void commandRemove(Player player, String[] args) {
		
		if (!Module.hasPermission(player, "brewarded.admin.remove"))
		{
			Module.sendMessage(getName(), player, "You do not have the required permission to do this (brewarded.admin.remove)");
			return;
		}
		
		if (args.length != 3)
		{
			Module.sendMessage(getName(), player, "Incorrect usage, /reward remove <username> <amount>");
			return;
		}
		
		OfflinePlayer otherPlayer = m_plugin.getServer().getOfflinePlayer(args[1]);
		if (otherPlayer == null)
		{
			Module.sendMessage(getName(), player, "Could not find the player '" + args[1] + "'");
			return;
		}
		
		int amount = 0;
		try {
			amount = Integer.parseInt(args[2]);
		} catch(Exception ex)
		{
			Module.sendMessage(getName(), player, "The amount '" + args[2] + "' is invalid");
			return;
		}
		
		final PlayerWallet wallet = findWallet(otherPlayer);
		if (wallet == null)
			return;
		
		wallet.giveAmount(-amount);
		
		Module.sendMessage(getName(), player, "You withdrew from the account '" + otherPlayer.getName() + "' " + amount + " tickets");
		
		if (otherPlayer.isOnline())
			Module.sendMessage(getName(), otherPlayer.getPlayer(), "You have been deducted " + amount + " tickets");
		
	}

	private void commandGive(Player player, String[] args) {
		
		if (!Module.hasPermission(player, "brewarded.admin.give"))
		{
			Module.sendMessage(getName(), player, "You do not have the required permission to do this (brewarded.admin.give)");
			return;
		}
		
		if (args.length != 3)
		{
			Module.sendMessage(getName(), player, "Incorrect usage, /reward give <username> <amount>");
			return;
		}
		
		OfflinePlayer otherPlayer = m_plugin.getServer().getOfflinePlayer(args[1]);
		if (otherPlayer == null)
		{
			Module.sendMessage(getName(), player, "Could not find the player '" + args[1] + "'");
			return;
		}
		
		int amount = 0;
		try {
			amount = Integer.parseInt(args[2]);
		} catch(Exception ex)
		{
			Module.sendMessage(getName(), player, "The amount '" + args[2] + "' is invalid");
			return;
		}
		
		final PlayerWallet wallet = findWallet(otherPlayer);
		if (wallet == null)
			return;
		
		wallet.giveAmount(amount);
		
		Module.sendMessage(getName(), player, "You gave the account '" + otherPlayer.getName() + "' " + amount + " tickets");
		
		if (otherPlayer.isOnline())
			Module.sendMessage(getName(), otherPlayer.getPlayer(), "You have been given " + amount + " tickets");
		
	}

	private void commandWallet(Player player, String[] args) {
		
		if (!Module.hasPermission(player, "brewarded.wallet"))
		{
			Module.sendMessage(getName(), player, "You do not have the required permission to do this (brewarded.wallet)");
			return;
		}
		
		final PlayerWallet wallet = findWallet(player);
		if (wallet == null)
			return;
		
		Module.sendMessage(getName(), player, "Your wallet contains " + wallet.getAmount() + " tickets");		
	}

	private PlayerWallet findWallet(OfflinePlayer player)
	{
		final String playerName = player.getName();
		
		for (PlayerWallet wallet : m_playerWallets)
		{
			if (wallet.getOwner().equalsIgnoreCase(playerName))
				return wallet;
		}
		
		return null;
	}
	
	private TradableItem findTradableItem(int id)
	{
		for (TradableItem item : m_tradableItems)
		{
			if (item.getID() == id)
				return item;
		}
		
		return null;
	}
	
}
