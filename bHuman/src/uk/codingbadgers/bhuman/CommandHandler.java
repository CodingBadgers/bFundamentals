package uk.codingbadgers.bhuman;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.topcat.npclib.entity.HumanNPC;

public class CommandHandler {

	public static boolean onCommand(Player player, String commandLabel, String[] args) {
		
		if (commandLabel.equalsIgnoreCase("npc")) {
			if (args.length == 0) {
				bHuman.sendMessage(bHuman.NAME, player, "/npc help");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("create")) {
				if (args.length != 2) {
					bHuman.sendMessage(bHuman.NAME, player, "/npc create <name>");
					return true;
				}
				
				String name = args[1];
				Location location = player.getLocation();
				HumanNPC npc = (HumanNPC)bHuman.getNPCManager().spawnHumanNPC(name, location);
				npc.setItemInHand(Material.BOW);
				bHuman.sendMessage(bHuman.NAME, player.getPlayer(), "Added npc " + name);
				bHuman.getDBManager().save(npc);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("remove")) {
				if (args.length != 2) {
					bHuman.sendMessage(bHuman.NAME, player, "/npc remove <name>");
					return true;
				}
				
				String name = args[1];
				HumanNPC npc = (HumanNPC) bHuman.getNPCManager().getHumanNPCByName(name).get(0);
				
				if (npc == null) {
					bHuman.sendMessage(bHuman.NAME, player.getPlayer(), "Sorry that npc doesn't exit");
					return true;
				}
				
				bHuman.getNPCManager().despawnHumanByName(name);
				bHuman.sendMessage(bHuman.NAME, player.getPlayer(), "Removed npc " + name);
				bHuman.getDBManager().remove(npc);
				return true;
			}
			
			return true;
		}
		return false;
	}
}
