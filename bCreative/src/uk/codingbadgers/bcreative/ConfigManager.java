package uk.codingbadgers.bcreative;

import java.io.File;
import org.bukkit.World;
import org.bukkit.event.block.Action;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bcreative.containers.GamemodeWorld;

public class ConfigManager {

	private final File m_worldsFolder = new File(bCreative.getInstance().getDataFolder() + File.separator + "worlds" + File.separator);
	
	public void loadConfig() {
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
}
