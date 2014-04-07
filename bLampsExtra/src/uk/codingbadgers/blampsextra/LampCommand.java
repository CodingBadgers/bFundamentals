/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.blampsextra;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

/**
 *
 * @author n3wton
 */
public class LampCommand extends ModuleCommand {

    /**
     * 
     */
    public List<Player> activePlayers = new ArrayList<Player>();
    
    /**
     * 
     */
    public List<Player> activeForcePlayers = new ArrayList<Player>();
    
    /**
     * 
     */
    public LampCommand() {
        super("lamp", "/lamp <force>");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player)sender;
        if (args.length == 1) {
            // Force
            if (this.activePlayers.contains(player)) {
                this.activePlayers.remove(player);
            }
            
            if (this.activeForcePlayers.contains(player)) {
                this.activeForcePlayers.remove(player);
                Module.sendMessage("bLampsExtra", sender, "Force lamp creation mode disabled!");
            } else {
                this.activeForcePlayers.add(player);
                Module.sendMessage("bLampsExtra", sender, "Force lamp creation mode enabled!");
            }
            
        }   
        else if (args.length == 0) {
            
            // Night only
            if (this.activeForcePlayers.contains(player)) {
                this.activeForcePlayers.remove(player);
            }
            
            if (this.activePlayers.contains(player)) {
                this.activePlayers.remove(player);
                Module.sendMessage("bLampsExtra", sender, "Lamp creation mode disabled!");
            } else {
                this.activePlayers.add(player);
                Module.sendMessage("bLampsExtra", sender, "Lamp creation mode enabled!");
            }
            
        }
        else {
            Module.sendMessage("bLampsExtra", sender, this.getUsage());
        }
        
        return true;
    }
    
}
