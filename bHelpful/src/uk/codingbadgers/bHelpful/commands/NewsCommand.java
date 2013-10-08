/**
 * bHelpful 1.2-SNAPSHOT
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
package uk.codingbadgers.bHelpful.commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import uk.codingbadgers.bHelpful.Config;
import uk.codingbadgers.bHelpful.bHelpful;

public class NewsCommand extends ConfigCommand {
	
	private static List<String> messages = new ArrayList<String>();

	public NewsCommand() {
		super(Config.NEWS_LABEL, "/" + Config.NEWS_LABEL + " [amount]", false);
		HelpfulCommandHandler.registerCommand(this);
	}

	@Override
	protected void handleCommand(CommandSender sender, String label, String[] args) {
		int noofEvents = 6;
		
		if (args.length >= 1) {
			noofEvents = Integer.parseInt(args[0]);
		}
		
    	displayNews(sender, noofEvents);
	}

	public static void displayNews(CommandSender sender, int noofEvents) {
		if (noofEvents == -1) {
    		noofEvents = messages.size() + 1;
    	}
    	
    	for(int i = 0; i < noofEvents; ++i ) {
    		try  {
    			sender.sendMessage(messages.get(messages.size()-i-1));		
    		} catch (Exception ex) {
    		}
    	}
	}

	@Override
	protected void loadConfig() throws IOException {
		Bukkit.getScheduler().runTaskAsynchronously(bHelpful.PLUGIN, new Runnable() {
			@Override
			public void run() {
				try {
					HttpURLConnection con = (HttpURLConnection) new URL(Config.NEWS_LINK).openConnection();
					con.setDoOutput(true);
					con.setDoInput(true);
					con.setRequestMethod("GET");
					OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());

		            //write parameters
		            writer.write("");
		            writer.flush();
		            
		            // Get the response
		            InputStreamReader isr = new InputStreamReader(con.getInputStream());
		            writer.close();

		            JSONParser parser = new JSONParser();
		            JSONArray json = (JSONArray)  parser.parse(isr);
	            	String message = "";
		            
	            	messages.clear();
	            	
		            for (int i = 0; i < json.size(); i++) {
		            	JSONObject post = (JSONObject) json.get(i);
			            message += ChatColor.RED + "[" + (String) post.get("date") + "] " + ChatColor.RESET;
			            message += (String)post.get("title") + "\n";
			            message += "                      ";
			            message += ChatColor.DARK_AQUA + (String)post.get("link");
			            messages.add(message);
			            message = "";
		            }
		            
				} catch (FileNotFoundException e1) {
					bHelpful.MODULE.getLogger().warning(e1.getMessage());
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			
		});
	}

}
