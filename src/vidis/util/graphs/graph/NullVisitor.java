package vidis.util.graphs.graph;

/**
 * A visitor that always return true when visiting.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */

public class NullVisitor implements Visitor {

    public boolean visit( Vertex vertexToVisit ){
        return true;
    }
}

