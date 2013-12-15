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
package uk.codingbadgers.bFundamentals.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;

/**
 * A minecraft command that is associated with a specific bFundamentals
 * {@link Module}.
 * 
 * @author James.
 */
public class ModuleCommand extends Command implements TabExecutor {

	protected String m_label = null;
	protected String m_usage = null;
	protected List<String> m_aliases = new ArrayList<String>();
	protected String m_description = null;
	protected ModuleCommandHelpTopic m_helpTopic = null;
	protected String m_permission = null;
	protected List<ModuleChildCommand> m_children = new ArrayList<ModuleChildCommand>();
	protected Module m_module;

	/**
	 * Instantiates a new module command.
	 * 
	 * @param module
	 *            the module this command is registered to
	 * @param label
	 *            the label
	 * @param usage
	 *            the usage
	 */
	public ModuleCommand(String label, String usage) {
		super(label);
		m_label = label;
		m_usage = usage == null ? "/" + m_label : usage;
		m_description = usage;
		m_helpTopic = new ModuleCommandHelpTopic(this);
		m_permission = "bfundamentals.command." + m_label;
		
	}

	/**
	 * Register this command to a module, called in
	 * {@link ModuleCommandHandler#registerCommand(Module, ModuleCommand)}.
	 * <p />
	 * <b>For internal use only<b/>.
	 * 
	 * @param module
	 *            to register it to
	 */
	public void register(Module module) {
		if (m_module != null) {
			throw new IllegalArgumentException("Command is already registered to a module");
		}

		m_module = module;
	}

	/**
	 * Gets the label.
	 * 
	 * @return the label
	 */
	@Override
	public String getLabel() {
		return m_label;
	}

	/**
	 * Sets the usage.
	 * 
	 * @param usage
	 *            the new usage
	 * @return the module command
	 */
	public ModuleCommand setUsage(String usage) {
		m_usage = usage;
		return this;
	}

	/**
	 * Gets the usage.
	 * 
	 * @return the usage
	 */
	@Override
	public String getUsage() {
		return m_usage;
	}

	/**
	 * Adds a aliase.
	 * 
	 * @param aliase
	 *            the aliase
	 * @return the module command
	 */
	public ModuleCommand addAliase(String aliase) {
		m_aliases.add(aliase.toLowerCase());
		return this;
	}

	/**
	 * Gets the aliases.
	 * 
	 * @return the aliases
	 */
	@Override
	public List<String> getAliases() {
		return m_aliases;
	}

	/**
	 * Gets the help text.
	 * 
	 * @return the help
	 */
	@Override
	public String getDescription() {
		return m_description;
	}

	/**
	 * Sets the description of this command.
	 * 
	 * @param description
	 *            the new help
	 * @return the module command
	 */
	public ModuleCommand setDescription(String description) {
		m_description = description;
		return this;
	}
	
	/**
	 * Sets the help text.
	 * 
	 * @param help the new help text
	 * @return the command instance
	 * @deprecated {@link #setDescription(String)}
	 */
	public ModuleCommand setHelp(String help) {
		return setDescription(help);
	}

	/**
	 * Gets the help topic.
	 * 
	 * @return the help topic
	 */
	public ModuleCommandHelpTopic getHelpTopic() {
		return m_helpTopic;
	}

	/**
	 * Gets the permission for this command.
	 * 
	 * @return the permission
	 */
	public String getPermission() {
		return m_permission;
	}

	/**
	 * Sets the permission for this command.
	 * 
	 * @param permission
	 *            the new permission
	 */
	public void setPermission(String permission) {
		this.m_permission = permission;
	}

	/**
	 * Add a new child command to this command.
	 * 
	 * @param command
	 *            the child command
	 */
	public void addChildCommand(ModuleChildCommand command) {
		this.m_children.add(command);
	}

