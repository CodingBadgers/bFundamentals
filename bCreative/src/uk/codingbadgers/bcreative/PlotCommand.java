package uk.codingbadgers.bcreative;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

public class PlotCommand extends ModuleCommand {

	private bCreative m_module = null;
	
	/**
	 * Command constructor.
	 */
	public PlotCommand(bCreative instance) {
		super("plot", "plot");
		m_module = instance;
	}
	
	/**
	 * Called when the 'plot' command is executed.
	 */
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		
		if (!(sender instanceof Player))
			return true;
		
		final Player player = (Player) sender;

		WorldGuardPlugin worldguard = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
		if (worldguard == null)
			return true;
		
		final String lookupName = args.length == 1 ? args[0] : player.getName();
		
		for (String worldName : m_module.getActiveWorlds()) {
			
			World world = Bukkit.getWorld(worldName);
			RegionManager regionManager = worldguard.getRegionManager(world);
			
			for (ProtectedRegion region : regionManager.getRegions().values()) {
				if (region.getOwners().contains(lookupName)) {
					
					Vector center = region.getMinimumPoint().add(region.getMaximumPoint()).divide(2.0f);
					Location location = new Location(world, center.getX(), center.getY(), center.getZ());
					location.setY(location.getWorld().getHighestBlockYAt(location) + 1);
					
					player.teleport(location);
					Module.sendMessage(m_module.getName(), player, "You have been teleported to " + lookupName + "'s plot...");
					
					return true;
				}
			}
			
			Module.sendMessage(m_module.getName(), player, "We couldn't find a plot for " + lookupName + ".");
		}
		
		return true;
	}
	
}
