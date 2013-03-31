package uk.thecodingbadgers.benchant;

import org.bukkit.entity.EntityType;

public class EntityHelper {

	public static SpawnerEntity createEntity(String entityName) {
		EntityType type = EntityType.fromName(entityName);
		return new SpawnerEntity(type);
	}

}
