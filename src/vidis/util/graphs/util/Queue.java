/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Jesus M. Salvo, Ralf Vandenhouten
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.util;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * A Queue represents a first-in-first-out (FIFO) data structure.
 * Unlike java.lang.Stack that simply extends java.util.Vector,
 * this class does not extend java.util.LinkedList but delegates its methods
 * to a java.util.LinkedList. Therefore, unlike java.util.Stack,
 * the methods from java.util.LinkedList are not available.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */

public class Queue implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = -1408917970948901855L;
List<Object>  delegate;

  /**
   * Creates an empty queue
   */
  public Queue() {
    this.delegate = new LinkedList<Object>();
  }

  /**
   * Puts an item at the end of the queue. Note that this will not
   * check of duplicate items in the queue.
   *
   * @param item    The item to be added at the end of the queue
   */
  public void put( Object item ) {
    this.delegate.add( item );
  }

  /**
   * Gets and removes the item at the beginning of the queue
   *
   * @return  The item at the beginning of the queue.
   */
  public Object get( ) throws EmptyQueueException {
    try {
      return this.delegate.remove( 0 );
    }
    catch( ArrayIndexOutOfBoundsException e ) {
      throw new EmptyQueueException();
    }
  }

  /**
   * Tests if the queue is empty
   *
   * @return  True if the queue is empty, false otherwise.
   */
  public boolean isEmpty() {
    return this.delegate.isEmpty();
  }

  /**
   * Tests if the item is in the queue. Because the Queue allows duplicate
   * items, isQueued() will return true even if the item was removed from
   * the queue if the item was queued twice before being removed from the queue.
   *
   * @return  True if the item is in the queue, false otherwise.
   */
  public boolean isQueued( Object item ) {
    return this.delegate.contains( item );
  }

  /**
   * Returns a String representation of the queue. This simply calls
   * the toString() method of the delegate, which is an <tt>ArrayList</tt>.
   */
  public String toString() {
    return this.delegate.toString();
  }
}