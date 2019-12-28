import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Filename: PackageManager.java Project: p4 Authors:
 * 
 * PackageManager is used to process json package dependency files and provide function that make
 * that information available to other users.
 * 
 * Each package that depends upon other packages has its own entry in the json file.
 * 
 * Package dependencies are important when building software, as you must install packages in an
 * order such that each package is installed after all of the packages that it depends on have been
 * installed.
 * 
 * For example: package A depends upon package B, then package B must be installed before package A.
 * 
 * This program will read package information and provide information about the packages that must
 * be installed before any given package can be installed. all of the packages in
 * 
 * You may add a main method, but we will test all methods with our own Test classes.
 */

public class PackageManager {

  private Graph graph;

  /*
   * Package Manager default no-argument constructor.
   */
  public PackageManager() {
    graph = new Graph();
  }

  /**
   * Takes in a file path for a json file and builds the package dependency graph from it.
   * 
   * @param jsonFilepath the name of json data file with package dependency information
   * @throws FileNotFoundException if file path is incorrect
   * @throws IOException           if the give file cannot be read
   * @throws ParseException        if the given json cannot be parsed
   */
  public void constructGraph(String jsonFilepath)
      throws FileNotFoundException, IOException, ParseException {
    // parsing file "JSONExample.json"
    Object obj = new JSONParser().parse(new FileReader(jsonFilepath));

    // typecasting obj to JSONObject
    JSONObject jo = (JSONObject) obj;

    // Get array of packages
    JSONArray packages = (JSONArray) jo.get("packages");
    Iterator pkgItr = packages.iterator();

    // Iterate through packages
    while (pkgItr.hasNext()) {
      // Current package JSON Object
      JSONObject current = (JSONObject) pkgItr.next();

      String currentVertex = (String) current.get("name");

      graph.addVertex(currentVertex); // Add package to graph

      // Array of dependencies
      JSONArray dependencies = (JSONArray) current.get("dependencies");
      Iterator dependency_itr = dependencies.iterator();

      // Iterate through dependencies
      while (dependency_itr.hasNext()) {
        String currentDependency = (String) dependency_itr.next();
        graph.addEdge(currentVertex, currentDependency);
      }
    }
  }

  /**
   * Helper method to get all packages in the graph.
   * 
   * @return Set<String> of all the packages
   */
  public Set<String> getAllPackages() {
    return graph.getAllVertices();
  }

  /**
   * Given a package name, returns a list of packages in a valid installation order.
   * 
   * Valid installation order means that each package is listed before any packages that depend upon
   * that package.
   * 
   * @return List<String>, order in which the packages have to be installed
   * 
   * @throws CycleException           if you encounter a cycle in the graph while finding the
   *                                  installation order for a particular package. Tip: Cycles in
   *                                  some other part of the graph that do not affect the
   *                                  installation order for the specified package, should not throw
   *                                  this exception.
   * 
   * @throws PackageNotFoundException if the package passed does not exist in the dependency graph.
   */
  public List<String> getInstallationOrder(String pkg)
      throws CycleException, PackageNotFoundException {
    if (!graph.getAllVertices().contains(pkg)) { // If vertex not found in graph
      throw new PackageNotFoundException();
    }

    // order of installation of packages to be returned
    List<String> installOrder = new ArrayList<String>();
    List<String> callStack = new ArrayList<String>(); // Pseudo call stack for DFS search
    getInstallationOrderDFSHelper(pkg, callStack, installOrder);

    return installOrder;
  }

  /**
   * Recursive helper method for getInstallationOrder which uses DFS search and cycle detection
   * 
   * @param current      Current vertex in graph
   * @param callStack    Vertices that the DFS was called upon which are still incomplete
   * @param installOrder
   * @throws CycleException
   */
  private void getInstallationOrderDFSHelper(String current, List<String> callStack,
      List<String> installOrder) throws CycleException { // TODO FIX THIS

    if (callStack.contains(current)) { // If vertex already in callStack, cycle detected
      throw new CycleException();
    }

    if (!installOrder.contains(current)) { // To prevent duplicates for shared dependencies
      if (callStack.isEmpty()) { // Will only be empty during AllPackages call, not during regular
                                 // DFS
        installOrder.add(current);
      } else {
        installOrder.add(0, current); // Add to the installation order
      }
    }
    callStack.add(current); // Add to call stack

    List<String> adj = graph.getAdjacentVerticesOf(current);
    Collections.sort(adj); // To get vertices in sorted CS400 convention order
    for (String neighbor : adj) {
      getInstallationOrderDFSHelper(neighbor, callStack, installOrder);
    }
    callStack.remove(current); // Remove from call stack after all neighbors have been dfs visited
  }

