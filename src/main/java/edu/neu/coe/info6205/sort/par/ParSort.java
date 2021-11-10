package edu.neu.coe.info6205.sort.par;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.Arrays;
import java.util.concurrent.*;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
public class ParSort {

    public int cutoff ;

    private static ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("Parsort-%d")
            .setDaemon(false)
            .build();

    public ExecutorService threadPool;
    private boolean switchIfNoThreads;
    public int nThreads;


    public ParSort(int cutoff, int nThreads, boolean switchIfNoThreeads) {
        this.cutoff = cutoff;

        if(nThreads == -1) {
            nThreads = ForkJoinPool.getCommonPoolParallelism();
        }
        this.nThreads = nThreads;
        this.threadPool =  Executors.newFixedThreadPool(nThreads, threadFactory);
        this.switchIfNoThreads = switchIfNoThreeads;

    }
    private boolean allThreadsUsed() {
        return activeCount() == nThreads;
    }
    private int activeCount() {
        if(threadPool instanceof ThreadPoolExecutor)
            return ((ThreadPoolExecutor) threadPool).getActiveCount();
        else return -1;
    }

    public void sort(int[] array, int from, int to) { ;

        if(to -from <= cutoff) {
            //System.out.println("Sorted Directly " + Thread.currentThread().getName());
            Arrays.sort(array, from, to);
        }
        else if(switchIfNoThreads && allThreadsUsed())
            Arrays.sort(array, from, to);
        else {
            int mid = from + (to - from ) / 2;
            CompletableFuture<int[]> left =  parsort(array, from, mid);
            CompletableFuture<int[]> right = parsort(array, mid, to);
            CompletableFuture<int[]> combinedFuture = left.thenCombine(right,(l,r)-> {
                //System.out.println("Mergig from " + ( from + " " + mid + " ") + (mid + " " + to + " "));
                int[] result = new int[to - from];
                int i = from, j = mid;
                for(int k=0;k<result.length;k++) {

                    if(i >= mid) {
                        result[k] = array[j++];
                    } else if(j >= to) {
                        result[k] = array[i++];
                    } else if(array[j] < array[i]) {
                        result[k] = array[j++];
                    } else result[k] = array[i++];
                }
                System.arraycopy(result, 0, array, from, result.length);
                return array;
            });
            combinedFuture.join();
        }
        //System.out.println("Ended " + Thread.currentThread().getName() + " " + Thread.currentThread().getState());
    }

    private CompletableFuture<int[]> parsort(int[] array, int from, int to) {
        //Divide the work into number of threads and then return those threads
        return CompletableFuture.supplyAsync(() -> {
            //System.out.println("Started " + Thread.currentThread().getName() + " for "+(to-from));
            sort(array,from, to);
            //threadPool.shutdown();
            return array;
        },threadPool);

    }

    public void init() {
        threadPool.shutdown();
        threadPool = Executors.newFixedThreadPool(nThreads, threadFactory);
    }
}