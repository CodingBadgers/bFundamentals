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

import uk.codingbadgers.bFundamentals.config.ConfigFile;
import uk.codingbadgers.bFundamentals.config.annotation.Catagory;
import uk.codingbadgers.bFundamentals.config.annotation.Element;

public class Config implements ConfigFile {

	@Element
	public static final String testVal1 = "test";

	@Element
	public static final String testVal2 = "test";

	@Element
	public static final String testVal3 = "test";
	
	@Catagory("database-settings")
	public static class Database {

		@Element
		public static final String host = "localhost";

		@Element
		public static final String user = "root";

		@Element
		public static final String password = "";
	}
	
	@Catagory("update-settings")
	public static class Update {

		@Element
		public static final boolean enabled = true;

		@Element
		public static final boolean apply = false;
	}
}
