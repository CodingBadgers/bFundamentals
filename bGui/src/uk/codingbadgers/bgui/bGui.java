/**
 * bGui 1.2-SNAPSHOT
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
package uk.codingbadgers.bgui;

import java.util.logging.Level;

import org.bukkit.Bukkit;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.bgui.gui.GuiHandler;

public class bGui extends Module {

    @Override
    public void onEnable() {
        
        try {
            GuiHandler.loadGui(this);
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "An exception occurred whilst loading the gui.");
            getLogger().log(Level.SEVERE, "Disabling module", e);
            setEnabled(true);
        }
        
        register(new PlayerListener());
        
        Bukkit.getMessenger().registerOutgoingPluginChannel(bFundamentals.getInstance(), "BungeeCord");
    }

    @Override
    public void onDisable() {
        
    }

}
