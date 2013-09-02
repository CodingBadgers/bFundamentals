package uk.codingbadgers.bFundamentals.module.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModuleInfo {

	String value();
	
	String version() default "1.0";
	
	String[] authors() default {"Unkown"};

	String description() default "";
	
}
