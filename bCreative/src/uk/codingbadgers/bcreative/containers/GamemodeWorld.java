package uk.codingbadgers.bcreative.containers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.block.Action;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bcreative.GameModeHelper;
import uk.codingbadgers.bcreative.bCreative;

public class GamemodeWorld {

	private World m_world = null;
	
	private File m_folder = null;
	private File m_configFile = null;

	private FileConfiguration m_config = null;
	private File m_blacklistFolder = null;
	private File m_breakBlacklistFile = null;
	private File m_placeBlacklistFile = null;
	private File m_interactBlacklistFile = null;
	
	private GameMode m_defaultGamemode = GameMode.SURVIVAL;
	private GameMode m_monitorGamemode = GameMode.CREATIVE;
	private boolean m_interactMonitor = true;
	private boolean m_monitored = true;
	private boolean m_survival = true;
	private boolean m_creative = true;
	private boolean m_adventure = true;
	
	private List<Material> m_breakBlacklist = new ArrayList<Material>();
	private List<Material> m_placeBlacklist = new ArrayList<Material>();
	private List<Material> m_interactBlacklist = new ArrayList<Material>();
	
	public GamemodeWorld(World world, File folder) {
		m_world = world;
		m_folder = folder;
		
		m_configFile = new File(folder + File.separator + "config.yml");
		m_blacklistFolder = new File(folder + File.separator + "blacklist" + File.separator);
		m_breakBlacklistFile = new File(m_blacklistFolder + File.separator + "break.cfg");
		m_placeBlacklistFile = new File(m_blacklistFolder + File.separator + "place.cfg");
		m_interactBlacklistFile = new File(m_blacklistFolder + File.separator + "interact.cfg");
		
		try {
			loadConfig();
			loadBlacklist();
		} catch (IOException ex) {
			bFundamentals.getInstance().getLogger().log(Level.INFO, null, ex);
		}
	}
	
	public World getWorld() {
		return m_world;
	}
	
	public File getConfigFolder() {
		return m_folder;
	}
	
	private void loadConfig() throws IOException{
		if (!m_configFile.exists()) {
			m_configFile.createNewFile();
			
			m_config = YamlConfiguration.loadConfiguration(m_configFile);
			
			m_config.addDefault("gamemode.default", "survival");
			m_config.addDefault("gamemode.status.survival", true);
			m_config.addDefault("gamemode.status.creative", true);
			m_config.addDefault("gamemode.status.adventure", true);
			m_config.addDefault("gamemode.monitor.enabled", true);
			m_config.addDefault("gamemode.monitor.gamemode", "creative");
			m_config.addDefault("gamemode.monitor.interact", true);
			m_config.addDefault("gamemode.monitor.rightClick.air", true);
			m_config.addDefault("gamemode.monitor.rightClick.block", true);
			m_config.addDefault("gamemode.monitor.leftClick.air", true);
			m_config.addDefault("gamemode.monitor.leftClick.block", true);
			m_config.addDefault("gamemode.monitor.physical", true);
			
			m_config.options().copyDefaults(true);
			m_config.save(m_configFile);
		}
		
		if (m_config == null)
			m_config = YamlConfiguration.loadConfiguration(m_configFile);
		
		m_defaultGamemode = GameModeHelper.getGamemode(m_config.getString("gamemode.default", "survival"));
		m_monitorGamemode = GameModeHelper.getGamemode(m_config.getString("gamemode.monitor.gamemode", "creative"));
		m_interactMonitor = m_config.getBoolean("gamemode.monitor.interact", true);
		m_monitored = m_config.getBoolean("gamemode.monitor.enabled", true);
		m_survival = m_config.getBoolean("gamemode.status.survival", true);
		m_creative = m_config.getBoolean("gamemode.status.creative", true);
		m_adventure = m_config.getBoolean("gamemode.status.adventure", true);
	}
	
	private void loadBlacklist() throws IOException{
		if (!m_blacklistFolder.exists())
			m_blacklistFolder.mkdirs();
		
		m_placeBlacklist = loadBlacklist(m_breakBlacklistFile);
		m_breakBlacklist = loadBlacklist(m_placeBlacklistFile);
		m_interactBlacklist = loadBlacklist(m_interactBlacklistFile);
	}
	
