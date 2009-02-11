/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.util.graphs.graph.algorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import vidis.util.graphs.graph.DirectedWeightedEdge;
import vidis.util.graphs.graph.Edge;
import vidis.util.graphs.graph.Vertex;
import vidis.util.graphs.graph.WeightedGraph;

/**
 * Abstract class for implementing the shortest path algorithm.
 * A shortest path spanning tree is a subgraph of the original
 * weighted graph showing how to reach all other vertices from
 * a given vertex in the same connected set in the shortest possible way.
 * The shortest path between two vertices should be such that the sum
 * of the weights of all the edges between the two vertices be at a minimum.
 * Note that, like minimum spanning trees, there may be more than one
 * shortest spanning tree for a single weighted graph.
 *
 * Concrete subclasses must never modify the weighted graph where
 * it is computing the shortest path.
 *
 * @author Jesus M. Salvo Jr., Ralf Vandenhouten
 * @version 1.0 2002-10-20
 */

public abstract class ShortestPathAlgorithm implements Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = -2013586716116971828L;

/**
   * The WeightedGraph object that the algorithm uses to determine
   * the shortest path spanning tree. Concrete subclasses must implement wgraph
   * as a directed (!) WeightedGraph.
   */
  WeightedGraph   wgraph;

  /**
   * Start vertex of the shortest path tree.
   */
  Vertex startVertex = null;

  /**
   * The WeightedGraph that represents the shortest path spanning tree.
   */
  WeightedGraph shortestpathtree;

  public ShortestPathAlgorithm( WeightedGraph wgraph ) {
    this.wgraph = wgraph;
  }

  /**
   * Wrapper method that sets the start vertex and calls shortestPath() for
   * computing the shortest path spanning tree.
   *
   * @param from The root vertex of the shortest path spanning tree.
   * @return A new WeightedGraph that represents the shortest path spanning
   * tree of the original WeightedGraph.
   */
  public WeightedGraph shortestPath( Vertex from ) {
    startVertex = from;
    shortestpathtree = shortestPath();
    return shortestpathtree;
  }

  /**
   * Abstract method to be implemented by subclasses to determine
   * a shortest path spanning tree from a given vertex in the form
   * of a graph.
   *
   * @return  A new WeightedGraph that represents the shortest path spanning
   * tree of the original WeightedGraph. <b>Do not</b> modify the contents
   * of the returned WeightedGraph.
   */
  public abstract WeightedGraph shortestPath();

  /**
   * This method returns the shortest path between two vertices as a list of the
   * vertices the path consists of.
   *
   * @param v1 The start vertex of the path
   * @param v2 The target vertex of the path
   * @return The List object containing the vertices of the path.
   */
  public List<Vertex> getShortestPath( Vertex v1, Vertex v2 ) {
    if ( shortestpathtree==null || v1!=startVertex )
        shortestpathtree = shortestPath( v1 );
    List<Vertex> vlist = new ArrayList<Vertex>( shortestpathtree.getVerticesCount() );
    Map<Vertex,Vertex> predecessor = new HashMap<Vertex,Vertex>();
    Iterator<Edge> it = shortestpathtree.getAllEdges().iterator();
    // Generate the predecessor list by stepping through the edges
    while ( it.hasNext() ) {
        Edge e = (Edge)it.next();
        predecessor.put( e.getVertexB(), e.getVertexA() );
    }
    // Generate the path by stepping backward through the predecessor list
    Vertex pred = v2;
    while ( pred != v1 && pred != null ) {
        vlist.add(0, pred);
        pred = (Vertex)predecessor.get( pred );
    }
    if ( pred == v1 ) {
        vlist.add( 0, pred );
        return vlist;
    } else {
        return null;
    }
  }

  /**
   * Compute the distance between two vertices in the shortest path spanning
   * tree.
   *
   * @param v1 The start vertex
   * @param v2 The target vertex
   * @return The distance between v1 and v2 in the shortest path spanning tree
   *         if v2 is reachable from v1, otherwise Double.POSITIVE_INFINITY
   *         is returned.
   */
  public double getDistance( Vertex v1, Vertex v2 ) {
    if ( shortestpathtree==null || v1!=startVertex )
        shortestpathtree = shortestPath( v1 );
    return getDistanceInTree( v1, v2 );
  }

  /**
   * Recursive method used by getDistance(v1,v2) for finding partial distances
   * of two vertices in the shortest path spanning tree.
   *
   * @param v1 The start vertex
   * @param v2 The target vertex
   * @return The distance between v1 and v2, if v2 is a successor of v1,
   *         otherwise Double.POSITIVE_INFINITY is returned.
   */
  public double getDistanceInTree( Vertex v1, Vertex v2 ) {
    if ( v1 == v2 )
        return 0.0;
    // recursive depth-first search for v2
    Iterator<Edge> it = shortestpathtree.getEdges( v1 ).iterator();
    while ( it.hasNext() ) {
        DirectedWeightedEdge e = (DirectedWeightedEdge)it.next();
        if ( e.getVertexB() == v1 ) // skip incoming edges
            continue;
        double distance = getDistanceInTree( e.getVertexB(), v2 );
        if ( distance != Double.POSITIVE_INFINITY )
            return distance + e.getWeight();
    }
    return Double.POSITIVE_INFINITY;
  }

  /**
   * Method that computes the longest distance of any vertex from the start
   * vertex of the shortest path spanning tree.
   *
   * @param v1 The start vertex
   * @return The longest distance between v1 and any other vertex in the
   *         shortest path spanning tree.
   */
  public double getLongestDistance( Vertex v1 ) {
    if ( shortestpathtree==null || v1!=startVertex )
        shortestpathtree = shortestPath( v1 );
    return getLongestDistanceInTree( v1 );
  }

  /**
   * Recursive method used by getLongestDistance(v1) for finding the longest
   * distance of the subtree starting at vertex v1
   *
   * @param v1 The root vertex of the subtree considered.
   * @return The longest distance between v1 any other vertex in the subtree
   *         of v1.
   */
  public double getLongestDistanceInTree( Vertex v1 ) {
    double max = 0.0;
    // recursive depth-first search
    Iterator<Edge> it = shortestpathtree.getEdges( v1 ).iterator();
    while ( it.hasNext() ) {
        DirectedWeightedEdge e = (DirectedWeightedEdge)it.next();
        if ( e.getVertexB() == v1 ) // skip incoming edges
            continue;
        double distance = e.getWeight() + getLongestDistanceInTree( e.getVertexB() );
        if ( distance > max )
            max = distance;
    }
    return max;
  }

  /**
   * Method that computes the routing table corresponding to a shortest path
   * spanning tree as a map of target vertices to neighbor vertices.
   *
   * @param v The node of which the routing table is of interest.
   * @return A Map object that maps a target vertex of the spanning tree to the
   *    neighboring node of the vertex v that is used for routing to this
   *    target.
   */
  public Map getRoutingTable( Vertex v ) {
    if ( shortestpathtree==null || v!=startVertex )
        shortestpathtree = shortestPath( v );
    Map routingMap = new HashMap();
    /*
        Aufgabe: Hier fehlt noch der eigentliche Algorithmus!
        Kleiner Tipp: Am besten definiert man sich eine rekursive Hilfsroutine
        (aehnlich wie oben fuer getLongestDistance() ).
    */
    return routingMap;
  }
}
