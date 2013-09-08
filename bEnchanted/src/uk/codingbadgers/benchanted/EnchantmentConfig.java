/**
 * bFundamentalsBuild 1.2-SNAPSHOT
 * Copyright (C) 2013  CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
