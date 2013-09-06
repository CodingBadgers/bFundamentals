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
package uk.codingbadgers.bFundamentals.update;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

/**
 * The Update Logger.
 */
public class UpdateLogger extends Logger {

	/** The module name. */
	String moduleName;
	
	/**
	 * Instantiates a new module logger.
	 * 
	 * @param update the {@link Updater} for this logger
	 */
	public UpdateLogger(Updater update) {
		super(update.getModule().getName(), null);
		 moduleName = new StringBuilder().append("[Update] ").append("[").append(update.getModule().getName()).append("] ").toString();
	     setParent(Bukkit.getServer().getLogger());
	     setLevel(Level.ALL);
	}
	
	/* (non-Javadoc)
	 * @see java.util.logging.Logger#log(java.util.logging.LogRecord)
	 */
	public void log(LogRecord logRecord) {
        logRecord.setMessage(moduleName + logRecord.getMessage());
        super.log(logRecord);
	}

}
