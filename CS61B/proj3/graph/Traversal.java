package graph;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.Stack;
import java.util.LinkedList;
import java.util.PriorityQueue;

/** Implements a generalized traversal of a graph.  At any given time,
 *  there is a particular set of untraversed vertices---the "fringe."
 *  Traversal consists of repeatedly removing an untraversed vertex
 *  from the fringe, visting it, and then adding its untraversed
 *  successors to the fringe.  The client can dictate an ordering on
 *  the fringe, determining which item is next removed, by which kind
 *  of traversal is requested.
 *     + A depth-first traversal treats the fringe as a list, and adds
 *       and removes vertices at one end.  It also revisits the node
 *       itself after traversing all successors by calling the
 *       postVisit method on it.
 *     + A breadth-first traversal treats the fringe as a list, and adds
 *       and removes vertices at different ends.  It also revisits the node
 *       itself after traversing all successors as for depth-first
 *       traversals.
 *     + A general traversal treats the fringe as an ordered set, as
 *       determined by a Comparator argument.  There is no postVisit
 *       for this type of traversal.
 *  As vertices are added to the fringe, the traversal calls a
 *  preVisit method on the vertex.
 *
 *  Generally, the client will extend Traversal, overriding the visit,
 *  preVisit, and postVisit methods, as desired (by default, they do nothing).
 *  Any of these methods may throw StopException to halt the traversal
 *  (temporarily, if desired).  The preVisit method may throw a
 *  RejectException to prevent a vertex from being added to the
 *  fringe, and the visit method may throw a RejectException to
 *  prevent its successors from being added to the fringe.
 *  @author Jesse Li
 */
public class Traversal<VLabel, ELabel> {

    /** Perform a traversal of G over all vertices reachable from V.
     *  ORDER determines the ordering in which the fringe of
     *  untraversed vertices is visited. The effect of specifying an
     *  ORDER whose results change as a result of modifications made during the
     *  traversal is undefined.*/
    public void traverse(Graph<VLabel, ELabel> G,
                         Graph<VLabel, ELabel>.Vertex v,
                         Comparator<VLabel> order) {
        if (!_regTravLast) {
            _visited.clear();
        }
        if (_visited.contains(v)) {
            _regTravLast = false;
            return;
        }
        boolean stopTrav = false;
        PriorityQueue<Graph<VLabel, ELabel>.Vertex> queue = new
                PriorityQueue<Graph<VLabel, ELabel>.Vertex>(
                _graph.vertexSize(), new CompareVertex(order));
        _graph = G;
        queue.add(v);
        _travComparator = order;
        _regTravLast = true;
        while (!queue.isEmpty()) {
            try {
                _visited.add(queue.peek());
                visit(queue.peek());
            } catch (StopException travStop) {
                _finalEdge = null;
                _finalVertex = queue.peek();
                return;
            } catch (RejectException travReject) {
                stopTrav = true;
            }
            if (!stopTrav) {
                Graph<VLabel, ELabel>.Vertex currentVert = queue.poll();
                for (Graph<VLabel, ELabel>.Edge out
                        : G.outEdges(currentVert)) {
                    if (!_visited.contains(out.getV(currentVert))) {
                        try {
                            preVisit(out, out.getV(currentVert));
                            queue.add(out.getV(currentVert));
                        } catch (StopException | RejectException terminate) {
                            return;
                        }
                    }
                }
            } else {
                queue.poll();
            }
        }
        _regTravLast = false;
    }

    /** Helper for a depthFirstTraverse that performs a visit on the first
     *  element of post visit stack STACK. Returns 0 for a StopException and
     *  1 otherwise. */
    public int depthPostHelper(Stack<Graph<VLabel, ELabel>.Vertex> stack) {
        _depthPostVisit.add(stack.peek());
        try {
            postVisit(stack.peek());
        } catch (StopException depStop) {
            _finalEdge = null;
            _finalVertex = stack.pop();
            return 0;
        } catch (RejectException depReject) {
            return 1;
        }
        return 1;
    }

