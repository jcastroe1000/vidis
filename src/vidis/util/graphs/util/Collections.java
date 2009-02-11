/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.util;

import java.util.*;

/**
 * A collections class.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */
public class Collections {

  /**
   * Returns a subset of the specified collection whose elements match a given object
   * using a specified Comparator. Only elements in the collection that match the
   * given object compareTo using the specified comparator are returned in the subset.
   *
   * @param   c   Collection object from which a subset will be taken.
   * @param   compareTo   Object to which elements of the collection will be compared against.
   * @param   comparator  Comparator object determining how elements in the collection will
   *                      be compared to the compareTo object.
   * @return  Collection object that is a subset of the collection specified in the argument.
   */
  public static Set subset( Collection c, Object compareTo, Comparator comparator ) {
    Set       subset = new HashSet();
    Iterator  iterator = c.iterator();
    Object    currentobject;

    while( iterator.hasNext() ) {
      currentobject = iterator.next();
      if( comparator.compare( compareTo, currentobject ) == 0 )
        c.add( currentobject );
    }

    return subset;
  }

  /**
   * Returns true if the specified Collection has an object that matches
   * the argument object compareTo using the given Comparator. This therefore
   * does use obj1.equals( obj2 ) but uses comparator.compare( obj1, obj2 ).
   *
   * @param   c   Collection object from which a subset will be taken.
   * @param   compareTo   Object to which elements of the collection will be compared against.
   * @param   comparator  Comparator object determining how elements in the collection will
   *                      be compared to the compareTo object.
   * @return  boolean   True if the Collection has a matching object
   */
  public static Object contains( Collection c, Object compareTo, Comparator comparator ) {
    Set       subset = new HashSet();
    Iterator  iterator = c.iterator();
    Object    currentobject;

    while( iterator.hasNext() ) {
      currentobject = iterator.next();
      if( comparator.compare( compareTo, currentobject ) == 0 )
        return currentobject;
    }

    return null;
  }

}
