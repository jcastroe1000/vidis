package vidis.util.graphs.graph.algorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import vidis.util.graphs.graph.Edge;
import vidis.util.graphs.graph.Graph;
import vidis.util.graphs.graph.GraphException;
import vidis.util.graphs.graph.Network;
import vidis.util.graphs.graph.NetworkImpl;
import vidis.util.graphs.graph.Vertex;

/**
 * Class that computes the connectivity of a undirected graph.
 *
 * @author Ralf Vandenhouten
 * @version 1.0 2002-10-12
 */

public class Connectivity implements Serializable {

  /**
   * The graph considered.
   */
  Graph graph;

  /**
   * The vertices of the graph.
   */
  List vertices;

  /**
   * The Menger-network generated from the graph.
   */
  Network network;

  /**
   * The two Menger sets of vertices in the network, stored as ArrayLists.
   */
  ArrayList v1, v2;

  /**
   * Constructor for instances of this class.
   */
  public Connectivity( Graph graph ) {
    this.graph = graph;
    generateNetwork();
  }

  /**
   * Computes the (node) connectivity number of the considered graph using
   * the Menger algorithm (see Turau, Addison-Wesley 1996).
   *
   * @return The connectivity number of the graph.
   */
  public int getConnectivity() throws GraphException {
    int j, i = 0, n = graph.getVerticesCount(), connect = n-1;
    UnitCapacityNetworkFlow ucflow;

    while ( i <= connect ) {
        i++;
        for (j=i+1; j<=n; j++) {
            Vertex v = (Vertex)vertices.get(i-1);
            Vertex w = (Vertex)vertices.get(j-1);
            if ( !graph.haveCommonEdge( v, w ) ) {
                network.setSource( (Vertex)v2.get(i-1) );
                network.setSink( (Vertex)v1.get(j-1) );
                ucflow = new UnitCapacityNetworkFlow( network );
                ucflow.maximizeFlow();
                connect = Math.min( connect, (int)ucflow.getTotalFlow());
            }
        }
    }
    return connect;
  }

  /**
   * Generates the Menger-network out of the given graph.
   */
  protected void generateNetwork() {
    network = new NetworkImpl();
    int i, j, nvertices = graph.getVerticesCount();
    v1 = new ArrayList(nvertices);
    v2 = new ArrayList(nvertices);
    vertices = graph.getVertices();

    // generate vertices
    for (i=0; i<nvertices; i++) {
        try {
            Vertex v = (Vertex)vertices.get(i);
            Vertex w = new Vertex(v.toString()+"'");
            v1.add(i, w);
            network.add( w );
            w = new Vertex(v.toString()+"''");
            v2.add(i, w);
            network.add( w );
            network.addEdge((Vertex)v1.get(i), (Vertex)v2.get(i), 1.0);
        } catch (Exception e) { e.printStackTrace(); }
    }

    // generate edges
    try {
        Iterator it = graph.getAllEdges().iterator();
        while ( it.hasNext() ) {
            Edge e = (Edge)it.next();
            Vertex v = e.getVertexA();
            Vertex w = e.getVertexB();
            i = vertices.indexOf( v );
            j = vertices.indexOf( w );
            network.addEdge((Vertex)v2.get(i), (Vertex)v1.get(j), 1.0);
            network.addEdge((Vertex)v2.get(j), (Vertex)v1.get(i), 1.0);
        }
    } catch (Exception e) { e.printStackTrace(); }
  }
}
