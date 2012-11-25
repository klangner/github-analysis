package com.matrobot.gha.app.repo;

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
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.category.ActivityRating;
import com.matrobot.gha.dataset.ActivityDataset;
import com.matrobot.gha.dataset.ActivityRecord;

/**
 * Checked correlation:
 * 	- current activity <-> activity rating (no correlation)  
 * 	- old rating <-> new rating (no correlation)  
 *
 */

@SuppressWarnings("serial")
public class CorrelationActivityApp extends ApplicationFrame {

	private HashMap<String, ActivityRecord> prevDataset;
	private HashMap<String, ActivityRecord> currentDataset;
	private HashMap<String, ActivityRecord> nextDataset;
	private int minActivity = Settings.MIN_ACTIVITY;
	
	
	public CorrelationActivityApp(String firstPath, String secondPath, String thirdPath) throws IOException {
        super("Activity correlations");
		
        loadDatasets(firstPath, secondPath, thirdPath);

		JPanel jpanel = createChartPanel();
        jpanel.setPreferredSize(new Dimension(800, 600));
        add(jpanel);
    }


	private void loadDatasets(String firstPath, String secondPath, String thirdPath) throws IOException {
		
		ActivityDataset datasetReader = new ActivityDataset();
		prevDataset = datasetReader.loadData(firstPath);
		currentDataset = datasetReader.loadData(secondPath);
		nextDataset = datasetReader.loadData(thirdPath);
	}

	
    private JPanel createChartPanel() {
    	
        JFreeChart jfreechart = ChartFactory.createScatterPlot(
            "Activity correlation", "Month 1", "Month 2", createChartDataset(),
            PlotOrientation.VERTICAL, true, true, false);
        
        return new ChartPanel(jfreechart);
    }

    
	private XYDataset createChartDataset(){
		
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        XYSeries series = new XYSeries("Activity");

		for(ActivityRecord record : currentDataset.values()){
			ActivityRecord nextRecord = nextDataset.get(record.repository); 
			if(record.activity > minActivity && nextRecord != null)
			{
				int category = ActivityRating.estimateCategory(record.activity, nextRecord.activity);
				int oldCategory = getOldActivityRating(record);
		        series.add(oldCategory, category);
			}
		}
        
        xySeriesCollection.addSeries(series);
        return xySeriesCollection;
	}
	
	
	public double calculateCorrelationCoff(){
		
		Vector<Double> x = new Vector<Double>();
		Vector<Double> y = new Vector<Double>();
		
		for(ActivityRecord record : currentDataset.values()){
			ActivityRecord nextRecord = nextDataset.get(record.repository); 
			if(record.activity > minActivity && nextRecord != null && 
					record.activity < 5000 && nextRecord.activity < 5000)
			{
				int category = ActivityRating.estimateCategory(record.activity, nextRecord.activity);
				x.add((double) getOldActivityRating(record));
				y.add((double) category);
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

	
	private int getOldActivityRating(ActivityRecord current) {
		int oldCategory;
		ActivityRecord prevRecord = prevDataset.get(current.repository);
		if(prevRecord == null){
			oldCategory = ActivityRating.UNKNOWN;
		}
		else{
			oldCategory = ActivityRating.estimateCategory(
					prevRecord.activity, current.activity);
		}
		return oldCategory;
	}

	
   public static void main(String args[]) throws IOException {
        CorrelationActivityApp app = new CorrelationActivityApp(
        		Settings.DATASET_PATH+"2012/1/", 
        		Settings.DATASET_PATH+"2012/2/",
        		Settings.DATASET_PATH+"2012/3/");
        
        System.out.println("Correlation=" + app.calculateCorrelationCoff());
        app.pack();
        RefineryUtilities.centerFrameOnScreen(app);
        app.setVisible(true);
    }
}