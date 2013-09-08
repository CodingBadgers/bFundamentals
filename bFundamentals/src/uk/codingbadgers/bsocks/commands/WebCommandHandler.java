/**
 * bFundamentals 1.2-SNAPSHOT
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
package uk.codingbadgers.bsocks.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class WebCommandHandler, handles registering and handling of web
 * commands for bSocks.
 */
public class WebCommandHandler {

	/** The commands. */
	private static List<WebCommand> commands = new ArrayList<WebCommand>();

	/**
	 * Register command a web command.
	 *
	 * @param command the command
	 * @see WebCommand
	 */
	public static void registerCommand(WebCommand command) {
		WebCommandHandler.commands.add(command);
	}
	
	/**
	 * Gets the commands registered for bSocks.
	 *
	 * @return all the commands registered
	 */
	public static List<WebCommand> getCommands() {
		return commands;
	}
}
