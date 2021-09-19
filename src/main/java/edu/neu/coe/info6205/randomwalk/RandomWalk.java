/*
 * Copyright (c) 2017. Phasmid Software
 */

package edu.neu.coe.info6205.randomwalk;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.io.File;


import java.io.IOException;
import java.util.Random;
public class RandomWalk {

    private int x = 0;
    private int y = 0;

    private final Random random = new Random();

    /**
     * Private method to move the current position, that's to say the drunkard moves
     *
     * @param dx the distance he moves in the x direction
     * @param dy the distance he moves in the y direction
     */
    private void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    /**
     * Perform a random walk of m steps
     *
     * @param m the number of steps the drunkard takes
     */
    private void randomWalk(int m) {
        for(int i=0;i<m;i++) {
            randomMove();
        }
    }

    /**
     * Private method to generate a random move according to the rules of the situation.
     * That's to say, moves can be (+-1, 0) or (0, +-1).
     */
    private void randomMove() {
        //Determines if moving in x or y direcion
        //True means in x, else in y
        boolean ns = random.nextBoolean();
        int step = random.nextBoolean() ? 1 : -1;
        move(ns ? step : 0, ns ? 0 : step);
    }

    /**
     * Method to compute the distance from the origin (the lamp-post where the drunkard starts) to his current position.
     *
     * @return the (Euclidean) distance from the origin to the current position.
     */
    public double distance() {
        return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }

    /**
     * Perform multiple random walk experiments, returning the mean distance.
     *
     * @param m the number of steps for each experiment
     * @param n the number of experiments to run
     * @return the mean distance
     */
    public static double randomWalkMulti(int m, int n) {
        double totalDistance = 0;
        for (int i = 0; i < n; i++) {
            RandomWalk walk = new RandomWalk();
            walk.randomWalk(m);
            totalDistance +=  walk.distance();
        }
        return totalDistance / n;
    }


    public static void main(String[] args) throws IOException {
        //Number of moves to test for
        int N = 6;
        //Number of experiments
        int experiments = 100000;

        Random r = new Random(System.currentTimeMillis());
        int[] randomSteps = new int[N];
        int stepIndex = 0;
        double[] distances = new double[N];
        for(int i=0;i<N;i++) {
            int step = r.nextInt(3000);
            randomSteps[stepIndex] = step;
            double distance = 0;
            for(int j=0;j<experiments;j++) {
                RandomWalk walk = new RandomWalk();
                walk.randomWalk(step);
                distance += walk.distance();
            }
            distances[stepIndex++] = distance/experiments;
            System.out.println(step + " " + distances[stepIndex-1]);
        }

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                "Relation of d and N" ,
                "N - Number of Steps" ,
                "d - Average Distance ove 100 experiments" ,
                createDataset(randomSteps,distances) ,
                PlotOrientation.VERTICAL ,
                true , true , false);


        int width = 640;   /* Width of the image */
        int height = 480;  /* Height of the image */

        String dir = System.getProperty("user.dir");
        File XYChart = new File( dir + "/XYLineChart.jpeg" );
        ChartUtils.saveChartAsPNG( XYChart, xylineChart, width, height);

    }

    private static XYDataset createDataset(int[] randomSteps, double[] distances) {

        final XYSeries simulated = new XYSeries( "simulated" );
        for(int i=0;i<randomSteps.length;i++) {
            simulated.add(randomSteps[i],distances[i]);
        }
        final XYSeries rootOfN = new XYSeries( "sqrt(N)" );
        for(int i=0;i<randomSteps.length;i++) {
            rootOfN.add(randomSteps[i],Math.sqrt(randomSteps[i]));
        }

        final XYSeries logN = new XYSeries( "ln N" );
        for(int i=0;i<randomSteps.length;i++) {
            logN.add(randomSteps[i],Math.log(randomSteps[i]) );
        }
        final XYSeries cbrtOfN = new XYSeries( "cubeRoot Of N");
        for(int i=0;i<randomSteps.length;i++) {
            cbrtOfN.add(randomSteps[i],Math.cbrt(randomSteps[i]) );
        }

        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries(simulated);
        dataset.addSeries(rootOfN);
        dataset.addSeries(cbrtOfN);
        dataset.addSeries(logN);




        return dataset;
    }

}
