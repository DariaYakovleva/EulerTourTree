package euler_tour_tree;

import euler_tour_tree.treap.Treap;
import euler_tour_tree.treap.TreapImpl;
import javafx.util.Pair;

import java.util.*;

public class EulerTourTreeHard extends EulerTourTreeAbstract {

    int levels;

    List<EulerTour> eulerTours;
    Map<Edge, Integer> edgeLevels;

    public EulerTourTreeHard(int n) {
        this.n = n;
        levels = (int)(Math.log(n));
        edgeLevels = new HashMap<>();
        eulerTours = new ArrayList<>();
        for (int level = 0; level < levels; level++)
            eulerTours.add(new EulerTour(level));
    }

    private void link(int vertex1, int vertex2, int level) {
        if (vertex1 == vertex2)
            return;
        Edge e1 = new Edge(vertex1, vertex2);
        if (eulerTours.get(level).getEdgeToTreap().containsKey(e1))
            return;
        Edge e2 = new Edge(vertex2, vertex1);
        edgeLevels.put(e1, Math.max(edgeLevels.getOrDefault(e1, 0), level));
        edgeLevels.put(e2, Math.max(edgeLevels.getOrDefault(e2, 0), level));
        if (isConnected(vertex1, vertex2, level)) {
            eulerTours.get(level).getVertexToFreeEdges().get(vertex1).add(e1);
            eulerTours.get(level).getVertexToFreeEdges().get(vertex2).add(e2);
            return;
        }

        Map<Integer, Set<Treap>> vertexToTreap = eulerTours.get(level).getVertexToTreaps();
        Set<Treap> eulerPaths = eulerTours.get(level).getEulerPaths(); // TODO: надо ли?
        Map<Edge, Treap> edgeToTreap = eulerTours.get(level).getEdgeToTreap();

        Pair<Treap, Treap> p1;
        if (!vertexToTreap.get(vertex1).isEmpty()) {
            Treap t1 = vertexToTreap.get(vertex1).iterator().next();
            eulerPaths.remove(t1.getRoot());
            p1 = t1.getRoot().split(t1.getPos());
        } else {
            p1 = new Pair<>(new TreapImpl(), new TreapImpl());
        }

        Pair<Treap, Treap> p2;
        if (!vertexToTreap.get(vertex2).isEmpty()) {
            Treap t2 = vertexToTreap.get(vertex2).iterator().next();
            eulerPaths.remove(t2.getRoot());
            p2 = t2.getRoot().split(t2.getPos());
        } else {
            p2 = new Pair<>(new TreapImpl(), new TreapImpl());
        }

        Treap res = p1.getKey();
        Treap te1 = res.append(e1, level);
        edgeToTreap.put(e1, te1);
        vertexToTreap.get(vertex1).add(te1);

        res = Treap.merge(Treap.merge(res.getRoot(), p2.getValue()), p2.getKey());
        Treap te2 = res.append(e2, level);
        edgeToTreap.put(e2, te2);
        vertexToTreap.get(vertex2).add(te2);

        res = Treap.merge(res.getRoot(), p1.getValue());

        if (res.getRoot().getSize() > 0)
            eulerPaths.add(res.getRoot());
    }

    public void link(int vertex1, int vertex2) {
        link(vertex1, vertex2, 0);
    }

    private Pair<Treap, Treap> removeNode(Treap node) {
        int pos = node.getPos();
        Pair<Treap, Treap> first = node.getRoot().split(pos);
        Pair<Treap, Treap> second = first.getValue().getRoot().split(1);
        return new Pair(first.getKey().getRoot(), second.getValue().getRoot());
    }

    public void cut(int vertex1, int vertex2) {
        if (vertex1 == vertex2)
            return;
        Edge e = new Edge(vertex1, vertex2);
        if (!edgeLevels.containsKey(e))
            return;
        int edge_level = edgeLevels.remove(e);
        edgeLevels.remove(new Edge(vertex2, vertex1));
        deleteEdgeFromLevel(edge_level, vertex1, vertex2, null);
    }

