/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.brewarded;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 *
 * @author Sam
 */
public class RewardedVotifierListener implements Listener {
	
	final private bRewarded module;
	
	/**
	 * Class constructor
	 * @param module 
	 */
	public RewardedVotifierListener(bRewarded module) {
		this.module = module;
	}
	
	/**
	 * 
	 * @param event 
	 */
	@EventHandler(priority = EventPriority.NORMAL)
    public void onVotifierEvent(VotifierEvent event) {
		
        final Vote vote = event.getVote();
		if (vote == null) {
			return;
		}
		
		OfflinePlayer player = Bukkit.getOfflinePlayer(vote.getUsername());
		if (player == null || !player.hasPlayedBefore()) {
			return;
		}
		
		this.module.logVote(vote);
		this.module.announceVote(vote);
		this.module.payVotee(vote);
		this.module.rewardVotee(vote);
		
	}
	
}
