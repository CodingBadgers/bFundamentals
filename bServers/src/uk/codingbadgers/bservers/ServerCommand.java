package uk.codingbadgers.bservers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.module.Module;

public class ServerCommand extends ModuleCommand {

    private String server;

    public ServerCommand(String server) {
        super(server, "/" + server);
        this.server = server;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Module.sendMessage("bServers", sender, "This command can only be executed as a player");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("bServers.server." + server)) {
            Module.sendMessage("bServers", sender, "You do not have permission to go to " + server);
            return true;
        }
        
        bFundamentals.getBungeeMessenger().connect(sender.getName(), this.getLabel());
        return true;
    }
}
