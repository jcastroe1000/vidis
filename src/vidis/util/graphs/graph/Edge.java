/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph;

/**
 * Represents an undirected edge in a graph.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */
public class Edge implements GraphComponent {
  /**
   * The A vertex of the edge.
   */
  protected Vertex  vertexA;

  /**
   * The B vertex of the edge.
   */
  protected Vertex  vertexB;

  /**
   * The string returned when toString() is called.
   */
  protected String    str;

  /**
    * Creates an UndirectedEdge object.
    *
    * @see		Vertex
    */
  public Edge( Vertex a, Vertex b ) {
    this.vertexA = a;
    this.vertexB = b;
    this.str = this.vertexA.toString() + "-" + this.vertexB.toString();
  }

  /**
   * Returns the endpoint A of the edge.
   *
   * @return  Vertex  Endpoint A of the edge.
   */
  public Vertex getVertexA() {
    return this.vertexA;
  }

  /**
   * Returns the endpoint B of the edge.
   *
   * @return  Vertex  Endpoint B of the edge.
   */
  public Vertex getVertexB() {
    return this.vertexB;
  }

  /**
   * Returns the Vertex opposite to the specified Vertex in the edge.
   *
   * @return  Vertex  The Vertex object that is the opposite to the specifid
   *                  Vertex. If the specified Vertex is not an endpoint of the
   *                  edge, returns null.
   */
  public Vertex getOppositeVertex( Vertex v ) {
    if( this.vertexA == v )
      return this.vertexB;
    else if( this.vertexB == v )
      return this.vertexA;
    else
      return null;
  }

  public void setString( String text ) {
    this.str = text;
  }

  /**
    * Returns a String representation of the Edge.
    * By default, the format is:
    * fromVertex.toString() + "->" + toVertex.toString()
    *
    * @return	The String representation of the Edge
    * @see		Vertex
    */
  public String toString(){
    return this.str;
  }

  /**
    * Creates a clone of this Edge. This calls the EdgeImpl constructor,
    * thereby creating a new instance of EdgeImpl. However, the vertices
    * in both endpoints of the Edge are not cloned.
    *
    * @return  A clone of an instance of Edge.
    */
  protected Object clone(){
    Edge edge = new Edge( vertexA, vertexB );
    edge.setString(this.str);
    return edge;
  }

}
