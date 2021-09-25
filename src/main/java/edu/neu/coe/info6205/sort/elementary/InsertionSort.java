/*
  (c) Copyright 2018, 2019 Phasmid Software
 */
package edu.neu.coe.info6205.sort.elementary;

import edu.neu.coe.info6205.sort.BaseHelper;
import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.sort.SortWithHelper;
import edu.neu.coe.info6205.util.Benchmark;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import edu.neu.coe.info6205.util.Config;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class InsertionSort<X extends Comparable<X>> extends SortWithHelper<X> {

    /**
     * Constructor for any sub-classes to use.
     *
     * @param description the description.
     * @param N           the number of elements expected.
     * @param config      the configuration.
     */
    protected InsertionSort(String description, int N, Config config) {
        super(description, N, config);
    }

    /**
     * Constructor for InsertionSort
     *
     * @param N      the number elements we expect to sort.
     * @param config the configuration.
     */
    public InsertionSort(int N, Config config) {
        this(DESCRIPTION, N, config);
    }

    public InsertionSort(Config config) {
        this(new BaseHelper<>(DESCRIPTION, config));
    }

    /**
     * Constructor for InsertionSort
     *
     * @param helper an explicit instance of Helper to be used.
     */
    public InsertionSort(Helper<X> helper) {
        super(helper);
    }

    public InsertionSort() {
        this(BaseHelper.getHelper(InsertionSort.class));
    }

    /**
     * Sort the sub-array xs:from:to using insertion sort.
     *
     * @param xs   sort the array xs from "from" to "to".
     * @param from the index of the first element to sort
     * @param to   the index of the first element not to sort
     */
    public void sort(X[] xs, int from, int to) {
        final Helper<X> helper = getHelper();
        for (int i = from; i < to; i++) {
            X key = xs[i];
            int j = i - 1;
            while (j >= from && !helper.less(xs[j],key)) {
                helper.swap(xs, j, j+1);
                j = j - 1;
            }
            xs[j + 1] = key;
        }
    }

    /**
     * Sort the sub-array xs:from:to using insertion sort.
     *
     * @param xs   sort the array xs from "from" to "to".
     * @param from the index of the first element to sort
     * @param to   the index of the first element not to sort
     */
    /*
    public void binaryInsertionSort(X[] xs, int from, int to) {
        final Helper<X> helper = getHelper();
        for (int i = from; i < to; i++) {
            X key = xs[i];
            int j = i - 1;
            while (j >= from && !helper.less(xs[j],key)) {
                helper.swap(xs, j, j+1);
                j = j - 1;
            }
            xs[j + 1] = key;
        }
    }
    */

    public static final String DESCRIPTION = "Insertion sort";

    public static <T extends Comparable<T>> void sort(T[] ts) {
        new InsertionSort<T>().mutatingSort(ts);
    }

    public static List<Integer> generateRandomArray(int n){
        ArrayList<Integer> list = new ArrayList<Integer>(n);
        Random random = new Random();
        for (int i = 0; i < n; i++)
        {
            list.add(random.nextInt(1000));
        }
        return list;
    }
    public static List<Integer> generateSortedArray(int n) {
        ArrayList<Integer> list = new ArrayList<Integer>(n);
        for (int i = 0; i < n; i++) {
            list.add(i+1);
        }
        return list;
    }

    public static List<Integer> generateReverseSortedArray(int n) {
        ArrayList<Integer> list = new ArrayList<Integer>(n);
        for (int i = 0; i < n; i++) {
            list.add(n-i);
        }
        return list;
    }
    public static List<Integer> generatePartialArray(int n) {
        ArrayList<Integer> list = new ArrayList<Integer>(n);
        for (int i = 0; i < n/2; i++) {
            list.add(i);
        }
        for (int i = n/2; i < n; i++) {
            list.add((n/2) - i);
        }
        return list;
    }


        public static void main(String args[]) throws IOException {


        int NUM_TESTS = 6;
        int INITIAL_N = 1000;

        InsertionSort<Integer> insertionSort = new InsertionSort<>();
            Benchmark<List<Integer>> bm = new Benchmark_Timer<>(
                    "BM Test", null, b -> {
                Integer[] arr = (b.toArray(new Integer[b.size()]));
                insertionSort.sort(arr,0,b.size());
            }, null);

            XYSeriesCollection dataset = new XYSeriesCollection();
            XYSeriesCollection logDataset = new XYSeriesCollection();

            XYSeries sortedSeries = new XYSeries("Sorted Array");
            XYSeries randomSeries = new XYSeries("Random Array");
            XYSeries reverseSeries = new XYSeries("Reverse Ordered Array");
            XYSeries partialSeries = new XYSeries("Partial Ordered Array");

            XYSeries logSeries = new XYSeries("Partial Ordered Array");


            double time;

            for(int i=0;i<NUM_TESTS;i++,INITIAL_N *= 2) {

                System.out.println("************N=" + INITIAL_N+"**************");

                List<Integer> sortedOrder = generateSortedArray(INITIAL_N);
                List<Integer> randomOrder = generateRandomArray(INITIAL_N);
                List<Integer> reverseOrder =generateReverseSortedArray(INITIAL_N);
                List<Integer> partialOrder = generatePartialArray(INITIAL_N);

                //time = bm.run(sortedOrder,10);
//                System.out.println("    Sorted order(N="+INITIAL_N+") = " +time +" ms");
//                sortedSeries.add(INITIAL_N,time);
//
//                 time = bm.run(partialOrder,10);
//                System.out.println("    Partial order(N="+INITIAL_N+") = " +time +" ms");
//
//                 partialSeries.add(INITIAL_N,time);
//                 time = bm.run(reverseOrder,10);
//                System.out.println("    Reverse order(N="+INITIAL_N+") = " +time +" ms");
//                reverseSeries.add(INITIAL_N,time);

                 time = bm.run(randomOrder,10);
                System.out.println("    Randome order(N="+INITIAL_N+") = " +time +" ms");
                randomSeries.add(INITIAL_N,time);
                logSeries.add(Math.log(INITIAL_N),Math.log(time));
            }

            dataset.addSeries(sortedSeries);
            dataset.addSeries(reverseSeries);
            dataset.addSeries(partialSeries);
            dataset.addSeries(randomSeries);

            createChart(dataset,"Assignment 2.jpg");
            createChart(logDataset,"log-log plot.jpg");

        }

    public static void createChart(XYDataset dataset,String a) throws IOException {
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                "Insertion Sort Benchmark Test" ,
                "N - Number Elements" ,
                "d - Average running time over 10 runs" ,
                dataset,
                PlotOrientation.VERTICAL ,
                true , true , false);


        int width = 640;   /* Width of the image */
        int height = 480;  /* Height of the image */

        String dir = System.getProperty("user.dir");
        File XYChart = new File( dir + "/"+a );
        ChartUtils.saveChartAsPNG( XYChart, xylineChart, width, height);

    }
}
