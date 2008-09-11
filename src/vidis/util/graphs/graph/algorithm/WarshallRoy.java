package vidis.util.graphs.graph.algorithm;

import java.util.Iterator;

import vidis.util.graphs.graph.Graph;
import vidis.util.graphs.graph.Vertex;

/**
 * An implementation of the transitive closure algorithm
 * of S.A. Warshall and B. Roy
 *
 * @author  Ralf Vandenhouten
 */

public class WarshallRoy {

  /**
   * Perform the Warshall/Roy algorithm for determining the transitive closure
   * of a graph
   *
   * @param	graph   The Graph that the algorithm will be applied to.
   *
   * @result    The resulting Graph of the algorithm (identical to input
   *            parameter graph).
   */
  public static Graph transClosure(Graph graph) {
    // Three iterators are needed instead of integer loop variables
    // in ordert to meet the specifications of the Graph interface
    Iterator i, j, l;
    Vertex vi, vj, vl;

    // The algorithm itself is quite the same as the original
    l = graph.getVerticesIterator();
    while ( l.hasNext() ) {
      vl = (Vertex)l.next();
      i = graph.getVerticesIterator();
      while ( i.hasNext() ) {
        vi = (Vertex)i.next();
        if ( graph.haveCommonEdge( vi, vl ) ) {
          j = graph.getVerticesIterator();
          while ( j.hasNext() ) {
            vj = (Vertex)j.next();
            if ( graph.haveCommonEdge(vl, vj) && !graph.haveCommonEdge(vi, vj))
              try {
                graph.addEdge( vi, vj );
              } catch (Exception e) {}
          }
        }
      }
    }

    return graph;
  }
}