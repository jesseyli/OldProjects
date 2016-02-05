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
 *  @author Unzi Park
 */
public class Traversal<VLabel, ELabel> {

    /** A comparator made to compare vertices. It takes in a
     *  Comparator<VLabel>. */
    private class CompareVertex implements
        Comparator<Graph<VLabel, ELabel>.Vertex> {

        /** Initializes an instance of CompareVertex and sets its _C
         *  variable equal to a Comparator<VLabel> C. */
        public CompareVertex(Comparator<VLabel> comparer) {
            _comparer = comparer;
        }

        @Override
        public int compare(Graph<VLabel, ELabel>.Vertex v1,
                Graph<VLabel, ELabel>.Vertex v2) {
            return _comparer.compare(v1.getLabel(), v2.getLabel());
        }

        /** Instantiate a Comparator<VLabel> that will be a comparator used
         *  for comparisons. */
        private Comparator<VLabel> _comparer;
    }

    /** Perform a traversal of G over all vertices reachable from V.
     *  ORDER determines the ordering in which the fringe of
     *  untraversed vertices is visited. */
    public void traverse(Graph<VLabel, ELabel> G,
                         Graph<VLabel, ELabel>.Vertex v,
                         Comparator<VLabel> order) {
        if (!traversedLast) {
            _visited.clear();
        }
        if (_visited.contains(v)) {
            traversedLast = false;
        } else {
            PriorityQueue<Graph<VLabel, ELabel>.Vertex> queue = 
                    new PriorityQueue<Graph<VLabel, ELabel>.Vertex>(
                            G.vertexSize(), new CompareVertex(order));
            queue.add(v);
            lastComparator = order;
            _graph = G;
            traversedLast = true;
            boolean stopTrav = false;
            while (!queue.isEmpty()) {
                try {
                    _visited.add(queue.peek());
                    visit(queue.peek());
                } catch (StopException travStopped) {
                    _finalEdge = null;
                    _finalVertex = queue.peek();
                    return;
                } catch (RejectException TravRejected) {
                    stopTrav = true;
                }
                if (!stopTrav) {
                    Graph<VLabel, ELabel>.Vertex currVert = queue.poll();
                    for (Graph<VLabel, ELabel>.Edge e: G.outEdges(currVert)) {
                        if (!_visited.contains(e.getV(currVert))) {
                            try {
                                preVisit(e, e.getV(currVert));
                                queue.add(e.getV(currVert));
                            } catch (StopException
                                    | RejectException StopOrRejectTrav) {
                                return;
                            }
                        }
                    }
                } else {
                    queue.poll();
                }
            }
            traversedLast = false;
        }
    }

    /** Performs a depth-first traversal of G over all vertices
     *  reachable from V.  That is, the fringe is a sequence and
     *  vertices are added to it or removed from it at one end in
     *  an undefined order.  After the traversal of all successors of
     *  a node is complete, the node itself is revisited by calling
     *  the postVisit method on it. */
    public void depthFirstTraverse(Graph<VLabel, ELabel> G,
                                   Graph<VLabel, ELabel>.Vertex v) {
        if (!depthLast) {
            _visited.clear();
            depthPostVisited.clear();
        }
        if (_visited.contains(v)) {
            depthLast = false;
            return;
        }
        _graph = G;
        depthLast = true;
        Stack<Graph<VLabel, ELabel>.Vertex> dStack =
            new Stack<Graph<VLabel, ELabel>.Vertex>();
        dStack.push(v);
        depthHelper(G, dStack);
    }

    /** Helper method for depthFirstTraverse that performs the iteration
     *  on graph GRAPH with stack STACK. */
    public void depthHelper(Graph<VLabel, ELabel> graph, Stack<Graph<VLabel,
            ELabel>.Vertex> stack) {
        boolean stopTrav = false;
        while (!stack.empty()) {
            if (!_visited.contains(stack.peek())) {
                try {
                    _visited.add(stack.peek());
                    visit(stack.peek());
                } catch (StopException stop) {
                    _finalEdge = null;
                    _finalVertex = stack.peek();
                    return;
                } catch (RejectException reject) {
                    stopTrav = true;
                }
                if (stopTrav) {
                    if (!depthPostVisited.contains(stack.peek())) {
                        if (depthPostHelper(stack) == 0) {
                            return;
                        }
                    }
                    stopTrav = false;
                    stack.pop();
                } else {
                    Graph<VLabel, ELabel>.Vertex currVert = stack.peek();
                    for (Graph<VLabel, ELabel>.Edge e:
                        graph.outEdges(currVert)) {
                        if (!_visited.contains(e.getV(currVert))) {
                            try {
                                preVisit(e, e.getV(currVert));
                                stack.push(e.getV(currVert));
                            } catch (StopException stop) {
                                _finalVertex =
                                    e.getV(e.getV(currVert));
                                _finalEdge = e;
                                return;
                            } catch (RejectException reject) {
                            }
                        }
                    }
                }
            } else {
                if (!depthPostVisited.contains(stack.peek())) {
                    if (depthPostHelper(stack) == 0) {
                        return;
                    }
                }
                stack.pop();
            }
        }
        depthLast = false;
    }

    /** Helper for depthFirstTraverse that goes through the postvisit for the
     *  first element of stack STACK and returns 0 if a StopException is
     *  thrown. Otherwise returns 1. */
    public int depthPostHelper(Stack<Graph<VLabel, ELabel>.Vertex> stack) {
        try {
            depthPostVisited.add(stack.peek());
            postVisit(stack.peek());
        } catch (StopException stop) {
            _finalEdge = null;
            _finalVertex = stack.pop();
            return 0;
        } catch (RejectException reject) {
        }
        return 1;
    }

