package graph;

/** An edge between two vertices in a graph.
 *  @author
 */
public class Edge<VLabel, ELabel> {

    /** An edge (V0,V1) with label LABEL.  It is a directed edge (from
     *  V0 to V1) in a directed graph. */
    protected Edge(Vertex<VLabel> v0, Vertex<VLabel> v1, ELabel label) {
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
    public Vertex<VLabel> getV0() {
        return _v0;
    }

    /** Return the vertex this edge enters. For an undirected edge, this is
     *  the incident vertices other than getExit(). */
    public Vertex<VLabel> getV1() {
        return _v1;
    }

    /** Returns the vertex at the other end of me from V.  */
    public final Vertex<VLabel> getV(Vertex<VLabel> v) {
        if (v == _v0) {
            return _v1;
        } else if (v == _v1) {
            return _v0;
        } else {
            throw new IllegalArgumentException("vertex not incident to edge");
        }
    }

    @Override
    public String toString() {
        return String.format("(%s,%s):%s", _v0, _v1, _label);
    }

    /** Endpoints of this edge.  In directed edges, this edge exits _V0
     *  and enters _V1. */
    private final Vertex<VLabel> _v0, _v1;

    /** The label on this edge. */
    private final ELabel _label;

}
