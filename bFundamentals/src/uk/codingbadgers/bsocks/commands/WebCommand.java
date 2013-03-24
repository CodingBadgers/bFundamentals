package uk.codingbadgers.bsocks.commands;

import org.apache.commons.lang.Validate;
import org.json.simple.JSONObject;

public abstract class WebCommand {

	private String label;

	public WebCommand(String label) {
		Validate.notNull(label, "Label cannot be null");
		
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public abstract JSONObject handleCommand(JSONObject obj);
	
}
