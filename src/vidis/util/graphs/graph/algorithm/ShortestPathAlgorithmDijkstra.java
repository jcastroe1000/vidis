/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Jesus M. Salvo, Ralf Vandenhouten
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import vidis.util.graphs.graph.DirectedWeightedEdge;
import vidis.util.graphs.graph.Vertex;
import vidis.util.graphs.graph.WeightedEdge;
import vidis.util.graphs.graph.WeightedGraph;
import vidis.util.graphs.graph.WeightedGraphImpl;
import vidis.util.graphs.util.Heap;
import vidis.util.graphs.util.HeapNode;
import vidis.util.graphs.util.HeapNodeComparator;

/**
 * A concrete implementation of ShortestPathAlgorithm using Dijkstra's method.
 * Note that the Dijkstra method can only be used for graphs with
 * non-negative edge weights.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */

public class ShortestPathAlgorithmDijkstra extends ShortestPathAlgorithm {
  /**
   * List of vertices that has been added to the tree
   */
  private List  visited;

  /**
   * Heap of FringeObjects that are to be processed.
   */
  private Heap  fringe;

  /**
   * Subgraph forming the shortest spanning tree.
   */
  private WeightedGraph shortestpathtree;

  /**
   * Comparator used to be compare priorities in the fringe.
   */
  private HeapNodeComparator  comparator;

  /**
   * Creates an instance of ShortestPathAlgorithmDijkstra.
   *
   * @param wgraph  The WeightedGraph where a shortest path spanning tree will be determined.
   * @param comparator  The HeapNodeComparator to be used to compare priorities of objects in the fringe/heap.
   */
  public ShortestPathAlgorithmDijkstra( WeightedGraph wgraph, HeapNodeComparator comparator ) {
    super( wgraph );
    this.visited = new ArrayList( 10 );
    this.comparator = comparator;
    this.fringe = new Heap( new HeapNodeComparator( 1 ));
  }

  /**
   * Determines the shortest path from a given vertex to all other vertices
   * that are in the same connected set as the given vertex in the weighted graph
   * using Dijkstra's algorithm.
   *
   * @return  A WeightedGraph comprising of the shortest path spanning tree.
   */
  public WeightedGraph shortestPath() {
    if ( startVertex == null )
        return null;

    // clear the graph.
    this.shortestpathtree = new WeightedGraphImpl( true );

    // Now add the vertex we are interested in to the fringe.
    this.fringe.insert( new HeapNode(
                            new FringeObject( startVertex, null ), 0.0 ));

    // Continually process while there are vertices in the heap.
    while( !this.fringe.isEmpty() ) {

      // Moves the highest priority node from the heap to the tree.
      this.moveToVisited();
    }

    // Clear the vectors
    this.visited.clear();
    this.fringe.clear();

    return shortestpathtree;
  }

  /**
   * Moves the vertex that has the highest priority in the heap
   * from the fringe to the tree
   */
  private void moveToVisited() {
    HeapNode  prioritynode;

    // Remove the node with highest priority from the fringe ...
    prioritynode = this.fringe.remove( );

    FringeObject  fringeobject = (FringeObject) prioritynode.getObject();
    Vertex    priorityvertex = fringeobject.vertex;

    // ... and add it to the tree.
    this.visited.add( priorityvertex );
    if( fringeobject.edge != null ) {
        try {
            this.shortestpathtree.addEdge( fringeobject.edge );
        }
        catch( Exception ex ) {
            ex.printStackTrace();
        }
    }

    // ... then move all the adjacent vertices of the vertex
    // we just moved to the tree and put them in the fringe.
    this.moveAdjacentVerticesToFringe( prioritynode );
  }

