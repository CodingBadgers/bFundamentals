package uk.thecodingbadgers.bFundamentals.config;

import uk.codingbadgers.bFundamentals.config.ConfigFile;
import uk.codingbadgers.bFundamentals.config.Element;

public class Config implements ConfigFile {

	@Element()
	public static final String testVal1 = "test";

	@Element("testValTest")
	public static final String testVal2 = "test";

	@Element("Blah")
	public static final String testVal3 = "test";
	
}
