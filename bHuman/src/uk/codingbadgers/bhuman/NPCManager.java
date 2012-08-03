package uk.codingbadgers.bhuman;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;

public class NPCManager {

	private static List<BaseNPC> m_npcs = new ArrayList<BaseNPC>();
	
	public static void createNPC(String name, Location loc) {
		try {
			WorldServer ws = getWorldServer(loc.getWorld());
			
			BaseNPC npc = new BaseNPC(getMinecraftServer(ws.getServer()), ws, name, new ItemInWorldManager(ws));
			npc.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
			
			ws.addEntity(npc);
			ws.players.remove(npc);
			
			m_npcs.add(npc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createNPC(BaseNPC npc) {
		try {
			WorldServer ws = getWorldServer((World) npc.world);
			
			ws.addEntity(npc);
			ws.players.remove(npc);
			
			m_npcs.add(npc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static WorldServer getWorldServer(World world) {
        return ((CraftWorld) world).getHandle();
    }

    private static MinecraftServer getMinecraftServer(Server server) {
        return ((CraftServer) server).getServer();
    }
}
