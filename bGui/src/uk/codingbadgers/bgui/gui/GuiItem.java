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
package uk.codingbadgers.bgui.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import uk.codingbadgers.bgui.click.ClickHandler;

public class GuiItem {

    private final ItemStack item;
    private final ClickHandler handler;
    
    public GuiItem(ItemStack item, ClickHandler handler) {
        this.item = item;
        this.handler = handler;
    }
    
    public ItemStack getItem() {
        return item;
    }
    
    public void handleClick(Player player) {
        handler.handle(player);
    }
}
