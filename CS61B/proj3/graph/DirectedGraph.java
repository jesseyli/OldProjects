package graph;

import java.util.ArrayList;
import java.util.Iterator;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may add bodies to abstract methods, modify
 * existing bodies, or override inherited methods.  */

/** A directed graph with vertices labeled with VLABEL and edges
 *  labeled with ELABEL.
 *  @author Jesse Li
 */
public class DirectedGraph<VLabel, ELabel> extends Graph<VLabel, ELabel> {

    /** An empty graph. */
    public DirectedGraph() {
    }

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public int outDegree(Vertex v) {
        int counter = 0;
        for (int i = 0; i < getMap().get(v).size(); i += 1) {
            if (getMap().get(v).get(i).getV0().equals(v)) {
                counter += 1;
            }
        }
        return counter;
    }

    @Override
    public boolean contains(Vertex u, Vertex v) {
        boolean output = false;
        for (int i = 0; i < edgeSize(); i += 1) {
            if (getEdges().get(i).getV0().equals(u)) {
                if (getEdges().get(i).getV1().equals(v)) {
                    output = true;
                }
            }
        }
        return output;
    }

    @Override
    public boolean contains(Vertex u, Vertex v, ELabel label) {
        boolean output = false;
        for (int i = 0; i < edgeSize(); i += 1) {
            if (getEdges().get(i).getV0().equals(u)) {
                if (getEdges().get(i).getV1().equals(v)) {
                    if (getEdges().get(i).getLabel().equals(label)) {
                        output = true;
                    }
                }
            }
        }
        return output;
    }

    @Override
    public void remove(Vertex v1, Vertex v2) {
        for (int i = 0; i < getEdges().size(); i += 1) {
            if (getEdges().get(i).getV0().equals(v1)
                    && getEdges().get(i).getV1().equals(v2)) {
                getEdges().remove(i);
                i -= 1;
            }
        }
        for (int i = 0; i < getMap().get(v1).size(); i += 1) {
            if (getMap().get(v1).get(i).getV1().equals(v2)) {
                getMap().get(v1).remove(i);
            }
        }
        for (int i = 0; i < getMap().get(v2).size(); i += 1) {
            if (getMap().get(v2).get(i).getV0().equals(v1)) {
                getMap().get(v2).remove(i);
            }
        }
    }

    @Override
    public Iteration<Vertex> successors(Vertex v) {
        ArrayList<Vertex> success = new ArrayList<Vertex>();
        for (int i = 0; i < getMap().get(v).size(); i += 1) {
            if (getMap().get(v).get(i).getV0().equals(v)) {
                success.add(getMap().get(v).get(i).getV1());
            }
        }
        Iterator<Vertex> successor = success.iterator();
        return Iteration.iteration(successor);
    }

    @Override
    public Iteration<Edge> outEdges(Vertex v) {
        ArrayList<Edge> out = new ArrayList<Edge>();
        for (int i = 0; i < getMap().get(v).size(); i += 1) {
            if (getMap().get(v).get(i).getV0().equals(v)) {
                out.add(getMap().get(v).get(i));
            }
        }
        Iterator<Edge> outedges = out.iterator();
        return Iteration.iteration(outedges);
    }

}
