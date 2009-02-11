/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs;

import vidis.util.graphs.graph.Vertex;
import vidis.util.graphs.graph.WeightedGraph;
import vidis.util.graphs.graph.WeightedGraphImpl;
import vidis.util.graphs.graph.algorithm.ShortestPathAlgorithm;
import vidis.util.graphs.graph.algorithm.ShortestPathAlgorithmDijkstra;
import vidis.util.graphs.util.HeapNodeComparator;

public class TestDijkstra {

  public TestDijkstra() throws Exception {
    WeightedGraph graph = new WeightedGraphImpl( false ); // ungerichtet
    Vertex  u1, u2, u3, u4, u5, u6;

    // Beispielgraph fur Dijkstra-Algorithmus aus der Vorlesung
    
    u1 = new Vertex( "u1" );
    u2 = new Vertex( "u2" );
    u3 = new Vertex( "u3" );
    u4 = new Vertex( "u4" );
    u5 = new Vertex( "u5" );
    u5 = new Vertex( "u5" );
    u6 = new Vertex( "u6" );

  
    graph.add( u1 );
    graph.add( u2 );
    graph.add( u3 );
    graph.add( u4 );
    graph.add( u5 );
    graph.add( u6 );

    graph.addEdge( u1, u2, 100 );
    graph.addEdge( u2, u3, 150 );
    graph.addEdge( u2, u5, 40 );
    graph.addEdge( u2, u4, 200 );
    graph.addEdge( u4, u3, 30 );
    graph.addEdge( u3, u6, 70 );
    graph.addEdge( u5, u6, 120 );
   

    // Teste Dijkstra-Algorithmus
    System.out.println("************ Ungerichteter Graph ******************");
    System.out.println("Graph:\n" + graph);

    System.out.println("************ Dijkstra ******************");
    ShortestPathAlgorithm spa =
        new ShortestPathAlgorithmDijkstra( graph, new HeapNodeComparator(-1) );
    WeightedGraph shortestPathTree = spa.shortestPath( u1 );
    System.out.println("Shortest Path Tree:\n" + shortestPathTree);
    System.out.println("Distance between vertex u1 and u6: " +
                       spa.getDistance( u1, u6 ));
    System.out.println("Longest distance in shortest path tree: " +
                       spa.getLongestDistance( u1 ));
  }
}