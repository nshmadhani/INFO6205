package edu.neu.coe.info6205.sort.par;

import com.sun.javafx.binding.StringFormatter;
import edu.neu.coe.info6205.assignments.jfreechart.ChartHelper;
import edu.neu.coe.info6205.util.Benchmark;
import edu.neu.coe.info6205.util.Benchmark_Timer;
import edu.neu.coe.info6205.util.CustomOrder;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
public class Main {

    private static  int THREADS = 30;
    public static String FOLDER = "./Assignments/5/";





    public static void main(String[] args) throws IOException {
        int N = 1000000;
        /*
        * TODO Test for different cuttOff with nice Thread value
        * TODO Test for nThread value by keeping CutOff at 1
        * TODO Test for a combination of both somehow
        * */
        THREADS = 30;
        //runCutOffCases();
        //runThreadCases();
        combinationRun();


    }
    private static void combinationRun() throws IOException {

        int[] N_VALUES = {250000,500000, 1000000};
        int[] N_THREADS = {30,50,100,200};
        for(int i=0;i<N_VALUES.length;i++) {
            System.out.println("=======Testing for "+N_VALUES[i]+"=========");
            for(int threads: N_THREADS) {
                THREADS  = threads;
                String title = "Incremental CutOff - " +N_VALUES[i] + " " + threads;
                runCutOffCase(N_VALUES[i],
                        FOLDER +title+".jpg",title,prev -> (int) (prev * 1.1));
                title = "Doubling CutOff - " + N_VALUES[i] + " " + threads;
                runCutOffCase(N_VALUES[i],
                        FOLDER +title+".jpg",title,prev -> (int) (prev * 2));
            }
        }
    }
    private static void runCutOffCases() throws IOException {

        int[] N_VALUES = {250000,500000, 1000000};
        int[] N_THREADS = {30, 30, 50, 50};

        for(int i=0;i<N_VALUES.length;i++) {
            System.out.println("=======Testing for "+N_VALUES[i]+"=========");
            String title = "Incremental CutOff - " +N_VALUES[i];
            runCutOffCase(N_VALUES[i],
                    FOLDER +title+".jpg",title,prev -> (int) (prev * 1.1));

            System.out.println("=========Incremental Done=========");
            title = "Doubling CutOff - " +N_VALUES[i];
            runCutOffCase(N_VALUES[i],
                    FOLDER +title+".jpg",title,prev -> (int) (prev * 2));
        }
    }

    private static void runCutOffCase(int N, String fileName, String title, Scheme scheme) throws IOException {
        int[] randomOrder = CustomOrder.randomOrder(N);
        int[] sortedOrer = CustomOrder.sortedOrder(N);
        int[] partialOrder = CustomOrder.patialOrder(N);


        //CutOff Values
        XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(case1ForCutOffSchemes(randomOrder,"Random Order",scheme));
        dataset.addSeries(case1ForCutOffSchemes(sortedOrer,"Sorted Order", scheme));
        dataset.addSeries(case1ForCutOffSchemes(partialOrder,"Partial Order", scheme));

        ChartHelper.printChart(dataset,fileName,
                title, "CutOff", "Time in milliseconds");
    }

    public static XYSeries case1ForCutOffSchemes(int[] arrayToSort, String seriesName, Scheme scheme) {

        XYSeries series = new XYSeries(seriesName);
        int cutOff = 50000;
        int N = arrayToSort.length;
        ParSort sort = new ParSort(cutOff, THREADS, false);
        Benchmark<int[]> bm = new Benchmark_Timer<>(
                "Test for CutOFf", null, b -> {

            int[] order =  new int[arrayToSort.length];
            System.arraycopy(arrayToSort,0, order, 0, N);
            sort.init();
            sort.sort(order, 0, order.length);
            sort.threadPool.shutdown();
        }, null);

        while (cutOff <= N) {
            double time = bm.run(null,10);
            System.out.println(time + " " + cutOff);
            series.add(cutOff,time);
            cutOff = scheme.next(cutOff);
            sort.cutoff = cutOff;
        }

        return series;


    }

    private static void runThreadCases() throws IOException {

        int[] N_VALUES = {250000,500000, 1000000};

        for(int i=0;i<N_VALUES.length;i++) {
            System.out.println("=======Testing for "+N_VALUES[i]+"=========");

            System.out.println("=========Incremental Done=========");
            String title = "Doubling Threads - " +N_VALUES[i];
            runThreadCase(N_VALUES[i],
                    FOLDER +title+".jpg",title,prev -> (int) (prev * 2));
        }
    }

    private static void runThreadCase(int N, String fileName, String title, Scheme scheme) throws IOException {
        int[] randomOrder = CustomOrder.randomOrder(N);
        int[] sortedOrer = CustomOrder.sortedOrder(N);
        int[] partialOrder = CustomOrder.patialOrder(N);


        //CutOff Values
        XYSeriesCollection dataset = new XYSeriesCollection();

        dataset.addSeries(case1ForThreadSchemes(randomOrder,"Random Order",scheme));
        dataset.addSeries(case1ForThreadSchemes(sortedOrer,"Sorted Order", scheme));
        dataset.addSeries(case1ForThreadSchemes(partialOrder,"Partial Order", scheme));

        ChartHelper.printChart(dataset,fileName,
                title, "Number of Threads", "Time in milliseconds");
    }

    public static XYSeries case1ForThreadSchemes(int[] arrayToSort, String seriesName, Scheme scheme) {

        XYSeries series = new XYSeries(seriesName);
        int threads = 2;
        int N = arrayToSort.length;
        ParSort sort = new ParSort(50000, threads, true);
        Benchmark<int[]> bm = new Benchmark_Timer<>(
                "Test for Cutoff", null, b -> {
            int[] order =  new int[arrayToSort.length];
            System.arraycopy(arrayToSort,0, order, 0, N);
            sort.init();
            sort.sort(order, 0, order.length);
            sort.threadPool.shutdown();
        }, null);

        while (threads <= 8192) {
            sort.nThreads = threads;
            double time = bm.run(null,10);
            System.out.println(time + " " + threads);
            series.add(threads,time);
            threads = scheme.next(threads);
        }
        return series;
    }




    @FunctionalInterface
    interface Scheme {
        int next(int prev);
    }



}
