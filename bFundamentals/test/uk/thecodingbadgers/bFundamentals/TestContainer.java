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
package uk.thecodingbadgers.bFundamentals;

import net.minecraft.server.v1_7_R1.DispenserRegistry;

import org.junit.BeforeClass;

import uk.codingbadgers.bFundamentals.bFundamentals;
import uk.thecodingbadgers.bFundamentals.support.DummyEnchantments;
import uk.thecodingbadgers.bFundamentals.support.DummyPotions;
import uk.thecodingbadgers.bFundamentals.support.DummyServer;
import uk.thecodingbadgers.bFundamentals.support.TestConfigManager;

public class TestContainer {

	private static boolean setup = false;
	
	@BeforeClass
	public static void setup() {
		if (setup) {
			return;
		}
		
		// Setup craftbukkit, copied from AbstractTestingBase
        DispenserRegistry.b();
        DummyServer.setup();
        DummyPotions.setup();
        DummyEnchantments.setup();
        
        // Setup bFundamentals
        bFundamentals.setupGson();
        TestConfigManager.setup();
		
		setup = true;
	}
}
