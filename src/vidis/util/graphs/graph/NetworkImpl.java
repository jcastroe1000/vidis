package vidis.util.graphs.graph;


/**
 * This class implements the <tt>Network</tt> interface as a directed,
 * weighted graph with a source and a sink vertex.
 *
 * @author Ralf Vandenhouten
 * @version 1.0 2002-10-12
 */

public class NetworkImpl extends WeightedGraphImpl implements Network {

    /**
     * The source <tt>Vertex</tt> of the <tt>Network</tt>.
     */
    Vertex source;

    /**
     * The sink <tt>Vertex</tt> of the <tt>Network</tt>.
     */
    Vertex sink;

    /**
     * Constructor for instances of the <tt>NetworkImpl</tt> class.
     */
    public NetworkImpl() {
        super( true ); // directed weighted graph
        source = sink = null;
    }

    /**
     * Sets the source vertex of the <tt>Network</tt>. The
     * <tt>Vertex</tt> specified must already be in the <tt>Network</tt>.
     * Otherwise, a <tt>NoSuchVertexException</tt> is returned.
     */
    public void setSource( Vertex sourceVertex ) throws GraphException {
        if ( sourceVertex == null )
            throw new NoSuchVertexException();
        if ( !this.vertices.contains( sourceVertex )) {
            throw new NoSuchVertexException();
        }
        source = sourceVertex;
    }

    /**
     * Sets the sink vertex of the <tt>Network</tt>. The
     * <tt>Vertex</tt> specified must already be in the <tt>Network</tt>.
     * Otherwise, a <tt>NoSuchVertexException</tt> is returned.
     */
    public void setSink( Vertex sinkVertex ) throws GraphException {
        if ( sinkVertex == null )
            throw new NoSuchVertexException();
        if ( !this.vertices.contains( sinkVertex )) {
            throw new NoSuchVertexException();
        }
        sink = sinkVertex;
    }

    /**
     * Returns the current source of the <tt>Network</tt>. It is
     * possible that this method will return null if the <tt>Network</tt>
     * is empty.
     */
    public Vertex getSource() {
        return source;
    }

    /**
     * Returns the current sink of the <tt>Network</tt>. It is
     * possible that this method will return null if the <tt>Network</tt>
     * is empty.
     */
    public Vertex getSink() {
        return sink;
    }

    /**
     * Convenience method to add a WeightedEdge with a specified (non-negative)
     * network capacity into the <tt>WeightedGraph</tt>.
     * The default addEdge( v1, v2 ) will add a WeightedEdge with zero capacity,
     * after which you can call setWeight() to specify the capacity.
     *
     * @return  The WeightedEdge that has been added.
     */
    public WeightedEdge addEdge( Vertex v1, Vertex v2, double capacity )
    throws Exception {
        if ( capacity < 0 )
            throw new NegativeCapacityException();
        else if ( v1 == sink || v2 == source )
            throw new NetworkException("Source or sink condition violated.");
        else
            return super.addEdge( v1, v2, capacity );
    }
}
