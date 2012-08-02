package uk.codingbadgers.bHelpful;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 *
 * @author James
 */
public class News {
    
    static public void displayNews(Player player, int noofEvents) {
    	
    	if (noofEvents == -1)
    		noofEvents = Configuration.news.size() + 1;
    	
    	for( int i = 0; i < Configuration.news.size(); ++i ) {
    		player.sendMessage(Configuration.news.get(Configuration.news.size()-i-1));
    		
    		if (i >= noofEvents - 1)
    			break;
    	}
    	
    	if (bHelpful.spoutEnabled) {
	    	SpoutPlayer sPlayer = (SpoutPlayer)player;
	    	
			if (sPlayer.isSpoutCraftEnabled()) {
				String latest = Configuration.news.get(Configuration.news.size()-1);
				latest = latest.substring(latest.indexOf("]")+4).trim();
				
				if (latest.length() < 26) {
					sPlayer.sendNotification(ChatColor.RED + "Latest " + Configuration.m_servername + " News", latest, Material.DIAMOND_PICKAXE);
				}
			}
    	}
    	
    }
    
    static public void addNews (String news) {
    	
    	Configuration.news.clear();
    	File newsConfig = Configuration.newsConfig;
    	
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
		Player player = (Player) sender;
        
        if (commandLabel.equalsIgnoreCase("news")) {
        	
        	if (args.length == 0) {
        		
        		displayNews(player, 6);
        		
        		return true;
        	}
        	
        	if (args[0].equalsIgnoreCase("add")) {
        		
        		if (!bHelpful.hasPerms(player, "bhelpful.news.add")) {
        			Output.noPermission(player);
        			return false;
        		}
        		
        		String trimedNews = "";
        		
        		for (int i = 1; i<args.length; i++) {
        			
        			trimedNews += args[i] + " ";
        			
        		}
        		
        		trimedNews.trim();
        		
        		addNews(trimedNews);
        		
        		Output.player(player, "[bHelpful]", "News story added.");
        		
        		return true;
        	}

        }
		return false;
	}
    
}