	/**
	 * Internal command handling.
	 * 
	 * @param sender
	 *            the command sender
	 * @param label
	 *            the command label
	 * @param args
	 *            the command arguments
	 */
	@Override
	public final boolean execute(CommandSender sender, String label, String[] args) {
		if (args.length >= 1) {
			for (ModuleChildCommand child : m_children) {
				if (child.getLabel().equalsIgnoreCase(args[0])) {
					m_module.log(Level.INFO, child.getLabel());
					// cut first argument (sub command) out of command then handle as child command
					args = Arrays.copyOfRange(args, 1, args.length);
					return child.execute(sender, label, args);
				}
			}
		}

		// for backwards compatibility call old method first, will pass to new if left as default
		if (onCommand(sender, label, args)) {
			return true;
		}

		// call command method in module if still not handled
		if (m_module.onCommand(sender, label, args)) {
			return true;
		}
		
		// if not handled for any reason, send usage
		Module.sendMessage(m_module.getName(), sender, getUsage());
		return false;
	}

	/**
	 * Internal tab completion handling.
	 * 
	 * @param sender
	 *            the command sender
	 * @param label
	 *            the command label
	 * @param args
	 *            the command arguments
	 * @return the tab completed list
	 */
	@Override
    public final List<String> tabComplete(CommandSender sender, String alias, String[] args) throws CommandException, IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        List<String> completions = null;
        
        try {
        	completions = onTabComplete(sender, this, alias, args);
        } catch (Throwable ex) {
            StringBuilder message = new StringBuilder();
            message.append("Unhandled exception during tab completion for command '/").append(alias).append(' ');
            for (String arg : args) {
                message.append(arg).append(' ');
            }
            message.deleteCharAt(message.length() - 1).append("' in plugin ").append(m_module.getName());
            throw new CommandException(message.toString(), ex);
        }

        if (completions == null) {
            return super.tabComplete(sender, alias, args);
        }
        
        List<String> sortable = new ArrayList<String>(completions);
        Collections.sort(sortable, String.CASE_INSENSITIVE_ORDER);
        return ImmutableList.copyOf(completions);
    }
	
	/**
	 * Handle tab completion on the command.
	 * 
	 * @param sender
	 *            the command sender
	 * @param command
	 *            the command instance
	 * @param label
	 *            the label of the command
	 * @param args
	 *            the arguments added to the command
	 * @return true, if successful, false falls through to default module
	 *         command handling
	 * @see Module#onCommand(CommandSender, String, String[])
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {	
		Builder<String> builder = ImmutableList.builder();
		
		if (m_children.size() >= 1 && args.length == 1) {
			for (ModuleChildCommand child : m_children) {
				if (child.getLabel().startsWith(args[0])) {
					builder.add(child.getLabel());
				}
			}
		} else {
			if (args.length == 0) {
				return builder.build();
			}
			
			String name = args[args.length - 1];
			List<OfflinePlayer> players = m_module.matchPlayer(name, true);
			
			for (OfflinePlayer player : players) {
				builder.add(player.getName());
			}
		}
		
		return builder.build();
	}

	/**
	 * Handle the command.
	 * 
	 * @param sender
	 *            the command sender
	 * @param command
	 *            the command instance
	 * @param label
	 *            the label of the command
	 * @param args
	 *            the arguments added to the command
	 * @return true, if successful, false falls through to default module
	 *         command handling
	 * @see Module#onCommand(CommandSender, String, String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}

	/**
	 * Handle the command.
	 * 
	 * @param sender
	 *            the command sender
	 * @param label
	 *            the label of the command
	 * @param args
	 *            the arguments added to the command
	 * @return true, if successful, false falls through to default module
	 *         command handling
	 * @see Module#onCommand(CommandSender, String, String[])
	 */
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		return onCommand(sender, this, label, args);
	}
	
	protected void sendMessage(CommandSender sender, String message) {
		Module.sendMessage(m_module == null ? bFundamentals.getInstance().getName() : m_module.getName(), sender, message);
	}

}
