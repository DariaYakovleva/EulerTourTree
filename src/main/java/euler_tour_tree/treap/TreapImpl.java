package euler_tour_tree.treap;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class TreapImpl<T extends Comparable<T>> extends Treap<T> {

    T value;
    int size;
    Treap left;
    Treap right;
    Treap prev;
    int level;
    int minLevel;

    public TreapImpl() {
        this.value = null;
        size = 0;
        left = null;
        right = null;
        prev = null;
        level = Integer.MAX_VALUE;
        minLevel = Integer.MAX_VALUE;
    }

    public TreapImpl(T value, int level) {
        this.value = value;
        size = 1;
        left = null;
        right = null;
        prev = null;
        this.level = level;
        minLevel = level;
    }

    public Pair<Treap, Treap> split(int k) {
        if (getSize() == 0) {
            return new Pair(this, new TreapImpl());
        }
        if (k == 0) {
            return new Pair(new TreapImpl(), this);
        }
        int leftSize = getLeft().getSize();

        if (leftSize >= k) {
            Pair<Treap, Treap> cur = getLeft().split(k);
            setLeft(cur.getValue());
            setPrev(null);
            cur.getKey().setPrev(null);
            return new Pair(cur.getKey(), this);
        }
        Pair<Treap, Treap> cur = getRight().split(k - leftSize - 1);
        setRight(cur.getKey());
        setPrev(null);
        cur.getValue().setPrev(null);
        return new Pair(this, cur.getValue());
    }

    public Treap insert(int pos, T value, int level) {
        if (getSize() == 0) {
            this.value = value;
            size += 1;
            this.level = level;
            minLevel = level;
            return this;
        }
        Treap cur_treap = new TreapImpl(value, level);
        Pair<Treap, Treap> cur = split(pos);
        Treap res = merge(cur.getKey(), cur_treap);
        merge(res.getRoot(), cur.getValue());
        return cur_treap;
    }

    public Treap append(T value, int level) {
        return insert(size, value, level);
    }

    public Treap getRoot() {
        Treap root = this;
        while (root.getPrev() != null) {
            root = root.getPrev();
        }
        return root;
    }

    public int getPos() {
        int pos = getLeft().getSize();
        Treap root = this.getPrev();
        Treap prev = this;
        while (root != null) {
            if (root.getRight().equals(prev))
                pos += root.getLeft().getSize() + 1;
            prev = root;
            root = root.getPrev();
        }
        return pos;
    }

    public T getValue() {
        return value;
    }

    public int getSize() {
        return size;
    }

    public Treap getLeft() {
        if (left == null)
            return new TreapImpl();
        return left;
    }

    public Treap getRight() {
        if (right == null)
            return new TreapImpl();
        return right;
    }

    public Treap getPrev() {
        return prev;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public int getLevel() {
        return level;
    }

    public void setLeft(Treap left) {
        this.left = left;
        if (left != null)
            left.setPrev(this);
        updateSize();
        updateMinLevel();
    }

    public void setRight(Treap right) {
        this.right = right;
        if (right != null)
            right.setPrev(this);
        updateSize();
        updateMinLevel();
    }

    public void setPrev(Treap prev) {
        this.prev = prev;
    }

    public void setLevel(int level) {
        this.level = level;
        Treap root = this;
        while (root != null) {
            root.updateMinLevel();
            root = root.getPrev();
        }
    }

    private void updateSize() {
        size = getRight().getSize() + getLeft().getSize() + 1;
    }

    public void updateMinLevel() {
        minLevel = Math.min(level, Math.min(getRight().getMinLevel(), getLeft().getMinLevel()));
    }

    public List<T> getValues() {
        List<T> result = new ArrayList<T>();
        if (getSize() == 0)
            return result;
        if (getLeft().getSize() > 0)
            result.addAll(getLeft().getValues());
        if (getRight().getSize() > 0)
            result.addAll(getRight().getValues());
        result.add(getValue());
        return result;
    }
}
