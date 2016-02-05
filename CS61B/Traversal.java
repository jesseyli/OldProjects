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
 *  @author
 */
public class Traversal<VLabel, ELabel> {

    /** A comparator class specially made that compares vertices. It takes in
     *  a Comparator<VLabel>. */
    private class CompareVertex implements
        Comparator<Graph<VLabel, ELabel>.Vertex> {

        /** Initialize a CompareVertex instance variable that sets its _C
         *  variable equal to a Comparator<VLabel> C. */
        public CompareVertex(Comparator<VLabel> c) {
            _c = c;
        }

        @Override
        public int compare(Graph<VLabel, ELabel>.Vertex v1,
                Graph<VLabel, ELabel>.Vertex v2) {
            return _c.compare((VLabel) v1.getLabel(), (VLabel) v2.getLabel());
        }

        /** Instantiate a Comparator<VLabel> that will be a comparator used
         *  for comparisons. */
        private Comparator<VLabel> _c;
    }

    /** Perform a traversal of G over all vertices reachable from V.
     *  ORDER determines the ordering in which the fringe of
     *  untraversed vertices is visited. */
    public void traverse(Graph<VLabel, ELabel> G,
                         Graph<VLabel, ELabel>.Vertex v,
                         Comparator<VLabel> order) {
        if (!trav) {
            visited.clear();
        }
        if (visited.contains(v)) {
            trav = false;
            return;
        }
        trav = true;
        _graph = G;
        boolean noSuccTrav = false;
        travComp = order;
        PriorityQueue<Graph<VLabel, ELabel>.Vertex> travQueue =
            new PriorityQueue<Graph<VLabel, ELabel>.Vertex>(
                _graph.vertexSize(), new CompareVertex(travComp));
        travQueue.add(v);
        while (!travQueue.isEmpty()) {
            visited.add(travQueue.peek());
            try {
                visit(travQueue.peek());
            } catch (StopException visStTrav) {
                _finalVertex = travQueue.peek();
                _finalEdge = null;
                return;
            } catch (RejectException visRejTrav) {
                noSuccTrav = true;
            }
            if (!noSuccTrav) {
                Graph<VLabel, ELabel>.Vertex travVert = travQueue.poll();
                for (Graph<VLabel, ELabel>.Edge out
                        : _graph.outEdges(travVert)) {
                    if (!visited.contains(out.getV(travVert))) {
                        try {
                            preVisit(out, out.getV(travVert));
                            travQueue.add(out.getV(travVert));
                        } catch (StopException preStTrav) {
                            return;
                        } catch (RejectException preRejTrav) {
                            return;
                        }
                    }
                }
            } else {
                travQueue.poll();
            }
        }
        trav = false;
    }

    /** Performs a depth-first traversal of G over all vertices
     *  reachable from V.  That is, the fringe is a sequence and
     *  vertices are added to it or removed from it at one end in
     *  an undefined order.  After the traversal of all successors of
     *  a node is complete, the node itself is revisited by calling
     *  the postVisit method on it. */
    public void depthFirstTraverse(Graph<VLabel, ELabel> G,
                                   Graph<VLabel, ELabel>.Vertex v) {
        if (!depth) {
            visited.clear();
            depthPostVisited.clear();
        }
        if (visited.contains(v)) {
            depth = false;
            return;
        }
        depth = true;
        _graph = G;
        Stack<Graph<VLabel, ELabel>.Vertex> dStack =
            new Stack<Graph<VLabel, ELabel>.Vertex>();
        dStack.push(v);
        depthFirstHelper(dStack, _graph);
    }

    /** Helper method for the depth first traversal that performs the
     *  iteration over the current graph GRAPH based on the stack STACK. */
    public void depthFirstHelper(Stack<Graph<VLabel, ELabel>.Vertex> stack,
            Graph<VLabel, ELabel> graph) {
        boolean noSuccDep = false;
        while (!stack.empty()) {
            if (!visited.contains(stack.peek())) {
                visited.add(stack.peek());
                try {
                    visit(stack.peek());
                } catch (StopException visStDep) {
                    _finalVertex = stack.peek();
                    _finalEdge = null;
                    return;
                } catch (RejectException visRejDep) {
                    noSuccDep = true;
                }
                if (!noSuccDep) {
                    Graph<VLabel, ELabel>.Vertex dfVert = stack.peek();
                    for (Graph<VLabel, ELabel>.Edge outEdge
                            : graph.outEdges(dfVert)) {
                        if (!visited.contains(outEdge.getV(dfVert))) {
                            try {
                                preVisit(outEdge, outEdge.getV(dfVert));
                                stack.push(outEdge.getV(dfVert));
                            } catch (StopException preStDep) {
                                _finalVertex =
                                    outEdge.getV(outEdge.getV(dfVert));
                                _finalEdge = outEdge;
                                return;
                            } catch (RejectException preRejDep) {
                            }
                        }
                    }
                } else {
                    if (!depthPostVisited.contains(stack.peek())) {
                        if (depthFirstPostHelp(stack) == 0) {
                            return;
                        }
                    }
                    stack.pop();
                    noSuccDep = false;
                }
            } else {
                if (!depthPostVisited.contains(stack.peek())) {
                    if (depthFirstPostHelp(stack) == 0) {
                        return;
                    }
                }
                stack.pop();
            }
        }
        depth = false;
    }

