package com.matrobot.gha.app.repo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.matrobot.gha.app.Settings;
import com.matrobot.gha.dataset.repo.RepositoryDatasetList;
import com.matrobot.gha.dataset.repo.RepositoryRecord;
import com.matrobot.gha.regression.IRegression;
import com.matrobot.gha.regression.MultivariableRegression;

public class ForecastApp {

	private static final int MIN_ACTIVITY = 10;
	class Feature{
		double currentActivity;
		double prevActivity;
	}
	
	private RepositoryDatasetList datasets = new RepositoryDatasetList();
	private HashMap<String, Feature> features = new HashMap<String, Feature>();

	
	
	protected ForecastApp(String firstPath, String secondPath) throws IOException{
		
		datasets.addFromFile(firstPath);
		datasets.addFromFile(secondPath);
		
		prepareFeatures();
	}
	
	/**
	 * Training data for single feature vector
	 * @return
	 */
	private void prepareFeatures() {
	
		for(RepositoryRecord record : datasets.getDataset(1).values()){
			RepositoryRecord prevRecord = datasets.findRepository(0, record.repository); 
			if(prevRecord != null && record.pushEventCount > MIN_ACTIVITY){
				Feature feature = new Feature();
				feature.currentActivity = record.pushEventCount;
				feature.prevActivity = (prevRecord!=null)?prevRecord.pushEventCount:0; 
			}
		}
		
		scaleFeatures();
	}

	
	private void scaleFeatures() {
	
		DescriptiveStatistics statsCurrent = new DescriptiveStatistics();
		DescriptiveStatistics statsPrev = new DescriptiveStatistics();

		for(Feature feature : features.values()){
			statsCurrent.addValue(feature.currentActivity);
			statsPrev.addValue(feature.prevActivity);
		}
			
		for(Feature feature : features.values()){
			feature.currentActivity =  
					(feature.currentActivity-statsCurrent.getMean())/statsCurrent.getStandardDeviation();
			feature.prevActivity =  
					(feature.prevActivity-statsPrev.getMean())/statsPrev.getStandardDeviation();
		}
	}

	protected void saveAsCSV(String filename, IRegression model) {

		for(Feature feature : features.values()){
			
			double[] input = new double[2];
			input[0] = feature.currentActivity;
			input[1] = feature.prevActivity;
			double forecast = model.predict(input);
			System.out.println(feature.currentActivity + " -> " + forecast);
		}
		System.out.println("Save to: " + filename);
	}

	public static void main(String[] args) throws IOException {

		System.out.println("Load datasets");
		ForecastApp app = new ForecastApp(
				Settings.DATASET_PATH+"2012-9/", 
				Settings.DATASET_PATH+"2012-10/");

		
//		MultivariableRegression regression = MultivariableRegression.train(app.testSet.inputs, app.testSet.outputs);
		System.out.println("Save forecast: ");
//		app.saveAsCSV(Settings.DATASET_PATH+"forecast-2012-11.csv", regression);
		System.out.println();
	}
	
}
