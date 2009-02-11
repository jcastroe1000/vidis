/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * An implementation of the WeightedGraph interface where all
 * edges in the graph have a weight.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */

public class WeightedGraphImpl extends GraphImpl implements WeightedGraph {

  /**
   * Creates a new instance of WeightedGraphImpl. Default algorithm
   * for minimum spanning tree will use Kruskal's method
   * (MinimumSpanningTreeKruskalAlgorithm).
   */
  public WeightedGraphImpl(boolean isDirected) {
    super(isDirected);
  }

  /**
   * Creates an instance of a WeightedEdge.
   *
   * @param   v1    One endpoint of the edge
   * @param   v2    The other endpoint of the edge
   */
  public Edge createEdge( Vertex v1, Vertex v2 ) {
    Edge edge;
    if (this.isDirected)
        edge = new DirectedWeightedEdge( v1, v2, 0 );
    else
        edge = new WeightedEdge( v1, v2, 0 );
    return edge;
  }

  /**
   * Convenience method to add a WeightedEdge with a specified weight
   * into the WeightedGraph. The default addEdge( v1, v2 ) will add a
   * WeightedEdge with zero weight, after which you can call setWeight()
   * to specify the weight.
   *
   * @return  The WeightedEdge that has been added.
   */
  public WeightedEdge addEdge( Vertex v1, Vertex v2, double weight ) throws Exception {
    WeightedEdge  edge = new WeightedEdge( v1, v2, weight );
    addEdge( edge );
    return edge;
  }

  /**
   * Determines the Vertex that is 'closest' to the Vertex specified.
   * The definition of the closest vertex in this context is a
   * vertex that is directly adjacent to Vertex v where the edge
   * has the least weight.
   *
   * @return  The Vertex closes to Vertex v.
   */
  public Vertex getClosest( Vertex v ) {
    // If the vertex has no edges, return null
    if( this.getEdges( v ).size() == 0 )
      return null;

    // Specify a comparator to sort the adjacent edges by their weights
    TreeSet   set = new TreeSet(
      new Comparator() {
        public int compare( Object obj1, Object obj2 ) {
          WeightedEdge edge1 = (WeightedEdge) obj1;
          WeightedEdge edge2 = (WeightedEdge) obj2;

          if( edge1.getWeight() < edge2.getWeight() )
            return -1;
          else if( edge1.getWeight() > edge2.getWeight() )
            return 1;
          else
            return 0;
        }

        public boolean equals( Object obj ) {
          return obj.equals( this );
        }

      });

    set.addAll( this.getEdges( v ));
    Edge e = (Edge) set.first();
    return e.getOppositeVertex( v );
  }
}