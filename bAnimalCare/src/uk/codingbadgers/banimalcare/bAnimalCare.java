/**
 * bAnimalCare 1.2-SNAPSHOT
 * Copyright (C) 2013  CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.codingbadgers.banimalcare;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

//import de.kumpelblase2.remoteentities.EntityManager;
//import de.kumpelblase2.remoteentities.RemoteEntities;
//import de.kumpelblase2.remoteentities.api.RemoteEntity;
//import de.kumpelblase2.remoteentities.api.features.RemoteTamingFeature;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;

public class bAnimalCare extends Module implements Listener {

	public static WorldGuardPlugin WORLDGUARD = null;
	
	private HashMap<UUID, String> m_pets = new HashMap<UUID, String>();
	
	private HashMap<String, Entity> m_tpVehicle = new HashMap<String, Entity>();
	
	//private EntityManager m_entityManager = null;
	
	private static int MAX_NOOF_PETS = 10;
	
	/**
	 * Called when the module is disabled.
	 */
	public void onDisable() {
		WORLDGUARD = null;
	}

	/**
	 * Called when the module is loaded.
	 */
	public void onEnable() {
		WORLDGUARD = (WorldGuardPlugin)m_plugin.getServer().getPluginManager().getPlugin("WorldGuard");
		register(this);
		
		setupDatabase();
		loadPets();
	}
	
	/**
	 * Called when a player interacts with an entity
	 */	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerInteract(PlayerInteractEntityEvent event) {
				
		final Player player = event.getPlayer();
		final ItemStack item = player.getItemInHand();
		
		if (!(event.getRightClicked() instanceof LivingEntity)) {
			return;
		}

		final LivingEntity entity = (LivingEntity)event.getRightClicked();
		
		if (entity == null) {
			return;
		}
		
		if (m_pets.containsKey(entity.getUniqueId())) {
			if (!m_pets.get(entity.getUniqueId()).equalsIgnoreCase(player.getName())) {
				if (!Module.hasPermission(player, "bAnimalCare.override")) {
					Module.sendMessage("bAnimalCare", player, "This animal is someone elses pet.");
					event.setCancelled(true);
				}
			} 
			else {
				if (item != null && item.getType() == Material.STRING) {
					Module.sendMessage("bAnimalCare", player, "You have released your pet...");
					releasePet(player, entity);
				}
			}
			return;
		}
		
		if (item == null || item.getType() != Material.STRING) {
			return;
		}
		
		if (getNumberOfPets(player) >= MAX_NOOF_PETS) {
			Module.sendMessage("bAnimalCare", player, "You have the maximum number of pets, release an old one before getting a new.");
			return;
		}
		
		ProtectedRegion region = getChildRegionFromLocation(entity.getLocation());
		
		// In non safe, allow capture
		if (region == null) {			
			attemptToCaptureEntity(player, entity);
			return;
		}
		
		// check all owners of the region, if they are an owner then attempt to capture
		Set<String> owners = region.getOwners().getPlayers();
		for (String owner : owners) {
			if (owner.equalsIgnoreCase(player.getName())) {
				attemptToCaptureEntity(player, entity);
				return;
			}
		}
		
		// not an owner, so tell them NO!
		Module.sendMessage("bAnimalCare", player, "You can't capture pets in other peoples safezones.");
	}

	/**
	 * Called when an entity is damaged
	 */	
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamaged(EntityDamageByEntityEvent event) {
				
		if (!(event.getEntity() instanceof LivingEntity)) {
			return;
		}
		
		final LivingEntity entity = (LivingEntity)event.getEntity();
		final UUID entityID = entity.getUniqueId();
		
		Player attackPlayer = ((event.getDamager() instanceof Player) ? (Player)event.getDamager() : null);
		
		if (!m_pets.containsKey(entityID)) {
			return;
		}
		
		if (attackPlayer != null) {
			Module.sendMessage("bAnimalCare", attackPlayer, "This animal is a pet. You can't hurt it.");
			if (m_pets.get(entityID).equalsIgnoreCase(attackPlayer.getName())) {
				Module.sendMessage("bAnimalCare", attackPlayer, "To kill your pet. First release it by using String.");
			}
		}
		
		// Someone's pet, cancel any damage.	
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		
		ArrayList<String> commands = new ArrayList<String>();
		commands.add("spawn");
		commands.add("tp");
		commands.add("home");
		commands.add("build");
		commands.add("tpa");
		commands.add("warp");
		
		final Entity vehicle = event.getPlayer().getVehicle();
		if (vehicle == null || vehicle.getType() != EntityType.HORSE) {
			return;
		}
		
		for (String command : commands) {
			if (event.getMessage().startsWith("/" + command)) {
				vehicle.eject();
				m_tpVehicle.put(event.getPlayer().getName(), vehicle);
			}
		}
		
	}
	
	/**
	 * Called when an entity teleports
	 */	
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityTeleport(PlayerTeleportEvent event) {

		if (event.getCause() != TeleportCause.PLUGIN && event.getCause() != TeleportCause.COMMAND) {
			return;
		}
		
		final Player player = event.getPlayer();
		final Entity vehicle = m_tpVehicle.get(event.getPlayer().getName());

		if (vehicle == null) {
			return;
		}
		
		if (vehicle.getType() == EntityType.HORSE) {
			Module.sendMessage("bAnimalCare", player, "Please wait whilst we teleport your horse to you...");
			new BukkitRunnable() {
				public void run() {
					vehicle.teleport(player);
					new BukkitRunnable() {
						public void run() {
							vehicle.setPassenger(player);
						}
					}.runTaskLater(bFundamentals.getInstance(), 40L);
				}
			}.runTaskLater(bFundamentals.getInstance(), 20L);
		}
		
	}
	
	/**
	 * Called when an entity targets another entity
	 */	
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityTarget(EntityTargetEvent event) {
		
		return;
		
		/*
		if (!(event.getEntity() instanceof LivingEntity)) {
			return;
		}
		
		final LivingEntity entity = (LivingEntity)event.getEntity();
		final Integer entityID = entity.getUniqueId();
		
		if (!m_pets.containsKey(entityID)) {
			return;
		}
		
		// in a safezone, so dont attack anything
		if (getChildRegionFromLocation(entity.getLocation()) != null) {
			event.setCancelled(true);
			return;
		}
		
		if (!(event.getTarget() instanceof Player)) {
			return;
		}
		
		Player targetPlayer = (Player)event.getTarget();
		
		// in non-safe, so attack everything apart from the owner
		final String ownerName = m_pets.get(entityID);		
		if (ownerName.equalsIgnoreCase(targetPlayer.getName())) {
			event.setCancelled(true);
			return;
		}
		
		// let the event pass, and target the player!
		*/
		
	}
	
	
	
	/**
	 * Get the number of pets a player has
	 */
	private int getNumberOfPets(Player player) {
		
		final String playerName = player.getName();
		int noofPets = 0;
		
		for (String name : m_pets.values()) {
			if (name.equalsIgnoreCase(playerName)) {
				noofPets++;
			}
		}
		
		return noofPets;
		
	}
	
	/**
	 * Attempt to capture an entity
	 */
	private void attemptToCaptureEntity(Player player, LivingEntity entity) {
		
		if (isBlackListed(entity)) {
			// Dont output a message, just pretend this never happened
			return;
		}
		
		if (isEntityOwned(entity)) {
			if (!m_pets.get(entity.getUniqueId()).equalsIgnoreCase(player.getName())) {
					Module.sendMessage("bAnimalCare", player, "Someone else has already claimed this pet.");
			}
			return;
		}
	
		captureEntity(player, entity);
		
	}
	
	private void captureEntity(Player player, LivingEntity entity) {
		
		String playerName = player.getName();
		UUID entityID = entity.getUniqueId();
		
		// Store the pet in memory
		m_pets.put(entityID, playerName);
		
		// Store the pet into our database
		addNewPet(playerName, entityID);
	
		// Setup the new features of the entity
		//RemoteEntity rEntity = m_entityManager.createRemoteEntityFromExisting(entity, true);
		
		//RemoteTamingFeature taming = new RemoteTamingFeature(rEntity);
		//taming.tame(player);		
		//rEntity.getFeatures().addFeature(taming);
		
		String mobType = entity.getType().getName().replace("Entity", "");
		Module.sendMessage("bAnimalCare", player, "You have tamed a new " + mobType + " pet.");
		
	}
	
	private boolean isBlackListed(LivingEntity entity) {
		
		if (entity.getType() == EntityType.ARROW)
			return true;
		
		if (entity.getType() == EntityType.COMPLEX_PART)
			return true;
		
		if (entity.getType() == EntityType.DROPPED_ITEM)
			return true;
		
		if (entity.getType() == EntityType.EGG)
			return true;
		
		if (entity.getType() == EntityType.ENDER_CRYSTAL)
			return true;
		
		if (entity.getType() == EntityType.ENDER_DRAGON)
			return true;
		
		if (entity.getType() == EntityType.ENDER_PEARL)
			return true;
		
		if (entity.getType() == EntityType.ENDER_SIGNAL)
			return true;
		
		if (entity.getType() == EntityType.EXPERIENCE_ORB)
			return true;
		
		if (entity.getType() == EntityType.FALLING_BLOCK)
			return true;
		
		if (entity.getType() == EntityType.FIREBALL)
			return true;
		
		if (entity.getType() == EntityType.FIREWORK)
			return true;
		
		if (entity.getType() == EntityType.FISHING_HOOK)
			return true;
		
		if (entity.getType() == EntityType.GHAST)
			return true;
		
		if (entity.getType() == EntityType.ITEM_FRAME)
			return true;
		
		if (entity.getType() == EntityType.LIGHTNING)
			return true;
		
		if (entity.getType() == EntityType.PAINTING)
			return true;
		
		if (entity.getType() == EntityType.PLAYER)
			return true;
		
		if (entity.getType() == EntityType.PRIMED_TNT)
			return true;
		
		if (entity.getType() == EntityType.SMALL_FIREBALL)
			return true;
		
		if (entity.getType() == EntityType.SNOWBALL)
			return true;
		
		if (entity.getType() == EntityType.SPLASH_POTION)
			return true;
		
		if (entity.getType() == EntityType.THROWN_EXP_BOTTLE)
			return true;
		
		if (entity.getType() == EntityType.UNKNOWN)
			return true;
		
		if (entity.getType() == EntityType.WEATHER)
			return true;
		
		if (entity.getType() == EntityType.WITHER)
			return true;
		
		if (entity.getType() == EntityType.WITHER_SKULL)
			return true;
		
		return false;
	}
	
	private boolean isEntityOwned(LivingEntity entity) {
		final UUID entityID = entity.getUniqueId();
		return m_pets.containsKey(entityID);
	}
	
	/**
	 * Get the lowest child region from a give location
	 */	
	private ProtectedRegion getChildRegionFromLocation(Location location) {

        ArrayList<ProtectedRegion> possibleRegions = new ArrayList<ProtectedRegion>();
        
        final World world = location.getWorld();
        
        // create a world edit vector for the position
        final com.sk89q.worldedit.Vector worldeditLocation = new com.sk89q.worldedit.Vector(
            location.getX(),
            location.getY(),
            location.getZ()
        );

        // loop through every region
        for(ProtectedRegion currentRegion : WORLDGUARD.getRegionManager(world).getRegions().values()){
        	
            // if the current region contains the sign, add it to the list of possible regions
            if (currentRegion.contains(worldeditLocation)) {
                possibleRegions.add(currentRegion);
            }
            
        }

        // if we didnt get any regions, bail.
        if (possibleRegions.size() == 0) {
            return null;
        }

        // if we only got one region, it has to be the region we use
        if (possibleRegions.size() == 1) {
            return possibleRegions.get(0);
        }

        // work out the lowest child of the regions
        int childLevel = 0;
        ProtectedRegion lowestChild = null;
        for (ProtectedRegion currentRegion : possibleRegions)
        {
            if (currentRegion.getParent() != null)
            {
                ProtectedRegion tempRegion = currentRegion;
                int tempChildLevel = 0;
                while(tempRegion.getParent() != null)
                {
                    tempRegion = tempRegion.getParent();
                    tempChildLevel++;
                }

                if (tempChildLevel > childLevel)
                {
                    childLevel = tempChildLevel;
                    lowestChild = currentRegion;
                }
            }
        }

        // if we found the lowest child return that
        if (lowestChild != null) {
            return lowestChild;
        }

        // if we didn't find the lowest child, but found some regions, return the first region
        return possibleRegions.get(0);
    }
	
	/**
	 * Make sure the database table exists, else create it
	 */	
	private void setupDatabase() {
		
		if (m_database.tableExists("bAnimalCare"))
			return;
		
		final String createQuery = 
		"CREATE TABLE bAnimalCare " +
		"(" +
			"Player varchar(32)," +
			"EntityID TEXT" +
		")";

		m_database.query(createQuery, true);
		
	}
	
	/**
	 * Load all entities into memory for a quick lookup
	 */		
	private void loadPets() {
		
		final String selectAll = "Select * FROM bAnimalCare";
		
		ResultSet result = m_database.queryResult(selectAll);
		if (result != null) {
			
			try {
				while (result.next()) {
					
					String playername = result.getString("Player");
					UUID entityID = UUID.fromString(result.getString("EntityID"));
					m_pets.put(entityID, playername);
					
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				m_database.freeResult(result);
				return;
			}
			
			m_database.freeResult(result);
			
		}
		
	}
	
	/**
	 * Add a new pet to the database
	 */	
	private void addNewPet(String player, UUID entityID) {
		
		String addPet = 
			"INSERT INTO bAnimalCare " +
				"VALUES ('" +
				player + "', '" +
				entityID.toString() +
			"')";
		
		m_database.query(addPet);
		
	}
	
	private void releasePet(Player player, LivingEntity entity) {
		
		final UUID entityID = entity.getUniqueId();
		
		m_pets.remove(entityID);

		String removePet = 
				"DELETE FROM bAnimalCare " +
					"WHERE EntityID='" +
					entityID.toString() + 
				"'";
			
		m_database.query(removePet);
		
	}

}
