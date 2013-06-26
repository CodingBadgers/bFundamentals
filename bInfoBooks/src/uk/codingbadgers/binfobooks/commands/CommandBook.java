package uk.codingbadgers.binfobooks.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.binfobooks.bInfoBooks;

public class CommandBook extends ModuleCommand {

	/**
	 * Command constructor.
	 */
	public CommandBook() {
		super("book", "book | book <name>");
	}
	
	/**
	 * Called when the 'book' command is executed.
	 */
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			System.out.println("Book commands must be used in game");
			return true;
		}
		
		Player player = (Player)sender;
		
		// List books
		if (args.length == 0) {
			
			if (Module.hasPermission(player, "binfobooks.list")) {
				Module.sendMessage("bInfoBooks", player, "The following books are avaliable...");
				bInfoBooks.listBooks(player);				
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
		
		// See if the book exists
		if (!bInfoBooks.bookExists(bookName)) {
			Module.sendMessage("bInfoBooks", player, "No book by the name '" + bookName + "' exists.");			
			return true;
		}
		
		// See if player already has the same book
		if (bInfoBooks.playerHasBook(player, bookName)) {
			Module.sendMessage("bInfoBooks", player, "You already have a copy of the book '" + bookName + "' in your inventory.");			
			return true;
		}
		
		// Try and give the player the book
		if (bInfoBooks.givePlayerBook(player, bookName)) {
			Module.sendMessage("bInfoBooks", player, "You have been given the book '" + bookName + "'.");
		}
		
		return true;
		
	}

}
