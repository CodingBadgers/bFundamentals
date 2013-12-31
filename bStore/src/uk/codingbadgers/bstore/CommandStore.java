package uk.codingbadgers.bstore;

import java.util.logging.Level;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bstore.database.DatabaseManager;
import uk.codingbadgers.bstore.database.InvestorData;

public class CommandStore extends ModuleCommand {

    final private bStore module;
    final private DatabaseManager databasemanager;

    public CommandStore(bStore module) {
        super("store", "/store <command> [param] [...]");
        this.module = module;
        this.databasemanager = module.getDatabaseManager();
    }

    /**
     * Called when the 'store' command is executed.
     * usage: /store purchase <playername> investor <length>
     * usage: /store purchase <playername> xp <number of levels>
     * usage: /store purchase <playername> money <amount>
     * usage: /store purchase <playername> mobpackage <name>
     * usage: /store list [playername]
     * @return True if handled 
     */
    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {

        if (args.length == 0) {
            Module.sendMessage(module.getName(), sender, "Usage...");
            Module.sendMessage(module.getName(), sender, "/store purchase");
            Module.sendMessage(module.getName(), sender, "/store list");
            return true;
        }
        
        final String subCommand = args[0];
        if (subCommand.equalsIgnoreCase("purchase")) {
            handlePurchase(sender, args);
        } else if (subCommand.equalsIgnoreCase("list")) {
            handleList(sender, args);
        } else {
            Module.sendMessage(module.getName(), sender, "Usage...");
            Module.sendMessage(module.getName(), sender, "/store purchase");
            Module.sendMessage(module.getName(), sender, "/store list");            
        }
        
        return true;
    }

    /**
     * usage: /store purchase <playername> investor <length>
     * usage: /store purchase <playername> xp <number of levels>
     * usage: /store purchase <playername> money <amount>
     * usage: /store purchase <playername> mobpackage <name>
     * @param sender
     * @param args 
     */
    private void handlePurchase(CommandSender sender, String[] args) {
        
        if (!sender.hasPermission("bStore.purchase")) {
            Module.sendMessage(module.getName(), sender, "You do not have the required permission 'bStore.purchase'.");
            return;
        }
        
        if (args.length != 4) {
            Module.sendMessage(module.getName(), sender, "Usage...");
            Module.sendMessage(module.getName(), sender, "/store purchase <playername> investor <length>");
            Module.sendMessage(module.getName(), sender, "/store purchase <playername> xp <number of levels>");
            Module.sendMessage(module.getName(), sender, "/store purchase <playername> money <amount>");
            Module.sendMessage(module.getName(), sender, "/store purchase <playername> mobpackage <name>");
            return;
        }
        
        final String playerName = args[1];
        final String itemName = args[2];
        
        if (itemName.equalsIgnoreCase("investor")) {
            handlePurchaseInvestor(sender, playerName, args[3]);
        } else if (itemName.equalsIgnoreCase("xp")) {
            handlePurchaseXP(sender, playerName, args[3]);
        } else if (itemName.equalsIgnoreCase("money")) {
            handlePurchaseMoney(sender, playerName, args[3]);
        } else if (itemName.equalsIgnoreCase("mobpackage")) {
            handlePurchaseMobPackage(sender, playerName, args[3]);
        }
        
    }

    /**
     * 
     * @param sender
     * @param args 
     */
    private void handleList(CommandSender sender, String[] args) {
        
        if (!sender.hasPermission("bStore.list")) {
            Module.sendMessage(module.getName(), sender, "You do not have the required permission 'bStore.list'.");
            return;
        }
        
        
    }

