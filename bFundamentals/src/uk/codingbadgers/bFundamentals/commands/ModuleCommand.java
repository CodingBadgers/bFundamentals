package uk.codingbadgers.bFundamentals.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * The command object
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
	
	/**
	 * Instantiates a new module command.
	 *
	 * @param label the label
	 * @param usage the usage
	 */
	public ModuleCommand(String label, String usage) {
		m_label = label;
		m_usage = usage;
	}
	
	/**
	 * Sets the usage.
	 *
	 * @param usage the new usage
	 */
	public void setUsage(String usage) {
		m_usage = usage;
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
}
