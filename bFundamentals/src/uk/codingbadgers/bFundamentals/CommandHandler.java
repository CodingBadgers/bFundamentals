package uk.codingbadgers.bFundamentals;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import uk.codingbadgers.bFundamentals.module.Module;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class CommandHandler implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command commmand, String label, String[] args) {

		if (args.length < 1) {
			sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "bFundamentals commands");
			sender.sendMessage(ChatColor.DARK_AQUA + "module " + ChatColor.WHITE + "- access module load/unload/reload commands");
			sender.sendMessage(ChatColor.DARK_AQUA + "debug " + ChatColor.WHITE + "- debug a given module");
			sender.sendMessage(ChatColor.DARK_AQUA + "reload " + ChatColor.WHITE + "- reload the plugin");
			return true;	
		}
		
		if (!bFundamentals.getPermissions().has(sender, "bfundamentals.admin")) {
			sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Sorry you do not have permission to do that");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("reload")) {
			bFundamentals.getInstance().getPluginLoader().disablePlugin(bFundamentals.getInstance());
			bFundamentals.getInstance().getPluginLoader().enablePlugin(bFundamentals.getInstance());
			sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Reloading plugin");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("module")) {
			if (args.length < 1) {
				sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "bFundamentals module commands");
				sender.sendMessage(ChatColor.DARK_AQUA + "unload " + ChatColor.WHITE + "- unload a module");
				sender.sendMessage(ChatColor.DARK_AQUA + "load " + ChatColor.WHITE + "- load a module");
				sender.sendMessage(ChatColor.DARK_AQUA + "reload " + ChatColor.WHITE + "- reload a module");
				sender.sendMessage(ChatColor.DARK_AQUA + "debug " + ChatColor.WHITE + "- debug a module");
				return true;
			}
			
			if (args[1].equalsIgnoreCase("unload")) {
				if (args.length == 3) {
					Module module = bFundamentals.getModuleLoader().getModule(args[2]);
					
					if (module == null) {
						sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Sorry that module isn't enabled on bFundamentals.getInstance() server, do /modules for a list that are");
						return true;
					}
					bFundamentals.getModuleLoader().unload(module);
					sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Unloaded " + args[2]);
					return true;
				}
				
				bFundamentals.getModuleLoader().unload();
				sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "UnLoaded all modules");			
				return true;
			}
			
			if (args[1].equalsIgnoreCase("load")) {
				
				if (args.length == 3) {
					bFundamentals.getModuleLoader().load(args[2]);
					Module module = bFundamentals.getModuleLoader().getModule(args[2]);
					
					if(module == null) {
						sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Error loading module " + args[2]);
						return true;
					}
					
					module.onEnable();
					sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Loaded " + args[2]);
					return true;
				}
				
				bFundamentals.getModuleLoader().load();
				bFundamentals.getModuleLoader().enable();
				sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Loaded all modules");			
				return true;
			}
			
			if (args[1].equalsIgnoreCase("reload")) {
				
				if (args.length == 3) {
					Module module = bFundamentals.getModuleLoader().getModule(args[2]);
					
					if (module == null) {
						sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Sorry that module isn't enabled on this server, do /modules for a list that are");
						return true;
					}
					bFundamentals.getModuleLoader().unload(module);
					bFundamentals.getModuleLoader().load(args[2]);
					module = bFundamentals.getModuleLoader().getModule(args[2]);
					
					if(module == null) {
						sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Error loading module " + args[2]);
						return true;
					}
					
					module.onEnable();
					sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "ReLoaded " + args[2]);
					return true;
				}
				
				bFundamentals.getModuleLoader().unload();
				bFundamentals.getModuleLoader().load();
				bFundamentals.getModuleLoader().enable();
				sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Reloaded all modules");			
				return true;
			}
			
			if (args[1].equalsIgnoreCase("debug")) {
				
				if (args.length != 3) {
					sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "/bfundamentals module debug <module>");
					return true;
				}
				
				Module module = bFundamentals.getModuleLoader().getModule(args[2]);
				
				if (module == null) {
					sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "Module " + args[2] + " isn't loaded");
					return true;
				}
				
				module.setDebug(!module.isDebug());
				sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + module.getName() + ": debug " + (module.isDebug() ? "enabled" : "disabled"));
				return true;
			}
			
			sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "bFundamentals module commands");
			sender.sendMessage(ChatColor.DARK_AQUA + "unload " + ChatColor.WHITE + "- unload a module");
			sender.sendMessage(ChatColor.DARK_AQUA + "load " + ChatColor.WHITE + "- load a module");
			sender.sendMessage(ChatColor.DARK_AQUA + "reload " + ChatColor.WHITE + "- reload a module");
			sender.sendMessage(ChatColor.DARK_AQUA + "debug " + ChatColor.WHITE + "- debug a module");
			return true;
		}

		sender.sendMessage(ChatColor.DARK_AQUA + "[bFundamentals] " + ChatColor.WHITE + "bFundamentals commands");
		sender.sendMessage(ChatColor.DARK_AQUA + "module " + ChatColor.WHITE + "- access module load/unload/reload commands");
		sender.sendMessage(ChatColor.DARK_AQUA + "debug " + ChatColor.WHITE + "- debug a given module");
		sender.sendMessage(ChatColor.DARK_AQUA + "reload " + ChatColor.WHITE + "- reload the plugin");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sebder, Command command, String label, String[] args) {
		if (args.length == 0) {
			return ImmutableList.of("module", "debug", "reload");
		}
		
		if (args.length == 1) {
			Builder<String> list = ImmutableList.builder();
			for (String string : Arrays.asList("module", "debug", "reload")) {
				if (string.startsWith(args[0])) {
					list.add(string);
				}
			}
			return list.build();
		}
		
		if (args.length == 2) {
			Builder<String> list = ImmutableList.builder();
			for (String string : Arrays.asList("unload", "load", "reload", "debug")) {
				if (string.startsWith(args[0])) {
					list.add(string);
				}
			}
			return list.build();
		}
		
		return ImmutableList.of();
	}

}
