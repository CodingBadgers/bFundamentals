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
package uk.codingbadgers.bgui.click;

import java.lang.reflect.Constructor;

import org.json.simple.JSONObject;

import uk.codingbadgers.bgui.exception.GuiFormatException;

import com.google.common.base.Throwables;

public enum ActionType {

    WARP(WarpHandler.class),
    COMMAND(CommandHandler.class),
    MESSAGE(MessageHandler.class),
    SERVER(ServerHandler.class),
    ;

    private Constructor<? extends ClickHandler> cotr;

    private ActionType(Class<? extends ClickHandler> clazz) {
        try {
            this.cotr = clazz.getDeclaredConstructor(String.class);
            this.cotr.setAccessible(true);
        } catch (Throwable e) {
            Throwables.propagate(e);
        }
    }
    
    public ClickHandler newHandler(String value) {
        try {
            return cotr.newInstance(value);
        } catch (Throwable e) {
            Throwables.propagate(e);
            return new MessageHandler("An unexpected error has occured, please inform staff");
        } 
    }
    
    public static ClickHandler getClickHandler(JSONObject json) {
        String type = (String) json.get("type");
        String value = (String) json.get("value");
        
        ActionType actiontype = valueOf(type);
        
        if (actiontype == null) {
            throw new GuiFormatException(type + " is not a valid action type");
        }
        
        return actiontype.newHandler(value);
    }
}
