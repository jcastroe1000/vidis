package vidis.util.graphs.graph.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import vidis.util.graphs.graph.Edge;
import vidis.util.graphs.graph.Network;
import vidis.util.graphs.graph.NetworkException;
import vidis.util.graphs.graph.Vertex;
import vidis.util.graphs.util.Queue;

/**
 * Class that provides a flow on a 0-1-<tt>Network</tt>, i.e. a network with
 * each of its edges having a flow of either 0 or 1.
 * The capacity (i.e. the weight) of an edge is interpreted as 0 if it is 0
 * and interpreted as 1 otherwise. Having it this way we need not define
 * a new class for unit capacity networks, but can use the regular
 * <tt>Network</tt> classes here as well.
 * The class provides an efficient algorithm derived from that of Dinic for
 * finding a maximum flow.
 *
 * @author Ralf Vandenhouten
 * @version 1.0 2002-10-12
 */

public class UnitCapacityNetworkFlow extends NetworkFlow {
  /**
   * Number of vertices in the network.
   */
  int nvertices;

  /**
   * Indices of source and sink vertex, respectively.
   */
  int sourceIndex, sinkIndex;

  /**
   * Array storing the vertices linearly.
   */
  Vertex[] vertex;

  /**
   * HashMap storing the indices of the vertices in the above array vertex.
   */
  HashMap vertIndex;

  /**
   * Arrays of lists containing the adjacent vertices.
   */
  ArrayList[] successors, residueSuccessors;

  /**
   * Flow matrix storing the binary flow of the edges.
   */
  boolean[][] flow;

  /**
   * Constructor for instances of this class.
   */
  public UnitCapacityNetworkFlow( Network network ) throws NetworkException {
    super( network );
    int i = 0, j;
    nvertices = network.getVerticesCount();
    vertex = new Vertex[nvertices];
    vertIndex = new HashMap( nvertices*3/2 );
    successors = new ArrayList[nvertices];
    residueSuccessors = new ArrayList[nvertices];
    flow = new boolean[nvertices][nvertices];

    // Initializing the data structures
    Iterator it = network.getVerticesIterator();
    while ( it.hasNext() ) {
        Vertex v = (Vertex)it.next();
        vertIndex.put( v, new Integer(i) );
        successors[i] = (ArrayList)
                ((ArrayList)network.getOutgoingAdjacentVertices( v )).clone();
        vertex[i++] = v;
    }
    for (i=0; i<nvertices; i++) {
        for (j=0; j<nvertices; j++) {
            flow[i][j] = false;
        }
    }
    try {
        sourceIndex = ((Integer)vertIndex.get( source )).intValue();
        sinkIndex = ((Integer)vertIndex.get( sink )).intValue();
    } catch (Exception e) {
        throw new NetworkException("Source or sink not defined.");
    }
  }

  /**
   * Method for finding a maximum flow in the network. The method uses the
   * algorithm of Dinic by iteratively searching blocking flows in
   * breadth-first search-tree residue networks.
   */
  public void maximizeFlow() {
    while ( residueNetwork() )
        blockingFlow();

    // Finally generate the flow HashMap
    Iterator it = network.getAllEdges().iterator();
    while ( it.hasNext() ) {
        Edge e = (Edge)it.next();
        int i = ((Integer)vertIndex.get(e.getVertexA())).intValue();
        int j = ((Integer)vertIndex.get(e.getVertexB())).intValue();
        flowMap.put( e, new Integer( flow[i][j]?1:0 ) );
    }
  }

  /**
   * Compute a residue network by a breadth-first search of paths from source
   * to sink.
   *
   * @return Returns true if the breadth-first search finds a path to the sink,
   *         otherwise false.
   */
  public boolean residueNetwork() {
    int i, index;
    Queue q = new Queue();
    boolean[] visited = new boolean[nvertices];
    int[] niveau = new int[nvertices];
    int currentNiveau=0, stopNiveau=nvertices-1;
    boolean found = false;

    // Initializing
    for (i=0; i<nvertices; i++) {
        residueSuccessors[i] = new ArrayList();
    }

    // breadth-first search through the network until sink is found
    q.put( new Integer(sourceIndex) );
    visited[sourceIndex] = true;
    while ( !q.isEmpty() ) {
        try {
            index = ((Integer)q.get()).intValue();
        } catch (Exception e) { e.printStackTrace(); return false; }
        currentNiveau = niveau[index]+1;
        if ( currentNiveau > stopNiveau )
            break;

        Iterator it = successors[index].iterator();
        while ( it.hasNext() ) {
            int neighbor = ((Integer)vertIndex.get(it.next())).intValue();
            if ( visited[neighbor] && niveau[neighbor] < currentNiveau )
                continue;
            residueSuccessors[index].add( vertex[neighbor] );
            if ( !visited[neighbor] && currentNiveau < stopNiveau )
                q.put( new Integer(neighbor) );
            if ( neighbor == sinkIndex ) {
                stopNiveau = currentNiveau;
                found = true;
            }
            visited[neighbor] = true;
            niveau[neighbor] = currentNiveau;
        }
    }
    return found;
  }

  /**
   * Compute a blocking flow by using findFlowPath() iteratively.
   */
  public void blockingFlow() {
    while ( findFlowPath( sourceIndex ) )
        ; // this is not a mistake!
  }

  /**
   * Helper method for finding flow paths used by maximizeFlow().
   */
  private boolean findFlowPath(int vertex) {
    if ( residueSuccessors[vertex].isEmpty() )
        return false;
    else
        if ( residueSuccessors[vertex].contains( sink ) ) { // sink found
            flow[vertex][sinkIndex] = true;
            successors[vertex].remove( sink );
            residueSuccessors[vertex].remove( sink );
            return true;
        } else { // depth-first-search for a path to sink
            int size;
            while ( (size = residueSuccessors[vertex].size()) > 0 ) {
                Vertex vj = (Vertex)
                    residueSuccessors[vertex].remove(size-1);
                // Recursive call to findFlowPath()
                int j = ((Integer)vertIndex.get(vj)).intValue();
                if ( findFlowPath(j) ) {
                    flow[vertex][j] = true;
                    // Remove edge from the original graph
                    successors[vertex].remove( vj );
                    return true;
                }
            }
        }
    return false;
  }
}