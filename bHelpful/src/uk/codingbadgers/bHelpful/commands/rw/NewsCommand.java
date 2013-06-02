package uk.codingbadgers.bHelpful.commands.rw;

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

import uk.codingbadgers.bHelpful.Configuration;
import uk.codingbadgers.bHelpful.bHelpful;

public class NewsCommand extends ConfigCommand {
	
	private List<String> messages = new ArrayList<String>();

	public NewsCommand() {
		super("news", "/news [amount]", false);
	}

	@Override
	protected void handleCommand(CommandSender sender, String label, String[] args) {
		int noofEvents = 6;
		
		if (args.length >= 1) {
			noofEvents = Integer.parseInt(args[0]);
		}
		
    	displayNews(sender, noofEvents);
	}

	public void displayNews(CommandSender sender, int noofEvents) {
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
					HttpURLConnection con = (HttpURLConnection) new URL(Configuration.NEWS_LINK).openConnection();
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
