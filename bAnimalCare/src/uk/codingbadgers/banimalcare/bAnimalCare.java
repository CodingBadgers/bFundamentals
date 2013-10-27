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
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.module.Module;

public class bAnimalCare extends Module implements Listener {

	public static WorldGuardPlugin WORLDGUARD = null;
	
	private HashMap<UUID, PlayerPet> m_pets = new HashMap<UUID, PlayerPet>();
	
	private HashMap<String, Entity> m_tpVehicle = new HashMap<String, Entity>();
	
	private static int MAX_NOOF_PETS = 10;
	
	private String m_dbPrefix = "";
	
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
		
		m_dbPrefix = bFundamentals.getConfigurationManager().getDatabaseSettings().prefix;
		
		WORLDGUARD = (WorldGuardPlugin)m_plugin.getServer().getPluginManager().getPlugin("WorldGuard");
		register(this);
		registerCommand(new AnimalCommand(this));
		
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
			if (!m_pets.get(entity.getUniqueId()).GetOwner().equalsIgnoreCase(player.getName())) {
				if (!Module.hasPermission(player, "bAnimalCare.override")) {
					Module.sendMessage("bAnimalCare", player, "This animal is someone elses pet.");
					event.setCancelled(true);
				}
			} 
			else {
				if (item != null && item.getType() == Material.STRING) {
					Module.sendMessage("bAnimalCare", player, "You have released your pet...");
					releasePet(player, entity.getUniqueId());
					if (entity.getCustomName() != null) {
						entity.setCustomName(null);
						entity.setCustomNameVisible(false);
					}
				}
			}
			return;
		}
				
		if (item == null || item.getType() != Material.STRING) {
			return;
		}
		
		if (isBlackListed(entity)) {
			// Dont output a message, just pretend this never happened
			return;
		}
		
		if (!Module.hasPermission(player, "bAnimalCare.capture")) {
			Module.sendMessage("bAnimalCare", player, "You do not have permission to capture pet animals.");
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
	 * Called when a chunk is unloaded
	 */	
	@EventHandler(priority = EventPriority.LOW)
	public void onChunkUnload(ChunkUnloadEvent event) {
		Entity[] entities = event.getChunk().getEntities();
		
		// see if any entities were a pet, and update their location
		for (Entity entity : entities) {
			if (m_pets.containsKey(entity.getUniqueId())) {
				// this entity is a pet!
				m_pets.get(entity.getUniqueId()).UpdateLastKnownLocation(entity.getLocation());
				updatePet(entity);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamaged(EntityDamageEvent event) {
		
		if (!(event.getEntity() instanceof LivingEntity)) {
			return;
		}
		
		final LivingEntity entity = (LivingEntity)event.getEntity();
		final UUID entityID = entity.getUniqueId();
		
		if (!m_pets.containsKey(entityID)) {
			return;
		}
		
		event.setCancelled(true);
	}
	
	/**
	 * Called when an entity is damaged
	 */	
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
				
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
			
			PlayerPet playerPet = m_pets.get(entityID);
			
			if (Module.hasPermission(attackPlayer, "bAnimalCare.override")) {
				Module.sendMessage("bAnimalCare", attackPlayer, "Animal owner: " + playerPet.GetOwner());
			}
			
			if (playerPet.GetOwner().equalsIgnoreCase(attackPlayer.getName())) {
				Module.sendMessage("bAnimalCare", attackPlayer, "To kill your pet. First release it by using String.");
			}
		}
		
		// Someone's pet, cancel any damage.	
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		
		final Player player = event.getPlayer();
		if (!Module.hasPermission(player, "bAnimalCare.teleportHorse")) {
			return;
		}
		
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
				return;
			}
		}
		
	}
	
	/**
	 * Called when an entity teleports
	 */	
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityTeleport(PlayerTeleportEvent event) {

		final Player player = event.getPlayer();
		if (!Module.hasPermission(player, "bAnimalCare.teleportHorse")) {
			return;
		}
		
		if (event.getCause() != TeleportCause.PLUGIN && event.getCause() != TeleportCause.COMMAND) {
			return;
		}

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
	 * Get the number of pets a player has
	 */
	private int getNumberOfPets(Player player) {
		
		final String playerName = player.getName();
		int noofPets = 0;
		
		for (PlayerPet pet : m_pets.values()) {
			if (pet.GetOwner().equalsIgnoreCase(playerName)) {
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
			if (!m_pets.get(entity.getUniqueId()).GetOwner().equalsIgnoreCase(player.getName())) {
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
		PlayerPet pet = new PlayerPet(playerName);
		pet.UpdateLastKnownLocation(entity.getLocation());
		m_pets.put(entityID, pet);
		
		// Store the pet into our database
		addNewPet(playerName, entity);
		
		// Give the pet a custom name
		if (entity.getCustomName() == null) {
			String petName = getRandomPetName();
			entity.setCustomName(petName);
			entity.setCustomNameVisible(true);
		}
		
		String mobType = entity.getType().name().replace("Entity", "");
		Module.sendMessage("bAnimalCare", player, "You have tamed a new " + mobType.toLowerCase() + " pet.");
		
	}
	
	private boolean isBlackListed(LivingEntity entity) {
		
		if (entity.getType() == EntityType.WOLF)
			return true;
		
		if (entity.getType() == EntityType.HORSE)
			return false;
		
		if (entity.getType() == EntityType.SQUID)
			return false;
		
		if (entity.getType() == EntityType.VILLAGER)
			return false;
		
		if (entity instanceof Animals)
			return false;

		return true;
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
		
		if (m_database.tableExists(m_dbPrefix + "bAnimalCare"))
			return;
		
		final String createQuery = 
		"CREATE TABLE " + m_dbPrefix + "bAnimalCare " +
		"(" +
			"Player varchar(32)," +
			"EntityID TEXT," +
			"Location TEXT" +
		")";

		m_database.query(createQuery, true);
		
	}
	
	/**
	 * Load all entities into memory for a quick lookup
	 */		
	private void loadPets() {
		
		final String selectAll = "Select * FROM " + m_dbPrefix + "bAnimalCare";
		
		ResultSet result = m_database.queryResult(selectAll);
		if (result != null) {
			
			try {
				while (result.next()) {
					
					String playername = result.getString("Player");
					UUID entityID = UUID.fromString(result.getString("EntityID"));
					Location location = locationFromString(result.getString("Location"));
					
					PlayerPet pet = new PlayerPet(playername);
					pet.UpdateLastKnownLocation(location);
					m_pets.put(entityID, pet);
					
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
	 * Convert a location to a database format string
	 */	
	private String locationToString(Location location) {
		return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ();
	}
	
	/**
	 * Get a location from a location string
	 */	
	private Location locationFromString(String locationString) {
		
		String[] locationPart = locationString.split(":");

		World world = Bukkit.getWorld(locationPart[0].replace(":", ""));
		double xPosition = Double.parseDouble(locationPart[1].replace(":", ""));
		double yPosition = Double.parseDouble(locationPart[2].replace(":", ""));
		double zPosition = Double.parseDouble(locationPart[3].replace(":", ""));
		
		return  new Location(world, xPosition, yPosition, zPosition);
		
	}
	
	/**
	 * Add a new pet to the database
	 */	
	private void addNewPet(String player, Entity entity) {
		
		String addPet = 
			"INSERT INTO " + m_dbPrefix + "bAnimalCare " +
				"VALUES ('" +
				player + "', '" +
				entity.getUniqueId().toString() + "', '" +
				locationToString(entity.getLocation()) +
			"')";
		
		m_database.query(addPet);
		
	}
	
	/**
	 * Update a pet in the database
	 */	
	private void updatePet(Entity entity) {
		
		String updatePet = 
			"UPDATE " + m_dbPrefix + "bAnimalCare " +
				"SET Location='" + locationToString(entity.getLocation()) + "' " +
				"WHERE EntityID='" + entity.getUniqueId().toString() +
			"'";

		m_database.query(updatePet);
		
	}
	
	private void releasePet(OfflinePlayer player, UUID entityID) {
		
		m_pets.remove(entityID);

		String removePet = 
				"DELETE FROM " + m_dbPrefix + "bAnimalCare " +
					"WHERE EntityID='" +
					entityID.toString() + 
				"'";
			
		m_database.query(removePet);
		
	}

	public void listPets(Player player, OfflinePlayer listPlayer) {
		
		final String playerName = listPlayer.getName();
		HashMap<String, PlayerPet> pets = new HashMap<String, PlayerPet>();
		
		for (Entry<UUID, PlayerPet> petEntry : m_pets.entrySet()) {
			if (petEntry.getValue().GetOwner().equalsIgnoreCase(playerName)) {
				pets.put(petEntry.getKey().toString(), petEntry.getValue());
			}
		}
		
		if (player.getName().equalsIgnoreCase(listPlayer.getName())) {
			Module.sendMessage("bAnimalCare", player, "You have " + pets.size() + " pets...");
		} else {
			Module.sendMessage("bAnimalCare", player, listPlayer.getName() + " has " + pets.size() + " pets...");
		}

		int petIndex = 0;
		for (Entry<String, PlayerPet> petEntry : pets.entrySet()) {
			UUID petId = UUID.fromString(petEntry.getKey());
			Entity pet = entityFromUUID(petId);
			if (pet == null) {
				// Get last known location from database
				Location location = petEntry.getValue().GetLocation();
				location.getWorld().loadChunk(location.getChunk());
				pet = entityFromUUID(petId, location.getChunk());
				if (pet == null) {

					String petLocation = "[" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ", " + location.getWorld().getName() + "]";
					String petInfo = petIndex + " - " + petLocation;
					Module.sendMessage("bAnimalCare", player, petInfo);
					
					petIndex++;
					continue;
				}
			}
						
			String petName = ((LivingEntity)pet).getCustomName();
			Location location = pet.getLocation();
			String petLocation = "[" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ", " + location.getWorld().getName() + "]";
			
			String petInfo = petIndex + " - " + petName + " - " + petLocation;
			Module.sendMessage("bAnimalCare", player, petInfo);
			
			petIndex++;
		}
		
	}

	private Entity entityFromUUID(UUID petId) {
		
		for (World world : Bukkit.getWorlds()) {
			for (Entity entity : world.getEntities()) {
				if (entity.getUniqueId().toString().equalsIgnoreCase(petId.toString()))
					return entity;
			}
		}
		
		return null;
		
	}
	
	private Entity entityFromUUID(UUID petId, Chunk chunk) {
		
		for (Entity entity : chunk.getEntities()) {
			if (entity.getUniqueId().toString().equalsIgnoreCase(petId.toString()))
				return entity;
		}
		
		return null;
		
	}

	public void releasePet(OfflinePlayer player, String petIdString) {
		int petIndex = Integer.parseInt(petIdString);
		
		final String playerName = player.getName();
		HashMap<String, PlayerPet> pets = new HashMap<String, PlayerPet>();
		
		for (Entry<UUID, PlayerPet> petEntry : m_pets.entrySet()) {
			if (petEntry.getValue().GetOwner().equalsIgnoreCase(playerName)) {
				pets.put(petEntry.getKey().toString(), petEntry.getValue());
			}
		}
		
		int index = 0;
		for (Entry<String, PlayerPet> petEntry : pets.entrySet()) {
			if (index == petIndex) {
				UUID petId = UUID.fromString(petEntry.getKey());
				releasePet(player, petId);
				
				if (player.isOnline()) {
					Module.sendMessage(getName(), player.getPlayer(), "Your pet has been released...");
				}
				
				Entity pet = entityFromUUID(petId);
				if (pet == null) {
					// Get last known location from database
					Location location = petEntry.getValue().GetLocation();
					location.getWorld().loadChunk(location.getChunk());
					pet = entityFromUUID(petId, location.getChunk());
				}
				
				if (pet != null) {
					((LivingEntity)pet).setCustomName(null);
					((LivingEntity)pet).setCustomNameVisible(false);
				}
				break;
			}
			index++;
		}
		
	}

	public void tpToPet(Player player, String petIdString, OfflinePlayer owner) {
		
		int petIndex = Integer.parseInt(petIdString);
		
		final String playerName = owner.getName();
		HashMap<String, PlayerPet> pets = new HashMap<String, PlayerPet>();
		
		for (Entry<UUID, PlayerPet> petEntry : m_pets.entrySet()) {
			if (petEntry.getValue().GetOwner().equalsIgnoreCase(playerName)) {
				pets.put(petEntry.getKey().toString(), petEntry.getValue());
			}
		}
		
		int index = 0;
		for (Entry<String, PlayerPet> petEntry : pets.entrySet()) {
			if (index == petIndex) {
				UUID petId = UUID.fromString(petEntry.getKey());
				Entity pet = entityFromUUID(petId);
				Location location;
				if (pet == null) {
					// Get last known location from database
					location = petEntry.getValue().GetLocation();
				}
				else {
					location = pet.getLocation();
				}
				
				player.teleport(location);
				Module.sendMessage(getName(), player, "You have been teleported to the pet...");
				break;
			}
		}
		
	}
	
	private String getRandomPetName() {
		String[] allnames =
			{
				"Aaliyah","Aaron","Aarushi","Abbey","Abbi","Abbie","Abby","Abdul","Abdullah","Abel",
				"Abi","Abia","Abigail","Abraham","Abram","Abriel","Acacia","Ace","Ada","Adalyn",
				"Adam","Adan","Addie","Addison","Ade","Adelaide","Adele","Adelina","Aden","Adnan",
				"Adreanna","Adrian","Adriana","Adrianna","Adrianne","Adrienne","Aerona","Agatha","Aggie","Agnes",
				"Ahmad","Aida","Aidan","Aiden","Aileen","Aimee","Aine","Ainsley","Ainsley","Aisha",
				"Aisling","Al","Alain","Alaina","Alan","Alana","Alanna","Alannah","Alayah","Alayna",
				"Alba","Albert","Alberto","Alden","Aleah","Alec","Alecia","Aleisha","Alejandra","Alejandro",
				"Alena","Alessandra","Alex","Alex","Alexa","Alexander","Alexandra","Alexandria","Alexia","Alexis",
				"Alexis","Alexus","Alfie","Alfonso","Alfred","Alfredo","Ali","Ali","Alia","Alice",
				"Alicia","Alina","Alisa","Alisha","Alison","Alissa","Alistair","Alivia","Aliyah","Alize",
				"Alka","Allan","Allen","Allie","Allison","Ally","Allyson","Alma","Alondra","Alonzo",
				"Alphonso","Alton","Alvin","Alycia","Alyshialynn","Alyson","Alyssa","Alyssia","Amalia","Amanda",
				"Amani","Amara","Amari","Amari","Amaris","Amaya","Amber","Amelia","Amelie","America",
				"Amethyst","Amie","Amina","Amir","Amos","Amy","Amya","Ana","Anahi","Anamaria",
				"Anastasia","Andie","Andre","Andrea","Andreas","Andres","Andrew","Andromeda","Andy","Angel",
				"Angel","Angela","Angelia","Angelica","Angelina","Angeline","Angelique","Angelo","Angie","Angus",
				"Anika","Anita","Aniya","Aniyah","Anjali","Ann","Anna","Annabel","Annabella","Annabelle",
				"Annabeth","Annalisa","Annalise","Anne","Anneke","Annemarie","Annette","Annie","Annika","Annmarie",
				"Anselma","Anthea","Anthony","Antoinette","Anton","Antonia","Antonio","Antony","Anuja","Anya",
				"Aoibhe","Aoife","Aphrodite","Apple","April","Aqua","Arabella","Aran","Archer","Archie",
				"Ari","Aria","Ariadne","Ariana","Arianna","Arianne","Ariel","Ariella","Arielle","Arisha",
				"Arleen","Arlene","Arlo","Arman","Armando","Arnold","Arrie","Art","Artemis","Arthur",
				"Arturo","Asa","Ash","Asha","Ashanti","Asher","Ashlee","Ashleigh","Ashley","Ashley",
				"Ashlie","Ashlyn","Ashlynn","Ashton","Ashton","Ashvini","Asia","Asma","Aspen","Aspen",
				"Aston","Astrid","Athena","Atticus","Aubree","Aubrey","Audra","Audrey","Audwin","August",
				"Aurora","Austin","Autumn","Ava","Avalon","Avery","Avery","Avril","Axel","Aya",
				"Ayanna","Ayden","Ayesha","Ayisha","Ayla","Azalea","Azaria","Azariah","Bailey","Bailey",
				"Barbara","Barclay","Barnaby","Barney","Barry","Bart","Bartholomew","Basil","Baylee","Bea",
				"Beatrice","Beatrix","Beau","Becca","Beccy","Beckett","Becky","Belinda","Bella","Belle",
				"Ben","Benedict","Benita","Benjamin","Bennett","Bennie","Benny","Benthe","Bentley","Bernadette",
				"Bernard","Bernice","Bert","Bertha","Beryl","Bess","Beth","Bethan","Bethanie","Bethany",
				"Betsy","Bettina","Betty","Beverly","Beyonce","Bhu","Bianca","Bill","Billie","Billy",
				"Bladen","Blain","Blaine","Blair","Blaise","Blake","Blanche","Blaze","Blossom","Blythe",
				"Bob","Bobbi","Bobbie","Bobby","Bonita","Bonnie","Boris","Boston","Boyd","Brad",
				"Braden","Bradford","Bradley","Bradwin","Brady","Braeden","Braelyn","Branden","Brandi","Brandon",
				"Braxton","Brayan","Brayden","Braydon","Breanna","Bree","Brenda","Brendan","Brenden","Brendon",
				"Brenna","Brennan","Brent","Bret","Brett","Brevyn","Bria","Brian","Briana","Brianna",
				"Brianne","Brice","Bridget","Bridgette","Briella","Brielle","Brinley","Britney","Britt","Brittany",
				"Brittney","Brock","Brodie","Brody","Brogan","Bronson","Bronte","Bronwen","Bronwyn","Brooke",
				"Brooklyn","Brooklynn","Bruce","Bruno","Bryan","Bryanna","Bryant","Bryce","Brynlee","Brynn",
				"Bryon","Bryony","Bryson","Buddy","Burt","Burton","Butch","Byron","Cadby","Cade",
				"Caden","Cadence","Cael","Caesar","Cailin","Caitlan","Caitlin","Caitlyn","Caleb","Caleigh",
				"Calhoun","Cali","Callan","Callie","Callista","Callum","Calum","Calvin","Calypso","Cam",
				"Camden","Cameron","Cameron","Cami","Camila","Camilla","Camille","Campbell","Camron","Camryn",
				"Candace","Candice","Candy","Caoimhe","Caprice","Cara","Carey","Carina","Carissa","Carl",
				"Carla","Carley","Carlos","Carlton","Carly","Carlynn","Carmen","Carol","Carole","Carolina",
				"Caroline","Carolyn","Carrie","Carsen","Carson","Carter","Carter","Cary","Carys","Casey",
				"Casey","Cash","Casper","Cassandra","Cassidy","Cassie","Castiel","Cate","Caterina","Cathal",
				"Cathalina","Catherine","Cathleen","Cathy","Catriona","Cayden","Cayla","Cece","Cecelia","Cecil",
				"Cecilia","Cecily","Cedric","Celeste","Celestine","Celia","Celina","Celine","Cerys","Cesar",
				"Chad","Chance","Chandler","Chanel","Chanelle","Channing","Chantal","Chantelle","Charis","Charity",
				"Charlene","Charles","Charley","Charlie","Charlie","Charlize","Charlotte","Charmaine","Chase","Chastity",
				"Chaz","Chelsea","Chelsey","Chenille","Cher","Cheri","Cherie","Cherry","Cheryl","Chesney",
				"Chester","Cheyanne","Cheyenne","Chiara","Chip","Chloe","Chris","Chris","Chrissy","Christa",
				"Christal","Christi","Christian","Christiana","Christie","Christina","Christine","Christopher","Christy","Chrystal",
				"Chuck","Cian","Ciara","Ciaran","Ciel","Cierra","Cillian","Cindy","Claire","Clancy",
				"Clara","Clare","Clarence","Clarice","Clarissa","Clarisse","Clark","Claude","Claudette","Claudia",
				"Clay","Clayton","Clea","Clement","Cleo","Clifford","Clifton","Clint","Clinton","Clodagh",
				"Clover","Clyde","Coby","Coco","Cody","Colby","Cole","Colette","Colin","Colleen",
				"Collin","Colm","Colt","Colton","Conner","Connie","Connor","Conor","Conrad","Constance",
				"Cooper","Cora","Coral","Coralie","Coraline","Corbin","Cordelia","Corey","Cori","Corina",
				"Corinne","Cormac","Cornelius","Corra","Cory","Cosette","Courtney","Craig","Cristian","Cristina",
				"Cristobal","Cruz","Crystal","Cullen","Curt","Curtis","Cuthbert","Cynthia","Cyril","Cyrus",
				"Dacey","Dagmar","Dahlia","Daire","Daisy","Dakota","Dakota","Dale","Dallas","Dalton",
				"Damian","Damien","Damion","Damon","Dan","Dana","Dana","Dane","Danette","Dani",
				"Danica","Daniel","Daniela","Daniella","Danielle","Danika","Danny","Dante","Daphne","Dara",
				"Dara","Daragh","Darcie","Darcy","Darcy","Daren","Daria","Darian","Darin","Darius",
				"Darla","Darlene","Darnell","Darragh","Darrel","Darrell","Darren","Darrin","Darryl","Darwin",
				"Daryl","Dashawn","Dashee","Dave","David","Davida","Davina","Davion","Davis","Dawn",
				"Dawson","Dax","Dayna","Daysha","Deacon","Dean","Deana","Deandre","Deann","Deanna",
				"Deanne","Debbie","Debora","Deborah","Debra","Declan","Dee","Deena","Deidre","Deirdre",
				"Deja","Delaney","Delanie","Delbert","Delia","Delilah","Della","Delores","Demetrius","Demi",
				"Dena","Denis","Denise","Dennis","Denny","Denver","Denzel","Derek","Derrick","Desiree",
				"Desmond","Destinee","Destiny","Devin","Devon","Dewayne","Dewey","Dexter","Dhalsim","Diamond",
				"Diana","Diane","Dianna","Dianne","Diarmuid","Dick","Diego","Dieter","Dillon","Dimitri",
				"Dina","Dino","Dion","Dionne","Dirk","Django","Dmitri","Dolly","Dolores","Dominic",
				"Dominick","Dominique","Don","Donald","Donna","Donnie","Donovan","Dora","Doreen","Dorian",
				"Doris","Dorothy","Doug","Douglas","Doyle","Drake","Drew","Drew","Duane","Duke",
				"Dulce","Duncan","Dustin","Dwayne","Dwight","Dylan","Eabha","Eamon","Earl","Earnest",
				"Easton","Ebony","Ed","Eddie","Eddy","Eden","Eden","Edgar","Edie","Edison",
				"Edith","Edmund","Edna","Eduardo","Edward","Edwin","Efrain","Eileen","Eilidh","Eimear",
				"Elaina","Elaine","Eleanor","Electra","Elena","Eli","Eliana","Elias","Elijah","Elin",
				"Elina","Eliot","Elisa","Elisabeth","Elise","Elisha","Eliza","Elizabeth","Ella","Elle",
				"Ellen","Ellie","Elliot","Elliott","Ellis","Elly","Elmer","Elmo","Elodie","Eloise",
				"Elora","Elsa","Elsie","Elyza","Emanuel","Emanuela","Ember","Emely","Emer","Emerald",
				"Emerson","Emet","Emil","Emilee","Emilia","Emiliano","Emilie","Emilio","Emily","Emma",
				"Emmaline","Emmalyn","Emmanuel","Emmeline","Emmett","Emmie","Emmy","Enrique","Enzo","Eoghan",
				"Eoin","Eric","Erica","Erick","Erik","Erika","Erin","Ernest","Ernesto","Ernie",
				"Errol","Ervin","Esmay","Esme","Esmeralda","Esteban","Estee","Estelle","Ester","Esther",
				"Ethan","Ethel","Etienne","Eugene","Eugenie","Eunice","Eustace","Eva","Evan","Evangeline",
				"Evangelos","Eve","Evelyn","Everett","Evie","Ewan","Ezekiel","Ezra","abian","abio",
				"Fabrizia","Faith","Fallon","Fanny","Farah","arley","Farrah","Fatima","Fawn","Fay",
				"Faye","ebian","Felicia","Felicity","elipe","elix","Fern","Fernanda","ernando","Ffion",
				"Fidel","inbar","inlay","inley","inn","Fiona","ionn","letcher","Fleur","Flor",
				"Flora","Florence","loyd","lynn","orrest","oster","ox","Frances","Francesca","rancesco",
				"Francine","rancis","rancisco","rank","rankie","ranklin","ranklyn","raser","red","reddie",
				"Freddy","rederick","redrick","Freya","Gabby","Gabe","Gabriel","Gabriela","Gabriella","Gabrielle",
				"Gage","Gail","Gale","Gareth","Garman","Garrett","Garrison","Garry","Garth","Gary",
				"Gavin","Gayle","Gaynor","Gemma","Gena","Gene","Genesis","Genevieve","Geoffrey","George",
				"Georgia","Georgina","Geraint","Gerald","Geraldine","Gerard","Gerardo","Gerry","Gertrude","Gia",
				"Gian","Gianna","Gideon","Gigi","Gilbert","Gilberto","Giles","Gillian","Gina","Ginger",
				"Ginny","Gino","Giorgio","Giovanna","Giovanni","Giselle","Gisselle","Gladys","Glen","Glenda",
				"Glenn","Gloria","Glyndwr","Godric","Gordon","Grace","Gracie","Grady","Graeme","Graham",
				"Grant","Grayson","Greg","Gregg","Gregor","Gregory","Greta","Gretchen","Griffin","Guadalupe",
				"Guillermo","Guinevere","Gunner","Gus","Gustavo","Guy","Gwen","Gwendolyn","Gwyneth","Habiba",
				"Hadley","Hailee","Hailey","Haleigh","Haley","Halle","Hallie","Hamish","Hank","Hanna",
				"Hannah","Hans","Harley","Harley","Harmony","Harold","Harper","Harriet","Harrison","Harry",
				"Harvey","Hassan","Hattie","Hayden","Haylee","Hayley","Hazel","Hazeline","Heath","Heather",
				"Heaven","Hector","Heidi","Helen","Helena","Helga","Helina","Henri","Henrietta","Henry",
				"Hera","Herbert","Herman","Hermione","Hester","Hetty","Hilary","Hilda","Holden","Hollie",
				"Holly","Homer","Honesty","Honey","Honor","Honour","Hope","Horace","Horatio","Howard",
				"Hubert","Hudson","Hugh","Hugo","Hunter","Huw","Hyacinth","Iain","Ian","Ibrahim",
				"Ida","Idris","Iggy","Ignacio","Igor","Iliana","Ilona","Ilse","Imani","Imogen",
				"India","Indiana","Indira","Ines","Ingrid","Iona","Ira","Irene","Iris","Irma",
				"Irving","Irwin","Isa","Isaac","Isabel","Isabella","Isabelle","Isaiah","Isha","Isiah",
				"Isis","Isla","Ismael","Isobel","Isolde","Israel","Issac","Itzel","Ivan","Ivana",
				"Ivy","Iyanna","Izabella","Izidora","Izzy","Jace","Jacinda","Jacinta","Jack","Jackie",
				"Jackie","Jackson","Jacob","Jacoby","Jacqueline","Jacquelyn","Jada","Jade","Jaden","Jaden",
				"Jadon","Jadyn","Jaelynn","Jago","Jai","Jaiden","Jaime","Jake","Jakob","Jalen",
				"Jamal","James","Jameson","Jamie","Jamie","Jamison","Jan","Jana","Jancis","Jane",
				"Janelle","Janessa","Janet","Janette","Janice","Janie","Janine","Janis","Janiya","Jaqueline",
				"Jared","Jarrett","Jarvis","Jase","Jasmin","Jasmine","Jason","Jasper","Javier","Javon",
				"Jaxon","Jaxson","Jay","Jayda","Jayden","Jayden","Jaydon","Jayla","Jaylen","Jaylin",
				"Jaylinn","Jaylon","Jaylynn","Jayne","Jayson","Jazlyn","Jazmin","Jazmine","Jean","Jeanette",
				"Jeanine","Jeanne","Jeannette","Jeannie","Jeannine","Jeb","Jed","Jediah","Jeff","Jeffery",
				"Jeffrey","Jeffry","Jemima","Jemma","Jena","Jenna","Jenni","Jennie","Jennifer","Jenny",
				"Jensen","Jensen","Jerald","Jeremiah","Jeremy","Jeri","Jericho","Jermaine","Jerome","Jerri",
				"Jerry","Jess","Jesse","Jessica","Jessie","Jessie","Jesus","Jethro","Jett","Jewel",
				"Jill","Jillian","Jim","Jimmie","Jimmy","Jo","Joan","Joann","Joanna","Joanne",
				"Joaquin","Jocelyn","Jodi","Jodie","Jody","Jody","Joe","Joel","Joelle","Joey",
				"Johan","Johanna","John","Johnathan","Johnathon","Johnnie","Johnny","Jolene","Jolie","Jon",
				"Jonah","Jonas","Jonathan","Jonathon","Joni","Jordan","Jordan","Jordy","Jordyn","Jorge",
				"Jorja","Jose","Joselyn","Joseph","Josephine","Josh","Joshua","Josiah","Josie","Josue",
				"Joy","Joyce","Juan","Juanita","Judas","Jude","Judith","Judy","Jules","Julia",
				"Julian","Juliana","Julianna","Julianne","Julie","Juliet","Juliette","Julio","Julissa","Julius",
				"June","Justice","Justice","Justin","Justine","Kacey","Kade","Kaden","Kai","Kaiden",
				"Kaidence","Kailey","Kailyn","Kaitlin","Kaitlyn","Kaitlynn","Kale","Kalea","Kaleb","Kaleigh",
				"Kali","Kalia","Kamala","Kameron","Kamryn","Kane","Kara","Karen","Kari","Karin",
				"Karina","Karissa","Karl","Karla","Karla","Karlee","Karly","Karolina","Karyn","Kasey",
				"Kassandra","Kassidy","Kassie","Kat","Katarina","Kate","Katelyn","Katelynn","Katerina","Katharine",
				"Katherine","Kathleen","Kathryn","Kathy","Katia","Katie","Katlyn","Katrina","Katy","Katya",
				"Kay","Kaya","Kayden","Kaye","Kayla","Kaylee","Kayleigh","Kaylen","Kayley","Kaylie",
				"Kaylin","Kayson","Keanu","Keaton","Kedrick","Keegan","Keeley","Keely","Keenan","Keira",
				"Keisha","Keith","Kelis","Kellen","Kelley","Kelli","Kellie","Kellin","Kelly","Kelly",
				"Kelsey","Kelsie","Kelvin","Ken","Kendall","Kendall","Kendra","Kendrick","Kennedy","Kenneth",
				"Kenny","Kent","Kenzie","Keri","Kerian","Kerri","Kerry","Kerry","Kevin","Khalil",
				"Kian","Kiana","Kiara","Kiera","Kieran","Kierra","Kiersten","Kiki","Kiley","Killian",
				"Kim","Kim","Kimberlee","Kimberley","Kimberly","Kimbriella","Kimmy","Kingsley","Kingston","Kinley",
				"Kinsey","Kinsley","Kip","Kira","Kirk","Kirsten","Kirsty","Kiswa","Kit","Kitty",
				"Klay","Kobe","Kody","Kolby","Kourtney","Kris","Kris","Krista","Kristen","Kristi",
				"Kristian","Kristie","Kristin","Kristina","Kristine","Kristopher","Kristy","Krystal","Kurt","Kurtis",
				"Kye","Kyla","Kylar","Kyle","Kylee","Kyleigh","Kylen","Kyler","Kylie","Kyra",
				"Kyrin","Lacey","Lacey","Lachlan","Lacy","Ladonna","Laila","Lakyn","Lala","Lamar",
				"Lamont","Lana","Lance","Landen","Landon","Lane","Langdon","Lara","Larissa","Larry",
				"Lars","Laura","Laurel","Lauren","Laurence","Lauri","Laurie","Laurie","Lauryn","Lavana",
				"Lavender","Lawrence","Lawson","Layla","Layne","Layton","Lea","Leah","Leandro","Leann",
				"Leanna","Leanne","Lee","Lee","Leela","Leena","Leia","Leigh","Leila","Leilani",
				"Lela","Leland","Lena","Lennie","Lennox","Lenny","Leo","Leon","Leona","Leonard",
				"Leonardo","Leonel","Leonie","Leopold","Leora","Leroy","Lesa","Lesley","Leslie","Leslie",
				"Lesly","Lester","Leticia","Leuan","Leven","Levi","Lewis","Lexi","Lexie","Lia",
				"Liam","Liana","Lianne","Libby","Liberty","Lidia","Lief","Lila","Lilac","Lilah",
				"Lilian","Liliana","Lilita","Lilith","Lillian","Lillie","Lilly","Lily","Lina","Lincoln",
				"Linda","Lindsay","Lindsey","Lionel","Lisa","Lisandro","Lisette","Livia","Liz","Liza",
				"Lizbeth","Lizzie","Lizzy","Lloyd","Lochlan","Logan","Logan","Lois","Lola","Lolita",
				"Lonnie","Lora","Loran","Loren","Lorena","Lorenzo","Loretta","Lori","Lorie","Loris",
				"Lorna","Lorraine","Lorri","Lorrie","Lottie","Lotus","Louie","Louis","Louisa","Louise",
				"Lowell","Luann","Luca","Lucas","Lucia","Lucian","Luciano","Lucie","Lucille","Lucinda",
				"Lucky","Lucy","Luigi","Luis","Lukas","Luke","Lulu","Luna","Lupita","Luther",
				"Lydia","Lyla","Lyle","Lyna","Lynda","Lyndon","Lynette","Lynn","Lynn","Lynne",
				"Lynnette","Lyra","Lysander","Mabel","Macey","Macie","Mack","Mackenzie","Macy","Mada",
				"adalyn","Maddie","Maddison","Maddox","Maddy","Madeleine","Madeline","Madelyn","Madison","Madisyn",
				"adyson","Mae","Maeve","Magda","Magdalena","Maggie","Magnus","Maia","Maire","Maisie",
				"aisy","Maja","Makayla","Makenna","Makenzie","Malachi","Malcolm","Malia","Malik","Mallory",
				"andy","Manuel","Mara","Marc","Marcel","Marcella","Marci","Marcia","Marco","Marcos",
				"Marcus","Marcy","Margaret","Margarita","Margie","Margo","Margret","Maria","Mariah","Mariam",
				"arian","Mariana","Marianna","Marianne","Marie","Marilyn","Marina","Mario","Marion","Marion",
				"arisa","Marisol","Marissa","Maritza","Marjorie","Mark","Marla","Marlee","Marlene","Marley",
				"Marley","Marlon","Marnie","Marquis","Marsha","Marshall","Martha","Martin","Martina","Marty",
				"Martyn","Marvin","Mary","Maryam","Maryann","Marybeth","Mason","Mat","Mateo","Mathew",
				"atilda","Matt","Matthew","Matthias","Maura","Maureen","Maurice","Mauricio","Maverick","Mavis",
				"Max","Maximilian","Maximus","Maxine","Maxwell","May","Maya","Mckayla","Mckenna","Mckenzie",
				"ea","Meadow","Meagan","Meera","Meg","Megan","Meghan","Mehtab","Mei","Mekhi",
				"elanie","Melina","Melinda","Melissa","Melody","Melvin","Mercedes","Mercy","Meredith","Merick",
				"Mervyn","Mia","Micah","Michael","Michaela","Micheal","Michele","Michelle","Mick","Mickey",
				"Miguel","Mika","Mikaela","Mikayla","Mike","Mikey","Mikhaela","Mila","Milan","Mildred",
				"ilena","Miles","Miley","Miller","Millie","Milly","Milo","Milton","Mimi","Mina",
				"indy","Minerva","Minnie","Mira","Miranda","Miriam","Misha","Misty","Mitch","Mitchell",
				"Mitt","Mitzi","Mohamed","Mohammad","Mohammed","Moises","Mollie","Molly","Mona","Monica",
				"onika","Monique","Montana","Monte","Monty","Morgan","Morgan","Morris","Moses","Muhammad",
				"Murphy","Murray","Mya","Myfanwy","Myles","Myra","Myron","Nadene","Nadia","Nadine",
				"Naja","Nala","Nancy","Nanette","Naomi","Nash","Nasir","Natalia","Natalie","Natasha",
				"Nate","Nath","Nathan","Nathanael","Nathaniel","Naya","Nayeli","Neal","Ned","Neil",
				"Nell","Nelly","Nelson","Nena","Nesbit","Nestor","Nevaeh","Neve","Neville","Nia",
				"Niall","Niamh","Nichola","Nicholas","Nick","Nicki","Nickolas","Nicky","Nico","Nicola",
				"Nicolas","Nicole","Nicolette","Nigel","Niki","Nikita","Nikki","Nikolas","Nila","Nils",
				"Nina","Nishka","Noah","Noe","Noel","Noelle","Nolan","Nora","Noreen","Norma",
				"Norman","Nova","Oakes","Oakley","Oasis","Ocean","Octavia","Octavio","Odalis","Odele",
				"Odelia","Odette","Oisin","Olga","Olive","Oliver","Olivia","Ollie","Olly","Omar",
				"Opal","Ophelia","Oran","Orianna","Orla","Orlaith","Orlando","Oscar","Osvaldo","Otis",
				"Otto","Owen","Ozzie","Pablo","Padraig","Paige","Paisley","Palmer","Pam","Pamela",
				"Pandora","Pansy","Paola","Paolo","Paris","Parker","Pascal","Pasquale","Pat","Patience",
				"Patrice","Patricia","Patrick","Patsy","Patti","Patty","Paul","Paula","Paulette","Paulina",
				"Pauline","Paxton","Payton","Payton","Pearl","Pedro","Peggy","Penelope","Penny","Percy",
				"Perla","Perry","Persephone","Petar","Pete","Peter","Petra","Petunia","Peyton","Peyton",
				"Phebian","Phil","Philip","Phillip","Phillipa","Philomena","Phineas","Phoebe","Phoenix","Phoenix",
				"Phyllis","Pierce","Piers","Piper","Pippa","Polly","Poppy","Porter","Portia","Precious",
				"Presley","Preslie","Preston","Primrose","Prince","Princess","Priscilla","Promise","Prudence","Prue",
				"Queenie","Quentin","Quiana","Quincy","Quinlan","Quinn","Quinn","Quinton","Quintrell","Rabia",
				"Rachael","Rachel","Rachelle","Rae","Raegan","Rafael","Rafferty","Raheem","Rahul","Raiden",
				"Raina","Raine","Rajesh","Ralph","Ram","Rameel","Ramon","Ramona","Ramsha","Randal",
				"Randall","Randi","Randolph","Randy","Rania","Raphael","Raquel","Rashad","Rashan","Rashid",
				"Raul","Raven","Ravi","Ray","Raymond","Rayna","Rayne","Reagan","Reanna","Reanne",
				"Rebecca","Rebekah","Reece","Reed","Reef","Reese","Reese","Regan","Regina","Reginald",
				"Rehan","Reid","Reina","Remco","Rena","Renata","Rene","Rene","Renee","Reuben",
				"Rex","Reyna","Reynaldo","Reza","Rhea","Rhett","Rhian","Rhiannon","Rhoda","Rhona",
				"Rhonda","Rhys","Ria","Rian","Rianna","Ricardo","Richard","Richie","Rick","Rickey",
				"Rickie","Ricky","Rico","Rihanna","Rik","Riker","Rikki","Riley","Riley","Rio",
				"Rita","River","River","Roanne","Rob","Robbie","Robby","Robert","Roberta","Roberto",
				"Robin","Robin","Robyn","Rocco","Rochelle","Rocio","Rocky","Rod","Roderick","Rodger",
				"Rodney","Rodolfo","Rodrigo","Rogelio","Roger","Rohan","Roisin","Roland","Rolando","Roman",
				"Romeo","Ron","Ronald","Ronan","Ronda","Roni","Ronnie","Ronny","Roosevelt","Rory",
				"Rosa","Rosalie","Rosalind","Rosalynn","Rose","Rosella","Rosemarie","Rosemary","Rosetta","Rosie",
				"Ross","Rosy","Rowan","Rowan","Roxanne","Roxie","Roxy","Roy","Rozlynn","Ruairi",
				"Ruben","Rubin","Ruby","Rudolph","Rudy","Rufus","Rupert","Russell","Rusty","Ruth",
				"Ryan","Ryder","Ryker","Rylan","Ryland","Rylee","Ryleigh","Ryley","Rylie","Sabrina",
				"Sade","Sadhbh","Sadie","Saffron","Sage","Said","Saige","Saira","Sally","Salma",
				"Salome","Salvador","Salvatore","Sam","Sam","Samantha","Samara","Samir","Samira","Sammie",
				"Sammy","Samuel","Sandra","Sandy","Sandy","Sanjay","Santiago","Saoirse","Sapphire","Sara",
				"Sarah","Sarina","Sariya","Sascha","Sasha","Saskia","Saul","Savanna","Savannah","Sawyer",
				"Scarlet","Scarlett","Scot","Scott","Scottie","Scotty","Seamus","Sean","Seb","Sebastian",
				"Sebastianne","Sebestian","Selah","Selena","Selina","Selma","Senuri","Seren","Serena","Serenity",
				"Sergio","Seth","Shakira","Shana","Shane","Shania","Shannon","Shannon","Shari","Sharon",
				"Shaun","Shauna","Shawn","Shawn","Shawna","Shawnette","Shayla","Shea","Shea","Sheena",
				"Sheila","Shelby","Sheldon","Shelia","Shelley","Shelly","Sheri","Sheridan","Sherman","Sherri",
				"Sherrie","Sherry","Sheryl","Shirley","Shreya","Shyla","Sian","Sid","Sidney","Sidney",
				"Sienna","Sierra","Silas","Silvia","Simon","Simone","Simran","Sinead","Siobhan","Sky",
				"Skye","Skylar","Skylar","Skyler","Skyler","Slade","Sloane","Sofia","Solomon","Sondra",
				"Sonia","Sonja","Sonny","Sonya","Sophia","Sophie","Sophy","Spencer","Spike","Stacey",
				"Stacey","Staci","Stacie","Stacy","Stacy","Stan","Stanley","Star","Starla","Stefan",
				"Stefanie","Stella","Stephan","Stephanie","Stephen","Sterling","Steve","Steven","Stevie","Stewart",
				"Stuart","Sue","Suki","Summer","Susan","Susanna","Susanne","Susie","Sutton","Suzanne",
				"Suzette","Suzy","Sybil","Sydney","Sylvester","Sylvia","Tabatha","Tabitha","Tadhg","Tahlia",
				"Tala","Talia","Tallulah","Tamara","Tamera","Tami","Tamia","Tammi","Tammie","Tammy",
				"Tamra","Tamsin","Tania","Tanisha","Tanner","Tanya","Tara","Tariq","Tarquin","Taryn",
				"Tasha","Tate","Tatiana","Tatum","Tawana","Taya","Tayla","Taylah","Tayler","Taylor",
				"Taylor","Teagan","Ted","Teddy","Teegan","Tegan","Teo","Terence","Teresa","Teri",
				"Terrance","Terrell","Terrence","Terri","Terrie","Terry","Terry","Tess","Tessa","Tex",
				"Thad","Thaddeus","Thea","Thelma","Theo","Theodore","Theresa","Therese","Thomas","Tia",
				"Tiago","Tiana","Tiberius","Tiffany","Tiger","Tilly","Tim","Timmy","Timothy","Tina",
				"Titus","Tobias","Toby","Tod","Todd","Tom","Tomas","Tommie","Tommy","Toni",
				"Tonia","Tony","Tonya","Tori","Toryn","Trace","Tracey","Tracey","Traci","Tracie",
				"Tracy","Tracy","Travis","Trent","Trenton","Trevon","Trevor","Trey","Tricia","Trina",
				"Trinity","Trish","Trisha","Tristan","Tristen","Triston","Trixy","Troy","Trudy","Tucker",
				"Ty","Tyler","Tyra","Tyrese","Tyrone","Tyson","Ulrica","Ulysses","Uma","Umar",
				"Una","Uriah","Uriel","Ursula","Usama","Valarie","Valentin","Valentina","Valentino","Valeria",
				"Valerie","Van","Vance","Vanessa","Vasco","Vaughn","Veda","Velma","Venetia","Venus",
				"Vera","Verity","Vernon","Veronica","Vicki","Vickie","Vicky","Victor","Victoria","Vihan",
				"Vijay","Vince","Vincent","Vinnie","Viola","Violet","Virgil","Virginia","Vishal","Vivian",
				"Vivian","Viviana","Vivienne","Vladimir","Vonda","Wade","Walker","Wallace","Wallis","Walter",
				"Wanda","Warren","Waverley","Waylon","Wayne","Wendell","Wendi","Wendy","Wesley","Weston",
				"Whitney","Wilbert","Wilbur","Wiley","Wilfred","Wilhelm","Wilhelmina","Will","Willam","Willard",
				"Willem","William","Willie","Willis","Willow","Wilma","Wilson","Winnie","Winnifred","Winston",
				"Winter","Wolfgang","Wyatt","Xander","Xanthe","Xavier","Xaviera","Xena","Ximena","Xochitl",
				"Yahir","Yardley","Yasmin","Yasmine","Yehudi","Yesenia","Yestin","Yolanda","York","Ysabel",
				"Yulissa","Yuri","Yvaine","Yvette","Yvonne","Zac","Zach","Zachariah","Zachary","Zachery",
				"Zack","Zackary","Zackery","Zaheera","Zahra","Zaiden","Zain","Zaine","Zaira","Zak",
				"Zali","Zander","Zane","Zara","Zaria","Zayden","Zayn","Zayne","Zeb","Zebulon",
				"Zed","Zeke","Zelda","Zelida","Zelina","Zendaya","Zia","Ziggy","Zina","Zion",
				"Ziva","Zoe","Zoey","Zola","Zoltan","Zora","Zoya","Zula","Zuri","Zuriel",
				"Zyana", "Zylen"
			}; 
		
		int randomIndex = (int) (Math.random() * (allnames.length - 1));
		return allnames[randomIndex];
	}

}
