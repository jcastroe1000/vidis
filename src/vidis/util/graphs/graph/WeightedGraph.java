package vidis.util.graphs.graph;


/**
 * A interface for a Graph where all edges have a specified weight.
 */

public interface WeightedGraph extends Graph {

  /**
   * Convenience method to add a WeightedEdge with a specified weight
   * into the WeightedGraph. The default addEdge( v1, v2 ) will add a
   * WeightedEdge with zero weight, after which you can call setWeight()
   * to specify the weight.
   *
   * @return  The WeightedEdge that has been added.
   */
  public WeightedEdge addEdge( Vertex v1, Vertex v2, double weight ) throws Exception;

  /**
   * Determines the Vertex that is 'closest' to the Vertex specified.
   * The definition of the closest vertex in this context is a
   * vertex that is directly adjacent to Vertex v where the edge
   * has the least weight.
   *
   * @return  The Vertex closes to Vertex v.
   */
  public Vertex getClosest( Vertex v );
}