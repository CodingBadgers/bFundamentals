package uk.codingbadgers.bhuman;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

public class BaseNPC extends EntityPlayer{

	public BaseNPC(MinecraftServer minecraftserver, World world, String s, ItemInWorldManager iteminworldmanager) {
		super(minecraftserver, world, s, iteminworldmanager);
	}
}
