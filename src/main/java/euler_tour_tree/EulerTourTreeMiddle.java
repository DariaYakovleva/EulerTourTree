//package euler_tour_tree;
//
//import euler_tour_tree.treap.Treap;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//public class EulerTourTreeMiddle extends EulerTourTreeAbstract {
//
//    private List<Treap> eulerPaths;
//
//    public EulerTourTreeHard(int n, List<Edge> edges) {
//        this.n = n;
//        used = new boolean[n];
//        eulerPaths = new ArrayList<Treap>();
//        vertexLocations = new ArrayList<VertexLocation>();
//        for (int i = 0; i < n; i++) {
//            vertexLocations.add(new VertexLocation());
//        }
//        List<Set<Integer>> adj_list = getAdjacencyListFromEdges(n, edges);
//        for (int v = 0; v < n; v++) {
//            if (!used[v]) {
//                eulerPaths.add(new ArrayList<Integer>());
//                addEulerPath(v, adj_list);
//                updateVertexLocations(eulerPaths.size() - 1);
//            }
//        }
//    }
//
//    private void updateVertexLocations(int eulerPathNum) {
//        List<Integer> eulerPath = eulerPaths.get(eulerPathNum);
//        for (int v : eulerPath) {
//            vertexLocations.get(v).clearInsertion();
//        }
//        for (int i = 0; i < eulerPath.size(); i++) {
//            int v = eulerPath.get(i);
//            vertexLocations.get(v).setEulerPathNumber(eulerPathNum);
//            vertexLocations.get(v).addInsertion(i);
//        }
//    }
//
//    public void link(int vertex1, int vertex2) {
//        int eulerPath1 = vertexLocations.get(vertex1).getEulerPathNumber();
//        int eulerPath2 = vertexLocations.get(vertex2).getEulerPathNumber();
//        if (eulerPath1 == eulerPath2)
//            return;
//        int pos1 = vertexLocations.get(vertex1).getInsertions().get(0);
//        int pos2 = vertexLocations.get(vertex2).getInsertions().get(0);
//        List<Integer> newEulerPath = new ArrayList<Integer>();
//        for (int v = 0; v <= pos1; v++) {
//            newEulerPath.add(eulerPaths.get(eulerPath1).get(v));
//        }
//        for (int v = pos2; v < eulerPaths.get(eulerPath2).size(); v++) {
//            newEulerPath.add(eulerPaths.get(eulerPath2).get(v));
//        }
//        for (int v = pos2; v >= 0; v--) {
//            newEulerPath.add(eulerPaths.get(eulerPath2).get(v));
//        }
//        for (int v = pos1; v < eulerPaths.get(eulerPath1).size(); v++) {
//            newEulerPath.add(eulerPaths.get(eulerPath1).get(v));
//        }
//        eulerPaths.set(pos1, newEulerPath);
//        updateVertexLocations(pos1);
//        eulerPaths.set(pos2, new ArrayList<Integer>());
//    }
//
//    public void cut(int vertex1, int vertex2) {
//        int eulerPathNum = vertexLocations.get(vertex1).getEulerPathNumber();
//        if (eulerPathNum != vertexLocations.get(vertex2).getEulerPathNumber())
//            return;
//        if (vertexLocations.get(vertex1).getInsertions().get(0) > vertexLocations.get(vertex2).getInsertions().get(0)) {
//            int tmp = vertex1;
//            vertex1 = vertex2;
//            vertex2 = tmp;
//        }
//        int first_pos1 = vertexLocations.get(vertex1).getInsertions().get(0);
//        int second_pos1 = vertexLocations.get(vertex1).getInsertions().get(1);
//        int first_pos2 = vertexLocations.get(vertex2).getInsertions().get(0);
//        int second_pos2 = vertexLocations.get(vertex2).getInsertions().get(1);
//        List<Integer> newEulerPath1 = new ArrayList<Integer>();
//        for (int v = 0; v < first_pos1; v++) {
//            newEulerPath1.add(eulerPaths.get(eulerPathNum).get(v));
//        }
//        for (int v = second_pos1; v < eulerPaths.get(eulerPathNum).size(); v++) {
//            newEulerPath1.add(eulerPaths.get(eulerPathNum).get(v));
//        }
//        List<Integer> newEulerPath2 = new ArrayList<Integer>();
//        for (int v = first_pos2; v <= second_pos2; v++) {
//            newEulerPath2.add(eulerPaths.get(eulerPathNum).get(v));
//        }
//        eulerPaths.set(eulerPathNum, newEulerPath1);
//        eulerPaths.add(newEulerPath2);
//        updateVertexLocations(eulerPathNum);
//        updateVertexLocations(eulerPaths.size() - 1);
//    }
//
//    public boolean isConnected(int vertex1, int vertex2) {
//        return vertexLocations.get(vertex1).getEulerPathNumber() == vertexLocations.get(vertex2).getEulerPathNumber();
//    }
//
//
//    protected boolean[] used;
//
//
//    protected void addEulerPath(int vertex, List<List<Integer>> adj_list) {
//        used[vertex] = true;
//        int eulerPathNumber = eulerPaths.size() - 1;
//        eulerPaths.get(eulerPathNumber).add(vertex);
//        for (Integer to : adj_list.get(vertex)) {
//            if (!used[to]) {
//                addEulerPath(to, adj_list);
//                eulerPaths.get(eulerPathNumber).add(vertex);
//            }
//        }
//    }
//
//    protected class VertexLocation {
//        private int eulerPathNumber;
//        private List<Integer> insertions;
//
//        public VertexLocation() {
//            this.eulerPathNumber = -1;
//            this.insertions = new ArrayList<Integer>();
//        }
//
//        public void setEulerPathNumber(int eulerPathNumber) {
//            this.eulerPathNumber = eulerPathNumber;
//        }
//
//        public void addInsertion(int insertion) {
//            insertions.add(insertion);
//        }
//
//        public void clearInsertion() {
//            insertions.clear();
//        }
//
//        public int getEulerPathNumber() {
//            return eulerPathNumber;
//        }
//
//        public List<Integer> getInsertions() {
//            return insertions;
//        }
//    }
//}
