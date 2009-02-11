/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import vidis.util.graphs.graph.algorithm.TopologicalSorting;

/**
 * The DirectedAcyclicGraph class represents a directed acyclic graph (DAG)
 * where there is no cyclic paths for any of its vertices.
 *
 * A cylic path is a path from a vertex back to itself by following the
 * direction of the edges.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */

public class DirectedAcyclicGraphImpl extends GraphImpl implements DirectedAcyclicGraph {
  /**
   * Delegate object to handle topological sorting
   */
  TopologicalSorting  topologicalsorting;

  /**
   * Creates a DirectedAcyclicGraph object.
   */
  public DirectedAcyclicGraphImpl() {
    super(true);
    topologicalsorting = new TopologicalSorting( this );
  }

  /**
    * Adds an Edge into the DirectedAcyclicGraph. This will only add the Edge
    * if there is currently no path from the Vertex toVertex to Vertex fromVertex.
    * If there is, there will be a cycle thereby violating the property of a
    * directed acyclic graph.
    *
    * @param		fromVertex		Vertex that will be the origin of the Edge
    *           that will be added into the Graph.
    * @param		toVertex		Vertex that will be the destination of the Edge
    *           that will be added into the Graph.
    * @return   The Edge object added to the Graph. The Edge object will be
    *           an instance of DirectedEdge. If there is already a path
    *           from fromVertex to toVertex, then this method returns null.
    */
  public Edge addEdge( Vertex fromVertex, Vertex toVertex ) throws Exception {
    if( !isPath( toVertex, fromVertex ))
        return super.addEdge( fromVertex, toVertex );
    else
        throw new CycleException();
  }

  /**
    * Adds an Edge into the DirectedAcyclicGraph. This will only add the Edge
    * if there is currently no path from the Vertex toVertex to Vertex fromVertex
    * of the edge being added. If there is, there will be a cycle thereby
    * violating the property of a directed acyclic graph.
    *
    * @param	e   The edge to be added to the Graph.
    */
  public void addEdge( Edge edge ) throws Exception {
    DirectedEdge dEdge = ( DirectedEdge ) edge;
    Vertex  fromVertex = dEdge.getSource();
    Vertex  toVertex = dEdge.getSink();

    if( !isPath( toVertex, fromVertex ))
        super.addEdge( dEdge );
    else
        throw new CycleException();
  }

  /**
    * Returns a List of vertices that is not depended on by other vertices.
    * That is, a List of vertices where there are no Edges pointing to it.
    *
    * @return	List of vertices
    */
  public List getRoot( ){
    Iterator  iterator;
    Iterator  edgeiterator;
    List        currentedges;
    List        rootVertices;
    DirectedEdge  dedge;

    // Initially assume all vertices as root vertices
    rootVertices = (List) ((ArrayList) vertices).clone();

    // Then remove all vertices that are sinks of an edge
    iterator = this.getAllEdges().iterator();
    while( iterator.hasNext() ){
      dedge = (DirectedEdge)iterator.next();
      rootVertices.remove( dedge.getSink() );
    }

    // The remaining vertices in the vector will be the root vertices.
    return rootVertices;
  }

  /**
   * Perform a topological sort of the entire directed acyclic graph.
   * Note that the sequence of vertices in the return List will not distinguish
   * between connected components of the graph.
   *
   * This method is not part of the GraphTraversal abstract class, but is added
   * here for convenience.
   *
   * @return List containing the sequence of the vertices visited in the
   * entire directed acyclic graph, regardless of the connected components of the graph.
   */
  public List topologicalSort( ){
    return this.topologicalsorting.traverse();
  }

  /**
   * Perform a reverse topological sort of the entire directed acyclic graph.
   * Note that the sequence of vertices in the return List will not distinguish
   * between connected components of the graph.
   *
   * This method is not part of the GraphTraversal abstract class, but is added
   * here for convenience.
   *
   * @return List containing the sequence of the vertices visited in the
   * entire directed acyclic graph, regardless of the connected components of the graph.
   */
  public List reverseTopologicalSort( ){
    return this.topologicalsorting.reverseTraverse();
  }

  /**
   * Perform a topological sort of the connected set of a directed acyclic graph
   * to which Vertex startat belongs, starting at Vertex startat.
   *
   * @param	startat	  The Vertex to which you want to start the traversal.
   *
   * @return  A List of vertices in the order that they were visited.
   */
  public List topologicalSort( Vertex startat ){
    return this.topologicalsorting.traverse( startat );
  }

  /**
   * Perform a reverse topological sort of the connected set of a directed acyclic graph
   * to which Vertex startat belongs, starting at Vertex startat.
   *
   * @param	startat	  The Vertex to which you want to start the traversal.
   *
   * @return  A List of vertices in the order that they were visited.
   */
  public List reverseTopologicalSort( Vertex startat ){
    return this.topologicalsorting.reverseTraverse( startat );
  }

}
