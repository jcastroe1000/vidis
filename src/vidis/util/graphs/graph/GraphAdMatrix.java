package vidis.util.graphs.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import vidis.util.graphs.graph.algorithm.DepthFirstGraphTraversal;
import vidis.util.graphs.graph.algorithm.GraphTraversal;

/**
 * An implementation of the Graph interface. A Graph object
 * represents a graph data structure, which are vertices
 * connected by edges, where the edges are non-directional.
 */
public class GraphAdMatrix implements Graph {

  /**
   *  boolean variable that is <tt>true</tt> if graph is directed
   */
  protected boolean isDirected = false;

  /**
    * List of vertices in the graph.
    */
  protected List vertices;

  /**
   * 2-dimensional array (matrix) that indicates if two vertices are adjacent.
   * Entry [i][j] is 1 if there is an edge from Vertex i to Vertex j, otherwise
   * it is 0.
   */
  protected boolean[][] admatrix;

  public GraphAdMatrix(boolean directed) {
    isDirected = directed;
    vertices = new ArrayList( 10 );
    admatrix = new boolean[0][0];
  }

  /**
   * Returns true if the Graph is directed.
   */
  public boolean isDirected() {
    return isDirected;
  }

  /**
   * Returns an iterator that iterates through the graph's vertices.
   *
   * @return  An iterator of List vertices.
   */
  public Iterator getVerticesIterator() {
    return this.vertices.iterator();
  }

  /**
   * Returns a clone of the List of vertices.
   *
   * @return  A clone of the List of vertices.
   */
  public List cloneVertices() {
    return (List) ((ArrayList) this.vertices).clone();
  }

  /**
   * Returns a List of edges of the specified vertex.
   *
   * @param   v   The vertex whose edges we want returned
   * @return  A List of Edges that are incident edges of the specified vertex.
   */
  public List getEdges( Vertex v ) {
    List    incidentedges = new ArrayList();
    int     index = this.vertices.indexOf( v );

    for (int i=0; i<admatrix[index].length; i++)
      if (admatrix[index][i])
        if (isDirected)
          incidentedges.add(new DirectedEdge((Vertex)vertices.get(index),
                                             (Vertex)vertices.get(i)));
        else
          incidentedges.add(new Edge((Vertex)vertices.get(index),
                                     (Vertex)vertices.get(i)));

    return incidentedges;
  }

  /**
   * Returns a set of all edges in the graph.
   *
   * @return  A Set of all Edges in the Graph.
   */
  public Set getAllEdges() {
    HashSet allEdges = new HashSet();
    if ( isDirected ) {
      for (int i=0; i<admatrix.length; i++)
        for (int j=0; j<admatrix.length; j++)
          if ( admatrix[i][j] )
            allEdges.add(new DirectedEdge((Vertex)vertices.get(i),
                                          (Vertex)vertices.get(j)));
    } else {
      for (int i=0; i<admatrix.length; i++)
        for (int j=i; j<admatrix.length; j++)
          if ( admatrix[i][j] )
            allEdges.add(new Edge((Vertex)vertices.get(i),
                                  (Vertex)vertices.get(j)));
    }
    return allEdges;
  }

  /**
    * Adds a Vertex into the Graph. This will also create a new entry
    * in the edges List and add the newly added Vertex to its own
    * connected set, thereby adding a new List in the connectedSet List.
    * Finally, all GraphAddVertexListeners are informed of the event that a
    * Vertex has been added to the Graph.
    *
    * @param		newvertex		Vertex to be added to the Graph
    */
  public void add( Vertex newvertex ) throws Exception {
    ArrayList   newconnectedSet;
    Iterator    iterator;

    // Add the vertex
    vertices.add( newvertex );

    // Whenever a new vertex is added, we also need to increase the size of the
    // adjacency matrix
    boolean[][] newmatrix = new boolean[admatrix.length+1][admatrix.length+1];
    for (int i=0; i<admatrix.length; i++)
      for (int j=0; j<admatrix.length; j++)
        newmatrix[i][j] = admatrix[i][j];
    admatrix = newmatrix;
  }

  /**
   * Returns <code>true</code> if there is an edge from v1 to v2.
   *
   * @param   v1    One endpoint of the edge
   * @param   v2    Other endpoint of the edge
   */
  public boolean haveCommonEdge( Vertex v1, Vertex v2 ) {
    int index1 = vertices.indexOf(v1);
    int index2 = vertices.indexOf(v2);
    return admatrix[index1][index2];
  }

  /**
   * Method to create the proper type of Edge class.
   *
   * @param   v1    One endpoint of the edge
   * @param   v2    Other endpoint of the edge
   */
  public Edge createEdge( Vertex v1, Vertex v2 ) {
    Edge edge;
    int index1 = vertices.indexOf(v1);
    int index2 = vertices.indexOf(v2);
    this.admatrix[index1][index2] = true;
    if ( !isDirected )
      this.admatrix[index2][index1] = true;
    if (this.isDirected)
        edge = new DirectedEdge( v1, v2 );
    else
        edge = new Edge( v1, v2 );
    return edge;
  }