    /** Helper method for a depth first traversal that goes through the post
     *  visit for the frst element on a stack, PVSTACK, and returns 0 if a
     *  StopException is thrown. */
    public int depthFirstPostHelp(Stack<Graph<VLabel, ELabel>.Vertex> pvStack) {
        try {
            depthPostVisited.add(pvStack.peek());
            postVisit(pvStack.peek());
        } catch (StopException postStDep) {
            _finalVertex = pvStack.pop();
            _finalEdge = null;
            return 0;
        } catch (RejectException postRejDep) {
            return 1;
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
        if (!breadth) {
            visited.clear();
            breadthPv.clear();
        }
        if (visited.contains(v)) {
            breadth = false;
            return;
        }
        _graph = G;
        breadth = true;
        LinkedList<Graph<VLabel, ELabel>.Vertex> bQueue =
            new LinkedList<Graph<VLabel, ELabel>.Vertex>();
        bQueue.add(v);
        breadthFirstHelper(_graph, bQueue);
    }

    /** A helper method for a breadth first traversal that takes in a GRAPH of
     *  the current graph that you are traversing and the queue BQ that
     *  contains vertices that need to be visited and post visited. */
    public void breadthFirstHelper(Graph<VLabel, ELabel> graph,
            LinkedList<Graph<VLabel, ELabel>.Vertex> bq) {
        boolean noSuccBr = false;
        int location = 0;
        while (bq.peekFirst() != null) {
            if (breadthPv.contains(bq.peek())) {
                try {
                    postVisit(bq.peek());
                } catch (StopException postStBr) {
                    _finalVertex = bq.pop();
                    _finalEdge = null;
                    breadthPv.clear();
                    return;
                } catch (RejectException postRejBr) {
                }
                bq.pop();
            } else {
                visited.add(bq.peek());
                try {
                    visit(bq.peek());
                } catch (StopException visStBr) {
                    _finalVertex = bq.pop();
                    _finalEdge = null;
                    breadthPv.clear();
                    return;
                } catch (RejectException visRejBr) {
                    noSuccBr = true;
                }
                breadthPv.add(bq.peek());
                if (!noSuccBr) {
                    Graph<VLabel, ELabel>.Vertex iter = bq.pop();
                    for (Graph<VLabel, ELabel>.Edge out
                            : graph.outEdges(iter)) {
                        if (!visited.contains(out.getV(iter))) {
                            try {
                                preVisit(out, out.getV(iter));
                                if (!bPreVisited.contains(out.getV(iter))) {
                                    bPreVisited.add(out.getV(iter));
                                    bq.add(out.getV(iter));
                                }
                            } catch (StopException preStBr) {
                                _finalVertex = out.getV(iter);
                                _finalEdge = out;
                                breadthPv.clear();
                                return;
                            } catch (RejectException preRejBr) {
                            }
                        }
                    }
                } else {
                    bq.pop();
                }
                bq.add(breadthPv.get(location));
                location += 1;
            }
        }
        breadth = false;
    }


    /** Continue the previous traversal starting from V.
     *  Continuing a traversal means that we do not traverse
     *  vertices or edges that have been traversed previously. */
    public void continueTraversing(Graph<VLabel, ELabel>.Vertex v) {
        if (trav) {
            traverse(_graph, v, travComp);
        } else if (depth) {
            depthFirstTraverse(_graph, v);
        } else if (breadth) {
            breadthFirstTraverse(_graph, v);
        } else {
            System.out.println("No traversal to continue from");
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
    private ArrayList<Graph<VLabel, ELabel>.Vertex> visited =
        new ArrayList<Graph<VLabel, ELabel>.Vertex>();

    /** An arrayList that holds all vertices that have been postvisited in a
     *  traversal. */
    private ArrayList<Graph<VLabel, ELabel>.Vertex> postVisited =
        new ArrayList<Graph<VLabel, ELabel>.Vertex>();

    /** Boolean that is true iff the most recent traversal being done
     *  was a depth first traversal. */
    private boolean depth = false;

    /** Boolean that is true iff the most recent traversal being done
     *  was a breadth first traversal. */
    private boolean breadth = false;

    /** Boolean that is true iff the most recent traversal being done
     *  was a regular traversal based on a comparator. */
    private boolean trav = false;

    /** ArrayList that keeps track of when to postvisit for a breadth first
     *  traversal. */
    private ArrayList<Graph<VLabel, ELabel>.Vertex> breadthPv =
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
    private Comparator<VLabel> travComp;

}