    /** Helper for depthFirstTraverse that iterates over the graph GRAPH with
     *  the post visit stack STACK. */
    public void depthFirstHelper(Graph<VLabel, ELabel> graph,
            Stack<Graph<VLabel, ELabel>.Vertex> stack) {
        boolean stopTrav = false;
        while (!stack.empty()) {
            if (!_visited.contains(stack.peek())) {
                _visited.add(stack.peek());
                try {
                    visit(stack.peek());
                } catch (StopException depStop) {
                    _finalEdge = null;
                    _finalVertex = stack.peek();
                    return;
                } catch (RejectException depReject) {
                    stopTrav = true;
                }
                if (stopTrav) {
                    if (!_depthPostVisit.contains(stack.peek())) {
                        if (depthPostHelper(stack) == 0) {
                            return;
                        }
                    }
                    stack.pop();
                    stopTrav = false;
                } else {
                    Graph<VLabel, ELabel>.Vertex currentVert = stack.peek();
                    for (Graph<VLabel, ELabel>.Edge out
                        : graph.outEdges(currentVert)) {
                        if (!_visited.contains(out.getV(currentVert))) {
                            try {
                                preVisit(out, currentVert);
                                stack.addElement(out.getV(currentVert));
                            } catch (StopException depStop) {
                                _finalEdge = out;
                                _finalVertex =
                                        out.getV(out.getV(currentVert));
                                return;
                            } catch (RejectException depReject) {
                                continue;
                            }
                        }
                    }
                }
            } else {
                if (!_depthPostVisit.contains(stack.peek())) {
                    if (depthPostHelper(stack) != 1) {
                        return;
                    }
                }
                stack.pop();
            }
        }
        _depthLast = false;
    }

    /** Performs a depth-first traversal of G over all vertices
     *  reachable from V.  That is, the fringe is a sequence and
     *  vertices are added to it or removed from it at one end in
     *  an undefined order.  After the traversal of all successors of
     *  a node is complete, the node itself is revisited by calling
     *  the postVisit method on it. */
    public void depthFirstTraverse(Graph<VLabel, ELabel> G,
                                   Graph<VLabel, ELabel>.Vertex v) {
        if (!_depthLast) {
            _depthPostVisit.clear();
            _visited.clear();
        }
        if (_visited.contains(v)) {
            _depthLast = false;
            return;
        }
        Stack<Graph<VLabel, ELabel>.Vertex> stack = new
                Stack<Graph<VLabel, ELabel>.Vertex>();
        stack.addElement(v);
        _graph = G;
        _depthLast = true;
        depthFirstHelper(G, stack);
    }

    /** A helper for breadthFirstTravere that takes in the graph GRAPH that
     *  is currently being traversed and a LinkedList QUEUE that contains
     *  vertices in queue to be visited or post visited. */
    public void breadthHelper(Graph<VLabel, ELabel> graph,
            LinkedList<Graph<VLabel, ELabel>.Vertex> queue) {
        int index = 0;
        boolean stopTrav = false;
        while (queue.peekFirst() != null) {
            if (_breadthPostVisit.contains(queue.peek())) {
                try {
                    postVisit(queue.peek());
                } catch (StopException brStop) {
                    _breadthPostVisit.clear();
                    _finalEdge = null;
                    _finalVertex = queue.pop();
                    return;
                } catch (RejectException brReject) {
                    continue;
                }
                queue.removeFirst();
            } else {
                try {
                    _visited.add(queue.peek());
                    visit(queue.peek());
                } catch (StopException brStop) {
                    _breadthPostVisit.clear();
                    _finalEdge = null;
                    _finalVertex = queue.removeFirst();
                    return;
                } catch (RejectException brReject) {
                    stopTrav = true;
                }
                _breadthPostVisit.add(queue.peek());
                if (stopTrav) {
                    queue.removeFirst();
                } else {
                    Graph<VLabel, ELabel>.Vertex currentVert =
                            queue.removeFirst();
                    for (Graph<VLabel, ELabel>.Edge e
                        : graph.outEdges(currentVert)) {
                        if (!_visited.contains(e.getV(currentVert))) {
                            try {
                                preVisit(e, currentVert);
                                if (!_preVisit.contains(e.getV(currentVert))) {
                                    queue.add(e.getV(currentVert));
                                    _preVisit.add(e.getV(currentVert));
                                }
                            } catch (StopException brStop) {
                                _breadthPostVisit.clear();
                                _finalEdge = e;
                                _finalVertex = e.getV(currentVert);
                                return;
                            } catch (RejectException brReject) {
                                continue;
                            }
                        }
                    }
                }
                queue.add(_breadthPostVisit.get(index));
                index += 1;
            }
        }
        _breadthLast = false;
    }

    /** Performs a breadth-first traversal of G over all vertices
     *  reachable from V.  That is, the fringe is a sequence and
     *  vertices are added to it at one end and removed from it at the
     *  other in an undefined order.  After the traversal of all successors of
     *  a node is complete, the node itself is revisited by calling
     *  the postVisit method on it. */
    public void breadthFirstTraverse(Graph<VLabel, ELabel> G,
                                     Graph<VLabel, ELabel>.Vertex v) {
        if (!_breadthLast) {
            _breadthPostVisit.clear();
            _visited.clear();
        }
        if (_visited.contains(v)) {
            _breadthLast = false;
            return;
        }
        _breadthLast = true;
        _graph = G;
        LinkedList<Graph<VLabel, ELabel>.Vertex> queue = new
                LinkedList<Graph<VLabel, ELabel>.Vertex>();
        queue.add(v);
        breadthHelper(G, queue);
    }

