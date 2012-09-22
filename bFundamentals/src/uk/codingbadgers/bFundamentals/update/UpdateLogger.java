package uk.codingbadgers.bFundamentals.update;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

/**
 * The Module Logger.
 */
public class UpdateLogger extends Logger {

	/** The module name. */
	String moduleName;
	
	/**
	 * Instantiates a new module logger.
	 *
	 * @param module the module
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
