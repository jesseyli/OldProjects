package graph;


import static org.junit.Assert.assertEquals;
import graph.Graph.Vertex;

import org.junit.Test;

import ucb.junit.textui;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your graph package per se (that is, it must be
 * possible to remove them and still have your package work). */

/** Unit tests for the graph package. */
public class Testing {

    /** Run all JUnit tests in the graph package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(graph.Testing.class));
    }

    // Add tests.  Here's a sample.

    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }
    @Test
    public void testAddVertexDirected() {
        Graph<String, Integer> g = new DirectedGraph<String, Integer>();
        DirectedGraph<String, Integer>.Vertex test1 = g.add("test1");
        DirectedGraph<String, Integer>.Vertex test2 = g.add("test2");
        g.add(test1, test2, 4);
        assertEquals("Error in vertex size", 2, g.vertexSize());
        assertEquals("Error in edge size", 1, g.edgeSize());
        assertEquals("Error in outDegree", 1, g.outDegree(test1));
        assertEquals("Error in outDegree", 0, g.outDegree(test2));
        assertEquals("Error in inDegree", 0, g.inDegree(test1));
        assertEquals("Error in inDegree", 1, g.inDegree(test2));
    }
    
    @Test
    public void testIndegreeUndirected() {
        Graph<String, Integer> g = new UndirectedGraph<String, Integer>();
        UndirectedGraph<String, Integer>.Vertex A = g.add("A");
        UndirectedGraph<String, Integer>.Vertex B = g.add("B");
        UndirectedGraph<String, Integer>.Vertex C = g.add("C");
        UndirectedGraph<String, Integer>.Vertex D = g.add("D");
        UndirectedGraph<String, Integer>.Vertex E = g.add("E");
        g.add(C, A, 5);
        g.add(B, D, 10);
        g.add(A, D, 2);
        g.add(E, A, 8);
        g.add(E, C, 4);
        assertEquals("Error in degree undirected", 3, g.degree(A));
        assertEquals("Error with contains without label undirected", true, g.contains(A, C));
        assertEquals("Error with contains without label undirected", true, g.contains(C, A));
        assertEquals("Error with contains with label undirected", true, g.contains(A, C, 5));
        assertEquals("Error with contains with label undirected", true, g.contains(C,  A, 5));
        assertEquals("Error in vertex size", 5, g.vertexSize());
        assertEquals("Error in edge size", 5, g.edgeSize());
        
    }
    
    @Test
    public void testContainsDirected() {
        DirectedGraph<String, Integer> g = new DirectedGraph<String, Integer>();
        DirectedGraph<String, Integer>.Vertex A = g.add("A");
        DirectedGraph<String, Integer>.Vertex B = g.add("B");
        DirectedGraph<String, Integer>.Vertex C = g.add("C");
        DirectedGraph<String, Integer>.Vertex D = g.add("D");
        DirectedGraph<String, Integer>.Vertex E = g.add("E");
        g.add(C, A, 5);
        g.add(B, D, 10);
        g.add(A, D, 2);
        g.add(E, A, 8);
        g.add(E, C, 4);
        assertEquals("Error with contains without a label", true, g.contains(C, A));
        assertEquals("Error with contains with a label", true, g.contains(C, A, 5));
        assertEquals("Error with contains", false, g.contains(A, C));
    }
    
    @Test
    public void testContainsUndirected() {
        Graph<String, Integer> g = new UndirectedGraph<String, Integer>();
        UndirectedGraph<String, Integer>.Vertex A = g.add("A");
        UndirectedGraph<String, Integer>.Vertex B = g.add("B");
        UndirectedGraph<String, Integer>.Vertex C = g.add("C");
        UndirectedGraph<String, Integer>.Vertex D = g.add("D");
        UndirectedGraph<String, Integer>.Vertex E = g.add("E");
        g.add(C, A, 5);
        g.add(B, D, 10);
        g.add(A, D, 2);
        g.add(E, A, 8);
        g.add(E, C, 4);
        assertEquals("Error with contains without a label", true, g.contains(A, C));
        assertEquals("Error with contains without a label", true, g.contains(C, A));
        assertEquals("Error with contains with a label", true, g.contains(A, C, 5));
        assertEquals("Error with contains with a label", true, g.contains(C, A, 5));
    }
    @Test
    public void testRemoveVertexDirected() {
        DirectedGraph<String, Integer> g = new DirectedGraph<String, Integer>();
        DirectedGraph<String, Integer>.Vertex A = g.add("A");
        DirectedGraph<String, Integer>.Vertex B = g.add("B");
        DirectedGraph<String, Integer>.Vertex C = g.add("C");
        DirectedGraph<String, Integer>.Vertex D = g.add("D");
        DirectedGraph<String, Integer>.Vertex E = g.add("E");
        DirectedGraph<String, Integer>.Edge e1 = g.add(C, A, 5);
        DirectedGraph<String, Integer>.Edge e2 = g.add(B, D, 10);
        DirectedGraph<String, Integer>.Edge e3 = g.add(A, D, 2);
        DirectedGraph<String, Integer>.Edge e4 = g.add(E, A, 8);
        DirectedGraph<String, Integer>.Edge e5 = g.add(E, C, 4);
        g.remove(A);
        assertEquals("Error with remove1", false, g.contains(C, A));
        assertEquals("Error with remove2", false, g.contains(C, A, 5));
        assertEquals("Error with remove3", false, g.contains(E, A));
        assertEquals("Error with remove4", false, g.contains(A, D));
        assertEquals("Error with remove5", 4, g.vertexSize());
        assertEquals("Error with remove6", 2, g.edgeSize());
        assertEquals("Error with remove7", 1, g.inDegree(D));
        assertEquals("Error with remove8", 1, g.outDegree(E));
        
    }
    
    @Test
    public void testRemoveVertexUndirected() {
        Graph<String, Integer> g = new UndirectedGraph<String, Integer>();
        UndirectedGraph<String, Integer>.Vertex A = g.add("A");
        UndirectedGraph<String, Integer>.Vertex B = g.add("B");
        UndirectedGraph<String, Integer>.Vertex C = g.add("C");
        UndirectedGraph<String, Integer>.Vertex D = g.add("D");
        UndirectedGraph<String, Integer>.Vertex E = g.add("E");
        UndirectedGraph<String, Integer>.Edge e1 = g.add(C, A, 5);
        UndirectedGraph<String, Integer>.Edge e2 = g.add(B, D, 10);
        UndirectedGraph<String, Integer>.Edge e3 = g.add(A, D, 2);
        UndirectedGraph<String, Integer>.Edge e4 = g.add(E, A, 8);
        UndirectedGraph<String, Integer>.Edge e5 = g.add(E, C, 4);
        g.remove(A);
        assertEquals("Error with remove1", false, g.contains(A, C));
        assertEquals("Error with remove2", false, g.contains(A, C, 5));
        assertEquals("Error with remove3", false, g.contains(A, E));
        assertEquals("Error with remove4", false, g.contains(A, D));
        assertEquals("Error with remove5", 4, g.vertexSize());
        assertEquals("Error with remove6", 2, g.edgeSize());
        assertEquals("Error with remove7", 1, g.degree(D));
        assertEquals("Error with remove8", 1, g.degree(E));
        assertEquals("Error with remove9", false, g.contains(C, A));
    }
    
    @Test
    public void testRemoveEdgeDirected() {
        DirectedGraph<String, Integer> g = new DirectedGraph<String, Integer>();
        DirectedGraph<String, Integer>.Vertex A = g.add("A");
        DirectedGraph<String, Integer>.Vertex B = g.add("B");
        DirectedGraph<String, Integer>.Vertex C = g.add("C");
        DirectedGraph<String, Integer>.Vertex D = g.add("D");
        DirectedGraph<String, Integer>.Vertex E = g.add("E");
        DirectedGraph<String, Integer>.Edge e1 = g.add(C, A, 5);
        DirectedGraph<String, Integer>.Edge e2 = g.add(B, D, 10);
        DirectedGraph<String, Integer>.Edge e3 = g.add(A, D, 2);
        DirectedGraph<String, Integer>.Edge e4 = g.add(E, A, 8);
        DirectedGraph<String, Integer>.Edge e5 = g.add(E, C, 4);
        g.remove(e5);
        assertEquals("Error with remove1", false, g.contains(E, C));
        assertEquals("Error with remove2", false, g.contains(E, C, 4));
        assertEquals("Error with remove3", 5, g.vertexSize());
        assertEquals("Error with remove4", 4, g.edgeSize());
        assertEquals("Error with remove5", 0, g.inDegree(E));
        assertEquals("Error with remove6", 1, g.outDegree(E));
        assertEquals("Error with remove7", 0, g.inDegree(C));
        assertEquals("Error with remove8", 1, g.outDegree(C));
        
    }
    
    @Test
    public void testRemoveEdgeUndirected() {
        Graph<String, Integer> g = new UndirectedGraph<String, Integer>();
        UndirectedGraph<String, Integer>.Vertex A = g.add("A");
        UndirectedGraph<String, Integer>.Vertex B = g.add("B");
        UndirectedGraph<String, Integer>.Vertex C = g.add("C");
        UndirectedGraph<String, Integer>.Vertex D = g.add("D");
        UndirectedGraph<String, Integer>.Vertex E = g.add("E");
        UndirectedGraph<String, Integer>.Edge e1 = g.add(C, A, 5);
        UndirectedGraph<String, Integer>.Edge e2 = g.add(B, D, 10);
        UndirectedGraph<String, Integer>.Edge e3 = g.add(A, D, 2);
        UndirectedGraph<String, Integer>.Edge e4 = g.add(E, A, 8);
        UndirectedGraph<String, Integer>.Edge e5 = g.add(E, C, 4);
        g.remove(e1);
        assertEquals("Error with remove1", false, g.contains(A, C));
        assertEquals("Error with remove2", false, g.contains(A, C, 5));
        assertEquals("Error with remove3", 5, g.vertexSize());
        assertEquals("Error with remove4", 4, g.edgeSize());
        assertEquals("Error with remove5", 1, g.degree(C));
        assertEquals("Error with remove6", 2, g.degree(A));
        assertEquals("Error with remove7", false, g.contains(C, A));
    }

    @Test
    public void testRemoveVerticesDirected() {
        DirectedGraph<String, Integer> g = new DirectedGraph<String, Integer>();
        DirectedGraph<String, Integer>.Vertex A = g.add("A");
        DirectedGraph<String, Integer>.Vertex B = g.add("B");
        DirectedGraph<String, Integer>.Vertex C = g.add("C");
        DirectedGraph<String, Integer>.Vertex D = g.add("D");
        DirectedGraph<String, Integer>.Vertex E = g.add("E");
        DirectedGraph<String, Integer>.Edge e1 = g.add(C, A, 5);
        DirectedGraph<String, Integer>.Edge e2 = g.add(B, D, 10);
        DirectedGraph<String, Integer>.Edge e3 = g.add(A, D, 2);
        DirectedGraph<String, Integer>.Edge e4 = g.add(E, A, 8);
        DirectedGraph<String, Integer>.Edge e5 = g.add(E, C, 4);
        g.remove(E, C);
        assertEquals("Error with remove1", false, g.contains(E, C));
        assertEquals("Error with remove2", false, g.contains(E, C, 4));
        assertEquals("Error with remove3", 5, g.vertexSize());
        assertEquals("Error with remove4", 4, g.edgeSize());
        assertEquals("Error with remove5", 0, g.inDegree(E));
        assertEquals("Error with remove6", 1, g.outDegree(E));
        assertEquals("Error with remove7", 0, g.inDegree(C));
        assertEquals("Error with remove8", 1, g.outDegree(C));
    }
    
    @Test
    public void testRemoveVerticesUndirected() {
        Graph<String, Integer> g = new UndirectedGraph<String, Integer>();
        UndirectedGraph<String, Integer>.Vertex A = g.add("A");
        UndirectedGraph<String, Integer>.Vertex B = g.add("B");
        UndirectedGraph<String, Integer>.Vertex C = g.add("C");
        UndirectedGraph<String, Integer>.Vertex D = g.add("D");
        UndirectedGraph<String, Integer>.Vertex E = g.add("E");
        UndirectedGraph<String, Integer>.Edge e1 = g.add(C, A, 5);
        UndirectedGraph<String, Integer>.Edge e2 = g.add(B, D, 10);
        UndirectedGraph<String, Integer>.Edge e3 = g.add(A, D, 2);
        UndirectedGraph<String, Integer>.Edge e4 = g.add(E, A, 8);
        UndirectedGraph<String, Integer>.Edge e5 = g.add(E, C, 4);
        g.remove(A, C);
        assertEquals("Error with remove1", false, g.contains(A, C));
        assertEquals("Error with remove2", false, g.contains(A, C, 5));
        assertEquals("Error with remove3", 5, g.vertexSize());
        assertEquals("Error with remove4", 4, g.edgeSize());
        assertEquals("Error with remove5", 1, g.degree(C));
        assertEquals("Error with remove6", 2, g.degree(A));
        assertEquals("Error with remove7", false, g.contains(C, A));
    }
    
    @Test
    public void tester() {
        depthFirstTest1();
        //depthFirstTest2();
    }
    
    /**@Test
    public void testVertices() {
        DirectedGraph<String, Integer> g = new DirectedGraph<String, Integer>();
        DirectedGraph<String, Integer>.Vertex A = g.add("A");
        DirectedGraph<String, Integer>.Vertex B = g.add("B");
        DirectedGraph<String, Integer>.Vertex C = g.add("C");
        DirectedGraph<String, Integer>.Vertex D = g.add("D");
        DirectedGraph<String, Integer>.Vertex E = g.add("E");
        g.add(C, A, 5);
        g.add(B, D, 10);
        g.add(A, D, 2);
        g.add(E, A, 8);
        g.add(E, C, 4);
        ArrayList<DirectedGraph<String, Integer>.Vertex> vertices =
                new ArrayList<DirectedGraph<String, Integer>.Vertex>();
        vertices.add(A);
        vertices.add(B);
        vertices.add(C);
        vertices.add(D);
        vertices.add(E);
        assertEquals("Error in vertices()", true, sameVertices(Iteration.iteration(vertices), g.vertices()));
    }
    
    /** Returns true if the contents of both Iterations, A1 and A2, are the same.
    public boolean sameVertices(Iteration<Graph<String, Integer>.Vertex> A1, Iteration<Graph<String, Integer>.Vertex> A2) {
        while (A1.hasNext()) {
            while (A2.hasNext()) {
                
            }
        }
    } */
    
