package com.matrobot.gha.insights.app.repo;

import java.io.IOException;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import com.matrobot.gha.archive.app.Settings;
import com.matrobot.gha.insights.filter.ClassifyRepositoryFilter;
import com.matrobot.gha.insights.ml.Dataset;

public class CorrelationApp{

	private Dataset dataset;
	
	public CorrelationApp(String firstPath, String secondPath, String thirdPath) throws IOException {
		
		ClassifyRepositoryFilter filter = new ClassifyRepositoryFilter(firstPath, secondPath, thirdPath);
		dataset = filter.getDataset();
    }

	
	public double calculateCorrelationCoff(int featureIndex){
		
		PearsonsCorrelation cor = new PearsonsCorrelation();
		return cor.correlation(dataset.getFeatureFromSamples(featureIndex), dataset.getOutputs());
	}

	
   public static void main(String args[]) throws IOException {
	   
        CorrelationApp app = new CorrelationApp(
        		Settings.DATASET_PATH+"2012-8/", 
        		Settings.DATASET_PATH+"2012-9/",
        		Settings.DATASET_PATH+"2012-10/");
        
        System.out.println("Correlation 1 =" + app.calculateCorrelationCoff(0));
        System.out.println("Correlation 2 =" + app.calculateCorrelationCoff(1));
    }
}