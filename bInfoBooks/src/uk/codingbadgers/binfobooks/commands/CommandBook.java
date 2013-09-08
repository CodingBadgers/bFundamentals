/**
 * bInfoBooks 1.2-SNAPSHOT
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
package uk.codingbadgers.binfobooks.commands;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.binfobooks.InfoBook;
import uk.codingbadgers.binfobooks.bInfoBooks;

public class CommandBook extends ModuleCommand {
	
	private final bInfoBooks m_module;

	/**
	 * Command constructor.
	 */
	public CommandBook(bInfoBooks intance) {
		super("book", "book | book <name>");
		m_module = intance;
	}
	
	/**
	 * Called when the 'book' command is executed.
	 */
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			bFundamentals.log(Level.INFO, "Book commands must be used in game");
			return true;
		}
		
		Player player = (Player)sender;
		
		// List books
		if (args.length == 0) {
			
			if (Module.hasPermission(player, "binfobooks.list")) {
				Module.sendMessage("bInfoBooks", player, "The following books are avaliable...");
				m_module.listBooks(player);				
			} else {
				Module.sendMessage("bInfoBooks", player, "You do not have permission to list books. [binfobooks.list]");
			}
			
			return true;
		}
		
		if (!Module.hasPermission(player, "binfobooks.get")) {
			Module.sendMessage("bInfoBooks", player, "You do not have permission to get books. [binfobooks.get]");
			return true;
		}
				
		// Get the name of the requested book
		String bookName = "";
		for (String arg : args) {
			bookName = bookName + arg + " ";
		}
		bookName = bookName.substring(0, bookName.length() - 1);
		
		boolean silent = false;
		if (bookName.length() > 1 && bookName.substring(bookName.length() - 2).equalsIgnoreCase("-s")) {
			silent = true;
			bookName = bookName.substring(0, bookName.length() - 3);
		}
		
		// See if the book exists
		InfoBook book = m_module.bookExists(bookName);
		if (book == null) {
			if (!silent) {
				Module.sendMessage("bInfoBooks", player, "No book by the name '" + bookName + "' exists.");			
			}
			return true;
		}
		
		// See if player already has the same book
		if (m_module.playerHasBook(player, book)) {
			if (!silent) {
				Module.sendMessage("bInfoBooks", player, "You already have a copy of the book '" + book.getName() + "' in your inventory.");			
			}
			return true;
		}
		
		// Try and give the player the book
		if (m_module.givePlayerBook(player, book)) {
			if (!silent) {
				Module.sendMessage("bInfoBooks", player, "You have been given the book '" + book.getName() + "'.");
			}
		}
		
		return true;
		
	}

}
