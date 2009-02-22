/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Jesus M. Salvo, Ralf Vandenhouten
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph;


/**
 * A vertex in a graph. This class encapsulates an object that the vertex will represent.
 * Hence, a Vertex can represent any object that extends java.lang.Object by simply
 * calling setObject() or specifying the objet on the constructor.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */
public class Vertex implements GraphComponent {

  /**
    * The object that the vertex represents.
    */
  protected Object	object;

  /**
   * The string returned when toString() is called.
   */
  protected String    str;

  /**
    * Constructor that initializes this.object to null
    */
  public Vertex( ){
    object = null;
  }

  /**
    * Creates a new Vertex object that initializes this.object to newobject
    *
    * @param	newobject	The object that the Vertex will encapsulate
    */
  public Vertex( Object newobject ) {
    this.object = newobject;
    this.str = this.object.toString();
  }

  /**
    * Getter method that returns the object that the Vertex represents
    *
    * @return	The object that this Vertex encapsulates
    */
  public Object getObject( ){
    return object;
  }

  /**
    * Setter method sets this.object to newobject
    */
  public void setObject( Object newobject ){
    this.object = newobject;
  }


  public void setString( String string ) {
    this.str = string;
  }

  /**
    * If <tt>setString()</tt> has never been called, this
    * method then simply calls <tt>this.object.toString()</tt>.
    * Otherwise, it returns the string that was set during the
    * call to <tt>setString()</tt>.
    *
    * @return   String representation of the Vertex object
    */
  public String toString( ){
    if( this.str == null )
        return this.object.toString();
    else
        return this.str;
  }
}
