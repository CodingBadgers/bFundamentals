package uk.thecodingbadgers.bFundamentals.messaging;

import java.util.Arrays;

import org.bukkit.Achievement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.codingbadgers.bFundamentals.message.Message;
import uk.thecodingbadgers.bFundamentals.TestContainer;

import static org.junit.Assert.*;

public class SerializationTest extends TestContainer {

	private static Gson GSON = null;
	
	private static final String JSON_MESSAGE_1 = "{\"text\":\"Hello world\",\"color\":\"gold\",\"bold\":true}";
	
	private static final String JSON_ITEMSTACK_1 = "{\"id\":260}";
	private static final String JSON_ITEMSTACK_2 = "{\"id\":260,\"Count\":5}";
	private static final String JSON_ITEMSTACK_3 = "{\"id\":260,\"Count\":5,\"Damage\":7}";
	private static final String JSON_ITEMSTACK_4 = "{\"id\":260,\"Count\":5,\"Damage\":7,\"tag\":{\"Name\":\"Apple of Awsome\",\"Lore\":[\"Its just a apple\"]}}";
	private static final String JSON_ITEMSTACK_5 = "{\"id\":260,\"Count\":5,\"Damage\":7,\"tag\":{\"ench\":[{\"id\":0,\"lvl\":1}]}}";
	
	@Before
	public void setupTest() {
		if (GSON == null) {
			GSON = bFundamentals.getGsonInstance();
		}
	}
	
	@Test
	public void testMessageSerialize() {
		Message message = new Message("Hello world");
		message.setColor(ChatColor.GOLD);
		message.addStyle(ChatColor.BOLD);
		
		assertEquals(JSON_MESSAGE_1, GSON.toJson(message));
	}

	@Test
	public void testMessageDeserialize() {
		Message expected = new Message("Hello world");
		expected.setColor(ChatColor.GOLD);
		expected.addStyle(ChatColor.BOLD);
		
		assertEquals(expected, GSON.fromJson(JSON_MESSAGE_1, Message.class));
	}
	
	@Test
	public void testAchievementSerialize() {
		assertEquals("\"achievement.buildWorkBench\"", GSON.toJson(Achievement.BUILD_WORKBENCH));
		assertEquals("\"achievement.bakeCake\"", GSON.toJson(Achievement.BAKE_CAKE));
	}

	@Test
	public void testAchievementDeserialize() {
		assertEquals(Achievement.BUILD_WORKBENCH, GSON.fromJson("\"achievement.buildWorkBench\"", Achievement.class));
		assertEquals(Achievement.BAKE_CAKE,  GSON.fromJson("\"achievement.bakeCake\"", Achievement.class));
	}
	
	@Test
	public void testItemStackSerialize1() {
		ItemStack stack = new ItemStack(Material.APPLE);
		assertEquals(JSON_ITEMSTACK_1, GSON.toJson(stack));
	}

	@Test
	public void testItemStackSerialize2() {
		ItemStack stack = new ItemStack(Material.APPLE, 5);
		assertEquals(JSON_ITEMSTACK_2, GSON.toJson(stack));
	}

	@Test
	public void testItemStackSerialize3() {
		ItemStack stack = new ItemStack(Material.APPLE, 5, (short) 7);
		assertEquals(JSON_ITEMSTACK_3, GSON.toJson(stack));
	}

	@Test
	public void testItemStackSerialize4() {
		ItemStack stack = new ItemStack(Material.APPLE, 5, (short) 7);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("Apple of Awsome");
		meta.setLore(Arrays.asList("Its just a apple"));
		stack.setItemMeta(meta);
		assertEquals(JSON_ITEMSTACK_4, GSON.toJson(stack));
	}

	@Test
	public void testItemStackSerialize5() {
		ItemStack stack = new ItemStack(Material.APPLE, 5, (short) 7);
		ItemMeta meta = stack.getItemMeta();
		meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		stack.setItemMeta(meta);
		assertEquals(JSON_ITEMSTACK_5, GSON.toJson(stack));
	}

	@Test
	public void testItemStackDeserialize1() {
		ItemStack stack = new ItemStack(Material.APPLE);
		assertEquals(stack, GSON.fromJson(JSON_ITEMSTACK_1, ItemStack.class));
	}

	@Test
	public void testItemStackDeserialize2() {
		ItemStack stack = new ItemStack(Material.APPLE, 5);
		assertEquals(stack, GSON.fromJson(JSON_ITEMSTACK_2, ItemStack.class));
	}

	@Test
	public void testItemStackDeserialize3() {
		ItemStack stack = new ItemStack(Material.APPLE, 5, (short) 7);
		assertEquals(stack, GSON.fromJson(JSON_ITEMSTACK_3, ItemStack.class));
	}

	@Test
	public void testItemStackDeserialize4() {
		ItemStack stack = new ItemStack(Material.APPLE, 5, (short) 7);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("Apple of Awsome");
		meta.setLore(Arrays.asList("Its just a apple"));
		stack.setItemMeta(meta);
		assertEquals(stack, GSON.fromJson(JSON_ITEMSTACK_4, ItemStack.class));
	}

	@Test
	public void testItemStackDeserialize5() {
		ItemStack stack = new ItemStack(Material.APPLE, 5, (short) 7);
		ItemMeta meta = stack.getItemMeta();
		meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		stack.setItemMeta(meta);
		assertEquals(stack, GSON.fromJson(JSON_ITEMSTACK_5, ItemStack.class));
	}

}
