package uk.codingbadgers.bcreative;

import java.io.File;
import java.io.IOException;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.block.Action;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bcreative.containers.GamemodeWorld;

public class ConfigManager {

	private final File m_worldsFolder = new File(bCreative.getInstance().getDataFolder() + File.separator + "worlds" + File.separator);
	private FileConfiguration m_config = null;
	private FileConfiguration m_monitorConfig = null;
	
	public void loadConfig() {
		try {			
			// standard conifg
			m_config = bCreative.getInstance().getConfig();
												
			m_config.options().copyDefaults(true);
			bCreative.getInstance().saveConfig();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			// monitor config
			File monitorFile = new File(bCreative.getInstance().getDataFolder() + File.separator + "monitor.yml");
			
			if (!monitorFile.exists())
				monitorFile.createNewFile();
			
			m_monitorConfig = YamlConfiguration.loadConfiguration(monitorFile);
			
			m_monitorConfig.addDefault("monitor.rightClick.air", true);
			m_monitorConfig.addDefault("monitor.rightClick.block", true);
			m_monitorConfig.addDefault("monitor.leftClick.air", true);
			m_monitorConfig.addDefault("monitor.leftClick.block", true);
			m_monitorConfig.addDefault("monitor.physical", true);
			
			m_monitorConfig.options().copyDefaults(true);
			m_monitorConfig.save(monitorFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	
		// worlds config
		for (World world : bFundamentals.getInstance().getServer().getWorlds()) {
			File folder = new File(m_worldsFolder + File.separator + world.getName() + File.separator);
			
			if (!folder.exists())
				folder.mkdirs();
			
			GamemodeWorld gmWorld = new GamemodeWorld(world, folder);
			bCreative.getWorldManager().getWorlds().add(gmWorld);
		}
	}
	
	public File getWorldsFolder() {
		return m_worldsFolder;
	}
	
	public boolean isMonitor(GamemodeWorld world, Action action) {
		return world.isMonitored(action);
	}

	public boolean isRedstoneMonitored() {
		// TODO Auto-generated method stub
		return false;
	}
}
