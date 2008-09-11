package vidis.util.graphs.graph;

/**
 * Thrown whenever a <tt>Tree</tt> becomes malformed as a result
 * of calling a method that is declared on its superinterface
 * but is not supported.
 */

public class IllegalTreeException extends GraphException {

    public IllegalTreeException() {
        super();
    }

    public IllegalTreeException( String msg ) {
        super( msg );
    }
}