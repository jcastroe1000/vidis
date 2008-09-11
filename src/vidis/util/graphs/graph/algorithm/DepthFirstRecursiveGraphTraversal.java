package vidis.util.graphs.graph.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import vidis.util.graphs.graph.Graph;
import vidis.util.graphs.graph.NullVisitor;
import vidis.util.graphs.graph.Vertex;
import vidis.util.graphs.graph.Visitor;

/**
 * A concrete subclass of GraphTraversal that uses depth-first search
 * in traversing a graph. Note that the traverse() method will only
 * traverse the connected set to which the Vertex the traversal will start at belongs.
 */

public class DepthFirstRecursiveGraphTraversal extends GraphTraversal {
  int count;

  /**
   * Creates a DepthFirstRecursiveGraphTraversal object.
   */
  public DepthFirstRecursiveGraphTraversal( Graph graph ) {
    super( graph );
    count = 0;
  }

  public int traverse( Vertex startat, List visited, Visitor visitor ) {
    Vertex  adjacent;
    List    adjacentVertices;
    Iterator  iterator;

    visitedMap.put( startat, new Integer(count++) );
    visited.add( startat );

    // Exit if the visitor tells us so
    if( !visitor.visit( startat ))
      return TERMINATEDBYVISITOR;

    // Get all of its adjacent vertices and call recursively traverse() for
    // each, but only if it has not been visited
    adjacentVertices = graph.getOutgoingAdjacentVertices( startat );
    iterator = adjacentVertices.iterator();
    while( iterator.hasNext()) {
      adjacent = (Vertex) iterator.next();
      if ( visitedMap.get(adjacent) == null )
        if ( traverse( adjacent, visited, visitor ) == TERMINATEDBYVISITOR )
          return TERMINATEDBYVISITOR;
    }

    return OK;
  }

  public List traverse( Vertex startat ) {
    return this.traverse( startat, new NullVisitor());
  }

  public List traverse( Vertex startat, Visitor visitor ) {
    List  visited = new ArrayList( 10 );

    this.traverse( startat, visited, visitor );
    return visited;
  }
}