package uk.codingbadgers.benchanted;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentConfig {

	final private FileConfiguration m_config;
	final private Enchantment m_enchantment;
	
	public EnchantmentConfig(Enchantment enchant, YamlConfiguration loadConfiguration) {
		m_config = loadConfiguration;
		m_enchantment = enchant;
	}
	
	public Enchantment GetEnchantment() {
		return m_enchantment;
	}
	
	public FileConfiguration GetConfig() {
		return m_config;	
	}

}
