package edu.neu.coe.info6205.assignments;

import edu.neu.coe.info6205.assignments.jfreechart.ChartHelper;
import edu.neu.coe.info6205.union_find.UF_HWQUPC;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.IOException;
import java.util.Random;

public class Assignment3 {

    private static int random(int N) {

        Random r = new Random();
        return r.nextInt(N);
    }

    /**
     * Function to connect n sites and return edges
     * @param n - No of Sites
     * @return Total number of tries
     */
    public static int count(int n) {

        UF_HWQUPC unionFind = new UF_HWQUPC(n);
        int tries = 0;
        while(unionFind.components() > 1) {
            int p = random(n);
            int q = random(n);
            unionFind.connect(p,q);
            tries++;
        }
        return tries;
    }

    /**
     * Calls the count method, and
     * @param n - number of sites
     * @param trials - number of trials to run and avergage over
     * @return average of n sites over trials
     */
    public static double count(int n, int trials) {
        double total = 0;
        for(int i=0;i<trials;i++)
            total += count(n);
        return total/trials;
    }



    public static void main(String args[]) throws IOException {

        int N = 100;


        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries experimentSeries = new XYSeries("Expreiment Values");
        XYSeries logSeries = new XYSeries(" 1/2 n ln n");


        for(int i=0;i<10;i++, N *= 2) {
            double average = count(N, 10000);
            experimentSeries.add(N,average);
            logSeries.add(N, 0.5 * N * Math.log(N));
            System.out.println( " N = "+N+ ", Pairs = " + average+  ",1/2 N lg N = " + (0.5 * N * Math.log(N)));
        }
        dataset.addSeries(experimentSeries);
        //dataset.addSeries(logSeries);

        ChartHelper.printChart(dataset, "Ass3.jpg","Union-Find", "N - Number of Sites", "Y - Avg Number of Connections");

    }


}
