package uk.codingbadgers.binfobooks;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class InfoBook {
	
	private String m_author = "";
	private String m_name = "";
	private ArrayList<String> m_pages = new ArrayList<String>();
	private ArrayList<String> m_tagLines = new ArrayList<String>();
	
	public InfoBook(JSONObject bookJSON) {
		
		m_name = (String)bookJSON.get("name");
		m_author = (String)bookJSON.get("author");
		
		JSONArray jPages = (JSONArray)bookJSON.get("pages");
		for (Object page : jPages) {
			m_pages.add(ChatColor.translateAlternateColorCodes('&', (String)page));
		}
		
		JSONArray jTagLines = (JSONArray)bookJSON.get("taglines");
		for (Object tag : jTagLines) {
			m_tagLines.add((String)tag);
		}
		
	}
	
	private String formatPage(Player player, String page) {
		
		page = page.replaceAll("<<playername>>", player.getName());		
		page = page.replaceAll("<<servername>>", Bukkit.getServerName());	
		page = page.replaceAll("<<bukkitversion>>", Bukkit.getBukkitVersion());		
		page = page.replaceAll("<<minecraftversion>>", Bukkit.getVersion());	
		
		return page;
		
	}

	public String getAuthor() {
		return m_author;
	}

	public String getName() {
		return m_name;
	}

	public ArrayList<String> getTagLines() {
		return m_tagLines;
	}

	public ArrayList<String> getPages(Player player) {
		ArrayList<String> pages = new ArrayList<String>();
		for (String page : m_pages) {
			pages.add(formatPage(player, page));
		}
		return pages;
	}

}
