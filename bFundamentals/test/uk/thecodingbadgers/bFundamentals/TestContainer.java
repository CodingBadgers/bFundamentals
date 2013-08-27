package uk.thecodingbadgers.bFundamentals;

import org.junit.BeforeClass;

import uk.codingbadgers.bFundamentals.bFundamentals;

public class TestContainer {

	@BeforeClass
	public static void setup() {
		bFundamentals.setInstance(new TestPlugin());
		bFundamentals.getInstance().setConfigManager(new TestConfigManager());
	}
}
