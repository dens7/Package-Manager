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
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class represents a test suite for testing a Graph implementation
 * 
 * @author Vedaant Tambi
 */
class GraphTest {
  // an instance of the graph to be tested
  private Graph graphInstance; // the instance of the graph which will be used for testing
  private Set<String> verticeTest; // the instance of a set used for checking the vertex list
  private LinkedList<String> adjListTest; // the instance of linked list for checking adjacency list

  /**
   * This method runs before every test method
   * 
   * @throws Exception in case object is not created
   */
  @BeforeEach
  public void setUp() throws IOException {
    graphInstance = new Graph(); // an object of Graph is initialized
    verticeTest = new HashSet<String>(); // a HashSet object is instantiated
    adjListTest = new LinkedList<String>(); // a linked list is instantiated
  }

  /**
   * This method runs after every test method
   * 
   * @throws Exception in case of error
   */
  @AfterEach
  public void tearDown() throws Exception {
    // after every method object is initialized to null
    graphInstance = null;
    verticeTest = null;
    adjListTest = null;
  }

  /**
   * Helper method to fill a graph instance with vertices and edges
   */
  private void graph_filler() {
    graphInstance.addEdge("A", "B");
    graphInstance.addEdge("A", "C");
    graphInstance.addEdge("C", "D");
    graphInstance.addEdge("D", "E");
    graphInstance.addVertex("F");
    graphInstance.addEdge("D", "G");
    graphInstance.addEdge("E", "G");
    graphInstance.addVertex("H");
  }

  /**
   * This method tests the addEdge method of Graph.java. This method checks whether the addition
   * of a null vertex in the graph does not happen
   */
  @Test
  public void test001_null_vertex() {

    graphInstance.addEdge(null, "cat"); // this method does nothing as a null vertex is passed
    // checks if the graph instance has no vertices
    if (!graphInstance.getAllVertices().equals(verticeTest))
      // test fails and execution reaches here
      fail("should not be able to insert edge with null vertex");

  }

  /**
   * This method tests the addEdge and removeVertex methods of the graph. This method checks whether
   * the addition and removal still returns the correct and order and size of the graph
   * 
   */
  @Test
  public void test002_after_insert_one_remove_one_order_is_zero() {
    // Insertion of keys and corresponding values in the hash Table
    graphInstance.addEdge("A", "B"); // inserting an edge
    graphInstance.removeVertex("A"); // removing one of the vertices
    verticeTest.add("B"); // test set for comparing the graphInstance
    if (!graphInstance.getAllVertices().equals(verticeTest)) // if the correct vertices are not
                                                             // present
      fail("The vertex B must be  present in the graph");
    // if the correct order and size are not returned then test has failed
    if (graphInstance.order() != 1)
      fail("graph should have an order of 1, but order = " + graphInstance.order());
    if (graphInstance.size() != 0)
      fail("graph should have a size of 0, but size = " + graphInstance.size());
  }

  /**
   * This method tests the insertEdges method of the graph. This method checks the adjacency list of
   * to see if the edges were inserted properly
   */
  @Test
  public void test003_insert_edges_and_check_adjacent_list_of_vertices() {
    graph_filler(); // this helper method fills the graph with edges and vertices
    // fill the Adjacency List
    adjListTest.add("E");
    adjListTest.add("G");
    // checks whether the test list is filled with the right edges
    if (!graphInstance.getAdjacentVerticesOf("D").equals(adjListTest))
      // if execution reaches here, it means that the method has failed
      fail("Adjacent list of D is supposed to be: " + adjListTest + " and not "
          + graphInstance.getAdjacentVerticesOf("D"));
    adjListTest.clear(); // to clear the test adjacency list
    adjListTest.add("G");
    // checks whether the test list is filled with the right edges
    if (!graphInstance.getAdjacentVerticesOf("E").equals(adjListTest))
      // if execution reaches here, it means that the method has failed
      fail("Adjacent list of E is supposed to be: " + adjListTest + " and not "
          + graphInstance.getAdjacentVerticesOf("E"));
  }

  /**
   * This method tests the removeEdge method of the Graph. This method tests that if the remove
   * method removes the edge that is present in the graph instance
   */
  @Test
  public void test004_removeEdge_removes_edge_which_is_present() {

    graphInstance.addEdge("A", "B"); // inserting an Edge
    graphInstance.removeEdge("A", "B"); // removing the same edge
    // checks if the adjacency lists are empty
    if (!(graphInstance.getAdjacentVerticesOf("A").equals(adjListTest))
        && (graphInstance.getAdjacentVerticesOf("B").equals(adjListTest)))
      fail("The vertex A and B are not supposed to form an edge: " + "Adjacent list of A: "
          + graphInstance.getAdjacentVerticesOf("A") + "Adjacancy List of B: "
          + graphInstance.getAdjacentVerticesOf("B"));
    // checks if the vertices are still there even if the adjacecny lists are empty
    if (graphInstance.order() != 2)
      fail("graph should have an order of 1, but order = " + graphInstance.order());
    // checks if the size is zero
    if (graphInstance.size() != 0)
      fail("graph should have a size of 0, but size = " + graphInstance.size());
  }

  /**
   * This method tests the remove method of the graph. It checks whether the removeVertex method
   * returns the correct value for the specified key
   * 
   */
  @Test
  public void test005_removeVertex_removes_the_right_vertex() {
    graph_filler(); // fills the graph with vertices and edges
    // the test set is filled with the expected vertices
    verticeTest.add("A");
    verticeTest.add("B");
    verticeTest.add("C");
    verticeTest.add("E");
    verticeTest.add("F");
    verticeTest.add("G");
    verticeTest.add("H");
    graphInstance.removeVertex("D"); //vertex D is removed
    //checks if the order of the graph is right
    if (graphInstance.order() != 7)
      fail("graph should have a order of 7, but order = " + graphInstance.order());
    // checks if the vertices set has been correctly updated
    if (!graphInstance.getAllVertices().equals(verticeTest))
      fail("Vertex list of is supposed to be: " + verticeTest + " and not "
          + graphInstance.getAllVertices());
    // checks if the the adjacency list of each vertex has been updated after the removal
    if (!graphInstance.getAdjacentVerticesOf("C").equals(adjListTest))
      fail("Adjacent list of C is supposed to be: " + adjListTest + " and not "
          + graphInstance.getAdjacentVerticesOf("C"));

  }

}

