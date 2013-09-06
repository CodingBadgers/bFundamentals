/**
 * bFundamentals 1.2-SNAPSHOT
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
package uk.thecodingbadgers.bFundamentals.config;

import java.io.File;

import org.junit.Test;
import static org.junit.Assert.*;

import uk.codingbadgers.bFundamentals.config.ConfigFactory;

public class ConfigTest {

	@Test
	public void test() {
		
		File configFile = new File("test/config.yml");

		try {
			if (!configFile.exists()) {
				ConfigFactory.createDefaultConfig(Config.class, configFile);
			}
			
			ConfigFactory.loadConfig(Config.class, configFile);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}	
	}
}
