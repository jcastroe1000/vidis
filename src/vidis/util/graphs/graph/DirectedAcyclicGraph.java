/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph;

import java.util.List;

/**
 * The DirectedAcyclicGraph class represents a directed acyclic graph (DAG)
 * where there is no cyclic paths for any of its vertices.
 *
 * A cylic path is a path from a vertex back to itself by following the
 * direction of the edges.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */

public interface DirectedAcyclicGraph extends Graph {

  /**
    * Returns a List of vertices that is not depended on by other vertices.
    * That is, a List of vertices where there are no Edges pointing to it.
    *
    * @return	List of vertices
    */
  public List getRoot( );

  /**
   *  Perform a topological sort of the entire directed acyclic graph.
   *  Note that the sequence of vertices in the return List will not distinguish
   *  between connected components of the graph.
   *
   *  @return List containing the sequence of the vertices visited in the entire graph,
   *  regardless of the connected components of the graph.
   *  @see #reverseTopologicalSort()
   */
  public List topologicalSort( );

  /**
   *  Perform a reverse topological sort of the entire directed acyclic graph.
   *  Note that the sequence of vertices in the return List will not distinguish
   *  between connected components of the graph. This simply calls topologicalSort()
   *  and reverses the sequence of vertices visited.
   *
   *  @return List containing the sequence of the vertices visited in the entire graph,
   *  regardless of the connected components of the graph.
   *  @see #topologicalSort()
   */
  public List reverseTopologicalSort( );

  /**
   * Perform a topological sort of the connected set of a directed acyclic graph
   * to which Vertex startat belongs, starting at Vertex startat.
   *
   * @param	startat	  The Vertex to which you want to start the traversal.
   *
   * @return  A List of vertices in the order that they were visited.
   */
  public List topologicalSort( Vertex startat );

  /**
   * Perform a reverse topological sort of the connected set of a directed acyclic graph
   * to which Vertex startat belongs, starting at Vertex startat.
   *
   * This method is not part of the GraphTraversal abstract class, but is added
   * here for convenience.
   *
   * @param	startat	  The Vertex to which you want to start the traversal.
   *
   * @return  A List of vertices in the order that they were visited.
   */
  public List reverseTopologicalSort( Vertex startat );
}
