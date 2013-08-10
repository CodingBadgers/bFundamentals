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
