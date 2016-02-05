package grader;

import graph.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;

import org.junit.Test;
import ucb.junit.textui;
import static org.junit.Assert.*;

/** Unit tests for the graph package.
 *  @author P. N. Hilfinger
 */
public class GraphTest {
    private abstract static class TG extends Graph<Integer, Integer> {
    }

    private static class DG extends DirectedGraph<Integer, Integer> {
    }

    private static class UG extends UndirectedGraph<Integer, Integer> {
    }

    /* Graph descriptions are 3-dimensional arrays of the form
     *     {  {  {  VERTEX LABEL, ... } },
     *        {  { VL1s, VL1e, EL1 }, { VL2s, VL2e, EL2 } ... }
     *     }
     * where the VLx are vertex labels and the ELx are edge labels. */
       
    /** A simple tree. */
    private final int[][][] GRAPH1 = {
        { { 0, 10, 20, 30, 110, 210, 330 } },
        { { 0, 10, 10 }, { 0, 20, 20 }, { 0, 30, 30 },
          { 10, 110, 100 }, { 10, 210, 200 },
          { 30, 330, 300 } }
    };

    /** Edges for test graph 1. */
    private static final int[][][] E1 = {
        { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 } },
        {
            { 0, 1, 0 }, { 0, 2, 1 }, { 0, 3, 2 }, { 1, 2, 3 }, { 1, 4, 4 },
            { 1, 5, 5 }, { 2, 6, 6 }, { 2, 7, 7 }, { 7, 0, 8 }, { 7, 8, 9 },
            { 7, 9, 10 }, { 9, 6, 11 }
        } 
    };
    /** Edges for test graph 2. */
    private static final int[][][] E2 = {
        { { 0, 1, 3, 4, 5, 6, 7, 8, 9 } },
        {
            { 0, 1, 0 }, { 0, 3, 2 }, { 1, 4, 4 },
            { 1, 5, 5 }, { 7, 0, 8 }, { 7, 8, 9 },
            { 7, 9, 10 }, { 9, 6, 11 }
        }
    };
    private static final int[][][] E3 = {
        { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 } },
        { 
            { 0, 1, 0 }, { 1, 4, 1 }, { 4, 8, 2 }, { 0, 2, 3 }, { 0, 3, 4 },
            { 2, 5, 5 }, { 5, 9, 6 }, { 3, 10, 7 }, { 2, 6, 8 }, { 3, 7, 9 },
            { 7, 10, 10 }, { 6, 10, 11 }
        }
    };
    private static final int[] BF_DISTS1 = { 0, 1, 1, 1, 2, 2, 2, 2, 3, 3, 2 };

    /** Vertices traversed for breadth-first search of E1 from 0. */
    private static final List<String> DF_TRAVERSED_SET1 =
        asList("000B", "000C",
               "001B", "001C",
               "002B", "002C",
               "003B", "003C",
               "004B", "004C",
               "005B", "005C",
               "006B", "006C",
               "007B", "007C",
               "008B", "008C",
               "009B", "009C");

    /** Previsits */
    private static final List<String> PREVISITS1 =
        asList("001A", "002A", "003A", "004A", "005A", "006A", "007A", "008A",
               "009A", "010A");

    /** Vertices traversed for breadth-first search of E3 from 0. */
    private static final List<String> BF_TRAVERSED_SET1 =
        asList("000B", "000C",
               "001B", "001C",
               "002B", "002C",
               "003B", "003C",
               "004B", "004C",
               "005B", "005C",
               "006B", "006C",
               "007B", "007C",
               "008B", "008C",
               "009B", "009C",
               "010B", "010C");

    /** Return a sorted list of e[WHICH] for all e in EDGES for which
     *  either V is e[0] (if V0 is true) or V is e[1] (if V1 is true). */
    private ArrayList<Integer> labelList(int v, int[][] edges,
                                         boolean v0, boolean v1,
                                         int which) {
        ArrayList<Integer> r = new ArrayList<>();
        for (int[] e : edges) {
            if ((v0 && e[0] == v) || (v1 && e[1] == v)) {
                r.add(e[which]);
            }
        }
        Collections.sort(r);
        return r;
    }

    /** Return the number of vertices in DESC. */
    private int numVertices(int[][][] desc) {
        return desc[0][0].length;
    }

    /** The vertex list of DESC. */
    private int[] vertices(int[][][] desc) {
        return desc[0][0];
    }

    /** The edge list of DESC. */
    private int[][] edges(int[][][] desc) {
        return desc[1];
    }

    /** Return the number of edges in DESC. */
    private int numEdges(int[][][] desc) {
        return desc[1].length;
    }

    /** Return a sorted list of labels on outgoing edges of V in DESC. */
    private ArrayList<Integer> outgoing(int v, int[][][] desc) {
        return labelList(v, desc[1], true, false, 2);
    }

    /** Return a sorted list of labels on incoming edges of V in DESC. */
    private ArrayList<Integer> incoming(int v, int[][][] desc) {
        return labelList(v, desc[1], false, true, 2);
    }

    /** Return a sorted list of labels on incident edges of V in DESC. */
    private ArrayList<Integer> incident(int v, int[][][] desc) {
        return labelList(v, desc[1], true, true, 2);
    }

    /** Return a sorted list of successor vertex labels. */
    private ArrayList<Integer> successors(int v, int[][][] desc) {
        return labelList(v, desc[1], true, false, 1);
    }

    /** Return a sorted list of predecessor vertex labels. */
    private ArrayList<Integer> predecessors(int v, int[][][] desc) {
        return labelList(v, desc[1], false, true, 0);
    }

    /** A sorted list of predecessor vertex labels. */
    private ArrayList<Integer> neighbors(int v, int[][][] desc) {
        ArrayList<Integer> r = new ArrayList<>();
        for (int[] e : edges(desc)) {
            if (e[0] == v) {
                r.add(e[1]);
            } else if (e[1] == v) {
                r.add(e[0]);
            }
        }
        Collections.sort(r);
        return r;
    }

    /** Return true iff DESC contains an edge from V0 to V1. */
    private boolean containsDirected(int v0, int v1, int[][][] desc) {
        for (int[] e : desc[1]) {
            if (e[0] == v0 && e[1] == v1) {
                return true;
            }
        }
        return false;
    }

    /** Return true iff DESC contains an edge from V0 to V1 or V1 to V0. */
    private boolean containsUndirected(int v0, int v1, int[][][] desc) {
        for (int[] e : desc[1]) {
            if ((e[0] == v0 && e[1] == v1) || (e[0] == v1 && e[1] == v0)) {
                return true;
            }
        }
        return false;
    }

    /** Return true iff DESC contains an edge from V0 to V1 labeled L. */
    private boolean containsDirected(int v0, int v1, int L, int[][][] desc) {
        for (int[] e : desc[1]) {
            if (e[0] == v0 && e[1] == v1 && e[2] == L) {
                return true;
            }
        }
        return false;
    }

    /** Return true iff DESC contains an edge from V0 to V1 or V1 to
     *  V0 labeled L. */
    private boolean containsUndirected(int v0, int v1, int L, int[][][] desc) {
        for (int[] e : desc[1]) {
            if (((e[0] == v0 && e[1] == v1) || (e[0] == v1 && e[1] == v0))
                && e[2] == L) {
                return true;
            }
        }
        return false;
    }

    /** Set  _G to G with the vertices and edges given in DESC.  Puts the
     *  vertices in _V and the edges in _E. */
    private void fillGraph(Graph<Integer, Integer> G, int[][][] desc) {
        _G = G;
        _V.clear();
        _E.clear();
        for (Integer i : desc[0][0]) {
            _V.put(i, _G.add(i));
        }
        for (int[] e : desc[1]) {
            _E.put(e[2], _G.add(_V.get(e[0]), _V.get(e[1]), e[2]));
        }
    }

    /** A sorted list of the vertices produced by IT. */
    private ArrayList<Integer>
    toVList(Iterable<Graph<Integer, Integer>.Vertex> it) {

        ArrayList<Integer> r = new ArrayList<>();
        for (Graph<Integer, Integer>.Vertex v : it) {
            r.add(v.getLabel());
        }
        Collections.sort(r);
        return r;
    }

    /** A sorted list of the edges produced by IT. */
    private ArrayList<Integer>
    toEList(Iterable<Graph<Integer, Integer>.Edge> it) {

        ArrayList<Integer> r = new ArrayList<>();
        for (Graph<Integer, Integer>.Edge v : it) {
            r.add(v.getLabel());
        }
        Collections.sort(r);
        return r;
    }


    /** Run all JUnit tests in the graph package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(GraphTest.class));
    }

    /** Check properties of empty graph. */
    @Test
    public void emptyGraph() {
        _G = new DG();
        assertEquals("Initial graph has vertices", 0, _G.vertexSize());
        assertEquals("Initial graph has edges", 0, _G.edgeSize());
    }

    /** Check that creating a directed graph yields right edges and
     *  vertices. */
    @Test
    public void makeDirected() {
        fillGraph(new DG(), E1);

        assertTrue("directedness", _G.isDirected());
        assertEquals("Wrong vertex count", numVertices(E1), _G.vertexSize());
        assertEquals("Wrong vertices",
                     asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9),
                     toVList(_G.vertices()));
        assertEquals("Wrong edge count", numEdges(E1), _G.edgeSize());
        assertEquals("Wrong edges",
                     asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
                     toEList(_G.edges()));

        for (int i : vertices(E1)) {
            for (int j : vertices(E1)) {
                assertEquals(String.format("bad containment for %d -> %d",
                                           i, j),
                             containsDirected(i, j, E1),
                             _G.contains(_V.get(i), _V.get(j)));
                for (int L = 0; L < numEdges(E1); L += 1) {
                    assertEquals(String.format("bad containment for %d-%d->%d",
                                               i, L, j),
                                 containsDirected(i, j, L, E1),
                                 _G.contains(_V.get(i), _V.get(j), L));
                }
            }
        }

        for (int v : vertices(E1)) {
            assertEquals("wrong outgoing edges for " + v,
                         outgoing(v, E1),
                         toEList(_G.outEdges(_V.get(v))));
            assertEquals("wrong outgoing count for " + v,
                         outgoing(v, E1).size(),
                         _G.outDegree(_V.get(v)));
            assertEquals("wrong incoming edges for " + v,
                         incoming(v, E1),
                         toEList(_G.inEdges(_V.get(v))));
            assertEquals("wrong incoming count for " + v,
                         incoming(v, E1).size(),
                         _G.inDegree(_V.get(v)));
        }
    }

    /** Check that creating an undirected graph yields right edges and
     *  vertices. */
    @Test
    public void makeUndirected() {
        fillGraph(new UG(), E1);
        assertFalse("undirectedness", _G.isDirected());
        assertEquals("Wrong vertex count", numVertices(E1), _G.vertexSize());
        assertEquals("Wrong edge count", numEdges(E1), _G.edgeSize());

        for (int i : vertices(E1)) {
            for (int j : vertices(E1)) {
                assertEquals(String.format("bad containment for %d -> %d",
                                           i, j),
                             containsUndirected(i, j, E1),
                             _G.contains(_V.get(i), _V.get(j)));
                for (int L = 0; L < numEdges(E1); L += 1) {
                    assertEquals(String.format("bad containment for %d-%d->%d",
                                               i, L, j),
                                 containsUndirected(i, j, L, E1),
                                 _G.contains(_V.get(i), _V.get(j), L));
                }
            }
        }

        for (int v : vertices(E1)) {
            assertEquals("wrong incident edges for " + v,
                         incident(v, E1),
                         toEList(_G.edges(_V.get(v))));
            assertEquals("wrong edge count for " + v,
                         incident(v, E1).size(),
                         _G.degree(_V.get(v)));
        }
    }

    /** Check that neighbor nodes are correct in directed graph. */
    @Test
    public void directedNeighbors() {
        fillGraph(new DG(), E1);

        for (int v : vertices(E1)) {
            assertEquals("wrong successors for " + v,
                         successors(v, E1),
                         toVList(_G.successors(_V.get(v))));
            assertEquals("wrong predecessors for " + v,
                         predecessors(v, E1),
                         toVList(_G.predecessors(_V.get(v))));
        }
    }

    /** Check that neighbor nodes are correct in undirected graph. */
    @Test
    public void undirectedNeighbors() {
        fillGraph(new UG(), E1);

        for (int v : vertices(E1)) {
            assertEquals("wrong neighbors for " + v,
                         neighbors(v, E1),
                         toVList(_G.neighbors(_V.get(v))));
        }
    }

    /** Check that creating a directed graph and then removing a
     *  vertex yields right edges and vertices. */
    @Test
    public void removeDirected() {
        fillGraph(new DG(), E1);
        int nv1 = numVertices(E1);

        _G.remove(_V.get(2));
        assertEquals("wrong vertex count after removal", nv1 - 1,
                     _G.vertexSize());
        assertEquals("wrong edge count after removal", numEdges(E2),
                     _G.edgeSize());
        assertEquals("failed to remove vertex 2 properly",
                     asList(0, 1, 3, 4, 5, 6, 7, 8, 9),
                     toVList(_G.vertices()));
        assertEquals("failed to remove vertex 2's edges properly",
                     asList(0, 2, 4, 5, 8, 9, 10, 11),
                     toEList(_G.edges()));

        for (int v : vertices(E1)) {
            if (v == 2) {
                continue;
            }
            assertEquals("after removal, wrong outgoing edges for " + v,
                         outgoing(v, E2),
                         toEList(_G.outEdges(_V.get(v))));
            assertEquals("after removal, wrong outgoing count for " + v,
                         outgoing(v, E2).size(),
                         _G.outDegree(_V.get(v)));
            assertEquals("after removal, wrong incoming edges for " + v,
                         incoming(v, E2),
                         toEList(_G.inEdges(_V.get(v))));
            assertEquals("after removal, wrong incoming count for " + v,
                         incoming(v, E2).size(),
                         _G.inDegree(_V.get(v)));
        }

    }

    /** Check that creating an undirected graph and then removing a
     *  vertex yields right edges and vertices. */
    @Test
    public void removeUndirected() {
        fillGraph(new UG(), E1);
        int nv1 = numVertices(E1);

        _G.remove(_V.get(2));
        assertEquals("wrong vertex count after removal", nv1 - 1,
                     _G.vertexSize());
        assertEquals("wrong edge count after removal", numEdges(E2),
                     _G.edgeSize());
        assertEquals("failed to remove vertex 2 properly",
                     asList(0, 1, 3, 4, 5, 6, 7, 8, 9),
                     toVList(_G.vertices()));
        assertEquals("failed to remove vertex 2's edges properly",
                     asList(0, 2, 4, 5, 8, 9, 10, 11),
                     toEList(_G.edges()));

        for (int v : vertices(E1)) {
            if (v == 2) {
                continue;
            }
            assertEquals("after removal, wrong incident edges for " + v,
                         incident(v, E2),
                         toEList(_G.edges(_V.get(v))));
            assertEquals("after removal, wrong edge count for " + v,
                         incident(v, E2).size(),
                         _G.degree(_V.get(v)));
        }

    }

    private static class Traverse1 extends Traversal<Integer, Integer> {

        private ArrayList<String> _trail = new ArrayList<>();

        @Override
        protected void preVisit(Graph<Integer, Integer>.Edge e,
                                Graph<Integer, Integer>.Vertex v0) {
            _trail.add(String.format("%03dA", e.getV(v0).getLabel()));
        }

        @Override
        protected void visit(Graph<Integer, Integer>.Vertex v) {
            _trail.add(String.format("%03dB", v.getLabel()));
        }

        @Override
        protected void postVisit(Graph<Integer, Integer>.Vertex v) {
            _trail.add(String.format("%03dC", v.getLabel()));
        }

        ArrayList<String> trail() {
            return new ArrayList<>(_trail);
        }

    }

    private void checkTraverseOrder(List<String> tr) {
        for (int i = 0; i < tr.size(); i += 1) {
            String v = tr.get(i);
            String n = v.substring(0, 3);
            if (v.endsWith("B") && !v.equals("000B")) {
                assertTrue("misplaced or missing previsit for " + n,
                           tr.subList(0, i).contains(n + "A"));
            }
            if (v.endsWith("B")) {
                assertTrue("misplaced or missing postvisit for " + v,
                           tr.subList(i + 1, tr.size()).contains(n + "C"));
                assertFalse("late previsit for " + v,
                            tr.subList(i + 1, tr.size()).contains(n + "A"));
            }
        }
    }

    /** Test depth-first travesal on directed graph. */
    @Test
    public void depthFirstDirectedTraverse1() {
        fillGraph(new DG(), E1);

        Traverse1 traverse = new Traverse1();
        traverse.depthFirstTraverse(_G, _V.get(0));
        ArrayList<String> trail = traverse.trail();
        checkTraverseOrder(trail);
        trail.removeAll(PREVISITS1);
        Collections.sort(trail);
        assertEquals("wrong nodes visited",
                     DF_TRAVERSED_SET1,
                     trail);
    }

    /** Test depth-first travesal on undirected graph. */
    @Test
    public void depthFirstUndirectedTraverse1() {
        fillGraph(new UG(), E1);

        Traverse1 traverse = new Traverse1();
        traverse.depthFirstTraverse(_G, _V.get(0));

        ArrayList<String> trail = traverse.trail();
        checkTraverseOrder(trail);
        trail.removeAll(PREVISITS1);
        Collections.sort(trail);
        assertEquals("wrong nodes visited",
                     DF_TRAVERSED_SET1,
                     trail);
    }

    private void checkBreadthOrder(List<String> trail, int[] dists) {
        int last;
        last = -1;
        for (String s : trail) {
            if (s.endsWith("B")) {
                int d = dists[Integer.parseInt(s.substring(0, 3))];
                assertTrue("visit not in depth-first order: " + s,
                           d >= last);
                last = d;
            }
        }
    }

    /** Test breadth-first travesal on directed graph. */
    @Test
    public void breadthFirstDirectedTraverse1() {
        fillGraph(new DG(), E3);

        Traverse1 traverse = new Traverse1();
        traverse.breadthFirstTraverse(_G, _V.get(0));
        ArrayList<String> trail = traverse.trail();
        checkTraverseOrder(trail);
        trail.removeAll(PREVISITS1);
        checkBreadthOrder(trail, BF_DISTS1);
        Collections.sort(trail);
        assertEquals("wrong nodes visited",
                     BF_TRAVERSED_SET1,
                     trail);
    }

    /** Test breadth-first travesal on undirected graph. */
    @Test
    public void breadthFirstUndirectedTraverse1() {
        fillGraph(new UG(), E3);

        Traverse1 traverse = new Traverse1();
        traverse.breadthFirstTraverse(_G, _V.get(0));

        ArrayList<String> trail = traverse.trail();
        checkTraverseOrder(trail);
        trail.removeAll(PREVISITS1);
        checkBreadthOrder(trail, BF_DISTS1);
        Collections.sort(trail);
        assertEquals("wrong nodes visited",
                     BF_TRAVERSED_SET1,
                     trail);
    }

    /** Mapping of vertex labels to vertices of _G. */
    private HashMap<Integer, Graph<Integer, Integer>.Vertex> _V =
        new HashMap<>();
    /** Mapping of edges labels to edges of _G. */
    private HashMap<Integer, Graph<Integer, Integer>.Edge> _E = new HashMap<>();
    /** The test graph. */
    /** Set of vertex labels in _V. */
    private HashSet<Integer> _VL = new HashSet<>();
    /** Set of edge labels in _V. */
    private HashSet<Integer> _EL = new HashSet<>();
    /** The test graph. */
    private Graph<Integer, Integer> _G;
}
