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
