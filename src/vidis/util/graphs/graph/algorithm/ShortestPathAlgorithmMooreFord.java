package vidis.util.graphs.graph.algorithm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import vidis.util.graphs.graph.DirectedWeightedEdge;
import vidis.util.graphs.graph.Vertex;
import vidis.util.graphs.graph.WeightedEdge;
import vidis.util.graphs.graph.WeightedGraph;
import vidis.util.graphs.graph.WeightedGraphImpl;
import vidis.util.graphs.util.Queue;

/**
 * A concrete implementation of ShortestPathAlgorithm using the method
 * of E.F. Moore and L.R. Ford.
 * Note that the Moore-Ford method can only be applied to graphs without a
 * cycle that has a negative length.
 */

public class ShortestPathAlgorithmMooreFord extends ShortestPathAlgorithm {
  /**
   * Map containing the predecessors of the vertices in the shortest path tree
   */
  private Map predecessor;

  /**
   * Map mapping vertices to their distances from the start vertex
   */
  private Map distance;

  /**
   * Subgraph forming the shortest spanning tree.
   */
  private WeightedGraph shortestpathtree;

  /**
   * Creates an instance of ShortestPathAlgorithmMooreFord.
   *
   * @param wgraph  The WeightedGraph where a shortest path spanning tree will
   *                be determined.
   */
  public ShortestPathAlgorithmMooreFord( WeightedGraph wgraph ) {
    super( wgraph );
    this.predecessor = new HashMap();
    this.distance = new HashMap();
  }

  /**
   * Determines the shortest path from a given vertex to all other vertices
   * that are in the same connected set as the given vertex in the weighted graph
   * using the algorithm of Moore and Ford.
   *
   * @return  A WeightedGraph comprising of the shortest path spanning tree.
   */
  public WeightedGraph shortestPath() {
    int i;
    double di, dj;
    Vertex vi, vj;
    if ( startVertex == null )
        return null;

    // Initializing
    this.shortestpathtree = new WeightedGraphImpl( true );
    Iterator it = wgraph.getVerticesIterator();
    while ( it.hasNext() )
        distance.put( (Vertex)it.next(), new Double(Double.POSITIVE_INFINITY) );

    distance.put( startVertex, new Double(0.0) );
    Queue queue = new Queue();

    // generate the shortest path tree
    queue.put( startVertex );
    while ( !queue.isEmpty() ) {
        try {
            vi = (Vertex)queue.get();
            di = ((Double)distance.get( vi )).doubleValue();
            it = wgraph.getEdges( vi ).iterator();
            while ( it.hasNext() ) {
                WeightedEdge e = (WeightedEdge)it.next();
                // skip incoming edges
                if ( wgraph.isDirected() && e.getVertexB()== vi )
                    continue;
                vj = e.getOppositeVertex( vi );
                dj = ((Double)distance.get( vj )).doubleValue();
                if ( di + e.getWeight() < dj ) {
                    distance.put( vj, new Double(di + e.getWeight()) );
                    predecessor.put( vj,
                        new DirectedWeightedEdge( vi, vj, e.getWeight() ) );
                    if ( !queue.isQueued( vj ) )
                        queue.put( vj );
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // insert the edges into the shortest path tree
    it = predecessor.values().iterator();
    while ( it.hasNext() )
        try {
            shortestpathtree.addEdge( (DirectedWeightedEdge)it.next() );
        } catch (Exception e) { e.printStackTrace(); }
    return shortestpathtree;
  }

  /**
   * Compute the distance between two vertices in the shortest path spanning
   * tree.
   *
   * @param v1 The start vertex
   * @param v2 The target vertex
   * @return The distance between v1 and v2 in the shortest path spanning tree
   *         if v2 is reachable from v1, otherwise Double.POSITIVE_INFINITY
   *         is returned.
   */
  public double getDistance( Vertex v1, Vertex v2 ) {
    if ( shortestpathtree==null || v1!=startVertex )
        shortestpathtree = shortestPath( v1 );
    return ((Double)distance.get( v2 )).doubleValue();
  }

  /**
   * Method that computes the longest distance of any vertex from the start
   * vertex of the shortest path spanning tree.
   *
   * @param v1 The start vertex
   * @return The longest distance between v1 and any other vertex in the
   *         shortest path spanning tree.
   */
  public double getLongestDistance( Vertex v1 ) {
    if ( shortestpathtree==null || v1!=startVertex )
        shortestpathtree = shortestPath( v1 );
    double d, max = Double.NEGATIVE_INFINITY;
    Iterator it = wgraph.getVerticesIterator();
    while ( it.hasNext() ) {
        Vertex v = (Vertex)it.next();
        d = ((Double)distance.get( v )).doubleValue();
        if ( d > max && d!=Double.POSITIVE_INFINITY )
            max = d;
    }
    return max;
  }
}