  /**
   * Given two packages - one to be installed and the other installed, return a List of the packages
   * that need to be newly installed.
   * 
   * For example, refer to shared_dependecies.json - toInstall("A","B") If package A needs to be
   * installed and packageB is already installed, return the list ["A", "C"] since D will have been
   * installed when B was previously installed.
   * 
   * @return List<String>, packages that need to be newly installed.
   * 
   * @throws CycleException           if you encounter a cycle in the graph while finding the
   *                                  dependencies of the given packages. If there is a cycle in
   *                                  some other part of the graph that doesn't affect the parsing
   *                                  of these dependencies, cycle exception should not be thrown.
   * 
   * @throws PackageNotFoundException if any of the packages passed do not exist in the dependency
   *                                  graph.
   */
  public List<String> toInstall(String newPkg, String installedPkg)
      throws CycleException, PackageNotFoundException {
    List<String> alreadyInstalled = this.getInstallationOrder(installedPkg); // List of currently
                                                                             // installed packages
    List<String> toInstall = this.getInstallationOrder(newPkg); // List of dependencies of newPkg
    List<String> needToInstall = new ArrayList<String>(); // Packages that are needed for newPkg

    for (String pkg : toInstall) {
      if (!alreadyInstalled.contains(pkg)) {
        needToInstall.add(pkg);
      }
    }
    return needToInstall;
  }

  /**
   * Return a valid global installation order of all the packages in the dependency graph.
   * 
   * assumes: no package has been installed and you are required to install all the packages
   * 
   * returns a valid installation order that will not violate any dependencies
   * 
   * @return List<String>, order in which all the packages have to be installed
   * @throws CycleException if you encounter a cycle in the graph
   */
  public List<String> getInstallationOrderForAllPackages() throws CycleException { // TODO
    this.detectCycle(); // Check if graph has cycles

    Set<String> noDependencyPackages = this.getPackagesNotDependencies();
    List<String> installOrder = new ArrayList<String>();
    List<String> callStack = new ArrayList<String>();

    for (String p : noDependencyPackages) {
      this.getInstallationOrderDFSHelper(p, callStack, installOrder);
    }

    // Collections.reverse(installOrder); // Because topological ordering
    return installOrder;
  }

  /**
   * Returns a set of packages that are not a dependency for any other package i.e. Graphnodes with
   * no predecessors
   * 
   * @return
   */
  private Set<String> getPackagesNotDependencies() {
    Set<String> allVertices = graph.getAllVertices(); // All vertices in graph, used for iteration
    List<String> dependencyPackages = null; // Packages which are a dependency for
                                            // another package
    // Using second duplicate set to prevent concurrent modification. This is the final set that
    // will be returned
    Set<String> allVerticesNoDep = graph.getAllVertices(); //

    // Remove packages which are a dependency for another package from the set
    for (String vertex : allVertices) {
      dependencyPackages = graph.getAdjacentVerticesOf(vertex);
      for (String dPackage : dependencyPackages) {
        allVerticesNoDep.remove(dPackage);
      }
    }
    // At this point, this only contains packages which aren't a dependency for any other package
    // i.e. Graphnodes with no predecessors
    return allVerticesNoDep;
  }

  private void detectCycle() throws CycleException {
    Set<String> allVertices = graph.getAllVertices();
    List<String> checked = new ArrayList<String>();
    List<String> callStack = new ArrayList<String>();

    while (true) {
      allVertices.removeAll(checked); // Remove checked vertices

      if (allVertices.isEmpty()) {
        break; // Stop when all vertices are checked
      }

      String current = (String) allVertices.toArray()[0]; // Get one element from the set

      this.getInstallationOrderDFSHelper(current, callStack, checked);
    }
  }

  /**
   * Find and return the name of the package with the maximum number of dependencies.
   * 
   * Tip: it's not just the number of dependencies given in the json file. The number of
   * dependencies includes the dependencies of its dependencies. But, if a package is listed in
   * multiple places, it is only counted once.
   * 
   * Example: if A depends on B and C, and B depends on C, and C depends on D. Then, A has 3
   * dependencies - B,C and D.
   * 
   * @return String, name of the package with most dependencies.
   * @throws CycleException if you encounter a cycle in the graph
   */
  public String getPackageWithMaxDependencies() throws CycleException { // TODO
    // We only need to check number of dependencies for packages which aren't a dependency for
    // another package as they will have the max number of dependencies
    Set<String> noDependencyPackages = this.getPackagesNotDependencies();
    int maxDep = 0;
    String maxDepPackage = "";

    try {
      for (String pkg : noDependencyPackages) {
        List<String> numPackagesToInstall = this.getInstallationOrder(pkg);
        if (numPackagesToInstall.size() > maxDep) {
          maxDep = numPackagesToInstall.size();
          maxDepPackage = pkg;
        }
      }
    } catch (PackageNotFoundException e) {
      // Will never occur as we are only calling on packages present in graph
    }
    return maxDepPackage;
  }

  public static void main(String[] args) {
    System.out.println("PackageManager.main()");

    PackageManager p = new PackageManager();
    try {
      p.constructGraph("valid.json");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
