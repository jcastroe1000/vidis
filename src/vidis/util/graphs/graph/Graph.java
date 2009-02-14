/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * An interface for Graphs.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */
public interface Graph extends Serializable {

  // ------------------- Informational methods
  /**
   * Returns true if the Graph is directed.
   */
  public boolean isDirected();

  /**
    * Returns the number of vertices in the graph
    *
    * @return	The number of vertices in the graph.
    */
  public int getVerticesCount();

  // ------------------- Vertex manipulation
  /**
    * Adds a Vertex into the Graph. This will also create a new entry
    * in the edges List and add the newly added Vertex to its own
    * connected set, thereby adding a new List in the connectedSet List.
    * Finally, all GraphAddVertexListeners are informed of the event that a
    * Vertex has been added to the Graph.
    *
    * @param		v		Vertex to be added to the Graph
    */
  public void add( Vertex v ) throws Exception;

  /**
   * Removes the specified Edge from the Graph.
   *
   * @param   edge    The Edge object to be removed.
   */
  public void remove( Vertex v ) throws Exception;

  /**
   * Returns an iterator that iterates through the graph's vertices.
   *
   * @return  An iterator of List vertices.
   */
  public Iterator<Vertex> getVerticesIterator();

  /**
   * Returns a clone of the List of vertices.
   *
   * @return  A clone of the List of vertices.
   */
  public List<Vertex> cloneVertices();

  /**
   * Returns <code>true</code> if there is an edge from v1 to v2.
   *
   * @param   v1    One endpoint of the edge
   * @param   v2    Other endpoint of the edge
   */
  public boolean haveCommonEdge( Vertex v1, Vertex v2 );

  // ----------------------- Edge manipulation
  /**
   * Method to create the proper type of Edge class.
   *
   * @param   v1    One endpoint of the edge
   * @param   v2    Other endpoint of the edge
   *
   */
  public Edge createEdge( Vertex v1, Vertex v2 );

  /**
    * Adds an Edge into the Graph. The vertices of the Edge must already
    * be existing in the Graph for this method to work properly.
    * The vertices in both ends of the Edge are merged into one connected set,
    * thereby possibly decreasing the number of Lists in the coonectedSet List.
    * Finally, all GraphAddEdgeListeners are informed of the event that a
    * Edge has been added to the Graph.
    *
    * @param		v1	One endpoint of the edge
    * @param    v2  Other endpoint of the edge
    * @return   The Edge object created and added to the Graph.
    */
  public Edge addEdge( Vertex v1, Vertex v2 ) throws Exception;


  /**
    * Adds an Edge into the Graph. The vertices of the Edge need not be
    * existing in the Graph for this method to work properly.
    * The vertices in both ends of the Edge are merged into one connected set,
    * thereby possibly decreasing the number of Lists in the coonectedSet List.
    * Finally, all GraphAddEdgeListeners are informed of the event that a
    * Edge has been added to the Graph.
    * <p>
    * In the event that any one of the vertices are not existing in the Graph,
    * they are added to the Graph.
    * <p>
    * <b>Note:</b> It is the caller's responsibility to make sure that the
    * type of Edge being added to the Graph matches the Graph. For example,
    * only a DirectedEdge must be added to a DirectedGraph.
    *
    * @param	e   The edge to be added to the Graph.
    */
  public void addEdge( Edge e ) throws Exception;

  /**
   * Removes the specified Edge from the Graph.
   *
   * @param   e    The Edge object to be removed.
   */
  public void removeEdge( Edge e ) throws Exception;

  /**
    * Removes incident Edges of a Vertex. The Edges removed are those whose
    * either endpoints has the specified vertex. This method is usually
    * called just prior to removing a Vertex from a Graph.
    *
    * @param		v	Vertex whose Edges are to be removed
    */
  public void removeEdges( Vertex v ) throws Exception;

  // --------------------------- Degree methods
  /**
   * Returns the degree of the graph, which is simply the highest degree
   * of all the graph's vertices.
   *
   * @return  An int indicating the degree of the graph.
   */
  public int getDegree();

  /**
   * Returns the degree of the vertex, which is simply the number of edges
   * of the vertex.
   *
   * @return  The degree of the vertex.
   */
  public int getDegree( Vertex v );

  /**
   * Returns all vertices.
   *
   * @return  A collection containing all vertices.
   */
  public List<Vertex> getVertices();

  /**
   * Returns all vertices with the specified degree.
   *
   * @param   degree    The degree of the vertex to be returned.
   * @return  A collection of vertices with the above specified degree.
   */
  public Set<Vertex> getVertices( int degree );

  // ---------------------- Adjacency methods
  /**
   * Returns a List of edges of the specified vertex.
   *
   * @param   v   The vertex whose edges we want returned
   * @return  A List of Edges that are incident edges of the specified vertex.
   */
  public List<Edge> getEdges( Vertex v );

  /**
   * Returns a List of all edges in the graph.
   *
   * @return  A List of all Edges in the Graph.
   */
  public Set<Edge> getAllEdges();

  /**
    * Returns the vertices adjacent to the specified vertex.
    *
    * @param		v	The Vertex you want to determine its adjacent vertices.
    * @return	  List of vertices adjacent to the specified vertex v.
    */
  public List<Vertex> getAdjacentVertices( Vertex v );

  /**
    * Returns the vertices adjacent to the specified vertex where the connecting
    * edge is directed from the adjacent vertex to the specified vertex.
    * This method is useful only for directed graphs.
    *
    * @param  v	  The Vertex you want to determine its adjacent vertices.
    * @return	  List of vertices adjacent to the specified vertex v.
    */
  public List<Vertex> getIncomingAdjacentVertices( Vertex v );

  /**
    * Returns the vertices adjacent to the specified vertex where the connecting
    * edge is directed from the specified vertex to the adjacent vertex.
    * This method is useful only for directed graphs.
    *
    * @param  v   The Vertex you want to determine its adjacent vertices.
    * @return	  List of vertices adjacent to the specified vertex v.
    */
  public List<Vertex> getOutgoingAdjacentVertices( Vertex v );

  /**
    * Returns the vertices adjacent to all the vertices in the given collection.
    *
    * @param		vertices		List of Vertex where each vertex in the returned Set
    *                               must be adjacent to.
    * @return	  Set of vertices adjacent to all the vertices in the supplied List.
    */
  public Set<Vertex> getAdjacentVertices( List<Vertex> vertices );

  /**
   * Determines if there is a path from Vertex fromVertex to Vertex toVertex.
   * This will not return true if the only path has at least one Edge pointing
   * in the opposite direction of the path.
   *
   * @param		fromVertex		starting Vertex for the path
   * @param		toVertex			ending Vertex for the path
   * @return	true if there is a path from Vertex to toVertex. false otherwise.
   */
  public boolean isPath( Vertex fromVertex, Vertex toVertex );
}

