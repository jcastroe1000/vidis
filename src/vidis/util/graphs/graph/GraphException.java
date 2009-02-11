package vidis.util.graphs.graph;

/**
 * Exception superclass thrown from methods of graphs.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */

public class GraphException extends Exception {

    public GraphException() {
        super();
    }

    public GraphException( String msg ) {
        super( msg );
    }
}