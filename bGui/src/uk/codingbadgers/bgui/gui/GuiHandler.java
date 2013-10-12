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

import java.io.File;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import uk.codingbadgers.bFundamentals.module.Module;

public class GuiHandler {

    private static Gui gui = null;

    public static void loadGui(Module plugin) throws Exception {
        File file = new File(plugin.getDataFolder(), "gui.json");
        
        if (!file.exists()) {
            throw new Exception("Could not find gui file");
        }
        
        JSONParser parser = new JSONParser();
        
        JSONObject json = (JSONObject) parser.parse(new FileReader(new File(plugin.getDataFolder(), "gui.json")));
        Gui gui = new Gui(json);
        plugin.getLogger().info("Loaded gui '" + gui.getName() + "'");
        GuiHandler.gui = gui;
    }

    public static Gui getGui() {
        return gui;
    }
}