	/**
	 * Load a specific blacklist from file
	 * 
	 * @param file the blacklist file
	 * @return the blacklist
	 * @throws IOException
	 */
	private List<Material> loadBlacklist(File file) throws IOException{
		List<Material> blacklist = new ArrayList<Material>();
		// place blacklist
		BufferedReader reader;
		int lineNumber;
		String line;
		
		if (!m_placeBlacklistFile.exists()) {
			m_placeBlacklistFile.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
					
			writer.write("# This the " + file.getName().substring(0, file.getName().indexOf('.')) + " blacklist file for world " + m_world.getName() + "\n");
			writer.write("# Please either use id's or full bukkit Material names (Case insensitive)\n");
			writer.write("# Bukkit material names can be found at \"https://github.com/Bukkit/Bukkit/blob/master/src/main/java/org/bukkit/Material.java\"");
			writer.write(Material.BEDROCK.toString().toLowerCase() + "\n");
					
			writer.flush();
			writer.close();
		}
				
		reader = new BufferedReader(new FileReader(file));
				
		line = "";
		lineNumber = 0;
				
		while((line = reader.readLine()) != null) {
			lineNumber++;
					
			if (line.startsWith("#"))
				continue;
					
			if (line.length() == 0)
				continue;
					
			Material material = null;
					
			try {
				int id;
				id = Integer.parseInt(line);
				material = Material.getMaterial(id);
			} catch (NumberFormatException ex) {
				material = Material.getMaterial(line.toUpperCase());
			}
					
			if (material == null) {
				bCreative.getInstance().debugConsole("Error parsing " + line + " on line " + lineNumber + " in the place Blacklist for " + m_world.getName());
				continue;
			}
					
			blacklist.add(material);
		}
				
		reader.close();
		return blacklist;
	}
	
	public GameMode getDefaultGamemode() {
		return m_defaultGamemode;
	}
	
	public GameMode getMonitoredGM() {
		return m_monitorGamemode;
	}
	
	public boolean isMonitored() {
		return m_monitored;
	}
	
	public boolean isSurvivalEnabled() {
		return m_survival;
	}
	
	public boolean isCreativeEnabled() {
		return m_creative;
	}
	
	public boolean isAdventureEnabled() {
		return m_adventure;
	}
	
	public boolean isGamemodeEnabled(GameMode gamemode) {
		if (gamemode.equals(GameMode.SURVIVAL) && m_survival)
			return true;
		if (gamemode.equals(GameMode.CREATIVE) && m_creative)
			return true;
		if (gamemode.equals(GameMode.ADVENTURE) && m_adventure)
			return true;
		return false;
	}
	
	public void addBreakBlacklistedBlock(Material mat) throws IOException {
		m_breakBlacklist.add(mat);

		addBlacklistToFile(m_breakBlacklistFile, mat);
	}
	
	public void addPlaceBlacklistedBlock(Material mat) throws IOException {
		m_placeBlacklist.add(mat);
		
		addBlacklistToFile(m_placeBlacklistFile, mat);
	}
	
	public void addInteractBlacklistedBlock(Material mat) throws IOException {
		m_interactBlacklist.add(mat);
		
		addBlacklistToFile(m_interactBlacklistFile, mat);
	}
	
	private void addBlacklistToFile(File file, Material mat) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
		writer.write(mat.toString().toLowerCase() + "\n");
		writer.flush();
		writer.close();
	}
	
	public boolean isBreakBlackListed(Block block) {
		return m_breakBlacklist.contains(block.getType());
	}

	public boolean isPlaceBlackListed(Block block) {
		return m_placeBlacklist.contains(block.getType());
	}
	
	public boolean isMonitored(Action action) {
		if (!this.m_monitored)
			return false;
		
		switch (action) {
			case RIGHT_CLICK_AIR:
				return m_config.getBoolean("gamemode.monitor.rightClick.air");
			case RIGHT_CLICK_BLOCK:
				return m_config.getBoolean("gamemode.monitor.rightClick.block");
			case LEFT_CLICK_AIR:
				return m_config.getBoolean("gamemode.monitor.leftClick.air");
			case LEFT_CLICK_BLOCK:
				return m_config.getBoolean("gamemode.monitor.leftClick.block");
			case PHYSICAL:
				return m_config.getBoolean("gamemode.monitor.physical");
			default:
				return false;
		}
	}
	
	public boolean isInteractMonitored() {
		return m_interactMonitor;
	}
	
	public boolean isMonitored(Material material) {
		if (!this.m_monitored || !this.m_interactMonitor)
			return false;
		
		return m_interactBlacklist.contains(material);
	}
}
