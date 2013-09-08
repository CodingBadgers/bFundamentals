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
