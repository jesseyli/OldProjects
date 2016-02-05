package graph;

import graph.Graph.Edge;
import graph.Graph.Vertex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may make changes that don't affect the API as seen
 * from outside the graph package:
 *   + You may make methods in Graph abstract, if you want different
 *     implementations in DirectedGraph and UndirectedGraph.
 *   + You may add bodies to abstract methods, modify existing bodies,
 *     or override inherited methods.
 *   + You may change parameter names, or add 'final' modifiers to parameters.
 *   + You may private and package private members.
 *   + You may add additional non-public classes to the graph package.
 */

/** Represents a general graph whose vertices are labeled with a type
 *  VLABEL and whose edges are labeled with a type ELABEL. The
 *  vertices are represented by the inner type Vertex and edges by
 *  inner type Edge.  A graph may be directed or undirected.  For
 *  an undirected graph, outgoing and incoming edges are the same.
 *  Graphs may have self edges and may have multiple edges between vertices.
 *
 *  The vertices and edges of the graph, the edges incident on a
 *  vertex, and the neighbors of a vertex are all accessible by
 *  iterators.  Changing the graph's structure by adding or deleting
 *  edges or vertices invalidates these iterators (subsequent use of
 *  them is undefined.)
 *  @author Jesse Li
 */
public abstract class Graph<VLabel, ELabel> {

    /** Represents one of my vertices. */
    public class Vertex {

        /** A new vertex with LABEL as the value of getLabel(). */
        Vertex(VLabel label) {
            _label = label;
        }

        /** Returns the label on this vertex. */
        public VLabel getLabel() {
            return _label;
        }

        @Override
        public String toString() {
            return String.valueOf(_label);
        }

        /** The label on this vertex. */
        private final VLabel _label;

    }

    /** Represents one of my edges. */
    public class Edge {

        /** An edge (V0,V1) with label LABEL.  It is a directed edge (from
         *  V0 to V1) in a directed graph. */
        Edge(Vertex v0, Vertex v1, ELabel label) {
            _label = label;
            _v0 = v0;
            _v1 = v1;
        }

        /** Returns the label on this edge. */
        public ELabel getLabel() {
            return _label;
        }

        /** Return the vertex this edge exits. For an undirected edge, this is
         *  one of the incident vertices. */
        public Vertex getV0() {
            return _v0;
        }

        /** Return the vertex this edge enters. For an undirected edge, this is
         *  the incident vertices other than getV1(). */
        public Vertex getV1() {
            return _v1;
        }

        /** Returns the vertex at the other end of me from V.  */
        public final Vertex getV(Vertex v) {
            if (v == _v0) {
                return _v1;
            } else if (v == _v1) {
                return _v0;
            } else {
                throw new
                    IllegalArgumentException("vertex not incident to edge");
            }
        }

        @Override
        public String toString() {
            return String.format("(%s,%s):%s", _v0, _v1, _label);
        }

        /** Endpoints of this edge.  In directed edges, this edge exits _V0
         *  and enters _V1. */
        private final Vertex _v0, _v1;

        /** The label on this edge. */
        private final ELabel _label;

    }

    /*=====  Methods and variables of Graph =====*/

    /** Returns the number of vertices in me. */
    public int vertexSize() {
        return vertices.size();
    }

    /** Returns the number of edges in me. */
    public int edgeSize() {
        return edges.size();
    }

    /** Returns true iff I am a directed graph. */
    public abstract boolean isDirected();

    /** Returns the number of outgoing edges incident to V. Assumes V is one of
     *  my vertices.  */
    public int outDegree(Vertex v) {
        int counter = 0;
        for (Edge e: _inContainer.get(v)) {
            if (e.getV0().equals(v)) {
                counter += 1;
            }
        }
        return counter;
    }

    /** Returns the number of incoming edges incident to V. Assumes V is one of
     *  my vertices. */
    public int inDegree(Vertex v) {
        int counter = 0;
        for (Edge e: _inContainer.get(v)) {
            if (e.getV1().equals(v)) {
                counter += 1;
            }
        }
        return counter;
    }

    /** Returns outDegree(V). This is simply a synonym, intended for
     *  use in undirected graphs. */
    public final int degree(Vertex v) {
        return outDegree(v);
    }

