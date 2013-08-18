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
