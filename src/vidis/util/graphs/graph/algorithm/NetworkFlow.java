package vidis.util.graphs.graph.algorithm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import vidis.util.graphs.graph.Edge;
import vidis.util.graphs.graph.GraphException;
import vidis.util.graphs.graph.Network;
import vidis.util.graphs.graph.NetworkException;
import vidis.util.graphs.graph.Vertex;

/**
 * Abstract class for handling a flow on a <tt>Network</tt>.
 *
 * Concrete implementations of this class must never modify the Network itself.
 *
 * @author Ralf Vandenhouten
 * @version 1.0 2002-10-12
 */

public abstract class NetworkFlow implements Serializable {

  /**
   * The <tt>Network</tt> object on which the flow will be defined.
   */
  Network network;

  /**
   * Source and sink of the network.
   */
  Vertex source, sink;

  /**
   * The HashMap for mapping the (directed) edges of the <tt>Network</tt> to
   * their flows.
   */
  HashMap flowMap;

  /**
   * Constructor to be used by subclasses of this abstract class.
   */
  public NetworkFlow( Network network ) throws NetworkException {
    this.network = network;
    this.source = network.getSource();
    this.sink = network.getSink();
    if ( source == null || sink == null )
        throw new NetworkException("Source or sink missing.");
    this.flowMap = new HashMap();
  }

  /**
   * Returns a HashMap that maps each edge of the network to its flow.
   *
   * @return The HashMap that maps each edge of the network to its flow.
   */
  public Map getFlowMap() {
    return flowMap;
  }

  /**
   * Returns the total flow coming from the source and flowing to the sink.
   *
   * @return The total flow (absolute value of the flow).
   */
  public double getTotalFlow() throws GraphException {
    double totalFlow = 0;
    Vertex v = network.getSource();
    if ( v == null )
        throw new NetworkException("No source defined.");

    // Add all outgoing flows to the total flow
    Iterator it = network.getEdges( v ).iterator();
    while ( it.hasNext() ) {
        Edge e = (Edge)it.next();
        if ( flowMap.get(e) instanceof Double )
            totalFlow += ((Double)flowMap.get(e)).doubleValue();
        else if ( flowMap.get(e) instanceof Integer )
            totalFlow += ((Integer)flowMap.get(e)).doubleValue();
        else
            throw new NetworkException("Incomplete flow definition.");
    }
    return totalFlow;
  }

  /**
   * Initializes the flow in the network by setting the flow of each edge to 0.
   */
  public void initializeFlow() {
    Iterator it = network.getAllEdges().iterator();

    while ( it.hasNext() )
        flowMap.put( it.next(), new Double(0) );
  }

  /**
   * Method for finding a maximum flow in the network.
   */
  abstract public void maximizeFlow();
}