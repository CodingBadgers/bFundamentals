package uk.codingbadgers.bFundamentals.commands;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
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
}
