package uk.codingbadgers.bsocks.commands;

import java.util.ArrayList;
import java.util.List;

public class WebCommandHandler {

	private static List<WebCommand> commands = new ArrayList<WebCommand>();

	public static void registerCommand(WebCommand command) {
		WebCommandHandler.commands.add(command);
	}
	
	public static List<WebCommand> getCommands() {
		return commands;
	}
}
