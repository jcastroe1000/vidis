/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph;


/**
 * A interface for a Graph where all edges have a specified weight.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */

public interface WeightedGraph extends Graph {

  /**
   * Convenience method to add a WeightedEdge with a specified weight
   * into the WeightedGraph. The default addEdge( v1, v2 ) will add a
   * WeightedEdge with zero weight, after which you can call setWeight()
   * to specify the weight.
   *
   * @return  The WeightedEdge that has been added.
   */
  public WeightedEdge addEdge( Vertex v1, Vertex v2, double weight ) throws Exception;

  /**
   * Determines the Vertex that is 'closest' to the Vertex specified.
   * The definition of the closest vertex in this context is a
   * vertex that is directly adjacent to Vertex v where the edge
   * has the least weight.
   *
   * @return  The Vertex closes to Vertex v.
   */
  public Vertex getClosest( Vertex v );
}