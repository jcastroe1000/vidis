/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.util;

import java.io.Serializable;

/**
 * A node in a <tt>Heap</tt>, encapsulating the actual object represented by
 * the noode plus the priority of the node in the heap.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */

public class HeapNode implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = -8448344229851058917L;

/**
   * The actual object encapsulated by the <tt>HeapNode</tt>.
   */
  Object    node;

  /**
   * The priority of the <tt>HeapNode</tt>
   */
  double    priority;

  /**
   * Creates a <tt>HeapNode</tt> with a specified priority and encapsulating
   * an object.
   *
   * @param   priority  The priority of the node in the heap, determining the
   *                    position of the node within the heap.
   * @param   node      The actual Object encapsulated by the node.
   */
  public HeapNode( Object node, double priority ) {
    this.node = node;
    this.priority = priority;
  }


  /**
   * Returns the object encapsultaed by this <tt>HeapNode</tt>.
   */
  public Object getObject() {
    return this.node;
  }

  /**
   * Returns the priority of the <tt>HeapNode</tt> within the <tt>Heap</tt>.
   */
  public double getPriority() {
    return this.priority;
  }

  /**
   * Modifies the priority of the <tt>HeapNode</tt> within the <tt>Heap</tt>.
   * <p>
   * This method simply sets the <tt>priority</tt> attribute of the node.
   * This will not necessarily fixup the <tt>Heap</tt> such that this
   * <tt>HeapNode</tt> will be in its new position. To achieve that effect,
   * call <tt>Heap.setPriority( HeapNode )</tt>.
   */
  public void setPriority( double priority ) {
    this.priority = priority;
  }

  /**
   * Returns a <tt>String</tt> representation of this <tt>HeapNode</tt>.
   */
  public String toString() {
    return "Node: " + node + "; Weight: " + this.priority;
  }
}
