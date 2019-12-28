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
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class represents a test suite for testing a packageManager implementation
 *
 * @author Vedaant Tambi
 *
 */
class PackageManagerTest {
  // creates an instance of the packageManager class for testing
  PackageManager packageManagerInstance;
  // a List used for checking against lists returned by methods of PackagerManager.java
  List<String> installationLists;

  /**
   * This method runs before every test method
   * 
   * @throws Exception in case object is not created
   */
  @BeforeEach
  void setUp() throws Exception {
    packageManagerInstance = new PackageManager();
    installationLists = new LinkedList<String>();
  }

  /**
   * This method runs after every test method
   * 
   * @throws Exception in case of error
   */
  @AfterEach
  void tearDown() throws Exception {
    packageManagerInstance = null;
    installationLists = null;
  }

  /**
   * This method tests getInstallationOrder method of the package manager. This method checks
   * whether the correct installation order for a package was returned by the function
   */
  @Test
  public void test001_get_installation_order() {
    // installation list is filled with the expected order of installation
    installationLists.add("C");
    installationLists.add("D");
    installationLists.add("B");
    installationLists.add("A");
    try {
      // constructs graph from valid.json
      packageManagerInstance.constructGraph(
          "C:\\Users\\Vedaant Tambi\\Documents\\College\\2nd Semester\\CS 400 Programming III\\CS 400 Eclipse Workspace\\p4\\src\\valid.json");
      if (!(packageManagerInstance.getInstallationOrder("A").equals(installationLists)))
        fail("The installation order should be " + installationLists + " and not "
            + packageManagerInstance.getInstallationOrder("A"));

    } catch (Exception e) { // in case any unexpected exception is thrown
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  /**
   * This method tests toInstallation method of the package manager. This method checks whether the
   * correct packages are installed for the given package
   */
  @Test
  public void test002_checks_to_install_method() {
    // installation list is filled with packages expected to be installed
    installationLists.add("A");
    installationLists.add("C");
    try {
      // constructs graph from sharedDependencies.json
      packageManagerInstance.constructGraph(
          "C:\\Users\\Vedaant Tambi\\Documents\\College\\2nd Semester\\CS 400 Programming III\\CS 400 Eclipse Workspace\\p4\\src\\shared_dependencies.json");
      // if the expected and actual packages do not match then the test has failed
      if (!(packageManagerInstance.toInstall("A", "B").equals(installationLists)))
        fail("The installation order of A when B has been installed should be " + installationLists
            + " and not " + packageManagerInstance.toInstall("A", "B"));

    } catch (Exception e) {// in case any unexpected exception is thrown
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  /**
   * This method tests the getPackage method of the package manager. This method checks whether ALL
   * the packages in the graph are sorted in the right installation order
   */
  @Test
  public void test003_get_package_order_for_all() {
    // installation list is filled with the correct package order for all
    installationLists.add("C");
    installationLists.add("D");
    installationLists.add("B");
    installationLists.add("A");
    installationLists.add("E");

    try {
      // constructs graph from valid.json
      packageManagerInstance.constructGraph(
          "C:\\Users\\Vedaant Tambi\\Documents\\College\\2nd Semester\\CS 400 Programming III\\CS 400 Eclipse Workspace\\p4\\src\\valid.json");
      // tests fails if the package order is not right
      if (!(packageManagerInstance.getInstallationOrderForAllPackages().equals(installationLists)))
        fail("The package order for all should be " + installationLists + " and not "
            + packageManagerInstance.getInstallationOrderForAllPackages());

    } catch (Exception e) {// in case any unexpected exception is thrown
      fail("Should not throw exception: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * This method tests whether the package with the maximum number of dependencies in the graph is
   * returned by the getPackageWIth MaxDepenencies method of the package manager.
   */
  @Test
  public void test004_get_package_with_maximum_number_of_dependencies() {
    try {
      packageManagerInstance.constructGraph(
          "C:\\Users\\Vedaant Tambi\\Documents\\College\\2nd Semester\\CS 400 Programming III\\CS 400 Eclipse Workspace\\p4\\src\\shared_dependencies.json");
      // A has the maximum number of dependencies in the whole graph from shared_dependencies.json
      if (!(packageManagerInstance.getPackageWithMaxDependencies().equals("A")))
        fail("The package with maximum number of package dependencies should be A and not "
            + packageManagerInstance.getPackageWithMaxDependencies());
    } catch (Exception e) {// in case any unexpected exception is thrown
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  /**
   *
   * This method tests getInstallationOrder method to see if it throws CycleException when cycles
   * are present in the graph
   *
   */
  @Test
  public void test005_get_installation_order_for_a_graph_with_cycles_() {
    try {
      // constructs graph from cyclic.json
      packageManagerInstance.constructGraph(
          "C:\\Users\\Vedaant Tambi\\Documents\\College\\2nd Semester\\CS 400 Programming III\\CS 400 Eclipse Workspace\\p4\\src\\cyclic.json");
      packageManagerInstance.getInstallationOrder("A");
      fail("The getInstallationOrder method was supposed to throw a CycleException");
    } catch (CycleException cyc) {

    } catch (Exception e) {// in case any unexpected exception is thrown
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  /**
   * This method tests toInstall method to see if it throws PackageNotFoundException when a package
   * that does not exist is passed as an argument to the method
   *
   */
  @Test
  public void test006_checks_if_to_install_method_throws_package_exception() {
    try {
      packageManagerInstance.constructGraph(
          "C:\\Users\\Vedaant Tambi\\Documents\\College\\2nd Semester\\CS 400 Programming III\\CS 400 Eclipse Workspace\\p4\\src\\valid.json");
      // F is not present in the graph and hence PackageNotFOundException is supposed to be thrown
      packageManagerInstance.toInstall("A", "F");
      fail("The toInstall method was supposed to throw a PackageNotFoundException");
    } catch (PackageNotFoundException pke) {
      // if method execution reaches here then test was successful
    } catch (Exception e) {// in case any unexpected exception is thrown
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  /**
   * This method tests getConstructGraph method to see if it throws FIileNotFoundException when a
   * file that does not exist is passed as an argument to it
   *
   */
  @Test
  public void test007_construct_graph_throws_File_not_found_exception() {

    try {
      // FileNotFoundException is supposed to be thrown here has fake.json does not exist
      packageManagerInstance.constructGraph(
          "C:\\Users\\Vedaant Tambi\\Documents\\College\\2nd Semester\\CS 400 Programming III\\CS 400 Eclipse Workspace\\p4\\src\\fake.json");
      fail("The toInstall method was supposed to throw a FileNotFoundException");
    } catch (FileNotFoundException fe) {
      // if method execution reaches here then test was successful
    } catch (Exception e) {// in case any unexpected exception is thrown
      fail("Should not throw exception: " + e.getMessage());
    }
  }

  /**
   * This method tests the getAllPackages method to see if all the packages presnt in the graph are
   * returned.
   */
  @Test
  public void test008_checks_if_all_packages_were_returned() {

    try {
      packageManagerInstance.constructGraph(
          "C:\\Users\\Vedaant Tambi\\Documents\\College\\2nd Semester\\CS 400 Programming III\\CS 400 Eclipse Workspace\\p4\\src\\valid.json");
      // if statement to check whether ever package exists in the graph
      if (!((packageManagerInstance.getAllPackages().contains("A"))
          && (packageManagerInstance.getAllPackages().contains("B"))
          && (packageManagerInstance.getAllPackages().contains("C"))
          && (packageManagerInstance.getAllPackages().contains("D"))
          && (packageManagerInstance.getAllPackages().contains("E"))))
        fail("The package should contain all and only the packages of A, B, C ,D , E and not "
            + (packageManagerInstance.getAllPackages()));
    } catch (Exception e) {// in case any unexpected exception is thrown
      fail("Should not throw exception: " + e.getMessage());
    }
  }

}
