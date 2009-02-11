/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph;

/**
 * Thrown whenever a <tt>Tree</tt> becomes malformed as a result
 * of calling a method that is declared on its superinterface
 * but is not supported.
 * 
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 */

public class IllegalTreeException extends GraphException {

    public IllegalTreeException() {
        super();
    }

    public IllegalTreeException( String msg ) {
        super( msg );
    }
}