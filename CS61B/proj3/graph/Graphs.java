package graph;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;
import java.util.PriorityQueue;

/** Assorted graph algorithms.
 *  @author Jesse Li
 */
public final class Graphs {

    /* A* Search Algorithms */

    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the edge weighter EWEIGHTER.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. H.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. H.dist(v, w) <= H.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, uses VWEIGHTER to set the weight of vertex v
     *  to the weight of a minimal path from V0 to v, for each v in
     *  the returned path and for each v such that
     *       minimum path length from V0 to v + H.dist(v, V1)
     *              < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.  If V1 is
     *  unreachable from V0, returns null and sets the minimum path weights of
     *  all reachable nodes.  The distance to a node unreachable from V0 is
     *  Double.POSITIVE_INFINITY. */
    public static <VLabel, ELabel> List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G,
                 Graph<VLabel, ELabel>.Vertex V0,
                 Graph<VLabel, ELabel>.Vertex V1,
                 Distancer<? super VLabel> h,
                 Weighter<? super VLabel> vweighter,
                 Weighting<? super ELabel> eweighter) {
        ArrayList<Graph<VLabel, ELabel>.Vertex> evaluated =
            new ArrayList<Graph<VLabel, ELabel>.Vertex>();
        HashMap<Graph<VLabel, ELabel>.Vertex, Graph<VLabel, ELabel>.Edge>
            checked = new HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge>();
        final HashMap<Graph<VLabel, ELabel>.Vertex, Double> fCost =
                new HashMap<Graph<VLabel, ELabel>.Vertex, Double>();
        HashMap<Graph<VLabel, ELabel>.Vertex, Double> gCost =
            new HashMap<Graph<VLabel, ELabel>.Vertex, Double>();
        Comparator<Graph<VLabel, ELabel>.Vertex> heuristic
            = new Comparator<Graph<VLabel, ELabel>.Vertex>() {
                @Override
                public int compare(Graph<VLabel, ELabel>.Vertex v0,
                    Graph<VLabel, ELabel>.Vertex v1) {
                    if (fCost.get(v0) > fCost.get(v1)) {
                        return 1;
                    } else if (fCost.get(v0) == fCost.get(v1)) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            };
        PriorityQueue<Graph<VLabel, ELabel>.Vertex> path = new PriorityQueue
            <Graph<VLabel, ELabel>.Vertex>(G.vertexSize(), heuristic);
        path.add(V0);
        gCost.put(V0, 0.0);
        checked.put(V0, null);
        fCost.put(V0, gCost.get(V0) + h.dist(V0.getLabel(), V1.getLabel()));
        while (!path.isEmpty()) {
            Graph<VLabel, ELabel>.Vertex curr = path.poll();
            if (curr.equals(V1)) {
                return reconstructPath(checked, curr);
            }
            evaluated.add(curr);
            for (Graph<VLabel, ELabel>.Edge outEdge: G.outEdges(curr)) {
                Graph<VLabel, ELabel>.Edge e = outEdge;
                Graph<VLabel, ELabel>.Vertex v = e.getV(curr);
                Double currgCost = eweighter.weight(e.getLabel())
                        + gCost.get(curr);
                Double currfCost = h.dist(v.getLabel(), V1.getLabel())
                        + currgCost;
                if (evaluated.contains(v) && currfCost >= fCost.get(v)) {
                    continue;
                }
                if (!path.contains(v) || currfCost < fCost.get(v)) {
                    fCost.put(v, currfCost);
                    gCost.put(v, currgCost);
                    checked.put(v, e);
                    vweighter.setWeight(v.getLabel(), gCost.get(v));
                    if (!path.contains(v)) {
                        path.add(v);
                    }
                }
            }
        }
        return null;
    }

    /** Returns a path from V0 to V1 in G of minimum weight, according
     *  to the weights of its edge labels.  VLABEL and ELABEL are the types of
     *  vertex and edge labels.  Assumes that H is a distance measure
     *  between vertices satisfying the two properties:
     *     a. H.dist(v, V1) <= shortest path from v to V1 for any v, and
     *     b. H.dist(v, w) <= H.dist(w, V1) + weight of edge (v, w), where
     *        v and w are any vertices in G.
     *
     *  As a side effect, sets the weight of vertex v to the weight of
     *  a minimal path from V0 to v, for each v in the returned path
     *  and for each v such that
     *       minimum path length from V0 to v + H.dist(v, V1)
     *           < minimum path length from V0 to V1.
     *  The final weights of other vertices are not defined.
     *
     *  This function has the same effect as the 6-argument version of
     *  shortestPath, but uses the .weight and .setWeight methods of
     *  the edges and vertices themselves to determine and set
     *  weights. If V1 is unreachable from V0, returns null and sets
     *  the minimum path weights of all reachable nodes.  The distance
     *  to a node unreachable from V0 is Double.POSITIVE_INFINITY. */
    public static
    <VLabel extends Weightable, ELabel extends Weighted>
    List<Graph<VLabel, ELabel>.Edge>
    shortestPath(Graph<VLabel, ELabel> G,
                 Graph<VLabel, ELabel>.Vertex V0,
                 Graph<VLabel, ELabel>.Vertex V1,
                 Distancer<? super VLabel> h) {
        Weighter<VLabel> vWeighter = new Weighter<VLabel>() {
            @Override
            public void setWeight(VLabel label, double v) {
                label.setWeight(v);
            }
            @Override
            public double weight(VLabel label) {
                return label.weight();
            }
        };

        Weighting<ELabel> eWeighting = new Weighting<ELabel>() {
            @Override
            public double weight(ELabel label) {
                return label.weight();
            }
        };
        return shortestPath(G, V0, V1, h, vWeighter, eWeighting);
    }

    /** Returns a distancer whose dist method always returns 0. */
    public static final Distancer<Object> ZERO_DISTANCER =
        new Distancer<Object>() {
            @Override
            public double dist(Object v0, Object v1) {
                return 0.0;
            }
        };

    /** Helper that adds the edge from PATHMAP to the list PATH. V is the
     *  current vertex. Returns the updated PATH. VLABEL and ELABEL are vertex
     *  and edge labels. */
    private static <VLabel, ELabel> ArrayList<Graph<VLabel, ELabel>.Edge>
    pathHelper(ArrayList<Graph<VLabel, ELabel>.Edge> path,
            HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge> pathMap,
            Graph<VLabel, ELabel>.Vertex v) {
        if (pathMap.get(v) != null) {
            path.add(0, pathMap.get(v));
            return pathHelper(path, pathMap,
                pathMap.get(v).getV(v));
        }
        return path;
    }

    /** Creates and returns the shortest path by taking edges from a HashMap
     *  PATHMAP that maps vertices to edges. V represents the current
     *  vertex. VLABEL and ELABEL are vertex and edge labels. */
    public static <VLabel, ELabel> ArrayList<Graph<VLabel, ELabel>.Edge>
    reconstructPath(HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge> pathMap,
            Graph<VLabel, ELabel>.Vertex v) {
        return pathHelper(new ArrayList<Graph<VLabel, ELabel>.Edge>(),
            pathMap, v);
    }

}
