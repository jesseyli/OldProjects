package graph;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Comparator;
import java.util.PriorityQueue;

/** Assorted graph algorithms.
 *  @author Unzi Park
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
        
        HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge> navigated = new HashMap<Graph<VLabel,
            ELabel>.Vertex, Graph<VLabel, ELabel>.Edge>();
        
        HashMap<Graph<VLabel, ELabel>.Vertex, Double> gScore =
            new HashMap<Graph<VLabel, ELabel>.Vertex, Double>();
        
        final HashMap<Graph<VLabel, ELabel>.Vertex, Double> fScore =
            new HashMap<Graph<VLabel, ELabel>.Vertex, Double>();
        
        Comparator<Graph<VLabel, ELabel>.Vertex> heuristic
            = new Comparator<Graph<VLabel, ELabel>.Vertex>() {
                @Override
                public int compare(Graph<VLabel, ELabel>.Vertex v0,
                    Graph<VLabel, ELabel>.Vertex v1) {
                    if (fScore.get(v0) > fScore.get(v1)) {
                        return 1;
                    } else if (fScore.get(v0) < fScore.get(v1)) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            };
        PriorityQueue<Graph<VLabel, ELabel>.Vertex> pathQ = new PriorityQueue
            <Graph<VLabel, ELabel>.Vertex>(G.vertexSize(), heuristic);
        navigated.put(V0, null);
        gScore.put(V0, 0.0);
        pathQ.add(V0);
        fScore.put(V0, gScore.get(V0) + h.dist(V0.getLabel(), V1.getLabel()));
        while (!pathQ.isEmpty()) {
            Graph<VLabel, ELabel>.Vertex temp = pathQ.poll();
            if (temp.equals(V1)) {
                return reconstructPath(navigated, temp);
            }
            evaluated.add(temp);
            for (Graph<VLabel, ELabel>.Edge e : G.outEdges(temp)) {
                Graph<VLabel, ELabel>.Vertex v = e.getV(temp);
                Double tempgScore = gScore.get(temp)
                    + eweighter.weight(e.getLabel());
                Double tempfScore = tempgScore
                    + h.dist(v.getLabel(), V1.getLabel());
                if (evaluated.contains(v) && tempfScore >= fScore.get(v)) {
                    continue;
                }
                if (!pathQ.contains(v) || tempfScore < fScore.get(v)) {
                    navigated.put(v, e);
                    gScore.put(v, tempgScore);
                    fScore.put(v, tempfScore);
                    vweighter.setWeight(v.getLabel(), gScore.get(v));
                    if (!pathQ.contains(v)) {
                        pathQ.add(v);
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
        Weighter<VLabel> weighter = new Weighter<VLabel>() {
            @Override
            public void setWeight(VLabel x, double v) {
                x.setWeight(v);
            }
            @Override
            public double weight(VLabel x) {
                return x.weight();
            }
        };

        Weighting<ELabel> weighting = new Weighting<ELabel>() {
            @Override
            public double weight(ELabel x) {
                return x.weight();
            }
        };
        return shortestPath(G, V0, V1, h, weighter, weighting);
    }

    /** Returns a distancer whose dist method always returns 0. */
    public static final Distancer<Object> ZERO_DISTANCER =
        new Distancer<Object>() {
            @Override
            public double dist(Object v0, Object v1) {
                return 0.0;
            }
        };


    /** Reconstructs the path and returns the shortest path by recursively
     *  edges from MAP HashMap<VLabel, ELbael> with V as the current
     *  vertex. */
    public static <VLabel, ELabel> ArrayList<Graph<VLabel, ELabel>.Edge>
    reconstructPath(HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge> map,
            Graph<VLabel, ELabel>.Vertex v) {
        return reconstructHelper(new ArrayList<Graph<VLabel, ELabel>.Edge>(),
            map, v);
    }

    /** Helper recursively calls edges from HashMap<VLabel, ELabel> MAP
     *  based on vertex V and adds it to PATH. */
    private static <VLabel, ELabel> ArrayList<Graph<VLabel, ELabel>.Edge>
    reconstructHelper(ArrayList<Graph<VLabel, ELabel>.Edge> path,
            HashMap<Graph<VLabel, ELabel>.Vertex,
            Graph<VLabel, ELabel>.Edge> map,
            Graph<VLabel, ELabel>.Vertex v) {
        if (map.get(v) == null) {
            return path;
        } else {
            path.add(0, map.get(v));
            return reconstructHelper(path, map,
                map.get(v).getV(v));
        }
    }
}
