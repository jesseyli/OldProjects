package graph;

import java.util.ArrayList;
import java.util.Iterator;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may add bodies to abstract methods, modify
 * existing bodies, or override inherited methods.  */

/** An undirected graph with vertices labeled with VLABEL and edges
 *  labeled with ELABEL.
 *  @author Jesse Li
 */
public class UndirectedGraph<VLabel, ELabel> extends Graph<VLabel, ELabel> {

    /** An empty graph. */
    public UndirectedGraph() {
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public int outDegree(Vertex v) {
        int counter = 0;
        for (int i = 0; i < getEdges().size(); i += 1) {
            if (getEdges().get(i).getV0().equals(v)
                    || getEdges().get(i).getV1().equals(v)) {
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
        for (int i = 0; i < edgeSize(); i += 1) {
            if (getEdges().get(i).getV1().equals(u)) {
                if (getEdges().get(i).getV0().equals(v)) {
                    output = true;
                }
            }
        }
        return output;
    }

    @Override
    public boolean contains(Vertex u, Vertex v,
            ELabel label) {
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
        for (int i = 0; i < edgeSize(); i += 1) {
            if (getEdges().get(i).getV1().equals(u)) {
                if (getEdges().get(i).getV0().equals(v)) {
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
        for (int i = 0; i < getEdges().size(); i += 1) {
            if (getEdges().get(i).getV1().equals(v1)
                    && getEdges().get(i).getV0().equals(v2)) {
                getEdges().remove(i);
                i -= 1;
            }
        }
        for (int i = 0; i < getMap().get(v1).size(); i += 1) {
            if (getMap().get(v1).get(i).getV1().equals(v1)) {
                getMap().get(v1).remove(i);
            }
        }
        for (int i = 0; i < getMap().get(v2).size(); i += 1) {
            if (getMap().get(v2).get(i).getV0().equals(v2)) {
                getMap().get(v2).remove(i);
            }
        }
    }

    @Override
    public Iteration<Vertex> successors(Vertex v) {
        ArrayList<Vertex> succ = new ArrayList<Vertex>();
        for (int i = 0; i < getMap().get(v).size(); i += 1) {
            if (getMap().get(v).get(i).getV0().equals(v)) {
                succ.add(getMap().get(v).get(i).getV1());
            }
            if (getMap().get(v).get(i).getV1().equals(v)) {
                succ.add(getMap().get(v).get(i).getV0());
            }
        }
        Iterator<Vertex> successor = succ.iterator();
        return Iteration.iteration(successor);
    }

    @Override
    public Iteration<Edge> outEdges(Vertex v) {
        Iterator<Edge> out = getMap().get(v).iterator();
        return Iteration.iteration(out);
    }

}
