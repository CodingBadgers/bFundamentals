package uk.codingbadgers.bShortLinks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class PlayerListener implements Listener {
	
	/**
	 * Handle the message when a player talks
	 *
	 * @param event the chat event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChat(PlayerChatEvent event) {
				
		Player player = event.getPlayer();
		
		if (!Global.HasPermission(player, "shortlinks.url"))
			return;
		
		event.setMessage(parseMessage(player, event.getMessage()));
		
	}
	
	/**
	 * Check the header of the website for blacklisted words
	 *
	 * @param url the url to check
	 * @return true, if the site is safe, false if it is blacklisted
	 */
	private boolean CheckSite(String url) {
		
		// If there are no blacklisted words, the site is ok.
		if (Global.BlackList.size() == 0)
			return true;
		
		try {
			// Get the urls source
			URL urlRequest = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(urlRequest.openStream()));
			
			// spin through the source code
			String siteText = in.readLine();
			while (siteText != null) {
				
				// see if the line of source code contains a blacklisted word
				for (String word : Global.BlackList) {
					if (siteText.contains(word)) {
						in.close();
						return false;
					}
				}
				
				// if it's the end of the head tags then stop looking.
				if (siteText.contains("</head>"))
					break;
				
				siteText = in.readLine();
			}
			
			in.close();
		} catch (IOException e) {
			return false;
		}
		
		// No blacklisted words in the head
		return true;	
	}
	
	/**
	 * Shorten a url.
	 *
	 * @param player the player who said the url
	 * @param url the url to shorten
	 * @return a shortened version of the url
	 */
	private String ShortenURL(Player player, String url) {
		
		// Check the url for blacklisted words
		if (!Global.HasPermission(player, "shortlinks.excludeblacklist")) {
			
			if (Global.DEBUG) {
				Global.OutputConsole("Checking black list");
			}
			
			if (!CheckSite(url)) {
				return Global.UrlColour + "Blacklisted Url" + ChatColor.RESET;
			}
		}
		
		String shorturl = url;
		String urlShortenAPI = "";
		
		// If we are using adfly use there api
		if (Global.API.equalsIgnoreCase("adfly")) {
			urlShortenAPI = "http://api.adf.ly/api.php?key=" + Global.AfdlyAPIKey + "&uid=" + Global.AfdlyAPIUid + "&advert_type=int&domain=adf.ly&url=";
		// if we are using tinyurl use there api
		} else if (Global.API.equalsIgnoreCase("tinyurl")) {
			urlShortenAPI = "http://tinyurl.com/api-create.php?url=";
		// error in config? return a non shortened url
		} else {
			Global.OutputConsole("Error: Unknown api '" + Global.API + "'");
			return Global.UrlColour + shorturl + ChatColor.RESET;
		}
		
		// Use the api to shorten the url
		try {			
			URL urlRequest = new URL(urlShortenAPI + url);
			
			if (Global.DEBUG) {
				Global.OutputConsole("Shortening url with " + Global.API);
				Global.OutputConsole(urlRequest.getPath());
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(urlRequest.openStream()));
			shorturl = in.readLine();
			in.close();
		} catch (IOException e) {
		}
		
		// failed to shorten url, set back to original
		if (shorturl == null) {
			shorturl = url;
		}
		
		// If we are masking all links with a tinyurl link then mask it...
		if (Global.MaskWithTinyUrl) {
			try {
				URL urlRequest = new URL("http://tinyurl.com/api-create.php?url=" + shorturl);
				
				if (Global.DEBUG) {
					Global.OutputConsole("Masking with TinyUrl.");
					Global.OutputConsole(urlRequest.getPath());
				}
				
				BufferedReader in = new BufferedReader(new InputStreamReader(urlRequest.openStream()));
				shorturl = in.readLine();
				in.close();
			} catch (IOException e) {
			}
		}
		
		// if we failed to shorten, set back to original url
		if (shorturl == null) {
			shorturl = url;
		}
        
		return Global.UrlColour + shorturl + ChatColor.RESET;
	}
	
	/**
	 * Parses the players message.
	 *
	 * @param player the player
	 * @param msg the message to parse
	 * @return A message with all urls replaced
	 */
	private String parseMessage(Player player, String msg) {
		String returnMsg = "";
		
		// Split the message into words
		String[] words = msg.split(" ");
		for (String word : words)
		{
			// if it starts with www. then put a http:// infront of it.
			if (word.startsWith("www."))
				word = "http://" + word;
			
			// See if the word is an actual URL
			try {
				new URL(word);
				word = ShortenURL(player, word);
			} catch (MalformedURLException e) {
			}
			
			// rebuild the message
			returnMsg += word + " ";
		}
		
		return returnMsg;
	}

}
