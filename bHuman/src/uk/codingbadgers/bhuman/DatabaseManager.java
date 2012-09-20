package uk.codingbadgers.bhuman;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.World;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase;

import com.topcat.npclib.entity.HumanNPC;
import com.topcat.npclib.entity.NPC;

public class DatabaseManager {

	private BukkitDatabase m_database = null;
	
	public DatabaseManager() {
		m_database = bFundamentals.getBukkitDatabase();
	}
	
	public void loadDatabase() throws SQLException{
		if (m_database == null)
			return;
		
		loadNpcs();
		loadChatMessages();
		loadHealthTable();
	}
	
	private void loadNpcs() throws SQLException{
		if (!m_database.TableExists("bhuman_npc")) {
			createNpcTable();
		}
		
		String query = "SELECT * FROM bhuman_npc";
		ResultSet result = m_database.QueryResult(query);
		
		while(result.next()) {
			String name = result.getString("npc_name");
			double x = result.getDouble("npc_loc_x");
			double y = result.getDouble("npc_loc_y");
			double z = result.getDouble("npc_loc_z");
			float pitch = result.getFloat("npc_loc_pitch");
			float yaw = result.getFloat("npc_loc_yaw");
			World world = bFundamentals.getInstance().getServer().getWorld(result.getString("npc_loc_world"));
			Location loc = new Location(world, x, y, z, pitch, yaw);
			
			bHuman.getNPCManager().spawnHumanNPC(name, loc);
			bHuman.MODULE.debugConsole("Loaded npc '" + name + "' at location 'x:" + Math.round(x) + "', 'y:" + Math.round(y) + "', 'z:" + Math.round(z) + "'");
		}
		
		bHuman.MODULE.log(Level.INFO, "Loaded " + bHuman.getNPCManager().getNPCs().size() + " Npc's");	
	}
	
	private void loadChatMessages() throws SQLException{
		if (!m_database.TableExists("bhuman_chatMessage")) {
			createChatTable();
		}
		
		String query = "SELECT * FROM bhuman_chatMessage";
		ResultSet result = m_database.QueryResult(query);
		
		while(result.next()) {
			String name = result.getString("npc_name");
			String message = result.getString("npc_message");
			
			HumanNPC npc = (HumanNPC)bHuman.getNPCManager().getHumanNPCByName(name).get(0);

			if (npc == null)
				continue;
			
			npc.setMessage(message);
			bHuman.MODULE.debugConsole("Loaded chat message for '" + name + "'");
		}
	}
	
	private void loadHealthTable() throws SQLException{
		if (!m_database.TableExists("bhuman_health")) {
			createHealthTable();
		}
		
		String query = "SELECT * FROM bhuman_health";
		ResultSet result = m_database.QueryResult(query);
		
		while(result.next()) {
			String name = result.getString("npc_name");
			int health = result.getInt("npc_health");
			boolean invincible = result.getInt("npc_invincible") == 1;
			
			HumanNPC npc = (HumanNPC)bHuman.getNPCManager().getHumanNPCByName(name).get(0);
			
			if (npc == null)
				continue;
			
			npc.setHealth(health);
			npc.setInvincible(invincible);
			bHuman.MODULE.debugConsole("Loaded health for '" + name + "'");
		}
	}
	
	public void createNpcTable() {
		bHuman.MODULE.debugConsole("Creating table bHuman_npc");
		
		String query = "CREATE TABLE bhuman_npc (" +
						"npc_name VARCHAR(16)," +
						"npc_loc_x DOUBLE, " +
						"npc_loc_y DOUBLE, " +
						"npc_loc_z DOUBLE, " +
						"npc_loc_pitch FLOAT, " +
						"npc_loc_yaw FLOAT, " +
						"npc_loc_world VARCHAR(32));";
		m_database.Query(query, true);
	}
	
	public void createChatTable() {
		bHuman.MODULE.debugConsole("Creating table bhuman_chatMessage");
		
		String query = "CREATE TABLE bhuman_chatMessage (" +
						"npc_name VARCHAR(16)," +
						"npc_message VARCHAR(256));";
		m_database.Query(query, true);
	}
	
	public void createHealthTable() {
		bHuman.MODULE.debugConsole("Creating table bhuman_health");
		
		String query = "CREATE TABLE bhuman_health (" +
						"npc_name VARCHAR(16)," +
						"npc_health TINYINT" +
						"npc_invincible TINYINT);";
		m_database.Query(query, true);
	}
	
	public void save(List<NPC> npcs) {
		// empty both tables
		String query = "DELETE FROM bhuman_npc";
		m_database.Query(query, true);
		
		query = "DELETE FROM bhuamn_chatMessage";
		m_database.Query(query, true);
		
		// save the npcs we have
		for (NPC npc : npcs) {
			saveNPC(npc);
			addChatMessage(npc);
			updateHealth(npc);
		}
		bHuman.MODULE.log(Level.INFO, "Saved " + npcs.size() + " npc's");
	}
	
	public void saveNPC(NPC npc) {
		if (!(npc instanceof HumanNPC)) 
			return;
		
		HumanNPC hNPC = (HumanNPC)npc;
		
		Location loc = hNPC.getBukkitEntity().getLocation();
		String name = hNPC.getName();
		
		String query = "INSERT INTO bhuman_npc " +
						"(npc_name, npc_loc_x, npc_loc_y, npc_loc_z, npc_loc_pitch, npc_loc_yaw, npc_loc_world)" +
						"VALUES ('" + name + "', " +
								"'" + loc.getX() + "', " +
								"'" + loc.getY() + "', " +
								"'" + loc.getZ() + "', " +
								"'" + loc.getPitch() + "', " +
								"'" + loc.getYaw() + "', " +
								"'" + loc.getWorld().getName() + "');";
		m_database.Query(query);
		bHuman.MODULE.debugConsole("Saved npc '" + name + "'");
	}
	
	public void addChatMessage(NPC bnpc) {
		if (!(bnpc instanceof HumanNPC)) 
			return;
		
		HumanNPC npc = (HumanNPC)bnpc;
		
		String name = npc.getName();
		String message = npc.getMessage();
		
		String delete = "DELETE FROM bhuamn_chatMessage WHERE name='" + name + "'";
		m_database.Query(delete);
		
		if (npc.canTalk()) {
			String insert = "INSERT INTO bhuamn_chatMessage (npc_name, npc_message)" +
								"VALUES ('" + name + "', " +
										"'" + message + "');";
			m_database.Query(insert, true);
			bHuman.MODULE.debugConsole("Added chat message for '" + name + "'");
		}
	}
	
	public void removeNPC(NPC npc) {
		if (!(npc instanceof HumanNPC))
			return;
		
		String query = "DELETE FROM bhuman_npc WHERE npc_name='" + ((HumanNPC)npc).getName() + "'";
		m_database.Query(query);
		bHuman.MODULE.debugConsole("Removed npc '" + ((HumanNPC)npc).getName() + "'");
	}

	public void updateHealth(NPC npc) {
		if (!(npc instanceof HumanNPC))
			return;
		
		String delete = "DELETE FROM bhuman_health WHERE npc_name='" + ((HumanNPC)npc).getName() + "';";
		m_database.Query(delete);
		
		String query = "INSERT INTO bhuman_health (npc_name, npc_health, npc_invincible) " +
							"VALUES ('" + ((HumanNPC)npc).getName() + "', " +
									"'" + ((HumanNPC)npc).getHealth() + "', " +
									"'" + (((HumanNPC)npc).canDie() ? 1 : 0) + "');";
		m_database.Query(query);
	}
}
