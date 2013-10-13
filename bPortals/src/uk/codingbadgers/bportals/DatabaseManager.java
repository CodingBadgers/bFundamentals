/**
 * bPortals 1.2-SNAPSHOT
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
package uk.codingbadgers.bportals;

import java.io.IOException;

import uk.codingbadgers.bportals.utils.ResourceUtils;
import uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase;

public class DatabaseManager {

	private static final String PORTAL_TABLE_NAME = "bportals_portals";
	
	private BukkitDatabase database;

	public DatabaseManager(BukkitDatabase database) {
		this.database = database;
		
		createTable();
	}
	
	public void createTable() {
		if (database.tableExists(PORTAL_TABLE_NAME)) {
			return;
		}
		
		try {
			database.query(ResourceUtils.loadResourceContents("sql", PORTAL_TABLE_NAME), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadPortals() {
		
	}
	
	public void savePortals() {
		
	}

	void destroy() {
		
	}
}
