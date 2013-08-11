package uk.codingbadgers.bFundamentals.module;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;
import org.bukkit.util.ChatPaginator;

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;

/**
 * The help topic for a module
 */
public class ModuleHelpTopic extends HelpTopic {

	/** The module instance that help topic is for. */
	private Module m_module = null;
	
	/** The commands list. */
	private List<ModuleCommand> m_commands = null;
	
	/**
	 * Instantiates a new module help topic.
	 *
	 * @param module the module
	 */
	public ModuleHelpTopic (Module module) {
		m_module = module;
		m_commands = module.getCommands();
		
		name = m_module.getName();
		shortText = "All commands for " + m_module.getName();
	}
	
	@Override
	public String getFullText(CommandSender forWho) {
		StringBuilder sb = new StringBuilder();
		for (ModuleCommand command : m_module.getCommands()) {
               String lineStr = buildLine(command).replace("\n", ". ");
               if (lineStr.length() > ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH) {
                   sb.append(lineStr.substring(0, ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH - 3));
                   sb.append("...");
               } else {
                   sb.append(lineStr);
               }
               sb.append("\n");         
		}
		return sb.toString();
	}

	/**
	 * Builds the index line.
	 *
	 * @param command the command
	 * @return the string
	 */
	protected String buildLine(ModuleCommand command) {
        StringBuilder line = new StringBuilder();
        line.append(ChatColor.GOLD);
        line.append("/" + command.getLabel());
        line.append(": ");
        line.append(ChatColor.WHITE);
        line.append(command.getHelpTopic().getShortText());
        return line.toString();
    }
	
	/* (non-Javadoc)
	 * @see org.bukkit.help.HelpTopic#canSee(org.bukkit.command.CommandSender)
	 */
	@Override
	public boolean canSee(CommandSender arg0) {
		return m_commands.size() != 0;
	}

}
