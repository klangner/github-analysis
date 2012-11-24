package com.matrobot.gha.app;

import java.awt.Dimension;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JPanel;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.matrobot.gha.dataset.ActivityDataset;
import com.matrobot.gha.dataset.ActivityRecord;

@SuppressWarnings("serial")
public class CorrelationActivityApp extends ApplicationFrame {

	private HashMap<String, ActivityRecord> firstDataset;
	private HashMap<String, ActivityRecord> secondDataset;
	private int minActivity = Settings.MIN_ACTIVITY;
	
	
	public CorrelationActivityApp(String firstPath, String secondPath) throws IOException {
        super("Activity correlations");
		
        loadDatasets(firstPath, secondPath, Settings.MIN_ACTIVITY);

		JPanel jpanel = createChartPanel();
        jpanel.setPreferredSize(new Dimension(800, 600));
        add(jpanel);
    }


	private void loadDatasets(String firstPath, String secondPath, int minActivity) throws IOException {
		
		ActivityDataset datasetReader = new ActivityDataset();
		firstDataset = datasetReader.loadData(firstPath);
		secondDataset = datasetReader.loadData(secondPath);
	}

	
    private JPanel createChartPanel() {
    	
        JFreeChart jfreechart = ChartFactory.createScatterPlot(
            "Activity correlation", "Month 1", "Month 2", createChartDataset(),
            PlotOrientation.VERTICAL, true, true, false);
        
        XYPlot plot = (XYPlot) jfreechart.getPlot();
        plot.getRangeAxis().setRange(new Range(0, 1000));
        plot.getDomainAxis().setRange(new Range(0, 1000));
        
        return new ChartPanel(jfreechart);
    }

    
	private XYDataset createChartDataset(){
		
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        XYSeries series = new XYSeries("Activity");

		for(ActivityRecord record : firstDataset.values()){
			ActivityRecord nextRecord = secondDataset.get(record.repository); 
			if(record.activity > minActivity && nextRecord != null && 
					record.activity < 5000 && nextRecord.activity < 5000)
			{
		        series.add(record.activity, nextRecord.activity);
			}
		}
        
        xySeriesCollection.addSeries(series);
        return xySeriesCollection;
	}
	
	
	public double calculateCovariance(){
		
		Vector<Double> x = new Vector<Double>();
		Vector<Double> y = new Vector<Double>();
		
		for(ActivityRecord record : firstDataset.values()){
			ActivityRecord nextRecord = secondDataset.get(record.repository); 
			if(record.activity > minActivity && nextRecord != null && 
					record.activity < 5000 && nextRecord.activity < 5000)
			{
				x.add((double) record.activity);
				y.add((double) nextRecord.activity);
			}
		}
		
		double[] a = new double[x.size()];
		for(int i = 0; i < x.size(); i++){
			a[i] = x.get(i);
		}
		double[] b = new double[y.size()];
		for(int i = 0; i < y.size(); i++){
			b[i] = y.get(i);
		}
		
		double covariance = new PearsonsCorrelation().correlation(a, b);
		return covariance;
	}

	
   public static void main(String args[]) throws IOException {
        CorrelationActivityApp app = new CorrelationActivityApp(
        		Settings.DATASET_PATH+"2012/9/", Settings.DATASET_PATH+"2012/10/");
        
        System.out.println("Correlation=" + app.calculateCovariance());
        app.pack();
        RefineryUtilities.centerFrameOnScreen(app);
        app.setVisible(true);
    }
}