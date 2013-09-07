package uk.codingbadgers.banimalcare;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

public class AnimalCommand extends ModuleCommand {
	
	final private bAnimalCare m_module;

	public AnimalCommand(Module module) {
		super("pet", "/pet list | /pet release <id>");
		m_module = (bAnimalCare) module;
	}

	/**
	 * Called when the 'pet' command is executed.
	 */
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		
		if (!(sender instanceof Player))
			return true;
		
		Player player = (Player)sender;
		
		if (args.length == 0 || args.length > 2) {
			// show command help
			return true;
		}
		
		if (args[0].equalsIgnoreCase("list") && args.length == 1) {
			m_module.listPets(player);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("release") && args.length == 2) {
			
			return true;
		}
		
		return true;		
	}
	
}