    /** Performs a breadth-first traversal of G over all vertices
     *  reachable from V.  That is, the fringe is a sequence and
     *  vertices are added to it at one end and removed from it at the
     *  other in an undefined order.  After the traversal of all successors of
     *  a node is complete, the node itself is revisited by calling
     *  the postVisit method on it. */
    public void breadthFirstTraverse(Graph<VLabel, ELabel> G,
                                     Graph<VLabel, ELabel>.Vertex v) {
        if (!breadthLast) {
            _visited.clear();
            _postBreadth.clear();
        }
        if (_visited.contains(v)) {
            breadthLast = false;
            return;
        }
        LinkedList<Graph<VLabel, ELabel>.Vertex> queue =
            new LinkedList<Graph<VLabel, ELabel>.Vertex>();
        _graph = G;
        breadthLast = true;
        queue.add(v);
        breadthHelper(G, queue);
    }

    /** A helper method for a breadth first traversal that takes in a GRAPH of
     *  the current graph that you are traversing and the queue queue that
     *  contains vertices that need to be visited and post visited. */
    public void breadthHelper(Graph<VLabel, ELabel> graph,
            LinkedList<Graph<VLabel, ELabel>.Vertex> queue) {
        int counter = 0;
        boolean stopTrav = false;
        while (queue.peekFirst() != null) {
            if (_postBreadth.contains(queue.peek())) {
                try {
                    postVisit(queue.peek());
                } catch (StopException stop) {
                    _finalEdge = null;
                    _finalVertex = queue.pop();
                    _postBreadth.clear();
                    return;
                } catch (RejectException reject) {
                }
                queue.pop();
            } else {
                try {
                    _visited.add(queue.peek());
                    visit(queue.peek());
                } catch (StopException stop) {
                    _finalEdge = null;
                    _finalVertex = queue.pop();
                    _postBreadth.clear();
                    return;
                } catch (RejectException reject) {
                    stopTrav = true;
                }
                _postBreadth.add(queue.peek());
                if (stopTrav) {
                    queue.pop();
                } else {
                    Graph<VLabel, ELabel>.Vertex currVert = queue.pop();
                    for (Graph<VLabel, ELabel>.Edge e: graph.outEdges(
                            currVert)) {
                        if (!_visited.contains(e.getV(currVert))) {
                            try {
                                preVisit(e, e.getV(currVert));
                                if (!bPreVisited.contains(e.getV(currVert))) {
                                    queue.add(e.getV(currVert));
                                    bPreVisited.add(e.getV(currVert));
                                }
                            } catch (StopException stop) {
                                _finalEdge = e;
                                _finalVertex = e.getV(currVert);
                                _postBreadth.clear();
                                return;
                            } catch (RejectException reject) {
                            }
                        }
                    }
                }
                queue.add(_postBreadth.get(counter));
                counter += 1;
            }
        }
        breadthLast = false;
    }


    /** Continue the previous traversal starting from V.
     *  Continuing a traversal means that we do not traverse
     *  vertices or edges that have been traversed previously. */
    public void continueTraversing(Graph<VLabel, ELabel>.Vertex v) {
        if (traversedLast) {
            traverse(_graph, v, lastComparator);
        } else if (depthLast) {
            depthFirstTraverse(_graph, v);
        } else if (breadthLast) {
            breadthFirstTraverse(_graph, v);
        } else {
            System.out.println("No previous traversal.");
        }
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
    /** The graph currently being traversed. */
    protected Graph<VLabel, ELabel> _graph;
    /** An arrayList that holds all vertices that have been visited in a
     *  traversal. */
    private ArrayList<Graph<VLabel, ELabel>.Vertex> _visited =
        new ArrayList<Graph<VLabel, ELabel>.Vertex>();
    /** An arrayList that holds all vertices that have been postvisited in a
     *  traversal. */
    private ArrayList<Graph<VLabel, ELabel>.Vertex> postVisited =
        new ArrayList<Graph<VLabel, ELabel>.Vertex>();
    /** Boolean that is true iff the most recent traversal being done
     *  was a depth first traversal. */
    private boolean depthLast = false;
    /** Boolean that is true iff the most recent traversal being done
     *  was a breadth first traversal. */
    private boolean breadthLast = false;
    /** Boolean that is true iff the most recent traversal being done
     *  was a regular traversal based on a comparator. */
    private boolean traversedLast = false;
    /** An ArrayList that keeps track of when to postvisit a breadth first
     *  traversal. */
    private ArrayList<Graph<VLabel, ELabel>.Vertex> _postBreadth =
            new ArrayList<Graph<VLabel, ELabel>.Vertex>();
    /** ArrayList that keeps track of previsited vertices for a breadth first
     *  traversal. */
    private ArrayList<Graph<VLabel, ELabel>.Vertex> bPreVisited =
            new ArrayList<Graph<VLabel, ELabel>.Vertex>();
    /** ArrayList that keeps track of postvisited vertices for a depth first
     *  traversal. */
    private ArrayList<Graph<VLabel, ELabel>.Vertex> depthPostVisited =
            new ArrayList<Graph<VLabel, ELabel>.Vertex>();
    /** A Comparator<VLabel> that compares VLabels in traverse. */
    private Comparator<VLabel> lastComparator;
}
