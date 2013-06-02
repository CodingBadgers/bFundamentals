package uk.thecodingbadgers.bFundamentals.config;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

import uk.codingbadgers.bFundamentals.config.ConfigFactory;

public class ConfigTest {

	@Test
	public void test() {
		
		File configFile = new File("config.yml");
		configFile.delete();
		
		try {
			if (!configFile.exists()) {
				ConfigFactory.createDefaultConfig(Config.class, configFile);
			}
			
			ConfigFactory.loadConfig(Config.class, configFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		assertEquals(Config.testVal1, "test");
		assertEquals(Config.testVal2, "test");
		assertEquals(Config.testVal3, "test");
	}
}
