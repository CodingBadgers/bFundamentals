package uk.codingbadgers.bsocks.web;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import ru.tehkode.permissions.PermissionEntity;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.events.PermissionEntityEvent;
import ru.tehkode.permissions.events.PermissionEntityEvent.Action;
import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bsocks.bSocksModule;

/**
 * Listener for PermissionsEx based events
 *
 * @see PermissionsEvent
 */
public class PermissionsListener implements Listener {

	/**
	 * On permissions event.
	 *
	 * @param event the event fired by pex
	 */
	@EventHandler
	public void onPermissionsEvent(PermissionEntityEvent event) {
		if (event.getAction() == Action.INHERITANCE_CHANGED) {
			onRankChange(event);
		}
	}
	
	/**
	 * On rank change, handles sending information to website through
	 * the post handler and applying the fire effect around the player.
	 *
	 * @param event the event
	 */
	public void onRankChange(PermissionEntityEvent event) {
		PermissionEntity entity = event.getEntity();
		
		if (!(entity instanceof PermissionUser)) {
			return;
		}
		
		PermissionUser user = (PermissionUser) entity;
		PermissionGroup group = user.getGroups()[0];
		
		try {
			WebHandler ph = bSocksModule.getPostHandler("promote.php");
			
			Map<String, String> data = new HashMap<String, String>();
			data.put("user", user.getName());
			data.put("group", group.getName());
			
			ph.put(data);
			ph.start();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		Player player = bFundamentals.getInstance().getServer().getPlayerExact(user.getName());
		
		if (player == null) {
			return;
		}
		
		player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 9);
	}
}
