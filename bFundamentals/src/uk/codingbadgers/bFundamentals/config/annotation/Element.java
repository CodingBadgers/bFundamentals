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
package uk.codingbadgers.bFundamentals.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import uk.codingbadgers.bFundamentals.config.deserialiser.ConfigMapper;
import uk.codingbadgers.bFundamentals.config.deserialiser.DefaultConfigMapper;

/**
 * Repersents a class that is serialised in a config file by the
 * bFundamentals {@link ConfigFactory} as a element inside the file.
 * 
 * @author James Fitzpatrick
 * @see ConfigFile
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Element {

	/**
	 * The name of this config element, defaults to the field name
	 * 
	 * @return the name of the config element
	 */
	String value() default "";
	
	/**
	 * The config mapper for this element, this class will serialise and
	 * deserialise this config element
	 *
	 * @return the config mapper
	 * @see ConfigMapper
	 */
	Class<? extends ConfigMapper<?>> mapper() default DefaultConfigMapper.class;
	
}
