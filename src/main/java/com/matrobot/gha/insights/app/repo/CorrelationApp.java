package com.matrobot.gha.insights.app.repo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import com.matrobot.gha.insights.filter.ClassifyRepositoryFilter;
import com.matrobot.gha.insights.ml.Dataset;

public class CorrelationApp{

	Properties prop = new Properties();
	private Dataset dataset;
	
	public CorrelationApp(String firstPath, String secondPath, String thirdPath) throws IOException {
		
		prop.load(new FileInputStream("config.properties"));
		ClassifyRepositoryFilter filter = new ClassifyRepositoryFilter(
				prop.getProperty("data_path") + firstPath, 
				prop.getProperty("data_path") + secondPath, 
				prop.getProperty("data_path") + thirdPath);
		dataset = filter.getDataset();
    }

	
	public double calculateCorrelationCoff(int featureIndex){
		
		PearsonsCorrelation cor = new PearsonsCorrelation();
		return cor.correlation(dataset.getFeatureFromSamples(featureIndex), dataset.getOutputs());
	}

	
   public static void main(String args[]) throws IOException {
	   
        CorrelationApp app = new CorrelationApp("2012-8/", "2012-9/", "2012-10/");
        
        System.out.println("Correlation 1 =" + app.calculateCorrelationCoff(0));
        System.out.println("Correlation 2 =" + app.calculateCorrelationCoff(1));
    }
}