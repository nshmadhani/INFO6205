package edu.neu.coe.info6205.assignments.jfreechart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;

import java.io.File;
import java.io.IOException;

public class ChartHelper {







    public static void printChart(XYDataset dataset, String fileName, String title, String xLabel, String yLabel) throws IOException {
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                title ,
                xLabel ,
                yLabel,
                dataset,
                PlotOrientation.VERTICAL ,
                true , true , false);


        int width = 640;   /* Width of the image */
        int height = 480;  /* Height of the image */
        String dir = System.getProperty("user.dir");
        File XYChart = new File( dir + "/"+fileName);
        ChartUtils.saveChartAsPNG( XYChart, xylineChart, width, height);
    }
}
