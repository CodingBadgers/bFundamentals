package uk.codingbadgers.bFundamentals.bungee;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import uk.codingbadgers.bFundamentals.bFundamentals;

public class BungeePlayerListener implements Listener {

    private BungeeMessenger messenger;

    public BungeePlayerListener(BungeeMessenger messenger) {
        this.messenger = messenger;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(bFundamentals.getInstance(), new Runnable() {

            @Override
            public void run() {
                messenger.sendQueuedCommands();
            }
            
        }, 5);
    }
}
