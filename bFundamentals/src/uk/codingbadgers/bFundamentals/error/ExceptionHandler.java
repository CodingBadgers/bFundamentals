package uk.codingbadgers.bFundamentals.error;

import java.lang.Thread.UncaughtExceptionHandler;

public class ExceptionHandler implements UncaughtExceptionHandler {

	private static final ExceptionHandler instance;
	
	static {
		instance = new ExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(instance);
	}

	public static void handleException(Throwable e) {
		instance.uncaughtException(Thread.currentThread(), e);
	}
	
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		ReportExceptionRunnable run = new ReportExceptionRunnable(e);
		run.run();
		e.printStackTrace();
	}
}
