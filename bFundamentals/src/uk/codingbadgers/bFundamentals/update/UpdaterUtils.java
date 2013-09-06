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

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * The Updater utils class
 */
public class UpdaterUtils {

	/**
	 * Download a file from a specific url.
	 *
	 * @param link the link
	 * @param outputFile the output file
	 * @throws Exception the exception
	 */
	public static void download(URL link, File outputFile) throws Exception{
		if (link == null) 
			throw new IllegalArgumentException ("dl link cannot be null");
		
        ReadableByteChannel rbc = Channels.newChannel(link.openStream());
        FileOutputStream output = new FileOutputStream(outputFile);
        output.getChannel().transferFrom(rbc, 0, 1 << 24);
        output.close();
	}
}
