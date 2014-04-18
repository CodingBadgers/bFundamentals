/**
 * bHelpful 1.2-SNAPSHOT
 * Copyright (C) 2013  CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.codingbadgers.bHelpful;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bHelpful.commands.*;

/**
 * The base bHelpful module class
 *
 * @author James
 */
public class bHelpful extends Module {
	
	/** The plugin instance. */
	public static bFundamentals PLUGIN = null;
	
	/** The module instance. */
	public static bHelpful MODULE = null;
    
    /* (non-Javadoc)
     * @see uk.codingbadgers.bFundamentals.module.Module#onDisable()
     */
    public void onDisable() {
    	log(Level.INFO, "bHelpful disabled");
    }

    /* (non-Javadoc)
     * @see uk.codingbadgers.bFundamentals.module.Module#onLoad()
     */
    public void onLoad() {
    	MODULE = this;
        PLUGIN = m_plugin;
    }
    
    /* (non-Javadoc)
     * @see uk.codingbadgers.bFundamentals.module.Module#onEnable()
     */
    public void onEnable() {   
        register(new PlayerListener(this));

        registerConfig(Config.class);
        
        registerCommand(new AnnouncementCommand());
        registerCommand(new MaintenanceCommand());
        registerCommand(new MotdCommand());
        registerCommand(new NewsCommand());
        registerCommand(new PlayerListCommand());
        registerCommand(new RegisterCommand());
        registerCommand(new RulesCommand());
        registerCommand(new VoteCommand());
        
        log(Level.INFO, "bHelpful enabled");      		
    }

	public static boolean hasPermission(CommandSender sender, String node) {
		if (!(sender instanceof Player)) {
			return true;
		}
		
		return hasPermission((Player)sender, node);
	}
    
}
