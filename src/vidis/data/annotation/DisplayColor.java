package vidis.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation determines the variable's color
 * A method MUST return a <code>vidis.data.annotation.ColorType</code> instance
 * A field MUST be of the Type <code>vidis.data.annotation.ColorType</code>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.FIELD })
public @interface DisplayColor {
}
