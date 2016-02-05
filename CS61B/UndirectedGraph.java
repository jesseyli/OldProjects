package graph;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may add bodies to abstract methods, modify
 * existing bodies, or override inherited methods.  */

/** An undirected graph with vertices labeled with VLABEL and edges
 *  labeled with ELABEL.
 *  @author Unzi Park
 */
public class UndirectedGraph<VLabel, ELabel> extends Graph<VLabel, ELabel> {

    /** An empty graph. */
    public UndirectedGraph() {
    }

    @Override
    public int vertexSize() {
        if (_vertices.isEmpty()) {
            return 0;
        } else {
            return _vertices.size();
        }
    }

    @Override
    public int edgeSize() {
        if (_edges.isEmpty()) {
            return 0;
        } else {
            return _edges.size() / 2;
        }
    }

    @Override
    public boolean isDirected() {
        return false;
    }
    @Override
    public int outDegree(Vertex v) {
        if (_graph.get(v) != null) {
            return _graph.get(v).size();
        } else {
            return 0;
        }
    }

    @Override
    public int inDegree(Vertex v) {
        int count = 0;
        Iteration<Edge> edgeIter = edges();
        while (edgeIter.hasNext()) {
            Edge e = edgeIter.next();
            if (e.getV1().equals(v)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean contains(Vertex u, Vertex v) {
        boolean containsEdge = false;
        for (Edge e : _edges) {
            if (e.getV0().equals(u) && e.getV1().equals(v)) {
                containsEdge = true;
                break;
            }
        }
        return containsEdge;
    }

    @Override
    public boolean contains(Vertex u, Vertex v, ELabel label) {
        boolean containsEdge = false;
        for (Edge e : _edges) {
            if (e.getV0().equals(u) && e.getV1().equals(v)
                && e.getLabel().equals(label)) {
                containsEdge = true;
                break;
            }
        }
        return containsEdge;
    }


    @Override
    public Vertex add(VLabel label) {
        Vertex newVertex = new Vertex(label);
        if (!_graph.containsKey(newVertex)) {
            _vertices.add(newVertex);
            _graph.put(newVertex, null);
        }
        return newVertex;
    }

    @Override
    public Edge add(Vertex from, Vertex to, ELabel label) {
        Edge newEdge = new Edge(from, to, label);
        boolean addEdge = false;
        boolean addEdge1 = false;
        if (_graph.containsKey(from) && _graph.get(from) != null) {
            _graph.get(from).add(newEdge);
            addEdge = true;
        } else {
            if (_graph.containsKey(from)) {
                _graph.remove(from);
            }
            ArrayList<Edge> edges1 = new ArrayList<Edge>();
            edges1.add(newEdge);
            _graph.put(from, edges1);
            addEdge = true;
        }
        Edge oppEdge = new Edge(to, from, label);
        if (_graph.containsKey(to) && _graph.get(to) != null) {
            _graph.get(to).add(oppEdge);
            addEdge1 = true;
        } else {
            if (_graph.containsKey(to)) {
                _graph.remove(to);
            }
            ArrayList<Edge> edges2 = new ArrayList<Edge>();
            edges2.add(oppEdge);
            _graph.put(to, edges2);
            addEdge1 = true;
        }
        if (addEdge && addEdge1) {
            _edges.add(newEdge);
            _edges.add(oppEdge);
        }
        return newEdge;
    }

    @Override
    public Edge add(Vertex from, Vertex to) {
        Edge newEdge = new Edge(from, to, null);
        boolean addEdge = false;
        boolean addEdge1 = false;
        if (_graph.containsKey(from) && _graph.get(from) != null) {
            _graph.get(from).add(newEdge);
            addEdge = true;
        } else {
            if (_graph.containsKey(from)) {
                _graph.remove(from);
            }
            ArrayList<Edge> edges1 = new ArrayList<Edge>();
            edges1.add(newEdge);
            _graph.put(from, edges1);
            addEdge = true;
        }
        Edge oppEdge = new Edge(to, from, null);
        if (_graph.containsKey(to) && _graph.get(to) != null) {
            _graph.get(to).add(oppEdge);
            addEdge1 = true;
        } else {
            if (_graph.containsKey(to)) {
                _graph.remove(to);
            }
            ArrayList<Edge> edges2 = new ArrayList<Edge>();
            edges2.add(oppEdge);
            _graph.put(to, edges2);
            addEdge1 = true;
        }
        if (addEdge && addEdge1) {
            _edges.add(newEdge);
            _edges.add(oppEdge);
        }
        return newEdge;
    }

    @Override
    public void remove(Vertex v) {
        if (_graph.containsKey(v) && _graph.get(v) != null) {
            Iteration<Edge> inEdgeIter = inEdges(v);
            ArrayList<Edge> toRemove = new ArrayList<Edge>();
            while (inEdgeIter.hasNext()) {
                Edge e = inEdgeIter.next();
                toRemove.add(e);
            }
            if (!toRemove.isEmpty()) {
                Iterator<Edge> toRemoveIter = toRemove.iterator();
                while (toRemoveIter.hasNext()) {
                    Edge e1 = toRemoveIter.next();
                    _graph.get(e1.getV0()).remove(e1);
                    _edges.remove(e1);
                }
            }
            _graph.remove(v);
            _vertices.remove(v);
        }
        ArrayList<Edge> oppRemove = new ArrayList<Edge>();
        if (!_edges.isEmpty()) {
            Iteration<Edge> edgesIter = edges();
            while (edgesIter.hasNext()) {
                Edge e2 = edgesIter.next();
                if (e2.getV0().equals(v)) {
                    oppRemove.add(e2);
                }
            }
            if (!oppRemove.isEmpty()) {
                Iterator<Edge> oppRemoveIter = oppRemove.iterator();
                while (oppRemoveIter.hasNext()) {
                    Edge e3 = oppRemoveIter.next();
                    _edges.remove(e3);
                    if (_graph.get(e3.getV0()) != null) {
                        _graph.get(e3.getV0()).remove(e3);
                    }
                }
            }
        }
    }

    @Override
    public void remove(Edge e) {
        if (_graph.containsKey(e.getV0()) && _graph.get(e.getV0()) != null) {
            Iteration<Edge> edgeIter = edges(e.getV0());
            ArrayList<Edge> toRemove = new ArrayList<Edge>();
            while (edgeIter.hasNext()) {
                Edge e0 = edgeIter.next();
                if (e0.getV1().equals(e.getV1())
                    && e0.getLabel().equals(e.getLabel())) {
                    toRemove.add(e0);
                }
            }
            if (!toRemove.isEmpty()) {
                Iterator<Edge> removeIter = toRemove.iterator();
                while (removeIter.hasNext()) {
                    Edge removing = removeIter.next();
                    _graph.get(removing.getV0()).remove(removing);
                    _edges.remove(removing);
                }
            }
        }
        ArrayList<Edge> oppRemove = new ArrayList<Edge>();
        if (!_edges.isEmpty()) {
            Iteration<Edge> edgesIter = edges();
            while (edgesIter.hasNext()) {
                Edge e1 = edgesIter.next();
                if (e1.getV0().equals(e.getV1()) && e1.getV1().equals(e.getV0())
                    && e1.getLabel().equals(e.getLabel())) {
                    oppRemove.add(e1);
                }
            }
            if (!oppRemove.isEmpty()) {
                Iterator<Edge> oppRemoveIter = oppRemove.iterator();
                while (oppRemoveIter.hasNext()) {
                    Edge e2 = oppRemoveIter.next();
                    _edges.remove(e2);
                    _graph.get(e2.getV0()).remove(e2);
                }
            }
        }
    }

    @Override
    public void remove(Vertex v1, Vertex v2) {
        if (_graph.containsKey(v1) && _graph.get(v1) != null) {
            Iteration<Edge> edgeIter = edges(v1);
            ArrayList<Edge> toRemove = new ArrayList<Edge>();
            while (edgeIter.hasNext()) {
                Edge e0 = edgeIter.next();
                if (e0.getV1().equals(v2)) {
                    toRemove.add(e0);
                }
            }
            if (!toRemove.isEmpty()) {
                Iterator<Edge> removeIter = toRemove.iterator();
                while (removeIter.hasNext()) {
                    Edge removing = removeIter.next();
                    _graph.get(removing.getV0()).remove(removing);
                    _edges.remove(removing);
                }
            }
        }
        ArrayList<Edge> oppRemove = new ArrayList<Edge>();
        if (!_edges.isEmpty()) {
            Iteration<Edge> edgesIter = edges();
            while (edgesIter.hasNext()) {
                Edge e1 = edgesIter.next();
                if (e1.getV0().equals(v2) && e1.getV1().equals(v1)) {
                    oppRemove.add(e1);
                }
            }
            if (!oppRemove.isEmpty()) {
                Iterator<Edge> oppRemoveIter = oppRemove.iterator();
                while (oppRemoveIter.hasNext()) {
                    Edge e2 = oppRemoveIter.next();
                    _edges.remove(e2);
                    _graph.get(e2.getV0()).remove(e2);
                }
            }
        }
    }

    @Override
    public Iteration<Vertex> vertices() {
        Iterator<Vertex> verticesIter = _vertices.iterator();
        return Iteration.iteration(verticesIter);
    }

    @Override
    public Iteration<Vertex> successors(Vertex v) {
        ArrayList<Edge> edges = _graph.get(v);
        ArrayList<Vertex> successor = new ArrayList<Vertex>();
        for (Edge route : edges) {
            successor.add(route.getV1());
        }
        Iterator<Vertex> result = successor.iterator();
        return Iteration.iteration(result);
    }

    @Override
    public Iteration<Vertex> predecessors(Vertex v) {
        Iteration<Edge> edgeIter = edges();
        ArrayList<Vertex> predecessor = new ArrayList<Vertex>();
        while (edgeIter.hasNext()) {
            Edge e = edgeIter.next();
            if (e.getV1().equals(v)) {
                predecessor.add(e.getV0());
            }
        }
        Iterator<Vertex> result = predecessor.iterator();
        return Iteration.iteration(result);
    }

    @Override
    public Iteration<Edge> edges() {
        Iterator<Edge> edgeIter = _edges.iterator();
        return Iteration.iteration(edgeIter);
    }

    @Override
    public Iteration<Edge> outEdges(Vertex v) {
        ArrayList<Edge> edges = _graph.get(v);
        Iterator<Edge> result = edges.iterator();
        return Iteration.iteration(result);
    }
    @Override
    public Iteration<Edge> inEdges(Vertex v) {
        Iteration<Edge> edgeIter = edges();
        ArrayList<Edge> inEdge = new ArrayList<Edge>();
        while (edgeIter.hasNext()) {
            Edge e = edgeIter.next();
            if (e.getV1().equals(v)) {
                inEdge.add(e);
            }
        }
        Iterator<Edge> result = inEdge.iterator();
        return Iteration.iteration(result);
    }

    /** Holds my vertices and edges. */
    private HashMap<Vertex, ArrayList<Edge>> _graph =
        new HashMap<Vertex, ArrayList<Edge>>();

    /** Holds my vertices. */
    private ArrayList<Vertex> _vertices = new ArrayList<Vertex>();

    /** Holds my edges. */
    private ArrayList<Edge> _edges = new ArrayList<Edge>();
}
