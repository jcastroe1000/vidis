package vidis.util.graphs.graph.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import vidis.util.graphs.graph.Graph;
import vidis.util.graphs.graph.NullVisitor;
import vidis.util.graphs.graph.Vertex;
import vidis.util.graphs.graph.Visitor;

/**
 * A concrete subclass of GraphTraversal that uses depth-first search
 * in traversing a graph. Note that the traverse() method will only
 * traverse the connected set to which the Vertex the traversal will start at belongs.
 */

public class DepthFirstSimpleGraphTraversal extends GraphTraversal {
  Stack   stack;
  int     count;
  /**
   * Creates a DepthFirstSimpleGraphTraversal object.
   */
  public DepthFirstSimpleGraphTraversal( Graph graph ) {
    super( graph );
    this.stack = new Stack();
    count = 0;
  }

  public int traverse( Vertex startat, List visited, Visitor visitor ) {
    Vertex  next;
    Vertex  adjacent;
    List    adjacentVertices;
    Iterator  iterator;

    // Push the starting vertex onto the stack
    this.stack.push( startat );

    do {
      // Get the next vertex in the queue and add it to the visited
      next = (Vertex) this.stack.pop();
      visited.add( next );
      visitedMap.put( next, new Integer(count++) );

      // Exit if the visitor tells us so
      if( !visitor.visit( next ))
        return TERMINATEDBYVISITOR;

      // Get all of its adjacent vertices and push them onto the stack
      // only if it has not been visited and it has not been stacked
      adjacentVertices = graph.getOutgoingAdjacentVertices( next );
      iterator = adjacentVertices.iterator();
      while( iterator.hasNext()) {
        adjacent = (Vertex) iterator.next();
        if( !visited.contains( adjacent ) && !this.stack.contains( adjacent )) {
          this.stack.push( adjacent );
        }
      }

    } while( !this.stack.isEmpty() );
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