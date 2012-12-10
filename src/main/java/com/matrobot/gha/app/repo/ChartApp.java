package com.matrobot.gha.app.repo;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JPanel;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.filter.RegressionRepositoryFilter;
import com.matrobot.gha.ml.Dataset;
import com.matrobot.gha.ml.Sample;

/**
 * Checked correlation:
 * 	- current activity <-> activity rating (no correlation)  
 * 	- old rating <-> new rating (no correlation)  
 *
 */

@SuppressWarnings("serial")
public class ChartApp extends ApplicationFrame {

	private Dataset dataset;
	private XYSeriesCollection chartDataset;
	double corrCoeff;
	
	public ChartApp(String firstPath, String secondPath, String thirdPath) throws IOException {
        super("Activity correlations");
		
		RegressionRepositoryFilter filter = new RegressionRepositoryFilter(firstPath, secondPath, thirdPath);
		dataset = filter.getDataset();
		createChartDataset();
    }

	
	public void showChart(){

		ApplicationFrame frame = new ApplicationFrame("Correlation");
		
		JPanel jpanel = createChartPanel();
        jpanel.setPreferredSize(new Dimension(800, 600));
        frame.add(jpanel);
		frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);

	}

    private JPanel createChartPanel() {
    	
        JFreeChart jfreechart = ChartFactory.createScatterPlot(
            "Activity correlation", "Activity current", "Activity next", chartDataset,
            PlotOrientation.VERTICAL, true, true, false);
        
//        XYPlot plot = (XYPlot) jfreechart.getPlot();
//        plot.getRangeAxis().setRange(new Range(0, 10));
//        plot.getDomainAxis().setRange(new Range(0, 10));
        
        return new ChartPanel(jfreechart);
    }

    
	private void createChartDataset(){
		
		chartDataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Activity");

		for(Sample sample : dataset.getData()){

			if(sample.features[0] > 0 && sample.output > 0 /*&& Math.random() < 0.01*/){
//				double x = Math.log10(sample.features[0]);
//				double y = Math.log10(sample.output);
				double x = sample.features[0];
				double y = sample.output;
				if(x+y < 10000){
					series.add(x, y);
				}
				else{
					System.err.println("Suspicious repo: " + sample.name);
				}
			}
		}
        
		double[] a = new double[series.getItemCount()];
		double[] b = new double[series.getItemCount()];
		for(int i = 0; i < series.getItemCount(); i++){
			a[i] = series.getDataItem(i).getXValue();
			b[i] = series.getDataItem(i).getYValue();
		}
		
		corrCoeff = new PearsonsCorrelation().correlation(a, b);
		
		chartDataset.addSeries(series);
	}
	
	
   public static void main(String args[]) throws IOException {
	   
//        ChartApp app = new ChartApp(Settings.DATASET_PATH+"2012-2/", 
//        		Settings.DATASET_PATH+"2012-7/", Settings.DATASET_PATH+"2012-8/");
//        System.out.println("Correlation 7-8 = " + app.corrCoeff);
//        app.showChart();
        
        show2012();
    }
   
   public static void show2012() throws IOException {
	   
       ChartApp app;
       
       app = new ChartApp(Settings.DATASET_PATH+"2012-2/", 
       		Settings.DATASET_PATH+"2012-1/", Settings.DATASET_PATH+"2012-2/");
       System.out.println("Correlation 1-2 = " + app.corrCoeff);
       
       app = new ChartApp(Settings.DATASET_PATH+"2012-2/", 
       		Settings.DATASET_PATH+"2012-2/", Settings.DATASET_PATH+"2012-3/");
       System.out.println("Correlation 2-3 = " + app.corrCoeff);
       
       app = new ChartApp(Settings.DATASET_PATH+"2012-2/", 
       		Settings.DATASET_PATH+"2012-3/", Settings.DATASET_PATH+"2012-4/");
       System.out.println("Correlation 3-4 = " + app.corrCoeff);
       
       app = new ChartApp(Settings.DATASET_PATH+"2012-2/", 
       		Settings.DATASET_PATH+"2012-4/", Settings.DATASET_PATH+"2012-5/");
       System.out.println("Correlation 4-5 = " + app.corrCoeff);
       
       app = new ChartApp(Settings.DATASET_PATH+"2012-2/", 
       		Settings.DATASET_PATH+"2012-5/", Settings.DATASET_PATH+"2012-6/");
       System.out.println("Correlation 5-6 = " + app.corrCoeff);
       
       app = new ChartApp(Settings.DATASET_PATH+"2012-2/", 
       		Settings.DATASET_PATH+"2012-6/", Settings.DATASET_PATH+"2012-7/");
       System.out.println("Correlation 6-7 = " + app.corrCoeff);
       
       app = new ChartApp(Settings.DATASET_PATH+"2012-2/", 
       		Settings.DATASET_PATH+"2012-7/", Settings.DATASET_PATH+"2012-8/");
       System.out.println("Correlation 7-8 = " + app.corrCoeff);
       
       app = new ChartApp(Settings.DATASET_PATH+"2012-2/", 
       		Settings.DATASET_PATH+"2012-8/", Settings.DATASET_PATH+"2012-9/");
       System.out.println("Correlation 8-9 = " + app.corrCoeff);
       
       app = new ChartApp(Settings.DATASET_PATH+"2012-2/", 
       		Settings.DATASET_PATH+"2012-9/", Settings.DATASET_PATH+"2012-10/");
       System.out.println("Correlation 9-10 = " + app.corrCoeff);
       
       app = new ChartApp(Settings.DATASET_PATH+"2012-2/", 
       		Settings.DATASET_PATH+"2012-10/", Settings.DATASET_PATH+"2012-11/");
       System.out.println("Correlation 10-11 = " + app.corrCoeff);
   }
   
}