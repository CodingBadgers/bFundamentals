package uk.codingbadgers.bFundamentals.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import uk.codingbadgers.bFundamentals.module.Module;

/**
 * A minecraft command that is associated with a specific bFundamentals 
 * {@link Module}.
 *
 * @author James.
 */
public class ModuleCommand {

	/** The command label. */
	protected String m_label = null;
	
	/** The the usage text. */
	protected String m_usage = null;
	
	/** The aliases for that command. */
	protected List<String> m_aliases = new ArrayList<String>();
	
	/** the help text used by this command. */
	protected String m_help = null;
	
	/** The help topic. */
	protected ModuleCommandHelpTopic m_helpTopic = null;
	
	/** The permission for this command. */
	protected String m_permission = null;
	
	/**
	 * Instantiates a new module command.
	 *
	 * @param label the label
	 * @param usage the usage
	 */
	public ModuleCommand(String label, String usage) {
		m_label = label;
		m_usage = usage;
		m_helpTopic = new ModuleCommandHelpTopic(this);
		m_permission = "bfundamentals.command." + m_label;
	}
	
	/**
	 * Gets the label.
	 *
	 * @return the label
	 */
	public String getLabel() {
		return m_label;
	}
	
	/**
	 * Sets the usage.
	 *
	 * @param usage the new usage
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
	public String getUsage() {
		return m_usage;
	}
	
	/**
	 * Adds a aliase.
	 *
	 * @param aliase the aliase
	 * @return the module command
	 */
	public ModuleCommand addAliase(String aliase) {
		m_aliases.add(aliase.toLowerCase());
		return this;
	}
	
	/**
	 * Checks if is this command.
	 *
	 * @param label the label inputed by the user
	 * @return true, if it is this command
	 */
	public boolean isCommand(Object label) {
		if (!(label instanceof String))
			return false;
		
		return m_label.equalsIgnoreCase((String)label) || m_aliases.contains(((String)label).toLowerCase());
	}
	
	/**
	 * Gets the help.
	 *
	 * @return the help
	 */
	public String getHelp() {
		return m_help;
	}
	
	/**
	 * Sets the help.
	 *
	 * @param helpText the new help
	 * @return the module command
	 */
	public ModuleCommand setHelp(String helpText) {
		m_help = helpText;
		return this;
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
	 * Gets the aliases.
	 *
	 * @return the aliases
	 */
	public List<String> getAliases() {
		return m_aliases;
	}
	
	/**
	 * Handle the command.
	 *
	 * @param sender the command sender
	 * @param label the label of the command
	 * @param args the arguments added to the command
	 * @return true, if successful, false falls through to default module command handling
	 * @see Module#onCommand(CommandSender, String, String[])
	 */
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		return false;
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
	 * @param permission the new permission
	 */
	public void setPermission(String permission) {
		this.m_permission = permission;
	}
}
