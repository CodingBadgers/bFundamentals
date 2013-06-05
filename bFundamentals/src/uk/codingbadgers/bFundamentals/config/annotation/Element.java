package uk.codingbadgers.bFundamentals.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import uk.codingbadgers.bFundamentals.config.ConfigFile;
import uk.codingbadgers.bFundamentals.config.ConfigFactory;
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
