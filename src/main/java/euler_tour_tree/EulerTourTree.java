package euler_tour_tree;

public interface EulerTourTree {

    void link(int vertex1, int vertex2);

    void cut(int vertex1, int vertex2);

    boolean isConnected(int vertex1, int vertex2);
}
