package graph;

import java.util.ArrayList;
import java.util.Collections;
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
            if (v.equals(_v0)) {
                return _v1;
            } else if (v.equals(_v1)) {
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
        return _vert.size();
    }

    /** Returns the number of edges in me. */
    public int edgeSize() {
        return _edges.size();
    }

    /** Returns true iff I am a directed graph. */
    public abstract boolean isDirected();

    /** Returns the number of outgoing edges incident to V. Assumes V is one of
     *  my vertices.  */
    public abstract int outDegree(Vertex v);

    /** Returns the number of incoming edges incident to V. Assumes V is one of
     *  my vertices. */
    public int inDegree(Vertex v) {
        int counter = 0;
        for (int i = 0; i < _edges.size(); i += 1) {
            if (_edges.get(i).getV1().equals(v)) {
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
    public abstract boolean contains(Vertex u, Vertex v);

    /** Returns true iff there is an edge (U, V) in me with label LABEL. */
    public abstract boolean contains(Vertex u, Vertex v, ELabel label);

    /** Returns a new vertex labeled LABEL, and adds it to me with no
     *  incident edges. */
    public Vertex add(VLabel label) {
        Vertex newVertex = new Vertex(label);
        _vert.add(newVertex);
        ArrayList<Edge> edgesList = new ArrayList<Edge>();
        _holder.put(newVertex, edgesList);
        return newVertex;
    }

    /** Returns an edge incident on FROM and TO, labeled with LABEL
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from, Vertex to, ELabel label) {
        Edge newEdge = new Edge(from, to, label);
        _edges.add(newEdge);
        _holder.get(to).add(newEdge);
        _holder.get(from).add(newEdge);
        return newEdge;
    }

    /** Returns an edge incident on FROM and TO with a null label
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from, Vertex to) {
        return add(from, to, null);
    }

    /** Remove V and all adjacent edges, if present. */
    public void remove(Vertex v) {
        for (int i = 0; i < _vert.size(); i += 1) {
            for (int j = 0; j < _holder.get(_vert.get(i)).size(); j += 1) {
                if (_holder.get(_vert.get(i)).get(j).getV0().equals(v)
                        || _holder.get(_vert.get(i)).get(j).getV1().equals(v)) {
                    _holder.get(_vert.get(i)).remove(j);
                    j -= 1;
                }
            }
        }
        for (int i = 0; i < _edges.size(); i += 1) {
            if (_edges.get(i).getV0().equals(v)
                    || _edges.get(i).getV1().equals(v)) {
                _edges.remove(i);
                i -= 1;
            }
        }
        _holder.remove(v);
        _vert.remove(v);
    }

    /** Remove E from me, if present.  E must be between my vertices,
     *  or the result is undefined.  */
    public void remove(Edge e) {
        _edges.remove(e);
        for (int i = 0; i < _vert.size(); i += 1) {
            for (int j = 0; j < _holder.get(_vert.get(i)).size(); j += 1) {
                if (_holder.get(_vert.get(i)).get(j).getLabel().equals(
                        e.getLabel())) {
                    _holder.get(_vert.get(i)).remove(j);
                    j -= 1;
                }
            }
        }
    }

    /** Remove all edges from V1 to V2 from me, if present.  The result is
     *  undefined if V1 and V2 are not among my vertices.  */
    public abstract void remove(Vertex v1, Vertex v2);

    /** Returns an Iterator over all vertices in arbitrary order. */
    public Iteration<Vertex> vertices() {
        Iterator<Vertex> vert = _vert.iterator();
        return Iteration.iteration(vert);
    }

    /** Returns an iterator over all successors of V. */
    public abstract Iteration<Vertex> successors(Vertex v);

    /** Returns an iterator over all predecessors of V. */
    public Iteration<Vertex> predecessors(Vertex v) {
        ArrayList<Vertex> iter = new ArrayList<Vertex>();
        for (int i = 0; i < _holder.get(v).size(); i += 1) {
            if (_holder.get(v).get(i).getV1().equals(v)) {
                iter.add(_holder.get(v).get(i).getV0());
            }
        }
        Iterator<Vertex> predIterator = iter.iterator();
        return Iteration.iteration(predIterator);
    }

    /** Returns successors(V).  This is a synonym typically used on
     *  undirected graphs. */
    public final Iteration<Vertex> neighbors(Vertex v) {
        return successors(v);
    }

    /** Returns an iterator over all edges in me. */
    public Iteration<Edge> edges() {
        Iterator<Edge> edge = _edges.iterator();
        return Iteration.iteration(edge);
    }

    /** Returns iterator over all outgoing edges from V. */
    public abstract Iteration<Edge> outEdges(Vertex v);

    /** Returns iterator over all incoming edges to V. */
    public Iteration<Edge> inEdges(Vertex v) {
        ArrayList<Edge> in = new ArrayList<Edge>();
        for (int i = 0; i < _holder.get(v).size(); i += 1) {
            if (_holder.get(v).get(i).getV1().equals(v)) {
                in.add(_holder.get(v).get(i));
            }
        }
        Iterator<Edge> inedges = in.iterator();
        return Iteration.iteration(inedges);
    }

    /** Returns outEdges(V). This is a synonym typically used
     *  on undirected graphs. */
    public final Iteration<Edge> edges(Vertex v) {
        return outEdges(v);
    }

    /** Returns the natural ordering on T, as a Comparator.  For
     *  example, if stringComp = Graph.<Integer>naturalOrder(), then
     *  stringComp.compare(x1, y1) is <0 if x1<y1, ==0 if x1=y1, and >0
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
        final Comparator<ELabel> compFinal = comparator;
        Comparator<Edge> edgeComparator = new Comparator<Edge>() {
            public int compare(Edge e0, Edge e1) {
                return compFinal.compare(e0.getLabel(), e1.getLabel());
            }
        };
        Collections.sort(_edges, edgeComparator);
    }

    /** Returns my edges. */
    public ArrayList<Edge> getEdges() {
        return _edges;
    }

    /** Returns the Hashmap containing my vertices and edges. */
    public HashMap<Vertex, ArrayList<Edge>> getMap() {
        return _holder;
    }

    /** An ArrayList used to hold my vertices. */
    private ArrayList<Vertex> _vert = new ArrayList<Vertex>();
    /** A HashMap used to hold my edges and vertices. */
    private HashMap<Vertex, ArrayList<Edge>> _holder =
            new HashMap<Vertex, ArrayList<Edge>>();
    /** An ArrayList used to hold my edges. */
    private ArrayList<Edge> _edges = new ArrayList<Edge>();

}
