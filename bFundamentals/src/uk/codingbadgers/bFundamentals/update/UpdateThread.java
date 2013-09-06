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

/**
 * The Updater thread
 */
public class UpdateThread extends Thread {

	/** The updater to use. */
	private final Updater m_updater;
	
	/**
	 * Instantiates a new update thread.
	 *
	 * @param updater the updater
	 */
	public UpdateThread(Updater updater) {
		m_updater = updater;
		this.setName("Updater: "+updater.getModule());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			m_updater.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the updater from the thread.
	 *
	 * @return the updater
	 */
	public Updater getUpdater() {
		return m_updater;
	}
}
