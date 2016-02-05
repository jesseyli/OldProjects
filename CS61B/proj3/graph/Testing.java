package graph;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import ucb.junit.textui;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "Testing" in their name. These
 * may not be part of your graph package per se (that is, it must be
 * possible to remove them and still have your package work). */

/** Unit tests for the graph package.
 *  @author Jesse Li
 */
public class Testing {

    /** Run all JUnit tests in the graph package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(graph.Testing.class));
    }


    @Test
    public void testAddVertexDirected() {
        Graph<String, Integer> g = new DirectedGraph<String, Integer>();
        DirectedGraph<String, Integer>.Vertex v1 = g.add("v1");
        DirectedGraph<String, Integer>.Vertex v2 = g.add("v2");
        g.add(v1, v2, 10);
        assertEquals("Wrong vertex size.", 2, g.vertexSize());
        assertEquals("Wrong edge size.", 1, g.edgeSize());
        assertEquals("Incorrect out degree.", 1, g.outDegree(v1));
        assertEquals("Incorrect in degree.", 0, g.inDegree(v1));
        assertEquals("Incorrect out degree.", 0, g.outDegree(v2));
        assertEquals("Incorrect in degree.", 1, g.inDegree(v2));
    }

    @Test
    public void emptyGraph() {
        DirectedGraph g = new DirectedGraph();
        assertEquals("Initial graph has vertices", 0, g.vertexSize());
        assertEquals("Initial graph has edges", 0, g.edgeSize());
    }

    @Test
    public void testContainsDirected() {
        DirectedGraph<String, Integer> g = new DirectedGraph<String,
                Integer>();
        DirectedGraph<String, Integer>.Vertex v1 = g.add("v1");
        DirectedGraph<String, Integer>.Vertex v2 = g.add("v2");
        DirectedGraph<String, Integer>.Vertex v3 = g.add("v3");
        DirectedGraph<String, Integer>.Vertex v4 = g.add("v4");
        DirectedGraph<String, Integer>.Vertex v5 = g.add("v5");
        g.add(v1, v2, 1);
        g.add(v2, v3, 2);
        g.add(v1, v3, 3);
        g.add(v4, v2, 4);
        g.add(v5, v3, 5);
        assertEquals("Error with contains", true, g.contains(v1, v2));
        assertEquals("Edge not added correctly", true, g.contains(v1, v2, 1));
        assertEquals("Error with contains", false, g.contains(v3, v2));
    }

    @Test
    public void testContainsUndirected() {
        UndirectedGraph<String, Integer> g = new UndirectedGraph<String,
                Integer>();
        UndirectedGraph<String, Integer>.Vertex v1 = g.add("v1");
        UndirectedGraph<String, Integer>.Vertex v2 = g.add("v2");
        UndirectedGraph<String, Integer>.Vertex v3 = g.add("v3");
        UndirectedGraph<String, Integer>.Vertex v4 = g.add("v4");
        UndirectedGraph<String, Integer>.Vertex v5 = g.add("v5");
        g.add(v1, v2, 1);
        g.add(v2, v3, 2);
        g.add(v1, v3, 3);
        g.add(v4, v2, 4);
        g.add(v5, v3, 5);
        assertEquals("Error with contains without a label", true,
                g.contains(v5, v3));
        assertEquals("Error with contains without a label", true,
                g.contains(v3, v5));
        assertEquals("Error with contains with a label", true,
                g.contains(v5, v3, 5));
        assertEquals("Error with contains with a label", true,
                g.contains(v3, v5, 5));
    }
    @Test
    public void testRemoveVertexDirected() {
        DirectedGraph<String, Integer> g = new DirectedGraph<String,
                Integer>();
        DirectedGraph<String, Integer>.Vertex v1 = g.add("v1");
        DirectedGraph<String, Integer>.Vertex v2 = g.add("v2");
        DirectedGraph<String, Integer>.Vertex v3 = g.add("v3");
        DirectedGraph<String, Integer>.Vertex v4 = g.add("v4");
        DirectedGraph<String, Integer>.Vertex v5 = g.add("v5");
        DirectedGraph<String, Integer>.Edge e1 = g.add(v1, v2, 1);
        DirectedGraph<String, Integer>.Edge e2 = g.add(v2, v3, 2);
        DirectedGraph<String, Integer>.Edge e3 = g.add(v1, v3, 3);
        DirectedGraph<String, Integer>.Edge e4 = g.add(v4, v2, 4);
        DirectedGraph<String, Integer>.Edge e5 = g.add(v5, v3, 5);
        g.remove(v1);
        assertEquals("Did not remove properly", false, g.contains(v1, v2));
        assertEquals("Did not remove properly", false, g.contains(v1, v2, 5));
        assertEquals("Did not remove properly", false, g.contains(v1, v3));
        assertEquals("Incorrect vertex size", 4, g.vertexSize());
        assertEquals("Incorrect edge size", 3, g.edgeSize());
        assertEquals("Incorrect in degree", 1, g.inDegree(v2));
        assertEquals("Incorrect out degree", 1, g.outDegree(v2));

    }

    @Test
    public void testRemoveVertexUndirected() {
        Graph<String, Integer> g = new UndirectedGraph<String, Integer>();
        UndirectedGraph<String, Integer>.Vertex v1 = g.add("v1");
        UndirectedGraph<String, Integer>.Vertex v2 = g.add("v2");
        UndirectedGraph<String, Integer>.Vertex v3 = g.add("v3");
        UndirectedGraph<String, Integer>.Vertex v4 = g.add("v4");
        UndirectedGraph<String, Integer>.Vertex v5 = g.add("v5");
        UndirectedGraph<String, Integer>.Edge e1 = g.add(v1, v2, 1);
        UndirectedGraph<String, Integer>.Edge e2 = g.add(v2, v3, 2);
        UndirectedGraph<String, Integer>.Edge e3 = g.add(v1, v3, 3);
        UndirectedGraph<String, Integer>.Edge e4 = g.add(v4, v2, 4);
        UndirectedGraph<String, Integer>.Edge e5 = g.add(v5, v3, 5);
        g.remove(v1);
        assertEquals("Did not remove properly", false, g.contains(v1, v2));
        assertEquals("Did not remove properly", false, g.contains(v2, v1));
        assertEquals("Did not remove properly", false, g.contains(v1, v2, 5));
        assertEquals("Did not remove properly", false, g.contains(v2, v1, 5));
        assertEquals("Did not remove properly", false, g.contains(v1, v3));
        assertEquals("Did not remove properly", false, g.contains(v3, v1));
        assertEquals("Incorrect vertex size", 4, g.vertexSize());
        assertEquals("Incorrect edge size", 3, g.edgeSize());
        assertEquals("Incorrect degree", 2, g.degree(v2));
    }

    @Test
    public void testRemoveVerticesUndirected() {
        Graph<String, Integer> g = new UndirectedGraph<String, Integer>();
        UndirectedGraph<String, Integer>.Vertex v1 = g.add("v1");
        UndirectedGraph<String, Integer>.Vertex v2 = g.add("v2");
        UndirectedGraph<String, Integer>.Vertex v3 = g.add("v3");
        UndirectedGraph<String, Integer>.Vertex v4 = g.add("v4");
        UndirectedGraph<String, Integer>.Vertex v5 = g.add("v5");
        UndirectedGraph<String, Integer>.Edge e1 = g.add(v1, v2, 1);
        UndirectedGraph<String, Integer>.Edge e2 = g.add(v2, v3, 2);
        UndirectedGraph<String, Integer>.Edge e3 = g.add(v1, v3, 3);
        UndirectedGraph<String, Integer>.Edge e4 = g.add(v4, v2, 4);
        UndirectedGraph<String, Integer>.Edge e5 = g.add(v5, v3, 5);
        g.remove(v1, v2);
        assertEquals("Did not remove correctly", false, g.contains(v1, v2));
        assertEquals("Did not remove correctly", false, g.contains(v2, v1));
        assertEquals("Did not remove correctly", false, g.contains(v1, v2, 1));
        assertEquals("Incorrect vertex size", 5, g.vertexSize());
        assertEquals("Incorrect edge size", 4, g.edgeSize());
        assertEquals("Incorrect degree", 1, g.degree(v1));
        assertEquals("Incorrect degree", 2, g.degree(v2));
    }

    @Test
    public void testRemoveEdgeDirected() {
        DirectedGraph<String, Integer> g = new DirectedGraph<String, Integer>();
        DirectedGraph<String, Integer>.Vertex v1 = g.add("v1");
        DirectedGraph<String, Integer>.Vertex v2 = g.add("v2");
        DirectedGraph<String, Integer>.Vertex v3 = g.add("v3");
        DirectedGraph<String, Integer>.Vertex v4 = g.add("v4");
        DirectedGraph<String, Integer>.Vertex v5 = g.add("v5");
        DirectedGraph<String, Integer>.Edge e1 = g.add(v1, v2, 1);
        DirectedGraph<String, Integer>.Edge e2 = g.add(v2, v3, 2);
        DirectedGraph<String, Integer>.Edge e3 = g.add(v1, v3, 3);
        DirectedGraph<String, Integer>.Edge e4 = g.add(v4, v2, 4);
        DirectedGraph<String, Integer>.Edge e5 = g.add(v5, v3, 5);
        g.remove(e1);
        assertEquals("Did not remove correctly", false, g.contains(v1, v2));
        assertEquals("Incorrect vertex size", 5, g.vertexSize());
        assertEquals("Incorrect edge size", 4, g.edgeSize());
        assertEquals("Incorrect degree", 1, g.inDegree(v2));
        assertEquals("Incorrect degree", 1, g.outDegree(v1));
    }

    @Test
    public void testRemoveEdgeUndirected() {
        Graph<String, Integer> g = new UndirectedGraph<String, Integer>();
        UndirectedGraph<String, Integer>.Vertex v1 = g.add("v1");
        UndirectedGraph<String, Integer>.Vertex v2 = g.add("v2");
        UndirectedGraph<String, Integer>.Vertex v3 = g.add("v3");
        UndirectedGraph<String, Integer>.Vertex v4 = g.add("v4");
        UndirectedGraph<String, Integer>.Vertex v5 = g.add("v5");
        UndirectedGraph<String, Integer>.Edge e1 = g.add(v1, v2, 1);
        UndirectedGraph<String, Integer>.Edge e2 = g.add(v2, v3, 2);
        UndirectedGraph<String, Integer>.Edge e3 = g.add(v1, v3, 3);
        UndirectedGraph<String, Integer>.Edge e4 = g.add(v4, v2, 4);
        UndirectedGraph<String, Integer>.Edge e5 = g.add(v5, v3, 5);
        g.remove(e1);
        assertEquals("Did not remove correctly", false, g.contains(v1, v2));
        assertEquals("Did not remove correctly", false, g.contains(v1, v2, 1));
        assertEquals("Did not remove correctly", false, g.contains(v2, v1));
        assertEquals("Did not remove correctly", false, g.contains(v2, v1, 1));
        assertEquals("Incorrect vertex size", 5, g.vertexSize());
        assertEquals("Incorrect edge size", 4, g.edgeSize());
        assertEquals("Incorrect degree", 2, g.degree(v2));
        assertEquals("Incorrect degree", 1, g.degree(v1));
    }

    @Test
    public void testRemoveVerticesDirected() {
        DirectedGraph<String, Integer> g = new DirectedGraph<String, Integer>();
        DirectedGraph<String, Integer>.Vertex v1 = g.add("v1");
        DirectedGraph<String, Integer>.Vertex v2 = g.add("v2");
        DirectedGraph<String, Integer>.Vertex v3 = g.add("v3");
        DirectedGraph<String, Integer>.Vertex v4 = g.add("v4");
        DirectedGraph<String, Integer>.Vertex v5 = g.add("v5");
        DirectedGraph<String, Integer>.Edge e1 = g.add(v1, v2, 1);
        DirectedGraph<String, Integer>.Edge e2 = g.add(v2, v3, 2);
        DirectedGraph<String, Integer>.Edge e3 = g.add(v1, v3, 3);
        DirectedGraph<String, Integer>.Edge e4 = g.add(v4, v2, 4);
        DirectedGraph<String, Integer>.Edge e5 = g.add(v5, v3, 5);
        g.remove(v1, v2);
        assertEquals("Did not remove correctly", false, g.contains(v1, v2));
        assertEquals("Did not remove correctly", false, g.contains(v1, v2, 1));
        assertEquals("Incorrect vertex size", 5, g.vertexSize());
        assertEquals("Incorrect edge size", 4, g.edgeSize());
        assertEquals("#1Incorrect degree", 1, g.outDegree(v2));
        assertEquals("#2Incorrect degree", 1, g.inDegree(v2));
        assertEquals("Incorrect degree", 0, g.inDegree(v1));
        assertEquals("#3Incorrect degree", 1, g.outDegree(v1));
    }

    @Test
    public void testDegreeUndirected() {
        Graph<String, Integer> g = new UndirectedGraph<String, Integer>();
        UndirectedGraph<String, Integer>.Vertex v1 = g.add("v1");
        UndirectedGraph<String, Integer>.Vertex v2 = g.add("v2");
        UndirectedGraph<String, Integer>.Vertex v3 = g.add("v3");
        UndirectedGraph<String, Integer>.Vertex v4 = g.add("v4");
        UndirectedGraph<String, Integer>.Vertex v5 = g.add("v5");
        g.add(v1, v2, 1);
        g.add(v2, v3, 2);
        g.add(v1, v3, 3);
        g.add(v4, v2, 4);
        g.add(v5, v3, 5);
        assertEquals("Incorrect degree", 3, g.degree(v3));
        assertEquals("Edge not added correctly", true, g.contains(v1, v2));
        assertEquals("Edge not added correctly", true, g.contains(v2, v1));
        assertEquals("Edge not added correctly", false, g.contains(v1, v3, 5));
        assertEquals("Error in vertex size", 5, g.vertexSize());
        assertEquals("Error in edge size", 5, g.edgeSize());
    }
}
