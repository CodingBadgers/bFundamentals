package uk.thecodingbadgers.benchant;

import net.minecraft.server.v1_4_6.NBTTagCompound;
import net.minecraft.server.v1_4_6.TileEntityMobSpawner;

import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_4_6.CraftWorld;
import org.bukkit.craftbukkit.v1_4_6.block.CraftCreatureSpawner;

public class SpawnerInfo {

	private CreatureSpawner spawner;
	private SpawnerEntity spawningEntity;
	private int delay = -1;
	private int minSpawnDelay = -1;
	private int maxSpawnDelay = -1;
	private int maxNearbyEntities = -1;
	private int requiredPlayerRange = -1;
	private int spawnRange = -1;
	private int spawnCount = -1;
	
	
	public SpawnerInfo(CreatureSpawner spawner, SpawnerEntity spawning) {
		this.spawner = spawner;
		this.spawningEntity = spawning;
	}

	public void apply() {
		CraftCreatureSpawner cSpawner = (CraftCreatureSpawner)spawner;
		CraftWorld world = (CraftWorld)spawner.getWorld();
		TileEntityMobSpawner entity = (TileEntityMobSpawner) world.getTileEntityAt(cSpawner.getBlock().getX(), cSpawner.getBlock().getY(), cSpawner.getBlock().getZ());
		
		NBTTagCompound nbt = new NBTTagCompound();
		
		nbt.setString("EntityId", spawningEntity.getID());
		
		if (delay != -1) {
			nbt.setShort("Delay", (short) this.delay);
		}

		if (minSpawnDelay != -1) {
			nbt.setShort("MinSpawnDelay", (short) this.minSpawnDelay);
		}

		if (maxSpawnDelay != -1) {
			nbt.setShort("MaxSpawnDelay", (short) this.maxSpawnDelay);
		}

		if (spawnCount != -1) {
			nbt.setShort("SpawnCount", (short) this.spawnCount);
		}

		if (maxNearbyEntities != -1) {
			nbt.setShort("MaxNearbyEntities", (short) this.maxNearbyEntities);
		}

		if (requiredPlayerRange != -1) {
			nbt.setShort("RequiredPlayerRange", (short) this.requiredPlayerRange);
		}

		if (spawnRange != -1) {
			nbt.setShort("SpawnRange", (short) this.spawnRange);
		}

		nbt.setCompound("SpawnData", spawningEntity.getNBTData());
     
        entity.a(nbt);
	}

	public int getRequiredPlayerRange() {
		return requiredPlayerRange;
	}

	public void setRequiredPlayerRange(int requiredPlayerRange) {
		this.requiredPlayerRange = requiredPlayerRange;
	}

	public SpawnerEntity getSpawningEntity() {
		return spawningEntity;
	}

	public CreatureSpawner getSpawner() {
		return spawner;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public int getMinSpawnDelay() {
		return minSpawnDelay;
	}

	public void setMinSpawnDelay(int minSpawnDelay) {
		this.minSpawnDelay = minSpawnDelay;
	}

	public int getMaxSpawnDelay() {
		return maxSpawnDelay;
	}

	public void setMaxSpawnDelay(int maxSpawnDelay) {
		this.maxSpawnDelay = maxSpawnDelay;
	}

	public int getMaxNearbyEntities() {
		return maxNearbyEntities;
	}

	public void setMaxNearbyEntities(int maxNearbyEntities) {
		this.maxNearbyEntities = maxNearbyEntities;
	}

	public int getSpawnRange() {
		return spawnRange;
	}

	public void setSpawnRange(int spawnRange) {
		this.spawnRange = spawnRange;
	}

	public int getSpawnCount() {
		return spawnCount;
	}

	public void setSpawnCount(int spawnCount) {
		this.spawnCount = spawnCount;
	}
}
