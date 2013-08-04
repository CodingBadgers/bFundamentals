package uk.codingbadgers.bFundamentals.commands;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;

import uk.codingbadgers.bFundamentals.module.Module;

public class ModuleCommandHandler {

	private static Map<Module, List<ModuleCommand>> commands = new HashMap<Module, List<ModuleCommand>>();
	private static CommandMap commandMap;
	
	static {
		setupCommandMap();
	}

	private static void setupCommandMap() {
		// reflection to make sure we don't have to update with minecraft version
		// TODO check each update to make sure they don't change the field name
		try {
			Class<? extends PluginManager> clazz = Bukkit.getServer().getPluginManager().getClass();
			Field field = clazz.getDeclaredField("commandMap");
			field.setAccessible(true);
			commandMap = (CommandMap) field.get(Bukkit.getServer().getPluginManager());
		} catch (Exception e) {
			e.printStackTrace();
			commandMap = null;
		}
	}
	
	public static void registerCommand(Module module, ModuleCommand command) {
		command.register(module);
		commandMap.register(module.getName(), command);
		
		if (commands.containsKey(module)) {
			commands.get(module).add(command);
		} else {
			commands.put(module, new ArrayList<ModuleCommand>(Arrays.asList(command)));
		}
	}

	@SuppressWarnings("unchecked")
	public static void deregisterCommand(Module module, ModuleCommand command) {
		
		List<ModuleCommand> commands = new ArrayList<ModuleCommand>(ModuleCommandHandler.commands.get(module));
		for (int i = 0; i < commands.size(); i++) {
			ModuleCommand current = commands.get(i);
			if (current.equals(command)) {
				ModuleCommandHandler.commands.get(module).remove(i);
			}
		}
		
		Field commandsField = null;
		
		if (commandMap instanceof SimpleCommandMap) {
			try {
				commandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
				commandsField.setAccessible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return;
		}
		
		try {
			((Map<String, Command>)commandsField.get(commandMap)).remove(command.getLabel().toLowerCase());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
