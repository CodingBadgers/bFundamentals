package uk.codingbadgers.bhuman;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

public class BaseNPC extends EntityPlayer{

	/**
	 * Instantiates a new base npc.
	 *
	 * @param minecraftserver the minecraftserver
	 * @param world the world
	 * @param s the name(Also takes skin based of name)
	 * @param iteminworldmanager the iteminworldmanager
	 */
	public BaseNPC(MinecraftServer minecraftserver, World world, String s, ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);
	}
}
