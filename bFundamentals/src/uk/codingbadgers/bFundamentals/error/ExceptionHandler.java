package uk.codingbadgers.bFundamentals.error;

import java.lang.Thread.UncaughtExceptionHandler;

import uk.codingbadgers.bFundamentals.bFundamentals;

public class ExceptionHandler implements UncaughtExceptionHandler {

	private static final ExceptionHandler instance;
	
	static {
		instance = new ExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(instance);
	}

	public static boolean handleException(Throwable e) {
		if (!bFundamentals.getConfigurationManager().isDebugEnabled()) e.printStackTrace();
		ReportExceptionRunnable run = new ReportExceptionRunnable(e);
		return run.run();
	}
	
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		handleException(e);
	}
}
