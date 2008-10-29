package vidis.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Die Annotation ComponentInfo kann vor jedem User-Implementertem Link, jeder Node und jedem Packet stehen
 * 
 * Damit kann der Name einer Komponente festgelegt werden.
 * 
 * NOTIZ: der name kann anschliessend auch noch von einer funktion ueberschrieben werden!
 * @see Display
 * @author Christoph
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ComponentInfo {
	/**
	 * Der Name dieser Komponente
	 */
	String name();
}
