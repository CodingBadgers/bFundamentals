package uk.codingbadgers.binfobooks;

import java.util.ArrayList;

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
			m_pages.add((String)page);
		}
		
		JSONArray jTagLines = (JSONArray)bookJSON.get("taglines");
		for (Object tag : jTagLines) {
			m_tagLines.add((String)tag);
		}
		
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

	public ArrayList<String> getPages() {
		return m_pages;
	}

}
