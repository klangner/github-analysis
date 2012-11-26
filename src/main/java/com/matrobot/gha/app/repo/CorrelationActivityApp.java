package com.matrobot.gha.app.repo;

import java.awt.Dimension;
import java.io.IOException;
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

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.dataset.repo.RepositoryDatasetList;
import com.matrobot.gha.dataset.repo.RepositoryRecord;

/**
 * Checked correlation:
 * 	- current activity <-> activity rating (no correlation)  
 * 	- old rating <-> new rating (no correlation)  
 *
 */

@SuppressWarnings("serial")
public class CorrelationActivityApp extends ApplicationFrame {

	private RepositoryDatasetList datasets = new RepositoryDatasetList();
	
	public CorrelationActivityApp(String firstPath, String secondPath, String thirdPath) throws IOException {
        super("Activity correlations");
		
		datasets.addFromFile(firstPath);
		datasets.addFromFile(secondPath);
		datasets.addFromFile(thirdPath);

		JPanel jpanel = createChartPanel();
        jpanel.setPreferredSize(new Dimension(800, 600));
        add(jpanel);
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

		for(RepositoryRecord record : datasets.getDataset(1).values()){
			RepositoryRecord nextRecord = datasets.findRepository(2, record.repository); 
	        series.add(record.pushEventCount, nextRecord.pushEventCount);
		}
        
        xySeriesCollection.addSeries(series);
        return xySeriesCollection;
	}
	
	
	public double calculateCorrelationCoff(){
		
		Vector<Double> x = new Vector<Double>();
		Vector<Double> y = new Vector<Double>();
		
		for(RepositoryRecord record : datasets.getDataset(1).values()){
			RepositoryRecord nextRecord = datasets.findRepository(2, record.repository); 
			x.add((double) record.pushEventCount);
			y.add((double) nextRecord.pushEventCount);
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
        		Settings.DATASET_PATH+"2011-10/", 
        		Settings.DATASET_PATH+"2011-11/",
        		Settings.DATASET_PATH+"2011-12/");
        
        System.out.println("Correlation=" + app.calculateCorrelationCoff());
        app.pack();
        RefineryUtilities.centerFrameOnScreen(app);
        app.setVisible(true);
    }
}