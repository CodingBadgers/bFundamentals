package uk.codingbadgers.bHelpful.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import uk.codingbadgers.bHelpful.Configuration;
import uk.codingbadgers.bHelpful.Output;
import uk.codingbadgers.bHelpful.bHelpful;

/**
 *
 * @author James
 */
public class News {
    
    /**
     * Display news.
     *
     * @param player the player
     * @param noofEvent the number of events to be shown
     */
    static public void displayNews(CommandSender player, int noofEvents) {
    	
    	if (noofEvents == -1)
    		noofEvents = Configuration.NEWS.size() + 1;
    	
    	for( int i = 0; i < Configuration.NEWS.size(); ++i ) {
    		player.sendMessage(Configuration.NEWS.get(Configuration.NEWS.size()-i-1));
    		
    		if (i >= noofEvents - 1)
    			break;
    	}
    	
    }
    
    /**
     * Adds the news.
     *
     * @param news the news
     */
    static public void addNews (String news) {
    	
    	Configuration.NEWS.clear();
    	File newsConfig = Configuration.NEWS_CONFIG;
    	
    	try {
    		FileWriter fstream = new FileWriter(newsConfig.getPath(),true);
			BufferedWriter writer = new BufferedWriter(fstream);

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();
			
			writer.write(ChatColor.RED + "[" + dateFormat.format(date) + "] "
					+ ChatColor.YELLOW + news + "\n");

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	Configuration.loadNewsConfig(newsConfig);
    }
    
    public static boolean onCommand(CommandSender sender, String commandLabel, String[] args) {
		
        if (commandLabel.equalsIgnoreCase("news")) {
        	
        	if (args.length == 0) {
        		
        		displayNews(sender, 6);
        		
        		return true;
        	}
        	
        	if (args[0].equalsIgnoreCase("add")) {
        		
        		if (!bHelpful.hasPermission(sender, "bhelpful.news.add")) {
        			Output.noPermission(sender);
        			return false;
        		}
        		
        		String trimedNews = "";
        		
        		for (int i = 1; i<args.length; i++) {
        			
        			trimedNews += args[i] + " ";
        			
        		}
        		
        		trimedNews = trimedNews.trim();
        		
        		addNews(trimedNews);
        		
        		Output.player(sender, "[bHelpful]", "News story added.");
        		
        		return true;
        	}

        }
		return false;
	}
    
}
