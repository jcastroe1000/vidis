/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph.algorithm;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import vidis.util.graphs.graph.Graph;
import vidis.util.graphs.graph.Vertex;

/**
 * Class for coloring a graph using a back tracking algorithm.
 * Extends the abstract GraphColoring class.
 *
 * @author Ralf Vandenhouten
 * @version 1.0 2002/09/29
 */

public class BackTrackColoring extends GraphColoring {
    /**
     * A list of the vertices of the graph.
     */
    protected List vertices;

    /**
     * Constructor for BackTrackColoring objects
     */
    public BackTrackColoring( Graph graph ) {
        super( graph );
        vertices = graph.cloneVertices();
    }

    /**
     * Method that performs a minimum coloring by a binary search that calls
     * coloring(maxColor) iteratively, where the binary search finds the optimum
     * value for maxColor.
     *
     * @return The HashMap containing the color mapping of the vertices.
     */
    public Map coloring() {
      Map result;
      int lower=1, upper=vertices.size(), middle;

      while ( lower < upper ) {
        middle = (lower+upper)/2;
        result = null;
        try {
            result = coloring( middle );
        } catch (NotEnoughColorsException e) {}
        if ( result != null )
            if ( lower == middle )
                return result;
            else
                upper = middle;
        else
            if ( lower == middle )
                try {
                    return coloring( lower+1 );
                } catch (NotEnoughColorsException e) { return null; }
            else
                lower = middle;
      }
      return null;
    }

    /**
     * Coloring method using a back tracking algorithm.
     *
     * @param maxNumOfColors The maximum number of colors to be used for coloring.
     *          If that is not enough, a NotEnoughColorsException is thrown.
     *
     * @return The HashMap containing the color mapping of the vertices.
     */
    public Map coloring( int maxNumOfColors )
    throws NotEnoughColorsException {
        if ( !tryColor( 0, maxNumOfColors ) )
          throw new NotEnoughColorsException("Backtracking needs more than "+
                                             maxNumOfColors +" colors.");
        else
          return colorMap;
    }

    /**
     * Recursive helper method for coloring() that actually performs the
     * back tracking.
     */
    private boolean tryColor(int i, int maxColor) {
        int color = 0;
        Integer theColor;
        Vertex v;
        boolean quit = false;

        do {
            theColor = new Integer(++color);
            if ( possible(i, theColor) ) {
                v = (Vertex)vertices.get(i);
                colorMap.put( v, theColor );
                if ( i+1 < vertices.size() ) {
                    quit = tryColor( i+1, maxColor ); // recursive call
                    if ( !quit ) {
                        colorMap.remove( v );
                    }
                } else return true;
            }
        } while ( !(quit || color==maxColor) );
        return quit;
    }

    /**
     * Helper method for tryColor() that checks if one of the neighbors has
     * already the current color.
     */
    private boolean possible(int i, Integer color) {
        Vertex v = (Vertex)vertices.get(i);
        Iterator nb = graph.getAdjacentVertices( v ).iterator();
        while ( nb.hasNext() ) {
            Integer col = (Integer)colorMap.get( nb.next() );
            if ( col != null )
                if ( col.equals(color) ) // neighbor has the same color -> bad!
                    return false;
        }
        return true;
    }
}