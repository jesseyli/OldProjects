package graph;

/** A depth-first traversal of a graph.  One typically specializes
 *  this class by overriding the Visit methods, as needed. By default,
 *  no action is taken on any vertex or edge.  A Visit method may
 *  throw a RejectException or a StopException to alter the course of a
 *  traversal.
 *  @author
 */
public class DepthFirst<VLabel, ELabel> {

    /** Perform a depth-first traversal of G over all vertices reachable
     *  from V, or as modified by one of the visit methods throwing an
     *  exception. */
    public void traverse(Graph<VLabel, ELabel> G, Vertex<VLabel> v) {
        // FILL IN
    }

    /** Continue the previous traversal starting from V.
     *  Continuing a traversal means that we do not traverse
     *  vertices or edges that have been traversed previously. */
    public void continueTraversing(Vertex<VLabel> v) {
        // FILL IN
    }

    /** If the traversal ends prematurely, returns the Vertex argument to
     *  preVisit or postVisit that caused a Visit stop the traversal.
     *  Otherwise, returns null. */
    public Vertex<VLabel> finalVertex() {
        return _finalVertex;
    }

    /** If the traversal ends prematurely, returns the Edge argument to
     *  preVisit or postVisit that caused a Visit routine to stop the
     *  traversal. If it was not an edge that caused termination,
     *  returns null. */
    public Edge<VLabel, ELabel> finalEdge() {
        return _finalEdge;
    }

    /** Returns the graph currently being traversed.  Undefined if no traversal
     *  is in progress. */
    protected Graph<VLabel, ELabel> theGraph() {
        return null;
        // FILL IN
    }

    /** Method to be called on the first visit to vertex V in
     *  a traversal, before visiting the unvisited successors
     *  of V.  If this routine throws a StopException, the traversal ends.
     *  If it throws a RejectException, outgoing edges are not considered.
     *  The default does nothing.  */
    protected void preVisit(Vertex<VLabel> v) {
    }

    /** Method to be called on leaving vertex V after visiting all its
     *  unvisited successors in a traversal.  If this routine throws
     *  a StopException, the traversal ends.  A RejectException has no effect.
     *  The default simply returns true. */
    protected void postVisit(Vertex<VLabel> v) {
    }

    /** Method to be called when traversing an edge E from vertex V0
     *  during a traversal.  If this routine throws a StopException,
     *  the traversal ends. If it throws a RejectException, the node at the
     *  other end of the edge is not traversed.  The default does nothing. */
    protected void preVisit(Edge<VLabel, ELabel> e, Vertex<VLabel> v0) {
    }

    /** Method to be called after traversing the edge E from V0
     *  and finishing the traversal from the other incident vertex.
     *  If this routine throws a StopException, the traversal ends.
     *  A RejectException has no effect. The default does nothing. */
    protected void postVisit(Edge<VLabel, ELabel> e, Vertex<VLabel> v0) {
    }

    /** The Vertex (if any) that terminated the last traversal. */
    private Vertex<VLabel> _finalVertex;
    /** The Edge (if any) that terminated the last traversal. */
    private Edge<VLabel, ELabel> _finalEdge;

}
