package uk.codingbadgers.bFundamentals.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;

/**
 * The help topic for module commands
 */
public class ModuleCommandHelpTopic extends HelpTopic {

	/** The command instance for this help topic. */
	private ModuleCommand m_command;
	
	/**
	 * Instantiates a new module command help topic.
	 *
	 * @param command the command
	 */
	public ModuleCommandHelpTopic(ModuleCommand command) {
		m_command = command;
		this.name = "/" + command.getLabel();
		this.shortText = command.getUsage();
		this.fullText = command.getUsage();		
	}
	
	/* (non-Javadoc)
	 * @see org.bukkit.help.HelpTopic#canSee(org.bukkit.command.CommandSender)
	 */
	@Override
	public boolean canSee(CommandSender arg0) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.bukkit.help.HelpTopic#getFullText(org.bukkit.command.CommandSender)
	 */
	@Override 
	public String getFullText(CommandSender sender) {
		// Build full text
        StringBuilder sb = new StringBuilder();

        sb.append(ChatColor.GOLD);
        sb.append("Description: ");
        sb.append(ChatColor.WHITE);
        sb.append(m_command.getHelp());

        sb.append("\n");

        sb.append(ChatColor.GOLD);
        sb.append("Usage: ");
        sb.append(ChatColor.WHITE);
        sb.append(m_command.getUsage().replace("<command>", name));

        if (m_command.getAliases().size() > 0) {
            sb.append("\n");
            sb.append(ChatColor.GOLD);
            sb.append("Aliases: ");
            sb.append(ChatColor.WHITE);
            sb.append(ChatColor.WHITE + StringUtils.join(m_command.getAliases(), ", "));
        }
        return sb.toString();
	}

}
