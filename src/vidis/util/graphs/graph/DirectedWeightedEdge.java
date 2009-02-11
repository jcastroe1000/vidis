/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph;

/**
 * Represents a directed and weighted edge in a graph.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */
public class DirectedWeightedEdge extends DirectedEdge {

  /**
   * The weight of the edge.
   */
  protected double weight;

  /**
   * Creates a DirectedWeightedEdge object whose source and sink vertices and
   * weight are specified by the parameters.
   */
  public DirectedWeightedEdge( Vertex sourceVertex, Vertex sinkVertex, double weight ) {
    // Create an Edge object.
    super( sourceVertex, sinkVertex );
    this.weight = weight;
  }

  /**
    * Returns a String representation of the DirectedWeightedEdge.
    *
    * @return	The String representation of the Edge
    * @see		Vertex
    */
  public String toString(){
    return this.getSource().toString() + "->" + this.getSink().toString() + " (" + this.weight +")";
  }

  /**
   * Returns the weight of the edge.
   */
  public double getWeight() {
    return this.weight;
  }

  /**
   * Sets the weight of the edge.
   *
   * @param   weight    The new weight of the edge
   */
  public void setWeight( double weight ) {
    this.weight = weight;
  }
}
