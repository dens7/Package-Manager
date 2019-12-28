////////////////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION ////////////////////////////////
//
// Title: Package Manager
// Files: GraphADT.java, Graph.java, GraphTest.java, PackageManagerTest.java, PackageManager.java
//        valid.json, shared_dependencies.json, cyclic.json
// Course: CS400 Spring 2019
//
// Author: Vedaant Tambi
// Email: tambi@wisc.edu
// Lecturer's Name: Deb Deppeler
//
/////////////////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION /////////////////////////////
//                                                None
//////////////////////////////////////// CREDIT OUTSIDE HELP ///////////////////////////////////////
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This class creates an instance of a directed and unweighted graph implementation and implements
 * the graphADT.java
 * 
 * @author Vedaant Tambi
 *
 */
public class Graph implements GraphADT {


  private HashMap<String, LinkedList<String>> adjList; // HashMap used to store adjacency list
  private HashSet<String> vertices; // HashSet used to store all vertices
  private int numVertices; // number of vertices in the graph
  private int numEdges; // number of edges in the graph

  /**
   * This creates an instance of a graph annd initializes the respective data fields
   */
  public Graph() {
    // initialization of data fields
    adjList = new HashMap<String, LinkedList<String>>();
    vertices = new HashSet<String>();
    numVertices = 0; // the graph no vertices or edges
    numEdges = 0;
  }

  /**
   * This method adds a new vertex to the graph. If vertex is null or already exists, method ends
   * without adding a vertex or throwing an exception.
   * 
   * @param vertex must not be null and should not be already present in the graph
   */
  public void addVertex(String vertex) {
    if (vertex == null) // if the parameter is null, the the method does nothing
      return;
    if (!vertices.contains(vertex)) { // checks if the given vertex is already present in the set
      vertices.add(vertex); // if the vertex is not already present, then it is added to the set
      adjList.put(vertex, new LinkedList<String>()); // vertex is added to the adjacency list
      numVertices += 1; // numvertices is incremented
    }
  }

  /**
   * This method removes a vertex and all associated edges from the graph. If vertex is null or does
   * not exist, method ends without removing a vertex, edges, or throwing an exception.
   * 
   * @param vertex should not be null and should not be already in the graph
   */
  public void removeVertex(String vertex) {
    if (vertex == null) // if the parameter is null, the the method does nothing
      return;
    if (hasVertex(vertex)) { // checks if the vertex is present in the set
      // the number of edges decreases depending on the number of edges the deleted vertex has
      numEdges -= adjList.get(vertex).size();
      // for-each loop to visit every list in the adjacancy list
      for (LinkedList<String> adjNeighbors : adjList.values()) {
        // the edges that depend on the vertex are removed from each list
        if (adjNeighbors.remove(vertex))
          numEdges--; // 'the number of edges is decrease by one for each edge removedd
      }
      adjList.remove(vertex); // the vertex is removed from the adjacency list
      vertices.remove(vertex); // the vertex is removed from the set of vertices
      numVertices--; // the number of vertices is updated
    }
  }

  /**
   * Helper method to check whether a particular vertex is present in the set of vertices
   * 
   * @param vertex
   * @return true if the vertex is present., false otherwise
   */
  private boolean hasVertex(String vertex) {
    return vertices.contains(vertex); // Set.contains() is used to check
  }

  /**
   * This helper method checks if an edge exists between any two vertices
   * 
   * @param from the starting vertex
   * @param to   the ending vertex
   * @return true fi edge is present, false otherwise
   */
  private boolean hasEdge(String from, String to) {

    if (!hasVertex(from) || !hasVertex(to))
      return false; // if neither vertex is present then false is returned
    return adjList.get(from).contains(to); // checks adjacency list if edge exists
  }

  /**
   * This method adds the edge from vertex1 to vertex2 to this graph. (edge is directed and
   * unweighted). If either vertex does not exist, add the non-existing vertex to the graph and then
   * create an edge. If the edge exists in the graph, no edge is added and no exception is thrown.
   * 
   * @param vertex1 must not be null and the edge from this vertex should be in the graph
   * @param vertex2 must not be null and the edge from this vertex should not be in the graph
   */
  public void addEdge(String vertex1, String vertex2) {
    if (vertex1 == null || vertex2 == null) // checks if either vertex is null
      return; // method does nothing if either vertex is null
    if (hasEdge(vertex1, vertex2)) // checks if an edge is present between the vertices
      return; // method does nothing if edge is already presen
    if (!(hasVertex(vertex1))) // if one vertex is not present then it is added to the graph
      addVertex(vertex1);
    if (!(hasVertex(vertex2))) // if the second vertex is not present then it is added to the graph
      addVertex(vertex2);
    adjList.get(vertex1).add(vertex2); //
    numEdges++;
  }

  /**
   * This method removes the edge from vertex1 to vertex2 from this graph. (edge is directed and
   * unweighted). If either vertex does not exist, or if an edge from vertex1 to vertex2 does not
   * exist, no edge is removed and no exception is thrown.
   * 
   * @param vertex1 must not be null and the edge from this vertex should be in the graph
   * @param vertex2 must not be null and the edge to this vertex should be in the graph
   */
  public void removeEdge(String vertex1, String vertex2) {
    if (vertex1 == null || vertex2 == null) // if either vertex is null the method does nothing 
      return;
    if (!hasEdge(vertex1, vertex2)) // if an edge does not exist then the method does nothing
      return;
    adjList.get(vertex1).remove(vertex2); // the vertex is removed from vertex1's adjacency list
    numEdges--; // the number if edges decrease by 1
  }

  /**
   * This method returns a Set that contains all the vertices
   * @return Set<String> or the set of all vertices
   */
  public Set<String> getAllVertices() {
    return vertices;
  }

  /**
   * This method gets all the neighbor (adjacent) vertices of a vertex
   * @return List<String> the List containg a vertex's neighbors
   */
  public List<String> getAdjacentVerticesOf(String vertex) {
    return adjList.get(vertex);
  }

  /**
   * This method returns the number of edges (size) in this graph.
   * @return numEdges data field
   */
  public int size() {
    return numEdges;
  }

  /**
   * This method returns the number of vertices (order) in this graph.
   * @return the numVertices data filed
   */
  public int order() {
    return numVertices;
  }

}
