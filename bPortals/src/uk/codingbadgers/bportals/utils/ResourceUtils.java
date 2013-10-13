/**
 * bPortals 1.2-SNAPSHOT
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
package uk.codingbadgers.bportals.utils;

import static org.apache.commons.io.IOUtils.readLines;
import static org.apache.commons.lang.StringUtils.join;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import uk.codingbadgers.bFundamentals.bFundamentals;

public class ResourceUtils {

	public static String loadResourceContents(String type, String id) throws IOException {
		return join(readLines(loadResource(type, id)), ' ');
	}
	
	public static InputStream loadResource(String type, String id) throws IOException {
		String path = type + '/' + id + '.' + type;
		System.out.println(path);
		InputStream stream = bFundamentals.getInstance().getResource(path);
		
		if (stream == null) {
			throw new FileNotFoundException(path);
		}
		
		return stream;
	}
}
