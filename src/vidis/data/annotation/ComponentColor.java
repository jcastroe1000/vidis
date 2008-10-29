package vidis.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Die Annotation ComponentColor ist optional und beschreibt welchen
 * grundfarbton ein element haben soll.
 * 
 * @author dominik
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
public @interface ComponentColor {
	/**
	 * Die farbe dieser Komponente
	 */
	ColorType color();
}
