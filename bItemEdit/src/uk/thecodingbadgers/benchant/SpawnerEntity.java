package uk.thecodingbadgers.benchant;

import java.util.ArrayList;

import net.minecraft.server.v1_4_6.NBTTagCompound;
import net.minecraft.server.v1_4_6.NBTTagFloat;
import net.minecraft.server.v1_4_6.NBTTagList;

import org.bukkit.craftbukkit.v1_4_6.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class SpawnerEntity {

	public static final int HELMET = 0;
	public static final int CHEST_PLATE = 1;
	public static final int LEGGINGS = 2;
	public static final int BOOTS = 3;
	public static final int HAND = 4;
	
	private EntityType type;
	private ItemStack[] inventory;
	private ArrayList<PotionEffect> activeEffects;
	private int health;

	public SpawnerEntity(EntityType type) {
		this.type = type;
		inventory = new ItemStack[] {null, null, null, null, null} ;
		activeEffects = new ArrayList<PotionEffect>();
	}
	
	public ItemStack[] getInventory() {
		return inventory;
	}
	
	public void addEffect(PotionEffect effect) {
		activeEffects.add(effect);
	}
	
	public void setEquiment(int pos, ItemStack item) {
		inventory[pos] = item;
	}

	public NBTTagCompound getNBTData() {
		NBTTagCompound nbt = new NBTTagCompound();
		
		NBTTagList equipment = new NBTTagList();
		for(int i = 0; i < 5; i++) {
			
			if (inventory[i] == null) {
				equipment.add(new NBTTagCompound());
				continue;
			}
			
			net.minecraft.server.v1_4_6.ItemStack nmsStack = CraftItemStack.asNMSCopy(inventory[i]);
			equipment.add(nmsStack.save(new NBTTagCompound()));
		}
		
		NBTTagList potionEffects = new NBTTagList();
		for(PotionEffect effect : activeEffects) {
			
			NBTTagCompound effectNBT = new NBTTagCompound();
			effectNBT.setInt("Id", effect.getType().getId());
			effectNBT.setInt("Amplifier", effect.getAmplifier());
			effectNBT.setInt("Duration", effect.getDuration());
			effectNBT.setInt("Ambient", effect.isAmbient() ? 1 : 0);
			
			potionEffects.add(effectNBT);
		}
		
		NBTTagList dropChance = new NBTTagList();
		for(int i = 0; i < 5; i++) {
			dropChance.add(new NBTTagFloat(String.valueOf(i), 0.5f));
		}
		
		nbt.set("ActiveEffects", potionEffects);
		nbt.set("Equipment", equipment);
		nbt.set("DropChances", dropChance);
		nbt.setInt("Health", health);
		nbt.setInt("CanPickUpLoot", 1);
		
		return nbt;
	}

	public String getID() {
		return type.getName();
	}
}
