/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph.algorithm;

import java.util.Iterator;

import vidis.util.graphs.graph.Graph;
import vidis.util.graphs.graph.Vertex;

/**
 * An implementation of the transitive closure algorithm
 * of S.A. Warshall and B. Roy
 *
 * @author  Ralf Vandenhouten
 */

public class WarshallRoy {

  /**
   * Perform the Warshall/Roy algorithm for determining the transitive closure
   * of a graph
   *
   * @param	graph   The Graph that the algorithm will be applied to.
   *
   * @result    The resulting Graph of the algorithm (identical to input
   *            parameter graph).
   */
  public static Graph transClosure(Graph graph) {
    // Three iterators are needed instead of integer loop variables
    // in ordert to meet the specifications of the Graph interface
    Iterator i, j, l;
    Vertex vi, vj, vl;

    // The algorithm itself is quite the same as the original
    l = graph.getVerticesIterator();
    while ( l.hasNext() ) {
      vl = (Vertex)l.next();
      i = graph.getVerticesIterator();
      while ( i.hasNext() ) {
        vi = (Vertex)i.next();
        if ( graph.haveCommonEdge( vi, vl ) ) {
          j = graph.getVerticesIterator();
          while ( j.hasNext() ) {
            vj = (Vertex)j.next();
            if ( graph.haveCommonEdge(vl, vj) && !graph.haveCommonEdge(vi, vj))
              try {
                graph.addEdge( vi, vj );
              } catch (Exception e) {}
          }
        }
      }
    }

    return graph;
  }
}