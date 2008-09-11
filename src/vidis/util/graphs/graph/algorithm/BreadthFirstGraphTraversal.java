package vidis.util.graphs.graph.algorithm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import vidis.util.graphs.graph.Graph;
import vidis.util.graphs.graph.NullVisitor;
import vidis.util.graphs.graph.Vertex;
import vidis.util.graphs.graph.Visitor;
import vidis.util.graphs.util.EmptyQueueException;
import vidis.util.graphs.util.Queue;

/**
 * A concrete subclass of GraphTraversal that uses breadth-first search
 * in traversing a graph. Note that the traverse() method will only
 * traverse the connected set to which the Vertex the traversal will start at belongs.
 */

public class BreadthFirstGraphTraversal extends GraphTraversal {
  Queue queue;
  int count;

  /**
   * Creates a BreadthFirstGraphTraversal object.
   */
  public BreadthFirstGraphTraversal( Graph graph ) {
    super( graph );
    this.queue = new Queue();
    count = 0;
  }

  public int traverse( Vertex startat, List visited, Visitor visitor ) {
    Vertex  next;
    Vertex  adjacent;
    List    adjacentVertices;
    Iterator  iterator;

    // Push the starting vertex onto the stack
    this.queue.put( startat );
    visitedMap.put( startat, new Integer(count++) );

    do {
      // Get the next vertex in the queue and add it to the visited
      try {
        next = (Vertex) this.queue.get();
        visited.add( next );

        // Exit if the visitor tells us so
        if( !visitor.visit( next ))
          return TERMINATEDBYVISITOR;

        // Get all of its adjacent vertices and push them onto the stack
        // only if it has not been visited
        adjacentVertices = graph.getOutgoingAdjacentVertices( next );
        iterator = adjacentVertices.iterator();
        while( iterator.hasNext()) {
          adjacent = (Vertex) iterator.next();
          if ( visitedMap.get(adjacent) == null ) {
            visitedMap.put( adjacent, new Integer(count++) );
            this.queue.put( adjacent );
          }
        }
      } catch (EmptyQueueException e) { e.printStackTrace(); }

    } while( !this.queue.isEmpty() );
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