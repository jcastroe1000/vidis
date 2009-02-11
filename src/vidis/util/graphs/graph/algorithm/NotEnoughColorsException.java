package vidis.util.graphs.graph.algorithm;

import vidis.util.graphs.graph.GraphException;

/**
 * Thrown when a <tt>GraphColoring</tt> class needs more colors than
 * specified by the caller.
 * 
 * @author Ralf Vandenhouten
 */

public class NotEnoughColorsException extends GraphException {
    public NotEnoughColorsException() {
        super();
    }

    public NotEnoughColorsException( String msg ) {
        super( msg );
    }
}