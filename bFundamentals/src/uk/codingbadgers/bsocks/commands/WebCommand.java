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