    /**
     * 
     * @param sender
     * @param playerName
     * @param string 
     */
    private void handlePurchaseInvestor(CommandSender sender, String playerName, String length) {
        
        if (!sender.hasPermission("bStore.purchase.investor")) {
            Module.sendMessage(module.getName(), sender, "You do not have the required permission 'bStore.purchase.investor'.");
            return;
        }
        
        // See how long the investment is for
        long investorLength = 0;
        final long oneDay = 1000 * 60 * 60 * 24;
        if (length.equalsIgnoreCase("1week")) {
            investorLength = oneDay * 7;
        } else if (length.equalsIgnoreCase("1month")) {
            investorLength = oneDay * 30;
        } else if (length.equalsIgnoreCase("3month")) {
            investorLength = oneDay * 90;
        } else {
            try {
                investorLength = Integer.parseInt(length) * 1000 * 60; // if it isnt a string, presume length in minutes
            } catch (NumberFormatException ex) {
                Module.sendMessage(module.getName(), sender, "Invalid investment length. Valid: 1week, 1month, 3month, [x] in minutes");
                bFundamentals.log(Level.WARNING, "Invalid length of investment specified.", ex);
                return;
            }
        }
        
        Permission perms = bFundamentals.getPermissions();
        
        String oldRank = perms.getPrimaryGroup((World)null, playerName);
        String[] groups = perms.getPlayerGroups((World)null, playerName);
        
        // Remove all groups
        for (String group : groups) {
            perms.playerRemoveGroup((World)null, playerName, group);
        }
        
        // Set to investor
        perms.playerAddGroup((World)null, playerName, "investor");
        
        // Add additional groups back
        for (String group : groups) {
            if (group.equalsIgnoreCase(oldRank)) {
                continue;
            }
            perms.playerAddGroup((World)null, playerName, group);
        }
                
        // Log the end time of the investor
        long from = System.currentTimeMillis();
        long to = from + investorLength;
        
        // Extend an investors time
        InvestorData investor = this.databasemanager.getInvestor(playerName);
        if (investor != null) {
            from = investor.startTime;
            to = investor.endTime + investorLength;
        }
        
        this.databasemanager.logInvestor(playerName, from, to, oldRank, investor != null);        
        
        // Log the purchase
        logPurchase(playerName, "Investor", length);
        
        Module.sendMessage(module.getName(), sender, "[Sucess] Investor Purchase - '" + playerName + "' length '" + length + "' old rank '" + oldRank + "' .");
        
    }

    /**
     * 
     * @param sender
     * @param playerName
     * @param string 
     */
    private void handlePurchaseXP(CommandSender sender, String playerName, String levels) {
        
        if (!sender.hasPermission("bStore.purchase.xp")) {
            Module.sendMessage(module.getName(), sender, "You do not have the required permission 'bStore.purchase.xp'.");
            return;
        }
        
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
        if (player.isOnline()) {
            
            try {
                player.getPlayer().giveExpLevels(Integer.parseInt(levels));
            } catch(NumberFormatException ex) {
                Module.sendMessage(module.getName(), sender, "Failed to purchase xp for player '" + playerName + "' levels '" + levels + "'.");
                bFundamentals.log(Level.WARNING, "Failed to purchase xp for player '" + playerName + "' levels '" + levels + "'.", ex);
                return;
            }
            
        } else {
            
            // Store to database and give on login.
            // TODO:
            
        }
        
        logPurchase(playerName, "XP", levels + " Levels");
        
        Module.sendMessage(module.getName(), sender, "[Sucess] XP Purchase - '" + playerName + "' levels '" + levels + "'.");
        
    }

    /**
     * 
     * @param sender
     * @param playerName
     * @param string 
     */
    private void handlePurchaseMoney(CommandSender sender, String playerName, String amount) {
        
        if (!sender.hasPermission("bStore.purchase.money")) {
            Module.sendMessage(module.getName(), sender, "You do not have the required permission 'bStore.purchase.money'.");
            return;
        }
        
        try {
            bFundamentals.getEconomy().depositPlayer(playerName, Double.parseDouble(amount));
        } catch(NumberFormatException ex) {
            Module.sendMessage(module.getName(), sender, "Failed to purchase money for player '" + playerName + "' amount '" + amount + "'.");
            bFundamentals.log(Level.WARNING, "Failed to purchase money for player '" + playerName + "' amount '" + amount + "'.", ex);
            return;
        } 
        
        logPurchase(playerName, "Money", "£" + amount);
        
        Module.sendMessage(module.getName(), sender, "[Sucess] Money Purchase - '" + playerName + "' amount '" + amount + "'.");
    }

    /**
     * 
     * @param sender
     * @param playerName
     * @param string 
     */
    private void handlePurchaseMobPackage(CommandSender sender, String playerName, String type) {
        
        if (!sender.hasPermission("bStore.purchase.mobpackage")) {
            Module.sendMessage(module.getName(), sender, "You do not have the required permission 'bStore.purchase.mobpackage'.");
            return;
        }
        
        logPurchase(playerName, "MobPackage", type);
        
    }

    /**
     * 
     * @param playerName
     * @param investor
     * @param length 
     */
    private void logPurchase(String playerName, String type, String item) {
        this.databasemanager.logPurchase(playerName, type, item);
    }

}
