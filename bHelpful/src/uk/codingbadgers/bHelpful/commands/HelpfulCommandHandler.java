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
package uk.codingbadgers.bHelpful.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HelpfulCommandHandler {

	public static List<ConfigCommand> commands = new ArrayList<ConfigCommand>();
	
	public static List<ConfigCommand> getCommands() {
		return new ArrayList<ConfigCommand>(commands);
	}
	
	public static void registerCommand(ConfigCommand command) {
		command.loadCommand();
		commands.add(command);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends ConfigCommand> T getCommand(Class<? extends T> clazz) {
		Iterator<? extends ConfigCommand> itr = commands.iterator();
		
		while(itr.hasNext()) {
			ConfigCommand current = itr.next();
			if (current.getClass().equals(clazz)) {
				return (T) current;
			}
		}
		
		return null;
	}
}