    public static void depthFirstTest1() {
        UndirectedGraph<String, Integer> g = new UndirectedGraph<String, Integer>();
        UndirectedGraph<String, Integer>.Vertex A = g.add("A");
        UndirectedGraph<String, Integer>.Vertex B = g.add("B");
        UndirectedGraph<String, Integer>.Vertex C = g.add("C");
        UndirectedGraph<String, Integer>.Vertex D = g.add("D");
        UndirectedGraph<String, Integer>.Vertex E = g.add("E");
        UndirectedGraph<String, Integer>.Vertex F = g.add("F");
        UndirectedGraph<String, Integer>.Vertex G = g.add("G");
        UndirectedGraph<String, Integer>.Vertex H = g.add("H");
        g.add(A, B);
        g.add(A, D);
        g.add(A, G);
        g.add(B, E);
        g.add(B, F);
        g.add(C, F);
        g.add(C, H);
        g.add(D, F);
        g.add(E, G);
        Traversal<String, Integer> trav = new Traversal<String, Integer>();
        System.out.println("This is depth");
        trav.depthFirstTraverse(g, A);
       // System.out.println("This is breadth");
      //  trav.breadthFirstTraverse(g, A);
    }
    
    public static void depthFirstTest2() {
        DirectedGraph<String, Integer> g = new DirectedGraph<String, Integer>();
        DirectedGraph<String, Integer>.Vertex A = g.add("A");
        DirectedGraph<String, Integer>.Vertex B = g.add("B");
        DirectedGraph<String, Integer>.Vertex C = g.add("C");
        DirectedGraph<String, Integer>.Vertex D = g.add("D");
        g.add(A, B);
        g.add(B, C);
        g.add(A, C);
        g.add(C, B);
        g.add(D, A);
        Traversal<String, Integer> trav = new Traversal<String, Integer>();
        System.out.println("This is test2");
        trav.depthFirstTraverse(g, D);
    }
    /** Returns a DirectedGraph<String, Integer>. */
    public DirectedGraph<String, Integer> makeDirectedGraph() {
        DirectedGraph<String, Integer> g = new DirectedGraph<String, Integer>();
        DirectedGraph<String, Integer>.Vertex A = g.add("A");
        DirectedGraph<String, Integer>.Vertex B = g.add("B");
        DirectedGraph<String, Integer>.Vertex C = g.add("C");
        DirectedGraph<String, Integer>.Vertex D = g.add("D");
        DirectedGraph<String, Integer>.Vertex E = g.add("E");
        g.add(C, A, 5);
        g.add(B, D, 10);
        g.add(A, D, 2);
        g.add(E, A, 8);
        g.add(E, C, 4);
        return g;
    }

    
    /** Returns an UndirectedGraph<String, Integer>. */
    public UndirectedGraph<String, Integer> makeUndirectedGraph() {
        UndirectedGraph<String, Integer> g = new UndirectedGraph<String, Integer>();
        Vertex A = g.add("A");
        Vertex B = g.add("B");
        Vertex C = g.add("C");
        Vertex D = g.add("D");
        Vertex E = g.add("E");
        g.add(C, A, 5);
        g.add(B, D, 10);
        g.add(A, D, 2);
        g.add(E, A, 8);
        g.add(E, C, 4);
        return g;
    }


}
