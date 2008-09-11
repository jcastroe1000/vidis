package vidis.util.graphs.graph;


/**
 * The superinterface of all <tt>Network</tt>s. This interface
 * abstracts a network as a directed, weighted graph with a source and a sink
 * vertex.
 *
 * @author Ralf Vandenhouten
 * @version 1.0 2002-10-12
 */

public interface Network extends WeightedGraph {

    /**
     * Sets the source vertex of the <tt>Network</tt>. The
     * <tt>Vertex</tt> specified must already be in the <tt>Network</tt>.
     * Otherwise, a <tt>NoSuchVertexException</tt> is returned.
     */
    public void setSource( Vertex sourceVertex ) throws GraphException;

    /**
     * Sets the sink vertex of the <tt>Network</tt>. The
     * <tt>Vertex</tt> specified must already be in the <tt>Network</tt>.
     * Otherwise, a <tt>NoSuchVertexException</tt> is returned.
     */
    public void setSink( Vertex sinkVertex ) throws GraphException;

    /**
     * Returns the current source of the <tt>Network</tt>. It is
     * possible that this method will return null if the <tt>Network</tt>
     * is empty.
     */
    public Vertex getSource();

    /**
     * Returns the current sink of the <tt>Network</tt>. It is
     * possible that this method will return null if the <tt>Network</tt>
     * is empty.
     */
    public Vertex getSink();

    /**
     * Convenience method to add a WeightedEdge with a specified (non-negative)
     * network capacity into the <tt>WeightedGraph</tt>.
     * The default addEdge( v1, v2 ) will add a WeightedEdge with zero capacity,
     * after which you can call setWeight() to specify the capacity.
     *
     * @return  The WeightedEdge that has been added.
     */
    public WeightedEdge addEdge( Vertex v1, Vertex v2, double capacity )
    throws Exception;
}
