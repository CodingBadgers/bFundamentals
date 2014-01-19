package uk.thecodingbadgers.bFundamentals.messaging;

import org.bukkit.ChatColor;
import org.junit.Test;

import uk.codingbadgers.bFundamentals.message.Message;

import static org.junit.Assert.*;

public class MessagingTest {

	@Test(expected = IllegalArgumentException.class)
	public void testMessageColor() {
		Message message = new Message("Hello world");
		message.setColor(ChatColor.BOLD);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testMessageStyle1() {
		Message message = new Message("Hello world");
		message.addStyle(ChatColor.GREEN);
	}

	@Test
	public void testMessageStyle2() {
		Message message = new Message("Hello world");
		message.addStyle(ChatColor.BOLD);
		message.addStyle(ChatColor.UNDERLINE);
		message.addStyle(ChatColor.STRIKETHROUGH);
		message.addStyle(ChatColor.ITALIC);
		message.addStyle(ChatColor.MAGIC);
		
		assertTrue(message.hasStyle(ChatColor.BOLD));
		assertTrue(message.hasStyle(ChatColor.UNDERLINE));
		assertTrue(message.hasStyle(ChatColor.STRIKETHROUGH));
		assertTrue(message.hasStyle(ChatColor.ITALIC));
		assertTrue(message.hasStyle(ChatColor.MAGIC));
	}

	@Test
	public void testMessageReset() {
		Message message = new Message("Hello world");
		message.addStyle(ChatColor.BOLD);
		message.addStyle(ChatColor.UNDERLINE);
		message.addStyle(ChatColor.STRIKETHROUGH);
		message.addStyle(ChatColor.ITALIC);
		message.addStyle(ChatColor.MAGIC);
		message.setColor(ChatColor.GOLD);
		
		message.addStyle(ChatColor.RESET);

		assertTrue(!message.hasStyle(ChatColor.BOLD));
		assertTrue(!message.hasStyle(ChatColor.UNDERLINE));
		assertTrue(!message.hasStyle(ChatColor.STRIKETHROUGH));
		assertTrue(!message.hasStyle(ChatColor.ITALIC));
		assertTrue(!message.hasStyle(ChatColor.MAGIC));
		assertTrue(message.getColor() == ChatColor.WHITE);
	}
	
}