    private void deleteEdgeFromLevel(int level, int vertex1, int vertex2, Edge new_edge) {
        if (level < 0)
            return;
        Map<Integer, Set<Treap>> vertexToTreap = eulerTours.get(level).getVertexToTreaps();
        Set<Treap> eulerPaths = eulerTours.get(level).getEulerPaths();
        Map<Edge, Treap> edgeToTreap = eulerTours.get(level).getEdgeToTreap();

        Edge e1 = new Edge(vertex1, vertex2);
        Edge e2 = new Edge(vertex2, vertex1);

        if (!edgeToTreap.containsKey(e1)) {
            eulerTours.get(level).getVertexToFreeEdges().get(vertex1).remove(e1);
            eulerTours.get(level).getVertexToFreeEdges().get(vertex2).remove(e2);
            deleteEdgeFromLevel(level - 1, vertex1, vertex2, new_edge);
            return;
        }

        if (!edgeToTreap.containsKey(e2)) {
            System.err.println("???");
        }
        Treap start = edgeToTreap.remove(e1);
        vertexToTreap.get(vertex1).remove(start);
        Treap end = edgeToTreap.remove(e2);

        vertexToTreap.get(vertex2).remove(end);
        eulerPaths.remove(start.getRoot());

        if (end.getPos() < start.getPos()) {
            Treap tmp = start;
            start = end;
            end = tmp;
            int tmpv = vertex1;
            vertex1 = vertex2;
            vertex2 = tmpv;
        }
        Pair<Treap, Treap> first = removeNode(start);
        Pair<Treap, Treap> second = removeNode(end);

        Treap<Edge> res1 = Treap.merge(first.getKey(), second.getValue()).getRoot();
        Treap<Edge> res2 = second.getKey();

        if (new_edge != null) {
            moveEdgeFromFreeToEulerTour(new_edge, level);
            deleteEdgeFromLevel(level - 1, vertex1, vertex2, new_edge);
            return;
        }

        if (res1.getSize() == 0 && res2.getSize() == 0)
            deleteEdgeFromLevel(level - 1, vertex1, vertex2, null);

        if (res1.getSize() == 0) {
            eulerPaths.add(res2);
            for (Edge free_e : eulerTours.get(level).getVertexToFreeEdges().get(vertex1)) {
                if (getVertexsFromEulerTour(res2).contains(free_e.getTo())) {
                    moveEdgeFromFreeToEulerTour(free_e, level);
                    deleteEdgeFromLevel(level - 1, vertex1, vertex2, free_e);
                    return;
                }
            }
            deleteEdgeFromLevel(level - 1, vertex1, vertex2, new_edge);
            return;
        }
        if (res2.getSize() == 0) {
            eulerPaths.add(res1);
            for (Edge free_e: eulerTours.get(level).getVertexToFreeEdges().get(vertex2)) {
                if (getVertexsFromEulerTour(res1).contains(free_e.getTo())) {
                    moveEdgeFromFreeToEulerTour(free_e, level);
                    deleteEdgeFromLevel(level - 1, vertex1, vertex2, free_e);
                    return;
                }
            }
            deleteEdgeFromLevel(level - 1, vertex1, vertex2, new_edge);
            return;
        }
        eulerPaths.add(res1);
        eulerPaths.add(res2);

        if (level + 1 < levels) {
            if (res1.getSize() < res2.getSize()) {
                moveEdgesToNextLevel(res1, level);
            } else {
                moveEdgesToNextLevel(res2, level);
            }
        }

        Set<Integer> vertex_res1 = getVertexsFromEulerTour(res1);
        for (Integer vertex : vertex_res1) {
            if (new_edge != null)
                break;
            for (Edge free_e : eulerTours.get(level).getVertexToFreeEdges().get(vertex)) {
                Integer from = free_e.getFrom();
                Integer to = free_e.getTo();
                if (vertexToTreap.get(from).isEmpty() || vertexToTreap.get(to).isEmpty()) {
                    continue;
                }
                Treap tree1 = vertexToTreap.get(from).iterator().next().getRoot();
                Treap tree2 = vertexToTreap.get(to).iterator().next().getRoot();
                if (tree1 == res1 && tree2 == res2 || tree2 == res1 && tree1 == res2) {
                    new_edge = free_e;
                    moveEdgeFromFreeToEulerTour(new_edge, level);
                    break;
                }
            }
        }
        deleteEdgeFromLevel(level - 1, vertex1, vertex2, new_edge);
        return;
    }

