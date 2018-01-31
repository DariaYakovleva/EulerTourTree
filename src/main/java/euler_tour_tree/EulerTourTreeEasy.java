package euler_tour_tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EulerTourTreeEasy extends EulerTourTreeAbstract {

    List<Set<Integer>> adj_list;

    public EulerTourTreeEasy(int n) {
        this.n = n;
        adj_list = new ArrayList<>();
        for (int v = 0; v < n; v++) {
            adj_list.add(new HashSet<>());
        }
    }

    public void link(int vertex1, int vertex2) {
        adj_list.get(vertex1).add(vertex2);
        adj_list.get(vertex2).add(vertex1);
    }

    public void cut(int vertex1, int vertex2) {
        adj_list.get(vertex1).remove(vertex2);
        adj_list.get(vertex2).remove(vertex1);
    }

    public boolean isConnected(int vertex1, int vertex2) {
        boolean[] used = new boolean[n];
        for (int i = 0; i < n; i++)
            used[i] = false;
        dfs(vertex1, used);
        return used[vertex2];
    }

    void dfs(int v, boolean[] used) {
        used[v] = true;
        for (int to : adj_list.get(v)) {
            if (!used[to])
                dfs(to, used);
        }
    }
}