  /**
    * Adds an Edge into the Graph. The vertices of the Edge need not already
    * be existing in the Graph for this method to work properly.
    * The vertices in both ends of the Edge are merged into one connected set,
    * thereby possibly decreasing the number of Lists in the coonectedSet List.
    * Finally, all GraphAddEdgeListeners are informed of the event that a
    * Edge has been added to the Graph.
    *
    * @param		v1	One endpoint of the edge
    * @param    v2  Other endpoint of the edge
    * @return   The Edge object created and added to the Graph.
    */
  public Edge addEdge( Vertex v1, Vertex v2 ) throws Exception {
    Edge    edge;
    List	v1edges, v2edges;
    Iterator	iterator;

    // If the vertices of the edge are not in the Graph, add them.
    if( !this.vertices.contains( v1 ) )
      this.add( v1 );
    if( !this.vertices.contains( v2 ) )
      this.add( v2 );

    int index1 = vertices.indexOf(v1);
    int index2 = vertices.indexOf(v2);
    this.admatrix[index1][index2] = true;
    if ( !this.isDirected )
      this.admatrix[index2][index1] = true;

    if (this.isDirected)
        edge = new DirectedEdge( v1, v2 );
    else
        edge = new Edge( v1, v2 );

    return edge;
  }

  /**
    * Adds an Edge into the Graph. The vertices of the Edge need not be
    * existing in the Graph for this method to work properly.
    * <p>
    * In the event that any one of the vertices are not existing in the Graph,
    * they are added to the Graph.
    *
    * @param	e   The edge to be added to the Graph.
    */
  public void addEdge( Edge edge ) throws Exception {
    Vertex      v1, v2;
    Iterator	iterator;
    List        v1edges, v2edges;

    v1 = edge.getVertexA();
    v2 = edge.getVertexB();

    // If the vertices of the edge are not in the Graph, add them.
    if( !this.vertices.contains( v1 ) )
      this.add( v1 );
    if( !this.vertices.contains( v2 ) )
      this.add( v2 );

    // Only then should we manipulate the admatrix, since add( Vertex ) will
    // initialise the matrix accordingly
    int index1 = vertices.indexOf(v1);
    int index2 = vertices.indexOf(v2);
    this.admatrix[index1][index2] = true;
    if ( !this.isDirected )
      this.admatrix[index2][index1] = true;
  }

  public void remove( Vertex v ) throws Exception {
    int i, j, k, l, len = this.vertices.size();

    // Remove all the edges of the vertex.
    this.removeEdges( v );

    // Remove the according row and column of the adjacency matrix
    int index = this.vertices.indexOf( v );
    boolean [][] newmatrix = new boolean[len-1][len-1];

    for (i=0; i<len; i++)
      for (j=0; j<len; j++) {
        if (i==index || j==index) continue;
        k = i<index ? i : i-1;
        l = j<index ? j : j-1;
        newmatrix[k][l] = admatrix[i][j];
      }
    admatrix = newmatrix;

    // Finally, remove the vertex
    this.vertices.remove( v );
  }

  /**
   * Removes the specified Edge from the Graph.
   *
   * @param   edge    The Edge object to be removed.
   */
  public void removeEdge( Edge edge ) throws Exception {
    Vertex v1, v2;
    v1 = edge.getVertexA();
    v2 = edge.getVertexB();
    int index1 = vertices.indexOf(v1);
    int index2 = vertices.indexOf(v2);
    this.admatrix[index1][index2] = false;
    if ( !isDirected )
      this.admatrix[index2][index1] = false;
  }

  /**
    * Removes incident Edges of a Vertex. The Edges removed are those whose
    * either endpoints has the specified vertex. This method is usually
    * called just prior to removing a Vertex from a Graph.
    *
    * @param		v	Vertex whose Edges are to be removed
    */
  public void removeEdges( Vertex v ) throws Exception {
    int index = vertices.indexOf( v );
    for (int i=0; i<admatrix.length; i++) {
      admatrix[index][i] = false;
      admatrix[i][index] = false;
    }
  }

  /**
    * Returns the number of vertices in the graph
    *
    * @return	The number of vertices in the graph.
    */
  public int getVerticesCount() {
    return this.vertices.size();
  }

  /**
   * Returns all vertices.
   *
   * @return  A collection containing all vertices.
   */
  public List getVertices() {
    return this.vertices;
  }

  /**
   * Returns all vertices with the specified degree.
   *
   * @param   degree    The degree of the vertex to be returned.
   * @return  A collection of vertices with the above specified degree.
   */
  public Set getVertices( int degree ) {
    Set       verticesofsamedegree = new HashSet();
    Iterator  iterator;
    Vertex    vertex;

    iterator = this.vertices.iterator();
    while( iterator.hasNext() ) {
      vertex = (Vertex) iterator.next();
      if( this.getAdjacentVertices( vertex ).size() == degree )
        verticesofsamedegree.add( vertex );
    }

    return verticesofsamedegree;
  }

