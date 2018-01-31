package euler_tour_tree;

public class Edge implements Comparable<Edge> {
    Integer from;
    Integer to;

    public Edge(Integer from, Integer to) {
        this.from = from;
        this.to = to;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public int compareTo(Edge other) {
        if (from == other.from && to == other.to)
            return 0;
        if (from > other.from || (from == other.from && to > other.to))
            return 1;
        return -1;
    }

    @Override
    public String toString() {
        return "(" + from + ", " + to + ")";
    }

    @Override
    public int hashCode() {
        return 31 * from.hashCode() + to.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Edge))
            return false;
        return this.from == ((Edge)other).getFrom() && this.to == ((Edge)other).getTo();
    }
}