package edu.neu.coe.info6205.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomOrder {



    public static int[]  randomOrder(int N) {
        Random r = new Random();
        int[] list = new int[N];
        for(int i=0;i<N;i++) {
            list[i] = r.nextInt();
        }
        return list;
    }
    public static int[] sortedOrder(int N) {
        int[] list = new int[N];
        for(int i=0;i<N;i++) {
            list[i] = i;
        }
        return list;
    }
    public static int[] patialOrder(int N) {
        int[] list = new int[N];

        int[] sorted = sortedOrder(N/2);
        int[] random = randomOrder(N/2);
        for(int i=0;i<N/2;i++) {
            list[i] = sorted[i];
        }
        for(int i=N/2;i<N;i++) {
            list[i] = random[i - (N/2)];
        }
        return list;

    }

}
