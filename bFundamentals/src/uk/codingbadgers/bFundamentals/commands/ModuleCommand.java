package uk.codingbadgers.bFundamentals.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * The command object.
 *
 * @author james.
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
		m_aliases.add(aliase);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object label) {
		if (!(label instanceof String))
			return false;
		
		return m_label.equalsIgnoreCase((String)label) || m_aliases.contains((String)label);
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
}
