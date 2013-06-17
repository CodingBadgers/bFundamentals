package uk.codingbadgers.bFundamentals.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

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
		m_usage = usage;
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
		for (ModuleChildCommand child : m_children) {
			if (child.getLabel().equalsIgnoreCase(label)) {
				return child.execute(sender, label, args);
			}
		}

		// for backwards compatibility call old method
		if (onCommand(sender, label, args)) {
			return true;
		}

		return m_module.onCommand(sender, label, args);
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
		return null;
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

}
