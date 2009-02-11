/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph.algorithm;

import java.util.HashMap;
import java.util.Iterator;

import vidis.util.graphs.graph.Network;
import vidis.util.graphs.graph.NetworkException;
import vidis.util.graphs.graph.Vertex;
import vidis.util.graphs.graph.WeightedEdge;
import vidis.util.graphs.util.EmptyQueueException;
import vidis.util.graphs.util.Queue;

/**
 * Class that provides a flow on a <tt>Network</tt> and maximizes it by
 * augmentation paths using the algorithm of Edmonds and Karp.
 *
 * @author Ralf Vandenhouten
 * @version 1.0 2002-10-12
 */

public class AugmentNetworkFlow extends NetworkFlow {

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
   * Adjacency matrix storing the capacities of the edges.
   */
  double[][] capacity;

  /**
   * Flow matrix storing the flow of the edges.
   */
  double[][] flow;

  /**
   * Array for storing the predecessor of each vertex during augmentation
   * search.
   */
  int[] predecessor;

  /**
   * Array for storing the edge type (true=forward, false=backward) for
   * each edge during augmentation search.
   */
  boolean[] forwardEdge;

  /**
   * Constructor for instances of this class.
   */
  public AugmentNetworkFlow( Network network ) throws NetworkException {
    super( network );
    int i = 0, j;
    nvertices = network.getVerticesCount();
    vertex = new Vertex[nvertices];
    vertIndex = new HashMap();
    capacity = new double[nvertices][nvertices];
    flow = new double[nvertices][nvertices];
    predecessor = new int[nvertices];
    forwardEdge = new boolean[nvertices];

    // Initializing the data structures
    Iterator it = network.getVerticesIterator();
    while ( it.hasNext() ) {
        Vertex v = (Vertex)it.next();
        vertIndex.put( v, new Integer(i) );
        vertex[i++] = v;
    }
    for (i=0; i<nvertices; i++) {
        predecessor[i] = -1;
        for (j=0; j<nvertices; j++) {
            capacity[i][j] = 0.0;
            flow[i][j] = 0.0;
        }
    }
    try {
        sourceIndex = ((Integer)vertIndex.get( source )).intValue();
        sinkIndex = ((Integer)vertIndex.get( sink )).intValue();
    } catch (Exception e) {
        throw new NetworkException("Source or sink not defined.");
    }
    // Initialize the capacity matrix
    it = network.getAllEdges().iterator();
    while ( it.hasNext() ) {
        WeightedEdge e = (WeightedEdge)it.next();
        i = ((Integer)vertIndex.get(e.getVertexA())).intValue();
        j = ((Integer)vertIndex.get(e.getVertexB())).intValue();
        capacity[i][j] = e.getWeight();
    }
  }

  /**
   * Method for finding a maximum flow in the network. The method uses the
   * algorithm of Edmonds and Karp by iteratively searching augmenting paths.
   */
  public void maximizeFlow() {
    double delta;
    // iterative search for augmentation paths
    do {
        delta = augmentationPath();
        if ( delta > 0.0 )
            increaseFlow( delta );
    } while ( delta > 0.0 );

    // Finally generate the flow HashMap
    Iterator it = network.getAllEdges().iterator();
    while ( it.hasNext() ) {
        WeightedEdge e = (WeightedEdge)it.next();
        int i = ((Integer)vertIndex.get(e.getVertexA())).intValue();
        int j = ((Integer)vertIndex.get(e.getVertexB())).intValue();
        flowMap.put( e, new Double( flow[i][j] ) );
    }
  }

  /**
   * Method for increasing the flow after a new augmenting path has been
   * computed.
   */
  private void increaseFlow( double delta ) {
    int v, pred;
    v = sinkIndex;
    while ( (pred = predecessor[v]) != -1 ) {
      if ( forwardEdge[v] ) // forward edge
        flow[pred][v] += delta;
      else                  // backward edge
        flow[v][pred] -= delta;
      v = pred;
    }
  }

  /**
   * This method searches an augmentation path for the current flow.
   */
  private double augmentationPath() {
    int i, j;
    // Array for storing the flow change for each vertex
    double[] fDelta = new double[nvertices];
    // Array for marking the visited vertices
    boolean[] visited = new boolean[nvertices];
    // Queue for the breadth-first-search
    Queue queue = new Queue();
    // Initialising
    predecessor[sourceIndex] = -1;
    visited[sourceIndex] = true;
    fDelta[sourceIndex] = Double.POSITIVE_INFINITY;
    fDelta[sinkIndex] = 0.0;
    // Search the shortest augmenting path with breadth-first-search
    queue.put( new Integer(sourceIndex) );
    while ( fDelta[sinkIndex]==0.0 && !queue.isEmpty() ) {
        try {
            i = ((Integer)queue.get()).intValue();
        } catch (EmptyQueueException e) { i=0; } // should never happen...
        // handle successors of this vertex
        for (j=0; j<nvertices; j++)
            if ( capacity[i][j]>0 )
                if ( !visited[j] && flow[i][j] < capacity[i][j] ) {
                    visited[j] = true;
                    queue.put( new Integer(j) );
                    fDelta[j] = Math.min( capacity[i][j]-flow[i][j], fDelta[i]);
                    predecessor[j] = i;
                    forwardEdge[j] = true;
                }
        // handle predecessors of the vertex
        for (j=0; j<nvertices; j++)
            if ( capacity[j][i]>0 )
                if ( !visited[j] && flow[j][i] > 0.0 ) {
                    visited[j] = true;
                    queue.put( new Integer(j) );
                    fDelta[j] = Math.min( flow[j][i], fDelta[i]);
                    predecessor[j] = i;
                    forwardEdge[j] = false;
                }
    }
    return fDelta[sinkIndex];
  }
}