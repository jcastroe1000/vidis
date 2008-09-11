package vidis.util.graphs.graph;

/**
 * A visitor that always return true when visiting.
 */

public class NullVisitor implements Visitor {

    public boolean visit( Vertex vertexToVisit ){
        return true;
    }
}

