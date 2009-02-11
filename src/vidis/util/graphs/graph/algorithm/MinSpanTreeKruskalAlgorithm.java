/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import vidis.util.graphs.graph.Edge;
import vidis.util.graphs.graph.Vertex;
import vidis.util.graphs.graph.WeightedEdge;
import vidis.util.graphs.graph.WeightedGraph;
import vidis.util.graphs.graph.WeightedGraphImpl;

/**
 * Kruskal's algorithm for determining a minimum spanning tree of a graph.
 *
 * @author Ralf Vandenhouten
 * @version 1.0 2002/09/25
 */

public class MinSpanTreeKruskalAlgorithm {

  /**
   * The weighted graph.
   */
  protected WeightedGraph wgraph;

  /**
   * Creates an instance of MinimumSpanningTreeKruskalAlgorithm
   *
   * @param wgraph  The WeightedGraph where the minimum spanning tree will be determined.
   */
  public MinSpanTreeKruskalAlgorithm( WeightedGraph wgraph ) {
    this.wgraph = wgraph;
  }

  /**
   * Determine the minimum spanning tree of a weighted graph using Kruskal's method.
   */
  public WeightedGraph minimumSpanningTree() {
    Vertex    v1, v2;
    Iterator  iterator;
    Edge      edge;
    int       numVertices = wgraph.getVerticesCount();
    int       treeSize = 0;
    WeightedGraph  spanningtree = new WeightedGraphImpl(false);
    HashSet   allEdges = new HashSet();
    // HashMap fuer Zahl der verbundenen Ecken zu jeder Ecke
    HashMap numConnected = new HashMap();
    // Verkettete Liste zusammenhaengender Ecken wird ebenfalls als HashMap
    // dargestellt, da einfacher zu handhaben. Jede Ecke wird durch die HashMap
    // auf ihren "Nachfolger" abgebildet (zyklisch).
    HashMap nextInConnectedSet = new HashMap();
    // Zusammenhangskomponente jeder Ecke wird auch als HashMap dargestellt.
    // Die Z.k. wird durch ihre "erste" Ecke identifiziert.
    HashMap connectedSet = new HashMap();

    // Initialisiere die drei HashMaps
    iterator = wgraph.getVerticesIterator();
    while ( iterator.hasNext() ) {
        v1 = (Vertex)iterator.next();
        numConnected.put(v1, new Integer(1));
        nextInConnectedSet.put(v1, v1);
        connectedSet.put(v1, v1);
    }

    // Sammle alle Kanten des Graphs. Die Klasse HashSet sorgt dafuer, dass
    // keine Kante doppelt aufgenommen wird.
    allEdges.addAll( wgraph.getAllEdges() );

    // Copy the HashSet to a List so we can sort the edges using
    // the sort() method of the Collections class.
    List listEdges = new ArrayList( allEdges );
    Collections.sort( listEdges,
      new Comparator() {
        public int compare( Object obj1, Object obj2 ) {
          WeightedEdge edge1 = (WeightedEdge) obj1;
          WeightedEdge edge2 = (WeightedEdge) obj2;

          if( edge1.getWeight() < edge2.getWeight() )
            return -1;
          else if( edge1.getWeight() > edge2.getWeight() )
            return 1;
          else
            return 0;
        }

        public boolean equals( Object obj ) {
          return obj.equals( this );
        }
      });

    iterator = listEdges.iterator();
    // For each edge ...
    while( iterator.hasNext() && treeSize < numVertices-1 ) {
      edge = (WeightedEdge) iterator.next();

      // Test the feasibility of adding an edge into the spanning tree
      v1 = edge.getVertexA();
      v2 = edge.getVertexB();

      // Falls v1 und v2 noch nicht zusammenhaengen, baue die Ecke in den
      // spanning tree ein und aktualisiere die HashMaps
      if ( connectedSet.get(v1) != connectedSet.get(v2) ) {
        try {
            spanningtree.addEdge( edge );
            treeSize++;
        } catch (Exception e) { e.printStackTrace(); }

        // Finde Vertex mit kleinerer/groesserer Zusammenhangskomponente
        Vertex min, max, v;
        int i1, i2;
        i1 = ((Integer)numConnected.get(connectedSet.get(v1))).intValue();
        i2 = ((Integer)numConnected.get(connectedSet.get(v2))).intValue();
        if ( i1 < i2 ) {
            min = v1;
            max = v2;
        } else {
            min = v2;
            max = v1;
        }

        // Berechne Groesse der neuen Zusammenhangskomponente
        numConnected.put(connectedSet.get(max), new Integer( i1 + i2 ));

        // Ordne alle Ecken der kleineren Zus.komp. der groesseren zu (durch
        // zyklisches Durchlaufen der "Zusammenhangs-HashMap")
        max = (Vertex)connectedSet.get( max );
        v = min;
        do {
            connectedSet.put( v, max );
            v = (Vertex)nextInConnectedSet.get( v );
        } while ( v != min );

        // Vertausche die Nachfolger von v1 und v2, um die beiden zyklischen
        // Zusammenhangslisten zusammenzufuehren
        v = (Vertex)nextInConnectedSet.get( v1 );
        nextInConnectedSet.put( v1, nextInConnectedSet.get( v2 ) );
        nextInConnectedSet.put( v2, v );
      }
    }

    return spanningtree;
  }
}