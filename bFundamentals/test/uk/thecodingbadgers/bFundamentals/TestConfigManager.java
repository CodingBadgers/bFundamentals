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

import java.io.File;
import java.io.IOException;

import uk.codingbadgers.bFundamentals.ConfigManager;
import uk.codingbadgers.bFundamentals.DatabaseSettings;

public class TestConfigManager implements ConfigManager {

	@Override
	public void loadConfiguration(File configFile) throws IOException {
		
	}

	@Override
	public DatabaseSettings getDatabaseSettings() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getLanguage() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDebugEnabled() {
		return true;
	}

	@Override
	public boolean isAutoUpdateEnabled() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isAutoDownloadEnabled() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isAutoInstallEnabled() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getLogPrefix() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCrashPassword() {
		return "PASSWORD";
	}

	
}
