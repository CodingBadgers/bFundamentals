package uk.codingbadgers.bsocks.commands;

import org.apache.commons.lang.Validate;
import org.json.simple.JSONObject;

/**
 * The Class WebCommand, represents a command that can only be fired over
 * the bSocks interface.
 */
public abstract class WebCommand {

	/** The command label. */
	private String label;

	/**
	 * Instantiates a new web command.
	 *
	 * @param label the label
	 */
	public WebCommand(String label) {
		Validate.notNull(label, "Label cannot be null");
		
		this.label = label;
	}
	
	/**
	 * Gets the command label.
	 *
	 * @return the command label
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Handle the command.
	 *
	 * @param obj the json object sent from the website
	 * @return the data to send back to the website
	 */
	public abstract JSONObject handleCommand(JSONObject obj);
	
}