    private Set<Integer> getVertexsFromEulerTour(Treap<Edge> tour) {
        Set<Integer> vertex_res1 = new HashSet<>();
        for (Edge edge_res1 : tour.getValues()) {
            vertex_res1.add(edge_res1.getFrom());
            vertex_res1.add(edge_res1.getTo());
        }
        return vertex_res1;
    }

    private void moveEdgeFromFreeToEulerTour(Edge edge, int level) {
        Integer from = edge.getFrom();
        Integer to = edge.getTo();
        eulerTours.get(level).getVertexToFreeEdges().get(from).remove(edge);
        eulerTours.get(level).getVertexToFreeEdges().get(to).remove(new Edge(to, from));
        link(from, to, level);
    }

    private boolean isConnected(int vertex1, int vertex2, int level) {
        if (vertex1 == vertex2)
            return true;
        if (eulerTours.get(level).getVertexToTreaps().get(vertex1).isEmpty() ||
                eulerTours.get(level).getVertexToTreaps().get(vertex2).isEmpty()) {
            return false;
        }
        return eulerTours.get(level).getVertexToTreaps().get(vertex1).iterator().next().getRoot()
                .equals(eulerTours.get(level).getVertexToTreaps().get(vertex2).iterator().next().getRoot());
    }


    public boolean isConnected(int vertex1, int vertex2) {
        return isConnected(vertex1, vertex2, 0);
    }

    private void moveEdgesToNextLevel(Treap<Edge> tree, int level) {
        Set<Integer> vertexes = getVertexsFromEulerTour(tree);
        for (Integer v : vertexes) {
            for (Edge e : eulerTours.get(level).getVertexToFreeEdges().get(v)) {
                if (vertexes.contains(e.getTo())) {
                    edgeLevels.put(e, level + 1);
                    eulerTours.get(level + 1).getVertexToFreeEdges().get(v).add(e);
                }
            }
        }
        moveTourToNextLevel(tree, level);
    }

    private void moveTourToNextLevel(Treap<Edge> tree, int level) {
        if (tree.getSize() == 0)
            return;
        if (tree.getLevel() == level) {
            tree.setLevel(level + 1);
            Edge cur_e = tree.getValue();
            link(cur_e.getFrom(), cur_e.getTo(), level + 1);
        }
        if (tree.getLeft().getMinLevel() <= level) {
            moveEdgesToNextLevel(tree.getLeft(), level);
        }
        if (tree.getRight().getMinLevel() <= level) {
            moveEdgesToNextLevel(tree.getRight(), level);
        }
    }

    private class EulerTour {

        private Set<Treap> eulerPaths;
        private Map<Integer, Set<Treap>> vertexToTreaps;
        private Map<Edge, Treap> edgeToTreap;
        private List<Set<Edge>> vertexToFreeEdges;

        public EulerTour(int level) {
            eulerPaths = new HashSet<>();
            edgeToTreap = new HashMap<>();
            vertexToTreaps = new HashMap<>();
            vertexToFreeEdges = new ArrayList<>();
            for (int v = 0; v < n; v++) {
                vertexToTreaps.put(v, new HashSet<>());
                vertexToFreeEdges.add(new HashSet<>());
            }
        }

        public Map<Edge, Treap> getEdgeToTreap() {
            return edgeToTreap;
        }

        public Map<Integer, Set<Treap>> getVertexToTreaps() {
            return vertexToTreaps;
        }

        public Set<Treap> getEulerPaths() {
            return eulerPaths;
        }

        public List<Set<Edge>> getVertexToFreeEdges() {
            return vertexToFreeEdges;
        }
    }
}

