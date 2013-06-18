package uk.codingbadgers.bFundamentals.commands;

public class ModuleChildCommand extends ModuleCommand {

	protected ModuleCommand m_parent;

	public ModuleChildCommand(ModuleCommand parent, String label) {
		super(label, parent.getUsage());
		m_parent = parent;
	}
	
	public ModuleCommand getParent() {
		return m_parent;
	}

}
