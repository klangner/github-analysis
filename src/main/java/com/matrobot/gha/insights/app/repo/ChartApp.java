package com.matrobot.gha.insights.app.repo;

import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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

import com.matrobot.gha.insights.filter.RegressionRepositoryFilter;
import com.matrobot.gha.insights.ml.Dataset;
import com.matrobot.gha.insights.ml.Sample;

/**
 * Checked correlation:
 * 	- current activity <-> activity rating (no correlation)  
 * 	- old rating <-> new rating (no correlation)  
 *
 */

@SuppressWarnings("serial")
public class ChartApp extends ApplicationFrame {

	Properties prop = new Properties();
	private Dataset dataset;
	private XYSeriesCollection chartDataset;
	double corrCoeff;
	
	public ChartApp(String firstPath, String secondPath, String thirdPath) throws IOException {
        super("Activity correlations");
		
		prop.load(new FileInputStream("config.properties"));
		RegressionRepositoryFilter filter = new RegressionRepositoryFilter(
				prop.getProperty("data_path") + firstPath, 
				prop.getProperty("data_path") + secondPath, 
				prop.getProperty("data_path") + thirdPath);
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
	   
//        ChartApp app = new ChartApp("2012-2/", 
//        		"2012-7/", "2012-8/");
//        System.out.println("Correlation 7-8 = " + app.corrCoeff);
//        app.showChart();
        
        show2011();
    }
   
   public static void show2011() throws IOException {
	   
       ChartApp app;
       
       app = new ChartApp("2012-2/", 
       		"2011-3/", "2011-4/");
       System.out.println("Correlation 3-4 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", 
       		"2011-4/", "2011-5/");
       System.out.println("Correlation 4-5 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", 
       		"2011-5/", "2011-6/");
       System.out.println("Correlation 5-6 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", 
       		"2011-6/", "2011-7/");
       System.out.println("Correlation 6-7 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", 
       		"2011-7/", "2011-8/");
       System.out.println("Correlation 7-8 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", 
       		"2011-8/", "2011-9/");
       System.out.println("Correlation 8-9 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", 
       		"2011-9/", "2011-10/");
       System.out.println("Correlation 9-10 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", 
       		"2011-10/", "2011-11/");
       System.out.println("Correlation 10-11 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", 
       		"2011-11/", "2011-12/");
       System.out.println("Correlation 11-12 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", 
       		"2011-12/", "2012-1/");
       System.out.println("Correlation 12-1 = " + app.corrCoeff);
   }
   
   public static void show2012() throws IOException {
	   
       ChartApp app;
       
       app = new ChartApp("2012-2/", "2012-1/", "2012-2/");
       System.out.println("Correlation 1-2 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", "2012-2/", "2012-3/");
       System.out.println("Correlation 2-3 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", "2012-3/", "2012-4/");
       System.out.println("Correlation 3-4 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", "2012-4/", "2012-5/");
       System.out.println("Correlation 4-5 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", "2012-5/", "2012-6/");
       System.out.println("Correlation 5-6 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", "2012-6/", "2012-7/");
       System.out.println("Correlation 6-7 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", "2012-7/", "2012-8/");
       System.out.println("Correlation 7-8 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", "2012-8/", "2012-9/");
       System.out.println("Correlation 8-9 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", "2012-9/", "2012-10/");
       System.out.println("Correlation 9-10 = " + app.corrCoeff);
       
       app = new ChartApp("2012-2/", "2012-10/", "2012-11/");
       System.out.println("Correlation 10-11 = " + app.corrCoeff);
   }
}