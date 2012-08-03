package uk.codingbadgers.bFundamentals;

import java.util.ArrayList;
import java.util.List;

public class ModuleCommand {

	protected String m_label = null;
	protected String m_usage = null;
	protected List<String> m_aliases = new ArrayList<String>();
	
	public ModuleCommand(String label, String usage) {
		m_label = label;
		m_usage = usage;
	}
	
	public void setUsage(String usage) {
		m_usage = usage;
	}
	
	public String getUsage() {
		return m_usage;
	}
	
	public ModuleCommand addAliase(String aliase) {
		m_aliases.add(aliase);
		return this;
	}
	
	public boolean equals(Object label) {
		if (!(label instanceof String))
			return false;
		
		return m_label.equalsIgnoreCase((String)label) || m_aliases.contains((String)label);
	}
}
