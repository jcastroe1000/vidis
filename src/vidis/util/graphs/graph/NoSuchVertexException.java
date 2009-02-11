package vidis.util.graphs.graph;

/**
 * No such vertex exception.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 *
 */
public class NoSuchVertexException extends GraphException {

    public NoSuchVertexException() {
        super();
    }

    public NoSuchVertexException( String msg ) {
        super( msg );
    }
}