package vidis.util.graphs.graph.algorithm;

import java.util.Iterator;
import java.util.Map;

import vidis.util.graphs.graph.Graph;
import vidis.util.graphs.graph.Vertex;

/**
 * Class for coloring a graph using the greedy algorithm.
 * Extends the abstract GraphColoring class.
 *
 * @author Ralf Vandenhouten
 * @version 1.0 2002/09/29
 */

public class GreedyColoring extends GraphColoring {

    /**
     * Constructor for GreedyColoring objects
     */
    public GreedyColoring( Graph graph ) {
        super( graph );
    }

    /**
     * Coloring method using the simple greedy algorithm.
     *
     * @param maxNumOfColors The maximum number of colors to be used for coloring.
     *          If that is not enough, a NotEnoughColorsException is thrown.
     *
     * @return The HashMap containing the color mapping of the vertices.
     */
    public Map coloring( int maxNumOfColors )
    throws NotEnoughColorsException {
        int count=0, color=0, n = graph.getVerticesCount();

        while ( count < n ) {
            color++;
            if ( color > maxNumOfColors )
                throw new NotEnoughColorsException(
                    "Greedy needs more than "+ maxNumOfColors +" colors.");
            Integer currentColor = new Integer(color);
            Integer blockedColor = new Integer(-color);
            Iterator vertices = graph.getVerticesIterator();
            while ( vertices.hasNext() ) {
                Vertex v = (Vertex)vertices.next();
                Integer mapped = (Integer)colorMap.get( v );
                if ( mapped != null && mapped != blockedColor ) {
                    if ( mapped.intValue() < 0 ) // Vertex still without color
                        mapped = null;
                }
                if ( mapped == null ) { // Vertex can be colored with color
                    colorMap.put( v, currentColor ); // paint v with curr. color
                    count++;
                    Iterator nb = graph.getAdjacentVertices( v ).iterator();
                    while ( nb.hasNext() ) { // Block all neighbors for color
                        Vertex w = (Vertex)nb.next();
                        mapped = (Integer)colorMap.get( w );
                        if ( mapped != null )
                            if ( mapped.intValue() > 0 ) // w already colored
                                continue;
                        colorMap.put( w, blockedColor ); // block w for color
                    }
                }
            }
        }
        return this.colorMap;
    }
}