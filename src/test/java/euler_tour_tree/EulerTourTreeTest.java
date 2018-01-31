package euler_tour_tree;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EulerTourTreeTest {

    EulerTourTree treeEasy;
    EulerTourTree treeHard;
    int n;

    @Before
    public void setUp() throws Exception {
        n = 100;
        treeEasy = new EulerTourTreeEasy(n);
        treeHard = new EulerTourTreeHard(n);
    }

    @Test
    public void HardTestInit() {
        Assert.assertEquals(treeEasy.isConnected(0, 1), treeHard.isConnected(0, 1));
    }

    @Test
    public void HardTestLink() {
        Assert.assertEquals(treeEasy.isConnected(0, 2), treeHard.isConnected(0, 2));
        treeEasy.link(0, 2);
        treeHard.link(0, 2);
        Assert.assertEquals(treeEasy.isConnected(0, 2), treeHard.isConnected(0, 2));
    }

    @Test
    public void HardTestCut() {
        treeEasy.link(0, 2);
        treeHard.link(0, 2);
        Assert.assertEquals(treeEasy.isConnected(0, 2), treeHard.isConnected(0, 2));
        treeEasy.cut(0, 2);
        treeHard.cut(0, 2);
        Assert.assertEquals(treeEasy.isConnected(0, 2), treeHard.isConnected(0, 2));
    }

    @Test
    public void TestFullGraph() {
        for (int i = 0; i < 4; i++) {
            for (int from = 0; from < n; from++) {
                for (int to = from + 1; to < n; to++) {
                    treeEasy.link(from, to);
                    treeHard.link(from, to);
                    Assert.assertEquals(treeEasy.isConnected(from, to), treeHard.isConnected(from, to));
                }
            }
            for (int from = 0; from < n; from++) {
                for (int to = from + 1; to < n; to++) {
                    treeEasy.cut(from, to);
                    treeHard.cut(from, to);
                    Assert.assertEquals(treeEasy.isConnected(from, to), treeHard.isConnected(from, to));
                }
            }
        }
    }

    @Test
    public void TestRandomGraph() {
        n = 100;
        treeEasy = new EulerTourTreeEasy(n);
        treeHard = new EulerTourTreeHard(n);
        Random rn = new Random();
        int cnt_cut = 0;
        int cnt_link = 0;
        List<String> queries = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 10; j++) {
                int from = rn.nextInt(n);
                int to = rn.nextInt(n);
                if (i % 2 == 0) {
                    treeEasy.cut(from, to);
                    treeHard.cut(from, to);
                    queries.add("cut " + from + " " + to);
                    cnt_cut += 1;
                } else {
                    treeEasy.link(from, to);
                    treeHard.link(from, to);
                    queries.add("link " + from + " " + to);
                    cnt_link += 1;
                }
                if (treeEasy.isConnected(from, to) != treeHard.isConnected(from, to)) {
                    for (String q : queries)
                        System.err.println(q);
                    System.err.println(from + " " + to);
                    System.err.println(treeEasy.isConnected(from, to) + " " + treeHard.isConnected(from, to));
                }
                Assert.assertEquals(treeEasy.isConnected(from, to), treeHard.isConnected(from, to));
            }
        }
        System.out.println(cnt_cut + ", " + cnt_link);
    }

    @Test
    public void MultiTest() {
        for (int i = 0; i < 100; i++)
            TestRandomGraph();
    }

}
