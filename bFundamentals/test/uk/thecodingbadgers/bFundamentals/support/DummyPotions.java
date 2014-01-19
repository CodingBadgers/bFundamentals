package uk.thecodingbadgers.bFundamentals.support;

import net.minecraft.server.v1_7_R1.MobEffectList;

import org.bukkit.craftbukkit.v1_7_R1.potion.CraftPotionBrewer;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;

public class DummyPotions {
	
	static {
		Potion.setPotionBrewer(new CraftPotionBrewer());
		MobEffectList.BLINDNESS.getClass();
		PotionEffectType.stopAcceptingRegistrations();
	}

	public static void setup() {
	}

}
