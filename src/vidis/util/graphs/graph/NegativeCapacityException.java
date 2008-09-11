package vidis.util.graphs.graph;

public class NegativeCapacityException extends GraphException {

    public NegativeCapacityException() {
        super();
    }

    public NegativeCapacityException( String msg ) {
        super( msg );
    }
}