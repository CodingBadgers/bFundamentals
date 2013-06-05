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
