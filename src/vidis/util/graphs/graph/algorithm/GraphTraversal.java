/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Jesus M. Salvo, Ralf Vandenhouten
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph.algorithm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vidis.util.graphs.graph.Graph;
import vidis.util.graphs.graph.Vertex;
import vidis.util.graphs.graph.Visitor;

/**
 * Abstract class for an algorithm implementing graph traversal.
 * Classes implementing the Graph interface uses the Strategy
 * pattern to allow different implementations of the graph traversal
 * algorithm to be used.
 *
 * Concrete implementations of this class must never modify the Graph itself.
 * 
 * @author Ralf Vandenhouten
 */

public abstract class GraphTraversal implements Serializable {
  static final public int   TERMINATEDBYVISITOR = -1;
  static final public int   OK = 1;

  /**
   * The Graph on which graph traversal will be performed.
   */
  Graph   graph;

  /**
   * The HashMap for mapping the traversed vertices to Integers indicating
   * their position during traversal.
   */
  HashMap visitedMap;

  public GraphTraversal( Graph graph ) {
    this.graph = graph;
    this.visitedMap = new HashMap();
  }

  /**
   * Abstract traversal method to be implemented by subclasses.
   *
   * @param startat The vertex from which traversal will start.
   * @param visitor Visitor object controlling if and when traversal will stop,
   *                apart from having visited all the vertices.
   * @param visited A List of vertices that has been visited in sequence by the traversal
   */
  public abstract int traverse( Vertex startat, List visited, Visitor visitor );

  /**
   * Abstract traversal method to be implemented by subclasses.
   *
   * @param startat The vertex from which traversal will start.
   * @return  A VList of vertices that has been visited in sequence by the traversal
   */
  public abstract List traverse( Vertex startat );

  /**
   * Abstract traversal method to be implemented by subclasses.
   *
   * @param startat The vertex from which traversal will start.
   * @param visitor Visitor object controlling if and when traversal will stop,
   *                apart from having visited all the vertices.
   * @return  A List of vertices that has been visited in sequence by the traversal
   */
  public abstract List traverse( Vertex startat, Visitor visitor );

  /**
   * Returns a HashMap that maps each traversed vertex to an Integer object
   * that indicates its position during the traversal.
   *
   * @return The HashMap that maps each traversed vertex to its position.
   */
  public Map getVisitedMap() {
    return visitedMap;
  }
}