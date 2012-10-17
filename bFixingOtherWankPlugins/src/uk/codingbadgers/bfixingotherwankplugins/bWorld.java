package uk.codingbadgers.bfixingotherwankplugins;

import java.util.List;
import uk.codingbadgers.bfixingotherwankplugins.bFixingOtherWankPlugins;

import org.bukkit.Server;
import org.bukkit.entity.Player;

public class bWorld {

	private List<String> m_onEnterWorldConsole = null;
	
	private List<String> m_onEnterWorldPlayer = null;
	
	private String m_name = null;
	
	public bWorld(String name) {
		m_name = name;
	}
	
	public String getName() {
		return m_name;
	}
	
	public void setOnEnterWorldCommands(List<String> consoleCommands, List<String> playerCommands) {
		m_onEnterWorldConsole = consoleCommands;
		m_onEnterWorldPlayer = playerCommands;
	}

	public void executeOnEnter(Player player) {
		
		final Server server = bFixingOtherWankPlugins.PLUGIN.getServer();
		
		for (String command : m_onEnterWorldConsole) {
			server.dispatchCommand(server.getConsoleSender(), command);
		}
		
		for (String command : m_onEnterWorldPlayer) {
			player.performCommand(command);
		}
		
	}
	
}