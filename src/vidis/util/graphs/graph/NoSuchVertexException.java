package vidis.util.graphs.graph;

public class NoSuchVertexException extends GraphException {

    public NoSuchVertexException() {
        super();
    }

    public NoSuchVertexException( String msg ) {
        super( msg );
    }
}