  /**
    * Returns the vertices adjacent to the specified vertex.
    *
    * @param  v   The Vertex you want to determine its adjacent vertices.
    * @return	  List of vertices adjacent to the specified vertex v.
    */
  public List getAdjacentVertices( Vertex v ) {
    List adjacentVertices = new ArrayList( 10 );
    int index = this.vertices.indexOf( v );

    for (int i=0; i<admatrix[index].length; i++)
      if ( admatrix[index][i] || admatrix[i][index] )
        adjacentVertices.add( this.vertices.get(i) );

    return adjacentVertices;
  }

  /**
    * Returns the vertices adjacent to the specified vertex where the connecting
    * edge is directed from the adjacent vertex to the specified vertex.
    * This method is useful only for directed graphs.
    *
    * @param  v	  The Vertex you want to determine its adjacent vertices.
    * @return	  List of vertices adjacent to the specified vertex v.
    */
  public List getIncomingAdjacentVertices( Vertex v ) {
    List adjacentVertices = new ArrayList( 10 );
    int index = this.vertices.indexOf( v );

    for (int i=0; i<admatrix[index].length; i++)
      if ( admatrix[i][index] )
        adjacentVertices.add( this.vertices.get(i) );

    return adjacentVertices;
  }

  /**
    * Returns the vertices adjacent to the specified vertex where the connecting
    * edge is directed from the specified vertex to the adjacent vertex.
    * This method is useful only for directed graphs.
    *
    * @param  v   The Vertex you want to determine its adjacent vertices.
    * @return	  List of vertices adjacent to the specified vertex v.
    */
  public List getOutgoingAdjacentVertices( Vertex v ) {
    List adjacentVertices = new ArrayList( 10 );
    int index = this.vertices.indexOf( v );

    for (int i=0; i<admatrix[index].length; i++)
      if ( admatrix[index][i] )
        adjacentVertices.add( this.vertices.get(i) );

    return adjacentVertices;
  }

  /**
    * Returns the vertices adjacent to all the vertices in the given collection.
    *
    * @param		vertices		List of Vertex where each vertex in the returned Set
    *                       must be adjacent to.
    * @return	  Set of vertices adjacent to all the vertices in the supplied List.
    */
  public Set getAdjacentVertices( List vertices ) {
    HashSet   adjacentVertices = new HashSet( this.getAdjacentVertices( (Vertex) vertices.get(0)) );
    int       i, size = vertices.size();

    for( i = 1; i < size; i++ ) {
      adjacentVertices.retainAll( this.getAdjacentVertices( (Vertex) vertices.get(i)));
    }

    return adjacentVertices;
  }

  /**
   * Returns the degree of the graph, which is simply the highest degree
   * of all the graph's vertices.
   *
   * @return  An int indicating the degree of the graph.
   */
  public int getDegree( ) {
    Vertex  v;
    HashSet set;

    set = new HashSet( this.vertices );
    if( set.size() > 0 ){
      v = (Vertex) Collections.max( set, new Comparator() {
          public int compare( Object obj1, Object obj2 ) {
            Vertex v1 = (Vertex) obj1, v2 = (Vertex) obj2;
            int    countv1 = getDegree( v1 );
            int    countv2 = getDegree( v2 );

            if( countv1 < countv2 ) return -1;
            if( countv1 > countv2 ) return 1;
            else return 0;
          }
          public boolean equals( Object objcomparator ) {
          return objcomparator.equals( this );
          }
        }
      );
      return this.getEdges( v ).size();
      }
    else
      return 0;
  }

  /**
   * Returns the degree of the vertex, which is simply the number of edges
   * of the vertex.
   *
   * @return  The degree of the vertex.
   */
  public int getDegree( Vertex v ) {
    int count = 0;
    int index = this.vertices.indexOf( v );
    for (int i=0; i<admatrix.length; i++)
      if ( admatrix[index][i] || admatrix[i][index] )
        count++;

    return count;
  }

  /**
   * Returns a String representation of the Graph. The string returned in the form:
   * "Vertices: " + this.vertices.toString() + "\n" + "Edges: " + this.edges.toString()
   *
   * @return	String representation of the Graph
   */
  public String toString() {
    String str = "Vertices: " + this.vertices.toString() + "\n" +
      "Edges: " + this.getAllEdges().toString() + "\n" +
      "Adjacency matrix:\n";
    for (int i=0; i<admatrix.length; i++) {
      for (int j=0; j<admatrix.length; j++)
        str = str + (int)(admatrix[i][j]?1:0) + " ";
      str += "\n";
    }
    return str;
  }

  /**
   * Determines if there is a path from Vertex fromVertex to Vertex toVertex.
   * This will not return true if the only path has at least one Edge pointing
   * in the opposite direction of the path.
   *
   * @param		fromVertex		starting Vertex for the path
   * @param		toVertex			ending Vertex for the path
   * @return	true if there is a path from Vertex to toVertex. false otherwise.
   */
  public boolean isPath( Vertex fromVertex, Vertex toVertex ){
    List  visited = new ArrayList( 10 );
    GraphTraversal traversal = new DepthFirstGraphTraversal(this);

    traversal.traverse( fromVertex, visited, new StopAtVisitor( toVertex ));
    if( toVertex == (Vertex) visited.get( visited.size() - 1 ) )
      return true;
    else
      return false;
  }
}

