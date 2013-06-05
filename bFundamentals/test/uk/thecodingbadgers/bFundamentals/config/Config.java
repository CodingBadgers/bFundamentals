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
