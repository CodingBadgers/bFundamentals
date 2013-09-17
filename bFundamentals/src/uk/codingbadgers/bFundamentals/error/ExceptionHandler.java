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
package uk.codingbadgers.bFundamentals.error;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.Level;

import uk.codingbadgers.bFundamentals.bFundamentals;

public class ExceptionHandler implements UncaughtExceptionHandler {

	private static final ExceptionHandler instance;
	
	static {
		instance = new ExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(instance);
	}

	public static boolean handleException(Throwable e) {
		bFundamentals.getInstance().getLogger().log(Level.WARNING, null, e);
		ReportExceptionRunnable run = new ReportExceptionRunnable(e);
		return run.run();
	}
	
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		handleException(e);
	}
}
