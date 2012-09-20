package uk.codingbadgers.bhuman;

import org.bukkit.ChatColor;
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
				handleCreateCommand(player, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("remove")) {
				handleRemoveCommand(player, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("chat")) {
				handleChatMessageChange(player, args);
				return true;
			}
			
			if (args[0].equalsIgnoreCase("health")) {
				handleInvincibleCommand(player, args);
				return true;
			}
			
			return true;
		}
		return false;
	}
	
	private static void handleInvincibleCommand(Player player, String[] args) {
		if (args.length < 2) {
			bHuman.sendMessage(bHuman.NAME, player, "/npc health <name> [health]");
			return;
		}
		
		String name = args[1];
		HumanNPC npc = (HumanNPC) bHuman.getNPCManager().getHumanNPCByName(name).get(0);
		
		if (npc == null) {
			bHuman.sendMessage(bHuman.NAME, player.getPlayer(), "Sorry that npc doesn't exit");
			return;
		}
		
		if (args.length == 2) {
			bHuman.sendMessage(bHuman.NAME, player, name + " has " + npc.getHealth() + " lives left");
			return;
		}
		
		try {
			int health = Integer.parseInt(args[2]);
			
			if (health <= -1)
				npc.setInvincible(true);
			else 
				npc.setInvincible(false);
			
			npc.setHealth(health);
		} catch (NumberFormatException ex) {
			bHuman.sendMessage(bHuman.NAME, player, "Could not parse " + args[2] + " as a number");
			return;
		}
		
		bHuman.sendMessage(bHuman.NAME, player, "Set " + npc.getName() + "'s health to " + npc.getHealth());
		bHuman.getDBManager().updateHealth(npc);
		return;
	}

	private static void handleCreateCommand(Player player, String[] args) {
		if (args.length < 2 || args.length > 3) {
			bHuman.sendMessage(bHuman.NAME, player, "/npc create <name> [item]");
			return;
		}
		
		String name = args[1];
		
		Material item = Material.AIR;
		if (args.length == 3)
			item = Material.getMaterial(Integer.parseInt(args[2]));
			
		Location location = player.getLocation();
		HumanNPC npc = (HumanNPC)bHuman.getNPCManager().spawnHumanNPC(name, location);
		npc.setItemInHand(item);
		npc.lookAtPoint(player.getTargetBlock(null, 100).getLocation());
		bHuman.sendMessage(bHuman.NAME, player.getPlayer(), "Added npc " + name);
		bHuman.getDBManager().saveNPC(npc);
		return;
	}

	private static void handleRemoveCommand(Player player, String[] args) {
		if (args.length != 2) {
			bHuman.sendMessage(bHuman.NAME, player, "/npc remove <name>");
			return;
		}
		
		String name = args[1];
		HumanNPC npc = (HumanNPC) bHuman.getNPCManager().getHumanNPCByName(name).get(0);
		
		if (npc == null) {
			bHuman.sendMessage(bHuman.NAME, player.getPlayer(), "Sorry that npc doesn't exit");
			return;
		}
		
		bHuman.getNPCManager().despawnHumanByName(name);
		bHuman.sendMessage(bHuman.NAME, player.getPlayer(), "Removed npc " + name);
		bHuman.getDBManager().removeNPC(npc);
		return;
	}

	private static void handleChatMessageChange(Player player, String[] args) {
		if (args.length <= 2) {
			bHuman.sendMessage(bHuman.NAME, player, "/npc chat <name> [message]");
			return;
		}
		
		String name = args[1];
		HumanNPC npc = (HumanNPC) bHuman.getNPCManager().getHumanNPCByName(name).get(0);
		
		if (npc == null) {
			bHuman.sendMessage(bHuman.NAME, player.getPlayer(), "Sorry that npc doesn't exit");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 2; i<args.length; i++) {
			sb.append(args[i] + " ");
		}
		String chatMessage = sb.toString().trim();
		
		npc.setMessage(formatString(chatMessage));
		bHuman.sendMessage(bHuman.NAME, player, "Changed " + npc.getName() + "'s message to " + formatString(chatMessage));
		bHuman.getDBManager().addChatMessage(npc);
		return;
	}

	public static String formatString(String message) {
		String formattedMessage = message;
				
		while(formattedMessage.indexOf("&") != -1) {
			String code = formattedMessage.substring(formattedMessage.indexOf("&") + 1, formattedMessage.indexOf("&") + 2);
			formattedMessage = formattedMessage.substring(0, formattedMessage.indexOf("&")) +  ChatColor.getByChar(code) + formattedMessage.substring(formattedMessage.indexOf("&") + 2);
										
		}
		
		return formattedMessage;
	}
} 
