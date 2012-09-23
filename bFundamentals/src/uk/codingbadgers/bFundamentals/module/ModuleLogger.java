package uk.codingbadgers.bFundamentals.module;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

import uk.codingbadgers.bFundamentals.bFundamentals;

/**
 * The Module Logger.
 */
public class ModuleLogger extends Logger {

	/** The logger prefix. */
	String prefix;
	
	/**
	 * Instantiates a new module logger.
	 *
	 * @param module the module
	 */
	public ModuleLogger(Module module) {
		super(module.getName(), null);
		 prefix = new StringBuilder().append(bFundamentals.getConfigurationManager().getLogPrefix() + " ").append("[").append(module.getName()).append("] ").toString();
	     setParent(Bukkit.getServer().getLogger());
	     setLevel(Level.ALL);
	}
	
	/* (non-Javadoc)
	 * @see java.util.logging.Logger#log(java.util.logging.LogRecord)
	 */
	public void log(LogRecord logRecord) {
        logRecord.setMessage(prefix + logRecord.getMessage());
        super.log(logRecord);
	}

}
