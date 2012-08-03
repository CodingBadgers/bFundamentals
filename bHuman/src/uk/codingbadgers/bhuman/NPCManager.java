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

// TODO Change to be non static
/**
 * The Class NPCManager.
 */
public class NPCManager {

	/** The npc array. */
	private static List<BaseNPC> m_npcs = new ArrayList<BaseNPC>();
	
	/**
	 * Creates a npc.
	 *
	 * @param name the name
	 * @param loc the location
	 */
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
	
	/**
	 * Creates a npc.
	 *
	 * @param npc the npc
	 */
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
	
	/**
	 * Gets the world server.
	 *
	 * @param world the world
	 * @return the world server
	 */
	private static WorldServer getWorldServer(World world) {
        return ((CraftWorld) world).getHandle();
    }

    /**
     * Gets the minecraft server.
     *
     * @param server the server
     * @return the minecraft server
     */
    private static MinecraftServer getMinecraftServer(Server server) {
        return ((CraftServer) server).getServer();
    }
}
