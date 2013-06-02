package uk.codingbadgers.bHelpful.commands.rw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HelpfulCommandHandler {

	public static List<ConfigCommand> commands = new ArrayList<ConfigCommand>();
	
	static {
		// register commands
		commands.add(new NewsCommand());
	}
	
	public static List<ConfigCommand> getCommands() {
		return new ArrayList<ConfigCommand>(commands);
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
