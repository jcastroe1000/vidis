/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph;

/**
 * A <tt>Visitor</tt> that notifies a traversal to stop at a particular <tt>Vertex</tt>.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */

class StopAtVisitor extends NullVisitor {
  /**
    * Vertex to check for when visiting a vertex
    */
  Vertex	objectToCheck;

  /**
    * Creates a new instance of StopAtVisitor and specifies
    * which Vertex stop
    *
    * @param		objectToCheck		stop at the specified vertex
    */
  StopAtVisitor( Vertex objectToCheck ){
    super();
    this.objectToCheck = objectToCheck;
  }

  /**
    * Override of superclass' visit() method. Compares the Vertex
    * being visited to the Vertex specified in the constructor.
    * If they are the same, return false. Otherwise, return true.
    *
    * @param	objectToVisit		Vertex being visited.
    * @return	false if the Vertex being visited is the same as the
    * Vertex specified in the constructor. True otherwise.
    */
  public boolean visit( Vertex objectToVisit ){
    if( objectToVisit == objectToCheck )
      return false;
    else
      return true;
  }

}
