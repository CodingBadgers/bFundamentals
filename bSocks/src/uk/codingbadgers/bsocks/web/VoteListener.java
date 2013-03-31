package uk.codingbadgers.bsocks.web;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import uk.codingbadgers.bsocks.bSocksModule;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class VoteListener implements Listener{

	@EventHandler
	public void onVote(VotifierEvent event) {
		Vote vote = event.getVote();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("user", vote.getUsername());
		params.put("time", vote.getTimeStamp());
		params.put("address", vote.getAddress());
		params.put("service", vote.getServiceName());
		
		try {
			WebHandler postHandler = bSocksModule.getPostHandler("vote.php");
			postHandler.put(params);
			postHandler.start();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
	}
}
