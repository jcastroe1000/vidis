package vidis.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation determines where a variable should be displayed
 * 
 * PLEASE NOTE: THIS CLASS IS DEPRECATED AND SHOULD NO MORE USED!
 * 				IT WILL BE REMOVED SOON!
 * 
 * @author Dominik
 * @see DisplayType
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.FIELD })
public @interface Display {
	@Deprecated
	DisplayType type() default DisplayType.SHOW_SWING;

	String name();
}
