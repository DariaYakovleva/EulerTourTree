import euler_tour_tree.Edge;
import euler_tour_tree.EulerTourTree;
import euler_tour_tree.EulerTourTreeEasy;
import euler_tour_tree.EulerTourTreeHard;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        timeTest1();
        timeTest2();
    }

    private static void timeTest1() {
        List<Integer> n_vertex = new ArrayList<>();
        List<Long> timeEasy = new ArrayList<>();
        List<Long> timeHard = new ArrayList<>();
        for (int n = 1; n <= 5001; n += 500) {
            List<Edge> queries = generateQueries(n, 10 * n);
            long durationEasy = timeTest(new EulerTourTreeEasy(n), queries);
            long durationHard = timeTest(new EulerTourTreeHard(n), queries);
            System.err.println(n + " " + durationEasy + " " + durationHard);
            n_vertex.add(n);
            timeEasy.add(durationEasy);
            timeHard.add(durationHard);
        }
        n_vertex.forEach(x -> System.out.print(x + ", "));
        System.out.println();
        timeEasy.forEach(x -> System.out.print(x + ", "));
        System.out.println();
        timeHard.forEach(x -> System.out.print(x + ", "));
    }

    private static void timeTest2() {
        for (int i = 0; i < 3; i++) {
            List<Long> timeEasy = new ArrayList<>();
            List<Long> timeHard = new ArrayList<>();
            List<Integer> n_vertex = new ArrayList<>();
            System.out.println("Test " + i);
            for (int n = 1; n <= 8001; n += 500) {
                int m = 10 * n;
                if (i == 1)
                    m = n * n / 128;
                if (i == 2)
                    m = n * n / 64;
                n_vertex.add(n);
                List<Edge> queries = generateQueries(n, m);
                System.out.print(timeTest(new EulerTourTreeEasy(n), queries) + ", ");
                timeEasy.add(timeTest(new EulerTourTreeEasy(n), queries));
                timeHard.add(timeTest(new EulerTourTreeHard(n), queries));
            }
            n_vertex.forEach(x -> System.out.print(x + ", "));
            System.out.println();
            timeEasy.forEach(x -> System.out.print(x + ", "));
            System.out.println();
            timeHard.forEach(x -> System.out.print(x + ", "));
            System.out.println();
        }
    }

    private static long timeTest(EulerTourTree tree, List<Edge> queries) {
        long startTime = System.nanoTime();
        for (int i = 0; i < queries.size(); i++) {
            int from = queries.get(i).getFrom();
            int to = queries.get(i).getTo();
            if (i / 5 % 2 == 0) {
                tree.link(from, to);
            } else {
                tree.cut(from, to);
            }
            tree.isConnected(from, to);
        }
        long duration = TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        return duration;
    }

    private static List<Edge> generateQueries(int n, int m) {
        List<Edge> queries = new ArrayList<>();
        Random rn = new Random();
        for (int i = 0; i < m; i++) {
            int from = rn.nextInt(n);
            int to = rn.nextInt(n);
            queries.add(new Edge(from, to));
        }
        return queries;
    }
}