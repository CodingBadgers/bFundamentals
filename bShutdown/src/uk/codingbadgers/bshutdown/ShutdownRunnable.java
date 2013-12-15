package uk.codingbadgers.bshutdown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ShutdownRunnable extends BukkitRunnable {

	private int time; // in seconds
	
	public ShutdownRunnable(int time) {
		this.time = time;
	}
	
	@Override
	public void run() {
		time--;

		boolean showMessage = false;
		
		if (time >= 300 && time % 300 == 0) { // every 5 minutes
			showMessage = true;
		} else if (time < 300 && time >= 60 && time % 60 == 0) { // every minute
			showMessage = true;
		} else if (time < 60 && time > 5 && time % 10 == 0 && time != 0) { // every 10 seconds
			showMessage = true;
		} else if (time <= 5 && time != 0) { // every second
			showMessage = true;
		}
		
		if (showMessage) {
			displayShutdownMessage(time);
		}
		
		if (time == 0) {
			shutdown();
			cancel();
		}
 	}
	
	private void shutdown() {
		Player[] players = Bukkit.getOnlinePlayers();
		
		for (Player player : players) {
			player.kickPlayer(Bukkit.getServer().getShutdownMessage());
		}
		
		Bukkit.shutdown();
	}

	private void displayShutdownMessage(int time) {
		Bukkit.broadcastMessage(ChatColor.BLUE + "[Shutdown]" + ChatColor.RESET + " The server will shutdown in " + formatTime(time));
	}

	private String formatTime(int time) {
		long elapsedTime = time;  
	    String format = String.format("%%0%dd", 2);  
	    String seconds = String.format(format, elapsedTime % 60);  
	    String minutes = String.format(format, (elapsedTime % 3600) / 60);  
	    String hours = String.format(format, elapsedTime / 3600);  
	    String timeString = "";
	    
	    boolean showHours = !hours.equalsIgnoreCase("00");
	    boolean showMinutes = !minutes.equalsIgnoreCase("00");
	    boolean showSeconds = !seconds.equalsIgnoreCase("00");
	    
	    timeString += showHours ? (hours.startsWith("0") ? hours.substring(1, 2) : hours) + " hour" + addPlural(minutes) + ", " : "";
	    timeString += showMinutes ? (minutes.startsWith("0") ? minutes.substring(1, 2) : minutes) + (showSeconds ? " minute" + addPlural(minutes) + " and " : " minute" + addPlural(minutes)) : "";
	    timeString += showSeconds ? (seconds.startsWith("0") ? seconds.substring(1, 2) : seconds) + " second" + addPlural(minutes) : "";  
	    return timeString;  
	}

	private String addPlural(String time) {
		return (time.endsWith("1") ? "" : "s");
	}

}
