package uk.codingbadgers.bhuman;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.World;

import uk.codingbadgers.bFundamentals.bFundamentals;

import com.topcat.npclib.entity.HumanNPC;
import com.topcat.npclib.entity.NPC;

import n3wton.me.BukkitDatabaseManager.Database.BukkitDatabase;

public class DatabaseManager {

	private BukkitDatabase m_database = null;
	
	public DatabaseManager() {
		m_database = bFundamentals.getBukkitDatabase();
	}
	
	public void loadDatabase() throws SQLException{
		if (m_database == null)
			return;
		
		if (!m_database.TableExists("bhuman_npc")) {
			createTables();
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
	
	public void createTables() {
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
	
	public void save(List<NPC> npcs) {
		String query = "DELETE FROM bhuman_npc";
		m_database.Query(query, true);
		
		for (NPC npc : npcs) {
			if (!(npc instanceof HumanNPC)) 
				continue;
			
			HumanNPC hNPC = (HumanNPC)npc;
			
			Location loc = hNPC.getBukkitEntity().getLocation();
			String name = hNPC.getName();
			
			query = "INSERT INTO bhuman_npc " +
							"(npc_name, npc_loc_x, npc_loc_y, npc_loc_z, npc_loc_pitch, npc_loc_yaw, npc_loc_world)" +
							"VALUES ('" + name + "', " +
									"'" + loc.getX() + "', " +
									"'" + loc.getY() + "', " +
									"'" + loc.getZ() + "', " +
									"'" + loc.getPitch() + "', " +
									"'" + loc.getYaw() + "', " +
									"'" + loc.getWorld().getName() + "');";
			m_database.Query(query, true);
			bHuman.MODULE.debugConsole("Removed npc '" + name + "'");
		}
		bHuman.MODULE.log(Level.INFO, "Removed " + npcs.size() + " npc's");
	}
	
	public void save(NPC npc) {
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
	
	public void remove(NPC npc) {
		if (!(npc instanceof HumanNPC))
			return;
		
		String query = "DELETE FROM bhuman_npc WHERE npc_name='" + ((HumanNPC)npc).getName() + "'";
		m_database.Query(query);
		bHuman.MODULE.debugConsole("Removed npc '" + ((HumanNPC)npc).getName() + "'");
	}
}
