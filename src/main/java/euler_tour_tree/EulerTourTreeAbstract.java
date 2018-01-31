package euler_tour_tree;

import euler_tour_tree.treap.Treap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class EulerTourTreeAbstract implements EulerTourTree {

    protected int n;

    protected List<Set<Integer>> getAdjacencyListFromEdges(int n, List<Edge> edges) {
        List<Set<Integer>> adj_list = new ArrayList<Set<Integer>>();
        for (int i = 0; i < n; i++) {
            adj_list.add(new HashSet<Integer>());
        }
        for (Edge edge : edges) {
            int from = edge.getFrom(), to = edge.getTo();
            adj_list.get(from).add(to);
            adj_list.get(to).add(from);
        }
        return adj_list;
    }
}
