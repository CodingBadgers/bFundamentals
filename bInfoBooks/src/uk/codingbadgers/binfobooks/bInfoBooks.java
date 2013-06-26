package uk.codingbadgers.binfobooks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import uk.codingbadgers.bFundamentals.module.Module;
import uk.codingbadgers.binfobooks.commands.CommandBook;

public class bInfoBooks extends Module implements Listener {
	
	private static HashMap<String, InfoBook> m_books = new HashMap<String, InfoBook>();

	/**
	 * Called when the module is disabled.
	 */
	public void onDisable() {

	}

	/**
	 * Called when the module is loaded.
	 */
	public void onEnable() {
		register(this);
		registerCommand(new CommandBook());
		loadBooks();
	}
	
	@EventHandler (priority=EventPriority.NORMAL)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
		
		ItemStack item = event.getItemDrop().getItemStack();
		if (item == null) {
			return;
		}
		
		if (item.getType() != Material.WRITTEN_BOOK) {
			return;
		}
		
		final BookMeta bookmeta = (BookMeta)item.getItemMeta();
		if (bookmeta.getDisplayName() != "InfoBook") {
			return;
		}
		
		event.getItemDrop().remove();
	}
	
	private void loadBooks() {
		
		File bookFolder = new File(this.getDataFolder() + File.separator + "books");
		if (!bookFolder.exists()) {
			// make the 'books' folder
			bookFolder.mkdirs();
			
			// make a default book
			makeExampleBook(bookFolder);
		}
		
		for (File file : bookFolder.listFiles()) {
			final String fileName = file.getName();
			if (fileName.substring(fileName.lastIndexOf(".")).contains("json")) {
				
				this.log(Level.INFO, "Loading Book: " + fileName);
				
				String jsonContents = "";
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				
					String inputLine;
		            while ((inputLine = in.readLine()) != null) {
		                jsonContents += inputLine;
		            }
		            
		            in.close();
				
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				JSONObject bookJSON = (JSONObject)JSONValue.parse(jsonContents);
				InfoBook newBook = new InfoBook(bookJSON);
				m_books.put(newBook.getName().toLowerCase(), newBook);
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	private void makeExampleBook(File booksfolder) {
		
		JSONObject book = new JSONObject();
		
		book.put("name", "Example Book");
		book.put("author", "McBadgerCraft");
		
		JSONArray pages = new JSONArray();
		pages.add("This is an example book");
		pages.add("Using bInfoBooks created by");
		pages.add("TheCodingBadgers");
		book.put("pages", pages);
		
		JSONArray taglines = new JSONArray();
		taglines.add("bInfoBooks");
		taglines.add("By TheCodingBadgers");
		book.put("taglines", taglines);
		
		String contents = book.toJSONString();
		try {
			 BufferedWriter out = new BufferedWriter(new FileWriter(booksfolder.getAbsolutePath() + File.separator + "examplebook.json"));
			 out.write(contents);
			 out.close();		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static boolean bookExists(String bookName) {
		return m_books.containsKey(bookName.toLowerCase());
	}
	
	public static void listBooks(Player player) {
		
		for (InfoBook book : m_books.values()) {
			Module.sendMessage("bInfoBooks", player, " - " + book.getName());
		}
	}
	
	public static boolean playerHasBook(Player player, String bookName) {
		
		final InfoBook infobook = m_books.get(bookName.toLowerCase());
		if (infobook == null) {
			Module.sendMessage("bInfoBooks", player, "No book by the name '" + bookName + "' exists.");
			return false;
		}
		
		final PlayerInventory invent = player.getInventory();
		
		if (invent.contains(Material.WRITTEN_BOOK)) {
			for (ItemStack item : invent.getContents()) {
				if (item.getType() == Material.WRITTEN_BOOK) {
					final BookMeta bookmeta = (BookMeta)item.getItemMeta();
					
					if (bookmeta.getDisplayName() != "InfoBook") {
						continue;
					}
					
					if (infobook.getAuthor() != bookmeta.getAuthor()) {
						continue;
					}
					
					if (infobook.getName() != bookmeta.getTitle()) {
						continue;
					}
					
					// The player already has a copy of the given book
					return true;
				}
			}
		}
		
		return false;
	}

	public static boolean givePlayerBook(Player player, String bookName) {
		
		final InfoBook infobook = m_books.get(bookName.toLowerCase());
		if (infobook == null) {
			Module.sendMessage("bInfoBooks", player, "No book by the name '" + bookName + "' exists.");
			return false;
		}

		ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta bookmeta = (BookMeta)book.getItemMeta();
		
		// fill in book info meta
		bookmeta.setAuthor(infobook.getAuthor());
		bookmeta.setTitle(infobook.getName());
		bookmeta.setLore(infobook.getTagLines());
		bookmeta.setPages(infobook.getPages());		
		bookmeta.setDisplayName("InfoBook");
		
		book.setItemMeta(bookmeta);
		player.getInventory().addItem(book);
		return true;
	}
}
