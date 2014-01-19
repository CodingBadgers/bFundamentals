package uk.codingbadgers.bFundamentals.serialization;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@SuppressWarnings("deprecation")
public class ItemStackSerializer implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

	@Override
	public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		if (!json.isJsonObject()) {
			return new ItemStack(Material.AIR);
		}
		
		JsonObject item = json.getAsJsonObject();
		
		ItemStack stack = new ItemStack(item.get("id").getAsInt());
		
		if (item.has("Damage") && item.get("Damage").isJsonPrimitive()) { stack.setDurability(item.get("Damage").getAsShort()); }
		if (item.has("Count") && item.get("Count").isJsonPrimitive()) { stack.setAmount(item.get("Count").getAsInt()); }
		
		if (item.has("tag") && item.get("tag").isJsonObject()) {
			JsonObject tag = item.get("tag").getAsJsonObject();
			
			ItemMeta meta = Bukkit.getItemFactory().getItemMeta(stack.getType());
			if (tag.has("Name") && tag.get("Name").isJsonPrimitive()) { meta.setDisplayName(tag.get("Name").getAsString()); }
			
			if (tag.has("Lore") && tag.get("Lore").isJsonArray()) {
				List<String> lore = new ArrayList<String>();
				for (JsonElement element : tag.get("Lore").getAsJsonArray()) {
					lore.add(element.getAsString());
				}
				meta.setLore(lore);
			}
			
			if (tag.has("ench") && tag.get("ench").isJsonArray()) {
				for (JsonElement element : tag.get("ench").getAsJsonArray()) {
					JsonObject ench = (JsonObject) element;
					meta.addEnchant(Enchantment.getById(ench.get("id").getAsInt()), ench.get("lvl").getAsInt(), true);
				}
			}
			
			if (meta instanceof LeatherArmorMeta) {
				LeatherArmorMeta laMeta = (LeatherArmorMeta) meta;
				
				if (tag.has("color") && tag.get("color").isJsonPrimitive()) {
					laMeta.setColor(Color.fromRGB(tag.get("color").getAsInt()));
				}
			}
			if (meta instanceof MapMeta) {
				MapMeta mMeta = (MapMeta) meta;
			
				if (tag.has("map_is_scaling") && tag.get("map_is_scaling").isJsonPrimitive()) {
					mMeta.setScaling(tag.get("map_is_scaling").getAsInt() == 1);
				}
			}
			if (meta instanceof PotionMeta) {
				PotionMeta pMeta = (PotionMeta) meta;
				
				for (JsonElement element : tag.get("CustomPotionEffects").getAsJsonArray()) {
					JsonObject object = (JsonObject) element;
					pMeta.addCustomEffect(new PotionEffect(PotionEffectType.getById(object.get("Id").getAsInt()),
															object.get("Amplifier").getAsInt(),
															object.get("Duration").getAsInt(),
															object.get("Ambient").getAsInt() == 1),
											true);
				}
			}
			if (meta instanceof SkullMeta) {
				SkullMeta sMeta = (SkullMeta) meta;
				
				if (tag.has("SkullOwner") && tag.get("SkullOwner").isJsonPrimitive()) {
					sMeta.setOwner(tag.get("SkullOwner").getAsString());
				}
			}
			if (meta instanceof EnchantmentStorageMeta) {
				EnchantmentStorageMeta esMeta = (EnchantmentStorageMeta) meta;
				
				if (tag.has("StoredEnchantments") && tag.get("StoredEnchantments").isJsonArray()) {
					for (JsonElement element : tag.get("StoredEnchantments").getAsJsonArray()) {
						JsonObject ench = (JsonObject) element;
						esMeta.addStoredEnchant(Enchantment.getById(ench.get("id").getAsInt()), ench.get("lvl").getAsInt(), true);
					}
				}
			}
			if (meta instanceof BookMeta) {
				BookMeta bMeta = (BookMeta) meta;
				
				if (tag.has("title") && tag.get("title").isJsonPrimitive()) {
					bMeta.setTitle(tag.get("title").getAsString());
				}
				if (tag.has("author") && tag.get("author").isJsonPrimitive()) {
					bMeta.setAuthor(tag.get("author").getAsString());
				}
				if (tag.has("pages") && tag.get("pages").isJsonArray()) {
					List<String> lore = new ArrayList<String>();
					for (JsonElement element : tag.get("pages").getAsJsonArray()) {
						lore.add(element.getAsString());
					}
					bMeta.setPages(lore);
				}
			}
			if (meta instanceof FireworkEffectMeta) {
				FireworkEffectMeta feMeta = (FireworkEffectMeta) meta;

				if (tag.has("Explosion") && tag.get("Explosion").isJsonObject()) {
					JsonObject explosion = tag.get("Explosion").getAsJsonObject();
					FireworkEffect effect = FireworkEffect.builder().
												flicker(explosion.get("Flicker").getAsBoolean()).
												trail(explosion.get("Trail").getAsBoolean()).
												with(getType(explosion.get("Type").getAsInt())).
												withColor(colorFromJson(explosion.get("Colors").getAsJsonArray())).
												withFade(colorFromJson(explosion.get("FadeColors").getAsJsonArray())).
												build();
					
					feMeta.setEffect(effect);
				}
			}
			if (meta instanceof FireworkMeta) {
				FireworkMeta fMeta = (FireworkMeta) meta;
				
				if (tag.has("Fireworks") && tag.get("Fireworks").getAsBoolean()) {
					List<FireworkEffect> effects = new ArrayList<FireworkEffect>();
					
					for (JsonElement element : tag.get("Fireworks").getAsJsonArray()) {
						JsonObject explosion = element.getAsJsonObject();
						FireworkEffect effect = FireworkEffect.builder().
													flicker(explosion.get("Flicker").getAsBoolean()).
													trail(explosion.get("Trail").getAsBoolean()).
													with(getType(explosion.get("Type").getAsInt())).
													withColor(colorFromJson(explosion.get("Colors").getAsJsonArray())).
													withFade(colorFromJson(explosion.get("FadeColors").getAsJsonArray())).
													build();
						effects.add(effect);
					}
					fMeta.addEffects(effects);
					
				}
			}
			if (meta instanceof Repairable) {
				Repairable rMeta = (Repairable) meta;
				
				if (tag.has("RepairCost") && tag.get("RepairCost").isJsonPrimitive()) {
					rMeta.setRepairCost(tag.get("RepairCost").getAsInt());
				}
			}
			
			stack.setItemMeta(meta);
		}
		
		return stack;
	}

	private List<Color> colorFromJson(JsonArray colors) {
		List<Color> array = new ArrayList<Color>();
		
		for (JsonElement color : colors) {
			array.add(Color.fromRGB(color.getAsInt()));
		}
		
		return array;
	}

	private org.bukkit.FireworkEffect.Type getType(int type) {
		switch (type) {
			case 0:
				return FireworkEffect.Type.BALL;
			case 1:
				return FireworkEffect.Type.BALL_LARGE;
			case 2:
				return FireworkEffect.Type.CREEPER;
			case 3:
				return FireworkEffect.Type.STAR;
		}
		return FireworkEffect.Type.BALL;
	}

	@Override
	public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject json = new JsonObject();
		
		json.addProperty("id", src.getTypeId());
		
		if (src.getAmount() != 1) { json.addProperty("Count", src.getAmount()); }
		if (src.getDurability() != 0) { json.addProperty("Damage", src.getDurability()); }
		
		if (src.hasItemMeta()) {
			JsonObject tag = new JsonObject();
			ItemMeta meta = src.getItemMeta();
			
			// Common tags
			if (meta.hasDisplayName()) { tag.addProperty("Name", meta.getDisplayName()); }
			if (meta.hasLore()) { tag.add("Lore", toJson(meta.getLore())); }
			
			if (meta.hasEnchants()) {
				JsonArray enchants = new JsonArray();
				
				for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
					JsonObject ench = new JsonObject();
					ench.addProperty("id", entry.getKey().getId());
					ench.addProperty("lvl", entry.getValue());
					enchants.add(ench);
				}
				
				tag.add("ench", enchants);
			}
			
			// ItemSpecific Tags
			if (meta instanceof LeatherArmorMeta) {
				LeatherArmorMeta laMeta = (LeatherArmorMeta) meta;
				
				if (laMeta.getColor() != null) {
					tag.addProperty("color", toJson(laMeta.getColor()));
				}
			}
			if (meta instanceof MapMeta) {
				MapMeta mMeta = (MapMeta) meta;
				
				if (mMeta.isScaling()) {
					tag.addProperty("map_is_scaling", toJson(mMeta.isScaling()));
				}
			}
			if (meta instanceof PotionMeta) {
				PotionMeta pMeta = (PotionMeta) meta;
				
				if (pMeta.hasCustomEffects()) {
					JsonArray effects = new JsonArray();
					
					for (PotionEffect effect : pMeta.getCustomEffects()) {
						JsonObject effectJson = new JsonObject();
						effectJson.addProperty("id", effect.getType().getId());
						effectJson.addProperty("Amplifier", effect.getAmplifier());
						effectJson.addProperty("Duration", effect.getDuration());
						effectJson.addProperty("Ambient", toJson(effect.isAmbient()));
						effects.add(effectJson);
					}
					
					tag.add("CustomPotionEffects", effects);
				}
			}
			if (meta instanceof SkullMeta) {
				SkullMeta sMeta = (SkullMeta) meta;
				
				if (sMeta.hasOwner()) {
					tag.addProperty("SkullOwner", sMeta.getOwner());
				}
			}
			if (meta instanceof EnchantmentStorageMeta) {
				EnchantmentStorageMeta esMeta = (EnchantmentStorageMeta) meta;
				
				if (esMeta.hasStoredEnchants()) {
					JsonArray enchants = new JsonArray();
					
					for (Map.Entry<Enchantment, Integer> entry : esMeta.getStoredEnchants().entrySet()) {
						JsonObject ench = new JsonObject();
						ench.addProperty("id", entry.getKey().getId());
						ench.addProperty("lvl", entry.getValue());
						enchants.add(ench);
					}
					
					tag.add("StoredEnchantments", enchants);
				}
			}
			if (meta instanceof BookMeta) {
				BookMeta sMeta = (BookMeta) meta;
				
				if (sMeta.hasAuthor()) {
					tag.addProperty("author", sMeta.getAuthor());
				}
				if (sMeta.hasTitle()) {
					tag.addProperty("title", sMeta.getTitle());
				}
				if (sMeta.hasPages()) {
					tag.add("pages", toJson(sMeta.getPages()));
				}
			}
			if (meta instanceof FireworkEffectMeta) {
				FireworkEffectMeta feMeta = (FireworkEffectMeta) meta;
				
				if (feMeta.hasEffect()) {
					FireworkEffect effect = feMeta.getEffect();
					JsonObject effectJson = new JsonObject();
					effectJson.addProperty("Flicker", toJson(effect.hasFlicker()));
					effectJson.addProperty("Trail", toJson(effect.hasTrail()));
					effectJson.addProperty("Type", effect.getType().ordinal());
					effectJson.add("Colors", colorToJson(effect.getColors()));
					effectJson.add("FadeColors", colorToJson(effect.getFadeColors()));
					tag.add("Explosion", effectJson);
				}
			}
			if (meta instanceof FireworkMeta) {
				FireworkMeta fMeta = (FireworkMeta) meta;
				JsonArray effects = new JsonArray();
				
				for (FireworkEffect effect : fMeta.getEffects()) {
					JsonObject effectJson = new JsonObject();
					effectJson.addProperty("Flicker", toJson(effect.hasFlicker()));
					effectJson.addProperty("Trail", toJson(effect.hasTrail()));
					effectJson.addProperty("Type", effect.getType().ordinal());
					effectJson.add("Colors", colorToJson(effect.getColors()));
					effectJson.add("FadeColors", colorToJson(effect.getFadeColors()));
					effects.add(effectJson);
				}
				
				tag.add("Firework", effects);
			}
			if (meta instanceof Repairable) {
				Repairable rMeta = (Repairable) meta;
				
				if (rMeta.hasRepairCost()) {
					tag.addProperty("RepairCost", rMeta.getRepairCost());
				}
			}
			
			json.add("tag", tag);
		}
		
		return json;
	}

	private JsonArray colorToJson(List<Color> colors) {
		JsonArray array = new JsonArray();
		
		for (Color color : colors) {
			array.add(new JsonPrimitive(toJson(color)));
		}
		
		return array;
	}

	private int toJson(Color color) {
		return color.getRed() << 16 + color.getGreen() << 8 + color.getBlue();
	}

	private int toJson(boolean bool) {
		return bool ? 1 : 0;
	}

	private JsonArray toJson(List<String> parts) {
		JsonArray array = new JsonArray();
		
		for (String string : parts) {
			array.add(new JsonPrimitive(string));
		}
		
		return array;
	}
	
}