    /** Returns true iff there is an edge (U, V) in me with any label. */
    public boolean contains(Vertex u, Vertex v) {
        if (_inContainer.containsKey(u)) {
            for (Edge e: _inContainer.get(u)) {
                if (e.getV1().equals(v)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Returns true iff there is an edge (U, V) in me with label LABEL. */
    public boolean contains(Vertex u, Vertex v,
                            ELabel label) {
        if (_inContainer.containsKey(u)) {
            for (Edge e: _inContainer.get(u)) {
                if (e.getV1().equals(v)) {
                    if (e.getLabel().equals(label)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** Returns a new vertex labeled LABEL, and adds it to me with no
     *  incident edges. */
    public Vertex add(VLabel label) {
        Vertex newVertex = new Vertex(label);
        _inContainer.put(newVertex, new ArrayList<Edge>());
        return newVertex;
    }

    /** Returns an edge incident on FROM and TO, labeled with LABEL
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from,
                    Vertex to,
                    ELabel label) {
        Edge newEdge = new Edge(from, to, label);
        _inContainer.get(to).add(newEdge);
        _inContainer.get(from).add(newEdge);
        return newEdge;
    }

    /** Returns an edge incident on FROM and TO with a null label
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from,
                    Vertex to) {
        Edge newEdge = new Edge(from, to, null);
        _inContainer.get(to).add(newEdge);
        _inContainer.get(from).add(newEdge);
        return newEdge;
    }

    /** Remove V and all adjacent edges, if present. */
    public void remove(Vertex v) {
        for (Edge e: _inContainer.get(v)) {
            _inContainer.get(e.getV(v)).remove(e);
        }
        _inContainer.remove(v);
    }

    /** Remove E from me, if present.  E must be between my vertices,
     *  or the result is undefined.  */
    public void remove(Edge e) {
        for (ArrayList<Edge> list: _inContainer.values()) {
            while (list.contains(e)) {
                list.remove(e);
            }
        }
    }

    /** Remove all edges from V1 to V2 from me, if present.  The result is
     *  undefined if V1 and V2 are not among my vertices.  */
    public void remove(Vertex v1, Vertex v2) {
        for (Edge e: _inContainer.get(v1)) {
            if (e.getV(v1).equals(v2)) {
                _inContainer.get(v2).remove(e);
                _inContainer.get(v1).remove(e);
            }
        }
    }

    /** Returns an Iterator over all vertices in arbitrary order. */
    public Iteration<Vertex> vertices() {
        ArrayList<Vertex> vertices = new ArrayList<Vertex>();
        for (Vertex v: _inContainer.keySet()) {
            vertices.add(v);
        }
        Iterator<Vertex> vertexes = vertices.iterator();
        return Iteration.iteration(vertexes);
    }

    /** Returns an iterator over all successors of V. */
    public Iteration<Vertex> successors(Vertex v) {
        ArrayList<Vertex> succ = new ArrayList<Vertex>();
        for (int x = 0; x < _inContainer.get(v).size(); x += 1) {
            if (_inContainer.get(v).get(x).getV0().equals(v)) {
                succ.add(_inContainer.get(v).get(x).getV1());
            }
            if (!isDirected()) {
                if (_inContainer.get(v).get(x).getV1().equals(v)) {
                    succ.add(_inContainer.get(v).get(x).getV0());
                }
            }
        }
        Iterator<Vertex> successor = succ.iterator();
        return Iteration.iteration(successor);
    }

    /** Returns an iterator over all predecessors of V. */
    public Iteration<Vertex> predecessors(Vertex v) {
        ArrayList<Vertex> predecess = new ArrayList<Vertex>();
        for (int x = 0; x < _inContainer.get(v).size(); x++) {
            if (_inContainer.get(v).get(x).getV1() == v) {
                predecess.add(_inContainer.get(v).get(x).getV0());
            }
        }
        Iterator<Vertex> predecessor = predecess.iterator();
        return Iteration.iteration(predecessor);
    }

    /** Returns successors(V).  This is a synonym typically used on
     *  undirected graphs. */
    public final Iteration<Vertex> neighbors(Vertex v) {
        return successors(v);
    }

    /** Returns an iterator over all edges in me. */
    public Iteration<Edge> edges() {
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for (ArrayList<Edge> list: _inContainer.values()) {
            for (Edge e: list) {
                edges.add(e);
            }
        }
        Iterator<Edge> edge = edges.iterator();
        return Iteration.iteration(edge);
    }

    /** Returns iterator over all outgoing edges from V. */
    public Iteration<Edge> outEdges(Vertex v) {
        if (!isDirected()) {
            Iterator<Edge> outedges = _inContainer.get(v).iterator();
            return Iteration.iteration(outedges);
        }
        ArrayList<Edge> outedge = new ArrayList<Edge>();
        for (int x = 0; x < _inContainer.get(v).size(); x++) {
            if (_inContainer.get(v).get(x).getV0() == v) {
                outedge.add(_inContainer.get(v).get(x));
            }
        }
        Iterator<Edge> outedges = outedge.iterator();
        return Iteration.iteration(outedges);
    }

    /** Returns iterator over all incoming edges to V. */
    public Iteration<Edge> inEdges(Vertex v) {
        ArrayList<Edge> inedge = new ArrayList<Edge>();
        for (int x = 0; x < _inContainer.get(v).size(); x++) {
            if (_inContainer.get(v).get(x).getV1() == v) {
                inedge.add(_inContainer.get(v).get(x));
            }
        }
        Iterator<Edge> inedges = inedge.iterator();
        return Iteration.iteration(inedges);
    }

    /** Returns outEdges(V). This is a synonym typically used
     *  on undirected graphs. */
    public final Iteration<Edge> edges(Vertex v) {
        return outEdges(v);
    }

    /** Returns the natural ordering on T, as a Comparator.  For
     *  example, if intComp = Graph.<Integer>naturalOrder(), then
     *  intComp.compare(x1, y1) is <0 if x1<y1, ==0 if x1=y1, and >0
     *  otherwise. */
    public static <T extends Comparable<? super T>> Comparator<T> naturalOrder()
    {
        return new Comparator<T>() {
            @Override
            public int compare(T x1, T x2) {
                return x1.compareTo(x2);
            }
        };
    }

    /** Cause subsequent calls to edges() to visit or deliver
     *  edges in sorted order, according to COMPARATOR. Subsequent
     *  addition of edges may cause the edges to be reordered
     *  arbitrarily.  */
    public void orderEdges(Comparator<ELabel> comparator) {
        // FIXME
    }

    /** Hashmap of the vertices and edges I contain. */
    private HashMap<Vertex, ArrayList<Edge>> _inContainer = new
            HashMap<Vertex, ArrayList<Edge>>();
    /** Arraylist that stores my vertices. */
    ArrayList<Vertex> vertices = new ArrayList<Vertex>();
    /** Arraylist that stores my edges. */
    ArrayList<Edge> edges = new ArrayList<Edge>();
}
