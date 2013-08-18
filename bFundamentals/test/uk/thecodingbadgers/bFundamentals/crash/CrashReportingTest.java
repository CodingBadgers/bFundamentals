package uk.thecodingbadgers.bFundamentals.crash;

import org.junit.Test;

import uk.codingbadgers.bFundamentals.error.ExceptionHandler;
import uk.thecodingbadgers.bFundamentals.TestContainer;
import static org.junit.Assert.*;

public class CrashReportingTest extends TestContainer {

	@Test
	public void sendException() {
		try {
			ExceptionHandler.handleException(new Exception("Test exception"));
		} catch (Exception ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}
}
