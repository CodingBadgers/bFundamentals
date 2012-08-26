package uk.codingbadgers.bconomy;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import uk.codingbadgers.bconomy.account.Account;
import uk.codingbadgers.bconomy.config.Config;

public class CommandManager {

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onCommand(org.bukkit.command.Player, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	public static boolean onCommand(Player sender, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("money")) {
						
			// show the help
			if (args.length != 0 && (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help"))) {
				showHelp(sender);
				return true;
			}

			// handle /money pay
			if (args.length != 0 && args[0].equalsIgnoreCase("pay")) {
				handlePay(sender, args);
				return true;
			}			
			
			// Admin cmds
			// handle /money grant
			if (args.length != 0 && args[0].equalsIgnoreCase("grant")) {
				handleGrant(sender, args);
				return true;
			}
			
			// handle /money withdraw
			if (args.length != 0 && args[0].equalsIgnoreCase("withdraw")) {
				handleWithdraw(sender, args);
				return true;
			}

			// handle /money set
			if (args.length != 0 && args[0].equalsIgnoreCase("set")) {
				handleSet(sender, args);
				return true;
			}
			
			// handle /money reset
			if (args.length != 0 && args[0].equalsIgnoreCase("reset")) {
				handleReset(sender, args);
				return true;
			}
			
			// handle /money top
			if (args.length != 0 && args[0].equalsIgnoreCase("top")) {
				handleTop(sender, args);
				return true;
			}
			
			// handle /money grantall
			if (args.length != 0 && args[0].equalsIgnoreCase("grantall")) {
				handleGrantAll(sender, args);
				return true;
			}

			// handle /money last
			if (args.length <= 1) {
				handleMoney(sender, args);
				return true;
			}

			return true;
		}
		
		return false;
	}

	private static void handleGrantAll(Player sender, String[] args) {

		if (!Global.hasPermission(sender, "bconomy.admin.grant", true))
			return;
		
		if (args.length != 2) {
			Global.output(sender, Global.getLanguage("INVALID-USAGE"));			
			return;
		}
		
		double amount = 0;
		try {
			amount = Double.parseDouble(args[1]);
		} catch(Exception ex) {
			Global.output(sender, Global.getLanguage("INVALID-AMOUNT").replace("<amount>", args[1]));
			return;
		}
		
		if (amount <= 0) {
			Global.output(sender, Global.getLanguage("NO-NEGATIVE"));
			return;
		}
		
		for (Account account : Global.getAccounts()) {
			account.deposit(amount);
		}
		
		Global.output(sender, Global.getLanguage("GRANTALL-SENDER").replace("<amount>", String.valueOf(amount)));
		Global.output(sender, Global.getLanguage("GRANTALL-SERVER").replace("<amount>", String.valueOf(amount)));
	}

	/**
	 * Handle top.
	 *
	 * @param sender the sender
	 * @param args the args
	 */
	private static void handleTop(Player sender, String[] args) {
		
		if (!Global.hasPermission(sender, "bconomy.top", true))
			return;
		
		int amount = 5;
		
		if (args.length == 2) {
			try {
				amount = Integer.parseInt(args[1]);
			} catch(Exception ex) {
				amount = 5;
			}
		}
		
		ArrayList<Account> topPlayers = Global.getAccounts().getTop(amount);
		
		for (int i = 0; i < topPlayers.size(); ++i) {
			int id = topPlayers.size() - i - 1;
			Global.output(sender, (i+1) + " - " + topPlayers.get(id).getPlayer().getName() + " - " + Global.format(topPlayers.get(id).getBalance()) );
		}
		
	}

	/**
	 * Handle reset.
	 *
	 * @param sender the sender
	 * @param args the args
	 */
	private static void handleReset(Player sender, String[] args) {
		
		if (!Global.hasPermission(sender, "bconomy.admin.reset", true))
			return;
		
		if (args.length != 2) {
			Global.output(sender, Global.getLanguage("INVALID-USAGE"));
			return;
		}

		Account playerAccount = Global.getAccounts().get(args[1]);
		
		if (playerAccount == null) {
			Global.output(sender, Global.getLanguage("NO-ACCOUNT").replace("<player>", args[1]));
			return;
		}
		
		double amount = Config.m_startingBalance;
		
		playerAccount.setBalance(amount);
		Global.output(sender, Global.getLanguage("RESET-SENDER").replace("<player>", args[1]));
		Global.output(sender, Global.getLanguage("RESET-TARGET"));
	}

	/**
	 * Handle set.
	 *
	 * @param sender the sender
	 * @param args the args
	 */
	private static void handleSet(Player sender, String[] args) {
		
		if (!Global.hasPermission(sender, "bconomy.admin.set", true))
			return;
		
		if (args.length != 3) {
			Global.output(sender, Global.getLanguage("INVALID-USAGE"));
			return;
		}
		
		Account playerAccount = Global.getAccounts().get(args[1]);
		
		if (playerAccount == null) {
			Global.output(sender, Global.getLanguage("NO-ACCOUNT").replace("<player>", args[1]));
			return;
		}
		
		double amount = 0;
		try {
			amount = Double.parseDouble(args[2]);
		} catch(Exception ex) {
			Global.output(sender, Global.getLanguage("INVALID-AMOUNT").replace("<amount>", args[2]));
			return;
		}
		
		if (amount <= 0) {
			Global.output(sender, Global.getLanguage("NO-NEGATIVE"));
			return;
		}
		
		playerAccount.setBalance(amount);
		Global.output(sender, Global.getLanguage("SET-SENDER").replace("<player>", args[1]).replace("<amount>", args[2]));
		Global.output(playerAccount.getPlayer(), Global.getLanguage("SET-TARGET").replace("<amount>", args[2]));
		}

	/**
	 * Handle withdraw.
	 *
	 * @param sender the sender
	 * @param args the args
	 */
	private static void handleWithdraw(Player sender, String[] args) {

		if (!Global.hasPermission(sender, "bconomy.admin.withdraw", true))
			return;
		
		if (args.length != 3) {
			Global.output(sender, Global.getLanguage("INVALID-USAGE"));
			return;
		}
		
		Account playerAccount = Global.getAccounts().get(args[1]);
		
		if (playerAccount == null) {
			Global.output(sender, Global.getLanguage("NO-ACCOUNT").replace("<player>", args[1]));
			return;
		}
		
		double amount = 0;
		try {
			amount = Double.parseDouble(args[2]);
		} catch(Exception ex) {
			Global.output(sender, Global.getLanguage("INVALID-AMOUNT").replace("<amount>", args[2]));
			return;
		}
		
		if (amount <= 0) {
			Global.output(sender, Global.getLanguage("NO-NEGATIVE"));
			return;
		}
		
		if (!playerAccount.has(amount)) {
			Global.output(sender, Global.getLanguage("NO-FUNDS"));
			return;
		}
		
		playerAccount.withdraw(amount);
		Global.output(sender, Global.getLanguage("WITHDRAW-SENDER").replace("<player>", args[1]).replace("<amount>", args[2]));
		Global.output(playerAccount.getPlayer(), Global.getLanguage("WITHDRAW-TARGET").replace("<amount>", args[2]));
		
	}
	
	/**
	 * Handle grant.
	 *
	 * @param sender the sender
	 * @param args the args
	 */
	private static void handleGrant(Player sender, String[] args) {

		if (!Global.hasPermission(sender, "bconomy.admin.grant", true))
			return;
		
		if (args.length != 3) {
			Global.output(sender, Global.getLanguage("INVALID-USAGE"));
			return;
		}
		
		Account playerAccount = Global.getAccounts().get(args[1]);
		
		if (playerAccount == null) {
			Global.output(sender, Global.getLanguage("NO-ACCOUNT").replace("<player>", args[1]));
			return;
		}
		
		double amount = 0;
		try {
			amount = Double.parseDouble(args[2]);
		} catch(Exception ex) {
			Global.output(sender, Global.getLanguage("INVALID-AMOUNT").replace("<amount>", args[2]));
			return;
		}
		
		if (amount <= 0) {
			Global.output(sender, Global.getLanguage("NO-NEGATIVE"));
			return;
		}
		
		playerAccount.deposit(amount);
		Global.output(sender, Global.getLanguage("GRANT-SENDER").replace("<player>", args[1]).replace("<amount>", args[2]));
		Global.output(playerAccount.getPlayer(), Global.getLanguage("GRANT-TARGET").replace("<amount>", args[2]));
		
	}

	/**
	 * Handle pay.
	 *
	 * @param sender the sender
	 * @param args the args
	 */
	private static void handlePay(Player sender, String[] args) {
		
		if (!Global.hasPermission(sender, "bconomy.pay", true)) {
			return;
		}
		
		if (!(sender instanceof Player)) {
			Global.output(sender, Global.getLanguage("NO-CONSOLE"));
			return;
		}
		
		if (args.length != 3) {
			Global.output(sender, Global.getLanguage("INVALID-USAGE"));
			return;
		}
		
		Account playerAccount = Global.getAccounts().get(args[1]);
		
		if (playerAccount == null) {
			Global.output(sender, Global.getLanguage("NO-ACCOUNT").replace("<player>", args[1]));
			return;
		}
		
		double amount = 0;
		try {
			amount = Double.parseDouble(args[2]);
		} catch(Exception ex) {
			Global.output(sender, Global.getLanguage("INVALID-AMOUNT").replace("<amount>", args[2]));
			return;
		}
		
		if (amount <= 0) {
			Global.output(sender, Global.getLanguage("NO-NEGATIVE"));
			return;
		}
		
		Account myAccount = Global.getAccounts().get((Player)sender);
		
		if (!myAccount.has(amount)) {
			Global.output(sender, Global.getLanguage("NO-FUNDS"));
			return;
		}
		
		myAccount.withdraw(amount);
		playerAccount.deposit(amount);
		
		Global.output(sender, Global.getLanguage("PAY-SENDER").replace("<player>", args[1]).replace("<amount>", args[2]));
		Global.output(playerAccount.getPlayer(), Global.getLanguage("PAY-TARGET").replace("<amount>", args[2]));
		
	}

	/**
	 * Handle money.
	 *
	 * @param sender the sender
	 * @param args the args
	 */
	private static void handleMoney(Player sender, String[] args) {
		
		if (!Global.hasPermission(sender, "bconomy.money", true)) {
			return;
		}
			
		Account playerAccount = null;
		
		if (args.length == 1) {
			playerAccount = Global.getAccounts().get(args[0]);
		}
		
		// not looking for an account and command is from console get out of here
		if (playerAccount == null && !(sender instanceof Player)) {
			Global.output(sender, Global.getLanguage("NO-CONSOLE"));
			return;
		} else if (playerAccount == null) {
			Player player = (Player)sender;
			playerAccount = Global.getAccounts().get(player.getName());
		}
		
		// could not find an account
		if (playerAccount == null) {
			Global.output(sender, Global.getLanguage("NO-ACCOUNT").replace("<player>", args.length == 1 ? args[0] : sender.getName()));
			return;
		}
		
		Global.output(sender, Global.getLanguage("LOOKUP-SENDER").replace("<player>", playerAccount.getPlayer().getName()).replace("<amount>", String.valueOf(playerAccount.getBalance())));
		}

	/**
	 * Show help.
	 *
	 * @param sender the sender
	 */
	//TODO change to use language
	private static void showHelp(Player sender) {

		sender.sendMessage(ChatColor.GOLD + "-- bConomy --");

		sender.sendMessage("/money [name] - Displays the amount of money in an account");
		sender.sendMessage("/money pay <name> <amount> - Pays a player");
		sender.sendMessage("/money top [amount] - Shows the top player balances");
		
		if (Global.hasPermission(sender, "bconomy.admin.grant", false)) {
			sender.sendMessage("/money grant <name> <amount> - Grants a player money");
			sender.sendMessage("/money grantall <amount> - Grants all players money");
		}
		
		if (Global.hasPermission(sender, "bconomy.admin.withdraw", false))
			sender.sendMessage("/money withdraw <name> <amount> - Withdraws from an account");
		
		if (Global.hasPermission(sender, "bconomy.admin.set", false))
			sender.sendMessage("/money set <name> <amount> - Sets a players balance");
		
		if (Global.hasPermission(sender, "bconomy.admin.reset", false))
			sender.sendMessage("/money reset (name) - Resets a players balance");
	}
}
