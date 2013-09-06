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

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;

import uk.codingbadgers.bFundamentals.module.Module;

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
	public boolean canSee(CommandSender sender) {
		if (!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;
		
		return Module.hasPermission(player, m_command.getPermission()) || Module.hasPermission(player, "bfundamentals.admin");
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
        sb.append(m_command.getDescription());

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
