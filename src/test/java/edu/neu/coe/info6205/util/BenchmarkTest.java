/*
 * Copyright (c) 2017. Phasmid Software
 */

package edu.neu.coe.info6205.util;

import edu.neu.coe.info6205.sort.elementary.InsertionSort;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("ALL")
public class BenchmarkTest {

    int pre = 0;
    int run = 0;
    int post = 0;

    @Test // Slow
    public void testWaitPeriods() throws Exception {
        int nRuns = 2;
        int warmups = 2;
        Benchmark<Boolean> bm = new Benchmark_Timer<>(
                "testWaitPeriods", b -> {
            GoToSleep(100L, -1);
            return null;
        },
                b -> {
                    GoToSleep(200L, 0);
                },
                b -> {
                    GoToSleep(50L, 1);
                });
        double x = bm.run(false, nRuns);
        //assertEquals("post",nRuns, post);
        assertEquals("run",nRuns + warmups, run);
        assertEquals("pre",nRuns + warmups, pre);
        assertEquals(200, x, 10);
    }
    @Test // Slow
    /*
    *   TEST FUNCTION CREATED for Insertion Sort
    *
    *
    *
    * */
    public void testInsertionsort() throws Exception {
        int nRuns = 2;
        int warmups = 2;


        Benchmark<List<Integer>> bm = new Benchmark_Timer<>(
                "testWaitPeriods", null, b -> {
            InsertionSort<Integer> sort = new InsertionSort<>();
            Integer[] arr = (b.toArray(new Integer[b.size()]));
            sort.sort(arr,0,b.size());
        }, null);

        List<Integer> a = new ArrayList<>();
        for(int i=0;i<10000;i++) {
            a.add(10000 - i);
        }
        double x = bm.run(a, nRuns);
        //assertEquals("post",nRuns, post);

    }

    private void GoToSleep(long mSecs, int which) {
        try {
            Thread.sleep(mSecs);
            if (which == 0) run++;
            else if (which > 0) post++;
            else pre++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getWarmupRuns() {
        assertEquals(2, Benchmark_Timer.getWarmupRuns(0));
        assertEquals(2, Benchmark_Timer.getWarmupRuns(20));
        assertEquals(3, Benchmark_Timer.getWarmupRuns(30));
        assertEquals(10, Benchmark_Timer.getWarmupRuns(100));
        assertEquals(10, Benchmark_Timer.getWarmupRuns(1000));
    }
}