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
package uk.codingbadgers.bFundamentals.module;

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
	
	/**
	 * Instantiates a new module help topic.
	 *
	 * @param module the module
	 */
	public ModuleHelpTopic (Module module) {
		m_module = module;
		
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
		return m_module.getCommands().size() != 0;
	}

}
