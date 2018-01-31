package euler_tour_tree.treap;

import javafx.util.Pair;

import java.util.List;

public abstract class Treap<T extends Comparable<T>> {

    public abstract Pair<Treap, Treap> split(int k);

    public abstract Treap insert(int pos, T value, int level);

    public abstract Treap append(T value, int level);

    public abstract Treap getRoot();

    public abstract int getSize();

    public abstract int getPos();

    public abstract T getValue();

    public abstract Treap getLeft();

    public abstract Treap getRight();

    abstract Treap getPrev();

    public abstract int getMinLevel();

    public abstract int getLevel();

    abstract void setLeft(Treap left);

    abstract void setRight(Treap right);

    abstract void setPrev(Treap prev);

    public abstract void setLevel(int level);

    public abstract void updateMinLevel();

    public static Treap merge(Treap a, Treap b) {
        if (a.equals(b))
            return a;
        if (a.getSize() == 0)
            return b;
        if (b.getSize() == 0)
            return a;
        if (a.getValue().compareTo(b.getValue()) > 0) {
            a.setRight(merge(a.getRight(), b));
            a.setPrev(null);
            return a;
        }
        b.setLeft(merge(a, b.getLeft()));
        b.setPrev(null);
        return b;
    }

    public String toString() {
        if (getSize() == 0)
            return "()";
        if (getSize() == 1)
            return hashCode() + "(value = " + getValue().toString() + ", size = " + getSize() + "pos=" + getPos() + "prev=" + (getPrev() != null) + ")";
        return hashCode() + "(value = " + getValue().toString() + ", size = " + getSize() + "pos=" + getPos() +
                ", left = " + getLeft().toString() + ", right = " + getRight().toString()
                + ", prev= " + (getPrev()!=null) + ")";
    }

    public abstract List<T> getValues();
}