  /**
   * Gets all the adjacent vertices from unseen to the fringe.
   * The parameter vertex must be a vertex that is being moved
   * from the fringe to the tree. This method must only be called
   * by the method moveToVisited().
   *
   * @param vertex  The vertex that is being moved from the fringe to the tree
   * and whose adjacent vertices we want added to the fringe.
   */
  private void moveAdjacentVerticesToFringe( HeapNode prioritynode ) {
    // Get the vertex encapsulated by the heap node
    Vertex    priorityvertex = ((FringeObject) prioritynode.getObject()).vertex;

    // Then get the edges of the vertex
    // We cant use wgraph.getAdjacentVertices() as that will
    // not tell us what edge to use.
    List    incidentedges = this.wgraph.getEdges( priorityvertex );

    // Get the priority of the heap node
    double    priority = prioritynode.getPriority();

    // We need to iterate through the edges
    Iterator  iterator = incidentedges.iterator();

    // Because we are using HeapNodes in the fringe,
    // we need a way to compare the object (a Vertex) encapsulated in a HeapNode in the
    // fringe and a Vertex
    HeapNodeObjectComparator  heapnodeobjectcomparator = new HeapNodeObjectComparator();

    // The current incident edge iterated
    WeightedEdge  incidentedge;

    // The current adjacent vertex within the iteration
    Vertex    adjacentvertex;

    // Either an existing HeapNode or an exsting one in the fringe
    HeapNode  heapnode;

    // The new priority of a heapnode
    double    fringepriority;

    // For each edge of the vertex ...
    while( iterator.hasNext() ) {
      incidentedge = (WeightedEdge) iterator.next();

      // skip edge if graph is directed and the edge is an incoming edge
      if ( wgraph.isDirected() && incidentedge.getVertexB()==priorityvertex )
        continue;

      // ... get the vertex opposite to the priority vertex
      // meaning get the adjacent vertex
      adjacentvertex = incidentedge.getOppositeVertex( priorityvertex );

      // ... check if the adjacent vertex has been visited
      // If it has been visited, then proceed with the next edge
      if( this.visited.contains( adjacentvertex )) continue;

      // ... check if the adjacent vertex is already in the fringe
      heapnode = (HeapNode) this.fringe.contains( adjacentvertex, heapnodeobjectcomparator );

      // Create a new DirectedWeightedEdge (!) for the target tree
      DirectedWeightedEdge newedge = new DirectedWeightedEdge(
                    priorityvertex, adjacentvertex, incidentedge.getWeight() );

      // ... if it is not yet on the fringe, add it to the fringe
      if( heapnode == null ) {
        fringepriority = priority + incidentedge.getWeight();
        heapnode = new HeapNode( new FringeObject( adjacentvertex, newedge ), fringepriority );
        this.fringe.insert( heapnode );
      }
      // If it is already the fringe, reassign its priority,
      // only if it will give it a "higher" priority.
      // Use the HeapNodeComparator to determine which is "higher".
      else {
        fringepriority = priority + incidentedge.getWeight();
        if( this.comparator.compare( new HeapNode( adjacentvertex, fringepriority), heapnode ) < 0 ) {
          this.fringe.setPriority( heapnode, fringepriority );
          // Also reassign the edge that is used to get the shortest path
          FringeObject   fobject = (FringeObject) heapnode.getObject();
          fobject.edge = newedge;
        }
      }
    }
  }
}

/**
 * A Comparator for comparing the Vertex in the FringObject of the HeapNode
 * against another Vertex.
 */
class HeapNodeObjectComparator implements Comparator {

  /**
   * Compare a Vertex against the Vertex in the FringeObject of a HeapNode.
   * The comparison is made using the == operator to compare the actual instance.
   *
   * @return  Returns 0 if they are the same object. Returns -1 otherwise.
   */
  public int compare( Object vertex, Object hnode ) {
    Vertex    compareTo = (Vertex) vertex;
    HeapNode  heapnode = (HeapNode) hnode;
    FringeObject  fringeobject = (FringeObject) heapnode.getObject();
    Vertex    heapobject = (Vertex) fringeobject.vertex;

    if( compareTo == heapobject )
      return 0;
    else
      return -1;
  }
}

/**
 * An Object encapsulated in a HeapNode, which in turn is added to the fringe.
 * The classical algorithm only mentions of storing vertices in the fringe, but
 * it is programatically difficult to determine what edge to add to the
 * shortest path spanning tree when moving a vertex from the fringe to the tree.
 * It is therefore easier to store the edge along with the vertex in the fringe.
 *
 * The edge stored along with the vertex in the fringe is the
 * edge connecting the vertex that has been moved from the fringe to the tree
 * and the vertex adjacent to the vertex that has been moved from the fringe
 * and being added to the fringe.
 *
 */
class FringeObject {
  Vertex        vertex;
  DirectedWeightedEdge  edge;

  public FringeObject( Vertex vertex, DirectedWeightedEdge edge ) {
    this.vertex = vertex;
    this.edge = edge;
  }

  public String toString() {
    return "Vertex: " + vertex + "; Edge: " + edge;
  }
}
