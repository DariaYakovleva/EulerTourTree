package euler_tour_tree.treap;

import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TreapTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void TreapTestInit() {
        Treap t1 = new TreapImpl();
        Assert.assertEquals(t1.getSize(), 0);
        Treap t2 = new TreapImpl(4, 0);
        Assert.assertEquals(t2.getSize(), 1);
        Assert.assertEquals(t2.getValue(), 4);
    }

    @Test
    public void TreapTestMerge() {
        Treap t1 = new TreapImpl(1, 0);
        Treap t2 = new TreapImpl(2, 0);
        Treap res = Treap.merge(t1, t2);
        Assert.assertEquals(res.getSize(), 2);
    }

    @Test
    public void TreapTestSplit() {
        Treap t1 = new TreapImpl(1, 0);
        Treap t2 = new TreapImpl(2, 0);
        t1 = Treap.merge(t1, t2);
        Assert.assertEquals(t1.getSize(), 2);
        Pair<Treap, Treap> res = t1.split(1);
        Assert.assertEquals(t1.getSize(), 1);
        Assert.assertEquals(res.getValue().getSize(), 1);
    }


    @Test
    public void TreapTestInsert() {
        Treap t1 = new TreapImpl();
        t1.insert(0, 5, 0);
        Assert.assertEquals(t1.getSize(), 1);
        Assert.assertEquals(t1.getValue(), 5);
        t1.insert(1, 6, 0);
        Assert.assertEquals(t1.getRoot().getSize(), 2);
    }

}