    /** Continue the previous traversal starting from V.
     *  Continuing a traversal means that we do not traverse
     *  vertices or edges that have been traversed previously. */
    public void continueTraversing(Graph<VLabel, ELabel>.Vertex v) {
        if (_depthLast) {
            depthFirstTraverse(_graph, v);
        } else if (_breadthLast) {
            breadthFirstTraverse(_graph, v);
        } else {
            traverse(_graph, v, _travComparator);
        }
    }

    /** A comparator that compares vertices. It takes in a Comparator<VLabel>
     *  and stores it. */
    private class CompareVertex implements
        Comparator<Graph<VLabel, ELabel>.Vertex> {

        /** Constructor that stores a Comparator<VLabel> COMPARATOR in
         *  variable _COMPARATOR. */
        public CompareVertex(Comparator<VLabel> comparator) {
            _comparator = comparator;
        }

        @Override
        public int compare(Graph<VLabel, ELabel>.Vertex V0,
                Graph<VLabel, ELabel>.Vertex V1) {
            return _comparator.compare(V0.getLabel(), V1.getLabel());
        }

        /** Stores a Comparator<VLabel> that will be used to compare
         *  vertices. */
        private Comparator<VLabel> _comparator;
    }

    /** If the traversal ends prematurely, returns the Vertex argument to
     *  preVisit that caused a Visit routine to return false.  Otherwise,
     *  returns null. */
    public Graph<VLabel, ELabel>.Vertex finalVertex() {
        return _finalVertex;
    }

    /** If the traversal ends prematurely, returns the Edge argument to
     *  preVisit that caused a Visit routine to return false. If it was not
     *  an edge that caused termination, returns null. */
    public Graph<VLabel, ELabel>.Edge finalEdge() {
        return _finalEdge;
    }

    /** Returns the graph currently being traversed.  Undefined if no traversal
     *  is in progress. */
    protected Graph<VLabel, ELabel> theGraph() {
        return _graph;
    }

    /** Method to be called when adding the node at the other end of E from V0
     *  to the fringe. If this routine throws a StopException,
     *  the traversal ends.  If it throws a RejectException, the edge
     *  E is not traversed. The default does nothing.
     */
    protected void preVisit(Graph<VLabel, ELabel>.Edge e,
                            Graph<VLabel, ELabel>.Vertex v0) {
    }

    /** Method to be called when visiting vertex V.  If this routine throws
     *  a StopException, the traversal ends.  If it throws a RejectException,
     *  successors of V do not get visited from V. The default does nothing. */
    protected void visit(Graph<VLabel, ELabel>.Vertex v) {
    }

    /** Method to be called immediately after finishing the traversal
     *  of successors of vertex V in pre- and post-order traversals.
     *  If this routine throws a StopException, the traversal ends.
     *  Throwing a RejectException has no effect. The default does nothing.
     */
    protected void postVisit(Graph<VLabel, ELabel>.Vertex v) {
    }

    /** The Vertex (if any) that terminated the last traversal. */
    protected Graph<VLabel, ELabel>.Vertex _finalVertex;
    /** The Edge (if any) that terminated the last traversal. */
    protected Graph<VLabel, ELabel>.Edge _finalEdge;
    /** The last graph traversed. */
    protected Graph<VLabel, ELabel> _graph;
    /** An arrayList that stores the vertices that have been visited. */
    private ArrayList<Graph<VLabel, ELabel>.Vertex> _visited = new
            ArrayList<Graph<VLabel, ELabel>.Vertex>();
    /** True iff the last traversal done was a depth first traversal.
     *  Initially false. */
    private boolean _depthLast = false;
    /** True iff the last traversal done was a breadth first traversal.
     *  Initially false. */
    private boolean _breadthLast = false;
    /** True iff the last traversal done was a regular traversal. Initially
     *  false. */
    private boolean _regTravLast = false;
    /** A list used to keep track of when to postvisit when using
     *  breadthFirstTraverse. */
    private ArrayList<Graph<VLabel, ELabel>.Vertex> _breadthPostVisit = new
            ArrayList<Graph<VLabel, ELabel>.Vertex>();
    /** A list used to keep track of vertices that have been previsited when
     *  using breadthFirstTraverse. */
    private ArrayList<Graph<VLabel, ELabel>.Vertex> _preVisit = new
            ArrayList<Graph<VLabel, ELabel>.Vertex>();
    /** A list used to keep track of vertices that have been postvisited when
     *  using depthFirstTraverse. */
    private ArrayList<Graph<VLabel, ELabel>.Vertex> _depthPostVisit = new
            ArrayList<Graph<VLabel, ELabel>.Vertex>();
    /** A Comparator<VLabel> that compares vertex labels in regular
     *  traversals. */
    private Comparator<VLabel> _travComparator;
}
