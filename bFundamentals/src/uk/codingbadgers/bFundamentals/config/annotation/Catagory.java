package uk.codingbadgers.bFundamentals.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import uk.codingbadgers.bFundamentals.config.ConfigFile;
import uk.codingbadgers.bFundamentals.config.ConfigFactory;

/**
 * Repersents a class that is serialised in a config file by the
 * bFundamentals {@link ConfigFactory} as a category inside the file.
 * 
 * @author James Fitzpatrick
 * @see ConfigFile
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Catagory {

	/**
	 * The config key from this category, if left at {@code ""} (default value) 
	 * it will use a lower case representation of the class name.
	 */
	String value() default "";
	
}
