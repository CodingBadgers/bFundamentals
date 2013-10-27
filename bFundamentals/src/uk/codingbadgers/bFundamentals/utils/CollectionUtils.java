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
package uk.codingbadgers.bFundamentals.utils;

import java.util.List;

import com.google.common.collect.ImmutableList.Builder;

/**
 * The Utility class for using google collections.
 */
public class CollectionUtils {

	/**
	 * Create a immutable copy of a list.
	 *
	 * @param <T> the generic type of the list
	 * @param list the list to create a copy of
	 * @return a immutable copy of the list
	 */
	public static <T> List<T> toImmutableList(List<T> list) {
		Builder<T> builder = new Builder<T>();
		builder.addAll(list);
		return builder.build();
	}
}
