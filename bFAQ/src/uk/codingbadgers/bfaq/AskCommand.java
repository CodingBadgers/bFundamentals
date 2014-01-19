package uk.codingbadgers.bfaq;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.commands.ModuleCommand;
import uk.codingbadgers.bFundamentals.message.ClickEventType;
import uk.codingbadgers.bFundamentals.message.HoverEventType;
import uk.codingbadgers.bFundamentals.message.Message;
import uk.codingbadgers.bFundamentals.player.FundamentalPlayer;

public class AskCommand extends ModuleCommand {

	public AskCommand() {
		super("ask", "/ask <question>");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("bfundamentals.faq.ask") || !(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "I'm sorry you cannot use this command");
			return true;
		}
		
		if (args.length < 1) {
			sender.sendMessage(getUsage());
			return true;
		}
		
		String url = "http://mcbadgercraft.freshdesk.com/support/search/solutions?term=" + StringUtils.join(args, "+");
		
		FundamentalPlayer player = bFundamentals.Players.getPlayer((Player) sender);
		
		Message message = new Message("To view your search results click ");
		message.setColor(ChatColor.GREEN);
		
		Message link = new Message("here");
		link.addStyle(ChatColor.BOLD);
		link.addHoverEvent(HoverEventType.SHOW_TOOLTIP, url);
		link.addClickEvent(ClickEventType.OPEN_URL, url);
		
		message.addExtra(link);
		
		player.sendMessage(message);
		return true;
	}

}
