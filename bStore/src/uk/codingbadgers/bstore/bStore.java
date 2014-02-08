package uk.codingbadgers.bstore;

import java.util.ArrayList;
import java.util.List;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bstore.database.DatabaseManager;
import uk.codingbadgers.bstore.database.InvestorData;
import uk.codingbadgers.bstore.database.XPPurchaseData;

public class bStore extends Module implements Listener {

    private DatabaseManager databaseManager;
    
    /**
     * Called when the module is disabled.
     */
    @Override
    public void onDisable() {
    }

    /**
     * Called when the module is loaded.
     */
    @Override
    public void onEnable() {
        
        databaseManager = new DatabaseManager();
        
        // Register this as a listener
        register(this);
        
        // Register all commands
        registerCommand(new CommandStore(this));
        
        // See if investors have run out
        checkInvestors();
    }
    
    /**
     * Get the database manager instance
     * @return The database manager instance
     */
    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    /**
     * Called when a player joins the server
     * Used to given any items which require the player to be online,
     * and to demote if their investor time has run out.
     * @param event The event containing join information
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {

        final Player player = event.getPlayer();
        final String playerName = player.getName();
        
        InvestorData investor = this.databaseManager.getInvestor(playerName);
        if (investor != null) {
            final long now = System.currentTimeMillis();
            if (investor.endTime < now) {
                // Investor has run out...
                demoteInvestor(investor);            
            } else {
                final long timeRemaining = investor.endTime - now;
                Bukkit.getScheduler().runTaskLater(m_plugin, new Runnable() {
                    @Override
                    public void run() {
                        Module.sendMessage(getName(), player, "Your investor runs out in " + formatMSTime(timeRemaining) + ".");
                    }
                }, 5 * 20L);
            }
        }
        
        // Give xp for offline purchases
        List<XPPurchaseData> xpData = this.databaseManager.getXPPurchases(playerName);
        for (XPPurchaseData data : xpData) {
            player.giveExpLevels(data.levels);
        }
        this.databaseManager.removeXPPurchases(playerName);
        
    }
    
    /**
     * 
     * @param timeMs
     * @return 
     */
    private String formatMSTime(long timeMs) {

        int minutes = (int)((timeMs / (1000*60)) % 60);
        int hours   = (int)((timeMs / (1000*60*60)) % 24);
        int days = (int)((timeMs / (1000*60*60*24)) % 7);
        int weeks = (int)((timeMs / (1000*60*60*24*7)));

        List<String> parts = new ArrayList<String>();
        if (weeks != 0) {
            parts.add(weeks + " " + (weeks == 1 ? "week" : "weeks"));
        }
        if (days != 0) {
            parts.add(days + " " + (days == 1 ? "day" : "days"));
        }
        if (hours != 0) {
            parts.add(hours + " " + (hours == 1 ? "hour" : "hours"));
        }
        if (minutes != 0) {
            parts.add(minutes + " " + (minutes == 1 ? "minute" : "minutes"));
        }
        
        String formattedTime = "";
        if (parts.isEmpty()) {
            formattedTime = "now";
        }
        else if (parts.size() == 1) {
            formattedTime = parts.get(0);
        }
        else {
            for (int partIndex = 0; partIndex < parts.size() - 1; ++partIndex) {
                if (partIndex == (parts.size() - 2)) {
                    formattedTime += parts.get(partIndex) + " and ";
                } else {
                    formattedTime += parts.get(partIndex) + ", ";
                }
            }
            formattedTime += parts.get(parts.size() - 1);
        }
        
        return formattedTime;
    }

    /**
     * 
     * @param investor 
     */
    private void demoteInvestor(InvestorData investor) {
        
        Permission perms = bFundamentals.getPermissions();
        String[] groups = perms.getPlayerGroups((World)null, investor.playerName);
        
        // Remove all groups
        for (String group : groups) {
            perms.playerRemoveGroup((World)null, investor.playerName, group);
        }
        
        // Set to old rank
        perms.playerAddGroup((World)null, investor.playerName, investor.oldRank);
        
        // Add additional groups back
        for (String group : groups) {
            if (group.equalsIgnoreCase("investor")) {
                continue;
            }
            perms.playerAddGroup((World)null, investor.playerName, group);
        }
        
        Player player = Bukkit.getPlayer(investor.playerName);
        if (player != null) {
            Module.sendMessage(getName(), player, "Your investor has run out.");
            Module.sendMessage(getName(), player, "To continue to recieve your investor perks, please re-invest.");
            Module.sendMessage(getName(), player, ChatColor.UNDERLINE + "" + ChatColor.AQUA + "http://mcbadgercraft.com/store/5-investor-perks");
        }
        
        this.databaseManager.removeInvestor(investor.playerName);
    }

    /**
     * 
     */
    private void checkInvestors() {
        
        final int checkMins = 5; // Number of minutes between checks
        
        Bukkit.getScheduler().runTaskLater(m_plugin, new Runnable() {

            @Override
            public void run() {
                final long now = System.currentTimeMillis();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    InvestorData investor = databaseManager.getInvestor(player.getName());
                    if (investor == null) {
                        continue;
                    }
                    if (investor.endTime < now) {
                        // Investor has run out...
                        demoteInvestor(investor);            
                    }
                }                
            }
            
        }, checkMins * 60L * 20L);
        
    }

    /**
     * 
     * @param type
     * @return 
     */
    ItemPackage getItemPackage(String type) {
        
        
        
        return null;
    }

}
